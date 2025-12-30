package com.app.screentime.network.repository.user

import com.app.screentime.core.network.model.*
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.network.service.user.UserService
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.utils.DeviceInfoUtils
import com.app.screentime.network.model.UserPreferences
import com.app.screentime.network.model.UserProfile
import com.app.screentime.network.model.UserProfileWithSync
import com.app.screentime.network.model.UserSearchResult
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import com.app.screentime.utils.DateUtils

/**
 * Repository for User related network operations
 */
@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
    private val preferencesManager: PreferencesManager,
    private val deviceInfoUtils: DeviceInfoUtils
) {
    suspend fun registerDevice(): Result<DeviceRegistrationResponse> {
        return try {
            val result = userService.registerDevice(deviceInfoUtils.getDeviceInfo())
            result.map { apiResponse ->
                val registrationData = apiResponse.data
                if (registrationData != null && apiResponse.success == true) {
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
        val json = Json { ignoreUnknownKeys = true }
        preferencesManager.putString(
            "user_reg_info",
            json.encodeToString(DeviceRegistrationResponse.serializer(), deviceRegistrationResponse)
        )

        // Parse and save lastSyncTime if present
        deviceRegistrationResponse.lastSyncTime?.let { lastSyncTimeString ->
            try {
                val timestampMillis = DateUtils.toMillis(lastSyncTimeString)
                if (timestampMillis > 0) {
                    preferencesManager.setLastSyncTime(timestampMillis)
                }
            } catch (e: Exception) {
                // Log error but don't fail registration if timestamp parsing fails
                android.util.Log.e(
                    "UserRepository",
                    "Failed to parse lastSyncTime: $lastSyncTimeString",
                    e
                )
            }
        }
    }

    suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>> {
        return userService.getUserProfile(userId)
    }

    suspend fun getUserProfileWithSync(userId: String): Result<ApiResponse<UserProfileWithSync>> {
        return userService.getUserProfileWithSync(userId)
    }

    suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>> {
        return userService.updateUserProfile(profile)
    }

    suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>> {
        return userService.getUserPreferences(userId)
    }

    suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<ApiResponse<UserPreferences>> {
        return userService.updateUserPreferences(userId, preferences)
    }

    suspend fun searchUsers(query: String): Result<ApiResponse<List<UserSearchResult>>> {
        return userService.searchUsers(query)
    }
}

