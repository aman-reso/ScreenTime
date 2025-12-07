package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * API Response for reward transactions
 */
@Serializable
data class RewardTransactionResponse(
    val success: Boolean,
    val status: Int,
    val data: List<RewardTransaction> = emptyList(),
    val message: String? = null,
    val timestamp: String? = null,
    val error: String? = null
)

/**
 * Reward transaction item
 */
@Serializable
data class RewardTransaction(
    val id: Int,
    val userId: String,
    val rewardCatalogId: Int,
    val rewardTitle: String,
    val coinPrice: Int,
    val status: String, // "PENDING", "PROCESSING", "DELIVERED", "CANCELLED"
    val transactionNumber: String,
    val recipientName: String,
    val recipientPhone: String,
    val recipientEmail: String? = null,
    val shippingAddress: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val adminNotes: String? = null,
    val trackingNumber: String? = null,
    val shippedAt: String? = null,
    val deliveredAt: String? = null,
    val createdAt: String,
    val updatedAt: String
)

