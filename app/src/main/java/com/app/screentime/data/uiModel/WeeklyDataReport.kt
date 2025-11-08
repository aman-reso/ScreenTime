package com.app.screentime.data.uiModel

import com.app.screentime.data.entity.AppUsage

data class WeeklyDataReport(
    val dayName: String? = null,
    val date: String? = null,
    val appUsage: List<AppUsage>? = null,
    val totalScreenTime: Long? = null,
    val displayScreenTime: String? = null,
    val totalWifiDataUsage: Long? = null,
    val totalMobileDataUsage: Long? = null,
    val displayWifiDataUsage: String? = null,
    val displayMobileDataUsage: String? = null,
    val displayTotalDataUsage: String? = null
)