package com.app.screentime.network.service

import com.app.screentime.network.ApiEndpoints
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.model.*
import com.app.screentime.network.model.DeviceRegistrationRequest
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.utils.DeviceInfoUtils
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.Uuid

/**
 * Implementation of ApiService using Ktor
 */
@Singleton
class ApiServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : ApiService {

    private val httpClient = networkClient.httpClient

    override suspend fun syncUsageData(request: ScreenTimeUsageRequest): Result<ApiResponse<Unit>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.ScreenTime.SYNC_DATA) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to sync usage data: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsageStats(
        userId: String,
        deviceId: String
    ): Result<ApiResponse<List<AppUsageData>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.ScreenTime.USAGE_STATS) {
                parameter("userId", userId)
                parameter("deviceId", deviceId)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<AppUsageData>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get usage stats: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncHourlyUsage(request: HourlyUsageRequest): Result<ApiResponse<Unit>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.ScreenTime.HOURLY_USAGE) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to sync hourly usage: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHourlyUsage(
        userId: String,
        deviceId: String,
        date: String
    ): Result<ApiResponse<Map<String, List<AppUsageData>>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.ScreenTime.HOURLY_USAGE) {
                parameter("userId", userId)
                parameter("deviceId", deviceId)
                parameter("date", date)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Map<String, List<AppUsageData>>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get hourly usage: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun exportData(request: ExportDataRequest): Result<ApiResponse<ExportDataResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.ScreenTime.EXPORT_DATA) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<ExportDataResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to export data: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAnalyticsInsights(
        userId: String,
        deviceId: String
    ): Result<ApiResponse<AnalyticsInsights>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Analytics.INSIGHTS) {
                parameter("userId", userId)
                parameter("deviceId", deviceId)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<AnalyticsInsights> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get analytics insights: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsageTrends(
        userId: String,
        deviceId: String,
        days: Int
    ): Result<ApiResponse<List<DailyUsage>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Analytics.TRENDS) {
                parameter("userId", userId)
                parameter("deviceId", deviceId)
                parameter("days", days)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<DailyUsage>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get usage trends: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateReport(
        userId: String,
        deviceId: String,
        reportType: String
    ): Result<ApiResponse<String>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Analytics.REPORTS) {
                parameter("userId", userId)
                parameter("deviceId", deviceId)
                parameter("reportType", reportType)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<String> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to generate report: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo): Result<ApiResponse<DeviceRegistrationResponse>> {
        return try {
            val request = DeviceRegistrationRequest(deviceInfo = deviceInfo)

            val apiResponse: ApiResponse<DeviceRegistrationResponse> =
                httpClient.post(ApiEndpoints.User.REGISTER_DEVICE) {
                    setBody(request)
                }.body() // reified type ensures proper deserialization
            println("Device registered successfully: $apiResponse")

            Result.success(apiResponse)
        } catch (e: ClientRequestException) {
            // 4xx errors
            val errorBody = e.response.bodyAsText()
            println("Client error: ${e.response.status}, body: $errorBody")
            Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
        } catch (e: ServerResponseException) {
            // 5xx errors
            val errorBody = e.response.bodyAsText()
            println("Server error: ${e.response.status}, body: $errorBody")
            Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
        } catch (e: Exception) {
            // Other errors (network, serialization, etc.)
            println("Error registering device: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiEndpoints.User.PROFILE}/$userId")

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserProfile> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get user profile: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>> {
        return try {
            val response: HttpResponse = httpClient.put(ApiEndpoints.User.PROFILE) {
                setBody(profile)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserProfile> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update user profile: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiEndpoints.User.PREFERENCES}/$userId")

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserPreferences> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get user preferences: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<ApiResponse<UserPreferences>> {
        return try {
            val response: HttpResponse =
                httpClient.put("${ApiEndpoints.User.PREFERENCES}/$userId") {
                    setBody(preferences)
                }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserPreferences> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update user preferences: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendNotification(
        userId: String,
        message: String,
        type: String
    ): Result<ApiResponse<Unit>> {
        return try {
            val request = mapOf(
                "userId" to userId,
                "message" to message,
                "type" to type
            )

            val response: HttpResponse = httpClient.post(ApiEndpoints.Notifications.SEND) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to send notification: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotificationHistory(userId: String): Result<ApiResponse<List<NotificationData>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Notifications.HISTORY) {
                parameter("userId", userId)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<NotificationData>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get notification history: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings): Result<ApiResponse<NotificationSettings>> {
        return try {
            val response: HttpResponse = httpClient.put(ApiEndpoints.Notifications.SETTINGS) {
                setBody(settings)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<NotificationSettings> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update notification settings: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(username: String): Result<ApiResponse<List<UserSearchResult>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.User.SEARCH) {
                parameter("username", username)
            }

            if (response.status.isSuccess()) {
                // API returns array directly, not wrapped in ApiResponse
                try {
                    val users: List<UserSearchResult> = response.body()
                    // Wrap it in ApiResponse
                    val apiResponse = ApiResponse(
                        success = true,
                        data = users,
                        message = null,
                        error = null
                    )
                    Result.success(apiResponse)
                } catch (e: Exception) {
                    // If that fails, try to deserialize as ApiResponse
                    val apiResponse: ApiResponse<List<UserSearchResult>> = response.body()
                    Result.success(apiResponse)
                }
            } else {
                Result.failure(Exception("Failed to search users: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendBatchUsage(records: List<BatchUsageRecord>): Result<ApiResponse<Unit>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.ScreenTime.BATCH_USAGE) {
                contentType(ContentType.Application.Json)
                setBody(records)
            }

            if (response.status.isSuccess()) {
                // Try to deserialize as ApiResponse<Unit>
                try {
                    val apiResponse: ApiResponse<Unit> = response.body()
                    Result.success(apiResponse)
                } catch (e: Exception) {
                    // If deserialization fails, create a success response
                    Result.success(ApiResponse(success = true))
                }
            } else {
                Result.failure(Exception("Failed to send batch usage: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsageRecordsByUsername(
        username: String,
        startDate: String,
        endDate: String
    ): Result<List<UsageRecordResponse>> {
        return try {
            val url = "${ApiEndpoints.ScreenTime.USER_USAGE}/$username"
            val response: HttpResponse = httpClient.get(url) {
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }

            if (response.status.isSuccess()) {
                // API returns array directly
                val records: List<UsageRecordResponse> = response.body()
                Result.success(records)
            } else {
                Result.failure(Exception("Failed to get usage records: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Consent.GET_CONSENTS)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<ApiConsentItem>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get consents: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitConsents(request: ConsentSubmissionRequest): Result<ApiResponse<Unit>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Consent.SUBMIT) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Failed to submit consents: ${response.status} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Consent.SUBMIT) {
                contentType(ContentType.Application.Json)
                setBody(consentRequest)
            }

            if (response.status.isSuccess()) {
                val consentResponse: ConsentResponse = response.body()
                Result.success(consentResponse)
            } else {
                Result.failure(Exception("Failed to submit consent: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConsentStatus(username: String): Result<ConsentResponse> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Consent.STATUS) {
                parameter("username", username)
            }

            if (response.status.isSuccess()) {
                val consentResponse: ConsentResponse = response.body()
                Result.success(consentResponse)
            } else {
                Result.failure(Exception("Failed to get consent status: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyTOTP(request: TOTPVerifyRequest): Result<ApiResponse<TOTPVerifyResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.TOTP.VERIFY) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<TOTPVerifyResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to verify TOTP: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
