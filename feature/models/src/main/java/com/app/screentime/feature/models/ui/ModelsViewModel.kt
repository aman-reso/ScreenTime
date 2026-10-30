package com.app.screentime.feature.models.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.feature.models.data.ModelsRepository
import com.app.screentime.feature.models.model.ModelGirl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModelsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val models: List<ModelGirl> = emptyList(),
    val isCurrentUserModel: Boolean = false,
    val error: String? = null,
    val rateUpdateMessage: String? = null
)

@HiltViewModel
class ModelsViewModel @Inject constructor(
    private val repository: ModelsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ModelsUiState(
            isLoading = true,
            isCurrentUserModel = repository.isCurrentUserModel()
        )
    )
    val uiState: StateFlow<ModelsUiState> = _uiState.asStateFlow()

    init {
        loadModels()
    }

    fun loadModels() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val res = repository.getModels()
            res.fold(
                onSuccess = { list ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        models = list,
                        error = null
                    )
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = err.message ?: "Failed to load models"
                    )
                }
            )
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            val res = repository.getModels()
            res.fold(
                onSuccess = { list ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        models = list,
                        error = null
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(isRefreshing = false)
                }
            )
        }
    }

    fun updateMyRates(audioRate: Int, videoRate: Int) {
        viewModelScope.launch {
            val res = repository.updateMyRates(audioRate, videoRate)
            res.fold(
                onSuccess = {
                    _uiState.value =
                        _uiState.value.copy(rateUpdateMessage = "Rates updated successfully")
                    refresh()
                },
                onFailure = { err ->
                    _uiState.value =
                        _uiState.value.copy(rateUpdateMessage = "Failed: ${err.message}")
                }
            )
        }
    }

    fun clearRateUpdateMessage() {
        _uiState.value = _uiState.value.copy(rateUpdateMessage = null)
    }
}
