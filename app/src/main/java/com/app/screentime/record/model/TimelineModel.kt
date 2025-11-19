package com.app.screentime.record.model

import com.app.screentime.network.model.AppUsageStatsData

/**
 * UI Model for Timeline Screen
 * Contains flattened list items for displaying timeline data in LazyColumn
 */
sealed class TimelineListItem {
    /**
     * Hour range header item (e.g., "00:00-1:00")
     */
    data class HourHeaderItem(
        val hour: Int
    ) : TimelineListItem()

    /**
     * Timeline item for app usage event
     */
    data class TimelineEventItem(
        val stat: AppUsageStatsData,
        val isFirst: Boolean = false,
        val isLast: Boolean = false
    ) : TimelineListItem()
}

