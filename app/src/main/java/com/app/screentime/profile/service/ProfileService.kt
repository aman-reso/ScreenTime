package com.app.screentime.profile.service

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.network.model.UserProfile
import com.app.screentime.network.model.UserPreferences
import com.app.screentime.network.model.UsernameUpdateRequest

/**
 * API Service interface for Profile operations
 */
interface ProfileService {
    suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>>
    suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>>
    suspend fun updateUsername(request: UsernameUpdateRequest): Result<ApiResponse<DeviceRegistrationResponse>>
    suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>>
    suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<ApiResponse<UserPreferences>>
}

