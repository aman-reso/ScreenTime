package com.app.screentime.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat

/**
 * Utility class for date and time operations using JodaTime
 * All operations use IST (Asia/Kolkata) timezone
 */
object DateUtils {
    
    private val IST_ZONE: DateTimeZone = DateTimeZone.forID("Asia/Kolkata")
    
    /**
     * Get current DateTime in IST timezone
     */
    fun now(): DateTime {
        return DateTime.now(IST_ZONE)
    }
    
    /**
     * Get current LocalDate in IST timezone
     */
    fun today(): LocalDate {
        return LocalDate.now(IST_ZONE)
    }
    
    /**
     * Parse ISO 8601 date string to DateTime in IST timezone
     */
    fun parseISO8601(isoString: String): DateTime {
        return ISODateTimeFormat.dateTimeParser().parseDateTime(isoString).withZone(IST_ZONE)
    }
    
    /**
     * Format DateTime to ISO 8601 string
     */
    fun formatISO8601(dateTime: DateTime): String {
        return ISODateTimeFormat.dateTime().print(dateTime)
    }
    
    /**
     * Format DateTime to custom pattern string
     * @param dateTime DateTime to format
     * @param pattern Pattern string (e.g., "MMM dd, yyyy", "HH:mm")
     */
    fun format(dateTime: DateTime, pattern: String): String {
        return DateTimeFormat.forPattern(pattern).print(dateTime)
    }
    
    /**
     * Format ISO 8601 string to custom pattern string
     * @param isoString ISO 8601 date string
     * @param pattern Pattern string (e.g., "MMM dd, yyyy", "HH:mm")
     */
    fun format(isoString: String, pattern: String): String {
        return try {
            val dateTime = parseISO8601(isoString)
            format(dateTime, pattern)
        } catch (e: Exception) {
            isoString // Return original string if parsing fails
        }
    }
    
    /**
     * Format date to "MMM dd, yyyy" pattern (e.g., "Jan 15, 2025")
     */
    fun formatDate(isoString: String): String {
        return format(isoString, "MMM dd, yyyy")
    }
    
    /**
     * Format time to "HH:mm" pattern (e.g., "14:30")
     */
    fun formatTime(isoString: String): String {
        return format(isoString, "HH:mm")
    }
    
    /**
     * Format date and time to "MMM dd, HH:mm" pattern (e.g., "Jan 15, 14:30")
     */
    fun formatDateTime(isoString: String): String {
        return format(isoString, "MMM dd, HH:mm")
    }
    
    /**
     * Convert ISO 8601 string to milliseconds
     */
    fun toMillis(isoString: String): Long {
        return try {
            parseISO8601(isoString).millis
        } catch (e: Exception) {
            0L
        }
    }
    
    /**
     * Convert milliseconds to DateTime in IST timezone
     */
    fun fromMillis(millis: Long): DateTime {
        return DateTime(millis, IST_ZONE)
    }
    
    /**
     * Calculate duration between two ISO 8601 date strings
     * @return Duration object
     */
    fun durationBetween(startIso: String, endIso: String): Duration {
        val start = parseISO8601(startIso)
        val end = parseISO8601(endIso)
        return Duration(start, end)
    }
    
    /**
     * Calculate duration in days between two ISO 8601 date strings
     */
    fun daysBetween(startIso: String, endIso: String): Long {
        return durationBetween(startIso, endIso).standardDays
    }
    
    /**
     * Calculate duration in hours between two ISO 8601 date strings
     */
    fun hoursBetween(startIso: String, endIso: String): Long {
        return durationBetween(startIso, endIso).standardHours
    }
    
    /**
     * Calculate duration in minutes between two ISO 8601 date strings
     */
    fun minutesBetween(startIso: String, endIso: String): Long {
        return durationBetween(startIso, endIso).standardMinutes
    }
    
    /**
     * Check if a date (ISO 8601 string) is after current time
     */
    fun isAfter(isoString: String): Boolean {
        return try {
            val dateTime = parseISO8601(isoString)
            now().isAfter(dateTime)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Check if a date (ISO 8601 string) is before current time
     */
    fun isBefore(isoString: String): Boolean {
        return try {
            val dateTime = parseISO8601(isoString)
            now().isBefore(dateTime)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Check if first date is after second date
     */
    fun isAfter(firstIso: String, secondIso: String): Boolean {
        return try {
            val first = parseISO8601(firstIso)
            val second = parseISO8601(secondIso)
            first.isAfter(second)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get start of day (midnight) for a given DateTime
     */
    fun startOfDay(dateTime: DateTime): DateTime {
        return dateTime.withTimeAtStartOfDay()
    }
    
    /**
     * Get start of day (midnight) for current date in IST
     */
    fun startOfToday(): DateTime {
        return now().withTimeAtStartOfDay()
    }
    
    /**
     * Get start of day (midnight) for a given ISO 8601 string
     */
    fun startOfDay(isoString: String): DateTime {
        return try {
            parseISO8601(isoString).withTimeAtStartOfDay()
        } catch (e: Exception) {
            startOfToday()
        }
    }
    
    /**
     * Add days to a DateTime
     */
    fun addDays(dateTime: DateTime, days: Int): DateTime {
        return dateTime.plusDays(days)
    }
    
    /**
     * Subtract days from a DateTime
     */
    fun minusDays(dateTime: DateTime, days: Int): DateTime {
        return dateTime.minusDays(days)
    }
    
    /**
     * Get future date by adding days to current date
     */
    fun futureDate(daysFromNow: Int): String {
        val future = now().plusDays(daysFromNow)
        return formatISO8601(future)
    }
    
    /**
     * Get past date by subtracting days from current date
     */
    fun pastDate(daysAgo: Int): String {
        val past = now().minusDays(daysAgo)
        return formatISO8601(past)
    }
    
    /**
     * Format duration to human-readable string
     * @param durationMs Duration in milliseconds
     * @return Formatted string (e.g., "2h 30m", "45m", "1d 5h")
     */
    fun formatDuration(durationMs: Long): String {
        val duration = Duration(durationMs)
        val days = duration.standardDays
        val hours = duration.standardHours % 24
        val minutes = duration.standardMinutes % 60
        
        return when {
            days > 0 -> {
                when {
                    hours > 0 -> "${days}d ${hours}h"
                    minutes > 0 -> "${days}d ${minutes}m"
                    else -> "${days}d"
                }
            }
            hours > 0 -> {
                when {
                    minutes > 0 -> "${hours}h ${minutes}m"
                    else -> "${hours}h"
                }
            }
            else -> "${minutes}m"
        }
    }
    
    /**
     * Format challenge duration between start and end dates
     * @return Formatted string (e.g., "14 Days", "1 Day", "5 Hours", "30 Minutes")
     */
    fun formatChallengeDuration(startIso: String, endIso: String): String {
        return try {
            val days = daysBetween(startIso, endIso)
            when {
                days == 1L -> "1 Day"
                days > 0 -> "$days Days"
                else -> {
                    val hours = hoursBetween(startIso, endIso)
                    when {
                        hours == 1L -> "1 Hour"
                        hours > 0 -> "$hours Hours"
                        else -> {
                            val minutes = minutesBetween(startIso, endIso)
                            "$minutes Minutes"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            "N/A"
        }
    }
}

