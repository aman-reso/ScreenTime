package com.app.screentime.landing.usecase

import android.content.Context
import android.util.Log
import com.app.screentime.config.R
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.customisation.model.ColorOption
import com.app.screentime.landing.mapper.LandingUiMapper
import com.app.screentime.landing.model.CategoryUsage
import com.app.screentime.landing.model.LandingUiProps
import com.app.screentime.landing.model.TodayUsageData
import com.app.screentime.landing.util.AppCategoryUtils
import com.app.screentime.network.model.UserChallenge
import com.app.screentime.network.sync.DataSyncService
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.leaderboard.service.LeaderboardService
import com.app.screentime.network.model.LeaderboardStatsUpdateRequest
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.utils.DateUtils
import com.telekom.odsystem.foundations.HexColor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    private val challengeRepository: ChallengeRepository,
    private val leaderboardService: LeaderboardService,
    @ApplicationContext private val context: Context
) {
    /**
     * Get yesterday's total screen time
     * @return Total screen time in milliseconds for yesterday, or null if unavailable
     */
    private suspend fun getYesterdayTotalScreenTime(): Long? {
        return withContext(Dispatchers.IO) {
            try {
                val todayStart = DateUtils.startOfToday()
                val yesterdayStart = DateUtils.minusDays(todayStart, 1)

                val yesterdayReport = localAppUsageRepository.getAppsUsageForInterval(
                    yesterdayStart.millis, todayStart.millis
                )
                val yesterdayTotal = yesterdayReport.sumOf { it.appScreenTime }
                if (yesterdayTotal > 0) yesterdayTotal else null
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Format screen time in milliseconds to human-readable string
     * @param millis Screen time in milliseconds
     * @return Formatted string like "5h 30m" or "240h"
     */
    private fun formatScreenTime(millis: Long): String {
        val hours = (millis / (60 * 60 * 1000)).toInt()
        val minutes = ((millis % (60 * 60 * 1000)) / (60 * 1000)).toInt()
        
        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "0m"
        }
    }

    /**
     * Load custom color from preferences
     * @return ColorOption if saved, null otherwise
     */
    private suspend fun getCustomColor(): ColorOption? {
        return withContext(Dispatchers.IO) {
            try {
                val colorId = preferencesManager.getString("custom_service_color_id", null)
                colorId?.let { id ->
                    ColorOption.DEFAULT_PALETTE.find { it.id == id }
                }
            } catch (e: Exception) {
                Log.e("LandingUsecase", "Error loading custom color: ${e.message}", e)
                null
            }
        }
    }

    /**
     * Calculate total screen time accumulated in the current year and convert to days
     * Sums all app usage screen time and converts milliseconds to days
     * @return Triple of (days, formatted yearly time, total yearly millis)
     */
    private suspend fun getYearlyUsageStats(): Triple<Int, String, Long> {
        return withContext(Dispatchers.IO) {
            try {
                val now = DateUtils.now()
                val yearStart = DateUtils.startOfYear(now)
                val yearStartMillis = yearStart.millis
                val nowMillis = now.millis

                // Get all app usage data for the current year
                val yearlyReport = localAppUsageRepository.getAppsUsageForInterval(
                    yearStartMillis,
                    nowMillis
                )
                
                // Sum all screen time for the year
                val totalScreenTimeMs = yearlyReport.sumOf { it.appScreenTime }
                
                // Convert milliseconds to days
                // 1 day = 24 hours = 24 * 60 * 60 * 1000 milliseconds = 86,400,000 ms
                val totalDays = (totalScreenTimeMs / (24 * 60 * 60 * 1000)).toInt()
                
                // Format the yearly screen time
                val formattedYearly = formatScreenTime(totalScreenTimeMs)
                
                Triple(totalDays, formattedYearly, totalScreenTimeMs)
            } catch (e: Exception) {
                Log.e("LandingUsecase", "Error calculating yearly usage days: ${e.message}", e)
                Triple(0, "0h", 0L)
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
                // Data collection disabled - no events collected for sync
                val todayReport = localAppUsageRepository.fetchAppUsageTodayTillNow()

                // Calculate totals once and reuse
                val totalScreenTime = todayReport.sumOf { it.appScreenTime }
                val totalWifiData = todayReport.sumOf { it.wifiDataUsage }
                val totalMobileData = todayReport.sumOf { it.mobileDataUsage }
                val totalData = totalWifiData + totalMobileData
                val totalNotificationCount = todayReport.sumOf { it.notificationCount }

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
                    categoryUsage = categoryUsage,
                    notificationCount = if (totalNotificationCount <= 0) {
                        0
                    } else {
                        totalNotificationCount

                    }
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

        val chartColors = listOf(
            HexColor(0xFF0070CC), // basicAccent
            HexColor(0xFF00A651), // functionalSuccessStandard
            HexColor(0xFF0070CC), // functionalInformationalStandard
            HexColor(0xFFFFB300), // functionalWarningStandard
            HexColor(0xFF0070CC)  // basicAccent
        )

        return getTodayUsageData().fold(
            onSuccess = { todayUsageData ->
                val yesterdayTotal = getYesterdayTotalScreenTime()
                val percentageChange = yesterdayTotal?.let { yesterday ->
                    if (yesterday > 0) {
                        ((todayUsageData.todayTotalScreenTime - yesterday).toFloat() / yesterday) * 100f
                    } else {
                        null
                    }
                }

                // Calculate yearly usage stats
                val (yearlyUsageDays, yearlyScreenTimeFormatted, _) = getYearlyUsageStats()
                
                // Format daily screen time
                val dailyScreenTimeFormatted = formatScreenTime(todayUsageData.todayTotalScreenTime)
                
                // Load custom color
                val customColor = getCustomColor()

                landingUiMapper.toUiProps(
                    todayUsageData = todayUsageData,
                    username = username,
                    shouldShowConsent = shouldShowConsent,
                    isLoading = false,
                    error = null,
                    chartColors = chartColors,
                    percentageChangeFromYesterday = percentageChange,
                    joinedChallenges = emptyList(),
                    yearlyUsageDays = yearlyUsageDays,
                    dailyScreenTimeFormatted = dailyScreenTimeFormatted,
                    yearlyScreenTimeFormatted = yearlyScreenTimeFormatted,
                    customColorOption = customColor
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
    suspend fun getJoinedChallenges(): List<UserChallenge> {
        return withContext(Dispatchers.IO) {
            try {
                challengeRepository.getUserChallenges().fold(
                    onSuccess = { response ->
                        if (response.success == true && response.data != null) {
                            // Return only active challenges (not expired)
                            response.data!!.challenges.filter { it.isActive && !it.isPast }
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

    /**
     * Sync leaderboard stats to server
     * Calculates total screen time from 12:00 AM (midnight) to current time
     * Called from landing screen to update leaderboard stats
     */
    suspend fun syncLeaderboardStats() {
        withContext(Dispatchers.IO) {
            try {
                if (!preferencesManager.isConsentScreenShown()) {
                    return@withContext
                }

                val now = DateUtils.now()
                val periodDate = DateUtils.format(now, "yyyy-MM-dd")
                val startOfToday = DateUtils.startOfToday()
                val startMillis = startOfToday.millis

                val currentMillis = now.millis

                val appUsageList = localAppUsageRepository.getAppsUsageForInterval(
                    startMillis,
                    currentMillis
                )

                val totalScreenTime = appUsageList.sumOf { it.appScreenTime }

                if (totalScreenTime <= 0) {
                    return@withContext
                }

                // Create request
                val request = LeaderboardStatsUpdateRequest(
                    period = "daily",
                    periodDate = periodDate,
                    totalScreenTime = totalScreenTime,
                    replace = true
                )
                leaderboardService.updateStats(request).fold(
                    onSuccess = {
                        Log.d(
                            "LandingUsecase",
                            "Leaderboard stats synced successfully"
                        )
                    },
                    onFailure = { exception ->
                        Log.d(
                            "LandingUsecase",
                            "Failed to sync leaderboard stats: ${exception.message}"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(
                    "LandingUsecase",
                    "Error syncing leaderboard stats: ${e.message}",
                    e
                )
            }
        }
    }

}
