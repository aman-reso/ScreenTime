package com.app.screentime.reward.usecase

import com.app.screentime.reward.model.RewardTransaction
import com.app.screentime.reward.repository.RewardRepository
import javax.inject.Inject

class RewardTransactionUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend fun getRewardTransactions(): Result<List<RewardTransaction>> {
        return rewardRepository.getRewardTransactions().fold(
            onSuccess = { apiResponse ->
                val transactionResponse = apiResponse.data
                if (transactionResponse != null) {
                    Result.success(transactionResponse.data)
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

