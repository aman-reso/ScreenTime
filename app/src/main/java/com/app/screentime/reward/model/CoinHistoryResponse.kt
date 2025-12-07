package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * API Response for coin history
 */
@Serializable
data class CoinHistoryResponse(
    val success: Boolean,
    val status: Int,
    val data: CoinHistoryData,
    val message: String?,
    val timestamp: String?,
    val error: String?
)

/**
 * Coin history data
 */
@Serializable
data class CoinHistoryData(
    val userId: String,
    val totalCoins: Int,
    val coinHistory: List<CoinHistoryItem>
)

/**
 * Individual coin history item
 */
@Serializable
data class CoinHistoryItem(
    val id: Int,
    val userId: String,
    val amount: Int,
    val source: String,
    val description: String,
    val challengeId: Int? = null,
    val challengeTitle: String? = null,
    val rank: Int? = null,
    val metadata: String? = null,
    val expiresAt: String? = null,
    val createdAt: String
)

/**
 * Coin source types
 */
enum class CoinSource {
    REDEMPTION,
    DAILY_LOGIN,
    CHALLENGE_WIN,
    ADMIN_GRANT,
    REFERRAL,
    ACHIEVEMENT,
    STREAK_MILESTONE,
    CHALLENGE_PARTICIPATION
}



