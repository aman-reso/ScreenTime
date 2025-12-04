package com.app.screentime.landing.model

import com.telekom.odsystem.foundations.HexColor

/**
 * Data model for usage donut chart
 * Contains pre-calculated data ready for display
 */
data class UsageDonutData(
    val formattedTotalTime: String,
    val segments: List<UsageSegment>
)

/**
 * Data for a single segment in the donut chart
 */
data class UsageSegment(
    val name: String,
    val percentage: Float,
    val color: HexColor
)

