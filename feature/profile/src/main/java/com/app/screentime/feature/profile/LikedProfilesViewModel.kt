package com.app.screentime.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.profile.domain.usecase.GetLikedProfilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LikedProfilesUiState(
    val isLoading: Boolean = false,
    val profiles: List<ModelProfile> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class LikedProfilesViewModel @Inject constructor(
    private val getLikedProfilesUseCase: GetLikedProfilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LikedProfilesUiState())
    val uiState: StateFlow<LikedProfilesUiState> = _uiState.asStateFlow()

    fun loadLikedProfiles() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            getLikedProfilesUseCase()
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
                        error = err.localizedMessage ?: "Failed to load liked profiles"
                    )
                }
        }
    }
}
