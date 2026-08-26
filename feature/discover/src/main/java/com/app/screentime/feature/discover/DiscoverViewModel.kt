package com.app.screentime.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.usecase.GetModelsUseCase
import com.app.screentime.feature.discover.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val models: List<ModelProfile> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val favoriteModelIds: Set<String> = emptySet(),
    val error: String? = null
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getModelsUseCase: GetModelsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadInitialModels()
    }

    fun loadInitialModels() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                currentPage = 1,
                hasMorePages = true
            )
            val list = getModelsUseCase.getPaginated(page = 1, pageSize = 4)
            _uiState.value = _uiState.value.copy(
                models = list,
                isLoading = false,
                hasMorePages = list.isNotEmpty()
            )
        }
    }

    fun loadNextPage() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMorePages || _uiState.value.isLoading) return

        val nextPage = _uiState.value.currentPage + 1
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)
            val newModels = getModelsUseCase.getPaginated(page = nextPage, pageSize = 4)
            if (newModels.isNotEmpty()) {
                val currentIds = _uiState.value.models.map { it.id }.toSet()
                val distinctNew = newModels.filter { it.id !in currentIds }
                _uiState.value = _uiState.value.copy(
                    models = _uiState.value.models + distinctNew,
                    currentPage = nextPage,
                    isLoadingMore = false,
                    hasMorePages = newModels.size >= 4
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    hasMorePages = false
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onFilterSelected(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun toggleFavorite(modelId: String) {
        val currentFavs = _uiState.value.favoriteModelIds
        val isCurrentlyFav = currentFavs.contains(modelId)
        val updatedFavs = if (isCurrentlyFav) currentFavs - modelId else currentFavs + modelId
        _uiState.value = _uiState.value.copy(favoriteModelIds = updatedFavs)
        viewModelScope.launch {
            toggleFavoriteUseCase(modelId, isCurrentlyFav)
        }
    }
}
