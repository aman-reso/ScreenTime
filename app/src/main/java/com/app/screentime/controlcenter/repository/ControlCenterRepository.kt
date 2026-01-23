package com.app.screentime.controlcenter.repository

import com.app.screentime.controlcenter.service.ControlCenterService
import com.app.screentime.network.model.AddAllowedUserRequest
import com.app.screentime.network.model.AllowedUser
import com.app.screentime.network.model.AllowedUsersResponse
import com.app.screentime.network.model.UpdateAllowedUserRequest
import com.app.screentime.network.model.ControlPanelResponse
import com.app.screentime.network.model.GrantAccessRequest
import com.app.screentime.network.model.RevokeAccessRequest
import com.app.screentime.network.model.ExtendAccessRequest
import com.app.screentime.network.model.AccessibleUsersResponse
import javax.inject.Inject

/**
 * Repository for Control Center operations
 * Provides a clean interface for ViewModels to interact with the service layer
 */
class ControlCenterRepository @Inject constructor(
    private val controlCenterService: ControlCenterService
) {
    suspend fun getAllowedUsers(): Result<AllowedUsersResponse> {
        return controlCenterService.getAllowedUsers().fold(
            onSuccess = { apiResponse ->
                if (apiResponse.data != null) {
                    Result.success(apiResponse.data!!)
                } else {
                    Result.failure(Exception(apiResponse.message ?: "No data received"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun addAllowedUser(username: String, duration: Long? = null): Result<AllowedUser> {
        val request = AddAllowedUserRequest(username = username, duration = duration)
        return controlCenterService.addAllowedUser(request).fold(
            onSuccess = { apiResponse ->
                if (apiResponse.data != null) {
                    Result.success(apiResponse.data!!)
                } else {
                    Result.failure(Exception(apiResponse.message ?: "No data received"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun removeAllowedUser(username: String): Result<Unit> {
        return controlCenterService.removeAllowedUser(username).fold(
            onSuccess = { apiResponse ->
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun updateAllowedUserDuration(username: String, duration: Long?): Result<AllowedUser> {
        val request = UpdateAllowedUserRequest(duration = duration)
        return controlCenterService.updateAllowedUserDuration(username, request).fold(
            onSuccess = { apiResponse ->
                if (apiResponse.data != null) {
                    Result.success(apiResponse.data!!)
                } else {
                    Result.failure(Exception(apiResponse.message ?: "No data received"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun getControlPanel(): Result<ControlPanelResponse> {
        return controlCenterService.getControlPanel().fold(
            onSuccess = { apiResponse ->
                if (apiResponse.data != null) {
                    Result.success(apiResponse.data!!)
                } else {
                    Result.failure(Exception(apiResponse.message ?: "No data received"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun grantAccess(username: String): Result<Unit> {
        val request = GrantAccessRequest(username = username)
        return controlCenterService.grantAccess(request).fold(
            onSuccess = { apiResponse ->
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun revokeAccess(username: String): Result<Unit> {
        val request = RevokeAccessRequest(username = username)
        return controlCenterService.revokeAccess(request).fold(
            onSuccess = { apiResponse ->
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun extendAccess(username: String, additionalSeconds: Long): Result<Unit> {
        val request = ExtendAccessRequest(
            username = username,
            additionalSeconds = additionalSeconds
        )
        return controlCenterService.extendAccess(request).fold(
            onSuccess = { apiResponse ->
                Result.success(Unit)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    suspend fun getAccessibleUsers(): Result<List<String>> {
        return controlCenterService.getAccessibleUsers().fold(
            onSuccess = { apiResponse ->
                if (apiResponse.data != null) {
                    Result.success(apiResponse.data!!.accessibleUserIds)
                } else {
                    Result.failure(Exception(apiResponse.message ?: "No data received"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}

