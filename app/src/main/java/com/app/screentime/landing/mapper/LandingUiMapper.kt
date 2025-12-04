package com.app.screentime.landing.mapper

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.landing.model.LandingUiProps
import com.app.screentime.landing.model.TodayUsageData
import com.app.screentime.landing.model.UsageDonutData
import com.app.screentime.landing.model.UsageSegment
import com.telekom.odsystem.foundations.HexColor
import javax.inject.Inject

/**
 * Mapper that converts use case results to UI Props
 * This is the only way the UI layer should receive data
 */
class LandingUiMapper @Inject constructor() {

    /**
     * Map TodayUsageData and username to UI Props
     */
    fun toUiProps(
        todayUsageData: TodayUsageData,
        username: String?,
        shouldShowConsent: Boolean = false,
        isLoading: Boolean = false,
        error: String? = null,
        chartColors: List<HexColor>,
        percentageChangeFromYesterday: Float? = null
    ): LandingUiProps {
        val usageDonutData = if (todayUsageData.topUsedApps.isNotEmpty()) {
            createUsageDonutData(
                apps = todayUsageData.topUsedApps,
                totalScreenTime = todayUsageData.todayTotalScreenTime,
                chartColors = chartColors
            )
        } else {
            null
        }

        return LandingUiProps(
            username = username,
            isLoading = isLoading,
            todayTotalScreenTime = todayUsageData.todayTotalScreenTime,
            todayTotalWifiDataUsage = todayUsageData.todayTotalWifiDataUsage,
            todayTotalMobileDataUsage = todayUsageData.todayTotalMobileDataUsage,
            topUsedApps = todayUsageData.topUsedApps,
            displayWifiDataUsage = todayUsageData.displayWifiDataUsage,
            displayMobileDataUsage = todayUsageData.displayMobileDataUsage,
            displayTotalDataUsage = todayUsageData.displayTotalDataUsage,
            usageDonutData = usageDonutData,
            error = error,
            shouldShowConsent = shouldShowConsent,
            percentageChangeFromYesterday = percentageChangeFromYesterday,
            categoryUsage = todayUsageData.categoryUsage
        )
    }

    /**
     * Map to UI Props with loading state
     */
    fun toLoadingUiProps(
        username: String?,
        shouldShowConsent: Boolean = false
    ): LandingUiProps {
        return LandingUiProps(
            username = username,
            isLoading = true,
            topUsedApps = emptyList(),
            displayWifiDataUsage = null,
            displayMobileDataUsage = null,
            displayTotalDataUsage = null,
            usageDonutData = null,
            shouldShowConsent = shouldShowConsent
        )
    }

    /**
     * Map to UI Props with error state
     */
    fun toErrorUiProps(
        username: String?,
        error: String,
        shouldShowConsent: Boolean = false
    ): LandingUiProps {
        return LandingUiProps(
            username = username,
            isLoading = false,
            topUsedApps = emptyList(),
            displayWifiDataUsage = null,
            displayMobileDataUsage = null,
            displayTotalDataUsage = null,
            usageDonutData = null,
            error = error,
            shouldShowConsent = shouldShowConsent
        )
    }

    /**
     * Create usage donut data from app usage list
     * This contains the business logic for calculating donut chart segments
     */
    private fun createUsageDonutData(
        apps: List<AppUsage>,
        totalScreenTime: Long,
        chartColors: List<HexColor>
    ): UsageDonutData {
        if (apps.isEmpty()) {
            return UsageDonutData(
                formattedTotalTime = formatTotalTime(0L),
                segments = emptyList()
            )
        }

        val total = apps.sumOf { it.appScreenTime }.coerceAtLeast(1L)
        val sorted = apps.sortedByDescending { it.appScreenTime }
        val top = sorted.take(5)
        val othersTime = sorted.drop(5).sumOf { it.appScreenTime }

        val topSegments = top.mapIndexed { index, app ->
            val percent = (app.appScreenTime.toFloat() / total.toFloat()) * 100f
            UsageSegment(
                name = app.appName ?: app.packageName.orEmpty(),
                percentage = percent,
                color = chartColors[index % chartColors.size]
            )
        }

        // Use muted color from palette for "Others"
        val othersColor =
            if (chartColors.size > 4) chartColors[4] else chartColors.lastOrNull() ?: HexColor(0xFF9E9E9E)
        val withOthers = if (othersTime > 0) {
            topSegments + UsageSegment(
                name = "Others",
                percentage = (othersTime.toFloat() / total.toFloat()) * 100f,
                color = othersColor
            )
        } else {
            topSegments
        }

        val filteredSegments = withOthers.filter { it.percentage > 0.1f }

        return UsageDonutData(
            formattedTotalTime = formatTotalTime(totalScreenTime),
            segments = filteredSegments
        )
    }

    /**
     * Format total time in milliseconds to human-readable string
     */
    private fun formatTotalTime(totalMs: Long): String {
        val totalMinutes = totalMs / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}

