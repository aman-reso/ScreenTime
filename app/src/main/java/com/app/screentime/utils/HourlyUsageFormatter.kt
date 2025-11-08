package com.app.screentime.utils

import org.joda.time.DateTime

/**
 * Utility class for formatting hourly usage data
 */
object HourlyUsageFormatter {
    
    /**
     * Formats usage time in milliseconds to a readable string
     * @param usageTimeMs Usage time in milliseconds
     * @return Formatted string (e.g., "2h 30m", "45m", "1h 5m")
     */
    fun formatUsageTime(usageTimeMs: Long): String {
        val minutes = usageTimeMs / (1000 * 60)
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return when {
            hours > 0 -> "${hours}h ${remainingMinutes}m"
            else -> "${minutes}m"
        }
    }
    
    /**
     * Formats hour to a readable time string
     * @param hour Hour (0-23)
     * @return Formatted time string (e.g., "12:00 AM", "2:00 PM")
     */
    fun formatHour(hour: Int): String {
        val now = DateTime.now()
        val dateTime = now.withTime(hour, 0, 0, 0)
        return dateTime.toString("h:mm a")
    }
    
    /**
     * Gets the hour range string for display
     * @param hour Hour (0-23)
     * @return Hour range string (e.g., "12:00 AM - 1:00 AM")
     */
    fun getHourRange(hour: Int): String {
        val now = DateTime.now()
        val startTime = now.withTime(hour, 0, 0, 0)
        val endTime = startTime.plusHours(1).minusMillis(1)
        
        return "${startTime.toString("h:mm a")} - ${endTime.toString("h:mm a")}"
    }
    
    /**
     * Gets the peak usage hour from hourly usage data
     * @param hourlyUsage Map of hour to usage time
     * @return Hour with maximum usage, or null if no usage
     */
    fun getPeakUsageHour(hourlyUsage: Map<Int, Long>): Int? {
        return hourlyUsage.maxByOrNull { it.value }?.key
    }
    
    /**
     * Gets the total usage time from hourly data
     * @param hourlyUsage Map of hour to usage time
     * @return Total usage time in milliseconds
     */
    fun getTotalUsageTime(hourlyUsage: Map<Int, Long>): Long {
        return hourlyUsage.values.sum()
    }
    
    /**
     * Gets usage statistics from hourly data
     * @param hourlyUsage Map of hour to usage time
     * @return UsageStats object with summary information
     */
    fun getUsageStats(hourlyUsage: Map<Int, Long>): UsageStats {
        val totalUsage = getTotalUsageTime(hourlyUsage)
        val peakHour = getPeakUsageHour(hourlyUsage)
        val activeHours = hourlyUsage.count { it.value > 0 }
        val averageUsagePerActiveHour = if (activeHours > 0) totalUsage / activeHours else 0L
        
        return UsageStats(
            totalUsage = totalUsage,
            peakHour = peakHour,
            activeHours = activeHours,
            averageUsagePerActiveHour = averageUsagePerActiveHour
        )
    }
}

/**
 * Data class for usage statistics
 */
data class UsageStats(
    val totalUsage: Long,
    val peakHour: Int?,
    val activeHours: Int,
    val averageUsagePerActiveHour: Long
)
