package com.app.screentime.login.repository

import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.network.repository.NetworkRepository
import javax.inject.Inject

/**
 * Repository for login and device registration operations
 */
class LoginRepository @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    
    /**
     * Register device with backend API and get userId
     */
    suspend fun registerDevice(): Result<DeviceRegistrationResponse> {
        return networkRepository.registerDevice()
    }
}

