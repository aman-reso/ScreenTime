package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * API Response for reward list
 */
@Serializable
data class RewardListResponse(
    val success: Boolean,
    val status: Int,
    val data: RewardListData? = null,
    val message: String? = null,
    val timestamp: String? = null,
    val error: String? = null
)

/**
 * Reward list data
 */
@Serializable
data class RewardListData(
    val userId: String,
    val totalPoints: Int,
    val totalBadges: Int,
    val totalTrophies: Int,
    val rewards: List<RewardItem>,
    val unclaimedCount: Int
)

/**
 * Reward type enum
 */
enum class RewardType {
    POINTS,
    BADGE,
    TROPHY
}

/**
 * Reward source enum
 */
enum class RewardSource {
    CHALLENGE_WIN,
    DAILY_LOGIN,
    STREAK_MILESTONE,
    CHALLENGE_PARTICIPATION,
    ACHIEVEMENT,
    REFERRAL,
    ADMIN_GRANT
}

/**
 * Individual reward item
 */
@Serializable
data class RewardItem(
    val id: Int,
    val userId: String,
    val type: String, // "POINTS", "BADGE", "TROPHY"
    val source: String, // "CHALLENGE_WIN", "DAILY_LOGIN", etc.
    val title: String,
    val description: String? = null,
    val amount: Int,
    val metadata: String? = null,
    val challengeId: Int? = null,
    val challengeTitle: String? = null,
    val rank: Int? = null,
    val earnedAt: String,
    val isClaimed: Boolean,
    val claimedAt: String? = null
)

