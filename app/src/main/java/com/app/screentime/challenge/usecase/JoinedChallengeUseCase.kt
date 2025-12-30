package com.app.screentime.challenge.usecase

import com.app.screentime.challenge.mapper.JoinedChallengeMapper
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.network.model.UserChallenge
import javax.inject.Inject

/**
 * Use case for joined challenge operations
 * Contains all business logic for processing joined challenge data
 */
class JoinedChallengeUseCase @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val joinedChallengeMapper: JoinedChallengeMapper
) {

    /**
     * Get user's joined challenges from API
     * @return Result containing list of UserChallenge
     */
    suspend fun getJoinedChallenges(): Result<List<UserChallenge>> {
        return challengeRepository.getUserChallenges().fold(
            onSuccess = { response ->
                if (response.success == true && response.data != null) {
                    Result.success(response.data!!.challenges)
                } else {
                    Result.failure(
                        Exception(response.message ?: "Failed to load joined challenges")
                    )
                }
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }

    /**
     * Filter challenges by status (current or expired)
     * @param challenges List of UserChallenge to filter
     * @param showCurrent If true, returns active challenges, if false returns expired challenges
     * @return Filtered list of UserChallenge
     */
    fun filterChallengesByStatus(
        challenges: List<UserChallenge>,
        showCurrent: Boolean
    ): List<UserChallenge> {
        return if (showCurrent) {
            challenges.filter { it.isActive && !it.isPast }
        } else {
            challenges.filter { it.isPast }
        }
    }
}

