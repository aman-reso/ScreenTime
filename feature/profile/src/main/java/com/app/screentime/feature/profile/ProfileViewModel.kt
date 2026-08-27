package com.app.screentime.feature.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.User
import com.app.screentime.core.ui.security.BiometricAuthManager
import com.app.screentime.core.ui.theme.AppThemeManager
import com.app.screentime.feature.profile.domain.usecase.FetchUserProfileUseCase
import com.app.screentime.feature.profile.domain.usecase.GetCurrentUserUseCase
import com.app.screentime.feature.profile.domain.usecase.LogoutUseCase
import com.app.screentime.feature.profile.domain.usecase.SubmitModelOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val displayName: String = "",
    val email: String = "",
    val bio: String = "",
    val selectedTheme: String = AppThemeManager.currentThemeName.value,
    val selectedLanguage: String = "English",
    val isFingerprintLockEnabled: Boolean = false,
    val favoritesCount: Int = 0,
    val walletCoins: Int = 0,
    val isOnboardingSubmitted: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val submitModelOnboardingUseCase: SubmitModelOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            isFingerprintLockEnabled = BiometricAuthManager.isFingerprintLockEnabled(context)
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun loadUser() {
        val cachedUser = getCurrentUserUseCase()
        _uiState.value = _uiState.value.copy(
            user = cachedUser,
            displayName = cachedUser.name,
            email = cachedUser.email ?: "",
            bio = cachedUser.bio ?: "",
            walletCoins = cachedUser.walletBalance.toInt()
        )

        viewModelScope.launch {
            fetchUserProfileUseCase().onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    user = user,
                    displayName = user.name,
                    email = user.email ?: "",
                    bio = user.bio ?: "",
                    walletCoins = user.walletBalance.toInt()
                )
            }
        }
    }

    fun updateProfile(name: String, email: String, bio: String) {
        _uiState.value = _uiState.value.copy(
            displayName = name.ifBlank { _uiState.value.displayName },
            email = email.ifBlank { _uiState.value.email },
            bio = bio,
            user = _uiState.value.user?.copy(
                name = name.ifBlank { _uiState.value.displayName },
                email = email.ifBlank { _uiState.value.email },
                bio = bio
            )
        )
    }

    fun setTheme(theme: String) {
        _uiState.value = _uiState.value.copy(selectedTheme = theme)
        AppThemeManager.setTheme(theme)
    }

    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
    }

    fun toggleFingerprintLock(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isFingerprintLockEnabled = enabled)
        BiometricAuthManager.setFingerprintLockEnabled(context, enabled)
    }

    fun logout() {
        _uiState.value = ProfileUiState(
            isFingerprintLockEnabled = BiometricAuthManager.isFingerprintLockEnabled(context)
        )
        logoutUseCase()
    }

    fun submitOnboarding(bio: String, voiceRate: Double, chatRate: Double) {
        viewModelScope.launch {
            submitModelOnboardingUseCase(bio, voiceRate, chatRate).onSuccess {
                _uiState.value = _uiState.value.copy(isOnboardingSubmitted = true)
            }
        }
    }
}
