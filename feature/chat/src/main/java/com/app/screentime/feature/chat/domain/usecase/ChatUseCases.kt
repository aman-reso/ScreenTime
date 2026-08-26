package com.app.screentime.feature.chat.domain.usecase

import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.ChattyWebSocketClient
import com.app.screentime.core.network.websocket.WSMessage
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(receiverId: String, text: String) {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        wsClient.sendChatMessage(receiverId, text)
    }
}

class ObserveMessagesUseCase @Inject constructor(
    private val wsClient: ChattyWebSocketClient
) {
    operator fun invoke(): SharedFlow<WSMessage> {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        return wsClient.eventsFlow
    }
}

class GetConversationsUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): List<Conversation> {
        val token = sessionManager.token ?: ""
        return try {
            val roomsResponse = api.getRooms(token)
            if (roomsResponse.rooms.isNotEmpty()) {
                roomsResponse.rooms.map { room ->
                    Conversation(
                        id = room.id,
                        modelId = room.model_id,
                        modelName = room.title.ifBlank { "Room ${room.id.take(4)}" },
                        modelAvatarUrl = "",
                        lastMessage = if (room.is_live) "🔴 Live Room" else "Tap to chat",
                        lastMessageTime = System.currentTimeMillis(),
                        unreadCount = 0,
                        isOnline = room.is_live
                    )
                }
            } else {
                // Fetch models to show start chat conversations
                val modelsResponse = api.getModels(token)
                modelsResponse.models.map { model ->
                    Conversation(
                        id = "conv_${model.id}",
                        modelId = model.id,
                        modelName = model.name,
                        modelAvatarUrl = model.avatar_url ?: "",
                        lastMessage = model.bio ?: "Say hello! 👋",
                        lastMessageTime = System.currentTimeMillis(),
                        unreadCount = 0,
                        isOnline = model.is_online
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
