package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

private val jsonFlexibleHelper = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
}

@Serializable
data class MessageConversationItemDto(
    val id: String = "",
    val partner_user_id: String = "",
    val partner_name: String = "",
    val partner_photo_url: String? = null,
    val last_message_text: String? = null,
    val last_message_at: String? = null,
    val unread_count: Int = 0
)

@Serializable
data class MessageConversationsData(
    val items: List<MessageConversationItemDto> = emptyList(),
    val page: Int = 1,
    val limit: Int = 10,
    val total_count: Int = 0,
    val has_more: Boolean = false
)

@Serializable
data class MessageConversationsResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: JsonElement? = null
) {
    fun getItems(): List<MessageConversationItemDto> {
        val element = data ?: return emptyList()
        return try {
            when (element) {
                is JsonArray -> {
                    jsonFlexibleHelper.decodeFromJsonElement<List<MessageConversationItemDto>>(element)
                }
                is JsonObject -> {
                    val itemsElement = element["items"] ?: element["conversations"] ?: element["data"]
                    if (itemsElement is JsonArray) {
                        jsonFlexibleHelper.decodeFromJsonElement<List<MessageConversationItemDto>>(itemsElement)
                    } else {
                        emptyList()
                    }
                }
                else -> emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// ── Conversation Detail & Messages History ──────────────────────────────────
@Serializable
data class ConversationDetailMessageDto(
    val id: String = "",
    val conversation_id: String? = null,
    val sender_id: String = "",
    val receiver_id: String? = null,
    val content: String = "",
    val status: String? = null,
    val created_at: String? = null
)

@Serializable
data class ConversationDetailData(
    val id: String? = null,
    val conversation_id: String? = null,
    val partner_user_id: String? = null,
    val partner_name: String? = null,
    val messages: List<ConversationDetailMessageDto> = emptyList(),
    val items: List<ConversationDetailMessageDto> = emptyList()
)

@Serializable
data class ConversationDetailResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: JsonElement? = null
) {
    fun getMessageList(): List<ConversationDetailMessageDto> {
        val element = data ?: return emptyList()
        return try {
            when (element) {
                is JsonArray -> {
                    jsonFlexibleHelper.decodeFromJsonElement<List<ConversationDetailMessageDto>>(element)
                }
                is JsonObject -> {
                    val msgsElement = element["messages"] ?: element["items"] ?: element["data"]
                    if (msgsElement is JsonArray) {
                        jsonFlexibleHelper.decodeFromJsonElement<List<ConversationDetailMessageDto>>(msgsElement)
                    } else {
                        emptyList()
                    }
                }
                else -> emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// ── Send Message Request & Response ─────────────────────────────────────────
@Serializable
data class SendMessageRequest(
    val conversation_id: String,
    val content: String
)

@Serializable
data class SendMessageDataDto(
    val id: String = "",
    val conversation_id: String = "",
    val sender_id: String = "",
    val receiver_id: String? = null,
    val content: String = "",
    val status: String? = null,
    val created_at: String? = null
)

@Serializable
data class SendMessageResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: SendMessageDataDto? = null
)

// ── Real-time WebSocket Event Payload ───────────────────────────────────────
@Serializable
data class WSChatMessageReceivedData(
    val id: String = "",
    val conversation_id: String = "",
    val sender_id: String = "",
    val receiver_id: String = "",
    val content: String = "",
    val created_at: String = ""
)
