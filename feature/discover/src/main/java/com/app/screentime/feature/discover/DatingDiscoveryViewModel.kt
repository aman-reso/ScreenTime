package com.app.screentime.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.usecase.DislikeProfileUseCase
import com.app.screentime.feature.discover.domain.usecase.GetDiscoveryDeckUseCase
import com.app.screentime.feature.discover.domain.usecase.LikeProfileUseCase
import com.app.screentime.feature.discover.domain.usecase.SuperLikeProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DatingDiscoveryUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val currentPage: Int = 1,
    val models: List<ModelProfile> = emptyList(),
    val currentIndex: Int = 0,
    val likedModelIds: Set<String> = emptySet(),
    val isMatched: Boolean = false,
    val matchedModel: ModelProfile? = null,
    val currentStorySegment: Int = 0,
    val error: String? = null
) {
    val currentModel: ModelProfile?
        get() = models.getOrNull(currentIndex)

    val hasMoreProfiles: Boolean
        get() = currentIndex < models.size
}

typealias DiscoverViewModel = DatingDiscoveryViewModel
typealias DiscoverUiState = DatingDiscoveryUiState

/**
 * Presentation-layer ViewModel for dating discovery card swiping.
 * Decoupled from data sources through Domain Use Cases.
 */
@HiltViewModel
class DatingDiscoveryViewModel @Inject constructor(
    private val getDiscoveryDeckUseCase: GetDiscoveryDeckUseCase,
    private val likeProfileUseCase: LikeProfileUseCase,
    private val superLikeProfileUseCase: SuperLikeProfileUseCase,
    private val dislikeProfileUseCase: DislikeProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DatingDiscoveryUiState(isLoading = true))
    val uiState: StateFlow<DatingDiscoveryUiState> = _uiState.asStateFlow()

    init {
        loadDiscoveryDeck()
    }

    /**
     * Loads discovery profiles through the domain UseCase.
     */
    fun loadDiscoveryDeck() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isLoadingMore = false,
                currentPage = 1,
                canLoadMore = true,
                error = null
            )
            val result = getDiscoveryDeckUseCase(page = 1, limit = 10)
            result.onSuccess { profiles ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    models = profiles,
                    currentIndex = 0,
                    currentPage = 1,
                    canLoadMore = profiles.isNotEmpty(),
                    currentStorySegment = 0,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    models = emptyList(),
                    currentIndex = 0,
                    currentPage = 1,
                    canLoadMore = false,
                    currentStorySegment = 0,
                    error = error.localizedMessage ?: "Failed to load discovery deck"
                )
            }
        }
    }

    /**
     * Loads the next page of discovery suggestions when user scrolls near the end.
     */
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
                        canLoadMore = distinctNew.size >= 3
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

    fun onDislike() {
        val current = _uiState.value.currentModel ?: return
        val nextIdx = _uiState.value.currentIndex + 1

        viewModelScope.launch {
            dislikeProfileUseCase(current.id)
        }

        _uiState.value = _uiState.value.copy(
            currentIndex = nextIdx,
            currentStorySegment = 0
        )
    }

    fun onLike() {
        val current = _uiState.value.currentModel ?: return
        val nextIdx = _uiState.value.currentIndex + 1
        val updatedLikes = _uiState.value.likedModelIds + current.id

        viewModelScope.launch {
            val result = likeProfileUseCase(current.id)
            val isMatch = result.getOrDefault(false)
            if (isMatch) {
                _uiState.value = _uiState.value.copy(
                    isMatched = true,
                    matchedModel = current
                )
            }
        }

        _uiState.value = _uiState.value.copy(
            currentIndex = nextIdx,
            likedModelIds = updatedLikes,
            currentStorySegment = 0
        )
    }

    fun onSuperLike() {
        val current = _uiState.value.currentModel ?: return
        val nextIdx = _uiState.value.currentIndex + 1
        val updatedLikes = _uiState.value.likedModelIds + current.id

        viewModelScope.launch {
            superLikeProfileUseCase(current.id)
        }

        _uiState.value = _uiState.value.copy(
            currentIndex = nextIdx,
            likedModelIds = updatedLikes,
            currentStorySegment = 0,
            isMatched = true,
            matchedModel = current
        )
    }

    fun dismissMatchDialog() {
        _uiState.value = _uiState.value.copy(isMatched = false, matchedModel = null)
    }

    fun onLikeById(id: String) {
        val target = _uiState.value.models.find { it.id == id } ?: return
        viewModelScope.launch {
            val result = likeProfileUseCase(id)
            val isMatch = result.getOrDefault(true)
            if (isMatch) {
                _uiState.value = _uiState.value.copy(
                    isMatched = true,
                    matchedModel = target
                )
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

    fun nextStorySegment() {
        val current = _uiState.value.currentModel ?: return
        val totalSegments = current.galleryUrls.size.coerceAtLeast(1)
        val nextSegment = (_uiState.value.currentStorySegment + 1) % totalSegments
        _uiState.value = _uiState.value.copy(currentStorySegment = nextSegment)
    }

    fun resetDeck() {
        _uiState.value = _uiState.value.copy(currentIndex = 0, currentStorySegment = 0)
        loadDiscoveryDeck()
    }
}
