package com.app.screentime.reward.model

/**
 * UI State for Coin History Screen
 */
data class CoinHistoryUiState(
    val isLoading: Boolean = false,
    val totalCoins: Int = 0,
    val coinHistory: List<CoinHistoryItem> = emptyList(),
    val filteredHistory: List<CoinHistoryItem> = emptyList(),
    val selectedFilter: CoinHistoryFilter = CoinHistoryFilter.ALL,
    val error: String? = null
)

/**
 * Filter types for coin history
 */
enum class CoinHistoryFilter {
    ALL,
    EARNED,
    USED,
    EXPIRED
}



























