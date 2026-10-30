package com.app.screentime.core.network.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive

object StringOrIntStatusSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringOrIntStatus", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String? {
        val jsonDecoder = decoder as? JsonDecoder ?: return try {
            decoder.decodeString()
        } catch (_: Exception) {
            null
        }
        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
            is JsonPrimitive -> element.content
            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: String?) {
        if (value != null) encoder.encodeString(value) else encoder.encodeNull()
    }
}

@Serializable
data class ApiResponse<T>(
    val success: Boolean = true,
    val status_code: Int = 200,
    @Serializable(with = StringOrIntStatusSerializer::class)
    val status: String? = null,
    val message: String = "",
    val data: T? = null,
    val error: String? = null,
    val timestamp: Long = 0L,
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


@Serializable
data class AuthResponse(
    val user: UserDto,
    val token: String,
    val is_new_user: Boolean = false,
    val has_profile: Boolean = false,
    val profile_complete: Boolean = false,
    val wallet: WalletDto? = null,
    val message: String = ""
)

@Serializable
data class SendOtpRequest(
    val phone: String
)

@Serializable
data class SendOtpDataDto(
    val mock_otp: String? = null,
    val hint: String? = null
)

@Serializable
data class SendOtpResponse(
    val status: Int = 200,
    val message: String = "",
    val data: SendOtpDataDto? = null
)

@Serializable
data class VerifyOtpRequest(
    val phone: String,
    val code: String
)

@Serializable
data class VerifyOtpDataDto(
    val user: UserDto? = null,
    val token: String = "",
    val is_new_user: Boolean = false,
    val has_profile: Boolean = false,
    val profile_complete: Boolean = false,
    val message: String = ""
)

@Serializable
data class VerifyOtpResponse(
    val status: Int = 200,
    val message: String = "",
    val data: VerifyOtpDataDto? = null
)

@Serializable
data class UserDto(
    val id: String = "",
    val phone: String = "",
    val email: String? = null,
    val name: String = "",
    val display_name: String? = null,
    val google_id: String? = null,
    val role: String = "user",
    val has_profile: Boolean = false,
    val profile_complete: Boolean = false,
    val avatar_url: String? = null,
    val photos: List<String> = emptyList(),
    val bio: String? = null,
    val gender: String? = null,
    val interests: List<String> = emptyList(),
    val dating_intent: String? = null,
    val city: String? = null,
    val country: String? = null,
    val area: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val quality_score: Double = 0.0,
    val profession: String? = null,
    val job: String? = null,
    val occupation: String? = null,
    val height: String? = null,
    val height_cm: Int? = null,
    val education: String? = null,
    val languages: List<String> = emptyList(),
    val date_of_birth: String? = null,
    val dob: String? = null,
    val age: Int? = null,
    val about_me: String? = null,
    val prompts: List<ConnectProfilePromptDto> = emptyList(),
    val likes_count: Int = 0,
    val matches_count: Int = 0,
    val likes: Int = 0,
    val matches: Int = 0,
    val voice_rate_per_min: Double = 10.0,
    val group_rate_per_min: Double = 5.0,
    val chat_rate_per_msg: Double = 1.0,
    val is_online: Boolean = false,
    val is_busy: Boolean = false,
    val visibility: String? = null,
    val is_private: Boolean = false,
    val discovery_enabled: Boolean = true,
    val created_at: String = "",
    val relation_type: String? = null,
    val video_url: String? = null,
    val videos: List<String> = emptyList(),
    val company: String? = null,
    val is_phone_verified: Boolean? = null,
    val phone_verified: Boolean? = null
) {
    fun getResolvedName(): String {
        return name.ifBlank { display_name ?: "User" }
    }
}

@Serializable
data class ProfileVisibilityRequest(
    val visibility: String? = null,
    val discovery_enabled: Boolean? = null,
    val is_private: Boolean? = null
)

@Serializable
data class ProfileVisibilityResponseData(
    val visibility: String = "public",
    val discovery_enabled: Boolean = true,
    val is_private: Boolean = false
)

@Serializable
data class WalletDto(
    val user_id: String = "",
    val balance: Double = 0.0,
    val bonus_given: Double = 0.0,
    val total_spent: Double = 0.0,
    val total_earned: Double = 0.0,
    val updated_at: String = ""
)

@Serializable
data class WalletInfoDto(
    val user_id: String = "",
    val credit_balance: Int = 0,
    val voice_minutes_available: Int = 0,
    val video_minutes_available: Int = 0
)

@Serializable
data class WalletPackDto(
    val id: String = "",
    val name: String = "",
    val amount: Double = 0.0,
    val amount_in_paise: Long = 0L,
    val currency: String = "INR",
    val credits: Int = 0,
    val voice_minutes: Int = 0,
    val video_minutes: Int = 0,
    val benefits: List<String> = emptyList(),
    val description: String = "",
    val popular_badge: Boolean = false,
    // Legacy / fallback fields
    val coins: Int = 0,
    val bonus_coins: Int = 0,
    val total_coins: Int = 0,
    val price_inr: Double = 0.0,
    val badge: String? = null,
    val is_popular: Boolean = false,
    val type: String = "inapp",
    val category: String = "",
    val google_product_id: String = ""
) {
    val effectiveCredits: Int
        get() = if (credits > 0) credits else if (total_coins > 0) total_coins else coins

    val effectivePrice: Double
        get() = if (amount > 0.0) amount else price_inr

    val effectivePriceInPaise: Long
        get() = if (amount_in_paise > 0L) amount_in_paise else (effectivePrice * 100).toLong()

    val effectiveName: String
        get() = if (name.isNotBlank()) name else "$effectiveCredits Credits Pack"

    val isPopularPack: Boolean
        get() = popular_badge || is_popular
}

@Serializable
data class WalletPacksResponse(
    val count: Int = 0,
    val packs: List<WalletPackDto> = emptyList()
)

@Serializable
data class GooglePlayPurchaseVerifyRequest(
    val purchase_token: String,
    val product_id: String,
    val order_id: String? = null,
    val package_id: String? = null,
    val credits: Int = 0,
    val amount: Double = 0.0
)

@Serializable
data class GooglePlayPurchaseVerifyResponse(
    val success: Boolean = true,
    val message: String = "",
    val credits_awarded: Int = 0,
    val balance: Double = 0.0
)


@Serializable
data class WalletOrderStatusDto(
    val order_id: String = "",
    val purchase_id: String? = null,
    val package_id: String? = null,
    val package_name: String? = null,
    val amount_in_rupees: Double = 0.0,
    val amount_in_paise: Long = 0L,
    val currency: String = "INR",
    val credits_awarded: Int = 0,
    val status: String = "",
    val gateway_transaction_id: String? = null,
    val created_at: String? = null,
    val verified_at: String? = null
)

@Serializable
data class EndCallRequest(
    val session_id: String = "",
    val call_id: String = "",
    val duration_seconds: Int = 0
)

@Serializable
data class RejectCallRequest(
    val session_id: String = "",
    val call_id: String = ""
)

@Serializable
data class AcceptCallRequest(
    val session_id: String = "",
    val call_id: String = ""
)

@Serializable
data class CallSessionResponse(
    val session_id: String? = null,
    val call_id: String? = null,
    val caller_id: String? = null,
    val receiver_id: String? = null,
    val status: String? = null,
    val duration_seconds: Int = 0,
    val credits_consumed: Int = 0
)

@Serializable
data class CheckCallBalanceResponse(
    val can_call: Boolean = false,
    val has_sufficient_balance: Boolean = false,
    val current_balance: Double = 0.0,
    val balance: Double = 0.0,
    val wallet_balance: Double = 0.0,
    val required_credits: Double = 0.0,
    val credits_per_minute: Double = 0.0,
    val rate_per_min: Double = 0.0,
    val min_required: Double = 0.0,
    val max_duration_sec: Int = 0,
    val call_type: String = "voice",
    val target_user_id: String = "",
    val model_id: String = "",
    val receiver_id: String = "",
    val model_name: String = "",
    val message: String = ""
) {
    val isCallAllowed: Boolean
        get() = can_call || has_sufficient_balance

    val effectiveRate: Double
        get() = when {
            credits_per_minute > 0.0 -> credits_per_minute
            rate_per_min > 0.0 -> rate_per_min
            else -> 1.0
        }

    val effectiveBalance: Double
        get() = when {
            current_balance > 0.0 -> current_balance
            wallet_balance > 0.0 -> wallet_balance
            balance > 0.0 -> balance
            else -> 0.0
        }

    val effectiveMinRequired: Double
        get() = when {
            required_credits > 0.0 -> required_credits
            min_required > 0.0 -> min_required
            else -> effectiveRate
        }
}

@Serializable
data class TransactionDto(
    val id: String = "",
    val user_id: String = "",
    val amount: Double = 0.0,
    val type: String = "",
    val direction: String = "",
    val transaction_type: String = "",
    val category: String = "",
    val reference_id: String? = null,
    val description: String? = null,
    val balance_after: Double = 0.0,
    val created_at: String = "",
    val call_id: String? = null,
    val room_id: String? = null
)

@Serializable
data class PaginatedTransactionsData(
    val items: List<TransactionDto> = emptyList(),
    val page: Int = 1,
    val limit: Int = 20,
    val total_count: Int = 0,
    val has_more: Boolean = false,
    val next_page: Int = 0
)

@Serializable
data class PaginationDto(
    val page: Int = 1,
    val limit: Int = 20,
    val total_count: Int = 0,
    val has_more: Boolean = false,
    val next_page: Int = 0
)

@Serializable
data class WalletResponse(
    val wallet: WalletDto?,
    val transactions: List<TransactionDto>? = null
)

@Serializable
data class ModelListResponse(
    val count: Int = 0,
    val models: List<UserDto> = emptyList()
)

@Serializable
data class CallRecordDto(
    val call_id: String,
    val caller_id: String,
    val model_id: String,
    val duration_sec: Int = 0,
    val amount_charged: Double = 0.0,
    val created_at: String = ""
)

@Serializable
data class CallHistoryResponse(
    val count: Int = 0,
    val calls: List<CallRecordDto> = emptyList(),
    val privacy_notice: String = ""
)

@Serializable
data class GroupRoomDto(
    val id: String,
    val model_id: String,
    val title: String,
    val rate_per_min: Double = 5.0,
    val is_live: Boolean = false
)

@Serializable
data class RoomListResponse(
    val count: Int = 0,
    val rooms: List<GroupRoomDto> = emptyList()
)

@Serializable
data class OnboardingStatusResponse(
    val status: String = "approved",
    val message: String = ""
)

// ── Ephemeral 24-Hour Chat DTOs ──────────────────────────────────────────────
@Serializable
data class ChatMessageDto(
    val id: String = "",
    val sender_id: String = "",
    val receiver_id: String = "",
    val content: String = "",
    val cost: Double = 0.0,
    val expires_at: String = "",
    val is_read: Boolean = false,
    val created_at: String = ""
)

@Serializable
data class ConversationDto(
    val id: String = "",
    val partner_id: String = "",
    val user_id: String = "",
    val peer_id: String = "",
    val caller_id: String = "",
    val receiver_id: String = "",
    val model_id: String = "",
    val partner_name: String = "",
    val user_name: String = "",
    val name: String = "",
    val username: String = "",
    val caller_name: String = "",
    val partner_avatar: String = "",
    val avatar_url: String = "",
    val last_message: String = "",
    val last_message_time: Long = 0L,
    val unread_count: Int = 0,
    val is_online: Boolean = false
) {
    fun getResolvedPartnerId(currentUserId: String? = null): String {
        return when {
            partner_id.isNotBlank() && partner_id != currentUserId -> partner_id
            user_id.isNotBlank() && user_id != currentUserId -> user_id
            peer_id.isNotBlank() && peer_id != currentUserId -> peer_id
            caller_id.isNotBlank() && caller_id != currentUserId -> caller_id
            receiver_id.isNotBlank() && receiver_id != currentUserId -> receiver_id
            model_id.isNotBlank() && model_id != currentUserId -> model_id
            id.isNotBlank() && !id.startsWith("conv_") && id != currentUserId -> id
            else -> partner_id.ifBlank { user_id.ifBlank { peer_id.ifBlank { caller_id.ifBlank { receiver_id.ifBlank { id } } } } }
        }
    }

    fun getResolvedPartnerName(): String {
        return when {
            partner_name.isNotBlank() -> partner_name
            user_name.isNotBlank() -> user_name
            name.isNotBlank() -> name
            username.isNotBlank() -> username
            caller_name.isNotBlank() -> caller_name
            else -> "User"
        }
    }
}

@Serializable
data class ConversationListResponse(
    val count: Int = 0,
    val conversations: List<ConversationDto> = emptyList(),
    val notice: String = ""
)

@Serializable
data class EphemeralChatResponse(
    val partner_id: String = "",
    val messages: List<ChatMessageDto> = emptyList(),
    val notice: String = ""
)

@Serializable
data class LiveStreamDto(
    val stream_id: String = "",
    val host_id: String = "",
    val host_name: String = "",
    val host_avatar: String = "",
    val title: String = "",
    val viewer_count: Int = 1,
    val total_earned: Double = 0.0,
    val is_active: Boolean = true,
    val is_paid_mode: Boolean = false,
    val coin_rate_per_min: Double = 10.0
)

@Serializable
data class LiveStreamListResponse(
    val streams: List<LiveStreamDto> = emptyList()
)

@Serializable
data class StartLiveResponse(
    val stream: LiveStreamDto? = null,
    val livekit_url: String = "",
    val token: String = ""
)

@Serializable
data class JoinLiveResponse(
    val livekit_url: String = "",
    val token: String = "",
    val room_name: String = ""
)

@Serializable
data class LiveTipRequest(
    val stream_id: String,
    val amount: Double,
    val gift_name: String
)

@Serializable
data class LiveTipResponse(
    val amount: Double = 0.0,
    val gift_name: String = "",
    val sender: String = ""
)

@Serializable
data class LivePaidModeRequest(
    val stream_id: String,
    val is_paid_mode: Boolean,
    val coin_rate_per_min: Double = 10.0
)

@Serializable
data class LivePaidModeResponse(
    val stream: LiveStreamDto? = null
)

@Serializable
data class LiveStatusResponse(
    val stream: LiveStreamDto? = null,
    val is_active: Boolean = true
)

@Serializable
data class LiveDeductRequest(
    val stream_id: String,
    val duration_seconds: Int = 60
)

@Serializable
data class LiveDeductResponse(
    val success: Boolean = false,
    val deducted: Double = 0.0,
    val balance: Double = 0.0,
    val required: Double = 0.0,
    val is_paid_mode: Boolean = false,
    val coin_rate_per_min: Double = 10.0,
    val error: String = ""
)

@Serializable
data class FcmTokenData(
    val user_id: String? = null,
    val fcm_token: String? = null,
    val platform: String? = null,
    val active: Boolean? = null
)

@Serializable
data class UploadImagesData(
    val urls: List<String> = emptyList(),
    val photos: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val url: String? = null,
    val thumbnail_url: String? = null,
    val media_type: String? = null,
    val original_bytes: Long? = null,
    val compressed_bytes: Long? = null,
    val count: Int? = null
) {
    fun getAllUrls(): List<String> {
        if (!url.isNullOrBlank()) return listOf(url)
        if (urls.isNotEmpty()) return urls
        if (photos.isNotEmpty()) return photos
        if (images.isNotEmpty()) return images
        return emptyList()
    }
}

@Serializable
data class UploadImagesResponse(
    val success: Boolean = true,
    val code: Int? = null,
    val message: String = "",
    val data: UploadImagesData? = null,
    val urls: List<String> = emptyList(),
    val photos: List<String> = emptyList()
) {
    val isSuccess: Boolean
        get() = (code == null || code in 200..299) && success

    fun getUploadedUrls(): List<String> {
        val fromData = data?.getAllUrls() ?: emptyList()
        if (fromData.isNotEmpty()) return fromData
        if (urls.isNotEmpty()) return urls
        if (photos.isNotEmpty()) return photos
        return emptyList()
    }
}

data class UploadImagePart(
    val filename: String,
    val bytes: ByteArray,
    val mimeType: String = "image/jpeg",
    val formKey: String? = null
)



