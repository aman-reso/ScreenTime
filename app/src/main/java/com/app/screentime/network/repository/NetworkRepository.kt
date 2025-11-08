package com.app.screentime.network.repository

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.network.model.*
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.network.service.ApiService
import com.app.screentime.network.model.UserSearchResult
import com.app.screentime.network.model.BatchUsageRecord
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.network.model.ConsentRequest
import com.app.screentime.network.model.ConsentResponse
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.record.repository.AppEvent
import com.app.screentime.utils.DeviceInfoUtils
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for network operations
 */
@Singleton
class NetworkRepository @Inject constructor(
    private val apiService: ApiService,
    private val deviceInfoUtils: DeviceInfoUtils,
    private val preferencesManager: PreferencesManager
) {

    /**
     * Register device and save complete response to SharedPreferences on success
     */
    suspend fun registerDevice(): Result<DeviceRegistrationResponse> {
        return try {
            val deviceInfo = deviceInfoUtils.getDeviceInfo()
            val result = apiService.registerDevice(deviceInfo)

            result.map { apiResponse ->
                // Extract the data from the wrapped response
                val registrationData = apiResponse.data
                if (registrationData != null && apiResponse.success == true) {
                    // Save the complete response data to SharedPreferences
                    saveRegistrationData(registrationData)
                    registrationData
                } else {
                    throw Exception(apiResponse.message ?: "Registration failed")
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveRegistrationData(deviceRegistrationResponse: DeviceRegistrationResponse) {
        preferencesManager.putString(
            "user_reg_info",
            Json.encodeToString(deviceRegistrationResponse)
        )
    }


    /**
     * Sync app usage data to server
     */
    suspend fun syncAppUsageData(
        userId: String,
        appEvent: List<AppEvent>,
        timezone: String = "UTC"
    ): Result<ApiResponse<Unit>> {
        return try {
            val request = ScreenTimeUsageRequest(
                userId = userId,
                appEvent = appEvent,
                timestamp = System.currentTimeMillis(),
                timezone = timezone
            )

            apiService.syncUsageData(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sync hourly usage data to server
     */
    suspend fun syncHourlyUsageData(
        userId: String,
        deviceId: String,
        hourlyAppUsage: Map<Int, List<AppUsage>>,
        date: String
    ): Result<ApiResponse<Unit>> {
        return try {
            val hourlyData = hourlyAppUsage.mapKeys { it.key.toString() }
                .mapValues { (_, appUsages) ->
                    appUsages.map { appUsage ->
                        AppUsageData(
                            packageName = appUsage.packageName,
                            appName = appUsage.appName,
                            usageTime = appUsage.appScreenTime,
                        )
                    }
                }

            val request = HourlyUsageRequest(
                userId = userId,
                deviceId = deviceId,
                date = date,
                hourlyData = hourlyData
            )

            apiService.syncHourlyUsage(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Export user data
     */
    suspend fun exportUserData(
        userId: String,
        deviceId: String,
        startDate: String,
        endDate: String,
        format: String = "json"
    ): Result<ApiResponse<ExportDataResponse>> {
        return try {
            val request = ExportDataRequest(
                userId = userId,
                deviceId = deviceId,
                startDate = startDate,
                endDate = endDate,
                format = format
            )

            apiService.exportData(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get user profile
     */
    suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>> {
        return try {
            apiService.getUserProfile(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user profile
     */
    suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>> {
        return try {
            apiService.updateUserProfile(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get user preferences
     */
    suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>> {
        return try {
            apiService.getUserPreferences(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user preferences
     */
    suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<ApiResponse<UserPreferences>> {
        return try {
            apiService.updateUserPreferences(userId, preferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Search users by username
     */
    suspend fun searchUsers(username: String): Result<ApiResponse<List<UserSearchResult>>> {
        return try {
            apiService.searchUsers(username)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Send batch usage records
     */
    suspend fun sendBatchUsage(records: List<BatchUsageRecord>): Result<ApiResponse<Unit>> {
        return try {
            apiService.sendBatchUsage(records)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get usage records by username and date range
     */
    suspend fun getUsageRecordsByUsername(
        username: String,
        startDate: String,
        endDate: String
    ): Result<List<UsageRecordResponse>> {
        return try {
            apiService.getUsageRecordsByUsername(username, startDate, endDate)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all consents from API
     */
    suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>> {
        return try {
            apiService.getConsents()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Submit consent data
     */
    suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse> {
        return try {
            apiService.submitConsent(consentRequest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get consent status for a user
     */
    suspend fun getConsentStatus(username: String): Result<ConsentResponse> {
        return try {
            apiService.getConsentStatus(username)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
