package com.app.screentime.record.usecase

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.network.model.BatchUsageRecord
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.record.repository.RecordRepository
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for recording and retrieving usage data
 */
class RecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
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
                    val instant = Instant.parse(timestamp)
                    val zonedDateTime = instant.atZone(ZoneId.systemDefault())
                    zonedDateTime.hour
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
                        try {
                            Instant.parse(it).toEpochMilli()
                        } catch (e: Exception) {
                            0L
                        }
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
}
