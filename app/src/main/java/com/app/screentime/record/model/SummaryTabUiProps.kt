package com.app.screentime.record.model

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.landing.model.UsageDonutData

/**
 * UI Props for Summary Tab
 * Contains all the data needed to render the summary tab
 */
data class SummaryTabUiProps(
    val appUsageList: List<AppUsage>,
    val totalScreenTime: Long,
    val isLoading: Boolean = false,
    val error: String? = null
)

