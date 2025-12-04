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

//    fun collectEvents(start: Long, end: Long): List<AppEvent> {
//        val events = usageStatsManager.queryEvents(start, end)
//
//        val finalizedEvents = mutableListOf<AppEvent>()
//
//        data class Session(
//            val packageName: String,
//            val appName: String,
//            val startTime: Long,
//            var accumulatedDuration: Long = 0,
//            var lastResumeTime: Long = 0,
//            var lastEventTime: Long = 0,
//            var isOpen: Boolean = true
//        )
//
//        var currentSession: Session? = null
//        val event = UsageEvents.Event()
//
//        while (events.hasNextEvent()) {
//            events.getNextEvent(event)
//            val pkg = event.packageName ?: continue
//
//            // Filter out launcher and non-launchable apps
//            if (isLauncherApp(pkg) || !hasLaunchableActivity(pkg)) {
//                continue
//            }
//
//            when (event.eventType) {
//                UsageEvents.Event.ACTIVITY_RESUMED -> {
//                    if (currentSession == null) {
//                        currentSession = Session(
//                            packageName = pkg,
//                            appName = getAppName(pkg),
//                            startTime = event.timeStamp,
//                            lastResumeTime = event.timeStamp,
//                            lastEventTime = event.timeStamp
//                        )
//                    } else if (currentSession.packageName == pkg) {
//                        currentSession.lastResumeTime = event.timeStamp
//                        currentSession.isOpen = true
//                        currentSession.lastEventTime = event.timeStamp
//                    } else {
//                        currentSession.let { session ->
//                            if (session.lastEventTime > start) {
//                                finalizedEvents.add(
//                                    AppEvent(
//                                        event = "MOVE_TO_FOREGROUND",
//                                        appName = session.appName,
//                                        packageName = session.packageName,
//                                        timestamp = session.startTime,
//                                        duration = session.accumulatedDuration
//                                    )
//                                )
//                            }
//                        }
//                        currentSession = Session(
//                            packageName = pkg,
//                            appName = getAppName(pkg),
//                            startTime = event.timeStamp,
//                            lastResumeTime = event.timeStamp,
//                            lastEventTime = event.timeStamp
//                        )
//                    }
//                }
//
//                UsageEvents.Event.ACTIVITY_PAUSED, UsageEvents.Event.ACTIVITY_STOPPED -> {
//                    if (currentSession?.packageName == pkg && currentSession.isOpen) {
//                        val duration = event.timeStamp - currentSession.lastResumeTime
//                        currentSession.accumulatedDuration =
//                            currentSession.accumulatedDuration + duration
//                        currentSession.isOpen = false
//                        currentSession.lastEventTime = event.timeStamp
//                    }
//                }
//            }
//        }
//
//        currentSession?.let { session ->
//            if (!session.isOpen && session.lastEventTime > start) {
//                finalizedEvents.add(
//                    AppEvent(
//                        event = "APP_USAGE",
//                        appName = session.appName,
//                        packageName = session.packageName,
//                        timestamp = session.startTime,
//                        duration = session.accumulatedDuration
//                    )
//                )
//            }
//        }
//
//        return finalizedEvents
//    }

    data class Session(
        val packageName: String,
        var startTime: Long,
        var lastResume: Long,
        var duration: Long = 0L,
        var isOpen: Boolean = true
    )

    fun collectEvents(start: Long, end: Long): List<AppEvent> {
        val events = usageStatsManager.queryEvents(start, end)

        val sessions = mutableMapOf<String, Session>()
        val sessionList = mutableListOf<AppEvent>()


        val event = UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)

            val pkg = event.packageName ?: continue

            // Ignore launchers or apps without activities
            if (isLauncherApp(pkg) || !hasLaunchableActivity(pkg))
                continue

            when (event.eventType) {

                UsageEvents.Event.ACTIVITY_RESUMED -> {
                    // If this app has no active session → new session
                    if (!sessions.containsKey(pkg) || !sessions[pkg]!!.isOpen) {
                        sessions[pkg] = Session(
                            packageName = pkg,
                            startTime = event.timeStamp,
                            lastResume = event.timeStamp
                        )

                        // Log OPEN event
                        sessionList.add(
                            AppEvent(
                                event = "MOVE_TO_FOREGROUND",
                                appName = getAppName(pkg),
                                packageName = pkg,
                                timestamp = event.timeStamp,
                                duration = 0
                            )
                        )
                    } else {
                        // App reopened after pause
                        sessions[pkg]!!.lastResume = event.timeStamp
                        sessions[pkg]!!.isOpen = true
                    }
                }

                UsageEvents.Event.ACTIVITY_PAUSED,
                UsageEvents.Event.ACTIVITY_STOPPED -> {

                    val session = sessions[pkg] ?: continue
                    if (session.isOpen) {

                        val duration = event.timeStamp - session.lastResume
                        session.duration += duration
                        session.isOpen = false

                        // Log CLOSE event
                        sessionList.add(
                            AppEvent(
                                event = "APP_CLOSED",
                                appName = getAppName(pkg),
                                packageName = pkg,
                                timestamp = event.timeStamp,
                                duration = session.duration
                            )
                        )
                    }
                }
            }
        }

        // Handle apps still open at the end time
        for ((pkg, session) in sessions) {
            if (session.isOpen) {
                session.duration += (end - session.lastResume)

                sessionList.add(
                    AppEvent(
                        event = "APP_CLOSED",
                        appName = getAppName(pkg),
                        packageName = pkg,
                        timestamp = end,
                        duration = session.duration
                    )
                )
            }
        }

        return sessionList.sortedBy { it.timestamp }
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
