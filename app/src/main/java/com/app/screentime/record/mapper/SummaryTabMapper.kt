package com.app.screentime.record.mapper

import android.content.Context
import android.content.pm.PackageManager
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.landing.model.UsageDonutData
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.record.model.SummaryTabUiProps
import com.app.screentime.record.util.formatUsageTime
import com.telekom.odsystem.foundations.HexColor
import javax.inject.Inject

/**
 * Mapper that converts use case results to UI Props for Summary Tab
 */
class SummaryTabMapper @Inject constructor() {

    /**
     * Map stats to SummaryTab UI Props
     * This contains the business logic for grouping, summing, and creating AppUsage objects
     */
    fun toUiProps(
        stats: List<AppUsageStatsData>,
        context: Context,
        chartColors: List<HexColor>,
        isLoading: Boolean = false,
        error: String? = null
    ): SummaryTabUiProps {
        if (stats.isEmpty()) {
            return SummaryTabUiProps(
                appUsageList = emptyList(),
                totalScreenTime = 0L,
                usageDonutData = null,
                isLoading = isLoading,
                error = error
            )
        }

        // Group by package and sum duration (duration is in milliseconds)
        val appUsageMap = stats.groupBy { it.packageName }.mapValues { (packageName, packageStats) ->
            // Sum all durations for this package
            val totalDurationMs = packageStats.sumOf { it.duration ?: 0L }
            val firstStat = packageStats.first()
            
            val appInfo = try {
                context.packageManager.getApplicationInfo(packageName, 0)
            } catch (e: Exception) {
                null
            }
            
            AppUsage(
                packageName = packageName,
                appName = firstStat.appName,
                appScreenTime = totalDurationMs,
                mobileDataUsage = -1L,
                wifiDataUsage = -1L
            ).apply {
                applicationInfo = appInfo
                displayFormatScreenTime = formatUsageTime(totalDurationMs)
            }
        }

        val appUsageList = appUsageMap.values.sortedByDescending { it.appScreenTime }

        // Total time: sum of all durations
        val totalScreenTime = stats.sumOf { it.duration ?: 0L }

        // Create donut data
        val usageDonutData = if (appUsageList.isNotEmpty()) {
            createUsageDonutData(
                apps = appUsageList,
                totalScreenTime = totalScreenTime,
                chartColors = chartColors
            )
        } else {
            null
        }

        return SummaryTabUiProps(
            appUsageList = appUsageList,
            totalScreenTime = totalScreenTime,
            usageDonutData = usageDonutData,
            isLoading = isLoading,
            error = error
        )
    }

    /**
     * Create usage donut data from app usage list
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
            com.app.screentime.landing.model.UsageSegment(
                name = app.appName ?: app.packageName.orEmpty(),
                percentage = percent,
                color = chartColors[index % chartColors.size]
            )
        }

        // Use muted color from palette for "Others"
        val othersColor =
            if (chartColors.size > 4) chartColors[4] else chartColors.lastOrNull() ?: HexColor(0xFF9E9E9E)
        val withOthers = if (othersTime > 0) {
            topSegments + com.app.screentime.landing.model.UsageSegment(
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

