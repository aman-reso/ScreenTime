package com.app.screentime.reward.usecase

import com.app.screentime.reward.model.CoinHistoryFilter
import com.app.screentime.reward.model.CoinHistoryItem
import com.app.screentime.reward.repository.RewardRepository
import javax.inject.Inject

class CoinHistoryUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend fun getCoinHistory(): Result<Pair<Int, List<CoinHistoryItem>>> {
        return rewardRepository.getCoinHistory().fold(
            onSuccess = { apiResponse ->
                val coinHistoryResponse = apiResponse.data
                if (coinHistoryResponse != null) {
                    val data = coinHistoryResponse.data
                    Result.success(Pair(data.totalCoins, data.coinHistory))
                } else {
                    Result.failure(Exception("No data in response"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}

