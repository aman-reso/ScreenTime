package com.app.screentime.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.usecase.DislikeProfileUseCase
import com.app.screentime.feature.discover.domain.usecase.GetDiscoveryDeckUseCase
import com.app.screentime.feature.discover.domain.usecase.LikeProfileUseCase
import com.app.screentime.feature.discover.domain.usecase.SuperLikeProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForYouFeedUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val currentPage: Int = 1,
    val models: List<ModelProfile> = emptyList(),
    val likedModelIds: Set<String> = emptySet(),
    val isMatched: Boolean = false,
    val matchedModel: ModelProfile? = null,
    val error: String? = null
)

@HiltViewModel
class ForYouFeedViewModel @Inject constructor(
    private val getDiscoveryDeckUseCase: GetDiscoveryDeckUseCase,
    private val likeProfileUseCase: LikeProfileUseCase,
    private val superLikeProfileUseCase: SuperLikeProfileUseCase,
    private val dislikeProfileUseCase: DislikeProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForYouFeedUiState(isLoading = true))
    val uiState: StateFlow<ForYouFeedUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        loadDeck(force = true)
    }

    fun loadDeck(force: Boolean = false) {
        if (fetchJob?.isActive == true && !force) return
        fetchJob = viewModelScope.launch {
            val showLoading = _uiState.value.models.isEmpty() || force
            if (showLoading) {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    currentPage = 1,
                    canLoadMore = true,
                    error = null
                )
            }
            val result = getDiscoveryDeckUseCase(page = 1, limit = 10)
            result.onSuccess { profiles ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    models = profiles,
                    currentPage = 1,
                    canLoadMore = profiles.size >= 10,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    models = emptyList(),
                    currentPage = 1,
                    canLoadMore = false,
                    error = error.localizedMessage ?: "Failed to load discovery deck"
                )
            }
        }
    }

    fun resetDeck() {
        loadDeck(force = true)
    }

    fun refresh() {
        if (_uiState.value.isRefreshing) return
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            val result = getDiscoveryDeckUseCase(page = 1, limit = 10)
            result.onSuccess { profiles ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    models = profiles,
                    currentPage = 1,
                    canLoadMore = profiles.size >= 10,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = error.localizedMessage ?: "Failed to refresh discovery deck"
                )
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.canLoadMore) return

        val nextPage = state.currentPage + 1
        _uiState.value = state.copy(isLoadingMore = true)

        viewModelScope.launch {
            val result = getDiscoveryDeckUseCase(page = nextPage, limit = 10)
            result.onSuccess { newProfiles ->
                val existingIds = state.models.map { it.id }.toSet()
                val distinctNew = newProfiles.filterNot { existingIds.contains(it.id) }
                if (distinctNew.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        models = state.models + distinctNew,
                        currentPage = nextPage,
                        canLoadMore = distinctNew.size >= 10
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        canLoadMore = false
                    )
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    canLoadMore = false
                )
            }
        }
    }

    fun onLikeById(id: String, onMutualMatch: () -> Unit = {}) {
        val target = _uiState.value.models.find { it.id == id } ?: return
        if (id in _uiState.value.likedModelIds) {
            _uiState.value = _uiState.value.copy(
                likedModelIds = _uiState.value.likedModelIds - id
            )
            return
        }
        viewModelScope.launch {
            val result = likeProfileUseCase(id)
            val isMatch = result.getOrDefault(false)
            if (isMatch) {
                _uiState.value = _uiState.value.copy(
                    isMatched = true,
                    matchedModel = target
                )
                onMutualMatch()
            }
        }
        _uiState.value = _uiState.value.copy(
            likedModelIds = _uiState.value.likedModelIds + id
        )
    }

    fun onDislikeById(id: String) {
        viewModelScope.launch {
            dislikeProfileUseCase(id)
        }
        _uiState.value = _uiState.value.copy(
            models = _uiState.value.models.filterNot { it.id == id }
        )
    }

    fun dismissMatchDialog() {
        _uiState.value = _uiState.value.copy(isMatched = false, matchedModel = null)
    }
}
