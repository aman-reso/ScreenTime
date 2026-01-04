package com.app.screentime.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.profile.model.ProfileUiProps
import com.app.screentime.profile.model.SettingsItemClickResult
import com.app.screentime.profile.usecase.ProfileUseCase
import com.app.screentime.security.TOTP
import com.app.screentime.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiProps = MutableStateFlow<ProfileUiProps?>(null)
    val uiProps: StateFlow<ProfileUiProps?> = _uiProps.asStateFlow()

    private val _totpState = MutableStateFlow(TOTPState())
    val totpState: StateFlow<TOTPState> = _totpState.asStateFlow()

    init {
        loadProfile()
        startTOTPGeneration()
        startPeriodicUpdate()
    }

    /**
     * Load profile data and get UI Props from use case
     */
    fun loadProfile() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val props = profileUseCase.getProfileUiProps(isLoading = true)
                _uiProps.value = props

                val updatedProps = profileUseCase.getProfileUiProps(isLoading = false)
                _uiProps.value = updatedProps
            } catch (e: Exception) {
                _uiProps.value = profileUseCase.getProfileUiProps(
                    isLoading = false,
                    error = e.message ?: "Failed to load profile"
                )
            }
        }
    }

    /**
     * Periodically update VPN status and blocked sites count
     */
    private fun startPeriodicUpdate() {
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(2000) // Check every 2 seconds
                val currentProps = _uiProps.value
                if (currentProps != null) {
                    try {
                        val updatedProps = profileUseCase.getProfileUiProps(
                            isLoading = currentProps.isLoading,
                            isUpdating = currentProps.isUpdating,
                            error = currentProps.error
                        )
                        _uiProps.value = updatedProps
                    } catch (e: Exception) {
                        // Silently fail on periodic updates
                    }
                }
            }
        }
    }

    /**
     * Handle settings item click
     */
    fun handleSettingsItemClick(
        key: com.app.screentime.profile.model.ProfileSettingsKey,
        url: String
    ): SettingsItemClickResult {
        val currentProps = _uiProps.value
        return profileUseCase.handleSettingsItemClick(key, url)
    }

    /**
     * Request widget setup
     */
    fun requestWidgetSetup() {
        viewModelScope.launch {
            profileUseCase.requestWidgetSetup()
        }
    }

    /**
     * Clear error
     */
    fun clearError() {
        val currentProps = _uiProps.value
        if (currentProps != null) {
            _uiProps.value = currentProps.copy(error = null)
        }
    }


    /**
     * Start TOTP generation and countdown
     */
    private fun startTOTPGeneration() {
        viewModelScope.launch(Dispatchers.Default) {
            val totpSecret = preferencesManager.getTOTPSecret()

            while (true) {
                val now = DateUtils.now()
                val epochSeconds = now.millis / 1000
                val remaining = (60 - (epochSeconds % 60)).toInt()

                val otp = if (totpSecret != null) {
                    TOTP.generateTOTP(totpSecret)
                } else {
                    "******"
                }

                _totpState.value = TOTPState(
                    otp = otp,
                    remainingSeconds = remaining,
                    isRunning = true
                )

                // Wait for the current period to complete
                for (i in remaining downTo 1) {
                    _totpState.value = _totpState.value.copy(remainingSeconds = i)
                    delay(1000)
                }

                // When period completes, regenerate OTP
                _totpState.value = _totpState.value.copy(remainingSeconds = 60)
            }
        }
    }

    /**
     * Update username
     * Returns true if successful, false otherwise
     */
    fun updateUsername(newUsername: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val currentProps = _uiProps.value
            _uiProps.value = currentProps?.copy(isUpdating = true, error = null)

            try {
                val result = profileUseCase.updateUsername(newUsername)
                if (result) {
                    // Reload profile to reflect changes
                    val updatedProps = profileUseCase.getProfileUiProps(
                        isLoading = currentProps?.isLoading ?: false,
                        isUpdating = false,
                        error = null
                    )
                    _uiProps.value = updatedProps
                    onSuccess() // Call success callback to dismiss bottom sheet
                } else {
                    _uiProps.value = currentProps?.copy(
                        isUpdating = false,
                        error = "Failed to update username"
                    )
                }
            } catch (e: Exception) {
                _uiProps.value = currentProps?.copy(
                    isUpdating = false,
                    error = e.message ?: "Failed to update username"
                )
            }
        }
    }
}

/**
 * TOTP State for displaying TOTP code and countdown
 */
data class TOTPState(
    val otp: String = "******",
    val remainingSeconds: Int = 60,
    val isRunning: Boolean = true
)

