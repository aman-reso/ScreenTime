package com.app.screentime.applock.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.blocking.repository.BlockingRepository
import com.app.screentime.service.ListenerService

/**
 * Helper class to manage ListenerService based on app lock state.
 * Service is only started if there are locked apps, otherwise it's stopped.
 */
/**
 * Checks if there are any locked apps or blocking rules.
 */
fun hasActiveRules(context: Context): Boolean {
    return try {
        val lockRepo = AppLockRepository(context)
        val blockingRepo = BlockingRepository(context)

        val hasLocks = lockRepo.getAllRules().any { it.isLocked }
        val hasBlocking = blockingRepo.getAllRules().isNotEmpty()

        hasLocks || hasBlocking
    } catch (e: Exception) {
        Log.e("AppLockServiceHelper", "Error checking rules", e)
        false
    }
}

/**
 * Starts ListenerService if there are locked apps and permissions are granted.
 * Stops the service if there are no locked apps.
 */
fun updateServiceState(
    context: Context,
    hasUsageStatsPermission: Boolean,
    hasOverlayPermission: Boolean
) {
    try {
        val shouldRun = hasActiveRules(context)
        val serviceIntent = Intent(context, ListenerService::class.java)

        if (shouldRun && hasUsageStatsPermission && hasOverlayPermission) {
            // Start service if there are active rules and permissions are granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
            Log.d("AppLockServiceHelper", "Started ListenerService - active rules exist")
        } else {
            // Stop service if no active rules or permissions not granted
            context.stopService(serviceIntent)
            Log.d(
                "AppLockServiceHelper",
                "Stopped ListenerService - no active rules or permissions missing"
            )
        }
    } catch (e: Exception) {
        Log.e("AppLockServiceHelper", "Error updating service state", e)
    }
}

/**
 * Starts ListenerService if conditions are met (locked apps + permissions).
 */
fun startServiceIfNeeded(
    context: Context,
    hasUsageStatsPermission: Boolean,
    hasOverlayPermission: Boolean
) {
    if (hasActiveRules(context) && hasUsageStatsPermission && hasOverlayPermission) {
        try {
            val serviceIntent = Intent(context, ListenerService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
            Log.d("AppLockServiceHelper", "Started ListenerService")
        } catch (e: Exception) {
            Log.e("AppLockServiceHelper", "Error starting ListenerService", e)
        }
    }
}

/**
 * Stops ListenerService.
 */
fun stopService(context: Context) {
    try {
        val serviceIntent = Intent(context, ListenerService::class.java)
        context.stopService(serviceIntent)
        Log.d("AppLockServiceHelper", "Stopped ListenerService")
    } catch (e: Exception) {
        Log.e("AppLockServiceHelper", "Error stopping ListenerService", e)
    }
}


