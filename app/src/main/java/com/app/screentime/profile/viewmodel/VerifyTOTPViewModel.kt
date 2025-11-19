package com.app.screentime.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.profile.repository.TOTPRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyTOTPViewModel @Inject constructor(
    private val totpRepository: TOTPRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyTOTPUiState())
    val uiState: StateFlow<VerifyTOTPUiState> = _uiState.asStateFlow()

    /**
     * Verify TOTP code by username
     * @param username Username of the user whose TOTP is being verified
     * @param code TOTP code to verify
     */
    fun verifyTOTPByUsername(username: String, code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVerifying = true, error = null)

            try {
                val result = totpRepository.verifyTOTPByUsername(username, code)
                result.fold(
                    onSuccess = { response ->
                        val isValid = response.data?.valid == true
                        _uiState.value = _uiState.value.copy(
                            isVerifying = false,
                            isVerified = isValid,
                            isValid = isValid,
                            error = if (!isValid) response.data?.message
                                ?: "Invalid TOTP code" else null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isVerifying = false,
                            isVerified = true,
                            isValid = false,
                            error = exception.message ?: "Failed to verify TOTP"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isVerifying = false,
                    isVerified = true,
                    isValid = false,
                    error = e.message ?: "Failed to verify TOTP"
                )
            }
        }
    }

    /**
     * Clear error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Reset verification status
     */
    fun resetVerification() {
        _uiState.value = _uiState.value.copy(
            isVerified = false,
            isValid = false,
            error = null
        )
    }
}

data class VerifyTOTPUiState(
    val isVerifying: Boolean = false,
    val isVerified: Boolean = false,
    val isValid: Boolean = false,
    val error: String? = null
)

