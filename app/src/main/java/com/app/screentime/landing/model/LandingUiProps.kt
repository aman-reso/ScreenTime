package com.app.screentime.landing.model

import com.app.screentime.customisation.model.ColorOption
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.network.model.UserChallenge

/**
 * UI Props for Landing Screen
 * Contains all the data needed to render the landing screen
 * This is the only data structure the UI layer should use
 */
data class LandingUiProps(
    val username: String?,
    val isLoading: Boolean = false,
    val todayTotalScreenTime: Long = 0L,
    val todayTotalWifiDataUsage: Long = 0L,
    val todayTotalMobileDataUsage: Long = 0L,
    val topUsedApps: List<AppUsage>,
    val displayWifiDataUsage: String?,
    val displayMobileDataUsage: String?,
    val displayTotalDataUsage: String?,
    val usageDonutData: UsageDonutData?,
    val error: String? = null,
    val shouldShowConsent: Boolean = false,
    val percentageChangeFromYesterday: Float? = null, // Percentage change from yesterday (null if no data)
    val categoryUsage: List<CategoryUsage> = emptyList(), // Category-wise usage data
    val joinedChallenges: List<UserChallenge> = emptyList(),
    val totalNotificationCount: Int = 0,
    val challengeBannerURL: String? = null,
    val wallpaperBannerURL: String? = null,
    val yearlyUsageDays: Int = 0, // Number of days mobile was used in the current year
    val dailyScreenTimeFormatted: String = "0h 0m", // Today's formatted screen time
    val yearlyScreenTimeFormatted: String = "0h", // Year's total formatted screen time
    val customColorOption: ColorOption? = null, // Custom color from customisation settings
)

