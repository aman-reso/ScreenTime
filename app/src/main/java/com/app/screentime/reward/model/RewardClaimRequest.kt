package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * Request model for claiming a reward
 */
@Serializable
data class RewardClaimRequest(
    val rewardCatalogId: Int,
    val recipientName: String,
    val recipientPhone: String,
    val shippingAddress: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null
)

