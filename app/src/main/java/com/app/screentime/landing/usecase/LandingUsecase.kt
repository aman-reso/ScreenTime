package com.app.screentime.landing.usecase

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.landing.model.TodayUsageData
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.toReadableDataSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

/**
 * Use case for landing screen operations
 * Handles business logic for fetching and processing today's usage data
 */
class LandingUsecase @Inject constructor(
    private val localAppUsageRepository: LocalAppUsageRepository,
) {
    /**
     * Get today's usage data with optimized calculations
     * @return Result containing today's usage data or error
     */
    suspend fun getTodayUsageData(): Result<TodayUsageData> {
        return withContext(Dispatchers.Main) {

            try {

                val currentTime = System.currentTimeMillis()
                val midNightCal = Calendar.getInstance()
                midNightCal[Calendar.HOUR_OF_DAY] = 0
                midNightCal[Calendar.MINUTE] = 0
                midNightCal[Calendar.SECOND] = 0
                midNightCal[Calendar.MILLISECOND] = 0

                val allEvents = localAppUsageRepository.collectEventsForSync(
                    startMsEpoch = midNightCal.timeInMillis, endMsEpoch = currentTime
                )
                val size = allEvents.filter { it.duration != null }.sumOf { it.duration ?: 0 }
                val todayReport = localAppUsageRepository.fetchAppUsageTodayTillNow()

                // Calculate totals once and reuse
                val totalScreenTime = todayReport.sumOf { it.appScreenTime }
                val totalWifiData = todayReport.sumOf { it.wifiDataUsage }
                val totalMobileData = todayReport.sumOf { it.mobileDataUsage }
                val totalData = totalWifiData + totalMobileData

                val topUsedApps = todayReport.asSequence()
                    .sortedByDescending { it.appScreenTime }
                    .toList()

                val todayUsageData = TodayUsageData(
                    todayTotalScreenTime = totalScreenTime,
                    todayTotalWifiDataUsage = totalWifiData,
                    todayTotalMobileDataUsage = totalMobileData,
                    topUsedApps = topUsedApps,
                    displayWifiDataUsage = totalWifiData.toReadableDataSize(),
                    displayMobileDataUsage = totalMobileData.toReadableDataSize(),
                    displayTotalDataUsage = totalData.toReadableDataSize()
                )

                Result.success(todayUsageData)
            } catch (e: SecurityException) {
                Result.failure(SecurityException("Permission denied: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Failed to load usage data: ${e.message}", e))
            }
        }
    }
}
