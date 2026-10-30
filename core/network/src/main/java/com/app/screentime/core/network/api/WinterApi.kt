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

    private fun formatFullPhoneNumber(phone: String): String {
        val trimmed = phone.trim()
        if (trimmed.startsWith("+")) {
            val digits = trimmed.drop(1).filter { it.isDigit() }
            return "+$digits"
        }
        val cleanDigits = trimmed.filter { it.isDigit() }
        return when {
            cleanDigits.length == 10 -> "+91$cleanDigits"
            cleanDigits.length > 10 && cleanDigits.startsWith("91") -> "+$cleanDigits"
            else -> "+$cleanDigits"
        }
    }

    suspend fun sendOtp(phone: String): SendOtpResponse {
        val fullPhone = formatFullPhoneNumber(phone)
        val res: SendOtpResponse = httpClient.post("$baseUrl${ApiEndpoints.Auth.OTP_SEND}") {
            contentType(ContentType.Application.Json)
            setBody(SendOtpRequest(phone = fullPhone))
        }.body()
        return res
    }

    suspend fun verifyOtp(phone: String, code: String): AuthResponse {
        val fullPhone = formatFullPhoneNumber(phone)
        val res: VerifyOtpResponse = httpClient.post("$baseUrl${ApiEndpoints.Auth.OTP_VERIFY}") {
            contentType(ContentType.Application.Json)
            setBody(VerifyOtpRequest(phone = fullPhone, code = code))
        }.body()

        val data = res.data ?: throw Exception(res.message.ifBlank { "OTP verification failed" })
        if (data.token.isBlank()) {
            throw Exception(data.message.ifBlank { res.message.ifBlank { "OTP verification failed" } })
        }
        val userDto = data.user
            ?: throw Exception(res.message.ifBlank { "User profile unavailable in OTP verification response" })
        return AuthResponse(
            user = userDto,
            token = data.token,
            is_new_user = data.is_new_user,
            has_profile = data.has_profile,
            profile_complete = data.profile_complete,
            message = res.message.ifBlank { data.message }
        )
    }

    // ── 1. Auth ───────────────────────────────────────────────────────────────
    suspend fun registerOrLogin(phone: String, name: String, role: String): AuthResponse {
        val res: ApiResponse<AuthResponse> = httpClient.post("$baseUrl/api/auth") {
            setBody(RegisterRequest(phone = phone, name = name, role = role))
        }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "Registration or login failed" })
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
        val res: ApiResponse<AuthResponse> =
            httpClient.post("$baseUrl${ApiEndpoints.Auth.GOOGLE}") {
                setBody(request)
            }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "Google authentication failed" })
    }

    // ── 2. Models ─────────────────────────────────────────────────────────────
    suspend fun getModels(token: String, page: Int = 1, limit: Int = 10): ModelListResponse {
        val res: ApiResponse<ModelListResponse> = httpClient.get("$baseUrl/api/models") {
            bearerAuth(token)
            parameter("page", page)
            parameter("limit", limit)
        }.body()
        return res.data ?: ModelListResponse(count = 0, models = emptyList())
    }

    suspend fun updateProfile(token: String, request: ProfileUpdateRequest): ProfileUpdateResponse {
        return httpClient.put("$baseUrl/api/profile") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updatePreferences(
        token: String,
        request: UpdatePreferencesRequest
    ): PreferencesResponse {
        val response = httpClient.put("$baseUrl${ApiEndpoints.Preferences.PREFERENCES}") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return if (response.status.isSuccess()) {
            try {
                response.body<PreferencesResponse>()
            } catch (_: Exception) {
                PreferencesResponse(success = true, message = "Preferences updated")
            }
        } else {
            throw Exception("Failed to update preferences: HTTP ${response.status.value}")
        }
    }

    suspend fun updateProfileVisibility(
        token: String,
        visibility: String
    ): Result<ProfileVisibilityResponseData> {
        val normalized = visibility.trim().lowercase()
        val request = ProfileVisibilityRequest(
            visibility = normalized,
            is_private = normalized == "private",
            discovery_enabled = normalized != "private"
        )
        val res: ApiResponse<ProfileVisibilityResponseData> =
            httpClient.put("$baseUrl${ApiEndpoints.Auth.VISIBILITY}") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

        val data = res.data ?: ProfileVisibilityResponseData(
            visibility = normalized,
            is_private = normalized == "private",
            discovery_enabled = normalized != "private"
        )
        return Result.success(data)
    }

    suspend fun getModelProfile(token: String, modelId: String): UserDto {
        val res: ApiResponse<UserDto> = httpClient.get("$baseUrl/api/models/$modelId") {
            bearerAuth(token)
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Model not found" })
    }

    // ── 3. Wallet & Packs ─────────────────────────────────────────────────────
    suspend fun getWallet(token: String): WalletResponse {
        val res: ApiResponse<WalletResponse> = httpClient.get("$baseUrl/api/wallet") {
            bearerAuth(token)
        }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "Failed to fetch wallet" })
    }

    suspend fun getWalletInfo(token: String): WalletInfoDto {
        val res: ApiResponse<WalletInfoDto> =
            httpClient.get("$baseUrl${ApiEndpoints.Wallet.INFO}") {
                bearerAuth(token)
            }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "Failed to fetch wallet info" })
    }

    suspend fun getWalletPackages(token: String? = null): List<WalletPackDto> {
        val res: ApiResponse<List<WalletPackDto>> =
            httpClient.get("$baseUrl${ApiEndpoints.Wallet.PACKAGES}") {
                if (!token.isNullOrBlank()) {
                    bearerAuth(token)
                }
            }.body()
        return res.data ?: emptyList()
    }

    suspend fun getWalletPacks(): WalletPacksResponse {
        val list = getWalletPackages()
        return WalletPacksResponse(count = list.size, packs = list)
    }

    suspend fun recharge(token: String, amount: Double): WalletDto {
        val res: ApiResponse<WalletDto> = httpClient.post("$baseUrl/api/wallet/recharge") {
            bearerAuth(token)
            setBody(RechargeRequest(amount = amount))
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Recharge failed" })
    }

    suspend fun verifyGooglePlayPurchase(
        token: String,
        purchaseToken: String,
        productId: String,
        orderId: String? = null,
        packId: String? = null,
        credits: Int = 0,
        amount: Double = 0.0
    ): Result<GooglePlayPurchaseVerifyResponse> {
        return try {
            val res: ApiResponse<GooglePlayPurchaseVerifyResponse> =
                httpClient.post("$baseUrl${ApiEndpoints.Wallet.GOOGLE_PLAY_VERIFY}") {
                    if (token.isNotBlank()) bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    setBody(
                        GooglePlayPurchaseVerifyRequest(
                            purchase_token = purchaseToken,
                            product_id = productId,
                            order_id = orderId,
                            package_id = packId,
                            credits = credits,
                            amount = amount
                        )
                    )
                }.body()

            val data = res.data
            if (data != null && res.success) {
                Result.success(data)
            } else {
                Result.failure(
                    Exception(
                        res.error ?: res.message.ifBlank { "Verification failed" })
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBillingProducts(token: String): Result<BillingCatalogDataDto> {
        return try {
            val url = ApiEndpoints.Billing.getProductsUrl()
            val response: BillingProductsResponse = httpClient.get(url) {
                if (token.isNotBlank()) bearerAuth(token)
                contentType(ContentType.Application.Json)
            }.body()

            val data = response.data
            if (data != null && (response.success || response.status.equals(
                    "success",
                    ignoreCase = true
                ) || response.status == "200")
            ) {
                Result.success(data)
            } else {
                Result.failure(Exception(response.message.ifBlank { "Failed to retrieve billing products" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyGooglePurchase(
        token: String,
        request: VerifyGooglePurchaseRequest
    ): Result<VerifyGooglePurchaseDataDto> {
        return try {
            val url = ApiEndpoints.Billing.getVerifyUrl()
            val response: VerifyGooglePurchaseResponse = httpClient.post(url) {
                if (token.isNotBlank()) bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

            val data = response.data
            if (data != null && (response.success || response.status.equals(
                    "success",
                    ignoreCase = true
                ) || response.status == "200" || data.success)
            ) {
                Result.success(data)
            } else {
                Result.failure(Exception(response.message.ifBlank { "Verification failed" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrderStatus(token: String, orderId: String): Result<WalletOrderStatusDto> {
        val res: ApiResponse<WalletOrderStatusDto> =
            httpClient.get("$baseUrl${ApiEndpoints.Wallet.ORDER_STATUS}") {
                if (token.isNotBlank()) bearerAuth(token)
                parameter("order_id", orderId)
            }.body()

        val statusData = res.data
        if (statusData != null) {
            return Result.success(statusData)
        } else {
            return Result.failure(
                Exception(
                    res.error ?: res.message.ifBlank { "Failed to retrieve order status" })
            )
        }
    }

    suspend fun getWalletTransactions(
        token: String,
        page: Int = 1,
        limit: Int = 20
    ): PaginatedTransactionsData {
        val res: ApiResponse<PaginatedTransactionsData> =
            httpClient.get("$baseUrl${ApiEndpoints.Wallet.TRANSACTIONS}") {
                bearerAuth(token)
                accept(ContentType.Application.Json)
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "Failed to fetch transactions" })
    }

    // ── 4. Calls & Balance Check ──────────────────────────────────────────────
    suspend fun checkCallBalance(
        token: String,
        modelId: String,
        callType: String = "voice"
    ): CheckCallBalanceResponse {
        val res: ApiResponse<CheckCallBalanceResponse> =
            httpClient.post("$baseUrl/api/calls/check-balance") {
                bearerAuth(token)
                setBody(CheckCallBalanceRequest(model_id = modelId, call_type = callType))
            }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "Balance check failed" })
    }

    suspend fun getCallHistory(token: String): CallHistoryResponse {
        val res: ApiResponse<CallHistoryResponse> =
            httpClient.get("$baseUrl/api/history/calls") {
                bearerAuth(token)
            }.body()
        return res.data ?: CallHistoryResponse()
    }

    suspend fun acceptCall(
        token: String,
        callId: String
    ): Boolean {
        return try {
            val res: ApiResponse<CallSessionResponse> =
                httpClient.post("$baseUrl/api/calls/accept") {
                    bearerAuth(token)
                    setBody(AcceptCallRequest(session_id = callId, call_id = callId))
                }.body()
            res.success || res.data != null
        } catch (e: Exception) {
            Log.e("WinterApi", "acceptCall HTTP failed: ${e.message}")
            false
        }
    }

    suspend fun endCall(
        token: String,
        callId: String,
        durationSeconds: Int = 0
    ): Boolean {
        return try {
            val res: ApiResponse<CallSessionResponse> =
                httpClient.post("$baseUrl/api/calls/end") {
                    bearerAuth(token)
                    setBody(EndCallRequest(session_id = callId, call_id = callId, duration_seconds = durationSeconds))
                }.body()
            res.success || res.data != null
        } catch (e: Exception) {
            Log.e("WinterApi", "endCall HTTP failed: ${e.message}")
            false
        }
    }

    suspend fun rejectCall(
        token: String,
        callId: String
    ): Boolean {
        return try {
            val res: ApiResponse<CallSessionResponse> =
                httpClient.post("$baseUrl/api/calls/reject") {
                    bearerAuth(token)
                    setBody(RejectCallRequest(session_id = callId, call_id = callId))
                }.body()
            res.success || res.data != null
        } catch (e: Exception) {
            Log.e("WinterApi", "rejectCall HTTP failed: ${e.message}")
            false
        }
    }

    suspend fun getConversations(token: String): ConversationListResponse {
        val res: ApiResponse<ConversationListResponse> =
            httpClient.get("$baseUrl/api/chat/conversations") {
                bearerAuth(token)
            }.body()
        return res.data ?: ConversationListResponse(conversations = emptyList())
    }

    suspend fun getChatMessages(token: String, partnerId: String): EphemeralChatResponse {
        val res: ApiResponse<EphemeralChatResponse> =
            httpClient.get("$baseUrl/api/chat/messages?partner_id=$partnerId") {
                bearerAuth(token)
            }.body()
        return res.data ?: EphemeralChatResponse(partner_id = partnerId, messages = emptyList())
    }

    suspend fun sendChatMessage(
        token: String,
        receiverId: String,
        content: String
    ): ChatMessageDto {
        val res: ApiResponse<ChatMessageDto> = httpClient.post("$baseUrl/api/chat/send") {
            bearerAuth(token)
            setBody(SendChatMessageRequest(receiver_id = receiverId, content = content))
        }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "Failed to send message" })
    }


    // ── 7. Favorites ──────────────────────────────────────────────────────────
    suspend fun getFavorites(token: String): ModelListResponse {
        val res: ApiResponse<ModelListResponse> =
            httpClient.get("$baseUrl/api/discovery/following") {
                bearerAuth(token)
            }.body()
        return res.data ?: ModelListResponse(count = 0, models = emptyList())
    }

    suspend fun addFavorite(token: String, modelId: String): Boolean {
        val res = httpClient.post("$baseUrl/api/models/favorite") {
            bearerAuth(token)
            setBody(mapOf("model_id" to modelId))
        }
        return res.status.isSuccess()
    }


    suspend fun reportUser(token: String, reportedId: String, reason: String): Boolean {
        val res = httpClient.post("$baseUrl/api/reports") {
            bearerAuth(token)
            setBody(ReportRequest(reported_id = reportedId, reason = reason))
        }
        return res.status.isSuccess()
    }

    suspend fun getUserProfile(token: String, userId: String? = null): UserDto {
        try {
            val connectRes: ApiResponse<ConnectProfileDataDto> =
                httpClient.get("$baseUrl${ApiEndpoints.Auth.PROFILE}") {
                    if (token.isNotBlank()) bearerAuth(token)
                    if (!userId.isNullOrBlank() && userId != "me") {
                        parameter("user_id", userId)
                    }
                }.body()
            if (connectRes.data != null) {
                return connectRes.data.toUserDto()
            }
        } catch (_: Exception) {
        }
        val res: ApiResponse<UserDto> = httpClient.get("$baseUrl${ApiEndpoints.Auth.PROFILE}") {
            if (token.isNotBlank()) bearerAuth(token)
            if (!userId.isNullOrBlank() && userId != "me") {
                parameter("user_id", userId)
            }
        }.body()
        return res.data ?: throw Exception(
            res.error ?: res.message.ifBlank { "User profile not found" })
    }

    suspend fun submitOnboarding(
        token: String,
        bio: String,
        voiceRate: Double,
        chatRate: Double
    ): Boolean {
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
        return res.status.isSuccess()
    }

    suspend fun registerFcmToken(
        token: String,
        fcmToken: String,
        deviceId: String? = null,
        appVersion: String? = null
    ): ApiResponse<FcmTokenData>? {
        val res: ApiResponse<FcmTokenData> =
            httpClient.post("$baseUrl${ApiEndpoints.Auth.FCM_TOKEN}") {
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
        return res
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
                        val defaultKey =
                            if (part.mimeType.startsWith("video/")) "video" else "images"
                        val key = part.formKey ?: defaultKey
                        append(
                            key = key,
                            value = part.bytes,
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, part.mimeType)
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "filename=\"${
                                        part.filename.ifBlank {
                                            if (part.mimeType.startsWith(
                                                    "video/"
                                                )
                                            ) "video_$index.mp4" else "photo_$index.jpg"
                                        }
                                    }\""
                                )
                            }
                        )
                    }
                }
            ) {
                if (token.isNotBlank()) {
                    bearerAuth(token)
                }
            }.body()
            Log.d(
                tag,
                "uploadImageParts success: ${response.getUploadedUrls().size} files uploaded: ${response.getUploadedUrls()}"
            )
            response
        } catch (t: Throwable) {
            Log.e(tag, "uploadImageParts failed: ${t.message}", t)
            UploadImagesResponse(
                success = false,
                code = 500,
                message = t.message ?: "Upload failed"
            )
        }
    }

    suspend fun uploadVideo(
        token: String,
        videoPart: UploadImagePart
    ): UploadImagesResponse {
        return uploadImageParts(token, listOf(videoPart.copy(formKey = "video")))
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
