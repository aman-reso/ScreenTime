package com.app.screentime.landing.model

import com.app.screentime.data.entity.AppUsage

/**
 * Data model for today's usage statistics
 * Contains all formatted data ready to be used in UI State
 */
data class TodayUsageData(
    val todayTotalScreenTime: Long,
    val todayTotalWifiDataUsage: Long,
    val todayTotalMobileDataUsage: Long,
    val topUsedApps: List<AppUsage>,
    val displayWifiDataUsage: String?,
    val displayMobileDataUsage: String?,
    val displayTotalDataUsage: String?
) {
    /**
     * Updates LandingUiState with this formatted data
     * @param currentState The current UI state to update
     * @return Updated LandingUiState with all formatted data
     */
    fun updateUiState(currentState: LandingUiState): LandingUiState {
        return currentState.copy(
            todayTotalScreenTime = todayTotalScreenTime,
            todayTotalWifiDataUsage = todayTotalWifiDataUsage,
            todayTotalMobileDataUsage = todayTotalMobileDataUsage,
            topUsedApps = topUsedApps,
            displayWifiDataUsage = displayWifiDataUsage,
            displayMobileDataUsage = displayMobileDataUsage,
            displayTotalDataUsage = displayTotalDataUsage,
            isLoading = false,
            error = null
        )
    }
}

