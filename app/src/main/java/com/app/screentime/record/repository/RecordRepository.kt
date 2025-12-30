package com.app.screentime.record.repository

import com.app.screentime.core.network.model.ApiResponse
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

    suspend fun getDailyUsageStats(
        date: String,
        targetUserId: String
    ): Result<com.app.screentime.network.model.DailyUsage> {
        return screenTimeRepository.getDailyUsageStats(date, targetUserId).map { apiResponse ->
            val statsResponse = apiResponse.data ?: throw Exception("No data in response")
            val stats = statsResponse.stats
            val totalUsage = stats.sumOf { it.duration ?: 0L }

            val groupedByPackage = stats.groupBy { it.packageName }
            val topApps = groupedByPackage.map { (packageName, packageStats) ->
                val totalDuration = packageStats.sumOf { it.duration ?: 0L }
                val firstStat = packageStats.first()
                com.app.screentime.network.model.AppUsageData(
                    packageName = packageName,
                    appName = firstStat.appName,
                    usageTime = totalDuration, // Use sum of durations
                    lastUsed = null,
                    isSystemApp = firstStat.isSystemApp,
                    category = firstStat.category
                )
            }

            com.app.screentime.network.model.DailyUsage(
                date = date,
                totalUsage = totalUsage,
                appCount = groupedByPackage.size,
                topApps = topApps
            )
        }
    }

    suspend fun getRawDailyStats(
        date: String,
        targetUserId: String
    ): Result<List<AppUsageStatsData>> {
        return screenTimeRepository.getDailyUsageStats(date, targetUserId).map { apiResponse ->
            val statsResponse = apiResponse.data ?: throw Exception("No data in response")
            statsResponse.stats
        }
    }
}
