package com.app.screentime.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ProfilePrompt
import com.app.screentime.core.model.User
import com.app.screentime.core.network.api.DiscoveryApi
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.feature.profile.domain.usecase.FetchUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileDetailUiState(
    val isLoading: Boolean = false,
    val profile: ProfileDetailUiModel = ProfileDetailUiModel(),
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isActionInProgress: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val fetchUserProfileUseCase: FetchUserProfileUseCase,
    private val discoveryApi: DiscoveryApi,
    private val winterApi: WinterApi,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    fun loadProfile(targetUserId: String, initialName: String = "", initialAge: Int = 24) {
        val resolvedTargetId = targetUserId.trim()
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            profile = _uiState.value.profile.copy(
                userId = resolvedTargetId,
                photos = emptyList(),
                displayName = initialName.ifBlank { _uiState.value.profile.displayName },
                headerTitle = if (initialAge > 0 && initialName.isNotBlank()) "$initialName, $initialAge" else initialName.ifBlank { _uiState.value.profile.headerTitle }
            ),
            error = null
        )

        viewModelScope.launch {
            fetchUserProfileUseCase(resolvedTargetId.takeIf { it.isNotBlank() && it != "me" })
                .onSuccess { user ->
                    val uiModel = user.toProfileDetailUiModel(
                        fallbackUserId = resolvedTargetId,
                        fallbackName = initialName,
                        fallbackAge = initialAge
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = uiModel,
                        error = null
                    )
                    try {
                        val token = preferencesManager.getToken().orEmpty()
                        if (token.isNotBlank()) {
                            val favorites = winterApi.getFavorites(token)
                            if (favorites.models.any { it.id == resolvedTargetId }) {
                                _uiState.value = _uiState.value.copy(isLiked = true)
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
                .onFailure { err ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = err.localizedMessage ?: "Failed to load profile"
                    )
                }
        }
    }

    fun likeProfile(onResult: (isMatch: Boolean, message: String) -> Unit) {
        val targetId = _uiState.value.profile.userId
        if (targetId.isBlank() || _uiState.value.isActionInProgress) return

        _uiState.value = _uiState.value.copy(isActionInProgress = true)

        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken().orEmpty()
                val currentUserId = resolveUserId(token)

                val response = discoveryApi.interact(
                    token = token,
                    targetUserId = targetId,
                    action = "like",
                    userId = currentUserId
                )

                try {
                    winterApi.addFavorite(token, targetId)
                } catch (_: Exception) {
                }

                val isMatch = response.data?.is_match ?: false
                val msg = if (isMatch) "It's a Match! 🎉" else "Liked!"

                _uiState.value = _uiState.value.copy(
                    isLiked = true,
                    isDisliked = false,
                    isActionInProgress = false
                )
                onResult(isMatch, msg)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isActionInProgress = false)
                onResult(false, "Failed to like profile")
            }
        }
    }

    fun dislikeProfile(onComplete: () -> Unit) {
        val targetId = _uiState.value.profile.userId
        if (targetId.isBlank() || _uiState.value.isActionInProgress) {
            onComplete()
            return
        }

        _uiState.value = _uiState.value.copy(isActionInProgress = true)

        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken().orEmpty()
                val currentUserId = resolveUserId(token)

                // 1. Call discovery interact API with action "dismiss"
                discoveryApi.interact(
                    token = token,
                    targetUserId = targetId,
                    action = "dismiss",
                    userId = currentUserId
                )

                _uiState.value = _uiState.value.copy(
                    isDisliked = true,
                    isLiked = false,
                    isActionInProgress = false
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isActionInProgress = false)
            } finally {
                onComplete()
            }
        }
    }

    fun reportProfile(reason: String = "Inappropriate behavior", onComplete: (Boolean) -> Unit) {
        val targetId = _uiState.value.profile.userId
        if (targetId.isBlank()) {
            onComplete(false)
            return
        }

        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken().orEmpty()
                val success = winterApi.reportUser(token, targetId, reason)
                onComplete(success)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun blockProfile(onComplete: (Boolean) -> Unit) {
        val targetId = _uiState.value.profile.userId
        if (targetId.isBlank()) {
            onComplete(false)
            return
        }

        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken().orEmpty()
                val currentUserId = resolveUserId(token)
                // Dismiss from discovery
                try {
                    discoveryApi.interact(
                        token = token,
                        targetUserId = targetId,
                        action = "dismiss",
                        userId = currentUserId
                    )
                } catch (_: Exception) {
                }
                val success = winterApi.reportUser(token, targetId, "Blocked by user")
                onComplete(success)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    private fun resolveUserId(token: String): String? {
        val uid = preferencesManager.getUserId()
        if (!uid.isNullOrBlank()) return uid
        if (token.startsWith("token_")) {
            return token.removePrefix("token_")
        }
        return null
    }
}
