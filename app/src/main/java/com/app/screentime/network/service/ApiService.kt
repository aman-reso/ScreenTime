package com.app.screentime.network.service

import com.app.screentime.network.model.*
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.utils.DeviceInfoUtils

/**
 * Main API service interface
 */
interface ApiService {

    // Screen Time API methods
    suspend fun syncUsageData(request: ScreenTimeUsageRequest): Result<ApiResponse<Unit>>
    suspend fun getUsageStats(
        userId: String,
        deviceId: String
    ): Result<ApiResponse<List<AppUsageData>>>

    suspend fun syncHourlyUsage(request: HourlyUsageRequest): Result<ApiResponse<Unit>>
    suspend fun getHourlyUsage(
        userId: String,
        deviceId: String,
        date: String
    ): Result<ApiResponse<Map<String, List<AppUsageData>>>>

    suspend fun exportData(request: ExportDataRequest): Result<ApiResponse<ExportDataResponse>>
    suspend fun sendBatchUsage(records: List<BatchUsageRecord>): Result<ApiResponse<Unit>>
    suspend fun getUsageRecordsByUsername(
        username: String,
        startDate: String,
        endDate: String
    ): Result<List<UsageRecordResponse>>

    suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>>
    suspend fun submitConsents(request: ConsentSubmissionRequest): Result<ApiResponse<List<ConsentSubmissionResponseItem>>>
    suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse>
    suspend fun getConsentStatus(username: String): Result<ConsentResponse>

    // Analytics API methods
    suspend fun getAnalyticsInsights(
        userId: String,
        deviceId: String
    ): Result<ApiResponse<AnalyticsInsights>>

    suspend fun getUsageTrends(
        userId: String,
        deviceId: String,
        days: Int
    ): Result<ApiResponse<List<DailyUsage>>>

    suspend fun generateReport(
        userId: String,
        deviceId: String,
        reportType: String
    ): Result<ApiResponse<String>>

    // User API methods
    suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo): Result<ApiResponse<DeviceRegistrationResponse>>
    suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>>
    suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>>
    suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>>
    suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<ApiResponse<UserPreferences>>

    suspend fun searchUsers(username: String): Result<ApiResponse<List<UserSearchResult>>>

    // Notification API methods
    suspend fun sendNotification(
        userId: String,
        message: String,
        type: String
    ): Result<ApiResponse<Unit>>

    suspend fun getNotificationHistory(userId: String): Result<ApiResponse<List<NotificationData>>>
    suspend fun updateNotificationSettings(settings: NotificationSettings): Result<ApiResponse<NotificationSettings>>

    // TOTP API methods
    suspend fun verifyTOTP(request: TOTPVerifyRequest): Result<ApiResponse<TOTPVerifyResponse>>
}

/**
 * Notification data model
 */
@kotlinx.serialization.Serializable
data class NotificationData(
    val id: String,
    val userId: String,
    val message: String,
    val type: String,
    val timestamp: Long,
    val read: Boolean
)
