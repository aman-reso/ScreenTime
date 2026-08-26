package com.app.screentime.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.feature.auth.domain.usecase.CheckAuthStatusUseCase
import com.app.screentime.feature.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val phone: String = "",
    val name: String = "",
    val role: String = "user", // "user" or "model"
    val isSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = checkAuthStatusUseCase.isLoggedInFlow

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onPhoneChanged(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone, error = null)
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name, error = null)
    }

    fun onRoleSelected(role: String) {
        _uiState.value = _uiState.value.copy(role = role)
    }

    fun submitAuth() {
        val current = _uiState.value
        if (current.phone.length < 10) {
            _uiState.value = current.copy(error = "Please enter a valid 10-digit phone number")
            return
        }
        if (current.name.isBlank()) {
            _uiState.value = current.copy(error = "Please enter your name")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = loginUseCase(current.phone, current.name, current.role)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Authentication failed")
            }
        }
    }
}
