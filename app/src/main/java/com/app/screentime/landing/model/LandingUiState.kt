package com.app.screentime.landing.model

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport

/**
 * UI State for Landing Screen
 */
data class LandingUiState(
    val isLoading: Boolean = false,
    val todayTotalScreenTime: Long = 0L,
    val todayTotalWifiDataUsage: Long = 0L,
    val todayTotalMobileDataUsage: Long = 0L,
    val topUsedApps: List<AppUsage>? = emptyList(),
    val weeklyReports: List<WeeklyDataReport> = emptyList(),
    val displayWifiDataUsage: String? = null,
    val displayMobileDataUsage: String? = null,
    val displayTotalDataUsage: String? = null,
    val username: String? = null,
    val error: String? = null
)

