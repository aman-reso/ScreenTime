package com.app.screentime.challenge.service

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.ActiveChallengesResponse
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.ChallengeRankingsResponse
import com.app.screentime.network.model.JoinChallengeRequest
import com.app.screentime.network.model.JoinChallengeResponse
import com.app.screentime.network.model.UserChallengesResponse
import com.app.screentime.network.model.BatchChallengeStatsRequest
import com.app.screentime.network.model.BatchChallengeStatsResponse
import com.app.screentime.network.model.ChallengeStatsRequest

/** Interface to obtain challenge leaderboards and ranks. */
interface ChallengeService {
    /**
     * Get all active challenges (public endpoint, no auth required)
     */
    suspend fun getActiveChallenges(): Result<ApiResponse<ActiveChallengesResponse>>
    
    /**
     * Get user's challenges (auth required)
     */
    suspend fun getUserChallenges(): Result<ApiResponse<UserChallengesResponse>>
    
    /**
     * Get challenge details by ID (auth required)
     */
    suspend fun getChallengeDetails(challengeId: Int): Result<ApiResponse<ChallengeDetails>>
    
    /**
     * Join a challenge (auth required)
     */
    suspend fun joinChallenge(challengeId: Int): Result<ApiResponse<JoinChallengeResponse>>
    
    /**
     * Get challenge rankings (auth required)
     */
    suspend fun getChallengeRankings(challengeId: Int): Result<ApiResponse<ChallengeRankingsResponse>>
    
    /**
     * Submit challenge stats (auth required)
     */
    suspend fun submitChallengeStats(request: ChallengeStatsRequest): Result<ApiResponse<Unit>>
    
    /**
     * Submit batch challenge stats (auth required)
     */
    suspend fun submitBatchChallengeStats(request: BatchChallengeStatsRequest): Result<ApiResponse<BatchChallengeStatsResponse>>
}
