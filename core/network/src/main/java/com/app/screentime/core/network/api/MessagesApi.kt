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
            httpClient.get("$baseUrl${ApiEndpoints.Messages.CONVERSATIONS}") {
                bearerAuth(token)
                if (!userId.isNullOrBlank()) {
                    header("X-User-Id", userId)
                }
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to get conversations: ${t.message}", t)
            MessageConversationsResponse(success = false, message = t.message ?: "Unknown error")
        }
    }

    suspend fun getConversationMessages(
        token: String,
        conversationId: String,
        userId: String? = null
    ): ConversationDetailResponse {
        return try {
            val endpoint = ApiEndpoints.Messages.conversationDetail(conversationId)
            httpClient.get("$baseUrl$endpoint") {
                bearerAuth(token)
                if (!userId.isNullOrBlank()) {
                    header("X-User-Id", userId)
                }
            }.body()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to get conversation messages: ${t.message}", t)
            ConversationDetailResponse(success = false, message = t.message ?: "Unknown error")
        }
    }

    suspend fun sendMessage(
        token: String,
        conversationId: String,
        content: String,
        userId: String? = null
    ): SendMessageResponse {
        return try {
            httpClient.post("$baseUrl${ApiEndpoints.Messages.SEND}") {
                bearerAuth(token)
                if (!userId.isNullOrBlank()) {
                    header("X-User-Id", userId)
                }
                contentType(ContentType.Application.Json)
                setBody(SendMessageRequest(conversation_id = conversationId, content = content))
            }.body()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to send message: ${t.message}", t)
            SendMessageResponse(success = false, message = t.message ?: "Unknown error")
        }
    }
}
