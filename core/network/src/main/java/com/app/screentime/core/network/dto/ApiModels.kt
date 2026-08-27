package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean = true,
    val status_code: Int = 200,
    val message: String = "",
    val data: T? = null,
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
    val name: String = "",
    val role: String = "user",
    val avatar_url: String? = null,
    val bio: String? = null,
    val voice_rate_per_min: Double = 10.0,
    val group_rate_per_min: Double = 5.0,
    val chat_rate_per_msg: Double = 1.0,
    val is_online: Boolean = false,
    val is_busy: Boolean = false,
    val created_at: String = ""
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
    val balance: Double = 0.0,
    val rate_per_min: Double = 0.0,
    val min_required: Double = 0.0,
    val max_duration_sec: Int = 0,
    val model_id: String = "",
    val model_name: String = "",
    val message: String = ""
)

@Serializable
data class TransactionDto(
    val id: String,
    val user_id: String,
    val amount: Double,
    val type: String,
    val description: String? = null,
    val call_id: String? = null,
    val room_id: String? = null,
    val created_at: String = ""
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
    val partner_name: String = "",
    val partner_avatar: String = "",
    val last_message: String = "",
    val last_message_time: Long = 0L,
    val unread_count: Int = 0,
    val is_online: Boolean = false
)

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
