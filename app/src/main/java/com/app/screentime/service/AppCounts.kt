package com.app.screentime.service

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

/**
 * Helper class to track app launch counts.
 * Resets counts daily.
 */
class AppCounts(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_launch_counts", Context.MODE_PRIVATE)
    
    fun increment(packageName: String) {
        checkDateReset()
        val currentCount = get(packageName)
        prefs.edit().putInt(packageName, currentCount + 1).apply()
    }

    fun get(packageName: String): Int {
        checkDateReset()
        return prefs.getInt(packageName, 0)
    }

    private fun checkDateReset() {
        val lastResetDay = prefs.getInt("last_reset_day", -1)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        if (lastResetDay != currentDay) {
            prefs.edit().clear().putInt("last_reset_day", currentDay).apply()
        }
    }
}
