package com.app.screentime.leaderboard.repository

import com.app.screentime.leaderboard.service.LeaderboardService
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.LeaderboardResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Leaderboard operations
 */
class LeaderboardRepository @Inject constructor(
    private val leaderboardService: LeaderboardService
) {
    suspend fun getDailyLeaderboard(date: String? = null): Result<ApiResponse<LeaderboardResponse>> {
        return leaderboardService.getDailyLeaderboard(date)
    }

    suspend fun getWeeklyLeaderboard(): Result<ApiResponse<LeaderboardResponse>> {
        return leaderboardService.getWeeklyLeaderboard()
    }

}

