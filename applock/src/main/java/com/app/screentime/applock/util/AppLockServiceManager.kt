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
     * Check if service should be running based on locked apps and app limits
     */
    fun shouldServiceRun(hasLockedApps: Boolean, hasOverlayPermission: Boolean, hasUsageStatsPermission: Boolean): Boolean {
        return hasLockedApps && hasOverlayPermission && hasUsageStatsPermission
    }

    /**
     * Check if service should be running based on repository state
     */
    fun shouldServiceRun(context: Context): Boolean {
        val repository = com.app.screentime.applock.repository.AppLockRepository(context)
        val hasOverlayPermission = PermissionHelper.hasOverlayPermission(context)
        val hasUsageStatsPermission = PermissionHelper.hasUsageStatsPermission(context)
        return repository.shouldServiceRun() && hasOverlayPermission && hasUsageStatsPermission
    }

    /**
     * Start service if needed based on app limits or locked apps
     */
    fun startServiceIfNeeded(context: Context) {
        if (shouldServiceRun(context)) {
            startService(context)
        }
    }
}

