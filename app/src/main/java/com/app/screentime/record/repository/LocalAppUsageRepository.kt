package com.app.screentime.record.repository

import android.app.usage.NetworkStats
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport
import com.app.screentime.utils.DateUtils
import java.text.NumberFormat

/**
 * Helper class to access and manage app usage statistics.
 * Requires USAGE_STATS permission to function properly.
 */
class LocalAppUsageRepository(
    private val context: Context,
    private val screenUsageHelper: ScreenUsageHelper,
    private val networkUsageHelper: NetworkUsageHelper
) {
    companion object {
        const val REMOVED_PACKAGE: String = "com.android.removed"
        const val TETHERING_PACKAGE: String = "com.android.tethering"
    }

    private val packageManager = context.packageManager

    fun getAppsUsageForInterval(
        startMsEpoch: Long?,
        endMsEpoch: Long?,
    ): List<AppUsage> {
        require(startMsEpoch != null && endMsEpoch != null) {
            "Either start or end time is null"
        }

        Log.d(
            "dailyreport", "Fetching day: $startMsEpoch → $endMsEpoch"
        )
        val screenUsageMap = screenUsageHelper.fetchUsageForInterval(startMsEpoch, endMsEpoch)
        val mobileDataUsage =
            networkUsageHelper.fetchMobileUsageForInterval(startMsEpoch, endMsEpoch)
        val wifiDataUsage = networkUsageHelper.fetchWifiUsageForInterval(startMsEpoch, endMsEpoch)

        val launchableApps = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0
        )

        val appsUsageList = launchableApps.asSequence().mapNotNull { info ->
            val pkg = info.activityInfo.packageName
            val name =
                info.activityInfo.applicationInfo?.loadLabel(packageManager)?.toString() ?: pkg
            val uid = info.activityInfo.applicationInfo.uid

            val appStats = screenUsageMap[pkg]
            val screenTime = appStats?.totalTimeMs ?: 0L
            val launchCount = appStats?.launchCount ?: 0
            val mobileData = mobileDataUsage[uid] ?: 0L
            val wifiData = wifiDataUsage[uid] ?: 0L

            if (screenTime <= 0L && mobileData <= 0L && wifiData <= 0L) null
            else {
                AppUsage(
                    packageName = pkg,
                    appName = name,
                    appScreenTime = screenTime,
                    mobileDataUsage = mobileData,
                    wifiDataUsage = wifiData,
                    launchCount = launchCount
                ).apply {
                    applicationInfo = info.activityInfo.applicationInfo
                    displayFormatScreenTime = formatDuration(this.appScreenTime)
                }
            }
        }.toMutableList()

        // Add special system entries
        addTetheringAndRemovedUsage(
            appsUsageList, mobileDataUsage, wifiDataUsage
        )

        return appsUsageList.distinctBy { it.packageName }
    }

    /**
     * Adds tethering and removed apps usage.
     */
    private fun addTetheringAndRemovedUsage(
        list: MutableList<AppUsage>, mobileUsage: Map<Int, Long>, wifiUsage: Map<Int, Long>
    ) {
        list += AppUsage(
            packageName = TETHERING_PACKAGE,
            appName = "Tethering",
            appScreenTime = 0L,
            mobileDataUsage = mobileUsage[NetworkStats.Bucket.UID_TETHERING] ?: 0L,
            wifiDataUsage = wifiUsage[NetworkStats.Bucket.UID_TETHERING] ?: 0L
        )

        list += AppUsage(
            packageName = REMOVED_PACKAGE,
            appName = "Removed Apps",
            appScreenTime = 0L,
            mobileDataUsage = mobileUsage[NetworkStats.Bucket.UID_REMOVED] ?: 0L,
            wifiDataUsage = wifiUsage[NetworkStats.Bucket.UID_REMOVED] ?: 0L
        )
    }

    /**
     * Sort helpers
     */
    fun List<AppUsage>.sortedByScreenTime(): List<AppUsage> =
        asSequence().sortedByDescending { it.appScreenTime }.toList()

    fun List<AppUsage>.sortedByDataUsage(): List<AppUsage> =
        asSequence().sortedByDescending { it.mobileDataUsage + it.wifiDataUsage }.toList()

    suspend fun fetchAppUsageTodayTillNow(): List<AppUsage> {
        val startOfToday = DateUtils.startOfToday()
        val start = startOfToday.millis
        val end = DateUtils.nowMillis()
        return getAppsUsageForInterval(start, end)
    }


    fun getOneWeekReport(): List<WeeklyDataReport> {
        val weeklyReports = mutableListOf<WeeklyDataReport>()

        // Start 6 days ago from today
        val startDate = DateUtils.minusDays(DateUtils.startOfToday(), 6)
        val todayStart = DateUtils.startOfToday()
        val now = DateUtils.nowMillis()

        // Loop 7 days
        for (i in 0 until 7) {
            val currentDay = DateUtils.addDays(startDate, i)
            val startOfDay = DateUtils.startOfDay(currentDay)
            val startOfDayMillis = startOfDay.millis

            val nextDay = DateUtils.addDays(startDate, i + 1)
            val endOfDayStart = DateUtils.startOfDay(nextDay)
            var endOfDayMillis = endOfDayStart.millis

            // If this day is today, use current time
            if (startOfDayMillis >= todayStart.millis) {
                endOfDayMillis = now
            }

            Log.d(
                "WeeklyReport", "Fetching day ${i + 1}: $startOfDayMillis → $endOfDayMillis"
            )

            val dailyUsage = getAppsUsageForInterval(startOfDayMillis, endOfDayMillis)
            val totalScreenTime = dailyUsage.sumOf { it.appScreenTime }
            val totalNotificationCount = dailyUsage.sumOf {
                if (it.notificationCount > 0) it.notificationCount else 0
            }
            val report = WeeklyDataReport(
                dayName = DateUtils.format(startOfDay, "EEEE"),
                date = DateUtils.format(startOfDay, "dd/MM/yyyy"),
                appUsage = dailyUsage,
                totalScreenTime = totalScreenTime,
                displayScreenTime = formatDuration(totalScreenTime),
                totalWifiDataUsage = dailyUsage.sumOf { it.wifiDataUsage },
                totalMobileDataUsage = dailyUsage.sumOf { it.mobileDataUsage },
                displayWifiDataUsage = dailyUsage.sumOf { it.wifiDataUsage }.toReadableDataSize(),
                displayMobileDataUsage = dailyUsage.sumOf { it.mobileDataUsage }
                    .toReadableDataSize(),
                displayTotalDataUsage = dailyUsage.sumOf { it.wifiDataUsage + it.mobileDataUsage }
                    .toReadableDataSize(),
                totalNotificationCount = totalNotificationCount
            )

            weeklyReports.add(report)
        }

        return weeklyReports
    }

    fun collectEventsForSync(
        startMsEpoch: Long,
        endMsEpoch: Long,
    ) = screenUsageHelper.collectEvents(startMsEpoch, endMsEpoch)

}

fun Long?.toReadableDataSize(): String? {
    if (this == null) return null
    val formatter = NumberFormat.getNumberInstance() // locale-aware

    val kb = 1024L
    val mb = kb * 1024
    val gb = mb * 1024
    val tb = gb * 1024

    return when {
        this < kb -> "$this B"

        this < mb -> formatter.format(this.toDouble() / kb) + " KB"
        this < gb -> formatter.format(this.toDouble() / mb) + " MB"
        this < tb -> formatter.format(this.toDouble() / gb) + " GB"
        else -> formatter.format(this.toDouble() / tb) + " TB"
    }
}

fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> {
            if (minutes > 0) "$hours h $minutes m" else "$hours hr"
        }

        minutes > 0 -> "$minutes min"
        else -> "$seconds sec"
    }
}