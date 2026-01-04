package com.app.screentime.network.service.user

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.*
import com.app.screentime.core.network.model.DeviceRegistrationRequest
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.core.network.utils.DeviceInfoUtils
import com.app.screentime.network.model.UserPreferences
import com.app.screentime.network.model.UserProfile
import com.app.screentime.network.model.UserProfileWithSync
import com.app.screentime.network.model.UserSearchResult
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of UserService using Ktor
 */
@Singleton
class UserServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : UserService {

    private val httpClient = networkClient.httpClient

    // API calls disabled - App works offline
    override suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo): Result<ApiResponse<DeviceRegistrationResponse>> {
        return Result.failure(Exception("API calls disabled - App works offline"))
    }

    // API calls disabled - App works offline
    override suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>> {
        return Result.failure(Exception("API calls disabled - App works offline"))
    }

    // API calls disabled - App works offline
    override suspend fun getUserProfileWithSync(userId: String): Result<ApiResponse<UserProfileWithSync>> {
        return Result.failure(Exception("API calls disabled - App works offline"))
    }

    // API calls disabled - App works offline
    override suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>> {
        return Result.failure(Exception("API calls disabled - App works offline"))
    }

    // API calls disabled - App works offline
    override suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>> {
        return Result.failure(Exception("API calls disabled - App works offline"))
    }

    // API calls disabled - App works offline
    override suspend fun updateUserPreferences(
        userId: String,
        preferences: UserPreferences
    ): Result<ApiResponse<UserPreferences>> {
        return Result.failure(Exception("API calls disabled - App works offline"))
    }

    // API calls disabled - App works offline
    override suspend fun searchUsers(query: String): Result<ApiResponse<List<UserSearchResult>>> {
        return Result.failure(Exception("API calls disabled - App works offline"))
    }
}

