package com.app.screentime.applock.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log

/**
 * Helper class to check and open system app lock settings if available
 */
object SystemAppLockHelper {
    private const val TAG = "SystemAppLockHelper"
    
    /**
     * Try to open system app lock settings (App Locker page specifically)
     * @return true if system app lock was opened, false otherwise
     */
    fun tryOpenSystemAppLock(context: Context): Boolean {
        // List of possible system app lock intents (manufacturer-specific App Locker pages)
        val appLockIntents = listOf(
            // Samsung - App Locker
            Intent("com.samsung.android.applock.ACTION_APP_LOCK_SETTINGS"),
            
            // Xiaomi/MIUI - App Lock
            Intent("miui.intent.action.APP_LOCK").apply {
                addCategory(Intent.CATEGORY_DEFAULT)
            },
            // Alternative Xiaomi App Lock
            Intent().apply {
                setClassName("com.miui.securitycenter", "com.miui.securitycenter.ui.settings.AppSecuritySettings")
            },
            
            // OnePlus - App Locker
            Intent().apply {
                setClassName("com.oneplus.security", "com.oneplus.security.safecenter.AppSecurityActivity")
            },
            Intent().apply {
                setClassName("com.oneplus.security", "com.oneplus.security.safecenter.SafeCenterActivity")
            },
            
            // Huawei - App Lock
            Intent().apply {
                setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.applock.activity.AppLockActivity")
            },
            Intent().apply {
                setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
            },
            
            // Oppo/ColorOS - App Lock
            Intent().apply {
                setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.AppLockManagerActivity")
            },
            Intent().apply {
                setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity")
            },
            
            // Vivo/FuntouchOS - App Lock
            Intent().apply {
                setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
            },
            Intent().apply {
                setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.AppLockActivity")
            },
            
            // Realme - App Lock
            Intent().apply {
                setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.AppLockManagerActivity")
            },
            
            // Motorola - App Lock (if available)
            Intent().apply {
                setClassName("com.motorola.applock", "com.motorola.applock.AppLockActivity")
            }
        )
        
        // Try each intent
        for (intent in appLockIntents) {
            try {
                // Check if the intent can be resolved
                val resolveInfo = context.packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
                
                if (resolveInfo != null) {
                    // Add flags for new task
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    
                    // Try to start the activity
                    context.startActivity(intent)
                    Log.d(TAG, "Successfully opened system app lock: ${intent.component?.className}")
                    return true
                }
            } catch (e: Exception) {
                // Intent failed, try next one
                Log.d(TAG, "Failed to open app lock intent: ${intent.component?.className}, error: ${e.message}")
            }
        }
        
        Log.d(TAG, "No system app lock found, will use custom app lock")
        return false
    }
    
    /**
     * Check if system app lock is available without opening it
     */
    fun isSystemAppLockAvailable(context: Context): Boolean {
        val appLockIntents = listOf(
            // Samsung
            Intent("com.samsung.android.applock.ACTION_APP_LOCK_SETTINGS"),
            
            // Xiaomi/MIUI
            Intent("miui.intent.action.APP_LOCK").apply {
                addCategory(Intent.CATEGORY_DEFAULT)
            },
            Intent().apply {
                setClassName("com.miui.securitycenter", "com.miui.securitycenter.ui.settings.AppSecuritySettings")
            },
            
            // OnePlus
            Intent().apply {
                setClassName("com.oneplus.security", "com.oneplus.security.safecenter.AppSecurityActivity")
            },
            Intent().apply {
                setClassName("com.oneplus.security", "com.oneplus.security.safecenter.SafeCenterActivity")
            },
            
            // Huawei
            Intent().apply {
                setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.applock.activity.AppLockActivity")
            },
            Intent().apply {
                setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
            },
            
            // Oppo/ColorOS
            Intent().apply {
                setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.AppLockManagerActivity")
            },
            Intent().apply {
                setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity")
            },
            
            // Vivo/FuntouchOS
            Intent().apply {
                setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
            },
            Intent().apply {
                setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.AppLockActivity")
            },
            
            // Realme
            Intent().apply {
                setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.AppLockManagerActivity")
            },
            
            // Motorola
            Intent().apply {
                setClassName("com.motorola.applock", "com.motorola.applock.AppLockActivity")
            }
        )
        
        return appLockIntents.any { intent ->
            try {
                val resolveInfo = context.packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
                resolveInfo != null
            } catch (e: Exception) {
                false
            }
        }
    }
}

