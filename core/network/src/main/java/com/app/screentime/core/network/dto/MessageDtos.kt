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
    val created_at: String? = null,
    val client_message_id: String? = null
)

@Serializable
data class MessagePaginationInfo(
    val page: Int = 1,
    val limit: Int = 20,
    val totalCount: Int = 0,
    val hasMore: Boolean = false,
    val nextPage: Int? = null
)

@Serializable
data class ConversationDetailData(
    val id: String? = null,
    val conversation_id: String? = null,
    val partner_user_id: String? = null,
    val partner_name: String? = null,
    val messages: List<ConversationDetailMessageDto> = emptyList(),
    val items: List<ConversationDetailMessageDto> = emptyList(),
    val page: Int? = null,
    val limit: Int? = null,
    val total_count: Int? = null,
    val has_more: Boolean? = null,
    val next_page: Int? = null
)

@Serializable
data class ConversationDetailResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: JsonElement? = null,
    // Generic limit fields
    val isLimitReached: Boolean = false,
    val limitMessage: String? = null,
    val quotaRemaining: Int? = null,
    val quotaLimit: Int? = null,
    // Backend compatibility aliases (snake_case)
    val is_limit_reached: Boolean? = null,
    val limit_message: String? = null,
    val quota_remaining: Int? = null,
    val quota_limit: Int? = null
) {
    val effectiveIsLimitReached: Boolean
        get() = isLimitReached || is_limit_reached == true

    val effectiveLimitMessage: String?
        get() = limitMessage ?: limit_message

    val effectiveQuotaRemaining: Int?
        get() = quotaRemaining ?: quota_remaining

    val effectiveQuotaLimit: Int?
        get() = quotaLimit ?: quota_limit

    fun toQuotaLimitInfo(): com.app.screentime.core.model.QuotaLimitInfo =
        com.app.screentime.core.model.QuotaLimitInfo(
            isLimitReached = effectiveIsLimitReached,
            limitMessage = effectiveLimitMessage,
            quotaRemaining = effectiveQuotaRemaining,
            quotaLimit = effectiveQuotaLimit
        )

    fun getMessageList(): List<ConversationDetailMessageDto> {
        val element = data ?: return emptyList()
        return try {
            when (element) {
                is JsonArray -> {
                    jsonFlexibleHelper.decodeFromJsonElement<List<ConversationDetailMessageDto>>(element)
                }
                is JsonObject -> {
                    val msgsElement = element["items"] ?: element["messages"] ?: element["data"]
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

    fun getPaginationInfo(): MessagePaginationInfo {
        val element = data ?: return MessagePaginationInfo()
        return try {
            if (element is JsonObject) {
                val page = element["page"]?.let {
                    try { it.toString().trim('"').toIntOrNull() } catch (_: Exception) { null }
                } ?: 1
                val limit = element["limit"]?.let {
                    try { it.toString().trim('"').toIntOrNull() } catch (_: Exception) { null }
                } ?: 20
                val totalCount = element["total_count"]?.let {
                    try { it.toString().trim('"').toIntOrNull() } catch (_: Exception) { null }
                } ?: 0
                val hasMore = element["has_more"]?.let {
                    try { it.toString().trim('"').toBooleanStrictOrNull() } catch (_: Exception) { null }
                } ?: (element["next_page"] != null && element["next_page"].toString() != "null")
                val nextPage = element["next_page"]?.let {
                    try { it.toString().trim('"').toIntOrNull() } catch (_: Exception) { null }
                }
                MessagePaginationInfo(
                    page = page,
                    limit = limit,
                    totalCount = totalCount,
                    hasMore = hasMore,
                    nextPage = nextPage
                )
            } else {
                MessagePaginationInfo()
            }
        } catch (e: Exception) {
            MessagePaginationInfo()
        }
    }
}

// ── Send Message Request & Response ─────────────────────────────────────────
@Serializable
data class SendMessageRequest(
    val conversation_id: String? = null,
    val receiver_id: String? = null,
    val content: String,
    val client_message_id: String? = null
)

@Serializable
data class SendMessageDataDto(
    val id: String = "",
    val conversation_id: String = "",
    val sender_id: String = "",
    val receiver_id: String? = null,
    val content: String = "",
    val status: String? = null,
    val created_at: String? = null,
    val client_message_id: String? = null
)

@Serializable
data class SendMessageResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: SendMessageDataDto? = null,
    // Generic limit fields
    val isLimitReached: Boolean = false,
    val limitMessage: String? = null,
    val quotaRemaining: Int? = null,
    val quotaLimit: Int? = null,
    // Backend compatibility aliases (snake_case)
    val is_limit_reached: Boolean? = null,
    val limit_message: String? = null,
    val quota_remaining: Int? = null,
    val quota_limit: Int? = null
) {
    val effectiveIsLimitReached: Boolean
        get() = isLimitReached || is_limit_reached == true

    val effectiveLimitMessage: String?
        get() = limitMessage ?: limit_message

    val effectiveQuotaRemaining: Int?
        get() = quotaRemaining ?: quota_remaining

    val effectiveQuotaLimit: Int?
        get() = quotaLimit ?: quota_limit

    fun toQuotaLimitInfo(): com.app.screentime.core.model.QuotaLimitInfo =
        com.app.screentime.core.model.QuotaLimitInfo(
            isLimitReached = effectiveIsLimitReached,
            limitMessage = effectiveLimitMessage,
            quotaRemaining = effectiveQuotaRemaining,
            quotaLimit = effectiveQuotaLimit
        )
}


// ── Real-time WebSocket Event Payload ───────────────────────────────────────
@Serializable
data class WSChatMessageReceivedData(
    val id: String = "",
    val conversation_id: String = "",
    val sender_id: String = "",
    val receiver_id: String = "",
    val content: String = "",
    val created_at: String = "",
    val client_message_id: String? = null
)
