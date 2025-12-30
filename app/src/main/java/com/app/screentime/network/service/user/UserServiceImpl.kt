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

    override suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo): Result<ApiResponse<DeviceRegistrationResponse>> {
        return try {
            val request = DeviceRegistrationRequest(deviceInfo = deviceInfo)

            val apiResponse: ApiResponse<DeviceRegistrationResponse> =
                httpClient.post(ApiEndpoints.Registration.REGISTER_DEVICE) {
                    setBody(request)
                }.body()

            Result.success(apiResponse)
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

    override suspend fun getUserProfile(userId: String): Result<ApiResponse<UserProfile>> {
        return try {
            val response: HttpResponse =
                httpClient.get("${ApiEndpoints.Profile.GET_PROFILE}/$userId")

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

    override suspend fun getUserProfileWithSync(userId: String): Result<ApiResponse<UserProfileWithSync>> {
        return try {
            val response: HttpResponse =
                httpClient.get("${ApiEndpoints.Profile.GET_PROFILE}/$userId") {
                    parameter("includeSyncTime", "true")
                }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserProfileWithSync> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get user profile with sync: ${response.status}"))
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

    override suspend fun getUserPreferences(userId: String): Result<ApiResponse<UserPreferences>> {
        return try {
            val response: HttpResponse =
                httpClient.get("${ApiEndpoints.Profile.GET_PREFERENCES}/$userId")

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

    override suspend fun searchUsers(query: String): Result<ApiResponse<List<UserSearchResult>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Search.SEARCH_USERS) {
                parameter("q", query)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<UserSearchResult>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to search users: ${response.status}"))
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

