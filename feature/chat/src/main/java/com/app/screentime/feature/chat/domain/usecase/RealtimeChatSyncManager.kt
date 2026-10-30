package com.app.screentime.feature.chat.domain.usecase

import android.util.Log
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.model.MessageType
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.WSEventTypes
import com.app.screentime.core.network.websocket.WinterWebSocketClient
import com.app.screentime.feature.chat.data.local.LocalChatStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global Real-Time Chat Sync Manager that ensures incoming WebSocket messages
 * (e.g. event "chat_message_received", "new_message", etc.) are immediately persisted
 * to the Room Database regardless of which screen the user is on.
 */
@Singleton
class RealtimeChatSyncManager @Inject constructor(
    private val wsClient: WinterWebSocketClient,
    private val localStorage: LocalChatStorage,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val viewOncePhotoTracker: com.app.screentime.feature.chat.data.local.ViewOncePhotoTracker,
    private val e2eCryptoManager: com.app.screentime.core.network.security.E2ECryptoManager,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager
) {
    private val tag = "RealtimeChatSync"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isListening = false

    init {
        startListening()
    }

    fun ensureConnected() {
        if (!wsClient.isConnected()) {
            Log.i(tag, "🔌 Connecting WebSocket client for real-time chat...")
            wsClient.connect()
        }
        startListening()
    }

    fun startListening() {
        if (isListening) return
        isListening = true
        Log.i(tag, "🚀 Started global real-time chat synchronization loop")

        scope.launch {
            wsClient.eventsFlow.collect { msg ->
                val isAdGateEvent = msg.event.equals("rewarded_ad_gate", ignoreCase = true) ||
                        msg.type.equals("rewarded_ad_gate", ignoreCase = true) ||
                        msg.event.equals("chat_gate_trigger", ignoreCase = true)

                if (isAdGateEvent) {
                    val gateType = (msg.gate_type ?: msg.gateType)?.lowercase()
                    val defaultPrompt = if (gateType == "billing") {
                        "Unlock unlimited messages to continue chatting!"
                    } else {
                        "Watch a short video ad or top up to continue chatting!"
                    }
                    val prompt = (msg.message?.takeIf { it.isNotBlank() }
                        ?: msg.content?.takeIf { it.isNotBlank() }
                        ?: defaultPrompt).trim()
                    val rewardCount = msg.reward_messages ?: 5
                    Log.i(tag, "🔒 Intercepted server Ad/Billing Gate: $prompt (Reward=+$rewardCount msgs, Type=$gateType)")
                    quotaLimitManager.updateChatQuota(
                        com.app.screentime.core.model.QuotaLimitInfo(
                            isLimitReached = true,
                            limitMessage = prompt,
                            rewardMessages = rewardCount,
                            gateType = gateType
                        )
                    )
                    return@collect
                }

                val isAdUnlockedEvent = msg.event.equals("rewarded_ad_unlocked", ignoreCase = true) ||
                        msg.type.equals("rewarded_ad_unlocked", ignoreCase = true) ||
                        msg.event.equals("ad_gate_unlocked", ignoreCase = true) ||
                        msg.type.equals("ad_gate_unlocked", ignoreCase = true) ||
                        msg.event.equals("reward_granted", ignoreCase = true)

                if (isAdUnlockedEvent) {
                    Log.i(tag, "🔓 Server cleared ad gate / unlocked messages!")
                    quotaLimitManager.clearChatQuota()
                    return@collect
                }

                val isChatEvent = msg.type.equals(WSEventTypes.CHAT_MESSAGE, ignoreCase = true) ||
                        msg.event.equals(WSEventTypes.CHAT_MESSAGE_RECEIVED, ignoreCase = true) ||
                        msg.event.equals(WSEventTypes.NEW_MESSAGE, ignoreCase = true) ||
                        msg.event.equals("chat_message_received", ignoreCase = true) ||
                        msg.event.equals("chat_message", ignoreCase = true) ||
                        msg.event.equals("new_message", ignoreCase = true)

                if (isChatEvent) {
                    val rawMyId = sessionManager.userId
                        ?: preferencesManager.getUserId()
                        ?: ""

                    val sender = (msg.effectiveSenderId ?: msg.sender_id ?: msg.caller_id ?: msg.user_id ?: "").trim()
                    val target = (msg.effectiveTargetId ?: msg.target_id ?: msg.receiver_id ?: "").trim()
                    val content = (msg.content?.takeIf { it.isNotBlank() }
                        ?: msg.message?.takeIf { it.isNotBlank() }
                        ?: msg.payloadAsString()).trim()

                    if (content.isBlank()) return@collect

                    val isFromMe = (rawMyId.isNotBlank() && sender.equals(rawMyId, ignoreCase = true))

                    // If message was sent by me, SendMessageUseCase handles optimistic save and confirmation.
                    // Ignore echo to prevent duplicate insertion or race conditions.
                    if (isFromMe) {
                        Log.d(tag, "⏭️ Ignoring WebSocket chat echo for own message (sender=$sender)")
                        return@collect
                    }

                    // The partner ID is always the OTHER party in the 1-on-1 chat
                    val partnerId = when {
                        sender.isNotBlank() && !sender.equals(rawMyId, ignoreCase = true) -> sender
                        target.isNotBlank() && !target.equals(rawMyId, ignoreCase = true) -> target
                        else -> sender.ifBlank { target }
                    }.trim()

                    if (partnerId.isBlank()) {
                        Log.w(tag, "⚠️ Cannot resolve partnerId for chat message: sender=$sender, target=$target, myId=$rawMyId")
                        return@collect
                    }

                    val msgId = msg.call_id?.takeIf { it.isNotBlank() }
                        ?: "msg_${msg.timestamp ?: System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}"

                    val token = preferencesManager.getToken().orEmpty().ifBlank { sessionManager.getToken().orEmpty() }
                    val plainContent = if (e2eCryptoManager.isE2EE(content)) {
                        e2eCryptoManager.decrypt(content, partnerId, token)
                    } else {
                        content
                    }

                    val (resolvedType, resolvedMediaUrl) = parseMessageTypeAndUrl(plainContent)
                    if (resolvedType == MessageType.IMAGE && viewOncePhotoTracker.isViewed(msgId, msg.client_message_id, plainContent)) {
                        Log.d(tag, "⏭️ Ignoring already viewed View Once photo")
                        return@collect
                    }

                    val chatMessage = ChatMessage(
                        id = msgId,
                        senderId = partnerId,
                        receiverId = rawMyId,
                        text = plainContent,
                        timestamp = msg.timestamp ?: System.currentTimeMillis(),
                        conversationId = msg.conversation_id ?: msg.room_id,
                        type = resolvedType,
                        mediaUrl = resolvedMediaUrl,
                        isSending = false,
                        isFailed = false,
                        clientMessageId = msg.client_message_id?.takeIf { it.isNotBlank() }
                            ?: msg.clientMessageId?.takeIf { it.isNotBlank() }
                    )

                    Log.i(tag, "💾 Persisting real-time incoming chat message to Room DB: partner=$partnerId, sender=$sender, id=$msgId, (len=${plainContent.length})")
                    localStorage.saveMessage(partnerId, chatMessage)
                }
            }
        }
    }
}
