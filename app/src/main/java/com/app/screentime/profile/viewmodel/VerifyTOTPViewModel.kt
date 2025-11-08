package com.app.screentime.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.network.model.TOTPVerifyRequest
import com.app.screentime.network.service.ApiService
import com.app.screentime.security.TOTP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyTOTPViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyTOTPUiState())
    val uiState: StateFlow<VerifyTOTPUiState> = _uiState.asStateFlow()

    /**
     * Verify TOTP code
     */
    fun verifyTOTP(code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVerifying = true, error = null)

            try {
                val secret = "O5YRY4I2737IGHVYOHXM6T7RWWNAW3X7" // From TOTP class
                val request = TOTPVerifyRequest(
                    secret = secret,
                    code = code,
                    tolerance = 1
                )

                val result = apiService.verifyTOTP(request)
                result.fold(
                    onSuccess = { response ->
                        val isValid = response.data?.valid == true
                        _uiState.value = _uiState.value.copy(
                            isVerifying = false,
                            isVerified = isValid,
                            isValid = isValid,
                            error = if (!isValid) response.data?.message ?: "Invalid TOTP code" else null
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

