package com.app.screentime.feature.chat.domain.usecase

import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.ChattyWebSocketClient
import com.app.screentime.core.network.websocket.WSMessage
import com.app.screentime.feature.chat.data.local.LocalChatStorage
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager,
    private val wsClient: ChattyWebSocketClient,
    private val localStorage: LocalChatStorage
) {
    suspend operator fun invoke(receiverId: String, text: String): Result<ChatMessage> {
        val token = sessionManager.token ?: ""
        val myUserId = sessionManager.userId ?: "user"
        val timestamp = System.currentTimeMillis()

        // 1. Optimistic Local Save
        val localMsg = ChatMessage(
            id = "msg_${timestamp}",
            senderId = myUserId,
            receiverId = receiverId,
            text = text,
            timestamp = timestamp
        )
        localStorage.saveMessage(receiverId, localMsg)

        // 2. WebSocket Push
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        wsClient.sendChatMessage(receiverId, text)

        // 3. Persistent Server Sync
        return try {
            val dto = api.sendChatMessage(token, receiverId, text)
            val serverMsg = ChatMessage(
                id = dto.id.ifBlank { localMsg.id },
                senderId = dto.sender_id.ifBlank { myUserId },
                receiverId = dto.receiver_id.ifBlank { receiverId },
                text = dto.content.ifBlank { text },
                timestamp = timestamp
            )
            localStorage.saveMessage(receiverId, serverMsg)
            Result.success(serverMsg)
        } catch (e: Exception) {
            Result.success(localMsg)
        }
    }
}

class GetMessagesUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager,
    private val localStorage: LocalChatStorage
) {
    suspend operator fun invoke(partnerId: String): List<ChatMessage> {
        localStorage.purgeExpired()
        val local = localStorage.getMessages(partnerId)
        val token = sessionManager.token ?: return local

        return try {
            val res = api.getChatMessages(token, partnerId)
            if (res.messages.isNotEmpty()) {
                val remote = res.messages.map { dto ->
                    ChatMessage(
                        id = dto.id,
                        senderId = dto.sender_id,
                        receiverId = dto.receiver_id,
                        text = dto.content,
                        timestamp = System.currentTimeMillis()
                    )
                }
                localStorage.saveMessages(partnerId, remote)
                localStorage.getMessages(partnerId)
            } else {
                local
            }
        } catch (e: Exception) {
            local
        }
    }
}

class ObserveMessagesUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient,
    private val localStorage: LocalChatStorage
) {
    operator fun invoke(): SharedFlow<WSMessage> {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        return wsClient.eventsFlow
    }

    fun saveIncoming(partnerId: String, msg: ChatMessage) {
        localStorage.saveMessage(partnerId, msg)
    }
}

class GetConversationsUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager,
    private val localStorage: LocalChatStorage
) {
    suspend operator fun invoke(): List<Conversation> {
        localStorage.purgeExpired()
        val token = sessionManager.token ?: ""

        val list = mutableListOf<Conversation>()
        val seenPartnerIds = mutableSetOf<String>()

        try {
            // 1. Load active conversations from server
            val myId = sessionManager.userId ?: ""
            val res = api.getConversations(token)
            for (dto in res.conversations) {
                val partnerId = dto.getResolvedPartnerId(myId)
                if (partnerId.isBlank()) continue
                val partnerName = dto.getResolvedPartnerName()
                seenPartnerIds.add(partnerId)
                val latestLocal = localStorage.getLatestMessage(partnerId)
                val (msg, time) = if (latestLocal != null && latestLocal.timestamp >= dto.last_message_time) {
                    latestLocal.text to latestLocal.timestamp
                } else {
                    dto.last_message to dto.last_message_time
                }
                list.add(
                    Conversation(
                        id = dto.id.ifBlank { "conv_$partnerId" },
                        modelId = partnerId,
                        modelName = partnerName,
                        modelAvatarUrl = dto.partner_avatar.ifBlank { dto.avatar_url },
                        lastMessage = msg,
                        lastMessageTime = time,
                        unreadCount = dto.unread_count,
                        isOnline = dto.is_online
                    )
                )
            }

            // 2. Discover/Models feed for start-chat conversations & local-only chats
            val modelsRes = api.getModels(token)
            for (model in modelsRes.models) {
                if (!seenPartnerIds.contains(model.id)) {
                    val latestLocal = localStorage.getLatestMessage(model.id)
                    val (msg, time) = if (latestLocal != null) {
                        latestLocal.text to latestLocal.timestamp
                    } else {
                        (model.bio ?: "Say hello! 👋") to 0L
                    }
                    list.add(
                        Conversation(
                            id = "conv_${model.id}",
                            modelId = model.id,
                            modelName = model.name,
                            modelAvatarUrl = model.avatar_url ?: "",
                            lastMessage = msg,
                            lastMessageTime = time,
                            unreadCount = 0,
                            isOnline = model.is_online
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // Offline fallback: load from models cache
        }

        // Sort by most recent conversation first (active chats at top)
        return list.sortedByDescending { it.lastMessageTime }
    }
}
