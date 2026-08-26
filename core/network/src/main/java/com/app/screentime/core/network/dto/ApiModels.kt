package com.app.screentime.core.network.dto

import com.app.screentime.core.model.UserRole
import kotlinx.serialization.Serializable

// ─── Requests ─────────────────────────────────────────────────────────────────

@Serializable
data class RegisterRequest(
    val phone: String,
    val name: String,
    val role: String   // "user" or "model"
)

@Serializable
data class RechargeRequest(
    val amount: Double
)

@Serializable
data class CreateRoomRequest(
    val title: String,
    val rate_per_min: Double
)

@Serializable
data class ReportRequest(
    val reported_id: String,
    val reason: String
)

@Serializable
data class ModelOnboardRequest(
    val bio: String,
    val voice_rate_per_min: Double,
    val group_rate_per_min: Double,
    val chat_rate_per_msg: Double
)

// ─── Responses ────────────────────────────────────────────────────────────────

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
    val id: String,
    val phone: String,
    val name: String,
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
data class TransactionDto(
    val id: String,
    val user_id: String,
    val amount: Double,
    val type: String,      // 'welcome_bonus', 'recharge', 'call_debit', 'call_credit', 'chat_debit'
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

@Serializable
data class ApiErrorResponse(
    val error: String = "An error occurred"
)

// ─── Domain Mappers ───────────────────────────────────────────────────────────

fun UserDto.toModelProfile(): com.app.screentime.core.model.ModelProfile {
    return com.app.screentime.core.model.ModelProfile(
        id = this.id,
        name = this.name,
        bio = this.bio ?: "",
        avatarUrl = this.avatar_url ?: "",
        coverUrl = this.avatar_url ?: "",
        ratePerMinute = this.voice_rate_per_min.toInt().coerceAtLeast(1),
        chatRate = this.chat_rate_per_msg.toInt().coerceAtLeast(1),
        isOnline = this.is_online,
        isBusy = this.is_busy
    )
}

fun UserDto.toUser(): com.app.screentime.core.model.User {
    return com.app.screentime.core.model.User(
        id = this.id,
        phone = this.phone,
        name = this.name,
        role = com.app.screentime.core.model.UserRole.fromString(this.role),
        avatarUrl = this.avatar_url,
        bio = this.bio,
        voiceRatePerMin = this.voice_rate_per_min,
        chatRatePerMsg = this.chat_rate_per_msg,
        isOnline = this.is_online,
        isBusy = this.is_busy,
        createdAt = this.created_at
    )
}

fun TransactionDto.toWalletTransaction(): com.app.screentime.core.model.WalletTransaction {
    return com.app.screentime.core.model.WalletTransaction(
        id = this.id,
        type = com.app.screentime.core.model.TransactionType.fromString(this.type),
        amount = this.amount,
        description = this.description ?: "Transaction",
        timestamp = System.currentTimeMillis()
    )
}
