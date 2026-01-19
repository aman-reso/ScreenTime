package com.app.screentime.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.network.model.UserSearchResult
import com.app.screentime.profile.repository.TOTPRepository
import com.app.screentime.search.usecase.SearchUseCase
import com.app.screentime.analytics.AnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val totpRepository: TOTPRepository,
    private val analyticsUseCase: AnalyticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        analyticsUseCase.trackSearchUsernameScreen()
    }

    fun trackTOTPVerify() {
        analyticsUseCase.trackTOTPVerify()
    }

    /**
     * Search users by query string
     * @param query Search query (username)
     */
    fun searchUsers(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isLoading = false,
                error = "Search query cannot be empty"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                searchResults = emptyList()
            )

            searchUseCase.searchUsers(query).fold(
                onSuccess = { users ->
                    _uiState.value = _uiState.value.copy(
                        searchResults = users,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to search users: ${exception.message}",
                        searchResults = emptyList()
                    )
                }
            )
        }
    }


    /**
     * Clear search results
     */
    fun clearSearch() {
        _uiState.value = SearchUiState()
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Check TOTP status for a username
     * Returns true if access is already granted, false otherwise
     */
    suspend fun checkTOTPStatus(username: String?): Boolean {
        if (username.isNullOrBlank()){
            return false
        }
        return totpRepository.getTOTPStatus(username).fold(
            onSuccess = { response ->
                response.data?.hasAccess == true
            },
            onFailure = {
                false
            }
        )
    }
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchResults: List<UserSearchResult> = emptyList(),
    val error: String? = null
)
