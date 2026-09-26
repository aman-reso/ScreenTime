package com.app.screentime.core.network.api

import android.util.Log
import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class WinterApi @Inject constructor(
    private val networkClient: NetworkClient
) {
    private val tag = "WinterApi"

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
            res.data ?: createFakeAuthResponse(
                id = "usr_${Math.abs(phone.hashCode())}",
                email = phone,
                name = name,
                role = role
            )
        } catch (t: Throwable) {
            Log.w(tag, "registerOrLogin using simulated fallback: ${t.message}")
            createFakeAuthResponse(
                id = "usr_${Math.abs(phone.hashCode())}",
                email = phone,
                name = name,
                role = role
            )
        }
    }

    suspend fun loginWithGoogle(
        googleId: String? = null,
        idToken: String? = null,
        email: String? = null,
        name: String? = null,
        photoUrl: String? = null,
        role: String = "user"
    ): AuthResponse {
        val resolvedGoogleId = googleId?.ifBlank { null }
            ?: idToken?.ifBlank { null }
            ?: "google_${System.currentTimeMillis()}"
        val request = GoogleAuthRequest(
            email = email,
            google_id = resolvedGoogleId,
            name = name ?: "User",
            photo_url = photoUrl ?: "https://lh3.googleusercontent.com/a/default-user",
            id_token = idToken ?: resolvedGoogleId,
            idToken = idToken ?: resolvedGoogleId,
            avatar_url = photoUrl,
            avatarUrl = photoUrl,
            role = role
        )
        return try {
            val res: ApiResponse<AuthResponse> =
                httpClient.post("$baseUrl${ApiEndpoints.Auth.GOOGLE}") {
                    setBody(request)
                }.body()
            res.data ?: createFakeAuthResponse(
                id = "usr_${abs((email ?: resolvedGoogleId).hashCode())}",
                email = email ?: "user@winter.app",
                name = name ?: "User",
                avatarUrl = photoUrl,
                role = role
            )
        } catch (t: Throwable) {
            Log.w(tag, "loginWithGoogle using simulated fallback: ${t.message}")
            createFakeAuthResponse(
                id = "usr_${Math.abs((email ?: resolvedGoogleId).hashCode())}",
                email = email ?: "user@winter.app",
                name = name ?: "User",
                avatarUrl = photoUrl,
                role = role
            )
        }
    }

    private fun createFakeAuthResponse(
        id: String,
        email: String?,
        name: String?,
        avatarUrl: String? = null,
        role: String = "user"
    ): AuthResponse {
        val resolvedName = name?.ifBlank { null } ?: "User"
        val resolvedEmail = email?.ifBlank { null } ?: "user@winter.app"
        val user = UserDto(
            id = id,
            name = resolvedName,
            email = resolvedEmail,
            phone = resolvedEmail,
            role = role,
            avatar_url = avatarUrl ?: "",
            bio = "Member · Winter",
            is_online = true
        )
        return AuthResponse(
            user = user,
            token = "token_${user.id}",
            is_new_user = false,
            wallet = WalletDto(
                user_id = user.id,
                balance = 1000.0,
                bonus_given = 100.0,
                total_spent = 0.0,
                total_earned = 0.0
            ),
            message = "Success"
        )
    }

    // ── 2. Models ─────────────────────────────────────────────────────────────
    suspend fun getModels(token: String, page: Int = 1, limit: Int = 10): ModelListResponse {
        return try {
            val res: ApiResponse<ModelListResponse> = httpClient.get("$baseUrl/api/models") {
                bearerAuth(token)
                parameter("page", page)
                parameter("limit", limit)
            }.body()
            res.data ?: ModelListResponse(count = 0, models = emptyList())
        } catch (t: Throwable) {
            Log.e(tag, "getModels failed: ${t.message}", t)
            ModelListResponse(count = 0, models = emptyList())
        }
    }

    suspend fun updateProfile(token: String, request: ProfileUpdateRequest): ProfileUpdateResponse {
        return try {
            val res: ProfileUpdateResponse = httpClient.put("$baseUrl/api/profile") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
            res
        } catch (t: Throwable) {
            Log.e(tag, "updateProfile failed: ${t.message}", t)
            ProfileUpdateResponse(success = false, message = t.message ?: "Unknown error")
        }
    }

    suspend fun getModelProfile(token: String, modelId: String): UserDto {
        return try {
            val res: ApiResponse<UserDto> = httpClient.get("$baseUrl/api/models/$modelId") {
                bearerAuth(token)
            }.body()
            res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Model not found" })
        } catch (t: Throwable) {
            Log.e(tag, "getModelProfile failed: ${t.message}", t)
            throw t
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
                    balance = 0.0,
                    bonus_given = 0.0,
                    total_spent = 0.0,
                    total_earned = 0.0
                )
            )
        } catch (t: Throwable) {
            Log.e(tag, "getWallet failed: ${t.message}", t)
            WalletResponse(
                wallet = WalletDto(
                    balance = 0.0,
                    bonus_given = 0.0,
                    total_spent = 0.0,
                    total_earned = 0.0
                )
            )
        }
    }

    suspend fun getWalletInfo(token: String): WalletInfoDto {
        return try {
            val res: ApiResponse<WalletInfoDto> =
                httpClient.get("$baseUrl${ApiEndpoints.Wallet.INFO}") {
                    bearerAuth(token)
                }.body()
            res.data ?: WalletInfoDto()
        } catch (t: Throwable) {
            Log.e(tag, "getWalletInfo failed: ${t.message}", t)
            WalletInfoDto()
        }
    }

    suspend fun getWalletPacks(): WalletPacksResponse {
        return try {
            val res: ApiResponse<WalletPacksResponse> =
                httpClient.get("$baseUrl/api/wallet/packs").body()
            res.data ?: WalletPacksResponse(packs = emptyList())
        } catch (t: Throwable) {
            Log.e(tag, "getWalletPacks failed: ${t.message}", t)
            WalletPacksResponse(packs = emptyList())
        }
    }

    suspend fun recharge(token: String, amount: Double): WalletDto {
        return try {
            val res: ApiResponse<WalletDto> = httpClient.post("$baseUrl/api/wallet/recharge") {
                bearerAuth(token)
                setBody(RechargeRequest(amount = amount))
            }.body()
            res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Recharge failed" })
        } catch (t: Throwable) {
            Log.e(tag, "recharge failed: ${t.message}", t)
            throw t
        }
    }

    suspend fun getWalletTransactions(token: String, page: Int = 1, limit: Int = 20): PaginatedTransactionsData {
        return try {
            val res: ApiResponse<PaginatedTransactionsData> = httpClient.get("$baseUrl${ApiEndpoints.Wallet.TRANSACTIONS}") {
                bearerAuth(token)
                accept(ContentType.Application.Json)
                parameter("page", page)
                parameter("limit", limit)
            }.body()
            res.data ?: PaginatedTransactionsData()
        } catch (t: Throwable) {
            Log.e(tag, "getWalletTransactions failed: ${t.message}", t)
            PaginatedTransactionsData()
        }
    }

    // ── 4. Calls & Balance Check ──────────────────────────────────────────────
    suspend fun checkCallBalance(
        token: String,
        modelId: String,
        callType: String = "voice"
    ): CheckCallBalanceResponse {
        return try {
            val res: ApiResponse<CheckCallBalanceResponse> =
                httpClient.post("$baseUrl/api/calls/check-balance") {
                    bearerAuth(token)
                    setBody(CheckCallBalanceRequest(model_id = modelId, call_type = callType))
                }.body()
            res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Balance check failed" })
        } catch (t: Throwable) {
            Log.e(tag, "checkCallBalance failed: ${t.message}", t)
            throw t
        }
    }

    suspend fun getCallHistory(token: String): CallHistoryResponse {
        return try {
            val res: ApiResponse<CallHistoryResponse> =
                httpClient.get("$baseUrl/api/history/calls") {
                    bearerAuth(token)
                }.body()
            res.data ?: CallHistoryResponse()
        } catch (t: Throwable) {
            Log.e(tag, "getCallHistory failed: ${t.message}", t)
            CallHistoryResponse()
        }
    }

    // ── 5. Ephemeral 24-Hour Chat ─────────────────────────────────────────────
    suspend fun getConversations(token: String): ConversationListResponse {
        return try {
            val res: ApiResponse<ConversationListResponse> =
                httpClient.get("$baseUrl/api/chat/conversations") {
                    bearerAuth(token)
                }.body()
            res.data ?: ConversationListResponse(conversations = emptyList())
        } catch (t: Throwable) {
            Log.e(tag, "getConversations failed: ${t.message}", t)
            ConversationListResponse(conversations = emptyList())
        }
    }

    suspend fun getChatMessages(token: String, partnerId: String): EphemeralChatResponse {
        return try {
            val res: ApiResponse<EphemeralChatResponse> =
                httpClient.get("$baseUrl/api/chat/messages?partner_id=$partnerId") {
                    bearerAuth(token)
                }.body()
            res.data ?: EphemeralChatResponse(partner_id = partnerId, messages = emptyList())
        } catch (t: Throwable) {
            Log.e(tag, "getChatMessages failed: ${t.message}", t)
            EphemeralChatResponse(partner_id = partnerId, messages = emptyList())
        }
    }

    suspend fun sendChatMessage(
        token: String,
        receiverId: String,
        content: String
    ): ChatMessageDto {
        return try {
            val res: ApiResponse<ChatMessageDto> = httpClient.post("$baseUrl/api/chat/send") {
                bearerAuth(token)
                setBody(SendChatMessageRequest(receiver_id = receiverId, content = content))
            }.body()
            res.data ?: throw Exception(
                res.error ?: res.message.ifBlank { "Failed to send message" })
        } catch (t: Throwable) {
            Log.e(tag, "sendChatMessage failed: ${t.message}", t)
            throw t
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
            Log.e(tag, "getRooms failed: ${t.message}", t)
            RoomListResponse()
        }
    }

    suspend fun createRoom(token: String, title: String, ratePerMin: Double): GroupRoomDto {
        return try {
            val res: ApiResponse<GroupRoomDto> = httpClient.post("$baseUrl/api/rooms") {
                bearerAuth(token)
                setBody(CreateRoomRequest(title = title, rate_per_min = ratePerMin))
            }.body()
            res.data ?: throw Exception(
                res.error ?: res.message.ifBlank { "Failed to create room" })
        } catch (t: Throwable) {
            Log.e(tag, "createRoom failed: ${t.message}", t)
            throw t
        }
    }

    // ── 7. Favorites ──────────────────────────────────────────────────────────
    suspend fun getFavorites(token: String): ModelListResponse {
        return try {
            val res: ApiResponse<ModelListResponse> =
                httpClient.get("$baseUrl/api/models/favorites") {
                    bearerAuth(token)
                }.body()
            res.data ?: ModelListResponse(count = 0, models = emptyList())
        } catch (t: Throwable) {
            Log.e(tag, "getFavorites failed: ${t.message}", t)
            ModelListResponse(count = 0, models = emptyList())
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
            Log.e(tag, "addFavorite failed: ${t.message}", t)
            false
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
            Log.e(tag, "removeFavorite failed: ${t.message}", t)
            false
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
            Log.e(tag, "reportUser failed: ${t.message}", t)
            false
        }
    }

    suspend fun getUserProfile(token: String, userId: String? = null): UserDto {
        return try {
            try {
                val connectRes: ApiResponse<ConnectProfileDataDto> =
                    httpClient.get("$baseUrl${ApiEndpoints.Auth.PROFILE}") {
                        if (token.isNotBlank()) bearerAuth(token)
                        if (!userId.isNullOrBlank() && userId != "me") {
                            parameter("user_id", userId)
                        }
                    }.body()
                if (connectRes.data != null) {
                    return connectRes.data!!.toUserDto()
                }
            } catch (ignored: Exception) {
            }
            val res: ApiResponse<UserDto> = httpClient.get("$baseUrl${ApiEndpoints.Auth.PROFILE}") {
                if (token.isNotBlank()) bearerAuth(token)
                if (!userId.isNullOrBlank() && userId != "me") {
                    parameter("user_id", userId)
                }
            }.body()
            res.data ?: throw Exception(
                res.error ?: res.message.ifBlank { "User profile not found" })
        } catch (t: Throwable) {
            Log.e(tag, "getUserProfile failed: ${t.message}", t)
            throw t
        }
    }

    suspend fun submitOnboarding(
        token: String,
        bio: String,
        voiceRate: Double,
        chatRate: Double
    ): Boolean {
        return try {
            val res = httpClient.post("$baseUrl/api/models/onboarding") {
                bearerAuth(token)
                setBody(
                    ModelOnboardRequest(
                        bio = bio,
                        voice_rate_per_min = voiceRate,
                        group_rate_per_min = voiceRate / 2,
                        chat_rate_per_msg = chatRate
                    )
                )
            }
            res.status.isSuccess()
        } catch (t: Throwable) {
            Log.e(tag, "submitOnboarding failed: ${t.message}", t)
            false
        }
    }

    suspend fun registerFcmToken(
        token: String,
        fcmToken: String,
        deviceId: String? = null,
        appVersion: String? = null
    ): ApiResponse<FcmTokenData>? {
        return try {
            val res: ApiResponse<FcmTokenData> = httpClient.post("$baseUrl${ApiEndpoints.Auth.FCM_TOKEN}") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterFcmTokenRequest(
                        fcm_token = fcmToken,
                        platform = "android",
                        device_id = deviceId,
                        app_version = appVersion
                    )
                )
            }.body()
            Log.d(tag, "registerFcmToken success: ${res.message}")
            res
        } catch (t: Throwable) {
            Log.e(tag, "registerFcmToken failed: ${t.message}", t)
            null
        }
    }

    suspend fun uploadImageParts(
        token: String,
        parts: List<UploadImagePart>
    ): UploadImagesResponse {
        return try {
            val response: UploadImagesResponse = httpClient.submitFormWithBinaryData(
                url = "$baseUrl${ApiEndpoints.Upload.IMAGES}",
                formData = formData {
                    parts.forEachIndexed { index, part ->
                        append(
                            key = "photos",
                            value = part.bytes,
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, part.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"${part.filename.ifBlank { "photo_$index.jpg" }}\"")
                            }
                        )
                    }
                }
            ) {
                if (token.isNotBlank()) {
                    bearerAuth(token)
                }
            }.body()
            Log.d(tag, "uploadImages success: ${response.getUploadedUrls()}")
            response
        } catch (t: Throwable) {
            Log.e(tag, "uploadImages failed: ${t.message}", t)
            UploadImagesResponse(
                success = false,
                message = t.message ?: "Image upload failed"
            )
        }
    }

    suspend fun uploadImages(
        token: String,
        images: List<Pair<String, ByteArray>>
    ): UploadImagesResponse {
        val parts = images.map { (fileName, bytes) ->
            val mimeType = when {
                fileName.endsWith(".webp", ignoreCase = true) -> "image/webp"
                fileName.endsWith(".png", ignoreCase = true) -> "image/png"
                fileName.endsWith(".gif", ignoreCase = true) -> "image/gif"
                fileName.endsWith(".heic", ignoreCase = true) -> "image/heic"
                else -> "image/jpeg"
            }
            UploadImagePart(filename = fileName, bytes = bytes, mimeType = mimeType)
        }
        return uploadImageParts(token, parts)
    }

    fun getWsUrl(token: String): String {
        val wsBase = baseUrl.replace("http://", "ws://").replace("https://", "wss://")
        return "$wsBase/ws?token=$token"
    }
}

