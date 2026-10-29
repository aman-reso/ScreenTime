package com.app.screentime.feature.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.feature.preferences.domain.model.UserPreferences
import com.app.screentime.feature.preferences.domain.usecase.GetEditPreferencesUseCase
import com.app.screentime.feature.preferences.domain.usecase.ResetEditPreferencesUseCase
import com.app.screentime.feature.preferences.domain.usecase.SaveEditPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditPreferencesUiState(
    val isLoading: Boolean = false,
    val preferences: UserPreferences = UserPreferences(),
    val message: String? = null,
    val error: String? = null
)

@HiltViewModel
class EditPreferencesViewModel @Inject constructor(
    private val getEditPreferencesUseCase: GetEditPreferencesUseCase,
    private val saveEditPreferencesUseCase: SaveEditPreferencesUseCase,
    private val resetEditPreferencesUseCase: ResetEditPreferencesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditPreferencesUiState(isLoading = true))
    val uiState: StateFlow<EditPreferencesUiState> = _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    fun loadPreferences() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getEditPreferencesUseCase()
                .onSuccess { prefs ->
                    _uiState.update { it.copy(isLoading = false, preferences = prefs) }
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isLoading = false, error = err.message) }
                }
        }
    }

    fun updateShowMe(value: String) {
        updatePreferencesInternal { it.copy(showMe = value) }
    }

    fun updateAgeRange(min: Float, max: Float) {
        updatePreferencesInternal { it.copy(minAge = min, maxAge = max) }
    }

    fun updateMaxDistance(distanceKm: Float) {
        updatePreferencesInternal { it.copy(maxDistanceKm = distanceKm) }
    }

    fun updateGlobal(enabled: Boolean) {
        updatePreferencesInternal { it.copy(isGlobal = enabled) }
    }

    fun updateReligion(value: String) {
        updatePreferencesInternal { it.copy(religion = value) }
    }

    fun updateHeightRange(value: String) {
        val (minH, maxH) = com.app.screentime.feature.preferences.data.repository.EditPreferencesRepositoryImpl.parseHeightRange(value)
        updatePreferencesInternal {
            it.copy(
                heightRange = value,
                minHeight = minH ?: it.minHeight,
                maxHeight = maxH ?: it.maxHeight
            )
        }
    }

    fun updateEducation(value: String) {
        updatePreferencesInternal { it.copy(education = value) }
    }

    fun updateDrinking(value: String) {
        updatePreferencesInternal { it.copy(drinking = value) }
    }

    fun updateSmoking(value: String) {
        updatePreferencesInternal { it.copy(smoking = value) }
    }

    fun updateChildren(value: String) {
        updatePreferencesInternal { it.copy(children = value) }
    }

    fun resetPreferences(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            resetEditPreferencesUseCase()
                .onSuccess { defaultPrefs ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            preferences = defaultPrefs,
                            message = "Preferences reset to default"
                        )
                    }
                    onComplete?.invoke()
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isLoading = false, error = err.message) }
                }
        }
    }

    fun savePreferences(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            saveJob?.cancel()
            saveEditPreferencesUseCase(_uiState.value.preferences)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "Preferences updated successfully"
                        )
                    }
                    onComplete?.invoke()
                }
                .onFailure { err ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = err.message ?: "Failed to update preferences"
                        )
                    }
                }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null, error = null) }
    }

    private var saveJob: kotlinx.coroutines.Job? = null

    private fun updatePreferencesInternal(transform: (UserPreferences) -> UserPreferences) {
        val updated = transform(_uiState.value.preferences)
        _uiState.update { it.copy(preferences = updated) }
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            kotlinx.coroutines.delay(300)
            saveEditPreferencesUseCase(updated)
        }
    }
}

