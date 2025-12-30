package com.app.screentime.profile.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.network.model.ProfileUpdateRequest
import com.app.screentime.network.model.UserProfile
import com.app.screentime.network.model.UserPreferences
import com.app.screentime.network.model.UsernameUpdateRequest
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ProfileService using Ktor
 */
@Singleton
class ProfileServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : ProfileService {

    private val httpClient = networkClient.httpClient

    override suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiEndpoints.Profile.GET_PROFILE}/$userId")

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserProfile> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get user profile: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
        } catch (e: ServerResponseException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile): Result<ApiResponse<UserProfile>> {
        return try {
            val response: HttpResponse = httpClient.put(ApiEndpoints.Profile.UPDATE_PROFILE) {
                setBody(profile)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserProfile> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update user profile: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
        } catch (e: ServerResponseException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(request: ProfileUpdateRequest): Result<ApiResponse<DeviceRegistrationResponse>> {
        return try {
            val response: HttpResponse = httpClient.put(ApiEndpoints.Profile.UPDATE_PROFILE) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<DeviceRegistrationResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update profile: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
        } catch (e: ServerResponseException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUsername(request: UsernameUpdateRequest): Result<ApiResponse<DeviceRegistrationResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Profile.UPDATE_USERNAME) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<DeviceRegistrationResponse> = response.body()
                // Check if API response indicates success
                if (apiResponse.success == true) {
                    Result.success(apiResponse)
                } else {
                    // API returned success=false, check for specific error messages
                    val errorMessage = apiResponse.message ?: apiResponse.error?.message ?: "Failed to update username"
                    if (errorMessage.contains("already taken", ignoreCase = true)) {
                        Result.failure(Exception("Username already taken"))
                    } else {
                        Result.failure(Exception(errorMessage))
                    }
                }
            } else {
                val errorBody = response.bodyAsText()
                // Try to parse error response
                try {
                    val errorResponse: ApiResponse<DeviceRegistrationResponse> = response.body()
                    val errorMessage = errorResponse.message ?: errorResponse.error?.message ?: errorBody
                    if (errorMessage.contains("already taken", ignoreCase = true)) {
                        Result.failure(Exception("Username already taken"))
                    } else {
                        Result.failure(Exception(errorMessage))
                    }
                } catch (e: Exception) {
                    // If parsing fails, check error body text
                    if (errorBody.contains("already taken", ignoreCase = true)) {
                        Result.failure(Exception("Username already taken"))
                    } else {
                        Result.failure(Exception("Failed to update username: ${response.status}"))
                    }
                }
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            // Try to parse error response
            try {
                val errorResponse: ApiResponse<DeviceRegistrationResponse> = e.response.body()
                val errorMessage = errorResponse.message ?: errorResponse.error?.message ?: errorBody
                if (errorMessage.contains("already taken", ignoreCase = true)) {
                    Result.failure(Exception("Username already taken"))
                } else {
                    Result.failure(Exception(errorMessage))
                }
            } catch (parseException: Exception) {
                // If parsing fails, check error body text
                if (errorBody.contains("already taken", ignoreCase = true)) {
                    Result.failure(Exception("Username already taken"))
                } else {
                    Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
                }
            }
        } catch (e: ServerResponseException) {
            val errorBody = e.response.bodyAsText()
            // Try to parse error response
            try {
                val errorResponse: ApiResponse<DeviceRegistrationResponse> = e.response.body()
                val errorMessage = errorResponse.message ?: errorResponse.error?.message ?: errorBody
                if (errorMessage.contains("already taken", ignoreCase = true)) {
                    Result.failure(Exception("Username already taken"))
                } else {
                    Result.failure(Exception(errorMessage))
                }
            } catch (parseException: Exception) {
                Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiEndpoints.Profile.GET_PREFERENCES}/$userId")

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserPreferences> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get user preferences: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
        } catch (e: ServerResponseException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
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
                httpClient.put("${ApiEndpoints.Profile.UPDATE_PREFERENCES}/$userId") {
                    setBody(preferences)
                }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserPreferences> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update user preferences: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
        } catch (e: ServerResponseException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

