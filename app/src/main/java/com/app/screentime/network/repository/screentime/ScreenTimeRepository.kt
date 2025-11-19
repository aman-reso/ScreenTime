package com.app.screentime.network.repository.screentime

import com.app.screentime.network.model.*
import com.app.screentime.network.service.screentime.ScreenTimeService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Screen Time related network operations
 */
@Singleton
class ScreenTimeRepository @Inject constructor(
    private val screenTimeService: ScreenTimeService
) {

    suspend fun syncBatchUsageEvents(request: BatchUsageEventsRequest): Result<ApiResponse<Unit>> {
        return screenTimeService.syncBatchUsageEvents(request)
    }

    suspend fun getDailyUsageStats(
        date: String,
        targetUserId: String
    ): Result<ApiResponse<UsageStatsResponse>> {
        return screenTimeService.getDailyUsageStats(date, targetUserId)
    }
}

