package com.app.screentime.network.service.screentime

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.*

/**
 * Service interface for Screen Time related API operations
 */
interface ScreenTimeService {

    // Batch events submission
    suspend fun syncBatchUsageEvents(request: BatchUsageEventsRequest): Result<ApiResponse<Unit>>

    // Get daily usage stats for a target user
    suspend fun getDailyUsageStats(
        date: String,
        targetUserId: String
    ): Result<ApiResponse<UsageStatsResponse>>

    // Get last sync time for usage stats
    suspend fun getUsageLastSyncTime(): Result<ApiResponse<UsageLastSyncResponse>>

    // Get summary screen time for multiple users
    suspend fun getSummaryScreenTime(request: SummaryScreenTimeRequest): Result<ApiResponse<SummaryScreenTimeResponseData>>

    // Submit app stats for a date
    suspend fun submitAppStats(request: AppStatsRequest): Result<ApiResponse<AppStatsResponse>>

    // Get app stats for a date
    suspend fun getAppStats(date: String, targetUserName: String): Result<ApiResponse<AppStatsGetResponse>>
}

