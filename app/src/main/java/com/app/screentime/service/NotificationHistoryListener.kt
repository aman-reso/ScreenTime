package com.app.screentime.service

/**
 * Service to listen for system-wide notifications and store them in the database.
 * This service provides richer notification data compared to Accessibility Service.
 */

import android.app.Notification
import android.content.ComponentName
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

class NotificationHistoryListener : NotificationListenerService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var database: ScreenTimeDatabase? = null

    override fun onCreate() {
        super.onCreate()
        if (database == null) {
            database = ScreenTimeDatabase.getDatabase(this)
        }
        Log.d(TAG, "NotificationListener started")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        // Ignore own app notifications
        if (sbn.packageName == packageName) return

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

        serviceScope.launch {
            try {
                val entity = CapturedNotificationEntity(
                    packageName = sbn.packageName,
                    title = title,
                    text = text?.toString(),
                    timestamp = sbn.postTime,
                    isRemoved = false
                )
                database?.capturedNotificationDao()?.insertNotification(entity)
                Log.d(TAG, "Saved notification: ${sbn.packageName}")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving notification", e)
            }
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

    companion object {
        private const val TAG = "NotificationHistory"
    }
}

