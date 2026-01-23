package com.app.screentime.permission.viewmodel

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.config.ConfigManager
import com.app.screentime.login.usecase.LoginUseCase
import com.app.screentime.permission.RegistrationStep
import com.app.screentime.registrations.screen.RegistrationScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistrationUiState(
    val isRegisteringDevice: Boolean = true,
    val registrationError: String? = null,
    val isRegistrationComplete: Boolean = false,
    val registrationStep: List<RegistrationStep> = emptyList()
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginUseCase: LoginUseCase,
    private val configManager: ConfigManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RegistrationUiState(
            registrationStep = listOf(
                RegistrationStep(
                    id = 1,
                    icon = Icons.Outlined.Cloud,
                    title = context.getString(com.app.screentime.config.R.string.register_device),
                    subtitle = context.getString(com.app.screentime.config.R.string.connecting_to_server),
                ),
                RegistrationStep(
                    id = 2,
                    icon = Icons.Outlined.PrivacyTip,
                    title = context.getString(com.app.screentime.config.R.string.grant_permissions),
                    subtitle = context.getString(com.app.screentime.config.R.string.allow_app_usage_access),
                )
            )
        )
    )
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    init {
        registerDevice()
    }

    fun registerDevice() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = _uiState.value.copy(
                isRegisteringDevice = true,
                registrationError = null,
                isRegistrationComplete = isLoginRequired(),
                registrationStep = _uiState.value.registrationStep.map {
                    if (it.id == 1) {
                        it.copy(state = RegistrationScreenState.LOADING)
                    } else {
                        it
                    }
                }
            )

            val deviceSuccess = try {
                loginUseCase.registerDevice()
                true
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    registrationError = e.message ?: "Failed to register device"
                )
                false
            }

            val loginStillRequired = loginUseCase.isLoginRequired()

            val registrationSuccess = deviceSuccess && !loginStillRequired

            _uiState.value = _uiState.value.copy(
                isRegisteringDevice = false,
                registrationStep = _uiState.value.registrationStep.map {
                    if (it.id == 1) {
                        it.copy(
                            state = if (registrationSuccess) {
                                RegistrationScreenState.SUCCESS
                            } else {
                                RegistrationScreenState.ERROR
                            }
                        )
                    } else {
                        it
                    }
                },
                isRegistrationComplete = registrationSuccess,
                registrationError = if (!registrationSuccess && deviceSuccess) {
                    "Registration incomplete. Please try again."
                } else {
                    _uiState.value.registrationError
                }
            )
        }
    }

    fun isLoginRequired() = loginUseCase.isLoginRequired()

    fun markRegistrationComplete() {
        val registrationSuccess = !loginUseCase.isLoginRequired()
        _uiState.value = uiState.value.copy(
            isRegisteringDevice = false,
            isRegistrationComplete = registrationSuccess,
            registrationError = null,
            registrationStep = _uiState.value.registrationStep.map {
                if (it.id == 2) {
                    it.copy(state = RegistrationScreenState.SUCCESS)
                } else {
                    it
                }
            }
        )
    }
}
