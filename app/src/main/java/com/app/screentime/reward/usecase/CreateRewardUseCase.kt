package com.app.screentime.reward.usecase

import com.app.screentime.reward.model.AddCoinsRequest
import com.app.screentime.reward.repository.RewardRepository
import javax.inject.Inject

class CreateRewardUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend fun addCoins(request: AddCoinsRequest): Result<Unit> {
        return rewardRepository.addCoins(request).fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}

