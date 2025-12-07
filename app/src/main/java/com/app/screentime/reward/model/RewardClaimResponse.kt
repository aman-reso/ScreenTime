package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * API Response for reward claim
 */
@Serializable
data class RewardClaimResponse(
    val success: Boolean,
    val status: Int,
    val data: RewardClaimData? = null,
    val message: String? = null,
    val timestamp: String? = null,
    val error: String? = null
)

/**
 * Reward claim data
 */
@Serializable
data class RewardClaimData(
    val transactionId: Int,
    val transactionNumber: String,
    val message: String,
    val remainingCoins: Int
)

