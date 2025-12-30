package com.app.screentime.profile.repository

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.TOTPVerifyResponse
import com.app.screentime.network.model.UsernameTOTPVerifyRequest
import com.app.screentime.profile.service.TOTPService
import javax.inject.Inject

/**
 * Repository for TOTP operations
 */
class TOTPRepository @Inject constructor(
    private val totpService: TOTPService
) {

    suspend fun verifyTOTPByUsername(username: String, code: String): Result<ApiResponse<TOTPVerifyResponse>> {
        val request = UsernameTOTPVerifyRequest(code = code)
        return totpService.verifyTOTPByUsername(username, request)
    }

}

