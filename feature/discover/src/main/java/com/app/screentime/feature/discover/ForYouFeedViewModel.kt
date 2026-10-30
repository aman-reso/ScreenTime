package com.app.screentime.feature.discover

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.ads.NativeAdCardManager
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

data class SwipedAction(
    val profile: ModelProfile,
    val isLike: Boolean
)

data class ForYouFeedUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val currentPage: Int = 1,
    val models: List<ModelProfile> = emptyList(),
    val deckItems: List<DiscoveryDeckItem> = emptyList(),
    val likedModelIds: Set<String> = emptySet(),
    val isMatched: Boolean = false,
    val matchedModel: ModelProfile? = null,
    val error: String? = null,
    val canUndo: Boolean = false,
    val quotaLimit: com.app.screentime.core.model.QuotaLimitInfo? = null
)

@HiltViewModel
class ForYouFeedViewModel @Inject constructor(
    private val getDiscoveryDeckUseCase: GetDiscoveryDeckUseCase,
    private val likeProfileUseCase: LikeProfileUseCase,
    private val superLikeProfileUseCase: SuperLikeProfileUseCase,
    private val dislikeProfileUseCase: DislikeProfileUseCase,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForYouFeedUiState(isLoading = true))
    val uiState: StateFlow<ForYouFeedUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null
    private val swipeHistory = mutableListOf<SwipedAction>()

    init {
        loadDeck(force = true)
        viewModelScope.launch {
            quotaLimitManager.homeQuota.collect { quota ->
                _uiState.value = _uiState.value.copy(quotaLimit = quota)
            }
        }
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
                swipeHistory.clear()
                val deck = profiles.map { DiscoveryDeckItem.ProfileCard(it) }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    models = profiles,
                    deckItems = deck,
                    currentPage = 1,
                    canLoadMore = profiles.size >= 10,
                    error = null,
                    canUndo = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    models = emptyList(),
                    deckItems = emptyList(),
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
                swipeHistory.clear()
                val deck = profiles.map { DiscoveryDeckItem.ProfileCard(it) }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    models = profiles,
                    deckItems = deck,
                    currentPage = 1,
                    canLoadMore = profiles.size >= 10,
                    error = null,
                    canUndo = false
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
                    val newDeckItems = distinctNew.map { DiscoveryDeckItem.ProfileCard(it) }
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        models = state.models + distinctNew,
                        deckItems = state.deckItems + newDeckItems,
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

    fun onLikeById(id: String, context: Context? = null, onMutualMatch: () -> Unit = {}) {
        val target = _uiState.value.models.find { it.id == id } ?: return
        swipeHistory.add(SwipedAction(target, isLike = true))
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

        var updatedDeck = _uiState.value.deckItems.filterNot { it.id == id }
        if (context != null && NativeAdCardManager.onProfileSwiped()) {
            val preloadedAd = NativeAdCardManager.consumePreloadedAd(context)
            if (preloadedAd != null) {
                val insertIndex = if (updatedDeck.size > 1) 1 else 0
                updatedDeck = updatedDeck.toMutableList().apply {
                    add(insertIndex, DiscoveryDeckItem.AdCard(preloadedAd))
                }
            }
        }

        _uiState.value = _uiState.value.copy(
            models = _uiState.value.models.filterNot { it.id == id },
            deckItems = updatedDeck,
            likedModelIds = _uiState.value.likedModelIds + id,
            canUndo = true
        )
    }

    fun onSuperLikeById(id: String, context: Context? = null) {
        val target = _uiState.value.models.find { it.id == id } ?: return
        swipeHistory.add(SwipedAction(target, isLike = true))
        viewModelScope.launch {
            superLikeProfileUseCase(id)
        }

        var updatedDeck = _uiState.value.deckItems.filterNot { it.id == id }
        if (context != null && NativeAdCardManager.onProfileSwiped()) {
            val preloadedAd = NativeAdCardManager.consumePreloadedAd(context)
            if (preloadedAd != null) {
                val insertIndex = if (updatedDeck.size > 1) 1 else 0
                updatedDeck = updatedDeck.toMutableList().apply {
                    add(insertIndex, DiscoveryDeckItem.AdCard(preloadedAd))
                }
            }
        }

        _uiState.value = _uiState.value.copy(
            models = _uiState.value.models.filterNot { it.id == id },
            deckItems = updatedDeck,
            likedModelIds = _uiState.value.likedModelIds + id,
            isMatched = true,
            matchedModel = target,
            canUndo = true
        )
    }

    fun onDislikeById(id: String, context: Context? = null) {
        val target = _uiState.value.models.find { it.id == id }
        if (target != null) {
            swipeHistory.add(SwipedAction(target, isLike = false))
        }
        viewModelScope.launch {
            dislikeProfileUseCase(id)
        }

        var updatedDeck = _uiState.value.deckItems.filterNot { it.id == id }
        if (context != null && NativeAdCardManager.onProfileSwiped()) {
            val preloadedAd = NativeAdCardManager.consumePreloadedAd(context)
            if (preloadedAd != null) {
                val insertIndex = if (updatedDeck.size > 1) 1 else 0
                updatedDeck = updatedDeck.toMutableList().apply {
                    add(insertIndex, DiscoveryDeckItem.AdCard(preloadedAd))
                }
            }
        }

        _uiState.value = _uiState.value.copy(
            models = _uiState.value.models.filterNot { it.id == id },
            deckItems = updatedDeck,
            canUndo = swipeHistory.isNotEmpty()
        )
    }

    /**
     * Handles when a native ad card is swiped away (either left to pass or right to interact).
     */
    fun onAdSwiped(adId: String, context: Context) {
        val adItem = _uiState.value.deckItems.find { it.id == adId } as? DiscoveryDeckItem.AdCard
        adItem?.nativeAd?.destroy()
        _uiState.value = _uiState.value.copy(
            deckItems = _uiState.value.deckItems.filterNot { it.id == adId }
        )
        NativeAdCardManager.preloadAd(context)
    }

    fun onUndo() {
        if (swipeHistory.isEmpty()) return
        val lastAction = swipeHistory.removeAt(swipeHistory.lastIndex)
        val restoredProfile = lastAction.profile
        val currentModels = _uiState.value.models
        // Insert restored profile at the top of the deck
        val newModels = listOf(restoredProfile) + currentModels.filterNot { it.id == restoredProfile.id }
        val newDeck = listOf(DiscoveryDeckItem.ProfileCard(restoredProfile)) + _uiState.value.deckItems.filterNot { it.id == restoredProfile.id }
        val newLikedIds = if (lastAction.isLike) {
            _uiState.value.likedModelIds - restoredProfile.id
        } else {
            _uiState.value.likedModelIds
        }
        _uiState.value = _uiState.value.copy(
            models = newModels,
            deckItems = newDeck,
            likedModelIds = newLikedIds,
            canUndo = swipeHistory.isNotEmpty()
        )
    }

    fun dismissMatchDialog() {
        _uiState.value = _uiState.value.copy(isMatched = false, matchedModel = null)
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.value.deckItems.filterIsInstance<DiscoveryDeckItem.AdCard>().forEach {
            it.nativeAd.destroy()
        }
        NativeAdCardManager.clear()
    }
}
