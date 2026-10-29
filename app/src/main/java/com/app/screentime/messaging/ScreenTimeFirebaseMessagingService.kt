package com.app.screentime.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.app.screentime.MainActivity
import com.app.screentime.R
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.network.session.FcmTokenManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.call.service.CallNotificationHelper
import com.app.screentime.feature.chat.data.local.LocalChatStorage
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class NotificationType(val eventName: String) {
    CHAT_MESSAGE("chat_message_received"),
    INCOMING_CALL("incoming_call"),
    CALL_CANCELLED("call_cancelled"),
    MESSAGE_REQUEST("incoming_message_request"),
    REQUEST_ACCEPTED("message_request_accepted"),
    NEW_LIKE("like_received"),
    NEW_MATCH("match_success"),
    WALLET_CREDITED("wallet_credited");

    companion object {
        fun from(eventOrType: String?): NotificationType {
            if (eventOrType.isNullOrBlank()) return CHAT_MESSAGE
            return when (eventOrType.lowercase().trim()) {
                "chat_message_received", "chat_message", "new_message", "message" -> CHAT_MESSAGE
                "incoming_call", "call", "voice_call", "video_call" -> INCOMING_CALL
                "call_cancelled", "call_canceled", "call_ended", "call_rejected", "call_timeout" -> CALL_CANCELLED
                "incoming_message_request", "message_request", "new_message_request" -> MESSAGE_REQUEST
                "message_request_accepted", "request_accepted" -> REQUEST_ACCEPTED
                "like_received", "new_like", "like" -> NEW_LIKE
                "match_success", "new_match", "match" -> NEW_MATCH
                "wallet_credited", "wallet_recharge", "credit_added" -> WALLET_CREDITED
                else -> CHAT_MESSAGE
            }
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FcmServiceEntryPoint {
    fun fcmTokenManager(): FcmTokenManager
    fun localChatStorage(): LocalChatStorage
    fun sessionManager(): SessionManager
    fun notificationDao(): com.app.screentime.feature.profile.data.local.NotificationDao
}

class ScreenTimeFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        const val MSG_CHANNEL_ID = "chatty_msg_channel"
        const val SOCIAL_CHANNEL_ID = "chatty_social_channel"
        const val WALLET_CHANNEL_ID = "chatty_wallet_channel"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed FCM token: $token")
        try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                FcmServiceEntryPoint::class.java
            )
            entryPoint.fcmTokenManager().syncFcmTokenAsync(token)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync refreshed FCM token", e)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        val eventStr = data["event"] ?: data["type"] ?: data["action"] ?: ""
        val notificationType = NotificationType.from(eventStr)
        Log.d(TAG, "📩 FCM Message received: event='$eventStr' -> resolved type=$notificationType, data=$data")

        when (notificationType) {
            NotificationType.CHAT_MESSAGE -> handleChatMessage(remoteMessage, data)
            NotificationType.INCOMING_CALL -> {
                val callId = data["call_id"] ?: data["session_id"] ?: ""
                val callerId = data["caller_id"] ?: data["sender_id"] ?: ""
                val callerName = data["caller_name"] ?: data["sender_name"] ?: remoteMessage.notification?.title ?: "Incoming Call"
                val callType = data["call_type"] ?: "voice"
                CallNotificationHelper.createChannels(applicationContext)
                CallNotificationHelper.showIncomingCallNotification(
                    context = applicationContext,
                    callId = callId,
                    callerId = callerId,
                    callerName = callerName,
                    callType = callType
                )
            }
            NotificationType.CALL_CANCELLED -> {
                CallNotificationHelper.cancelIncomingCallNotification(applicationContext)
            }
            NotificationType.MESSAGE_REQUEST -> handleMessageRequest(remoteMessage, data)
            NotificationType.REQUEST_ACCEPTED -> handleRequestAccepted(remoteMessage, data)
            NotificationType.NEW_LIKE -> handleNewLike(remoteMessage, data)
            NotificationType.NEW_MATCH -> handleNewMatch(remoteMessage, data)
            NotificationType.WALLET_CREDITED -> handleWalletCredited(remoteMessage, data)
        }
    }

    // ── 1. CHAT_MESSAGE (chat_message_received) ──────────────────────────────
    private fun handleChatMessage(remoteMessage: RemoteMessage, data: Map<String, String>) {
        createChannels()
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            FcmServiceEntryPoint::class.java
        )
        val myUserId = entryPoint.sessionManager().userId.orEmpty().trim()

        val rawSenderId = (data["sender_id"] ?: data["from_user_id"] ?: data["user_id"] ?: "").trim()
        val rawReceiverId = (data["receiver_id"] ?: data["to_user_id"] ?: data["target_id"] ?: "").trim()

        // 1. NEVER show notification or overwrite Room DB for our own sent messages!
        val isFromMe = myUserId.isNotBlank() && (
            rawSenderId.equals(myUserId, ignoreCase = true) ||
            data["sender_id"]?.trim()?.equals(myUserId, ignoreCase = true) == true ||
            data["from_user_id"]?.trim()?.equals(myUserId, ignoreCase = true) == true
        )
        if (isFromMe) {
            Log.d(TAG, "🚫 Skipping FCM notification & Room overwrite for own message (myUserId=$myUserId, sender=$rawSenderId)")
            return
        }

        val partnerId = when {
            rawSenderId.isNotBlank() && !rawSenderId.equals(myUserId, ignoreCase = true) -> rawSenderId
            rawReceiverId.isNotBlank() && !rawReceiverId.equals(myUserId, ignoreCase = true) -> rawReceiverId
            else -> (data["partner_id"] ?: rawSenderId.ifBlank { rawReceiverId }).trim()
        }

        val senderName = data["sender_name"] ?: data["name"] ?: remoteMessage.notification?.title ?: "New Message"
        val content = data["content"] ?: data["message"] ?: data["text"] ?: data["body"] ?: remoteMessage.notification?.body ?: "You have a new message"
        val conversationId = data["conversation_id"] ?: ""
        val messageId = data["id"] ?: data["message_id"] ?: "msg_${System.currentTimeMillis()}"

        if (partnerId.isNotBlank() && content.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val receiverId = if (rawReceiverId.isNotBlank()) rawReceiverId else myUserId
                    entryPoint.localChatStorage().saveMessage(
                        partnerId = partnerId,
                        message = ChatMessage(
                            id = messageId,
                            senderId = partnerId,
                            receiverId = receiverId,
                            conversationId = conversationId.ifBlank { null },
                            text = content,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    Log.d(TAG, "💾 Saved background FCM chat message to Room DB for partner: $partnerId")
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to persist FCM chat message to Room: ${e.message}")
                }
            }
        }

        saveNotificationToRoom(
            title = senderName,
            message = content,
            category = "Messages",
            avatarUrl = data["sender_avatar"] ?: data["avatar_url"],
            iconResName = "ic_chat",
            targetScreen = "chat",
            targetId = partnerId
        )

        // 2. If user is currently looking at this active chat screen, do not show heads-up banner
        val currentActive = com.app.screentime.core.model.ActiveChatTracker.activePartnerId
        if (currentActive != null && currentActive.equals(partnerId, ignoreCase = true)) {
            Log.d(TAG, "Active chat screen already open for partner $partnerId. Suppressing notification banner.")
            return
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "chat")
            putExtra("partner_id", partnerId)
            putExtra("partner_name", senderName)
            putExtra("conversation_id", conversationId)
            for ((key, value) in data) putExtra(key, value)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            if (partnerId.isNotBlank()) partnerId.hashCode() else 1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = getAppIconBitmap()

        val notification = NotificationCompat.Builder(this, MSG_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setColor(ContextCompat.getColor(this, R.color.splash_auth_background))
            .setContentTitle(senderName)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifId = if (partnerId.isNotBlank()) Math.abs(partnerId.hashCode()) else (System.currentTimeMillis() % 10000).toInt()
        notificationManager.notify(notifId, notification)
    }

    // ── 2. MESSAGE_REQUEST (incoming_message_request) ─────────────────────────
    private fun handleMessageRequest(remoteMessage: RemoteMessage, data: Map<String, String>) {
        createChannels()
        val senderName = data["sender_name"] ?: data["name"] ?: "Someone"
        val title = remoteMessage.notification?.title ?: "📩 New Message Request"
        val body = remoteMessage.notification?.body ?: (data["content"] ?: "$senderName sent you a message request.")

        saveNotificationToRoom(
            title = title,
            message = body,
            category = "Messages",
            avatarUrl = data["sender_avatar"] ?: data["avatar_url"],
            iconResName = "ic_chat",
            targetScreen = "chats",
            targetId = data["sender_id"]
        )

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "chats")
            for ((k, v) in data) putExtra(k, v)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            1004,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = getAppIconBitmap()

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setColor(ContextCompat.getColor(this, R.color.splash_auth_background))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1004, notification)
    }

    // ── 5. REQUEST_ACCEPTED (message_request_accepted) ───────────────────────
    private fun handleRequestAccepted(remoteMessage: RemoteMessage, data: Map<String, String>) {
        createChannels()
        val senderId = data["sender_id"] ?: data["partner_id"] ?: ""
        val senderName = data["sender_name"] ?: data["name"] ?: "Your match"
        val title = remoteMessage.notification?.title ?: "🔔 Request Accepted"
        val body = remoteMessage.notification?.body ?: "$senderName accepted your message request! Start chatting now."

        saveNotificationToRoom(
            title = title,
            message = body,
            category = "Messages",
            avatarUrl = data["sender_avatar"] ?: data["avatar_url"],
            iconResName = "ic_chat",
            targetScreen = "chat",
            targetId = senderId
        )

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "chat")
            putExtra("partner_id", senderId)
            putExtra("partner_name", senderName)
            for ((k, v) in data) putExtra(k, v)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            1005,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = getAppIconBitmap()

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setColor(ContextCompat.getColor(this, R.color.splash_auth_background))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1005, notification)
    }

    // ── 6. NEW_LIKE (like_received) ───────────────────────────────────────────
    private fun handleNewLike(remoteMessage: RemoteMessage, data: Map<String, String>) {
        createChannels()
        val name = data["name"] ?: data["sender_name"] ?: "Someone"
        val title = remoteMessage.notification?.title ?: "💖 Someone liked your profile!"
        val body = remoteMessage.notification?.body ?: (data["message"] ?: "$name liked you. Discover them in your feed!")

        saveNotificationToRoom(
            title = title,
            message = body,
            category = "Activity",
            avatarUrl = data["avatar_url"] ?: data["sender_avatar"],
            iconResName = "ic_heart",
            targetScreen = "discover",
            targetId = data["user_id"]
        )

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "discover")
            for ((k, v) in data) putExtra(k, v)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            1006,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = getAppIconBitmap()

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setColor(ContextCompat.getColor(this, R.color.splash_auth_background))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1006, notification)
    }

    // ── 7. NEW_MATCH (match_success) ──────────────────────────────────────────
    private fun handleNewMatch(remoteMessage: RemoteMessage, data: Map<String, String>) {
        createChannels()
        val partnerId = data["partner_id"] ?: data["user_id"] ?: ""
        val partnerName = data["partner_name"] ?: data["name"] ?: "Someone"
        val title = remoteMessage.notification?.title ?: "🎉 It's a Match!"
        val body = remoteMessage.notification?.body ?: "You and $partnerName liked each other! Start talking now."

        saveNotificationToRoom(
            title = title,
            message = body,
            category = "Matches",
            avatarUrl = data["avatar_url"] ?: data["sender_avatar"],
            iconResName = "ic_zap",
            targetScreen = "chat",
            targetId = partnerId
        )

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "chat")
            putExtra("partner_id", partnerId)
            putExtra("partner_name", partnerName)
            for ((k, v) in data) putExtra(k, v)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            1007,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = getAppIconBitmap()

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setColor(ContextCompat.getColor(this, R.color.splash_auth_background))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1007, notification)
    }

    // ── 8. WALLET_CREDITED (wallet_credited) ──────────────────────────────────
    private fun handleWalletCredited(remoteMessage: RemoteMessage, data: Map<String, String>) {
        createChannels()
        val amount = data["amount"] ?: data["coins"] ?: ""
        val title = remoteMessage.notification?.title ?: "💰 Wallet Credited"
        val body = remoteMessage.notification?.body ?: if (amount.isNotBlank()) "$amount coins have been added to your wallet!" else "Coins have been credited to your wallet."

        saveNotificationToRoom(
            title = title,
            message = body,
            category = "Activity",
            iconResName = "coin_icon",
            targetScreen = "wallet"
        )

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "wallet")
            for ((k, v) in data) putExtra(k, v)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            1008,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val appIcon = getAppIconBitmap()

        val notification = NotificationCompat.Builder(this, WALLET_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .apply {
                if (appIcon != null) {
                    setLargeIcon(appIcon)
                }
            }
            .setColor(ContextCompat.getColor(this, R.color.splash_auth_background))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1008, notification)
    }

    private fun getAppIconBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeResource(resources, R.drawable.app_icon)
        } catch (e: Exception) {
            null
        }
    }

    private fun saveNotificationToRoom(
        title: String,
        message: String,
        category: String,
        actionText: String? = null,
        avatarUrl: String? = null,
        iconResName: String? = null,
        targetScreen: String? = null,
        targetId: String? = null
    ) {
        try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                FcmServiceEntryPoint::class.java
            )
            val dao = entryPoint.notificationDao()
            CoroutineScope(Dispatchers.IO).launch {
                val entity = com.app.screentime.feature.profile.data.local.NotificationEntity(
                    id = "notif_${System.currentTimeMillis()}_${(1000..9999).random()}",
                    title = title,
                    message = message,
                    category = category,
                    timestamp = System.currentTimeMillis(),
                    isUnread = true,
                    actionText = actionText,
                    avatarUrl = avatarUrl,
                    iconResName = iconResName,
                    targetScreen = targetScreen,
                    targetId = targetId
                )
                dao.insertNotification(entity)
                Log.d(TAG, "💾 Saved FCM notification to Room DB: $title")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save FCM notification to Room DB", e)
        }
    }

    // ── Notification Channels Setup ───────────────────────────────────────────
    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. Chat Messages Channel
            val msgChannel = NotificationChannel(
                MSG_CHANNEL_ID,
                "Chat Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for incoming chat messages"
                enableVibration(true)
            }

            // 2. Social Channel (Likes, Matches, Requests)
            val socialChannel = NotificationChannel(
                SOCIAL_CHANNEL_ID,
                "Likes & Matches",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for likes, matches, and message requests"
                enableVibration(true)
            }

            // 3. Wallet Channel
            val walletChannel = NotificationChannel(
                WALLET_CHANNEL_ID,
                "Wallet & Credits",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for wallet balance updates and coin recharges"
            }

            manager.createNotificationChannels(listOf(msgChannel, socialChannel, walletChannel))
        }
    }
}
