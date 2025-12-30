package com.app.screentime.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Utility functions for handling permissions in the ScreenTime app
 */
object PermissionUtils {
    
    /**
     * Creates a permission launcher for notification permission (Android 13+)
     */
    fun createNotificationPermissionLauncher(
        activity: ComponentActivity,
        onResult: (Boolean) -> Unit
    ) = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onResult(isGranted)
    }

    /**
     * Creates a permission launcher for notification permission in fragments
     */
    fun createNotificationPermissionLauncher(
        fragment: Fragment,
        onResult: (Boolean) -> Unit
    ) = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onResult(isGranted)
    }

    /**
     * Creates a launcher for usage stats settings
     */
    fun createUsageStatsSettingsLauncher(
        activity: ComponentActivity,
        onResult: (Boolean) -> Unit
    ) = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Check if permission was granted after returning from settings
        val permissionManager = activity.createPermissionManager()
        onResult(permissionManager.hasUsageStatsPermission())
    }

    /**
     * Creates a launcher for usage stats settings in fragments
     */
    fun createUsageStatsSettingsLauncher(
        fragment: Fragment,
        onResult: (Boolean) -> Unit
    ) = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Check if permission was granted after returning from settings
        val permissionManager = fragment.createPermissionManager()
        onResult(permissionManager.hasUsageStatsPermission())
    }

    /**
     * Creates a launcher for app settings
     */
    fun createAppSettingsLauncher(
        activity: ComponentActivity,
        onResult: (Boolean) -> Unit
    ) = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Check if permissions were granted after returning from settings
        val permissionManager = activity.createPermissionManager()
        onResult(permissionManager.hasAllPermissions())
    }

    /**
     * Creates a launcher for app settings in fragments
     */
    fun createAppSettingsLauncher(
        fragment: Fragment,
        onResult: (Boolean) -> Unit
    ) = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Check if permissions were granted after returning from settings
        val permissionManager = fragment.createPermissionManager()
        onResult(permissionManager.hasAllPermissions())
    }

    /**
     * Gets the appropriate permission request message based on missing permissions
     */
    fun getPermissionRequestMessage(context: Context, permissionManager: PermissionManager): String {
        return when {
            !permissionManager.hasUsageStatsPermission() && !permissionManager.hasNotificationPermission() -> {
                "ScreenTime needs access to app usage statistics and notifications to track your screen time effectively."
            }
            !permissionManager.hasUsageStatsPermission() -> {
                "ScreenTime needs access to app usage statistics to track your screen time."
            }
            !permissionManager.hasNotificationPermission() -> {
                "ScreenTime needs notification permission to show usage alerts."
            }
            else -> {
                "All permissions are granted. You can now use ScreenTime to track your app usage."
            }
        }
    }

    /**
     * Gets the appropriate action text for permission requests
     */
    fun getPermissionActionText(permissionManager: PermissionManager): String {
        return when {
            !permissionManager.hasUsageStatsPermission() && !permissionManager.hasNotificationPermission() -> {
                "Grant Permissions"
            }
            !permissionManager.hasUsageStatsPermission() -> {
                "Grant Usage Access"
            }
            !permissionManager.hasNotificationPermission() -> {
                "Grant Notification Permission"
            }
            else -> {
                "All Set"
            }
        }
    }

    /**
     * Checks if the device supports usage stats (Android 5.1+)
     */
    fun isUsageStatsSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /**
     * Checks if the device requires notification permission (Android 13+)
     */
    fun isNotificationPermissionRequired(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU
    }

}
