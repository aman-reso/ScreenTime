package com.app.screentime.service

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class to track app usage start times for duration limits.
 */
class AppUsageTracker(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_usage_tracker", Context.MODE_PRIVATE)

    fun setAppStartTime(packageName: String, time: Long) {
        prefs.edit().putLong(packageName, time).apply()
    }

    fun getAppStartTime(packageName: String): Long {
        return prefs.getLong(packageName, 0L)
    }
}
