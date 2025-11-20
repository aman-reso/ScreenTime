package com.app.screentime.challenge.repository

import com.app.screentime.challenge.service.ChallengeService
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.ChallengeOverviewResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService
) {
    suspend fun getChallengeOverview(): Result<ApiResponse<ChallengeOverviewResponse>> {
        return challengeService.getChallengeOverview()
    }

    suspend fun joinChallenge(challengeId: String): Result<ApiResponse<Unit>> {
        return challengeService.joinChallenge(challengeId)
    }
}
