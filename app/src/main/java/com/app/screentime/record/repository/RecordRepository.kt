package com.app.screentime.record.repository

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.network.model.BatchUsageRecord
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.network.model.UsageStatsResponse
import com.app.screentime.network.repository.screentime.ScreenTimeRepository
import javax.inject.Inject

/**
 * Repository for recording and retrieving usage data
 */
class RecordRepository @Inject constructor(
    private val screenTimeRepository: ScreenTimeRepository
) {

    /**
     * Get app stats from /api/app-stats endpoint
     * Returns app usage data in AppUsage format
     */
    suspend fun getAppStats(date: String, targetUserName: String): Result<List<AppUsage>> {
        return screenTimeRepository.getAppStats(date, targetUserName).map { apiResponse ->
            val appStatsResponse = apiResponse.data ?: throw Exception("No data in response")
            appStatsResponse.stats?.map { statItem ->
                AppUsage(
                    packageName = statItem.packageName ?: "",
                    appName = statItem.appName ?: "",
                    appScreenTime = statItem.duration ?: 0L
                )
            } ?: emptyList()
        }
    }
}
