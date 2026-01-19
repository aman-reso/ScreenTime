package com.app.screentime.network.repository.screentime

import com.app.screentime.core.network.model.ApiResponse
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

    suspend fun getUsageLastSyncTime(): Result<ApiResponse<UsageLastSyncResponse>> {
        return screenTimeService.getUsageLastSyncTime()
    }

    suspend fun getSummaryScreenTime(request: SummaryScreenTimeRequest): Result<ApiResponse<SummaryScreenTimeResponseData>> {
        return screenTimeService.getSummaryScreenTime(request)
    }

    suspend fun submitAppStats(request: AppStatsRequest): Result<ApiResponse<AppStatsResponse>> {
        return screenTimeService.submitAppStats(request)
    }

    suspend fun getAppStats(date: String, targetUserName: String): Result<ApiResponse<AppStatsGetResponse>> {
        return screenTimeService.getAppStats(date, targetUserName)
    }
}

