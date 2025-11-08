package com.app.screentime.landing.component

import com.app.screentime.data.entity.AppUsage

/**
 * Format usage time in milliseconds to human readable format
 */
fun formatUsageTime(usageTimeMs: Long): String {
    val totalMinutes = usageTimeMs / (1000 * 60)
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return when {
        hours > 0 -> "$hours jam $minutes menit"
        else -> "$minutes menit"
    }
}

/**
 * Calculate percentage of usage time relative to total daily time
 */
fun calculateUsagePercentage(usageTimeMs: Long, totalDailyTimeMs: Long): Int {
    if (totalDailyTimeMs <= 0) return 0
    return ((usageTimeMs.toFloat() / totalDailyTimeMs.toFloat()) * 100).toInt()
}

/**
 * Format last used timestamp to relative time
 */
fun formatLastUsed(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val diffMs = currentTime - timestamp
    val diffMinutes = diffMs / (1000 * 60)
    val diffHours = diffMinutes / 60
    val diffDays = diffHours / 24

    return when {
        diffDays > 0 -> "${diffDays} hari yang lalu"
        diffHours > 0 -> "${diffHours} jam yang lalu"
        diffMinutes > 0 -> "${diffMinutes} menit yang lalu"
        else -> "Baru saja"
    }
}

/**
 * Get app icon color based on package name
 */
fun getAppIconColor(packageName: String): String {
    return when {
        packageName.contains("youtube") -> "YouTube"
        packageName.contains("instagram") -> "Instagram"
        packageName.contains("facebook") -> "Facebook"
        packageName.contains("whatsapp") -> "WhatsApp"
        packageName.contains("twitter") -> "Twitter"
        packageName.contains("tiktok") -> "TikTok"
        else -> packageName.split(".").lastOrNull()?.replaceFirstChar { it.uppercase() } ?: "App"
    }
}
