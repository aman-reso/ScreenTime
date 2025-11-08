package com.app.screentime.login.usecase

import android.content.Context
import android.provider.Settings
import com.app.screentime.login.repository.LoginRepository
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json

/**
 * Use case for login and device registration operations
 */
class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @ApplicationContext
    private val context: Context,
) {
    companion object {
        private const val TAG = "LoginUseCase"
        private const val USER_REG_INFO = "user_reg_info"
        private const val PREF_NAME = "screentime_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_IS_REGISTERED = "is_device_registered"
    }

    private fun saveRegistrationData(deviceRegistrationResponse: DeviceRegistrationResponse) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(USER_REG_INFO, Json.encodeToString(deviceRegistrationResponse))
        }
    }

    suspend fun registerDevice() {
        loginRepository.registerDevice()
    }

}

