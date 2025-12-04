package com.app.screentime.consent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.consent.mapper.ConsentMapper
import com.app.screentime.consent.model.ConsentUiModel
import com.app.screentime.consent.usecase.ConsentUseCase
import com.app.screentime.network.model.ApiConsentItem
import com.app.screentime.network.model.ConsentSubmissionItem
import com.app.screentime.network.model.ConsentSubmissionRequest
import com.app.screentime.preferences.PreferencesManager
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
    private val preferencesUseCase: PreferencesUseCase,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsentUiState())
    val uiState: StateFlow<ConsentUiState> = _uiState.asStateFlow()

    init {
        loadConsents()
    }

    /**
     * Load consents from API
     * Note: We don't mark as displayed here - only when user dismisses or submits
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
                        // Even if API fails, we still show the sheet (with error message)
                        // User can dismiss it, which will mark it as displayed
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = apiResponse.message ?: "Failed to load consents"
                        )
                    }
                },
                onFailure = { exception ->
                    // Even if loading fails, we still show the sheet (with error message)
                    // User can dismiss it, which will mark it as displayed
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load consents"
                    )
                }
            )
        }
    }

    /**
     * Submit consents to API
     * @param consentValues Map of consent item index to boolean value
     */
    fun submitConsents(consentValues: Map<Int, Boolean>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)

            val deviceId = preferencesManager.getUserId()
            if (deviceId.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = "Device not registered"
                )
                return@launch
            }

            // Map consent values to submission items
            val consentItems = _uiState.value.consentItems
            val submissionItems = consentItems.mapIndexed { index, item ->
                val value = consentValues[index] ?: item.isMandatory
                ConsentSubmissionItem(
                    id = item.id,
                    value = if (value) "accepted" else "rejected"
                )
            }

            val request = ConsentSubmissionRequest(
                consents = submissionItems
            )

            consentUseCase.submitConsents(request).fold(
                onSuccess = { apiResponse ->
                    // Mark consent as given (allows data sync to proceed)
                    preferencesUseCase.markConsentSheetShown()
                    if (apiResponse.success == true) {
                        _uiState.value = _uiState.value.copy(
                            isSubmitting = false,
                            isSubmitted = true,
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isSubmitting = false,
                            isSubmitted = true, // Still mark as submitted even if API failed
                            error = apiResponse.message ?: "Failed to submit consents"
                        )
                    }
                },
                onFailure = { exception ->
                    // Mark consent as given even on failure (allows data sync to proceed)
                    preferencesUseCase.markConsentSheetShown()
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        isSubmitted = true, // Mark as submitted even on failure
                        error = exception.message ?: "Failed to submit consents"
                    )
                }
            )
        }
    }

    /**
     * Update consent values (list of boolean values) - legacy method
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
    
    /**
     * Submit hardcoded consents to API
     * @param consentItems List of hardcoded consent items with their values
     */
    fun submitHardcodedConsents(consentItems: List<Pair<String, Boolean>>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)

            val deviceId = preferencesManager.getUserId()
            if (deviceId.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = "Device not registered"
                )
                return@launch
            }

            // Map hardcoded consent items to API format
            // Using integer IDs that map to consent names
            val idMapping = mapOf(
                "data_collection" to 1,
                "data_sharing" to 2,
                "analytics" to 3
            )
            
            val submissionItems = consentItems.map { (id, value) ->
                ConsentSubmissionItem(
                    id = idMapping[id] ?: 0,
                    value = if (value) "accepted" else "rejected"
                )
            }

            val request = ConsentSubmissionRequest(
                consents = submissionItems
            )

            consentUseCase.submitConsents(request).fold(
                onSuccess = { apiResponse ->
                    // Mark consent as given (allows usage stats and challenge data sync to proceed)
                    preferencesUseCase.markConsentSheetShown()
                    if (apiResponse.success == true) {
                        _uiState.value = _uiState.value.copy(
                            isSubmitting = false,
                            isSubmitted = true,
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isSubmitting = false,
                            isSubmitted = true, // Still mark as submitted even if API failed
                            error = apiResponse.message ?: "Failed to submit consents"
                        )
                    }
                },
                onFailure = { exception ->
                    // Mark consent as given even on failure (allows data sync to proceed)
                    preferencesUseCase.markConsentSheetShown()
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        isSubmitted = true, // Mark as submitted even on failure
                        error = exception.message ?: "Failed to submit consents"
                    )
                }
            )
        }
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

