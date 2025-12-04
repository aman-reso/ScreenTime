package com.app.screentime.record.usecase

import android.content.Context
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.network.model.BatchUsageRecord
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.record.mapper.SummaryTabMapper
import com.app.screentime.record.model.SummaryTabUiProps
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.record.repository.RecordRepository
import com.app.screentime.utils.DateUtils
import com.telekom.odsystem.foundations.HexColor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Use case for recording and retrieving usage data
 */
class RecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
    private val summaryTabMapper: SummaryTabMapper,
    @ApplicationContext private val context: Context
) {


    suspend fun getDailyUsageStats(
        date: String,
        targetUserId: String
    ): Result<com.app.screentime.network.model.DailyUsage> {
        return recordRepository.getDailyUsageStats(date, targetUserId)
    }

    suspend fun getRawDailyStats(
        date: String,
        targetUserId: String
    ): Result<List<AppUsageStatsData>> {
        return recordRepository.getRawDailyStats(date, targetUserId)
    }

    /**
     * Process timeline data by grouping stats by hour and creating a flattened list
     * @param stats List of app usage stats
     * @return List of timeline items (headers and events) sorted by hour (descending)
     */
    fun processTimelineData(stats: List<AppUsageStatsData>): List<TimelineListItem> {
        // Group stats by hour
        val statsByHour = stats.groupBy { stat ->
            stat.eventTimestamp?.let { timestamp ->
                try {
                    val dateTime = DateUtils.parseISO8601(timestamp)
                    dateTime.hourOfDay
                } catch (e: Exception) {
                    null
                }
            } ?: -1
        }.toSortedMap(compareByDescending { it })

        val items = mutableListOf<TimelineListItem>()
        
        statsByHour.forEach { (hour, hourStats) ->
            if (hour >= 0) {
                // Add hour header
                items.add(TimelineListItem.HourHeaderItem(hour = hour))

                // Sort stats for this hour by timestamp (newest first)
                val sortedStats = hourStats.sortedByDescending { stat ->
                    stat.eventTimestamp?.let {
                        DateUtils.toMillis(it)
                    } ?: 0L
                }

                // Add timeline event items
                sortedStats.forEachIndexed { index, stat ->
                    items.add(
                        TimelineListItem.TimelineEventItem(
                            stat = stat,
                            isFirst = index == 0,
                            isLast = index == sortedStats.size - 1
                        )
                    )
                }
            }
        }
        
        return items
    }

    /**
     * Get Summary Tab UI Props
     * This is the main method that returns all UI state needed for the summary tab
     */
    suspend fun getSummaryTabUiProps(
        stats: List<AppUsageStatsData>,
        isLoading: Boolean = false,
        error: String? = null
    ): SummaryTabUiProps {
        // Default chart colors
        val chartColors = listOf(
            HexColor(0xFF0070CC), // basicAccent
            HexColor(0xFF00A651), // functionalSuccessStandard
            HexColor(0xFF0070CC), // functionalInformationalStandard
            HexColor(0xFFFFB300), // functionalWarningStandard
            HexColor(0xFF0070CC)  // basicAccent
        )

        return summaryTabMapper.toUiProps(
            stats = stats,
            context = context,
            chartColors = chartColors,
            isLoading = isLoading,
            error = error
        )
    }
}
