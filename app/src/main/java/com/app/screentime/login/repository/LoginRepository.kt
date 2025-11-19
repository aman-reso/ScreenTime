package com.app.screentime.login.repository

import com.app.screentime.login.service.LoginService
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.utils.DeviceInfoUtils
import javax.inject.Inject

/**
 * Repository for login and device registration operations
 */
class LoginRepository @Inject constructor(
    private val loginService: LoginService,
    private val deviceInfoUtils: DeviceInfoUtils
) {
    
    /**
     * Register device with backend API and get userId
     */
    suspend fun registerDevice(): Result<DeviceRegistrationResponse> {
        val deviceInfo = deviceInfoUtils.getDeviceInfo()
        return loginService.registerDevice(deviceInfo).map { apiResponse ->
            apiResponse.data ?: throw Exception("Device registration failed: ${apiResponse.message}")
        }
    }
}

