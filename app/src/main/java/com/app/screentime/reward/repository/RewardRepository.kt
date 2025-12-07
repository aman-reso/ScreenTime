package com.app.screentime.reward.repository

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.reward.model.CoinHistoryResponse
import com.app.screentime.reward.model.RewardCatalogResponse
import com.app.screentime.reward.model.RewardClaimRequest
import com.app.screentime.reward.model.RewardClaimResponse
import com.app.screentime.reward.model.RewardTransactionResponse
import com.app.screentime.reward.service.RewardService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardRepository @Inject constructor(
    private val rewardService: RewardService
) {
    suspend fun getCoinHistory(): Result<ApiResponse<CoinHistoryResponse>> {
        return rewardService.getCoinHistory()
    }

    suspend fun getRewardCatalog(): Result<ApiResponse<RewardCatalogResponse>> {
        return rewardService.getRewardCatalog()
    }

    suspend fun claimReward(request: RewardClaimRequest): Result<ApiResponse<RewardClaimResponse>> {
        return rewardService.claimReward(request)
    }

    suspend fun getRewardTransactions(): Result<ApiResponse<RewardTransactionResponse>> {
        return rewardService.getRewardTransactions()
    }
}

