package com.app.screentime.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.network.model.UserSearchResult
import com.app.screentime.search.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    /**
     * Search users by username
     */
    fun searchUsers(username: String) {
        if (username.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isLoading = false,
                error = "Username cannot be empty"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                searchResults = emptyList()
            )

            searchUseCase.searchUsers(username).fold(
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
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchResults: List<UserSearchResult> = emptyList(),
    val error: String? = null
)
