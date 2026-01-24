package com.app.screentime.controlcenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.controlcenter.repository.ControlCenterRepository
import com.app.screentime.network.model.AllowedUser
import com.app.screentime.utils.DateUtils
import com.app.screentime.analytics.AnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ControlCenterUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val allowedUsers: List<AllowedUser> = emptyList(),
    val accessibleUsers: List<String> = emptyList(),
    val error: String? = null,
    val isAdding: Boolean = false,
    val isRemoving: Set<String> = emptySet() // Track usernames being removed
)

@HiltViewModel
class ControlCenterViewModel @Inject constructor(
    private val repository: ControlCenterRepository,
    private val analyticsUseCase: AnalyticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ControlCenterUiState())
    val uiState: StateFlow<ControlCenterUiState> = _uiState.asStateFlow()

    init {
        analyticsUseCase.trackControlCenter()
    }

    /**
     * Load all data in parallel (control panel + accessible users)
     */
    private fun loadData(isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = _uiState.value.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                error = null
            )

            // Load both APIs in parallel using async
            val controlPanelDeferred = async { repository.getControlPanel() }.await()
            val accessibleUsersDeferred = async { repository.getAccessibleUsers() }.await()

            controlPanelDeferred.fold(
                onSuccess = { response ->
                    val allowedUsers = response.activeSessions.map { session ->
                        AllowedUser(
                            username = session.requestingUsername,
                            addedAt = DateUtils.format(session.verifiedAt, "MMM dd, yyyy HH:mm"),
                            expiresAt = session.expiresAt,
                            duration = (session.remainingSeconds * 1000).toLong(),
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        allowedUsers = allowedUsers,
                        error = null,
                        accessibleUsers = accessibleUsersDeferred.getOrNull() ?: emptyList()
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = exception.message ?: "Failed to load control panel"
                    )
                }
            )
        }
    }

    /**
     * Load control panel data (active TOTP sessions)
     * Public method for initial load
     */
    fun loadControlPanel() {
        loadData(isRefresh = false)
    }

    /**
     * Refresh data (for pull-to-refresh)
     */
    fun refresh() {
        loadData(isRefresh = true)
    }

    /**
     * Grant access to a user (add new user via TOTP)
     */
    fun grantAccess(username: String) {
        if (username.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAdding = true, error = null)
            repository.grantAccess(username.trim()).fold(
                onSuccess = {
                    // Reload control panel to get updated list
                    loadControlPanel()
                    _uiState.value = _uiState.value.copy(isAdding = false)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        error = exception.message ?: "Failed to grant access"
                    )
                }
            )
        }
    }

    /**
     * Revoke access from a user (remove user via TOTP)
     */
    fun revokeAccess(username: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRemoving = _uiState.value.isRemoving + username,
                error = null
            )
            repository.revokeAccess(username).fold(
                onSuccess = {
                    // Reload control panel to get updated list
                    loadControlPanel()
                    _uiState.value = _uiState.value.copy(
                        isRemoving = _uiState.value.isRemoving - username
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRemoving = _uiState.value.isRemoving - username,
                        error = exception.message ?: "Failed to revoke access"
                    )
                }
            )
        }
    }

    /**
     * Extend access for a user (update duration via TOTP)
     * @param username The username to extend access for
     * @param duration The additional duration in milliseconds to add (null for permanent - not supported by extend API)
     */
    fun extendAccess(username: String, duration: Long?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRemoving = _uiState.value.isRemoving + username,
                error = null
            )

            // If duration is null (permanent), we can't use extend API
            // For now, just reload the panel
            if (duration == null) {
                loadControlPanel()
                _uiState.value = _uiState.value.copy(
                    isRemoving = _uiState.value.isRemoving - username
                )
                return@launch
            }

            // Convert duration from milliseconds to seconds
            // The duration parameter represents how much time to ADD to the current expiry
            val additionalSeconds = duration / 1000

            repository.extendAccess(username, additionalSeconds).fold(
                onSuccess = {
                    // Reload control panel to get updated list
                    loadControlPanel()
                    _uiState.value = _uiState.value.copy(
                        isRemoving = _uiState.value.isRemoving - username
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRemoving = _uiState.value.isRemoving - username,
                        error = exception.message ?: "Failed to extend access"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

