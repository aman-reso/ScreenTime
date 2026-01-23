package com.app.screentime.applock.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.app.screentime.applock.service.AppLockMonitoringService

/**
 * Helper class to manage App Lock Monitoring Service
 */
object AppLockServiceManager {

    /**
     * Start the app lock monitoring service
     */
    fun startService(context: Context) {
        val intent = Intent(context, AppLockMonitoringService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    /**
     * Stop the app lock monitoring service
     */
    fun stopService(context: Context) {
        val intent = Intent(context, AppLockMonitoringService::class.java)
        context.stopService(intent)
    }

    /**
     * Check if service should be running based on locked apps
     */
    fun shouldServiceRun(hasLockedApps: Boolean, hasOverlayPermission: Boolean, hasUsageStatsPermission: Boolean): Boolean {
        return hasLockedApps && hasOverlayPermission && hasUsageStatsPermission
    }
}

