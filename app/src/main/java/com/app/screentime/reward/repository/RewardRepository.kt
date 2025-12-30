package com.app.screentime.reward.repository

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.reward.model.CoinHistoryResponse
import com.app.screentime.reward.model.RewardCatalogResponse
import com.app.screentime.reward.model.RewardClaimRequest
import com.app.screentime.reward.model.RewardClaimResponse
import com.app.screentime.reward.model.RewardTransactionResponse
import com.app.screentime.reward.model.SavedClaimDetails
import com.app.screentime.reward.service.RewardService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardRepository @Inject constructor(
    private val rewardService: RewardService,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val KEY_SAVED_CLAIM_DETAILS = "saved_claim_details"
    }

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

    fun saveClaimDetails(details: SavedClaimDetails) {
        preferencesManager.saveSerializable(
            KEY_SAVED_CLAIM_DETAILS,
            details,
            SavedClaimDetails.serializer()
        )
    }

    fun getSavedClaimDetails(): SavedClaimDetails? {
        return preferencesManager.getSerializable(
            KEY_SAVED_CLAIM_DETAILS,
            SavedClaimDetails.serializer()
        )
    }

    fun clearSavedClaimDetails() {
        preferencesManager.remove(KEY_SAVED_CLAIM_DETAILS)
    }
}

