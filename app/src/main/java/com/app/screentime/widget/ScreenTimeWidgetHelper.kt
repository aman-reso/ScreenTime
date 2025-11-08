package com.app.screentime.widget

import android.content.Context
import com.app.screentime.data.entity.AppUsage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Helper class to make it easy to update the ScreenTime widget from anywhere in the app.
 * This provides a simple, non-coroutine interface for updating widget data.
 */
class ScreenTimeWidgetHelper {

    companion object {
        private val widgetUpdater = mutableMapOf<Context, ScreenTimeWidgetUpdater>()

        /**
         * Update the widget with the latest screen time data.
         * This can be called from anywhere in your app.
         *
         * @param context The application context
         * @param totalUsage Total usage time in milliseconds
         * @param dailyLimit Daily time limit in milliseconds
         * @param topApps List of top apps by usage
         */
        fun updateWidget(
            context: Context,
            totalUsage: Long,
            dailyLimit: Long = 10800000L, // Default 3 hours
            topApps: List<AppUsage> = emptyList()
        ) {
            val updater = widgetUpdater.getOrPut(context) {
                ScreenTimeWidgetUpdater(context)
            }

            // Update widget asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                updater.updateWidget(totalUsage, dailyLimit, topApps)
            }
        }

        /**
         * Simple update method that just updates the total usage time.
         *
         * @param context The application context
         * @param totalUsage Total usage time in milliseconds
         */
        fun updateWidgetSimple(context: Context, totalUsage: Long) {
            updateWidget(context, totalUsage)
        }

        /**
         * Get all app usages from the repository and update the widget.
         * This is a helper method that fetches data and updates the widget.
         *
         * @param context The application context
         * @param appUsages List of all app usages
         * @param dailyLimit Daily time limit in milliseconds
         */
        fun updateWidgetFromAppUsages(
            context: Context,
            appUsages: List<AppUsage>,
            dailyLimit: Long = 10800000L // Default 3 hours
        ) {
            val totalUsage = appUsages.sumOf { it.appScreenTime }
            val topApps = appUsages.sortedByDescending { it.appScreenTime }.take(5)

            updateWidget(context, totalUsage, dailyLimit, topApps)
        }
    }
}
