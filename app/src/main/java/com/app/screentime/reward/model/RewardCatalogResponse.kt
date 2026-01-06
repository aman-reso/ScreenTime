package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * API Response for reward catalog
 */
@Serializable
data class RewardCatalogResponse(
    val success: Boolean,
    val status: Int,
    val data: List<RewardCatalogItem> = emptyList(),
    val message: String? = null,
    val timestamp: String? = null,
    val error: String? = null
)

/**
 * Reward catalog item
 */
@Serializable
data class RewardCatalogItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val coinPrice: Int,
    val imageUrl: String? = null,
    val stockQuantity: Int,
    val isActive: Boolean,
    val metadata: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val rewardType: String
)

















