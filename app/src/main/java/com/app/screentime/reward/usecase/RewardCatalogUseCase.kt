package com.app.screentime.reward.usecase

import com.app.screentime.reward.model.RewardCatalogItem
import com.app.screentime.reward.repository.RewardRepository
import javax.inject.Inject

class RewardCatalogUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend fun getRewardCatalog(): Result<List<RewardCatalogItem>> {
        return rewardRepository.getRewardCatalog().fold(
            onSuccess = { apiResponse ->
                val catalogResponse = apiResponse.data
                if (catalogResponse != null) {
                    Result.success(catalogResponse.data)
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


























