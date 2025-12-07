package com.app.screentime.landing.usecase

import android.content.Context
import com.app.screentime.R
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.landing.mapper.LandingUiMapper
import com.app.screentime.landing.model.CategoryUsage
import com.app.screentime.landing.model.LandingUiProps
import com.app.screentime.landing.model.TodayUsageData
import com.app.screentime.landing.util.AppCategoryUtils
import com.app.screentime.network.model.BatchUsageEventsRequest
import com.app.screentime.network.model.UsageEvent
import com.app.screentime.network.sync.DataSyncService
import com.app.screentime.network.sync.SyncResult
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.utils.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val landingUiMapper: LandingUiMapper,
    private val preferencesUseCase: PreferencesUseCase,
    private val preferencesManager: PreferencesManager,
    private val dataSyncService: DataSyncService,
    private val challengeRepository: ChallengeRepository,
    @ApplicationContext private val context: Context
) {
    /**
     * Get yesterday's total screen time
     * @return Total screen time in milliseconds for yesterday, or null if unavailable
     */
    private suspend fun getYesterdayTotalScreenTime(): Long? {
        return withContext(Dispatchers.IO) {
            try {
                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
                calendar.add(Calendar.DAY_OF_MONTH, -1) // Go to yesterday
                
                val yesterdayStart = calendar.timeInMillis
                calendar.add(Calendar.DAY_OF_MONTH, 1) // End of yesterday
                val yesterdayEnd = calendar.timeInMillis
                
                val yesterdayReport = localAppUsageRepository.getAppsUsageForInterval(
                    yesterdayStart, yesterdayEnd
                )
                val yesterdayTotal = yesterdayReport.sumOf { it.appScreenTime }
                if (yesterdayTotal > 0) yesterdayTotal else null
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Get today's usage data with optimized calculations
     * @return Result containing today's usage data or error
     */
    suspend fun getTodayUsageData(): Result<TodayUsageData> {
        return withContext(Dispatchers.IO) {
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
                System.out.println("time-->$size")
                val todayReport = localAppUsageRepository.fetchAppUsageTodayTillNow()

                // Calculate totals once and reuse
                val totalScreenTime = todayReport.sumOf { it.appScreenTime }
                val totalWifiData = todayReport.sumOf { it.wifiDataUsage }
                val totalMobileData = todayReport.sumOf { it.mobileDataUsage }
                val totalData = totalWifiData + totalMobileData

                val topUsedApps = todayReport.asSequence()
                    .sortedByDescending { it.appScreenTime }
                    .toList()

                // Calculate category-wise usage
                val categoryMap = AppCategoryUtils.groupByCategory(todayReport)
                val categoryUsage = categoryMap.map { (category, time) ->
                    val percentage = if (totalScreenTime > 0) {
                        (time.toFloat() / totalScreenTime.toFloat()) * 100f
                    } else {
                        0f
                    }
                    // Format duration
                    val totalMinutes = time / (1000 * 60)
                    val hours = totalMinutes / 60
                    val minutes = totalMinutes % 60
                    val formattedTime = if (hours > 0) {
                        if (minutes > 0) "$hours h $minutes m" else "$hours hr"
                    } else if (minutes > 0) {
                        "$minutes min"
                    } else {
                        "${time / 1000} sec"
                    }
                    CategoryUsage(
                        category = category,
                        totalScreenTime = time,
                        formattedTime = formattedTime,
                        percentage = percentage
                    )
                }.sortedByDescending { it.totalScreenTime }

                val todayUsageData = TodayUsageData(
                    todayTotalScreenTime = totalScreenTime,
                    todayTotalWifiDataUsage = totalWifiData,
                    todayTotalMobileDataUsage = totalMobileData,
                    topUsedApps = topUsedApps,
                    displayWifiDataUsage = totalWifiData.toReadableDataSize(),
                    displayMobileDataUsage = totalMobileData.toReadableDataSize(),
                    displayTotalDataUsage = totalData.toReadableDataSize(),
                    categoryUsage = categoryUsage
                )

                Result.success(todayUsageData)
            } catch (e: SecurityException) {
                Result.failure(SecurityException("Permission denied: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Failed to load usage data: ${e.message}", e))
            }
        }
    }

    /**
     * Get Landing UI Props
     * This is the main method that returns all UI state needed for the landing screen
     */
    suspend fun getLandingUiProps(
        username: String?,
        isLoading: Boolean = false,
        error: String? = null
    ): LandingUiProps {
        val shouldShowConsent = preferencesUseCase.shouldShowConsentSheet()

        if (isLoading) {
            return landingUiMapper.toLoadingUiProps(username, shouldShowConsent)
        }

        if (error != null) {
            return landingUiMapper.toErrorUiProps(username, error, shouldShowConsent)
        }

        // Default chart colors - these could be moved to a theme/config if needed
        val chartColors = listOf(
            com.telekom.odsystem.foundations.HexColor(0xFF0070CC), // basicAccent
            com.telekom.odsystem.foundations.HexColor(0xFF00A651), // functionalSuccessStandard
            com.telekom.odsystem.foundations.HexColor(0xFF0070CC), // functionalInformationalStandard
            com.telekom.odsystem.foundations.HexColor(0xFFFFB300), // functionalWarningStandard
            com.telekom.odsystem.foundations.HexColor(0xFF0070CC)  // basicAccent
        )

        return getTodayUsageData().fold(
            onSuccess = { todayUsageData ->
                // Get yesterday's total screen time and calculate percentage change
                val yesterdayTotal = getYesterdayTotalScreenTime()
                val percentageChange = yesterdayTotal?.let { yesterday ->
                    if (yesterday > 0) {
                        ((todayUsageData.todayTotalScreenTime - yesterday).toFloat() / yesterday) * 100f
                    } else {
                        null
                    }
                }
                
                // Fetch joined challenges for notification
                val joinedChallenges = getJoinedChallenges()
                
                landingUiMapper.toUiProps(
                    todayUsageData = todayUsageData,
                    username = username,
                    shouldShowConsent = shouldShowConsent,
                    isLoading = false,
                    error = null,
                    chartColors = chartColors,
                    percentageChangeFromYesterday = percentageChange,
                    joinedChallenges = joinedChallenges
                )
            },
            onFailure = { exception ->
                val errorMessage = when (exception) {
                    is SecurityException -> context.getString(
                        R.string.permission_denied,
                        exception.message ?: ""
                    )

                    else -> context.getString(
                        R.string.failed_to_load_usage_data,
                        exception.message ?: ""
                    )
                }
                landingUiMapper.toErrorUiProps(username, errorMessage, shouldShowConsent)
            }
        )
    }

    /**
     * Check if consent screen should be shown
     */
    fun shouldShowConsentScreen(): Boolean {
        return preferencesUseCase.shouldShowConsentSheet()
    }

    /**
     * Mark consent as shown
     */
    fun markConsentShown() {
        preferencesUseCase.markConsentSheetShown()
    }

    /**
     * Get user's joined challenges (only active ones for notification)
     */
    private suspend fun getJoinedChallenges(): List<com.app.screentime.network.model.UserChallenge> {
        return withContext(Dispatchers.IO) {
            try {
                challengeRepository.getUserChallenges().fold(
                    onSuccess = { response ->
                        if (response.success == true && response.data != null) {
                            // Return only active challenges (not expired)
                            response.data.challenges.filter { it.isActive && !it.isPast }
                        } else {
                            emptyList()
                        }
                    },
                    onFailure = {
                        emptyList()
                    }
                )
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

}
