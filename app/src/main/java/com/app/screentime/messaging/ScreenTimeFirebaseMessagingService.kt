package com.app.screentime.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.screentime.MainActivity
import com.app.screentime.R
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.network.session.FcmTokenManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.call.ActiveCallManager
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
    fun activeCallManager(): ActiveCallManager
    fun sessionManager(): SessionManager
}

class ScreenTimeFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        const val CALL_CHANNEL_ID = "chatty_call_channel"
        const val MSG_CHANNEL_ID = "chatty_msg_channel"
        const val SOCIAL_CHANNEL_ID = "chatty_social_channel"
        const val WALLET_CHANNEL_ID = "chatty_wallet_channel"

        const val CALL_NOTIFICATION_ID = 2001
        const val ACTION_ACCEPT_CALL = "com.app.screentime.ACTION_ACCEPT_CALL"
        const val ACTION_REJECT_CALL = "com.app.screentime.ACTION_REJECT_CALL"
        const val EXTRA_CALL_ID = "call_id"
        const val EXTRA_CALLER_ID = "caller_id"
        const val EXTRA_CALLER_NAME = "caller_name"
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
            NotificationType.INCOMING_CALL -> handleIncomingCall(remoteMessage, data)
            NotificationType.CALL_CANCELLED -> handleCallCancelled(data)
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
        val senderId = data["sender_id"] ?: data["partner_id"] ?: data["from_user_id"] ?: ""
        val senderName = data["sender_name"] ?: data["name"] ?: remoteMessage.notification?.title ?: "New Message"
        val content = data["content"] ?: data["message"] ?: data["text"] ?: data["body"] ?: remoteMessage.notification?.body ?: "You have a new message"
        val conversationId = data["conversation_id"] ?: ""
        val messageId = data["id"] ?: data["message_id"] ?: "msg_${System.currentTimeMillis()}"
        if (senderId.isNotBlank() && content.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val entryPoint = EntryPointAccessors.fromApplication(
                        applicationContext,
                        FcmServiceEntryPoint::class.java
                    )
                    val receiverId = data["receiver_id"] ?: entryPoint.sessionManager().userId.orEmpty()
                    entryPoint.localChatStorage().saveMessage(
                        partnerId = senderId,
                        message = ChatMessage(
                            id = messageId,
                            senderId = senderId,
                            receiverId = receiverId,
                            conversationId = conversationId.ifBlank { null },
                            text = content,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    Log.d(TAG, "💾 Saved background FCM chat message to Room DB for partner: $senderId")
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to persist FCM chat message to Room: ${e.message}")
                }
            }
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("screen", "chat")
            putExtra("partner_id", senderId)
            putExtra("partner_name", senderName)
            putExtra("conversation_id", conversationId)
            for ((key, value) in data) putExtra(key, value)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            if (senderId.isNotBlank()) senderId.hashCode() else 1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, MSG_CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon_round)
            .setContentTitle(senderName)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifId = if (senderId.isNotBlank()) Math.abs(senderId.hashCode()) else (System.currentTimeMillis() % 10000).toInt()
        notificationManager.notify(notifId, notification)
    }

    // ── 2. INCOMING_CALL (incoming_call) ──────────────────────────────────────
    private fun handleIncomingCall(remoteMessage: RemoteMessage, data: Map<String, String>) {
        val callId = data["call_id"] ?: System.currentTimeMillis().toString()
        val callerId = data["caller_id"] ?: data["sender_id"] ?: "0"
        val callerName = data["caller_name"] ?: remoteMessage.notification?.title ?: "Incoming Call"
        showIncomingCallNotification(callId, callerId, callerName)
    }

    private fun showIncomingCallNotification(callId: String, callerId: String, callerName: String) {
        createChannels()

        // 1. Accept Intent (opens app directly to VoiceCallScreen)
        val acceptIntent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_ACCEPT_CALL
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_CALL_ID, callId)
            putExtra(EXTRA_CALLER_ID, callerId)
            putExtra(EXTRA_CALLER_NAME, callerName)
        }
        val acceptPendingIntent = PendingIntent.getActivity(
            this,
            101,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 2. Reject Intent (broadcast to dismiss notification and reject)
        val rejectIntent = Intent(this, RejectCallReceiver::class.java).apply {
            action = ACTION_REJECT_CALL
            putExtra("call_id", callId)
            putExtra("notification_id", CALL_NOTIFICATION_ID)
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(
            this,
            102,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, CALL_CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon_round)
            .setContentTitle("Incoming Voice Call")
            .setContentText("$callerName is calling you…")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(true)
            .setOngoing(true)
            .setSound(ringtoneUri)
            .setVibrate(longArrayOf(0, 800, 500, 800, 500, 800))
            .setFullScreenIntent(acceptPendingIntent, true)
            .setContentIntent(acceptPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Decline", rejectPendingIntent)
            .addAction(android.R.drawable.ic_menu_call, "Accept", acceptPendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(CALL_NOTIFICATION_ID, notificationBuilder.build())
    }

    // ── 3. CALL_CANCELLED (call_cancelled / call_ended) ───────────────────────
    private fun handleCallCancelled(data: Map<String, String>) {
        Log.i(TAG, "🔇 Call cancelled data push: dismissing incoming call notification")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(CALL_NOTIFICATION_ID)

        try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                FcmServiceEntryPoint::class.java
            )
            entryPoint.activeCallManager().hangup()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to hangup active call via EntryPoint: ${e.message}")
        }
    }

    // ── 4. MESSAGE_REQUEST (incoming_message_request) ─────────────────────────
    private fun handleMessageRequest(remoteMessage: RemoteMessage, data: Map<String, String>) {
        createChannels()
        val senderName = data["sender_name"] ?: data["name"] ?: "Someone"
        val title = remoteMessage.notification?.title ?: "📩 New Message Request"
        val body = remoteMessage.notification?.body ?: (data["content"] ?: "$senderName sent you a message request.")

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

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon_round)
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

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon_round)
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

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon_round)
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

        val notification = NotificationCompat.Builder(this, SOCIAL_CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon_round)
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

        val notification = NotificationCompat.Builder(this, WALLET_CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_icon_round)
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

    // ── Notification Channels Setup ───────────────────────────────────────────
    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. Voice Calls Channel
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()

            val callChannel = NotificationChannel(
                CALL_CHANNEL_ID,
                "Incoming Voice Calls",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High-priority notifications for incoming voice and video calls"
                setSound(ringtoneUri, audioAttributes)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 800, 500, 800, 500, 800)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            // 2. Chat Messages Channel
            val msgChannel = NotificationChannel(
                MSG_CHANNEL_ID,
                "Chat Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for incoming chat messages"
                enableVibration(true)
            }

            // 3. Social Channel (Likes, Matches, Requests)
            val socialChannel = NotificationChannel(
                SOCIAL_CHANNEL_ID,
                "Likes & Matches",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for likes, matches, and message requests"
                enableVibration(true)
            }

            // 4. Wallet Channel
            val walletChannel = NotificationChannel(
                WALLET_CHANNEL_ID,
                "Wallet & Credits",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for wallet balance updates and coin recharges"
            }

            manager.createNotificationChannels(listOf(callChannel, msgChannel, socialChannel, walletChannel))
        }
    }
}
