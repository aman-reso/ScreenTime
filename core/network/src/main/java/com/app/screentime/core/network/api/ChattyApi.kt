package com.app.screentime.core.network.api

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChattyApi @Inject constructor(
    private val networkClient: NetworkClient
) {

    val baseUrl: String
        get() = ApiEndpoints.getBaseUrl()

    private val httpClient: HttpClient
        get() = networkClient.httpClient

    // ── 1. Auth ───────────────────────────────────────────────────────────────

    suspend fun registerOrLogin(phone: String, name: String, role: String): AuthResponse {
        return httpClient.post("$baseUrl/api/auth") {
            setBody(RegisterRequest(phone = phone, name = name, role = role))
        }.body()
    }

    // ── 2. Models ─────────────────────────────────────────────────────────────

    suspend fun getModels(token: String): ModelListResponse {
        return httpClient.get("$baseUrl/api/models") {
            bearerAuth(token)
        }.body()
    }

    suspend fun getModelProfile(token: String, modelId: String): UserDto {
        return httpClient.get("$baseUrl/api/models/$modelId") {
            bearerAuth(token)
        }.body()
    }

    // ── 3. Wallet ─────────────────────────────────────────────────────────────

    suspend fun getWallet(token: String): WalletResponse {
        return httpClient.get("$baseUrl/api/wallet") {
            bearerAuth(token)
        }.body()
    }

    suspend fun recharge(token: String, amount: Double): WalletDto {
        return httpClient.post("$baseUrl/api/wallet/recharge") {
            bearerAuth(token)
            setBody(RechargeRequest(amount = amount))
        }.body()
    }

    // ── 4. Calls ──────────────────────────────────────────────────────────────

    suspend fun getCallHistory(token: String): CallHistoryResponse {
        return httpClient.get("$baseUrl/api/calls/history") {
            bearerAuth(token)
        }.body()
    }

    // ── 5. Rooms ──────────────────────────────────────────────────────────────

    suspend fun getRooms(token: String): RoomListResponse {
        return httpClient.get("$baseUrl/api/rooms") {
            bearerAuth(token)
        }.body()
    }

    suspend fun createRoom(token: String, title: String, ratePerMin: Double): GroupRoomDto {
        return httpClient.post("$baseUrl/api/rooms") {
            bearerAuth(token)
            setBody(CreateRoomRequest(title = title, rate_per_min = ratePerMin))
        }.body()
    }

    suspend fun joinRoom(token: String, roomId: String): Boolean {
        val res = httpClient.post("$baseUrl/api/rooms/$roomId/join") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    suspend fun leaveRoom(token: String, roomId: String): Boolean {
        val res = httpClient.post("$baseUrl/api/rooms/$roomId/leave") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    // ── 6. Favorites ──────────────────────────────────────────────────────────

    suspend fun getFavorites(token: String): ModelListResponse {
        return httpClient.get("$baseUrl/api/favorites") {
            bearerAuth(token)
        }.body()
    }

    suspend fun addFavorite(token: String, modelId: String): Boolean {
        val res = httpClient.post("$baseUrl/api/favorites/$modelId") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    suspend fun removeFavorite(token: String, modelId: String): Boolean {
        val res = httpClient.delete("$baseUrl/api/favorites/$modelId") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    // ── 7. Report ─────────────────────────────────────────────────────────────

    suspend fun reportUser(token: String, reportedId: String, reason: String): Boolean {
        val res = httpClient.post("$baseUrl/api/report") {
            bearerAuth(token)
            setBody(ReportRequest(reported_id = reportedId, reason = reason))
        }
        return res.status.isSuccess()
    }

    // ── 8. Model Onboarding ───────────────────────────────────────────────────

    suspend fun submitOnboarding(
        token: String,
        bio: String,
        voiceRatePerMin: Double,
        groupRatePerMin: Double,
        chatRatePerMsg: Double
    ): Boolean {
        val res = httpClient.post("$baseUrl/api/model/onboard") {
            bearerAuth(token)
            setBody(
                ModelOnboardRequest(
                    bio = bio,
                    voice_rate_per_min = voiceRatePerMin,
                    group_rate_per_min = groupRatePerMin,
                    chat_rate_per_msg = chatRatePerMsg
                )
            )
        }
        return res.status.isSuccess()
    }


    suspend fun getOnboardingStatus(token: String): OnboardingStatusResponse {
        return httpClient.get("$baseUrl/api/model/onboard/status") {
            bearerAuth(token)
        }.body()
    }

    // ── 9. User Profile ───────────────────────────────────────────────────────

    suspend fun getUserProfile(token: String): UserDto {
        return httpClient.get("$baseUrl/api/v1/user/profile") {
            bearerAuth(token)
        }.body()
    }

    fun getWsUrl(token: String): String {
        val wsBase = baseUrl.replace("http://", "ws://").replace("https://", "wss://")
        return "$wsBase/ws?token=$token"
    }
}
