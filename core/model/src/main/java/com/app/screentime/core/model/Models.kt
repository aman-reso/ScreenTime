package com.app.screentime.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfilePrompt(
    val id: String = "",
    val question: String = "",
    val answer: String = ""
)

@Serializable
data class User(
    val id: String,
    val phone: String,
    val name: String,
    val email: String? = null,
    val role: UserRole = UserRole.USER,
    val avatarUrl: String? = null,
    val photos: List<String> = emptyList(),
    val bio: String? = null,
    val aboutMe: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val interests: List<String> = emptyList(),
    val datingIntent: String? = null,
    val city: String? = null,
    val area: String? = null,
    val qualityScore: Double = 0.0,
    val job: String? = null,
    val occupation: String? = null,
    val height: String? = null,
    val heightCm: Int? = null,
    val education: String? = null,
    val languages: List<String> = emptyList(),
    val relationType: String? = null,
    val dateOfBirth: String? = null,
    val dob: String? = null,
    val prompts: List<ProfilePrompt> = emptyList(),
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
    val age: Int = 0,
    val distance: String = "",
    val location: String = "",
    val matchedPreferences: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val coverUrl: String = "",
    val galleryUrls: List<String> = emptyList(),
    val ratePerMinute: Int = 10,       // coins per minute for voice call
    val chatRate: Int = 1,             // coins per message
    val isOnline: Boolean = false,
    val isBusy: Boolean = false,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val totalCalls: Int = 0,
    val language: String = "English",
    val height: String? = null,
    val gender: String? = null,
    val job: String? = null,
    val education: String? = null,
    val relationType: String? = null,
    val tags: List<String> = emptyList(),
    val lat: Double? = null,
    val lng: Double? = null
)

@Serializable
data class DiscoveryMatch(
    val matchId: String,
    val matchedUserId: String,
    val displayName: String,
    val age: Int = 24,
    val city: String = "",
    val area: String = "",
    val primaryPhotoUrl: String = "",
    val datingIntent: String = "",
    val bio: String = "",
    val matchedAt: String = ""
)

@Serializable
data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val isSending: Boolean = false,
    val isFailed: Boolean = false,
    val conversationId: String? = null,
    val type: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null
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
    val balanceAfter: Double = 0.0,
    val direction: String = "",
    val transactionType: String = "",
    val category: String = "",
    val referenceId: String? = null,
    val createdAt: String = "",
    val isPositive: Boolean = false
)

@Serializable
enum class TransactionType {
    CREDIT,
    DEBIT,
    TOPUP,
    CHAT,
    CALL,
    GROUP_CALL,
    REFUND,
    BONUS;

    companion object {
        fun fromString(type: String): TransactionType {
            return when (type.lowercase()) {
                "credit" -> CREDIT
                "debit" -> DEBIT
                "recharge", "topup", "purchase" -> TOPUP
                "chat", "chat_debit" -> CHAT
                "voice_call", "video_call", "call", "call_debit", "call_credit" -> CALL
                "group_call", "group_call_debit", "group_call_credit" -> GROUP_CALL
                "welcome_bonus", "bonus" -> BONUS
                "refund" -> REFUND
                else -> if (type.lowercase().contains("credit")) CREDIT else TOPUP
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
