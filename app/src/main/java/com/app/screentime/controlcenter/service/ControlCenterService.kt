package com.app.screentime.controlcenter.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.AllowedUser
import com.app.screentime.network.model.AllowedUsersResponse
import com.app.screentime.network.model.AddAllowedUserRequest
import com.app.screentime.network.model.UpdateAllowedUserRequest
import com.app.screentime.network.model.ControlPanelResponse
import com.app.screentime.network.model.GrantAccessRequest
import com.app.screentime.network.model.RevokeAccessRequest
import com.app.screentime.network.model.ExtendAccessRequest
import com.app.screentime.network.model.AccessibleUsersResponse

/**
 * Service interface for Control Center (Timeline Privacy) operations
 */
interface ControlCenterService {
    /**
     * Get list of allowed users who can see the timeline
     */
    suspend fun getAllowedUsers(): Result<ApiResponse<AllowedUsersResponse>>
    
    /**
     * Add a username to the allowed users list
     */
    suspend fun addAllowedUser(request: AddAllowedUserRequest): Result<ApiResponse<AllowedUser>>
    
    /**
     * Remove a username from the allowed users list
     */
    suspend fun removeAllowedUser(username: String): Result<ApiResponse<Unit>>
    
    /**
     * Update the duration for an allowed user
     */
    suspend fun updateAllowedUserDuration(username: String, request: UpdateAllowedUserRequest): Result<ApiResponse<AllowedUser>>
    
    /**
     * Get control panel data (active TOTP sessions)
     */
    suspend fun getControlPanel(): Result<ApiResponse<ControlPanelResponse>>
    
    /**
     * Grant access to a user (add new user via TOTP)
     */
    suspend fun grantAccess(request: GrantAccessRequest): Result<ApiResponse<Unit>>
    
    /**
     * Revoke access from a user (remove user via TOTP)
     */
    suspend fun revokeAccess(request: RevokeAccessRequest): Result<ApiResponse<Unit>>
    
    /**
     * Extend access for a user (update duration via TOTP)
     */
    suspend fun extendAccess(request: ExtendAccessRequest): Result<ApiResponse<Unit>>
    
    /**
     * Get list of accessible users (users whose data you can access)
     */
    suspend fun getAccessibleUsers(): Result<ApiResponse<AccessibleUsersResponse>>
}

