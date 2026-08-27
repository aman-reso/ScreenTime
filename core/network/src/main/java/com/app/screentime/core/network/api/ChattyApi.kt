package com.app.screentime.core.network.api

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
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
        val res: ApiResponse<AuthResponse> = httpClient.post("$baseUrl/api/auth") {
            setBody(RegisterRequest(phone = phone, name = name, role = role))
        }.body()
        return res.data ?: throw Exception(res.message.ifBlank { "Authentication failed" })
    }

    // ── 2. Models ─────────────────────────────────────────────────────────────
    suspend fun getModels(token: String): ModelListResponse {
        val res: ApiResponse<ModelListResponse> = httpClient.get("$baseUrl/api/models") {
            bearerAuth(token)
        }.body()
        return res.data ?: ModelListResponse()
    }

    suspend fun getModelProfile(token: String, modelId: String): UserDto {
        val res: ApiResponse<UserDto> = httpClient.get("$baseUrl/api/models/$modelId") {
            bearerAuth(token)
        }.body()
        return res.data ?: throw Exception(res.message.ifBlank { "Model not found" })
    }

    // ── 3. Wallet & Packs ─────────────────────────────────────────────────────
    suspend fun getWallet(token: String): WalletResponse {
        val res: ApiResponse<WalletResponse> = httpClient.get("$baseUrl/api/wallet") {
            bearerAuth(token)
        }.body()
        return res.data ?: throw Exception(res.message.ifBlank { "Failed to fetch wallet" })
    }

    suspend fun getWalletPacks(): WalletPacksResponse {
        val res: ApiResponse<WalletPacksResponse> = httpClient.get("$baseUrl/api/wallet/packs").body()
        return res.data ?: WalletPacksResponse()
    }

    suspend fun recharge(token: String, amount: Double): WalletDto {
        val res: ApiResponse<WalletDto> = httpClient.post("$baseUrl/api/wallet/recharge") {
            bearerAuth(token)
            setBody(RechargeRequest(amount = amount))
        }.body()
        return res.data ?: throw Exception(res.message.ifBlank { "Recharge failed" })
    }

    // ── 4. Calls & Balance Check ──────────────────────────────────────────────
    suspend fun checkCallBalance(token: String, modelId: String, callType: String = "voice"): CheckCallBalanceResponse {
        val res: ApiResponse<CheckCallBalanceResponse> = httpClient.post("$baseUrl/api/calls/check-balance") {
            bearerAuth(token)
            setBody(CheckCallBalanceRequest(model_id = modelId, call_type = callType))
        }.body()
        return res.data ?: CheckCallBalanceResponse(message = res.message)
    }

    suspend fun getCallHistory(token: String): CallHistoryResponse {
        val res: ApiResponse<CallHistoryResponse> = httpClient.get("$baseUrl/api/history/calls") {
            bearerAuth(token)
        }.body()
        return res.data ?: CallHistoryResponse()
    }

    // ── 5. Ephemeral 24-Hour Chat ─────────────────────────────────────────────
    suspend fun getConversations(token: String): ConversationListResponse {
        val res: ApiResponse<ConversationListResponse> = httpClient.get("$baseUrl/api/chat/conversations") {
            bearerAuth(token)
        }.body()
        return res.data ?: ConversationListResponse()
    }

    suspend fun getChatMessages(token: String, partnerId: String): EphemeralChatResponse {
        val res: ApiResponse<EphemeralChatResponse> = httpClient.get("$baseUrl/api/chat/messages?partner_id=$partnerId") {
            bearerAuth(token)
        }.body()
        return res.data ?: EphemeralChatResponse(partner_id = partnerId)
    }

    suspend fun sendChatMessage(token: String, receiverId: String, content: String): ChatMessageDto {
        val res: ApiResponse<ChatMessageDto> = httpClient.post("$baseUrl/api/chat/send") {
            bearerAuth(token)
            setBody(SendChatMessageRequest(receiver_id = receiverId, content = content))
        }.body()
        return res.data ?: throw Exception(res.message.ifBlank { "Failed to send message" })
    }

    // ── 6. Rooms ──────────────────────────────────────────────────────────────
    suspend fun getRooms(token: String): RoomListResponse {
        val res: ApiResponse<RoomListResponse> = httpClient.get("$baseUrl/api/rooms") {
            bearerAuth(token)
        }.body()
        return res.data ?: RoomListResponse()
    }

    suspend fun createRoom(token: String, title: String, ratePerMin: Double): GroupRoomDto {
        val res: ApiResponse<GroupRoomDto> = httpClient.post("$baseUrl/api/rooms") {
            bearerAuth(token)
            setBody(CreateRoomRequest(title = title, rate_per_min = ratePerMin))
        }.body()
        return res.data ?: throw Exception(res.message.ifBlank { "Room creation failed" })
    }

    // ── 7. Favorites ──────────────────────────────────────────────────────────
    suspend fun getFavorites(token: String): ModelListResponse {
        val res: ApiResponse<ModelListResponse> = httpClient.get("$baseUrl/api/models/favorites") {
            bearerAuth(token)
        }.body()
        return res.data ?: ModelListResponse()
    }

    suspend fun addFavorite(token: String, modelId: String): Boolean {
        val res = httpClient.post("$baseUrl/api/models/favorite") {
            bearerAuth(token)
            setBody(mapOf("model_id" to modelId))
        }
        return res.status.isSuccess()
    }

    suspend fun removeFavorite(token: String, modelId: String): Boolean {
        val res = httpClient.post("$baseUrl/api/models/favorite") {
            bearerAuth(token)
            setBody(mapOf("model_id" to modelId))
        }
        return res.status.isSuccess()
    }

    // ── 8. Report & User Profile ──────────────────────────────────────────────
    suspend fun reportUser(token: String, reportedId: String, reason: String): Boolean {
        val res = httpClient.post("$baseUrl/api/reports") {
            bearerAuth(token)
            setBody(ReportRequest(reported_id = reportedId, reason = reason))
        }
        return res.status.isSuccess()
    }

    suspend fun getUserProfile(token: String): UserDto {
        val res: ApiResponse<UserDto> = httpClient.get("$baseUrl/api/user/profile") {
            bearerAuth(token)
        }.body()
        return res.data ?: throw Exception(res.message.ifBlank { "User not found" })
    }

    suspend fun submitOnboarding(token: String, bio: String, voiceRate: Double, chatRate: Double): Boolean {
        val res = httpClient.post("$baseUrl/api/models/onboarding") {
            bearerAuth(token)
            setBody(ModelOnboardRequest(bio = bio, voice_rate_per_min = voiceRate, group_rate_per_min = voiceRate / 2, chat_rate_per_msg = chatRate))
        }
        return res.status.isSuccess()
    }

    fun getWsUrl(token: String): String {
        val wsBase = baseUrl.replace("http://", "ws://").replace("https://", "wss://")
        return "$wsBase/ws?token=$token"
    }
}
