package com.app.screentime.reward.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.reward.model.CoinHistoryResponse
import com.app.screentime.reward.model.RewardCatalogResponse
import com.app.screentime.reward.model.RewardClaimRequest
import com.app.screentime.reward.model.RewardClaimResponse
import com.app.screentime.reward.model.RewardTransactionResponse

/**
 * Service interface for reward/coin operations
 */
interface RewardService {
    /**
     * Get user's coin history
     */
    suspend fun getCoinHistory(): Result<ApiResponse<CoinHistoryResponse>>

    /**
     * Get reward catalog
     */
    suspend fun getRewardCatalog(): Result<ApiResponse<RewardCatalogResponse>>

    /**
     * Claim a reward from catalog
     */
    suspend fun claimReward(request: RewardClaimRequest): Result<ApiResponse<RewardClaimResponse>>

    /**
     * Get user's reward transactions
     */
    suspend fun getRewardTransactions(): Result<ApiResponse<RewardTransactionResponse>>
}

