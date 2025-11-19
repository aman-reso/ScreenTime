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
import com.app.screentime.preferences.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json

/**
 * Use case for login and device registration operations
 */
class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferencesManager: PreferencesManager,
) {

    suspend fun registerDevice() {
        loginRepository.registerDevice().onFailure {

        }.onSuccess {
            preferencesManager.saveUserInformation(it)
        }
    }

}

