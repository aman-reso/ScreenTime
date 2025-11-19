package com.app.screentime.network.service.user

import com.app.screentime.network.model.*
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.utils.DeviceInfoUtils

/**
 * Service interface for User related API operations
 */
interface UserService {
    suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo): Result<ApiResponse<DeviceRegistrationResponse>>
    suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>>
    suspend fun getUserProfileWithSync(userId: String): Result<ApiResponse<UserProfileWithSync>>
    suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>>
    suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>>
    suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<ApiResponse<UserPreferences>>
    suspend fun searchUsers(query: String): Result<ApiResponse<List<UserSearchResult>>>
}

