package com.app.screentime.reward.model

/**
 * UI State for Reward Screen
 */
data class RewardUiState(
    val isLoading: Boolean = false,
    val totalCoins: Int = 0,
    val totalPoints: Int = 0,
    val totalBadges: Int = 0,
    val totalTrophies: Int = 0,
    val unclaimedCount: Int = 0,
    val rewards: List<RewardItem> = emptyList(),
    val catalog: List<RewardCatalogItem> = emptyList(),
    val error: String? = null
)
