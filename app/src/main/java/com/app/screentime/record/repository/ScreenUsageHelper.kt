package com.app.screentime.record.repository

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.app.usage.UsageStatsManager.INTERVAL_DAILY
import android.content.Context
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

        val openedApps = mutableMapOf<String, Long>()
        val result = mutableListOf<AppEvent>()

        val events = usageStatsManager.queryEvents(start, end)
        val event = UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)

            val pkg = event.packageName ?: continue

            // App name (optional, you already had this)
            val name = getAppName(pkg)

            // We only track user-launchable apps
            if (!isUserApp(pkg)) {
                continue
            }

            when (event.eventType) {

                UsageEvents.Event.MOVE_TO_FOREGROUND -> {

                    val now = event.timeStamp

                    // If this app is already open → continue same session
                    if (openedApps.containsKey(pkg)) {
                        continue
                    }

                    // Close any other currently opened user apps
                    val toClose = openedApps.filter { (otherPkg, _) ->
                        otherPkg != pkg && isUserApp(otherPkg)
                    }

                    for ((otherPkg, startTime) in toClose) {
                        val otherName = getAppName(otherPkg)
                        result.add(
                            AppEvent(
                                event = "MOVE_TO_FOREGROUND",
                                appName = otherName,
                                packageName = otherPkg,
                                timestamp = now,
                                duration = now - startTime
                            )
                        )
                        openedApps.remove(otherPkg)
                    }

                    // Start new session
                    openedApps[pkg] = now
                    result.add(AppEvent("MOVE_TO_FOREGROUND", name, pkg, now))
                }


                UsageEvents.Event.MOVE_TO_BACKGROUND -> {

                    val startTime = openedApps[pkg] ?: continue

                    result.add(
                        AppEvent(
                            event = "MOVE_TO_BACKGROUND",
                            appName = name,
                            packageName = pkg,
                            timestamp = event.timeStamp,
                            duration = event.timeStamp - startTime
                        )
                    )

                    openedApps.remove(pkg)
                }
            }
        }

        return result.sortedBy { it.timestamp }
    }


    fun isSystemApp(pkg: String): Boolean {
        val appInfo = context.packageManager.getApplicationInfo(pkg, 0)
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
    }

    fun isUserApp(pkg: String): Boolean {
        val pm = context.packageManager

        // Check if app is launchable (= has launcher activity)
        pm.getLaunchIntentForPackage(pkg) ?: return false   // Not a user-facing app

        // Exclude launcher itself
        val launcherPkgs = listOf(
            "com.android.launcher",
            "com.google.android.apps.nexuslauncher",
            "com.miui.home",
            "com.oneplus.launcher",
            "com.sec.android.app.launcher",
        )
        if (pkg in launcherPkgs) return false

        return true
    }


}
