package com.app.screentime.record.util

/**
 * Format usage time in milliseconds to a human-readable string
 * @param ms Duration in milliseconds
 * @return Formatted string like "2h 30m" or "45m"
 */
fun formatUsageTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}

