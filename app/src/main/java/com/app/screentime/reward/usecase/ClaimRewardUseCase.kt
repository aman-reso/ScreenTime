package com.app.screentime.reward.usecase

import com.app.screentime.reward.model.RewardClaimData
import com.app.screentime.reward.model.RewardClaimRequest
import com.app.screentime.reward.model.SavedClaimDetails
import com.app.screentime.reward.repository.RewardRepository
import javax.inject.Inject

class ClaimRewardUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend fun claimReward(request: RewardClaimRequest): Result<RewardClaimData> {
        return rewardRepository.claimReward(request).fold(
            onSuccess = { apiResponse ->
                val claimResponse = apiResponse.data
                if (claimResponse != null && claimResponse.data != null) {
                    Result.success(claimResponse.data)
                } else {
                    Result.failure(Exception(claimResponse?.message ?: "No data in response"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    fun saveClaimDetails(details: SavedClaimDetails) {
        rewardRepository.saveClaimDetails(details)
    }

    fun getSavedClaimDetails(): SavedClaimDetails? {
        return rewardRepository.getSavedClaimDetails()
    }

    fun clearSavedClaimDetails() {
        rewardRepository.clearSavedClaimDetails()
    }
}

