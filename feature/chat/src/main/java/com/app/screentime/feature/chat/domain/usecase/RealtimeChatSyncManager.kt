package com.app.screentime.feature.chat.domain.usecase

import android.util.Log
import com.app.screentime.core.model.ChatMessage
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
    private val preferencesManager: PreferencesManager
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
                val isChatEvent = msg.type.equals(WSEventTypes.CHAT_MESSAGE, ignoreCase = true) ||
                        msg.event.equals(WSEventTypes.CHAT_MESSAGE_RECEIVED, ignoreCase = true) ||
                        msg.event.equals(WSEventTypes.NEW_MESSAGE, ignoreCase = true) ||
                        msg.event.equals("chat_message_received", ignoreCase = true) ||
                        msg.event.equals("chat_message", ignoreCase = true) ||
                        msg.event.equals("new_message", ignoreCase = true)

                if (isChatEvent) {
                    val myId = sessionManager.userId
                        ?: preferencesManager.getUserId()
                        ?: "user"

                    val sender = msg.effectiveSenderId ?: msg.sender_id ?: msg.caller_id ?: msg.user_id ?: ""
                    val target = msg.effectiveTargetId ?: msg.target_id ?: msg.receiver_id ?: ""
                    val content = (msg.content?.takeIf { it.isNotBlank() }
                        ?: msg.message?.takeIf { it.isNotBlank() }
                        ?: msg.payloadAsString()).trim()

                    if (content.isBlank()) return@collect

                    // The partner ID is the other user in the conversation
                    val partnerId = when {
                        sender.isNotBlank() && sender != myId -> sender
                        target.isNotBlank() && target != myId -> target
                        else -> sender.ifBlank { target }
                    }

                    if (partnerId.isBlank()) return@collect

                    val msgId = msg.call_id?.takeIf { it.isNotBlank() }
                        ?: "msg_${msg.timestamp ?: System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}"

                    val isFromMe = (sender.isNotBlank() && sender == myId)

                    val chatMessage = ChatMessage(
                        id = msgId,
                        senderId = if (isFromMe) myId else partnerId,
                        receiverId = if (isFromMe) partnerId else myId,
                        text = content,
                        timestamp = msg.timestamp ?: System.currentTimeMillis(),
                        conversationId = msg.conversation_id ?: msg.room_id,
                        isSending = false,
                        isFailed = false
                    )

                    Log.i(tag, "💾 Persisting real-time chat message to Room DB: partner=$partnerId, id=$msgId, text=\"$content\"")
                    localStorage.saveMessage(partnerId, chatMessage)
                }
            }
        }
    }
}
