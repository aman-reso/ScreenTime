package com.app.screentime.messaging

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScreenTimeFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var tokenManager: FCMTokenManager

    companion object {
        private const val TAG = "FCMService"
    }

    override fun onCreate() {
        super.onCreate()
        // Create notification channels when service is created
        NotificationHelper.createNotificationChannels(this)
    }

    /**
     * Called when a new FCM token is generated
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

        // Save the new token
        tokenManager.saveToken(token)

        // Token will be sent to backend automatically by FCMTokenManager
    }

    /**
     * Called when a message is received
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Check if message contains a notification payload
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message Notification Body: ${notification.body}")
            handleNotificationMessage(notification, remoteMessage.data)
        }
    }

    /**
     * Handle data-only messages (when app is in foreground or background)
     */
    private fun handleDataMessage(data: Map<String, String>) {
        val title = data["title"] ?: "ScreenTime"
        val message = data["message"] ?: data["body"] ?: ""
        val type = data["type"] ?: "default"
        val notificationId =
            data["notification_id"]?.toIntOrNull() ?: System.currentTimeMillis().toInt()

        // Determine notification type
        val notificationType = when (type.lowercase()) {
            "challenge" -> NotificationHelper.NotificationType.CHALLENGE
            "alert" -> NotificationHelper.NotificationType.ALERT
            else -> NotificationHelper.NotificationType.DEFAULT
        }

        // Extract additional notification data
        val subtitle = data["subtitle"]
        val imageUrl = data["image"] ?: data["image_url"]
        val deeplink = data["deeplink"] ?: data["link"]

        // Show notification
        NotificationHelper.showNotification(
            context = this,
            notificationId = notificationId,
            title = title,
            message = message,
            type = notificationType,
            data = data,
            imageUrl = imageUrl,
            subtitle = subtitle,
            deeplink = deeplink
        )
    }

    /**
     * Handle notification messages
     */
    private fun handleNotificationMessage(
        notification: RemoteMessage.Notification,
        data: Map<String, String>
    ) {
        val title = notification.title ?: "ScreenTime"
        val message = notification.body ?: ""
        val type = data["type"] ?: "default"
        val notificationId =
            data["notification_id"]?.toIntOrNull() ?: System.currentTimeMillis().toInt()

        // Determine notification type
        val notificationType = when (type.lowercase()) {
            "challenge" -> NotificationHelper.NotificationType.CHALLENGE
            "alert" -> NotificationHelper.NotificationType.ALERT
            else -> NotificationHelper.NotificationType.DEFAULT
        }

        // Extract additional notification data
        val subtitle = data["subtitle"] ?: notification.body
        val imageUrl = data["image"] ?: data["image_url"] ?: notification.imageUrl?.toString()
        val deeplink = data["deeplink"] ?: data["link"]

        // Check if notifications are enabled
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            NotificationHelper.showNotification(
                context = this,
                notificationId = notificationId,
                title = title,
                message = message,
                type = notificationType,
                data = data,
                imageUrl = imageUrl,
                subtitle = subtitle,
                deeplink = deeplink
            )
        } else {
            Log.w(TAG, "Notifications are disabled by user")
        }
    }
}

