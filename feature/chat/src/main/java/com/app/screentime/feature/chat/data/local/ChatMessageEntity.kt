package com.app.screentime.feature.chat.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.model.MessageType

@Entity(
    tableName = "chat_messages",
    indices = [
        Index(value = ["partnerId"]),
        Index(value = ["conversationId"]),
        Index(value = ["timestamp"])
    ]
)
data class ChatMessageEntity(
    @PrimaryKey
    val id: String,
    val partnerId: String,
    val conversationId: String? = null,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val isSending: Boolean = false,
    val isFailed: Boolean = false,
    val mediaUrl: String? = null,
    val type: String = "TEXT"
) {
    fun toDomain(): ChatMessage {
        val msgType = try {
            MessageType.valueOf(type)
        } catch (_: Exception) {
            MessageType.TEXT
        }
        return ChatMessage(
            id = id,
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = timestamp,
            isRead = isRead,
            isSending = isSending,
            isFailed = isFailed,
            conversationId = conversationId,
            type = msgType,
            mediaUrl = mediaUrl
        )
    }

    companion object {
        fun fromDomain(partnerId: String, domain: ChatMessage): ChatMessageEntity {
            return ChatMessageEntity(
                id = domain.id,
                partnerId = partnerId,
                conversationId = domain.conversationId,
                senderId = domain.senderId,
                receiverId = domain.receiverId,
                text = domain.text,
                timestamp = domain.timestamp,
                isRead = domain.isRead,
                isSending = domain.isSending,
                isFailed = domain.isFailed,
                mediaUrl = domain.mediaUrl,
                type = domain.type.name
            )
        }
    }
}
