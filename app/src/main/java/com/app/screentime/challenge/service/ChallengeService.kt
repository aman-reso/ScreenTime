package com.app.screentime.challenge.service

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.ChallengeOverviewResponse

/** Interface to obtain challenge leaderboards and ranks. */
interface ChallengeService {
    suspend fun getChallengeOverview(): Result<ApiResponse<ChallengeOverviewResponse>>
    suspend fun joinChallenge(challengeId: String): Result<ApiResponse<Unit>>
}
