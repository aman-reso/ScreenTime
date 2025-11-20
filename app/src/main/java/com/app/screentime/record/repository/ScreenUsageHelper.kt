package com.app.screentime.record.repository

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.app.usage.UsageStatsManager.INTERVAL_DAILY
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import java.util.Calendar
import javax.inject.Inject

@Serializable
data class AppUsageStats(
    val totalTimeMs: Long,
    val launchCount: Int,
    val pkgName: String
)

@Serializable
data class AppEvent(
    val event: String,        // OPEN / CLOSE
    val appName: String,      // Example: WhatsApp
    val packageName: String,  // com.whatsapp
    val timestamp: Long,
    val duration: Long? = null
)


/**
 * ScreenUsageHelper provides utility methods for gathering and calculating screen usage statistics
 * for Android applications. It interacts with the UsageStatsManager to query and process usage data.
 */
class ScreenUsageHelper constructor(private val context: Context) {
    val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    /**
     * Fetches screen usage statistics for a specified time interval using usage events.
     * If the target package is not null then this method will fetch usage for that app only
     * otherwise for all device apps.
     *
     * @param usageStatsManager The UsageStatsManager used to query screen usage data.
     * @param start             The start time of the interval in milliseconds.
     * @param end               The end time of the interval in milliseconds.
     * @return A map with package names as keys and their corresponding screen usage time in seconds as values.
     */
    fun fetchUsageForInterval(
        start: Long,
        end: Long
    ): Map<String, AppUsageStats> {
        val usageMap = mutableMapOf<String, Long>()
        val launchCountMap = mutableMapOf<String, Int>()
        val lastResumedEvents = mutableMapOf<String, UsageEvents.Event>()

        runCatching {
            val usageEvents = usageStatsManager.queryEvents(start, end)

            while (usageEvents.hasNextEvent()) {
                val event = UsageEvents.Event()
                usageEvents.getNextEvent(event)

                val packageName = event.packageName
                val eventKey = packageName + event.className

                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        if (!lastResumedEvents.containsKey(eventKey)) {
                            launchCountMap[packageName] =
                                launchCountMap.getOrDefault(packageName, 0) + 1
                        }
                        lastResumedEvents[eventKey] = event
                    }

                    UsageEvents.Event.ACTIVITY_PAUSED, UsageEvents.Event.ACTIVITY_STOPPED -> {
                        lastResumedEvents.remove(eventKey)?.let { lastResumedEvent ->
                            if (event.timeStamp > start) {
                                val resumeTimeStamp = maxOf(lastResumedEvent.timeStamp, start)
                                usageMap[packageName] = usageMap.getOrDefault(packageName, 0L) +
                                        (event.timeStamp - resumeTimeStamp)
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        // Handle apps that were started but not stopped before 'end'
        lastResumedEvents.values.maxByOrNull { it.timeStamp }?.let { event ->
            val packageName = event.packageName
            usageMap[packageName] = usageMap.getOrDefault(packageName, 0L) + (end - event.timeStamp)
        }

        // Combine usage duration and launch count
        return usageMap.mapValues { (pkg, duration) ->
            AppUsageStats(
                pkgName = pkg,
                totalTimeMs = duration,
                launchCount = launchCountMap.getOrDefault(pkg, 0)
            )
        }.filterValues { it.totalTimeMs > 0L }
    }


    private fun getAppName(pkg: String): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(pkg, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            pkg // fallback
        }
    }


    /**
     * Fetches the screen usage time of a all installed application for the current day until now using usage events.
     *
     * @param usageStatsManager The UsageStatsManager used to query screen usage data.
     * @return The total screen usage time of the specified application in seconds.
     */

    fun collectEvents(start: Long, end: Long): List<AppEvent> {

        val eventsList = mutableListOf<AppEvent>()
        val events = usageStatsManager.queryEvents(start, end)
        val event = UsageEvents.Event()
        val activeApps = mutableMapOf<String, Long>()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)

            val pkg = event.packageName ?: continue
            if (isLauncherApp(packageName = pkg)) {
                continue
            }
            if (!hasLaunchableActivity(packageName = pkg)) {
                continue
            }
            val name = getAppName(pkg)

            when (event.eventType) {

                UsageEvents.Event.ACTIVITY_RESUMED -> {
//                    eventsList.add(
//                        AppEvent(
//                            event = "MOVE_TO_FOREGROUND",
//                            appName = name,
//                            packageName = pkg,
//                            timestamp = event.timeStamp
//                        )
//                    )
                    activeApps[pkg] = event.timeStamp
                }

                UsageEvents.Event.ACTIVITY_PAUSED, UsageEvents.Event.ACTIVITY_STOPPED -> {
                    val startTime = activeApps[pkg] ?: continue
                    val endTime = event.timeStamp
                    eventsList.add(
                        AppEvent(
                            event = "MOVE_TO_BACKGROUND",
                            appName = name,
                            packageName = pkg,
                            timestamp = event.timeStamp,
                            duration = endTime - startTime
                        )
                    )
                    activeApps.remove(pkg)
                }
            }
        }

        return eventsList
    }

    private fun isLauncherApp(packageName: String): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfo = context.packageManager.resolveActivity(intent, 0)
        return resolveInfo?.activityInfo?.packageName == packageName
    }

    private fun hasLaunchableActivity(packageName: String): Boolean {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage(packageName)
        return intent != null
    }

}
