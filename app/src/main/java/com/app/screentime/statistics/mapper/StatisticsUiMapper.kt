package com.app.screentime.statistics.mapper

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport
import com.app.screentime.landing.model.CategoryUsage
import com.app.screentime.landing.util.AppCategoryUtils
import com.app.screentime.statistics.model.ChartFormatterProps
import com.app.screentime.statistics.model.StatisticsUiProps
import com.telekom.odsystem.organisms.barchart.ODSBarItemDirection
import com.telekom.odsystem.organisms.barchart.ODSBarItemProps
import javax.inject.Inject

/**
 * Mapper that converts use case results to UI Props for Statistics Screen
 */
class StatisticsUiMapper @Inject constructor() {

    /**
     * Map weekly reports to Statistics UI Props
     */
    fun toUiProps(
        weeklyReports: List<WeeklyDataReport>,
        selectedDayIndex: Int?,
        chartOrientation: ODSBarItemDirection = ODSBarItemDirection.VERTICAL,
        isLoading: Boolean = false,
        error: String? = null
    ): StatisticsUiProps {
        // Convert weekly reports to bar chart data
        // For vertical charts: xValue = index, yValue = data
        // For horizontal charts: xValue = data, yValue = index (swapped)
        val barChartData = weeklyReports.mapIndexed { index, report ->
            val screenTimeHours = (report.totalScreenTime ?: 0L) / (1000.0 * 60.0 * 60.0)
            val dayLabel = when {
                report.dayName.isNullOrEmpty() -> "${index + 1}"
                report.dayName.length >= 3 -> report.dayName.take(3).uppercase()
                else -> report.dayName.uppercase()
            }
            if (chartOrientation == ODSBarItemDirection.HORIZONTAL) {
                // Horizontal: xValue is the data (bar length), yValue is the index (position)
                ODSBarItemProps(
                    xValue = screenTimeHours,
                    xLabel = String.format("%.1f", screenTimeHours),
                    yValue = index.toDouble(),
                    yLabel = dayLabel
                )
            } else {
                // Vertical: xValue is the index (position), yValue is the data (bar height)
            ODSBarItemProps(
                xValue = index.toDouble(),
                xLabel = dayLabel,
                yValue = screenTimeHours,
                yLabel = String.format("%.1f", screenTimeHours)
            )
            }
        }

        // Get selected day app usage list (sorted by screen time) and report
        val selectedDayReport = selectedDayIndex?.let { dayIndex ->
            if (dayIndex in weeklyReports.indices) {
                weeklyReports[dayIndex]
            } else {
                null
            }
        }

        val selectedDayAppUsageList = selectedDayReport?.appUsage?.sortedByDescending { it.appScreenTime } ?: emptyList()

        // Calculate category usage for selected day
        val categoryUsage = if (selectedDayAppUsageList.isNotEmpty()) {
            val totalScreenTime = selectedDayAppUsageList.sumOf { it.appScreenTime }
            val categoryMap = AppCategoryUtils.groupByCategory(selectedDayAppUsageList)
            categoryMap.map { (category, time) ->
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
        } else {
            emptyList()
        }

        return StatisticsUiProps(
            barChartData = barChartData,
            selectedDayAppUsageList = selectedDayAppUsageList,
            selectedDayIndex = selectedDayIndex,
            selectedDayReport = selectedDayReport,
            weeklyReports = weeklyReports,
            categoryUsage = categoryUsage,
            chartOrientation = chartOrientation,
            isLoading = isLoading,
            error = error
        )
    }

    /**
     * Create chart formatter props
     */
    fun createChartFormatterProps(): ChartFormatterProps {
        return ChartFormatterProps(
            valueFormatter = { x, y ->
                val hours = y.toInt()
                val minutes = ((y - hours) * 60).toInt()
                when {
                    hours > 0 -> "${hours}h ${minutes}m"
                    minutes > 0 -> "${minutes}m"
                    else -> "< 1m"
                }
            },
            verticalAxisFormatter = { value ->
                val hours = value.toInt()
                if (hours > 0) "${hours}h" else "${value.toInt()}h"
            }
        )
    }
}

