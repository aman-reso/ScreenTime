package com.app.screentime.battery.model

/**
 * Battery usage information for a specific app
 */
data class AppBatteryUsage(
    val packageName: String,
    val appName: String,
    val usageTimeMs: Long, // Screen time in milliseconds
    val estimatedBatteryPercent: Float, // Estimated battery percentage consumed
    val batteryConsumption: Float // Estimated battery consumption in mAh
)






















