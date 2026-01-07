package com.app.screentime.network.model

import com.app.screentime.record.repository.AppEvent
import com.app.screentime.core.network.utils.DeviceInfoUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Screen time usage data for API
 */
@Serializable
data class ScreenTimeUsageRequest(
    val appEvent: List<AppEvent>,
    val timestamp: Long,
    val timezone: String
)

/**
 * App usage data for API
 */
@Serializable
data class AppUsageData(
    val packageName: String? = null,
    val appName: String? = null,
    val usageTime: Long? = null,
    val lastUsed: Long? = null,
    val isSystemApp: Boolean = false,
    val category: String? = null
)

/**
 * App usage stats data from stats endpoint
 * This model matches the backend response structure with additional fields
 */
@Serializable
data class AppUsageStatsData(
    val packageName: String,
    val appName: String,
    val duration: Long? = null, // Session duration in milliseconds (if applicable)
    val isSystemApp: Boolean = false,
    val category: String? = null,
    val eventTimestamp: String? = null,
    val eventType: String? = null
)

/**
 * Usage stats response wrapper
 */
@Serializable
data class UsageStatsResponse(
    val stats: List<AppUsageStatsData>
)

/**
 * Hourly usage data for API
 */
@Serializable
data class HourlyUsageRequest(
    val date: String, // YYYY-MM-DD format
    val hourlyData: Map<String, List<AppUsageData>> // hour -> list of app usages
)

/**
 * Sync data request
 */
@Serializable
data class SyncDataRequest(
    val lastSyncTimestamp: Long,
    val data: ScreenTimeUsageRequest
)

/**
 * Analytics insights response
 */
@Serializable
data class AnalyticsInsights(
    val totalScreenTime: Long,
    val mostUsedApp: String,
    val averageSessionLength: Long,
    val peakUsageHour: Int,
    val weeklyTrend: List<DailyUsage>,
    val recommendations: List<String>
)

/**
 * Daily usage data
 */
@Serializable
data class DailyUsage(
    val date: String,
    val totalUsage: Long,
    val appCount: Int,
    val topApps: List<AppUsageData>
)

/**
 * Export data request
 */
@Serializable
data class ExportDataRequest(
    val startDate: String,
    val endDate: String,
    val format: String = "json" // json, csv, pdf
)

/**
 * Export data response
 */
@Serializable
data class ExportDataResponse(
    val downloadUrl: String,
    val expiresAt: Long,
    val fileSize: Long
)

/**
 * User profile data
 */
@Serializable
data class UserProfile(
    val userId: String,
    val email: String,
    val name: String,
    val avatar: String? = null,
    val preferences: UserPreferences
)

/**
 * User preferences
 */
@Serializable
data class UserPreferences(
    val dailyLimit: Long,
    val breakReminders: Boolean,
    val weeklyReports: Boolean,
    val dataSharing: Boolean,
    val timezone: String
)

/**
 * Notification settings
 */
@Serializable
data class NotificationSettings(
    val userId: String,
    val dailyLimitReached: Boolean,
    val breakReminders: Boolean,
    val weeklyReports: Boolean,
    val appBlocking: Boolean,
    val quietHours: QuietHours? = null
)

/**
 * Quiet hours configuration
 */
@Serializable
data class QuietHours(
    val enabled: Boolean,
    val startTime: String, // HH:MM format
    val endTime: String,   // HH:MM format
    val days: List<String> // ["monday", "tuesday", etc.]
)

/**
 * Username update request
 */
@Serializable
data class UsernameUpdateRequest(
    val username: String
)

/**
 * Profile update request
 */
@Serializable
data class ProfileUpdateRequest(
    val username: String? = null,
    val firebaseToken: String? = null
)

/**
 * User search result
 */
@Serializable
data class UserSearchResult(
    val username: String? = null,
    val email: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
    val isActive: Boolean? = null
)

/**
 * Usage record response from API
 */
@Serializable
data class UsageRecordResponse(
    val id: Long? = null,
    val appName: String? = null,
    val packageName: String? = null,
    val duration: Long? = null,
    val isSystemApp: Boolean? = null,
    val eventTimestamp: String? = null,
)

