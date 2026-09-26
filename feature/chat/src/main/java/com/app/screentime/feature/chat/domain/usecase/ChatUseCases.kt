package com.app.screentime.feature.chat.domain.usecase

import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.network.api.MessagesApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.WinterWebSocketClient
import com.app.screentime.core.network.websocket.WSMessage
import com.app.screentime.feature.chat.data.local.LocalChatStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import java.util.UUID
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messagesApi: MessagesApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val wsClient: WinterWebSocketClient,
    private val localStorage: LocalChatStorage
) {
    suspend operator fun invoke(
        receiverId: String,
        text: String,
        conversationId: String? = null
    ): Result<ChatMessage> {
        val trimmed = text.trim()
        if (trimmed.isBlank()) {
            return Result.failure(IllegalArgumentException("Message content cannot be blank"))
        }

        val token = preferencesManager.getToken().orEmpty()
        val myUserId = preferencesManager.getUserId() ?: "user"
        val timestamp = System.currentTimeMillis()

        // 1. Optimistic Local Save in Room DB with unique ID
        val localId = "msg_${timestamp}_${UUID.randomUUID().toString().take(8)}"
        val localMsg = ChatMessage(
            id = localId,
            senderId = myUserId,
            receiverId = receiverId,
            text = trimmed,
            timestamp = timestamp,
            conversationId = conversationId,
            isSending = true,
            isFailed = false
        )
        localStorage.saveMessage(receiverId, localMsg)

        // 2. Real-time WebSocket Send (Client -> Server)
        try {
            wsClient.sendChatMessage(targetId = receiverId, content = trimmed, conversationId = conversationId)
        } catch (e: Exception) {
            // Ignore socket failure and proceed with REST
        }

        // 3. HTTP POST /api/messages/send
        val targetConvId = conversationId?.takeIf { it.isNotBlank() } ?: "conv_${receiverId}"
        return try {
            val response = messagesApi.sendMessage(
                token = token,
                conversationId = targetConvId,
                content = trimmed,
                userId = myUserId
            )
            if (response.success && response.data != null) {
                val dto = response.data!!
                val parsedTime = try {
                    if (!dto.created_at.isNullOrBlank()) {
                        java.time.Instant.parse(dto.created_at).toEpochMilli()
                    } else {
                        timestamp
                    }
                } catch (e: Exception) {
                    timestamp
                }
                val confirmedMsg = ChatMessage(
                    id = dto.id.ifBlank { localId },
                    senderId = dto.sender_id.ifBlank { myUserId },
                    receiverId = dto.receiver_id?.ifBlank { receiverId } ?: receiverId,
                    text = dto.content.ifBlank { trimmed },
                    timestamp = parsedTime,
                    conversationId = dto.conversation_id.ifBlank { targetConvId },
                    isSending = false,
                    isFailed = false
                )
                localStorage.replaceOrSaveMessage(receiverId, localId, confirmedMsg)
                Result.success(confirmedMsg)
            } else {
                val failedMsg = localMsg.copy(isSending = false, isFailed = true)
                localStorage.replaceOrSaveMessage(receiverId, localId, failedMsg)
                Result.failure(Exception(response.message.ifBlank { "Failed to send message" }))
            }
        } catch (e: Exception) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, localId, failedMsg)
            Result.failure(e)
        }
    }

    private fun resolveUserId(token: String): String? {
        val uid = sessionManager.userId ?: preferencesManager.getUserId()
        if (!uid.isNullOrBlank()) return uid
        if (token.startsWith("token_")) {
            return token.removePrefix("token_")
        }
        return null
    }
}

