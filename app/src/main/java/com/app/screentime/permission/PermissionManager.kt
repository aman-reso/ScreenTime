package com.app.screentime.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

/**
 * Manages app usage access permissions for the ScreenTime application.
 * Handles both regular permissions and special app usage access permission.
 */
class PermissionManager(
    private val context: Context
) {
    private val _permissionState = MutableStateFlow(PermissionState())
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    /**
     * Checks if all required permissions are granted
     */
    fun hasAllPermissions(): Boolean {
        return hasUsageStatsPermission() && hasNotificationPermission()
    }

    /**
     * Checks if usage stats permission is granted
     */
    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }

    /**
     * Checks if notification permission is granted (Android 13+)
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Requests usage stats permission by opening system settings
     */
    fun requestUsageStatsPermission(activity: Activity) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.data = "package:${context.packageName}".toUri()
        activity.startActivity(intent)
    }

    /**
     * Requests notification permission (Android 13+)
     */
    fun requestNotificationPermission(
        activity: Activity,
        launcher: ActivityResultLauncher<String>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * Updates permission state
     */
    fun updatePermissionState() {
        _permissionState.value = PermissionState(
            hasUsageStats = hasUsageStatsPermission(),
            hasNotification = hasNotificationPermission(),
            hasAllPermissions = hasAllPermissions()
        )
    }

    /**
     * Opens app settings for manual permission granting
     */
    fun openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = "package:${context.packageName}".toUri()
        activity.startActivity(intent)
    }
}

/**
 * Data class representing the current permission state
 */
data class PermissionState(
    val hasUsageStats: Boolean = false,
    val hasNotification: Boolean = false,
    val hasAllPermissions: Boolean = false
)

/**
 * Extension function to create a permission manager for activities
 */
fun Activity.createPermissionManager(): PermissionManager {
    return PermissionManager(this)
}

/**
 * Extension function to create a permission manager for fragments
 */
fun Fragment.createPermissionManager(): PermissionManager {
    return PermissionManager(requireContext())
}
