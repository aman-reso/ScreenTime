package com.app.screentime.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.app.screentime.MainActivity
import com.app.screentime.R
import com.app.screentime.ScreenTimeApplication
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.ScreenUsageHelper
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.utils.DateUtils
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.joda.time.format.DateTimeFormat

/**
 * Pure XML-based widget provider (not using Glance)
 * Uses RemoteViews and XML layouts
 * Updates every 2 minutes using AlarmManager
 */
class ScreenTimeXmlWidgetProvider : AppWidgetProvider() {

    private val widgetScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    companion object {
        // Update interval: 2 minutes (120,000 ms)
        // Can be changed to 1 minute (60,000 ms) if needed
        private const val UPDATE_INTERVAL_MS = 2 * 60 * 1000L // 2 minutes
        private const val ACTION_UPDATE_WIDGET = "com.app.screentime.widget.UPDATE"
    }

    /**
     * Entry point for Hilt dependencies
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun localAppUsageRepository(): LocalAppUsageRepository
        fun preferencesUseCase(): PreferencesUseCase
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widget instances
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        // Handle custom update action from AlarmManager
        if (intent.action == ACTION_UPDATE_WIDGET) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, ScreenTimeXmlWidgetProvider::class.java)
            )
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
            // Schedule next update (in try so receiver doesn't crash)
            try {
                scheduleNextUpdate(context)
            } catch (e: Exception) {
                android.util.Log.e("ScreenTimeWidget", "onReceive: failed to schedule update", e)
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Called when the first widget is added
        // Update immediately
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            android.content.ComponentName(context, ScreenTimeXmlWidgetProvider::class.java)
        )
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        // Schedule periodic updates using AlarmManager (inside try to avoid crashing receiver)
        try {
            scheduleNextUpdate(context)
        } catch (e: Exception) {
            android.util.Log.e("ScreenTimeWidget", "onEnabled: failed to schedule update", e)
        }
    }

    override fun onDisabled(context: Context) {
        // Called when the last widget is removed
        // Cancel scheduled updates
        cancelScheduledUpdates(context)
    }
    
    /**
     * Schedule the next widget update using AlarmManager
     * Uses setWindow() instead of setExact() to avoid requiring SCHEDULE_EXACT_ALARM permission
     */
    private fun scheduleNextUpdate(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ScreenTimeXmlWidgetProvider::class.java).apply {
                action = ACTION_UPDATE_WIDGET
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val triggerTime = System.currentTimeMillis() + UPDATE_INTERVAL_MS
            
            // Use setWindow() which doesn't require SCHEDULE_EXACT_ALARM permission
            // Window of 1 minute is acceptable for widget updates (2 min interval)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setWindow(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    60_000L, // 1 minute window
                    pendingIntent
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setWindow(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    60_000L, // 1 minute window
                    pendingIntent
                )
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: SecurityException) {
            // If exact alarm permission is required and not granted, fall back to set()
            // This should not happen with setWindow(), but handle it gracefully
            try {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, ScreenTimeXmlWidgetProvider::class.java).apply {
                    action = ACTION_UPDATE_WIDGET
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val triggerTime = System.currentTimeMillis() + UPDATE_INTERVAL_MS
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } catch (e2: Exception) {
                // Log error but don't crash - widget will update on next system update cycle
                android.util.Log.e("ScreenTimeWidget", "Failed to schedule widget update", e2)
            }
        } catch (e: Exception) {
            // Log error but don't crash
            android.util.Log.e("ScreenTimeWidget", "Failed to schedule widget update", e)
        }
    }
    
    /**
     * Cancel scheduled widget updates
     */
    private fun cancelScheduledUpdates(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ScreenTimeXmlWidgetProvider::class.java).apply {
            action = ACTION_UPDATE_WIDGET
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        widgetScope.launch {
            try {
                // Get Hilt entry point
                val entryPoint = EntryPointAccessors.fromApplication(
                    context.applicationContext as ScreenTimeApplication,
                    WidgetEntryPoint::class.java
                )
                val localAppUsageRepository = entryPoint.localAppUsageRepository()
                val preferencesUseCase = entryPoint.preferencesUseCase()

                // Get today's start and end time
                val startOfToday = DateUtils.startOfToday()
                val start = startOfToday.millis
                val end = DateUtils.nowMillis()

                // Get today's app usage
                val appUsageList = localAppUsageRepository.getAppsUsageForInterval(start, end)
                val totalUsage = appUsageList.sumOf { it.appScreenTime }

                // Format the time
                val formattedTime = formatUsageTime(totalUsage)

                // Get daily goal
                val dailyGoalHours = preferencesUseCase.getDailyGoalHours()
                val dailyGoalMs = dailyGoalHours * 60 * 60 * 1000L
                val progress = if (dailyGoalMs > 0) {
                    ((totalUsage.toFloat() / dailyGoalMs.toFloat()) * 100).toInt().coerceIn(0, 100)
                } else {
                    0
                }

                // Create RemoteViews
                val views = RemoteViews(context.packageName, R.layout.widget_screentime)

                // Update views
                views.setTextViewText(R.id.widget_total_time, formattedTime)
                views.setTextViewText(R.id.widget_label, "Today")
                views.setProgressBar(R.id.widget_progress, 100, progress, false)

                // Format last updated time
                val timeFormat = DateTimeFormat.forPattern("HH:mm")
                val lastUpdated = "Updated: ${timeFormat.print(DateUtils.now())}"
                views.setTextViewText(R.id.widget_last_updated, lastUpdated)

                // Set click intent to open app
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                // Make the entire widget clickable
                views.setOnClickPendingIntent(android.R.id.background, pendingIntent)
                views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)
                views.setOnClickPendingIntent(R.id.widget_total_time, pendingIntent)
                views.setOnClickPendingIntent(R.id.widget_label, pendingIntent)

                // Update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                // Handle error - show default values
                val views = RemoteViews(context.packageName, R.layout.widget_screentime)
                views.setTextViewText(R.id.widget_total_time, "0h 0m")
                views.setTextViewText(R.id.widget_label, "Today")
                views.setProgressBar(R.id.widget_progress, 100, 0, false)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    /**
     * Format usage time in milliseconds to human-readable string
     */
    private fun formatUsageTime(ms: Long): String {
        val totalMinutes = ms / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }
}