/**
 * User Search Request with TOTP
 */
@Serializable
data class UserSearchRequest(
    val username: String
)

/**
 * Username-based TOTP Verify Request
 */
@Serializable
data class UsernameTOTPVerifyRequest(
    val code: String // TOTP code to verify
)


/**
 * TOTP verify response model
 */
@Serializable
data class TOTPVerifyResponse(
    val valid: Boolean,
    val message: String? = null,
    val time: Int = 60 // Time period in seconds (default 60 seconds)
)

/**
 * App Usage Submission Request
 * For submitting app usage data (appname, package name, opened at, duration, isSystemApp, total screentime)
 */
@Serializable
data class AppUsageSubmissionRequest(
    val appName: String,
    val packageName: String,
    val openedAt: String, // ISO 8601 format
    val duration: Long, // milliseconds
    val isSystemApp: Boolean,
    val totalScreenTime: Long // milliseconds
)

/**
 * App Usage Submission Response
 */
@Serializable
data class AppUsageSubmissionResponse(
    val success: Boolean,
    val message: String? = null,
    val submittedAt: String? = null
)

/**
 * Get App Usage Request (with time frame)
 */
@Serializable
data class GetAppUsageRequest(
    val startDate: String, // ISO 8601 format or YYYY-MM-DD
    val endDate: String,   // ISO 8601 format or YYYY-MM-DD
    val includeSystemApps: Boolean = false
)

/**
 * Get App Usage Response
 */
@Serializable
data class GetAppUsageResponse(
    val totalScreenTime: Long, // milliseconds
    val appUsages: List<AppUsageData>,
    val timeFrame: String? = null
)

/**
 * Complete App History Request
 */
@Serializable
data class CompleteAppHistoryRequest(
    val page: Int = 1,
    val pageSize: Int = 50
)

/**
 * Complete App History Response
 */
@Serializable
data class CompleteAppHistoryResponse(
    val appUsages: List<AppUsageData>,
    val totalCount: Int,
    val page: Int,
    val pageSize: Int,
    val hasMore: Boolean
)

/**
 * Focus Duration Submission Request
 */
@Serializable
data class FocusDurationSubmissionRequest(
    val focusDuration: Long, // milliseconds
    val startTime: String, // ISO 8601 format
    val endTime: String,   // ISO 8601 format
    val sessionType: String? = null // e.g., "work", "study", "break"
)

/**
 * Focus Duration Submission Response
 */
@Serializable
data class FocusDurationSubmissionResponse(
    val success: Boolean,
    val message: String? = null,
    val submittedAt: String? = null
)

/**
 * Focus Duration History Request
 */
@Serializable
data class FocusDurationHistoryRequest(
    val startDate: String,
    val endDate: String
)

/**
 * Focus Duration History Response
 */
@Serializable
data class FocusDurationHistoryItem(
    val id: String? = null,
    val focusDuration: Long,
    val startTime: String,
    val endTime: String,
    val sessionType: String? = null,
    val createdAt: String? = null
)

/**
 * Focus Duration History Response
 */
@Serializable
data class FocusDurationHistoryResponse(
    val focusSessions: List<FocusDurationHistoryItem>,
    val totalFocusTime: Long,
    val averageSessionDuration: Long
)

/**
 * Focus Duration Stats Response
 */
@Serializable
data class FocusDurationStatsResponse(
    val totalFocusTime: Long,
    val todayFocusTime: Long,
    val weeklyFocusTime: Long,
    val monthlyFocusTime: Long,
    val averageSessionDuration: Long,
    val totalSessions: Int
)

/**
 * Focus Mode Stats Sync Request
 */
@Serializable
data class FocusModeStatsSyncRequest(
    val startTime: Long, // milliseconds since epoch
    val endTime: Long    // milliseconds since epoch
)

/**
 * Focus Mode Stats Sync Response
 */
@Serializable
data class FocusModeStatsSyncResponse(
    val success: Boolean,
    val message: String? = null,
    val syncedAt: String? = null
)

/**
 * Focus Mode Session Item
 */
