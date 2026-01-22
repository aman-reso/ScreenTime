package com.app.screentime.challenge.component.util

import java.time.Duration
import java.time.Instant

/**
 * Parses an ISO 8601 challenge timestamp to [Instant]. Trims fractional seconds to 1–9 digits
 * to avoid parse errors. Returns null on failure.
 */
fun parseChallengeInstant(isoDateString: String): Instant? {
    if (isoDateString.isBlank()) return null
    return try {
        val normalized = Regex("""(\.\d{1,9})\d*Z$""").replace(isoDateString) { it.groupValues[1] + "Z" }
        Instant.parse(normalized)
    } catch (e: Exception) {
        null
    }
}

/**
 * Formats an ISO 8601 date string to a readable date (e.g. "26 Jan 2026").
 *
 * Supports the challenge API format, including fractional seconds and Z (UTC), e.g.:
 * - "2026-01-17T16:40:10.810250Z"
 * - "2026-01-31T16:40:10.810Z"
 *
 * The result uses the device's default timezone for the calendar date.
 *
 * @param isoDateString ISO 8601 date string (e.g. from startTime/endTime)
 * @return Formatted date string (e.g., "17 Jan 2026") or the input on parse error
 */
fun formatDate(isoDateString: String): String {
    if (isoDateString.isBlank()) return isoDateString
    return try {
        val instant = parseChallengeInstant(isoDateString) ?: return isoDateString
        val dateTime = instant.atZone(java.time.ZoneId.systemDefault())
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy", java.util.Locale.ENGLISH)
        dateTime.format(formatter)
    } catch (e: Exception) {
        isoDateString
    }
}

/** Used to select the countdown label (Starts in / Ends in). */
enum class CountdownLabel { STARTS_IN, ENDS_IN }

private val TWENTY_FOUR_HOURS = Duration.ofHours(24)

/**
 * Returns countdown target and label when the challenge is within the 24h countdown window:
 * - [CountdownLabel.STARTS_IN] when now &lt; start and (start - now) ≤ 24h
 * - [CountdownLabel.ENDS_IN] when start ≤ now &lt; end and (end - now) ≤ 24h
 * Returns null otherwise.
 */
fun getCountdownInfo(startTime: String, endTime: String): Pair<CountdownLabel, Instant>? {
    val start = parseChallengeInstant(startTime) ?: return null
    val end = parseChallengeInstant(endTime) ?: return null
    val now = Instant.now()
    if (now < start) {
        val d = Duration.between(now, start)
        if (d <= TWENTY_FOUR_HOURS) return CountdownLabel.STARTS_IN to start
    } else if (now < end) {
        val d = Duration.between(now, end)
        if (d <= TWENTY_FOUR_HOURS) return CountdownLabel.ENDS_IN to end
    }
    return null
}

/**
 * Formats a countdown string like "Starts in 23:45:12" or "Ends in 00:05:30".
 *
 * @param target target [Instant]
 * @param label e.g. "Starts in" or "Ends in" (already localized)
 */
fun formatCountdown(target: Instant, label: String): String {
    val d = Duration.between(Instant.now(), target)
    if (d.isNegative || d.isZero) return "$label 00:00:00"
    val hours = d.toHours()
    val minutes = (d.toMinutes() % 60).toInt()
    val seconds = (d.getSeconds() % 60).toInt()
    return "$label %02d:%02d:%02d".format(hours, minutes, seconds)
}

