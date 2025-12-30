package com.app.screentime.leaderboard.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.LeaderboardResponse

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
     * Get monthly leaderboard
     */
    suspend fun getMonthlyLeaderboard(): Result<ApiResponse<LeaderboardResponse>>
}

