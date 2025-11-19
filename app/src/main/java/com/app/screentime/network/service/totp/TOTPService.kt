package com.app.screentime.network.service.totp

import com.app.screentime.network.model.*

/**
 * Service interface for TOTP related API operations
 */
interface TOTPService {
    suspend fun generateTOTP(): Result<ApiResponse<TOTPVerifyResponse>>
}

