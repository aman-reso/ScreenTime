package com.app.screentime.login.service

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.utils.DeviceInfoUtils

/**
 * API Service interface for Login and Device Registration operations
 */
interface LoginService {
    suspend fun registerDevice(deviceInfo: DeviceInfoUtils.DeviceInfo, firebaseToken: String? = null): Result<ApiResponse<DeviceRegistrationResponse>>
}

