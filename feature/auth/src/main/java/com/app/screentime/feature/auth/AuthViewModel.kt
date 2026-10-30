package com.app.screentime.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.UserRole
import com.app.screentime.feature.auth.domain.usecase.CheckAuthStatusUseCase
import com.app.screentime.feature.auth.domain.usecase.GuestLoginUseCase
import com.app.screentime.feature.auth.domain.usecase.LoginUseCase
import com.app.screentime.feature.auth.domain.usecase.LoginWithGoogleUseCase
import com.app.screentime.feature.auth.util.PhotoVerificationUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.app.screentime.feature.auth.domain.usecase.SendOtpUseCase
import com.app.screentime.feature.auth.domain.usecase.VerifyOtpUseCase

import android.content.Context
import com.app.screentime.config.language.AppLanguageManager

enum class AuthStep {
    GOOGLE_AUTH,
    PHONE_INPUT,
    OTP_INPUT
}

data class AuthUiState(
    val step: AuthStep = AuthStep.GOOGLE_AUTH,
    val isLoading: Boolean = false,
    val isGuestLoading: Boolean = false,
    val error: String? = null,
    val email: String? = null,
    val phone: String = "",
    val countryCode: String = "+91",
    val countryFlag: String = "🇮🇳",
    val fullFormattedPhone: String = "",
    val otp: String = "",
    val otpHint: String? = null,
    val name: String = "",
    val role: UserRole = UserRole.USER,
    val bio: String = "",
    val voiceRate: String = "15",
    val avatarUrl: String = "",
    val photoStatus: PhotoVerificationUtil.VerificationStatus = PhotoVerificationUtil.VerificationStatus.Idle,
    val isSuccess: Boolean = false,
    val currentLanguageCode: String = "en"
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginUseCase: LoginUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val guestLoginUseCase: GuestLoginUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    private val appLanguageManager: AppLanguageManager
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = checkAuthStatusUseCase.isLoggedInFlow

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            currentLanguageCode = appLanguageManager.getLanguageCode()
        )
    }

    fun changeLanguage(context: Context, languageCode: String) {
        appLanguageManager.changeLanguage(context, languageCode)
        _uiState.value = _uiState.value.copy(currentLanguageCode = languageCode)
    }

    fun getCurrentLanguageCode(context: Context? = null): String {
        return if (context != null) appLanguageManager.getLanguageCode(context) else appLanguageManager.getLanguageCode()
    }

    /**
     * Handle the result of the Google Sign-In intent.
     * Extracts token/profile and initiates backend-verified login.
     */
    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        try {
            val account = task.getResult(ApiException::class.java)
            val googleId = account.id ?: "google_${System.currentTimeMillis()}"
            val idToken = account.idToken
                ?: account.serverAuthCode
                ?: googleId
            val email = account.email
            val name = account.displayName ?: account.givenName ?: "User"
            val avatarUrl =
                account.photoUrl?.toString() ?: "https://lh3.googleusercontent.com/a/default-user"

            loginWithGoogle(
                googleId = googleId,
                idToken = idToken,
                email = email,
                name = name,
                avatarUrl = avatarUrl
            )
        } catch (e: ApiException) {
            val errorMsg = when (e.statusCode) {
                7 -> "Network error. Please check your internet connection."
                12501 -> null // User simply cancelled the dialog, no error message needed
                12500 -> "Google Sign-In service error. Please try again."
                else -> e.message ?: "Google Sign-In failed (${e.statusCode})"
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = errorMsg
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = e.message ?: "Google Sign-In error. Please try again."
            )
        }
    }

    /**
     * Authenticate via Google Sign-In where the backend is the overall owner.
     * Google ID, email, name, and photo URL are sent to POST /api/auth/google.
     */
    fun loginWithGoogle(
        googleId: String? = null,
        idToken: String? = null,
        email: String? = null,
        name: String? = null,
        avatarUrl: String? = null,
        role: String = "user"
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )
        viewModelScope.launch {
            val result = loginWithGoogleUseCase(
                googleId = googleId,
                idToken = idToken,
                email = email,
                name = name,
                avatarUrl = avatarUrl,
                role = role
            )
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    name = user.name,
                    email = user.phone
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Authentication failed on backend server. Please try again."
                )
            }
        }
    }

    fun showGoogleSignInError(errorMsg: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = errorMsg
        )
    }

    fun setAuthStep(step: AuthStep) {
        _uiState.value = _uiState.value.copy(step = step, error = null)
    }

    fun resetAuthFlow() {
        val currentLang = _uiState.value.currentLanguageCode
        _uiState.value = AuthUiState(currentLanguageCode = currentLang)
    }

    fun onPhoneChange(phone: String) {
        val digits = phone.filter { it.isDigit() }.take(15)
        _uiState.value = _uiState.value.copy(phone = digits, error = null)
    }

    fun onCountryCodeChange(countryCode: String, flag: String = "") {
        val formatted = if (countryCode.startsWith("+")) countryCode else "+$countryCode"
        _uiState.value = _uiState.value.copy(
            countryCode = formatted,
            countryFlag = flag.ifBlank { _uiState.value.countryFlag },
            error = null
        )
    }

    fun onOtpChange(otp: String) {
        val digits = otp.filter { it.isDigit() }.take(6)
        _uiState.value = _uiState.value.copy(otp = digits, error = null)
    }

    fun requestPhoneOtp(phone: String) {
        val cleanDigits = phone.filter { it.isDigit() }
        if (cleanDigits.length < 6) {
            _uiState.value =
                _uiState.value.copy(error = "Please enter a valid mobile number")
            return
        }
        val formattedPhone = when {
            _uiState.value.fullFormattedPhone.isNotBlank() -> _uiState.value.fullFormattedPhone
            phone.startsWith("+") && cleanDigits.length == 10 && cleanDigits[0] in '6'..'9' -> "${_uiState.value.countryCode}$cleanDigits"
            phone.startsWith("+") -> phone
            cleanDigits.length == 10 && cleanDigits[0] in '6'..'9' -> "${_uiState.value.countryCode}$cleanDigits"
            else -> "${_uiState.value.countryCode}$cleanDigits"
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = sendOtpUseCase(formattedPhone)
            result.onSuccess { hint ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    step = AuthStep.OTP_INPUT,
                    fullFormattedPhone = formattedPhone,
                    otpHint = if (hint.contains("123456")) null else hint.ifBlank { null },
                    error = null
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    step = AuthStep.OTP_INPUT,
                    fullFormattedPhone = formattedPhone,
                    otpHint = null,
                    error = null
                )
            }
        }
    }

    /**
     * Verify OTP code with backend (/api/auth/otp/verify) and complete authentication.
     */
    fun loginWithPhone(phone: String) {
        val cleanDigits = phone.filter { it.isDigit() }
        val code = _uiState.value.otp.trim()
        if (cleanDigits.length < 6) {
            _uiState.value =
                _uiState.value.copy(error = "Please enter a valid mobile number")
            return
        }
        if (code.length < 4) {
            _uiState.value =
                _uiState.value.copy(error = "Please enter the 4-digit verification code sent to your mobile")
            return
        }
        val formattedPhone = when {
            _uiState.value.fullFormattedPhone.isNotBlank() -> _uiState.value.fullFormattedPhone
            phone.startsWith("+") && cleanDigits.length == 10 && cleanDigits[0] in '6'..'9' -> "${_uiState.value.countryCode}$cleanDigits"
            phone.startsWith("+") -> phone
            cleanDigits.length == 10 && cleanDigits[0] in '6'..'9' -> "${_uiState.value.countryCode}$cleanDigits"
            else -> "${_uiState.value.countryCode}$cleanDigits"
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = verifyOtpUseCase(formattedPhone, code)
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    name = user.name,
                    phone = user.phone
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "OTP verification failed. Please try again."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
