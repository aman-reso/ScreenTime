package com.app.screentime.core.network.api

import android.util.Log
import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.dto.ConversationDetailResponse
import com.app.screentime.core.network.dto.MessageConversationsResponse
import com.app.screentime.core.network.dto.SendMessageRequest
import com.app.screentime.core.network.dto.SendMessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagesApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl: String
        get() = ApiEndpoints.getBaseUrl()
    private val tag = "MessagesApi"

    suspend fun getConversations(
        token: String,
        page: Int = 1,
        limit: Int = 10,
        userId: String? = null
    ): MessageConversationsResponse {
        return try {
            httpClient.get("$baseUrl${ApiEndpoints.Messages.CONVERSATIONS_V2}") {
                bearerAuth(token)
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        } catch (t: Throwable) {
            try {
                httpClient.get("$baseUrl${ApiEndpoints.Messages.CONVERSATIONS}") {
                    bearerAuth(token)
                    if (!userId.isNullOrBlank()) {
                        header("X-User-Id", userId)
                    }
                    parameter("page", page)
                    parameter("limit", limit)
                }.body()
            } catch (fallbackErr: Throwable) {
                Log.e(tag, "Failed to get conversations: ${fallbackErr.message}", fallbackErr)
                MessageConversationsResponse(success = false, message = fallbackErr.message ?: "Unknown error")
            }
        }
    }

    suspend fun getConversationMessages(
        token: String,
        conversationId: String,
        page: Int? = null,
        limit: Int? = 20,
        before: String? = null,
        userId: String? = null
    ): ConversationDetailResponse {
        return try {
            val endpoint = ApiEndpoints.Messages.conversationMessagesV2(conversationId)
            val response: ConversationDetailResponse = httpClient.get("$baseUrl$endpoint") {
                bearerAuth(token)
                if (page != null) {
                    parameter("page", page)
                }
                if (limit != null) {
                    parameter("limit", limit)
                }
                if (!before.isNullOrBlank()) {
                    parameter("before", before)
                }
            }.body()

            if (!response.success && response.getMessageList().isEmpty()) {
                val legacyEndpoint = ApiEndpoints.Messages.legacyConversationMessages(conversationId)
                httpClient.get("$baseUrl$legacyEndpoint") {
                    bearerAuth(token)
                    if (!userId.isNullOrBlank()) {
                        header("X-User-Id", userId)
                    }
                    if (page != null) parameter("page", page)
                    if (limit != null) parameter("limit", limit)
                    if (!before.isNullOrBlank()) parameter("before", before)
                }.body()
            } else {
                response
            }
        } catch (t: Throwable) {
            try {
                val legacyEndpoint = ApiEndpoints.Messages.legacyConversationMessages(conversationId)
                httpClient.get("$baseUrl$legacyEndpoint") {
                    bearerAuth(token)
                    if (!userId.isNullOrBlank()) {
                        header("X-User-Id", userId)
                    }
                    if (page != null) parameter("page", page)
                    if (limit != null) parameter("limit", limit)
                    if (!before.isNullOrBlank()) parameter("before", before)
                }.body()
            } catch (t2: Throwable) {
                Log.e(tag, "Failed to get conversation messages: ${t2.message}", t2)
                ConversationDetailResponse(success = false, message = t2.message ?: "Unknown error")
            }
        }
    }

    suspend fun sendMessage(
        token: String,
        conversationId: String? = null,
        content: String,
        userId: String? = null,
        receiverId: String? = null,
        clientMessageId: String? = null
    ): SendMessageResponse {
        return try {
            httpClient.post("$baseUrl${ApiEndpoints.Messages.SEND_V2}") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(SendMessageRequest(
                    conversation_id = conversationId?.takeIf { it.isNotBlank() },
                    receiver_id = receiverId?.takeIf { it.isNotBlank() },
                    content = content,
                    client_message_id = clientMessageId
                ))
            }.body()
        } catch (t: Throwable) {
            try {
                httpClient.post("$baseUrl${ApiEndpoints.Messages.SEND}") {
                    bearerAuth(token)
                    if (!userId.isNullOrBlank()) {
                        header("X-User-Id", userId)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(SendMessageRequest(
                        conversation_id = conversationId?.takeIf { it.isNotBlank() },
                        receiver_id = receiverId?.takeIf { it.isNotBlank() },
                        content = content,
                        client_message_id = clientMessageId
                    ))
                }.body()
            } catch (t2: Throwable) {
                Log.e(tag, "Failed to send message: ${t2.message}", t2)
                SendMessageResponse(success = false, message = t2.message ?: "Unknown error")
            }
        }
    }

    suspend fun claimAdReward(
        token: String,
        userId: String,
        reward: Int = 5
    ): Boolean {
        return try {
            httpClient.post("$baseUrl/api/messages/reward/claim") {
                if (token.isNotBlank()) {
                    bearerAuth(token)
                }
                if (userId.isNotBlank()) {
                    header("X-User-Id", userId)
                }
                contentType(ContentType.Application.Json)
                setBody(mapOf("user_id" to userId, "reward" to reward.toString()))
            }
            true
        } catch (t: Throwable) {
            Log.e(tag, "Failed to claim ad reward: ${t.message}")
            false
        }
    }
}
