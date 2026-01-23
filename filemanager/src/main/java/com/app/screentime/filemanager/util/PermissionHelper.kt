package com.app.screentime.filemanager.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

/**
 * Helper class for checking and requesting file management permissions
 */
object FileManagerPermissionHelper {

    /**
     * Check if all required storage permissions are granted
     */
    fun hasStoragePermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ uses granular media permissions
            hasMediaPermissions(context)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ may need manage external storage
            hasLegacyStoragePermission(context)
        } else {
            // Android 10 and below use READ/WRITE_EXTERNAL_STORAGE
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Check if media permissions are granted (Android 13+)
     */
    private fun hasMediaPermissions(context: Context): Boolean {
        val hasImages = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

        val hasVideo = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_VIDEO
        ) == PackageManager.PERMISSION_GRANTED

        val hasAudio = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        return hasImages && hasVideo && hasAudio
    }

    /**
     * Check if legacy storage permission is granted (Android 11+)
     */
    private fun hasLegacyStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            android.os.Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Get the required permissions array based on Android version
     */
    fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    /**
     * Get intent to request manage external storage permission (Android 11+)
     */
    fun getManageStoragePermissionIntent(context: Context): Intent {
        return Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
            data = "package:${context.packageName}".toUri()
        }
    }

    /**
     * Check if manage external storage permission is needed
     */
    fun needsManageStoragePermission(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                !android.os.Environment.isExternalStorageManager()
    }
}

