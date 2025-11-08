package com.app.screentime.consent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.consent.mapper.ConsentMapper
import com.app.screentime.consent.model.ConsentUiModel
import com.app.screentime.consent.usecase.ConsentUseCase
import com.app.screentime.network.model.ApiConsentItem
import com.app.screentime.preferences.usecase.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsentViewModel @Inject constructor(
    private val consentUseCase: ConsentUseCase,
    private val consentMapper: ConsentMapper,
    private val preferencesUseCase: PreferencesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsentUiState())
    val uiState: StateFlow<ConsentUiState> = _uiState.asStateFlow()

    init {
        loadConsents()
    }

    /**
     * Load consents from API
     */
    fun loadConsents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            consentUseCase.getConsents().fold(
                onSuccess = { apiResponse ->
                    if (apiResponse.success == true && apiResponse.data != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            consentItems = apiResponse.data
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = apiResponse.message ?: "Failed to load consents"
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load consents"
                    )
                }
            )
        }
    }

    /**
     * Update consent values (list of boolean values)
     */
    fun updateConsentValues(values: List<Boolean>) {
        // Store consent values if needed for future use
        // Currently we don't save individual consents, only track if screen was shown
        _uiState.value = _uiState.value.copy(isSubmitted = true)
    }

    /**
     * Mark consent screen as shown
     */
    fun markConsentScreenShown() {
        preferencesUseCase.markConsentSheetShown()
        _uiState.value = _uiState.value.copy(isSubmitted = true)
    }

    /**
     * Clear error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Reset submission status
     */
    fun resetSubmission() {
        _uiState.value = _uiState.value.copy(isSubmitted = false)
    }
}

data class ConsentUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val consent: ConsentUiModel? = null,
    val consentItems: List<ApiConsentItem> = emptyList(),
    val error: String? = null
)

