package com.app.screentime.core.network.api

import android.util.Log
import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.dto.*
import com.app.screentime.core.network.mock.ChattyMockDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChattyApi @Inject constructor(
    private val networkClient: NetworkClient
) {
    private val tag = "ChattyApi"

    val baseUrl: String
        get() = ApiEndpoints.getBaseUrl()

    private val httpClient: HttpClient
        get() = networkClient.httpClient

    // ── 1. Auth ───────────────────────────────────────────────────────────────
    suspend fun registerOrLogin(phone: String, name: String, role: String): AuthResponse {
        return try {
            val res: ApiResponse<AuthResponse> = httpClient.post("$baseUrl/api/auth") {
                setBody(RegisterRequest(phone = phone, name = name, role = role))
            }.body()
            res.data ?: ChattyMockDataSource.createMockAuthResponse(idToken = "", email = phone, name = name)
        } catch (t: Throwable) {
            Log.w(tag, "registerOrLogin falling back to mock: ${t.message}")
            ChattyMockDataSource.createMockAuthResponse(idToken = "", email = phone, name = name)
        }
    }

    /**
     * Authenticate via Google Sign-In.
     * Backend is the overall owner: verifies the token, creates/fetches user, issues JWT session.
     * If the remote endpoint is unreachable or returns HTML, gracefully falls back to simulated authentication.
     */
    suspend fun loginWithGoogle(
        idToken: String,
        email: String? = null,
        name: String? = null,
        avatarUrl: String? = null,
        role: String = "user"
    ): AuthResponse {
        val request = GoogleAuthRequest(
            id_token = idToken,
            idToken = idToken,
            token = idToken,
            credential = idToken,
            email = email,
            name = name,
            avatar_url = avatarUrl,
            avatarUrl = avatarUrl,
            role = "user"
        )
        return try {
            val res: ApiResponse<AuthResponse> = try {
                httpClient.post("$baseUrl${ApiEndpoints.Auth.GOOGLE}") {
                    setBody(request)
                }.body()
            } catch (e: Throwable) {
                httpClient.post("$baseUrl${ApiEndpoints.Auth.AUTH}") {
                    setBody(request)
                }.body()
            }
            res.data ?: ChattyMockDataSource.createMockAuthResponse(idToken, email, name)
        } catch (t: Throwable) {
            Log.w(tag, "loginWithGoogle remote unavailable or HTML response, falling back to mock: ${t.message}")
            ChattyMockDataSource.createMockAuthResponse(idToken, email, name)
        }
    }

    // ── 2. Models ─────────────────────────────────────────────────────────────
    suspend fun getModels(token: String, page: Int = 1, limit: Int = 10): ModelListResponse {
        return try {
            val res: ApiResponse<ModelListResponse> = httpClient.get("$baseUrl/api/models") {
                bearerAuth(token)
                parameter("page", page)
                parameter("limit", limit)
            }.body()
            val list = res.data?.models
            if (!list.isNullOrEmpty()) {
                res.data
            } else {
                ModelListResponse(count = ChattyMockDataSource.mockUsers.size, models = ChattyMockDataSource.mockUsers)
            }
        } catch (t: Throwable) {
            Log.w(tag, "getModels falling back to mock: ${t.message}")
            ModelListResponse(count = ChattyMockDataSource.mockUsers.size, models = ChattyMockDataSource.mockUsers)
        }
    }

    suspend fun getModelProfile(token: String, modelId: String): UserDto {
        return try {
            val res: ApiResponse<UserDto> = httpClient.get("$baseUrl/api/models/$modelId") {
                bearerAuth(token)
            }.body()
            res.data ?: ChattyMockDataSource.mockUsers.find { it.id == modelId } ?: ChattyMockDataSource.mockUsers.first()
        } catch (t: Throwable) {
            Log.w(tag, "getModelProfile falling back to mock: ${t.message}")
            ChattyMockDataSource.mockUsers.find { it.id == modelId } ?: ChattyMockDataSource.mockUsers.first()
        }
    }

    // ── 3. Wallet & Packs ─────────────────────────────────────────────────────
    suspend fun getWallet(token: String): WalletResponse {
        return try {
            val res: ApiResponse<WalletResponse> = httpClient.get("$baseUrl/api/wallet") {
                bearerAuth(token)
            }.body()
            res.data ?: WalletResponse(
                wallet = WalletDto(
                    balance = 1000.0,
                    bonus_given = 100.0,
                    total_spent = 0.0,
                    total_earned = 0.0
                )
            )
        } catch (t: Throwable) {
            Log.w(tag, "getWallet falling back to mock: ${t.message}")
            WalletResponse(
                wallet = WalletDto(
                    balance = 1000.0,
                    bonus_given = 100.0,
                    total_spent = 0.0,
                    total_earned = 0.0
                )
            )
        }
    }

    suspend fun getWalletPacks(): WalletPacksResponse {
        return try {
            val res: ApiResponse<WalletPacksResponse> = httpClient.get("$baseUrl/api/wallet/packs").body()
            val packs = res.data?.packs
            if (!packs.isNullOrEmpty()) {
                res.data
            } else {
                WalletPacksResponse(packs = ChattyMockDataSource.mockWalletPacks)
            }
        } catch (t: Throwable) {
            Log.w(tag, "getWalletPacks falling back to mock: ${t.message}")
            WalletPacksResponse(packs = ChattyMockDataSource.mockWalletPacks)
        }
    }

    suspend fun recharge(token: String, amount: Double): WalletDto {
        return try {
            val res: ApiResponse<WalletDto> = httpClient.post("$baseUrl/api/wallet/recharge") {
                bearerAuth(token)
                setBody(RechargeRequest(amount = amount))
            }.body()
            res.data ?: WalletDto(balance = 1000.0 + amount, bonus_given = 100.0)
        } catch (t: Throwable) {
            Log.w(tag, "recharge falling back to mock: ${t.message}")
            WalletDto(balance = 1000.0 + amount, bonus_given = 100.0)
        }
    }

    // ── 4. Calls & Balance Check ──────────────────────────────────────────────
    suspend fun checkCallBalance(token: String, modelId: String, callType: String = "voice"): CheckCallBalanceResponse {
        return try {
            val res: ApiResponse<CheckCallBalanceResponse> = httpClient.post("$baseUrl/api/calls/check-balance") {
                bearerAuth(token)
                setBody(CheckCallBalanceRequest(model_id = modelId, call_type = callType))
            }.body()
            res.data ?: CheckCallBalanceResponse(
                can_call = true,
                balance = 1000.0,
                rate_per_min = 15.0,
                min_required = 15.0,
                max_duration_sec = 4000,
                message = "Balance verified"
            )
        } catch (t: Throwable) {
            Log.w(tag, "checkCallBalance falling back to mock: ${t.message}")
            CheckCallBalanceResponse(
                can_call = true,
                balance = 1000.0,
                rate_per_min = 15.0,
                min_required = 15.0,
                max_duration_sec = 4000,
                message = "Balance verified"
            )
        }
    }

    suspend fun getCallHistory(token: String): CallHistoryResponse {
        return try {
            val res: ApiResponse<CallHistoryResponse> = httpClient.get("$baseUrl/api/history/calls") {
                bearerAuth(token)
            }.body()
            res.data ?: CallHistoryResponse()
        } catch (t: Throwable) {
            Log.w(tag, "getCallHistory falling back to mock: ${t.message}")
            CallHistoryResponse()
        }
    }

    // ── 5. Ephemeral 24-Hour Chat ─────────────────────────────────────────────
    suspend fun getConversations(token: String): ConversationListResponse {
        return try {
            val res: ApiResponse<ConversationListResponse> = httpClient.get("$baseUrl/api/chat/conversations") {
                bearerAuth(token)
            }.body()
            val list = res.data?.conversations
            if (!list.isNullOrEmpty()) {
                res.data
            } else {
                ConversationListResponse(conversations = ChattyMockDataSource.mockConversations)
            }
        } catch (t: Throwable) {
            Log.w(tag, "getConversations falling back to mock: ${t.message}")
            ConversationListResponse(conversations = ChattyMockDataSource.mockConversations)
        }
    }

    suspend fun getChatMessages(token: String, partnerId: String): EphemeralChatResponse {
        return try {
            val res: ApiResponse<EphemeralChatResponse> = httpClient.get("$baseUrl/api/chat/messages?partner_id=$partnerId") {
                bearerAuth(token)
            }.body()
            val msgs = res.data?.messages
            if (!msgs.isNullOrEmpty()) {
                res.data
            } else {
                EphemeralChatResponse(partner_id = partnerId, messages = ChattyMockDataSource.getMockMessages(partnerId))
            }
        } catch (t: Throwable) {
            Log.w(tag, "getChatMessages falling back to mock: ${t.message}")
            EphemeralChatResponse(partner_id = partnerId, messages = ChattyMockDataSource.getMockMessages(partnerId))
        }
    }

    suspend fun sendChatMessage(token: String, receiverId: String, content: String): ChatMessageDto {
        return try {
            val res: ApiResponse<ChatMessageDto> = httpClient.post("$baseUrl/api/chat/send") {
                bearerAuth(token)
                setBody(SendChatMessageRequest(receiver_id = receiverId, content = content))
            }.body()
            res.data ?: ChatMessageDto(
                id = "msg_${System.currentTimeMillis()}",
                sender_id = "me",
                receiver_id = receiverId,
                content = content,
                cost = 0.0,
                created_at = java.time.Instant.now().toString()
            )
        } catch (t: Throwable) {
            Log.w(tag, "sendChatMessage falling back to mock: ${t.message}")
            ChatMessageDto(
                id = "msg_${System.currentTimeMillis()}",
                sender_id = "me",
                receiver_id = receiverId,
                content = content,
                cost = 0.0,
                created_at = java.time.Instant.now().toString()
            )
        }
    }

    // ── 6. Rooms ──────────────────────────────────────────────────────────────
    suspend fun getRooms(token: String): RoomListResponse {
        return try {
            val res: ApiResponse<RoomListResponse> = httpClient.get("$baseUrl/api/rooms") {
                bearerAuth(token)
            }.body()
            res.data ?: RoomListResponse()
        } catch (t: Throwable) {
            Log.w(tag, "getRooms falling back to mock: ${t.message}")
            RoomListResponse()
        }
    }

    suspend fun createRoom(token: String, title: String, ratePerMin: Double): GroupRoomDto {
        return try {
            val res: ApiResponse<GroupRoomDto> = httpClient.post("$baseUrl/api/rooms") {
                bearerAuth(token)
                setBody(CreateRoomRequest(title = title, rate_per_min = ratePerMin))
            }.body()
            res.data ?: GroupRoomDto(id = "room_${System.currentTimeMillis()}", model_id = "me", title = title, rate_per_min = ratePerMin)
        } catch (t: Throwable) {
            Log.w(tag, "createRoom falling back to mock: ${t.message}")
            GroupRoomDto(id = "room_${System.currentTimeMillis()}", model_id = "me", title = title, rate_per_min = ratePerMin)
        }
    }

    // ── 7. Favorites ──────────────────────────────────────────────────────────
    suspend fun getFavorites(token: String): ModelListResponse {
        return try {
            val res: ApiResponse<ModelListResponse> = httpClient.get("$baseUrl/api/models/favorites") {
                bearerAuth(token)
            }.body()
            val favs = res.data?.models
            if (!favs.isNullOrEmpty()) {
                res.data
            } else {
                ModelListResponse(count = ChattyMockDataSource.mockFavorites.size, models = ChattyMockDataSource.mockFavorites)
            }
        } catch (t: Throwable) {
            Log.w(tag, "getFavorites falling back to mock: ${t.message}")
            ModelListResponse(count = ChattyMockDataSource.mockFavorites.size, models = ChattyMockDataSource.mockFavorites)
        }
    }

    suspend fun addFavorite(token: String, modelId: String): Boolean {
        return try {
            val res = httpClient.post("$baseUrl/api/models/favorite") {
                bearerAuth(token)
                setBody(mapOf("model_id" to modelId))
            }
            res.status.isSuccess()
        } catch (t: Throwable) {
            Log.w(tag, "addFavorite falling back to mock: ${t.message}")
            true
        }
    }

    suspend fun removeFavorite(token: String, modelId: String): Boolean {
        return try {
            val res = httpClient.post("$baseUrl/api/models/favorite") {
                bearerAuth(token)
                setBody(mapOf("model_id" to modelId))
            }
            res.status.isSuccess()
        } catch (t: Throwable) {
            Log.w(tag, "removeFavorite falling back to mock: ${t.message}")
            true
        }
    }

    // ── 8. Report & User Profile ──────────────────────────────────────────────
    suspend fun reportUser(token: String, reportedId: String, reason: String): Boolean {
        return try {
            val res = httpClient.post("$baseUrl/api/reports") {
                bearerAuth(token)
                setBody(ReportRequest(reported_id = reportedId, reason = reason))
            }
            res.status.isSuccess()
        } catch (t: Throwable) {
            Log.w(tag, "reportUser falling back to mock: ${t.message}")
            true
        }
    }

    suspend fun getUserProfile(token: String): UserDto {
        return try {
            val res: ApiResponse<UserDto> = httpClient.get("$baseUrl/api/user/profile") {
                bearerAuth(token)
            }.body()
            res.data ?: ChattyMockDataSource.mockUsers.first()
        } catch (t: Throwable) {
            Log.w(tag, "getUserProfile falling back to mock: ${t.message}")
            ChattyMockDataSource.mockUsers.first()
        }
    }

    suspend fun submitOnboarding(token: String, bio: String, voiceRate: Double, chatRate: Double): Boolean {
        return try {
            val res = httpClient.post("$baseUrl/api/models/onboarding") {
                bearerAuth(token)
                setBody(ModelOnboardRequest(bio = bio, voice_rate_per_min = voiceRate, group_rate_per_min = voiceRate / 2, chat_rate_per_msg = chatRate))
            }
            res.status.isSuccess()
        } catch (t: Throwable) {
            Log.w(tag, "submitOnboarding falling back to mock: ${t.message}")
            true
        }
    }

    // ── 9. LiveKit Call Token ────────────────────────────────────────────────
    suspend fun getCallToken(token: String, remoteUserId: String, callType: String = "voice"): LiveKitTokenResponse {
        return try {
            val res: ApiResponse<LiveKitTokenResponse> = httpClient.post("$baseUrl/api/calls/token") {
                bearerAuth(token)
                setBody(mapOf("remote_user_id" to remoteUserId, "call_type" to callType))
            }.body()
            res.data ?: LiveKitTokenResponse(
                token = "simulated_livekit_token",
                room_name = "room_$remoteUserId",
                livekit_url = "wss://connecto-7sxi06vp.livekit.cloud"
            )
        } catch (t: Throwable) {
            Log.w(tag, "getCallToken falling back to mock: ${t.message}")
            LiveKitTokenResponse(
                token = "simulated_livekit_token",
                room_name = "room_$remoteUserId",
                livekit_url = "wss://connecto-7sxi06vp.livekit.cloud"
            )
        }
    }

    // ── 10. Live Streaming ──────────────────────────────────────────────────
    suspend fun getLiveStreams(token: String): List<LiveStreamDto> {
        return try {
            val res: ApiResponse<LiveStreamListResponse> = httpClient.get("$baseUrl/api/live/list") {
                bearerAuth(token)
            }.body()
            val streams = res.data?.streams
            if (!streams.isNullOrEmpty()) {
                streams
            } else {
                ChattyMockDataSource.mockLiveStreams
            }
        } catch (t: Throwable) {
            Log.w(tag, "getLiveStreams falling back to mock: ${t.message}")
            ChattyMockDataSource.mockLiveStreams
        }
    }

    suspend fun startLiveStream(token: String, title: String): StartLiveResponse {
        return try {
            val res: ApiResponse<StartLiveResponse> = httpClient.post("$baseUrl/api/live/start") {
                bearerAuth(token)
                setBody(mapOf("title" to title))
            }.body()
            res.data ?: StartLiveResponse(
                stream = LiveStreamDto(
                    stream_id = "live_${System.currentTimeMillis()}",
                    host_id = "me",
                    host_name = "Host",
                    title = title,
                    is_active = true
                ),
                token = "simulated_host_token",
                livekit_url = "wss://connecto-7sxi06vp.livekit.cloud"
            )
        } catch (t: Throwable) {
            Log.w(tag, "startLiveStream falling back to mock: ${t.message}")
            StartLiveResponse(
                stream = LiveStreamDto(
                    stream_id = "live_${System.currentTimeMillis()}",
                    host_id = "me",
                    host_name = "Host",
                    title = title,
                    is_active = true
                ),
                token = "simulated_host_token",
                livekit_url = "wss://connecto-7sxi06vp.livekit.cloud"
            )
        }
    }

    suspend fun endLiveStream(token: String, streamId: String): Boolean {
        return try {
            val res = httpClient.post("$baseUrl/api/live/end") {
                bearerAuth(token)
                setBody(mapOf("stream_id" to streamId))
            }
            res.status.isSuccess()
        } catch (t: Throwable) {
            Log.w(tag, "endLiveStream falling back to mock: ${t.message}")
            true
        }
    }

    suspend fun joinLiveStream(token: String, streamId: String): JoinLiveResponse {
        return try {
            val res: ApiResponse<JoinLiveResponse> = httpClient.post("$baseUrl/api/live/join") {
                bearerAuth(token)
                setBody(mapOf("stream_id" to streamId))
            }.body()
            res.data ?: JoinLiveResponse(
                token = "simulated_viewer_token",
                livekit_url = "wss://connecto-7sxi06vp.livekit.cloud",
                room_name = "room_$streamId"
            )
        } catch (t: Throwable) {
            Log.w(tag, "joinLiveStream falling back to mock: ${t.message}")
            JoinLiveResponse(
                token = "simulated_viewer_token",
                livekit_url = "wss://connecto-7sxi06vp.livekit.cloud",
                room_name = "room_$streamId"
            )
        }
    }

    suspend fun sendLiveTip(token: String, streamId: String, amount: Double, giftName: String): Boolean {
        return try {
            val res = httpClient.post("$baseUrl/api/live/tip") {
                bearerAuth(token)
                setBody(LiveTipRequest(stream_id = streamId, amount = amount, gift_name = giftName))
            }
            res.status.isSuccess()
        } catch (t: Throwable) {
            Log.w(tag, "sendLiveTip falling back to mock: ${t.message}")
            true
        }
    }

    suspend fun setLivePaidMode(token: String, streamId: String, isPaidMode: Boolean, coinRatePerMin: Double): LiveStreamDto? {
        return try {
            val res: ApiResponse<LivePaidModeResponse> = httpClient.post("$baseUrl/api/live/paid_mode") {
                bearerAuth(token)
                setBody(LivePaidModeRequest(stream_id = streamId, is_paid_mode = isPaidMode, coin_rate_per_min = coinRatePerMin))
            }.body()
            res.data?.stream
        } catch (t: Throwable) {
            Log.w(tag, "setLivePaidMode falling back to mock: ${t.message}")
            LiveStreamDto(
                stream_id = streamId,
                is_paid_mode = isPaidMode,
                coin_rate_per_min = coinRatePerMin
            )
        }
    }

    suspend fun getLiveStreamStatus(token: String, streamId: String): LiveStreamDto? {
        return try {
            val res: ApiResponse<LiveStatusResponse> = httpClient.get("$baseUrl/api/live/status") {
                bearerAuth(token)
                url {
                    parameters.append("stream_id", streamId)
                }
            }.body()
            res.data?.stream ?: ChattyMockDataSource.mockLiveStreams.find { it.stream_id == streamId }
        } catch (t: Throwable) {
            Log.w(tag, "getLiveStreamStatus falling back to mock: ${t.message}")
            ChattyMockDataSource.mockLiveStreams.find { it.stream_id == streamId } ?: ChattyMockDataSource.mockLiveStreams.first()
        }
    }

    suspend fun deductLiveCoins(token: String, streamId: String, durationSeconds: Int = 60): LiveDeductResponse {
        return try {
            val res: ApiResponse<LiveDeductResponse> = httpClient.post("$baseUrl/api/live/deduct") {
                bearerAuth(token)
                setBody(LiveDeductRequest(stream_id = streamId, duration_seconds = durationSeconds))
            }.body()
            res.data ?: LiveDeductResponse(
                success = true,
                deducted = 10.0,
                balance = 990.0
            )
        } catch (t: Throwable) {
            Log.w(tag, "deductLiveCoins falling back to mock: ${t.message}")
            LiveDeductResponse(
                success = true,
                deducted = 10.0,
                balance = 990.0
            )
        }
    }

    fun getWsUrl(token: String): String {
        val wsBase = baseUrl.replace("http://", "ws://").replace("https://", "wss://")
        return "$wsBase/ws?token=$token"
    }
}
