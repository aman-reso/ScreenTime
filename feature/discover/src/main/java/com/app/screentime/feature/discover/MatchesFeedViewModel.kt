package com.app.screentime.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.DiscoveryMatch
import com.app.screentime.feature.discover.domain.usecase.GetMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchesFeedUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val currentPage: Int = 1,
    val matches: List<DiscoveryMatch> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MatchesFeedViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesFeedUiState(isLoading = true))
    val uiState: StateFlow<MatchesFeedUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        loadMatches(page = 1, force = true)
    }

    fun loadMatches(page: Int = 1, force: Boolean = false) {
        if (fetchJob?.isActive == true && !force) return
        fetchJob = viewModelScope.launch {
            val showLoading = _uiState.value.matches.isEmpty() || force
            if (showLoading) {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    currentPage = page,
                    error = null
                )
            }
            val result = getMatchesUseCase(page = page, limit = 10)
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    matches = list,
                    currentPage = page,
                    canLoadMore = list.size >= 10,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    matches = emptyList(),
                    canLoadMore = false,
                    error = error.localizedMessage ?: "Failed to load matches"
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
            val result = getMatchesUseCase(page = nextPage, limit = 10)
            result.onSuccess { newMatches ->
                val existingIds = state.matches.map { it.matchId }.toSet()
                val distinctNew = newMatches.filterNot { existingIds.contains(it.matchId) }
                if (distinctNew.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        matches = state.matches + distinctNew,
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

    fun refresh() {
        if (_uiState.value.isRefreshing) return
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            val result = getMatchesUseCase(page = 1, limit = 10)
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    matches = list,
                    currentPage = 1,
                    canLoadMore = list.size >= 10,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = error.localizedMessage ?: "Failed to refresh matches"
                )
            }
        }
    }
}
