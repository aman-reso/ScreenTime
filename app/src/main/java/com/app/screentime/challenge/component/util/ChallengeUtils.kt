package com.app.screentime.challenge.component.util

/**
 * Formats an ISO date string to a readable format.
 *
 * @param isoDateString ISO 8601 date string
 * @return Formatted date string (e.g., "Jan 15, 2024")
 */
fun formatDate(isoDateString: String): String {
    return try {
        val instant = java.time.Instant.parse(isoDateString)
        val dateTime = instant.atZone(java.time.ZoneId.systemDefault())
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")
        dateTime.format(formatter)
    } catch (e: Exception) {
        isoDateString
    }
}

