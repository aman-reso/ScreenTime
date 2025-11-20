package com.app.screentime.challenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.network.model.Challenge
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.ChallengeRankingsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChallengesUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val challenges: List<Challenge> = emptyList(),
    val lastUpdated: String? = null,
    val joiningChallengeIds: Set<Int> = emptySet(),
    val challengeDetails: ChallengeDetails? = null,
    val challengeRankings: ChallengeRankingsResponse? = null,
    val isLoadingDetails: Boolean = false,
    val detailsError: String? = null
)

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengesUiState())
    val uiState: StateFlow<ChallengesUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            challengeRepository.getActiveChallenges().fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        _uiState.value = ChallengesUiState(
                            isLoading = false,
                            challenges = response.data.challenges,
                            lastUpdated = "Just now",
                            error = null
                        )
                    } else {
                        val errorMsg = response.message ?: "Failed to load challenges"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg,
                            challenges = emptyList()
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Failed to load challenges",
                        challenges = emptyList()
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun loadChallengeDetails(challengeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingDetails = true,
                detailsError = null,
                challengeDetails = null,
                challengeRankings = null
            )

            // Load challenge details
            challengeRepository.getChallengeDetails(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        _uiState.value = _uiState.value.copy(
                            challengeDetails = response.data,
                            isLoadingDetails = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoadingDetails = false,
                            detailsError = response.message ?: "Failed to load challenge details"
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingDetails = false,
                        detailsError = throwable.message ?: "Failed to load challenge details"
                    )
                }
            )

            // Load challenge rankings
            challengeRepository.getChallengeRankings(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        _uiState.value = _uiState.value.copy(
                            challengeRankings = response.data
                        )
                    }
                },
                onFailure = {
                    // Rankings failure is not critical, just log it
                }
            )
        }
    }

    fun joinChallenge(challengeId: Int, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        // Prevent duplicate join requests
        if (_uiState.value.joiningChallengeIds.contains(challengeId)) {
            return
        }

        viewModelScope.launch {
            // Mark as joining
            _uiState.value = _uiState.value.copy(
                joiningChallengeIds = _uiState.value.joiningChallengeIds + challengeId
            )

            challengeRepository.joinChallenge(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true) {
                        // Update local state to mark challenge as joined
                        val updatedChallenges = _uiState.value.challenges.map { challenge ->
                            if (challenge.id == challengeId) {
                                challenge.copy(hasJoined = true)
                            } else {
                                challenge
                            }
                        }
                        _uiState.value = _uiState.value.copy(
                            joiningChallengeIds = _uiState.value.joiningChallengeIds - challengeId,
                            challenges = updatedChallenges
                        )
                        onSuccess()
                    } else {
                        val errorMsg = response.message ?: "Failed to join challenge"
                        _uiState.value = _uiState.value.copy(
                            error = errorMsg,
                            joiningChallengeIds = _uiState.value.joiningChallengeIds - challengeId
                        )
                        onError(errorMsg)
                    }
                },
                onFailure = { throwable ->
                    val errorMsg = throwable.message ?: "Failed to join challenge"
                    _uiState.value = _uiState.value.copy(
                        error = errorMsg,
                        joiningChallengeIds = _uiState.value.joiningChallengeIds - challengeId
                    )
                    onError(errorMsg)
                }
            )
        }
    }
}
