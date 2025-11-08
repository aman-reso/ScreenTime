package com.app.screentime.network.model

import com.app.screentime.record.repository.AppEvent
import com.app.screentime.utils.DeviceInfoUtils
import kotlinx.serialization.Serializable

/**
 * Base API response wrapper
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean? = null,
    val status: Int? = null,
    val data: T? = null,
    val message: String? = null,
    val timestamp: String? = null,
    val error: ApiError? = null
)

/**
 * API error model
 */
@Serializable
data class ApiError(
    val code: String? = null,
    val message: String? = null,
    val details: Map<String, String>? = null
)

/**
 * Screen time usage data for API
 */
@Serializable
data class ScreenTimeUsageRequest(
    val userId: String,
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
    val usageTime: Long,
    val lastUsed: Long? = null,
    val isSystemApp: Boolean = false,
    val category: String? = null
)

/**
 * Hourly usage data for API
 */
@Serializable
data class HourlyUsageRequest(
    val userId: String,
    val deviceId: String,
    val date: String, // YYYY-MM-DD format
    val hourlyData: Map<String, List<AppUsageData>> // hour -> list of app usages
)

/**
 * Sync data request
 */
@Serializable
data class SyncDataRequest(
    val userId: String,
    val deviceId: String,
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
    val userId: String,
    val deviceId: String,
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
 * Device registration request
 */
@Serializable
data class DeviceRegistrationRequest(
    val deviceInfo: DeviceInfoUtils.DeviceInfo
)

/**
 * Device registration response
 */
@Serializable
data class DeviceRegistrationResponse(
    val userId: String,
    val username: String,
    val createdAt: String,
    val totpSecret: String? = null,
    val totpEnabled: Boolean = false,
    val totpPeriod: Int = 60
) {
    // Convenience property for deviceId (if needed for backward compatibility)
    val deviceId: String get() = userId
}

/**
 * User search result
 */
@Serializable
data class UserSearchResult(
    val userId: String? = null,
    val username: String? = null,
    val email: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    val deviceId: String? = null,
    val createdAt: String? = null,
    val isActive: Boolean? = null
)

/**
 * Usage record response from API
 */
@Serializable
data class UsageRecordResponse(
    val id: Long? = null,
    val deviceId: String? = null,
    val username: String? = null,
    val appName: String? = null,
    val packageName: String? = null,
    val usageTimeMilliseconds: Long? = null,
    val usageTimeMinutes: Int? = null,
    val usageStart: String? = null,
    val usageEnd: String? = null,
    val isSystemApp: Boolean? = null,
    val date: String? = null,
    val createdAt: String? = null
)

/**
 * Consent item from API
 */
@Serializable
data class ApiConsentItem(
    val id: Int,
    val name: String,
    val description: String,
    val isMandatory: Boolean
)

/**
 * Consent submission item model
 */
@Serializable
data class ConsentSubmissionItem(
    val id: Int,
    val value: String // "accepted" or "rejected"
)

/**
 * Consent submission request model
 */
@Serializable
data class ConsentSubmissionRequest(
    val deviceId: String,
    val consents: List<ConsentSubmissionItem>
)

/**
 * Consent submission response item model
 */
@Serializable
data class ConsentSubmissionResponseItem(
    val id: Int,
    val deviceId: String? = null,
    val consentId: Int? = null,
    val consentName: String? = null,
    val value: String? = null,
    val submittedAt: String? = null
)

/**
 * Consent request model (legacy - kept for backward compatibility)
 */
@Serializable
data class ConsentRequest(
    val username: String,
    val hasConsent: Boolean,
    val dataSharing: Boolean,
    val analytics: Boolean,
    val marketing: Boolean
)

/**
 * Consent response model
 */
@Serializable
data class ConsentResponse(
    val username: String? = null,
    val hasConsent: Boolean? = null,
    val dataSharing: Boolean? = null,
    val analytics: Boolean? = null,
    val marketing: Boolean? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

/**
 * TOTP verify request model
 */
@Serializable
data class TOTPVerifyRequest(
    val secret: String,
    val code: String,
    val tolerance: Int = 1
)

/**
 * TOTP verify response model
 */
@Serializable
data class TOTPVerifyResponse(
    val valid: Boolean,
    val message: String? = null
)
