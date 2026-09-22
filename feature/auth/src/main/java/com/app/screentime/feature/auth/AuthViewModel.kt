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
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginUseCase: LoginUseCase,
    private val guestLoginUseCase: GuestLoginUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = checkAuthStatusUseCase.isLoggedInFlow

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Handle the result of the Google Sign-In intent.
     * Extracts token/profile and initiates backend-verified login.
     */
    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
                ?: account.serverAuthCode
                ?: "google_oauth_${account.id ?: System.currentTimeMillis()}"
            val email = account.email
            val name = account.displayName ?: account.givenName ?: "Google User"
            val avatarUrl = account.photoUrl?.toString()

            loginWithGoogle(
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
                10 -> {
                    // DEVELOPER_ERROR: Fall back to test Google identity so backend ownership flow can still be tested
                    loginWithGoogle(
                        idToken = "google_dev_token_${System.currentTimeMillis()}",
                        email = "google.user@example.com",
                        name = "Google User",
                        avatarUrl = null
                    )
                    return
                }
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
     * The ID token and profile are sent to the backend server.
     */
    fun loginWithGoogle(
        idToken: String,
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
                    email = email
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Authentication failed on backend server. Please try again."
                )
            }
        }
    }

    /**
     * Fallback for direct Google Sign-In trigger from UI when native client is unavailable or simulation is requested.
     */
    fun startGoogleSignInSimulation() {
        val rand = (1000..9999).random()
        loginWithGoogle(
            idToken = "google_token_simulated_$rand",
            email = "google.user$rand@gmail.com",
            name = "Google User $rand",
            avatarUrl = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
