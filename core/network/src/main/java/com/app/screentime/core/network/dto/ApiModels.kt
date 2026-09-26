package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean = true,
    val status_code: Int = 200,
    val status: Int? = null,
    val message: String = "",
    val data: T? = null,
    val error: String? = null,
    val timestamp: Long = 0L
)

@Serializable
data class AuthResponse(
    val user: UserDto,
    val token: String,
    val is_new_user: Boolean = false,
    val wallet: WalletDto? = null,
    val message: String = ""
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
    val avatar_url: String? = null,
    val photos: List<String> = emptyList(),
    val bio: String? = null,
    val gender: String? = null,
    val interests: List<String> = emptyList(),
    val dating_intent: String? = null,
    val city: String? = null,
    val area: String? = null,
    val quality_score: Double = 0.0,
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
    val voice_rate_per_min: Double = 10.0,
    val group_rate_per_min: Double = 5.0,
    val chat_rate_per_msg: Double = 1.0,
    val is_online: Boolean = false,
    val is_busy: Boolean = false,
    val created_at: String = "",
    val relation_type: String? = null
) {
    fun getResolvedName(): String {
        return name.ifBlank { display_name ?: "User" }
    }
}

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
    val coins: Int = 0,
    val bonus_coins: Int = 0,
    val total_coins: Int = 0,
    val price_inr: Double = 0.0,
    val badge: String? = null,
    val is_popular: Boolean = false,
    val description: String = ""
)

@Serializable
data class WalletPacksResponse(
    val count: Int = 0,
    val packs: List<WalletPackDto> = emptyList()
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
    val wallet: WalletDto,
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
    val url: String? = null
) {
    fun getAllUrls(): List<String> {
        if (urls.isNotEmpty()) return urls
        if (photos.isNotEmpty()) return photos
        if (images.isNotEmpty()) return images
        if (!url.isNullOrBlank()) return listOf(url)
        return emptyList()
    }
}

@Serializable
data class UploadImagesResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: UploadImagesData? = null,
    val urls: List<String> = emptyList(),
    val photos: List<String> = emptyList()
) {
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
    val mimeType: String = "image/jpeg"
)



