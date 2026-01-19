package com.app.screentime.profile.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.TOTPStatusResponse
import com.app.screentime.network.model.TOTPVerifyResponse
import com.app.screentime.network.model.UsernameTOTPVerifyRequest

/**
 * API Service interface for TOTP operations
 */
interface TOTPService {
    suspend fun verifyTOTPByUsername(
        username: String,
        request: UsernameTOTPVerifyRequest
    ): Result<ApiResponse<TOTPVerifyResponse>>

    suspend fun getTOTPStatus(username: String): Result<ApiResponse<TOTPStatusResponse>>
}

