package com.app.screentime.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.applock.util.AppLockServiceManager

class BootCompletedReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootCompletedReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Boot completed, checking if app lock service should start")
            
            // Check if app lock service should be started
            val appLockRepository = AppLockRepository(context)
            val hasLockedApps = appLockRepository.getLockedApps().isNotEmpty()
            val hasOverlayPermission = Settings.canDrawOverlays(context)
            val hasUsageStatsPermission = checkUsageStatsPermission(context)
            
            Log.d(TAG, "hasLockedApps=$hasLockedApps, hasOverlay=$hasOverlayPermission, hasUsageStats=$hasUsageStatsPermission")
            
            if (hasLockedApps && hasOverlayPermission && hasUsageStatsPermission) {
                Log.d(TAG, "Starting AppLockMonitoringService")
                AppLockServiceManager.startService(context)
            }
        }
    }

    private fun checkUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }
}