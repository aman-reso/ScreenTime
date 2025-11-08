package com.app.screentime.appdetail.usecase

import com.app.screentime.appdetail.model.DailyAppUsageData
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.record.repository.toReadableDataSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Use case for retrieving single app usage data
 * Handles business logic for processing weekly app usage reports
 */
class GetSingleAppUsageUseCase @Inject constructor(
    private val localAppUsageRepository: LocalAppUsageRepository
) {
    /**
     * Get weekly usage data for a specific app
     * @param packageName The package name of the app
     * @return Result containing list of daily app usage data or error
     */
    suspend fun getWeeklyAppUsage(packageName: String): Result<List<DailyAppUsageData>> {
        return withContext(Dispatchers.Default) {
            try {
                val weeklyReports = localAppUsageRepository.getOneWeekReport()
                
                // Use sequence for better performance with large datasets
                // Process reports lazily to avoid unnecessary allocations
                val weeklyUsageData = weeklyReports.asSequence()
                    .map { report ->
                        // Find the app in this day's usage using firstOrNull for better performance
                        val appUsage = report.appUsage?.firstOrNull { it.packageName == packageName }
                        
                        val screenTime = appUsage?.appScreenTime ?: 0L
                        val wifiData = appUsage?.wifiDataUsage ?: 0L
                        val mobileData = appUsage?.mobileDataUsage ?: 0L
                        val totalData = wifiData + mobileData
                        
                        DailyAppUsageData(
                            dayName = report.dayName ?: "",
                            date = report.date ?: "",
                            screenTime = screenTime,
                            displayScreenTime = formatDuration(screenTime),
                            wifiDataUsage = wifiData,
                            mobileDataUsage = mobileData,
                            displayWifiDataUsage = wifiData.toReadableDataSize() ?: "0 B",
                            displayMobileDataUsage = mobileData.toReadableDataSize() ?: "0 B",
                            displayTotalDataUsage = totalData.toReadableDataSize() ?: "0 B"
                        )
                    }
                    .toList()
                
                Result.success(weeklyUsageData)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

