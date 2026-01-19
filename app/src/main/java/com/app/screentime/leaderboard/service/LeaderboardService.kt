package com.app.screentime.leaderboard.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.LeaderboardResponse
import com.app.screentime.network.model.LeaderboardStatsUpdateRequest

/**
 * Service interface for Leaderboard operations
 */
interface LeaderboardService {
    /**
     * Get daily leaderboard
     * @param date Optional date in format "yyyy-MM-dd". If null, uses today's date
     */
    suspend fun getDailyLeaderboard(date: String? = null): Result<ApiResponse<LeaderboardResponse>>

    /**
     * Get weekly leaderboard
     */
    suspend fun getWeeklyLeaderboard(): Result<ApiResponse<LeaderboardResponse>>
    

    /**
     * Update leaderboard stats for a period
     * @param request LeaderboardStatsUpdateRequest containing period, date, and screen time
     */
    suspend fun updateStats(request: LeaderboardStatsUpdateRequest): Result<ApiResponse<Unit>>
}

