package com.app.screentime.challenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.challenge.mapper.JoinedChallengeMapper
import com.app.screentime.challenge.usecase.JoinedChallengeUseCase
import com.app.screentime.network.model.Challenge
import com.app.screentime.network.model.UserChallenge
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI State for Joined Challenges Screen
 */
data class JoinedChallengesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val joinedChallenges: List<UserChallenge> = emptyList(),
    val filteredChallenges: List<Challenge> = emptyList(),
    val selectedFilter: ChallengeFilter = ChallengeFilter.ALL
)

/**
 * Filter options for joined challenges
 */
enum class ChallengeFilter {
    ALL,
    CURRENT,
    EXPIRED
}

/**
 * ViewModel for Joined Challenges Screen
 * Handles loading and filtering joined challenges
 */
@HiltViewModel
class JoinedChallengeViewModel @Inject constructor(
    private val joinedChallengeUseCase: JoinedChallengeUseCase,
    private val joinedChallengeMapper: JoinedChallengeMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinedChallengesUiState())
    val uiState: StateFlow<JoinedChallengesUiState> = _uiState.asStateFlow()

    /**
     * Load joined challenges from API
     */
    fun loadJoinedChallenges() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )
        viewModelScope.launch {
            joinedChallengeUseCase.getJoinedChallenges().fold(
                onSuccess = { challenges ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        joinedChallenges = challenges,
                        error = null
                    )
                    applyFilter()
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Failed to load joined challenges",
                        joinedChallenges = emptyList(),
                        filteredChallenges = emptyList()
                    )
                }
            )
        }
    }

    /**
     * Set filter and update filtered challenges
     */
    fun setFilter(filter: ChallengeFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        applyFilter()
    }

    /**
     * Apply current filter to challenges
     */
    private fun applyFilter() {
        val filtered = when (_uiState.value.selectedFilter) {
            ChallengeFilter.ALL -> _uiState.value.joinedChallenges
            ChallengeFilter.CURRENT -> joinedChallengeUseCase.filterChallengesByStatus(
                _uiState.value.joinedChallenges,
                showCurrent = true
            )
            ChallengeFilter.EXPIRED -> joinedChallengeUseCase.filterChallengesByStatus(
                _uiState.value.joinedChallenges,
                showCurrent = false
            )
        }
        val mappedChallenges = joinedChallengeMapper.toChallengeList(filtered)
        _uiState.value = _uiState.value.copy(filteredChallenges = mappedChallenges)
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