@Serializable
data class FocusModeSessionItem(
    val id: String? = null,
    val startTime: Long, // milliseconds since epoch
    val endTime: Long,   // milliseconds since epoch
    val duration: Long,  // milliseconds
    val completed: Boolean = false,
    val createdAt: String? = null
)

/**
 * Focus Mode Stats Response (GET endpoint)
 */
@Serializable
data class FocusModeStatsResponse(
    val sessions: List<FocusModeSessionItem>,
    val totalSessions: Int = 0,
    val totalDuration: Long = 0L // Total duration in milliseconds
)

/**
 * Blocked Domain Group
 */
@Serializable
data class BlockedDomainGroup(
    val id: Int,
    val name: String, // e.g., "adults", "social media", "gaming"
    val description: String? = null,
    val domains: List<BlockedDomainItem>? = null
)

/**
 * Blocked Domain Item
 */
@Serializable
data class BlockedDomainItem(
    val id: Int? = null,
    val domain: String,
    val groupId: Int? = null,
    val groupName: String? = null,
    val isActive: Boolean = true,
    val createdAt: String? = null
)

/**
 * Get Blocked Domains Response
 */
@Serializable
data class GetBlockedDomainsResponse(
    val domains: List<BlockedDomainItem>,
    val groups: List<BlockedDomainGroup>
)

/**
 * Submit Blocked Domain Request
 */
@Serializable
data class SubmitBlockedDomainRequest(
    val domain: String,
    val groupId: Int? = null,
    val isActive: Boolean = true
)

/**
 * Submit Blocked Domain Response
 */
@Serializable
data class SubmitBlockedDomainResponse(
    val success: Boolean,
    val domain: BlockedDomainItem? = null,
    val message: String? = null
)

/**
 * URL Search Submission Request (VPN tracking)
 */
@Serializable
data class URLSearchSubmissionRequest(
    val url: String,
    val domain: String,
    val searchedAt: String, // ISO 8601 format
    val searchType: String? = null // e.g., "web", "app", "vpn"
)

/**
 * URL Search Submission Response
 */
@Serializable
data class URLSearchSubmissionResponse(
    val success: Boolean,
    val message: String? = null,
    val submittedAt: String? = null
)

/**
 * Batch URL Search Submission Request
 */
@Serializable
data class BatchURLSearchSubmissionRequest(
    val urlSearches: List<URLSearchSubmissionRequest>
)

/**
 * URL Search History Request
 */
@Serializable
data class URLSearchHistoryRequest(
    val startDate: String,
    val endDate: String,
    val domain: String? = null
)

/**
 * URL Search History Item
 */
@Serializable
data class URLSearchHistoryItem(
    val id: String? = null,
    val url: String,
    val domain: String,
    val searchedAt: String,
    val searchType: String? = null,
    val createdAt: String? = null
)

/**
 * URL Search History Response
 */
@Serializable
data class URLSearchHistoryResponse(
    val searches: List<URLSearchHistoryItem>,
    val totalCount: Int
)

/**
 * User Profile with Last Sync Time
 */
@Serializable
data class UserProfileWithSync(
    val userId: String,
    val username: String,
    val email: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    val lastSyncTime: Long? = null, // timestamp in milliseconds
    val preferences: UserPreferences? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

/**
 * Notification Data model
 * Note: Notifications are stored locally in database, not from API
 * This model exists for compatibility with NotificationService interface
 */
@Serializable
data class NotificationData(
    val id: String? = null,
    val title: String,
    val image: String? = null,
    val text: String,
    val deeplink: String? = null,
    val createdAt: String? = null,
    val isRead: Boolean = false
)

/**
 * Usage Event for batch submission
 */
@Serializable
data class UsageEvent(
    val packageName: String,
    val appName: String,
    val duration: Long? = null,
    val event: String,
    val startTime: Long? = null,
    val endTime: Long? = null
)

/**
 * Batch Usage Events Request
 */
@Serializable
data class BatchUsageEventsRequest(
    val syncTime: String, // ISO 8601 format
    val events: List<UsageEvent>
)

/**
 * Usage Last Sync Response
 */
@Serializable
data class UsageLastSyncResponse(
    val userId: String,
    val lastSyncTime: String? = null, // ISO 8601 format, null if never synced
    val hasEvents: Boolean = false
)
