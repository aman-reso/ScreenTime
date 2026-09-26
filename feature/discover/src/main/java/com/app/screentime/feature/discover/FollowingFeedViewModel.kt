package com.app.screentime.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.usecase.GetDiscoveryDeckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FollowingFeedUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val currentPage: Int = 1,
    val followingProfiles: List<ModelProfile> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FollowingFeedViewModel @Inject constructor(
    private val getDiscoveryDeckUseCase: GetDiscoveryDeckUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowingFeedUiState(isLoading = true))
    val uiState: StateFlow<FollowingFeedUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        loadFollowing(force = true)
    }

    fun loadFollowing(force: Boolean = false) {
        if (fetchJob?.isActive == true && !force) return
        fetchJob = viewModelScope.launch {
            val showLoading = _uiState.value.followingProfiles.isEmpty() || force
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
                    followingProfiles = profiles,
                    currentPage = 1,
                    canLoadMore = profiles.size >= 10,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    followingProfiles = emptyList(),
                    canLoadMore = false,
                    error = error.localizedMessage ?: "Failed to load following feed"
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
                val existingIds = state.followingProfiles.map { it.id }.toSet()
                val distinctNew = newProfiles.filterNot { existingIds.contains(it.id) }
                if (distinctNew.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        followingProfiles = state.followingProfiles + distinctNew,
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
            val result = getDiscoveryDeckUseCase(page = 1, limit = 10)
            result.onSuccess { profiles ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    followingProfiles = profiles,
                    currentPage = 1,
                    canLoadMore = profiles.size >= 10,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = error.localizedMessage ?: "Failed to refresh following feed"
                )
            }
        }
    }

    fun unfollow(profileId: String) {
        _uiState.value = _uiState.value.copy(
            followingProfiles = _uiState.value.followingProfiles.filterNot { it.id == profileId }
        )
    }
}