class GetMessagesUseCase @Inject constructor(
    private val messagesApi: MessagesApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val localStorage: LocalChatStorage
) {
    fun getMessagesFlow(partnerId: String): Flow<List<ChatMessage>> {
        return localStorage.getMessagesFlow(partnerId)
    }

    suspend operator fun invoke(partnerId: String, conversationId: String? = null): List<ChatMessage> {
        val local = localStorage.getMessages(partnerId)
        val token = preferencesManager.getToken().orEmpty()
        val userId = preferencesManager.getUserId()

        if (token.isBlank()) return local

        val remoteList = mutableListOf<ChatMessage>()

        // Fetch from REST /api/messages/conversations/{conversation_id} if available
        if (!conversationId.isNullOrBlank()) {
            try {
                val res = messagesApi.getConversationMessages(
                    token = token,
                    conversationId = conversationId,
                    userId = userId
                )
                val msgs = res.getMessageList()
                if (msgs.isNotEmpty()) {
                    for (dto in msgs) {
                        if (dto.content.isBlank()) continue
                        val parsedTime = try {
                            if (!dto.created_at.isNullOrBlank()) {
                                java.time.Instant.parse(dto.created_at).toEpochMilli()
                            } else {
                                System.currentTimeMillis()
                            }
                        } catch (e: Exception) {
                            System.currentTimeMillis()
                        }
                        remoteList.add(
                            ChatMessage(
                                id = dto.id.ifBlank { "msg_${parsedTime}_${UUID.randomUUID().toString().take(6)}" },
                                senderId = dto.sender_id,
                                receiverId = dto.receiver_id ?: if (dto.sender_id == userId) partnerId else (userId ?: ""),
                                text = dto.content,
                                timestamp = parsedTime,
                                conversationId = conversationId,
                                isSending = false,
                                isFailed = false
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                // Fallback to local
            }
        }

        if (remoteList.isNotEmpty()) {
            localStorage.saveMessages(partnerId, remoteList)
            return localStorage.getMessages(partnerId)
        }

        return local
    }

    private fun resolveUserId(token: String): String? {
        val uid = sessionManager.userId ?: preferencesManager.getUserId()
        if (!uid.isNullOrBlank()) return uid
        if (token.startsWith("token_")) {
            return token.removePrefix("token_")
        }
        return null
    }
}

class ObserveMessagesUseCase @Inject constructor(
    private val wsClient: WinterWebSocketClient,
    private val localStorage: LocalChatStorage,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    val currentUserId: String
        get() = sessionManager.userId ?: preferencesManager.getUserId() ?: "user"

    operator fun invoke(): SharedFlow<WSMessage> {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        return wsClient.eventsFlow
    }

    suspend fun saveIncoming(partnerId: String, msg: ChatMessage) {
        if (msg.text.isNotBlank()) {
            localStorage.saveMessage(partnerId, msg)
        }
    }
}

class GetConversationsUseCase @Inject constructor(
    private val messagesApi: MessagesApi,
    private val preferencesManager: PreferencesManager,
    private val localStorage: LocalChatStorage
) {
    suspend operator fun invoke(): List<Conversation> {
        val token = preferencesManager.getToken().orEmpty()
        val userId = preferencesManager.getUserId()

        val list = mutableListOf<Conversation>()

        try {
            val response = messagesApi.getConversations(
                token = token,
                page = 1,
                limit = 20,
                userId = userId
            )
            val items = response.getItems()
            for (item in items) {
                val partnerId = item.partner_user_id
                if (partnerId.isBlank()) continue

                val serverTime = item.last_message_at?.let { timeStr ->
                    try {
                        java.time.Instant.parse(timeStr).toEpochMilli()
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }
                } ?: System.currentTimeMillis()

                val latestLocal = localStorage.getLatestMessage(partnerId)
                val (msg, time) = if (latestLocal != null && latestLocal.timestamp >= serverTime) {
                    latestLocal.text to latestLocal.timestamp
                } else {
                    (item.last_message_text ?: "") to serverTime
                }

                list.add(
                    Conversation(
                        id = item.id.ifBlank { "conv_$partnerId" },
                        modelId = partnerId,
                        modelName = item.partner_name,
                        modelAvatarUrl = item.partner_photo_url.orEmpty(),
                        lastMessage = msg,
                        lastMessageTime = time,
                        unreadCount = item.unread_count,
                        isOnline = false
                    )
                )
            }
        } catch (e: Exception) {
            // Handle error / return current list
        }

        // Sort by most recent conversation first (active chats at top)
        return list.sortedByDescending { it.lastMessageTime }
    }
}
