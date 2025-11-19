package com.app.screentime.network.repository.user

import com.app.screentime.network.model.*
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.network.service.user.UserService
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.utils.DeviceInfoUtils
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for User related network operations
 */
@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
    private val preferencesManager: PreferencesManager
) {
    suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo): Result<DeviceRegistrationResponse> {
        return try {
            val result = userService.registerDevice(deviceInfo)
            result.map { apiResponse ->
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
        val json = Json { ignoreUnknownKeys = true }
        preferencesManager.putString(
            "user_reg_info",
            json.encodeToString(DeviceRegistrationResponse.serializer(), deviceRegistrationResponse)
        )
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

