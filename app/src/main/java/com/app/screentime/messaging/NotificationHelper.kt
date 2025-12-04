package com.app.screentime.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.screentime.MainActivity
import com.app.screentime.R
import com.app.screentime.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import androidx.core.net.toUri

object NotificationHelper {
    private const val CHANNEL_ID_DEFAULT = "screentime_default"
    private const val CHANNEL_ID_CHALLENGES = "screentime_challenges"
    private const val CHANNEL_ID_ALERTS = "screentime_alerts"
    private const val CHANNEL_NAME_DEFAULT = "Default Notifications"
    private const val CHANNEL_NAME_CHALLENGES = "Challenge Notifications"
    private const val CHANNEL_NAME_ALERTS = "Alert Notifications"

    /**
     * Create notification channels for different notification types
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Default channel
            val defaultChannel = NotificationChannel(
                CHANNEL_ID_DEFAULT,
                CHANNEL_NAME_DEFAULT,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Default notifications for ScreenTime app"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 250, 250)
            }

            // Challenges channel
            val challengesChannel = NotificationChannel(
                CHANNEL_ID_CHALLENGES,
                CHANNEL_NAME_CHALLENGES,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about challenges and competitions"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            // Alerts channel
            val alertsChannel = NotificationChannel(
                CHANNEL_ID_ALERTS,
                CHANNEL_NAME_ALERTS,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important alerts and reminders"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000)
            }

            notificationManager.createNotificationChannels(
                listOf(defaultChannel, challengesChannel, alertsChannel)
            )
        }
    }

    /**
     * Get channel ID based on notification type
     */
    private fun getChannelId(type: NotificationType): String {
        return when (type) {
            NotificationType.CHALLENGE -> CHANNEL_ID_CHALLENGES
            NotificationType.ALERT -> CHANNEL_ID_ALERTS
            NotificationType.DEFAULT -> CHANNEL_ID_DEFAULT
        }
    }

    /**
     * Show a notification with support for images, deeplinks, title, and subtitle
     */
    fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String,
        type: NotificationType = NotificationType.DEFAULT,
        data: Map<String, String>? = null,
        imageUrl: String? = null,
        subtitle: String? = null,
        deeplink: String? = null
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent with deeplink support
        val intent = createDeeplinkIntent(context, deeplink, data)

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, getChannelId(type))
            .setSmallIcon(R.drawable.app_icon_vertical)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(
                when (type) {
                    NotificationType.ALERT, NotificationType.CHALLENGE -> NotificationCompat.PRIORITY_HIGH
                    NotificationType.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
                }
            )

        // Add subtitle if provided
        subtitle?.let {
            notificationBuilder.setSubText(it)
        }

        // Handle image
        if (!imageUrl.isNullOrEmpty()) {
            // Load image asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val bitmap = loadImageFromUrl(imageUrl)
                    withContext(Dispatchers.Main) {
                        if (bitmap != null) {
                            notificationBuilder
                                .setStyle(
                                    NotificationCompat.BigPictureStyle()
                                        .bigPicture(bitmap)
                                        .bigLargeIcon(null as Bitmap?)
                                )
                                .setLargeIcon(bitmap)
                            notificationManager.notify(notificationId, notificationBuilder.build())
                        } else {
                            // Fallback to BigTextStyle if image fails to load
                            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
                            notificationManager.notify(notificationId, notificationBuilder.build())
                        }
                    }
                } catch (e: Exception) {
                    // Fallback to BigTextStyle if image loading fails
                    withContext(Dispatchers.Main) {
                        notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
                        notificationManager.notify(notificationId, notificationBuilder.build())
                    }
                }
            }
            // Show notification immediately with text, image will update when loaded
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
            notificationManager.notify(notificationId, notificationBuilder.build())
        } else {
            // No image, use BigTextStyle
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    /**
     * Create intent with deeplink support
     */
    private fun createDeeplinkIntent(
        context: Context,
        deeplink: String?,
        data: Map<String, String>?
    ): Intent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Handle deeplink
        when {
            !deeplink.isNullOrEmpty() -> {
                // Parse deeplink and add navigation route
                intent.putExtra("deeplink", deeplink)
                
                // Parse deeplink format: apptime://screen/route?param1=value1&param2=value2
                // or simple route format: challenges, challenge_detail/123, etc.
                when {
                    deeplink.startsWith("apptime://") -> {
                        // Full deeplink format
                        val uri = deeplink.toUri()
                        val route = uri.host ?: ""
                        intent.putExtra("route", route)
                        
                        // Add query parameters
                        uri.queryParameterNames.forEach { key ->
                            uri.getQueryParameter(key)?.let { value ->
                                intent.putExtra(key, value)
                            }
                        }
                    }
                    deeplink.contains("/") -> {
                        // Route with parameters: challenge_detail/123
                        val parts = deeplink.split("/")
                        intent.putExtra("route", parts[0])
                        if (parts.size > 1) {
                            // Extract parameter name from route (e.g., challengeId from challenge_detail)
                            val paramName = when (parts[0]) {
                                "challenge_detail" -> "challengeId"
                                "app_usage_detail" -> "packageName"
                                "record_detail" -> "username"
                                else -> "id"
                            }
                            intent.putExtra(paramName, parts[1])
                        }
                    }
                    else -> {
                        // Simple route: challenges, statistics, etc.
                        intent.putExtra("route", deeplink)
                    }
                }
            }
            data?.containsKey("deeplink") == true -> {
                // Deeplink in data payload
                val link = data["deeplink"] ?: ""
                intent.putExtra("deeplink", link)
                intent.putExtra("route", link)
            }
        }

        // Add all data extras
        data?.forEach { (key, value) ->
            if (key != "deeplink" && key != "image" && key != "image_url") {
                intent.putExtra(key, value)
            }
        }

        return intent
    }

    /**
     * Load image from URL
     */
    private suspend fun loadImageFromUrl(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.connect()
                val inputStream = connection.getInputStream()
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                android.util.Log.e("NotificationHelper", "Error loading image: ${e.message}")
                null
            }
        }
    }

    /**
     * Cancel a notification
     */
    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    /**
     * Cancel all notifications
     */
    fun cancelAllNotifications(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    enum class NotificationType {
        DEFAULT,
        CHALLENGE,
        ALERT
    }
}

