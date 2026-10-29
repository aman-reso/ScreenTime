package com.app.screentime.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.profile.domain.usecase.GetWhoLikedMeProfilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WhoLikedMeUiState(
    val isLoading: Boolean = false,
    val profiles: List<ModelProfile> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class WhoLikedMeViewModel @Inject constructor(
    private val getWhoLikedMeProfilesUseCase: GetWhoLikedMeProfilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WhoLikedMeUiState())
    val uiState: StateFlow<WhoLikedMeUiState> = _uiState.asStateFlow()

    fun loadProfiles() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            getWhoLikedMeProfilesUseCase()
                .onSuccess { profiles ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profiles = profiles,
                        error = null
                    )
                }
                .onFailure { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Failed to load profiles"
                    )
                }
        }
    }
}
