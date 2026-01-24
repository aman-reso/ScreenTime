package com.app.screentime.service

/**
 * Service to listen for system-wide notifications and store them in the database.
 * This service provides richer notification data compared to Accessibility Service.
 * Special handling for WhatsApp and WhatsApp Business to capture images.
 */

import android.app.Notification
import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.entity.CapturedNotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class NotificationHistoryListener : NotificationListenerService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var database: ScreenTimeDatabase? = null

    // Package names
    companion object {
        private const val TAG = "NotificationHistory"
        private const val WHATSAPP_PACKAGE = "com.whatsapp"
        private const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
        private const val PLAY_STORE_PACKAGE = "com.android.vending"

        // Directory name for saving notification images
        private const val NOTIFICATION_IMAGES_DIR = "notification_images"
    }

    override fun onCreate() {
        super.onCreate()
        if (database == null) {
            database = ScreenTimeDatabase.getDatabase(this)
        }
        // Create images directory if it doesn't exist
        getNotificationImagesDir()
        Log.d(TAG, "NotificationListener started")
    }

    /**
     * Get or create the directory for storing notification images
     */
    private fun getNotificationImagesDir(): File {
        val dir = File(filesDir, NOTIFICATION_IMAGES_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Check if the package is WhatsApp or WhatsApp Business
     */
    private fun isWhatsAppPackage(packageName: String): Boolean {
        return packageName == WHATSAPP_PACKAGE || packageName == WHATSAPP_BUSINESS_PACKAGE
    }

    /**
     * Save bitmap to local storage and return the file path
     */
    private fun saveBitmapToLocal(bitmap: Bitmap, prefix: String): String? {
        return try {
            val fileName = "${prefix}_${UUID.randomUUID()}.jpg"
            val file = File(getNotificationImagesDir(), fileName)
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            }
            file.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error saving image to local storage", e)
            null
        }
    }

    /**
     * Extract large icon bitmap from notification
     */
    @Suppress("DEPRECATION")
    private fun extractLargeIcon(notification: Notification): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val icon: Icon? = notification.getLargeIcon()
                icon?.loadDrawable(this)?.let { drawable ->
                    val bitmap = Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = android.graphics.Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bitmap
                }
            } else {
                notification.largeIcon
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting large icon", e)
            null
        }
    }

    /**
     * Extract big picture from notification extras (for image notifications)
     */
    @Suppress("DEPRECATION")
    private fun extractBigPicture(extras: android.os.Bundle): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                extras.getParcelable(Notification.EXTRA_PICTURE, Bitmap::class.java)
            } else {
                extras.getParcelable(Notification.EXTRA_PICTURE) as? Bitmap
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting big picture", e)
            null
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        // Ignore own app notifications
        if (sbn.packageName == packageName) return

        // Ignore Play Store notifications
        if (sbn.packageName == PLAY_STORE_PACKAGE) return

        val notification = sbn.notification
        val extras = notification.extras ?: return

        // Ignore group summary notifications
        val isSummary =
            notification.flags and Notification.FLAG_GROUP_SUMMARY != 0
        if (isSummary) return

        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()

        val text =
            extras.getCharSequence(Notification.EXTRA_TEXT)
                ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
                ?: extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
                    ?.joinToString("\n")

        if (title.isNullOrBlank() && text.isNullOrBlank()) return

        try {
            serviceScope.launch {
                var imagePath: String? = null
                var profileImagePath: String? = null

                if (isWhatsAppPackage(sbn.packageName)) {
                    extractBigPicture(extras)?.let { bitmap ->
                        imagePath = saveBitmapToLocal(bitmap, "whatsapp_img")
                        if (!bitmap.isRecycled) {
                            bitmap.recycle()
                        }
                    }
                    extractLargeIcon(notification)?.let { bitmap ->
                        profileImagePath = saveBitmapToLocal(bitmap, "whatsapp_profile")
                        if (!bitmap.isRecycled) {
                            bitmap.recycle()
                        }
                    }
                }

                val entity = CapturedNotificationEntity(
                    packageName = sbn.packageName,
                    title = title,
                    text = text?.toString(),
                    timestamp = sbn.postTime,
                    isRemoved = false,
                    imagePath = imagePath,
                    profileImagePath = profileImagePath
                )
                database?.capturedNotificationDao()?.insertNotification(entity)
                Log.d(TAG, "Saved notification: ${sbn.packageName}, hasImage: ${imagePath != null}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving notification", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        requestRebind(
            ComponentName(this, NotificationHistoryListener::class.java)
        )
    }
}

