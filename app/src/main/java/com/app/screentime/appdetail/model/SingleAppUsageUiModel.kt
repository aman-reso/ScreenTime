package com.app.screentime.appdetail.model

/**
 * UI State for Single App Usage Detail Screen
 */
data class SingleAppUsageUiState(
    val isLoading: Boolean = false,
    val appName: String = "",
    val packageName: String = "",
    val weeklyUsageData: List<DailyAppUsageData> = emptyList(),
    val error: String? = null
)

/**
 * Daily app usage data for a specific app
 */
data class DailyAppUsageData(
    val dayName: String,
    val date: String,
    val screenTime: Long,  // in milliseconds
    val displayScreenTime: String,
    val wifiDataUsage: Long,
    val mobileDataUsage: Long,
    val displayWifiDataUsage: String,
    val displayMobileDataUsage: String,
    val displayTotalDataUsage: String
)

