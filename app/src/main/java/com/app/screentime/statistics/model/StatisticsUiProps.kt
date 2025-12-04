package com.app.screentime.statistics.model

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport
import com.telekom.odsystem.organisms.barchart.ODSBarItemDirection
import com.telekom.odsystem.organisms.barchart.ODSBarItemProps

/**
 * UI Props for Statistics Screen
 * Contains all the data needed to render the statistics screen
 */
data class StatisticsUiProps(
    val barChartData: List<ODSBarItemProps>,
    val selectedDayAppUsageList: List<AppUsage>,
    val selectedDayIndex: Int?,
    val selectedDayReport: WeeklyDataReport?,
    val weeklyReports: List<WeeklyDataReport>,
    val chartOrientation: ODSBarItemDirection = ODSBarItemDirection.VERTICAL,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Props for formatting chart values
 */
data class ChartFormatterProps(
    val valueFormatter: (Double, Double) -> String,
    val verticalAxisFormatter: (Double) -> String
)

