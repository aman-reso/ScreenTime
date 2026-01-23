package com.app.screentime.controlcenter.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.AddAllowedUserRequest
import com.app.screentime.network.model.AllowedUser
import com.app.screentime.network.model.AllowedUsersResponse
import com.app.screentime.network.model.UpdateAllowedUserRequest
import com.app.screentime.network.model.ControlPanelResponse
import com.app.screentime.network.model.GrantAccessRequest
import com.app.screentime.network.model.RevokeAccessRequest
import com.app.screentime.network.model.ExtendAccessRequest
import com.app.screentime.network.model.AccessibleUsersResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ControlCenterServiceImpl @Inject constructor(
    networkClient: NetworkClient
) : ControlCenterService {

    private val httpClient = networkClient.httpClient

    override suspend fun getAllowedUsers(): Result<ApiResponse<AllowedUsersResponse>> {
        return try {
            val response = httpClient.get(ApiEndpoints.Profile.GET_ALLOWED_USERS) {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<AllowedUsersResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get allowed users: ${response.status}"))
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

    override suspend fun addAllowedUser(request: AddAllowedUserRequest): Result<ApiResponse<AllowedUser>> {
        return try {
            val response = httpClient.post(ApiEndpoints.Profile.ADD_ALLOWED_USER) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<AllowedUser> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to add allowed user: ${response.status}"))
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

    override suspend fun removeAllowedUser(username: String): Result<ApiResponse<Unit>> {
        return try {
            val endpoint = ApiEndpoints.Profile.REMOVE_ALLOWED_USER.replace("{username}", username)
            val response = httpClient.delete(endpoint) {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to remove allowed user: ${response.status}"))
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

    override suspend fun updateAllowedUserDuration(username: String, request: UpdateAllowedUserRequest): Result<ApiResponse<AllowedUser>> {
        return try {
            val endpoint = ApiEndpoints.Profile.UPDATE_ALLOWED_USER.replace("{username}", username)
            val response = httpClient.put(endpoint) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<AllowedUser> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update allowed user duration: ${response.status}"))
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

    override suspend fun getControlPanel(): Result<ApiResponse<ControlPanelResponse>> {
        return try {
            val response = httpClient.get(ApiEndpoints.TOTP.CONTROL_PANEL) {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<ControlPanelResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get control panel: ${response.status}"))
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

    override suspend fun grantAccess(request: GrantAccessRequest): Result<ApiResponse<Unit>> {
        return try {
            val response = httpClient.post(ApiEndpoints.TOTP.GRANT_ACCESS) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to grant access: ${response.status}"))
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

    override suspend fun revokeAccess(request: RevokeAccessRequest): Result<ApiResponse<Unit>> {
        return try {
            val response = httpClient.post(ApiEndpoints.TOTP.REVOKE_ACCESS) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to revoke access: ${response.status}"))
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

    override suspend fun extendAccess(request: ExtendAccessRequest): Result<ApiResponse<Unit>> {
        return try {
            val response = httpClient.post(ApiEndpoints.TOTP.EXTEND_ACCESS) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to extend access: ${response.status}"))
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

    override suspend fun getAccessibleUsers(): Result<ApiResponse<AccessibleUsersResponse>> {
        return try {
            val response = httpClient.get(ApiEndpoints.TOTP.ACCESSIBLE_USERS) {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<AccessibleUsersResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get accessible users: ${response.status}"))
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

