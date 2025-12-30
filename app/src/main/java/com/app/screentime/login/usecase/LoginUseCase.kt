package com.app.screentime.login.usecase

import android.content.Context
import android.provider.Settings
import com.app.screentime.login.repository.LoginRepository
import com.app.screentime.messaging.FCMTokenManager
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.content.edit
import com.app.screentime.core.network.preferences.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json

/**
 * Use case for login and device registration operations
 */
class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferencesManager: PreferencesManager,
    private val fcmTokenManager: FCMTokenManager
) {

    suspend fun registerDevice() {
        // Get current FCM token if available
        val firebaseToken = fcmTokenManager.getCurrentToken()
        
        loginRepository.registerDevice(firebaseToken).onFailure {

        }.onSuccess {
            preferencesManager.saveUserInformation(it)
        }
    }

}

