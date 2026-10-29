package com.app.screentime.core.ui.security

import android.Manifest
import android.app.Activity
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresPermission
import com.app.screentime.core.ui.util.ODSToastManager
import com.telekom.odsystem.organisms.toast.ODSToastMode

/**
 * Manages privacy and security against screenshot capture:
 * 1. Enables WindowManager.LayoutParams.FLAG_SECURE during active foreground usage to prevent screenshots.
 * 2. Clears FLAG_SECURE when the app transitions into the background so the system recents preview
 *    displays the normal screen (without a black or white screen).
 * 3. Listens for screenshot capture attempts via Android 14+ ScreenCaptureCallback and ContentObserver fallback,
 *    and triggers an ODS warning toast notification.
 */
class ScreenshotProtectionManager(private val activity: Activity) {

    private var screenCaptureCallback: Activity.ScreenCaptureCallback? = null
    private var contentObserver: ContentObserver? = null
    private var lastToastTimestamp: Long = 0L

    companion object {
        private const val TAG = "ScreenshotProtection"
        private const val DEBOUNCE_MILLIS = 2000L
    }

    /**
     * Secures the current window against screen captures and screen recording while in foreground.
     */
    fun applySecureFlag() {
        try {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to apply FLAG_SECURE", e)
        }
    }

    /**
     * Clears FLAG_SECURE so that background / recent tasks switcher
     * renders the normal UI without black or white screens.
     */
    fun clearSecureFlag() {
        try {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to clear FLAG_SECURE", e)
        }
    }

    /**
     * Call in Activity.onPause() or when the window loses focus.
     * Clears FLAG_SECURE so system recents preview does not show a black or white screen.
     */
    fun onAppBackgrounded() {
        clearSecureFlag()
    }

    /**
     * Call in Activity.onResume() or when the window regains focus.
     * Re-applies FLAG_SECURE to prevent screenshot capturing during active usage.
     */
    fun onAppForegrounded() {
        applySecureFlag()
    }

    /**
     * Starts listening for screenshot attempts. Call in Activity.onStart().
     */
    @RequiresPermission(Manifest.permission.DETECT_SCREEN_CAPTURE)
    fun startListening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            try {
                val callback = Activity.ScreenCaptureCallback {
                    notifyScreenshotAttempt()
                }
                screenCaptureCallback = callback
                activity.registerScreenCaptureCallback(activity.mainExecutor, callback)
            } catch (e: Throwable) {
                Log.w(TAG, "Could not register ScreenCaptureCallback: ${e.message}")
            }
        }

        registerContentObserver()
    }

    /**
     * Stops listening for screenshot attempts. Call in Activity.onStop().
     */
    fun stopListening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            screenCaptureCallback?.let { callback ->
                try {
                    activity.unregisterScreenCaptureCallback(callback)
                } catch (e: Throwable) {
                    Log.w(TAG, "Could not unregister ScreenCaptureCallback: ${e.message}")
                }
            }
            screenCaptureCallback = null
        }

        unregisterContentObserver()
    }

    /**
     * Notifies the user with an ODS warning toast that screenshots are restricted.
     */
    fun notifyScreenshotAttempt() {
        val now = System.currentTimeMillis()
        if (now - lastToastTimestamp < DEBOUNCE_MILLIS) return
        lastToastTimestamp = now

        activity.runOnUiThread {
            ODSToastManager.show(
                title = "Security Alert",
                message = "Screenshots are restricted to protect privacy.",
                mode = ODSToastMode.INFORMATIVE
            )
        }
    }

    private fun registerContentObserver() {
        if (contentObserver != null) return
        try {
            val handler = Handler(Looper.getMainLooper())
            contentObserver = object : ContentObserver(handler) {
                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    super.onChange(selfChange, uri)
                    if (uri != null) {
                        checkScreenshotUri(uri)
                    }
                }
            }
            activity.contentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                contentObserver!!
            )
        } catch (e: Throwable) {
            Log.w(TAG, "Could not register MediaStore ContentObserver: ${e.message}")
        }
    }

    private fun unregisterContentObserver() {
        contentObserver?.let { observer ->
            try {
                activity.contentResolver.unregisterContentObserver(observer)
            } catch (e: Throwable) {
                Log.w(TAG, "Could not unregister MediaStore ContentObserver: ${e.message}")
            }
        }
        contentObserver = null
    }

    private fun checkScreenshotUri(uri: Uri) {
        val path = uri.toString().lowercase()
        if (path.contains("screenshot")) {
            notifyScreenshotAttempt()
            return
        }

        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED
            )
            activity.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val dataIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val dateAddedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)

                    val filePath = if (dataIndex >= 0) cursor.getString(dataIndex) ?: "" else ""
                    val dateAdded = if (dateAddedIndex >= 0) cursor.getLong(dateAddedIndex) else 0L

                    val currentTimeSec = System.currentTimeMillis() / 1000
                    val isRecent = (currentTimeSec - dateAdded) in -5..10

                    if (isRecent && (filePath.lowercase().contains("screenshot") || uri.toString().lowercase().contains("screenshot"))) {
                        notifyScreenshotAttempt()
                    }
                }
            }
        } catch (_: Throwable) {
            // Silently ignore permission/scoped-storage limitations
        }
    }
}
