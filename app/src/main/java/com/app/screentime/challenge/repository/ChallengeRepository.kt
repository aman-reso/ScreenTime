package com.app.screentime.challenge.repository

import com.app.screentime.challenge.service.ChallengeService
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.ActiveChallengesResponse
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.ChallengeRankingsResponse
import com.app.screentime.network.model.JoinChallengeResponse
import com.app.screentime.network.model.UserChallengesResponse
import com.app.screentime.network.model.BatchChallengeStatsRequest
import com.app.screentime.network.model.BatchChallengeStatsResponse
import com.app.screentime.network.model.ChallengeStatsRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService
) {
    suspend fun getActiveChallenges(): Result<ApiResponse<ActiveChallengesResponse>> {
        return challengeService.getActiveChallenges()
    }

    suspend fun getUserChallenges(): Result<ApiResponse<UserChallengesResponse>> {
        return challengeService.getUserChallenges()
    }

    suspend fun getChallengeDetails(challengeId: Int): Result<ApiResponse<ChallengeDetails>> {
        return challengeService.getChallengeDetails(challengeId)
    }

    suspend fun joinChallenge(challengeId: Int): Result<ApiResponse<JoinChallengeResponse>> {
        return challengeService.joinChallenge(challengeId)
    }

    suspend fun getChallengeRankings(challengeId: Int): Result<ApiResponse<ChallengeRankingsResponse>> {
        return challengeService.getChallengeRankings(challengeId)
    }

    suspend fun submitChallengeStats(request: ChallengeStatsRequest): Result<ApiResponse<Unit>> {
        return challengeService.submitChallengeStats(request)
    }

    suspend fun submitBatchChallengeStats(request: BatchChallengeStatsRequest): Result<ApiResponse<BatchChallengeStatsResponse>> {
        return challengeService.submitBatchChallengeStats(request)
    }
}
