package com.app.screentime.reward.mapper

import com.app.screentime.reward.model.CoinHistoryItem
import com.app.screentime.reward.model.CoinHistoryUiState
import com.app.screentime.reward.model.CoinHistoryFilter

/**
 * Mapper for Coin History UI State
 */
object CoinHistoryMapper {
    fun toUiState(
        totalCoins: Int,
        coinHistory: List<CoinHistoryItem>,
        selectedFilter: CoinHistoryFilter = CoinHistoryFilter.ALL,
        isLoading: Boolean = false,
        error: String? = null
    ): CoinHistoryUiState {
        val filteredHistory = when (selectedFilter) {
            CoinHistoryFilter.ALL -> coinHistory
            CoinHistoryFilter.EARNED -> coinHistory.filter { it.amount > 0 }
            CoinHistoryFilter.USED -> coinHistory.filter { it.amount < 0 }
            CoinHistoryFilter.EXPIRED -> {
                val now = java.time.Instant.now()
                coinHistory.filter { item ->
                    item.expiresAt != null && try {
                        val expiresAt = java.time.Instant.parse(item.expiresAt)
                        expiresAt.isBefore(now) && item.amount > 0
                    } catch (e: Exception) {
                        false
                    }
                }
            }
        }

        return CoinHistoryUiState(
            isLoading = isLoading,
            totalCoins = totalCoins,
            coinHistory = coinHistory,
            filteredHistory = filteredHistory,
            selectedFilter = selectedFilter,
            error = error
        )
    }

    fun toLoadingUiState(): CoinHistoryUiState {
        return CoinHistoryUiState(isLoading = true)
    }

    fun toErrorUiState(error: String): CoinHistoryUiState {
        return CoinHistoryUiState(error = error)
    }
}



























