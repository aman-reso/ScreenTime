package com.app.screentime.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.UserRole
import com.app.screentime.feature.auth.domain.usecase.CheckAuthStatusUseCase
import com.app.screentime.feature.auth.domain.usecase.GuestLoginUseCase
import com.app.screentime.feature.auth.domain.usecase.LoginUseCase
import com.app.screentime.feature.auth.util.PhotoVerificationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthStep {
    PHONE_INPUT,
    OTP_INPUT,
    CREATOR_DETAILS
}

data class AuthUiState(
    val step: AuthStep = AuthStep.PHONE_INPUT,
    val isLoading: Boolean = false,
    val isGuestLoading: Boolean = false,
    val error: String? = null,
    val phone: String = "",
    val otp: String = "",
    val name: String = "",
    val role: UserRole = UserRole.USER,
    val bio: String = "",
    val voiceRate: String = "15",
    val avatarUrl: String = "",
    val photoStatus: PhotoVerificationUtil.VerificationStatus = PhotoVerificationUtil.VerificationStatus.Idle,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val guestLoginUseCase: GuestLoginUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = checkAuthStatusUseCase.isLoggedInFlow

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onPhoneChanged(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone, error = null)
    }

    fun onOtpChanged(otp: String) {
        _uiState.value = _uiState.value.copy(otp = otp, error = null)
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name, error = null)
    }

    fun onRoleChanged(role: UserRole) {
        _uiState.value = _uiState.value.copy(role = role, error = null)
    }

    fun onBioChanged(bio: String) {
        _uiState.value = _uiState.value.copy(bio = bio)
    }

    fun onVoiceRateChanged(rate: String) {
        _uiState.value = _uiState.value.copy(voiceRate = rate)
    }

    fun onAvatarUrlChanged(url: String) {
        val status = PhotoVerificationUtil.validateProfilePhoto(url)
        _uiState.value = _uiState.value.copy(avatarUrl = url, photoStatus = status)
    }

    fun resetToPhoneInput() {
        _uiState.value = _uiState.value.copy(step = AuthStep.PHONE_INPUT, otp = "", error = null)
    }

    fun sendOtp() {
        val cleanPhone = _uiState.value.phone.trim()
        if (cleanPhone.length < 10) {
            _uiState.value = _uiState.value.copy(error = "Please enter a valid 10-digit mobile number")
            return
        }
        _uiState.value = _uiState.value.copy(step = AuthStep.OTP_INPUT, otp = "", error = null)
    }

    fun verifyOtp() {
        verifyAndLogin()
    }

    fun verifyAndLogin() {
        val current = _uiState.value
        val cleanPhone = current.phone.trim()
        val cleanOtp = current.otp.trim()

        if (cleanPhone.length < 10) {
            _uiState.value = current.copy(error = "Please enter a valid 10-digit mobile number")
            return
        }
        if (cleanOtp.length < 4) {
            _uiState.value = current.copy(error = "Please enter a valid 4-digit OTP code (e.g. 1234)")
            return
        }

        val defaultName = if (current.role == UserRole.MODEL) "Model " + cleanPhone.takeLast(4) else "User " + cleanPhone.takeLast(4)
        performLogin(defaultName, current.role)
    }

    fun loginWithGoogle(selectedRole: UserRole? = null) {
        val targetRole = selectedRole ?: _uiState.value.role
        val mockGooglePhone = "98" + (10000000..99999999).random()
        val mockName = if (targetRole == UserRole.MODEL) "Creator Google" else "Google User"
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null,
            role = targetRole
        )
        viewModelScope.launch {
            val result = loginUseCase(mockGooglePhone, mockName, targetRole.name.lowercase())
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Google Sign-In failed. Please try again."
                )
            }
        }
    }

    fun submitCreatorDetails() {
        val current = _uiState.value
        if (current.name.isBlank()) {
            _uiState.value = current.copy(error = "Creator display name is required")
            return
        }
        performLogin(current.name.trim(), UserRole.MODEL)
    }

    private fun performLogin(userName: String, role: UserRole) {
        val current = _uiState.value
        _uiState.value = current.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = loginUseCase(current.phone.trim(), userName, role.name.lowercase())
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Login failed. Please try again."
                )
            }
        }
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGuestLoading = true, error = null)
            val result = guestLoginUseCase()
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isGuestLoading = false, isSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isGuestLoading = false,
                    error = e.message ?: "Guest login failed"
                )
            }
        }
    }
}
