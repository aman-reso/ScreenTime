package com.app.screentime.core.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val phone: String,
    val name: String,
    val email: String? = null,
    val role: UserRole = UserRole.USER,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val voiceRatePerMin: Double = 10.0,
    val chatRatePerMsg: Double = 1.0,
    val isOnline: Boolean = false,
    val isBusy: Boolean = false,
    val walletBalance: Double = 0.0,
    val createdAt: String = ""
)

@Serializable
enum class UserRole {
    USER,
    MODEL;

    companion object {
        fun fromString(role: String): UserRole {
            return when (role.lowercase()) {
                "model" -> MODEL
                else -> USER
            }
        }
    }
}

@Serializable
data class ModelProfile(
    val id: String,
    val name: String,
    val age: Int = 24,
    val distance: String = "200 m",
    val location: String = "Mumbai, India",
    val matchedPreferences: String = "Matched 5+ Preferences",
    val bio: String = "",
    val avatarUrl: String = "",
    val coverUrl: String = "",
    val galleryUrls: List<String> = emptyList(),
    val ratePerMinute: Int = 10,       // coins per minute for voice call
    val chatRate: Int = 5,             // coins per message
    val isOnline: Boolean = false,
    val isBusy: Boolean = false,
    val rating: Float = 4.9f,
    val reviewCount: Int = 120,
    val totalCalls: Int = 500,
    val language: String = "English",
    val tags: List<String> = emptyList()
)

@Serializable
data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: MessageType = MessageType.TEXT
)

@Serializable
enum class MessageType { TEXT, IMAGE, STICKER, SYSTEM }

@Serializable
data class Conversation(
    val id: String,
    val modelId: String,
    val modelName: String,
    val modelAvatarUrl: String = "",
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
)

@Serializable
data class WalletTransaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val description: String,
    val timestamp: Long,
    val balanceAfter: Double = 0.0
)

@Serializable
enum class TransactionType {
    TOPUP,
    CHAT,
    CALL,
    GROUP_CALL,
    REFUND,
    BONUS;

    companion object {
        fun fromString(type: String): TransactionType {
            return when (type.lowercase()) {
                "recharge", "topup" -> TOPUP
                "chat_debit" -> CHAT
                "call_debit", "call_credit" -> CALL
                "group_call_debit", "group_call_credit" -> GROUP_CALL
                "welcome_bonus" -> BONUS
                else -> TOPUP
            }
        }
    }
}

@Serializable
data class TopUpPackage(
    val id: String,
    val coins: Int,
    val priceInr: Int,
    val bonusCoins: Int = 0,
    val isPopular: Boolean = false
)

@Serializable
data class GroupRoom(
    val id: String,
    val modelId: String,
    val title: String,
    val ratePerMin: Double,
    val isLive: Boolean = false,
    val participantCount: Int = 0
)

@Serializable
data class CallRecord(
    val callId: String,
    val callerId: String,
    val modelId: String,
    val durationSec: Int,
    val amountCharged: Double,
    val createdAt: String = ""
)
