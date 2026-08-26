package com.app.screentime.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.usecase.GetModelDetailsUseCase
import com.app.screentime.feature.discover.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModelProfileUiState(
    val model: ModelProfile? = null,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ModelProfileViewModel @Inject constructor(
    private val getModelDetailsUseCase: GetModelDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModelProfileUiState())
    val uiState: StateFlow<ModelProfileUiState> = _uiState.asStateFlow()

    fun loadModel(modelId: String, initialName: String) {
        _uiState.value = _uiState.value.copy(
            model = ModelProfile(id = modelId, name = initialName),
            isLoading = true,
            error = null
        )
        viewModelScope.launch {
            val profile = getModelDetailsUseCase(modelId)
            if (profile != null) {
                _uiState.value = _uiState.value.copy(
                    model = profile,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun toggleFavorite() {
        val currentModel = _uiState.value.model ?: return
        val currentFav = _uiState.value.isFavorite
        _uiState.value = _uiState.value.copy(isFavorite = !currentFav)
        viewModelScope.launch {
            toggleFavoriteUseCase(currentModel.id, currentFav)
        }
    }
}
