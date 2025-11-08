package com.app.screentime.statistics.model

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport

/**
 * UI State for Statistics Screen
 */
data class StatisticsUiState(
    val isLoading: Boolean = false,
    val weeklyReports: List<WeeklyDataReport> = emptyList(),
    val error: String? = null,
    val selectedDayIndex: Int? = null  // Index of selected day (null = collapsed, Int = selected day)
)

/**
 * UI Model for Statistics Screen
 * Contains flattened list items for displaying daily data in LazyColumn
 */
sealed class StatisticsListItem {
    /**
     * Daily header item showing day name, date, screen time, and data usage
     */
    data class DailyHeaderItem(
        val report: WeeklyDataReport,
        val appUsageList: List<AppUsage>
    ) : StatisticsListItem()

    /**
     * App usage item for a specific day
     */
    data class DailyAppUsageItem(
        val appUsage: AppUsage,
        val dayIndex: Int,
        val appIndex: Int,
        val totalCount: Int
    ) : StatisticsListItem()

    /**
     * Divider item between days
     */
    object DividerItem : StatisticsListItem()
}

