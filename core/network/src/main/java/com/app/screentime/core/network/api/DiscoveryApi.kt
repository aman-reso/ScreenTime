package com.app.screentime.core.network.api

import android.util.Log
import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.dto.DiscoveryApiResponse
import com.app.screentime.core.network.dto.DiscoveryInteractData
import com.app.screentime.core.network.dto.DiscoveryInteractRequest
import com.app.screentime.core.network.dto.DiscoveryInteractResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
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
class DiscoveryApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl: String
        get() = ApiEndpoints.getBaseUrl()
    private val tag = "DiscoveryApi"

    suspend fun getFeed(
        token: String,
        page: Int = 1,
        limit: Int = 10,
        userId: String? = null
    ): DiscoveryApiResponse {
        return try {
            httpClient.get("$baseUrl${ApiEndpoints.Discovery.FEED}") {
                if (token.isNotBlank()) {
                    bearerAuth(token)
                }
                accept(ContentType.Application.Json)
                if (!userId.isNullOrBlank()) {
                    header("X-User-Id", userId)
                }
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to get feed candidates: ${t.message}", t)
            DiscoveryApiResponse(success = false, message = t.message ?: "Unknown error")
        }
    }

    suspend fun getDiscoveryCandidates(
        token: String,
        page: Int = 1,
        limit: Int = 10,
        userId: String? = null
    ): DiscoveryApiResponse {
        return try {
            httpClient.get("$baseUrl${ApiEndpoints.Discovery.CANDIDATES}") {
                if (!token.isNullOrBlank()) {
                    bearerAuth(token)
                }
                accept(ContentType.Application.Json)
                if (!userId.isNullOrBlank()) {
                    header("X-User-Id", userId)
                }
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to get discovery candidates: ${t.message}", t)
            DiscoveryApiResponse(success = false, message = t.message ?: "Unknown error")
        }
    }

    suspend fun interact(
        token: String,
        targetUserId: String,
        action: String,
        userId: String? = null
    ): DiscoveryInteractResponse {
        return try {
            httpClient.post("$baseUrl${ApiEndpoints.Discovery.INTERACT}") {
                if (!token.isNullOrBlank()) {
                    bearerAuth(token)
                }
                contentType(ContentType.Application.Json)
                if (!userId.isNullOrBlank()) {
                    header("X-User-Id", userId)
                }
                setBody(DiscoveryInteractRequest(target_user_id = targetUserId, action = action))
            }.body()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to interact on discovery ($action on $targetUserId): ${t.message}", t)
            DiscoveryInteractResponse(
                success = false,
                message = t.message ?: "Interaction failed",
                data = DiscoveryInteractData(
                    success = false,
                    action = action,
                    is_match = false,
                    target_user_id = targetUserId,
                    message = t.message
                )
            )
        }
    }

    suspend fun getMatches(
        token: String,
        page: Int = 1,
        limit: Int = 10,
        userId: String? = null
    ): com.app.screentime.core.network.dto.DiscoveryMatchesResponse {
        return try {
            httpClient.get("$baseUrl${ApiEndpoints.Discovery.MATCHES}") {
                if (!token.isNullOrBlank()) {
                    bearerAuth(token)
                }
                if (!userId.isNullOrBlank()) {
                    header("X-User-Id", userId)
                }
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to get discovery matches: ${t.message}", t)
            com.app.screentime.core.network.dto.DiscoveryMatchesResponse(
                success = false,
                message = t.message ?: "Failed to retrieve matches"
            )
        }
    }
}
