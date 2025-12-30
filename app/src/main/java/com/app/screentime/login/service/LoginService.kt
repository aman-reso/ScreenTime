package com.app.screentime.login.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.core.network.utils.DeviceInfoUtils

/**
 * API Service interface for Login and Device Registration operations
 */
interface LoginService {
    suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo, firebaseToken: String? = null): Result<ApiResponse<DeviceRegistrationResponse>>
}

