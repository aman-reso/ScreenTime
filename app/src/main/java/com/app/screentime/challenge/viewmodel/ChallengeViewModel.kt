package com.app.screentime.challenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.network.model.ChallengeAppRanking
import com.app.screentime.network.model.ChallengeCompetitor
import com.app.screentime.network.model.ChallengeReward
import com.app.screentime.network.model.ChallengeTrend
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChallengesUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val challenges: List<ChallengeAppRanking> = emptyList(),
    val lastUpdated: String? = null,
    val joiningChallengeIds: Set<String> = emptySet()
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
            var handled = false
            challengeRepository.getChallengeOverview().fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null && response.data.challenges.isNotEmpty()) {
                        _uiState.value = ChallengesUiState(
                            isLoading = false,
                            challenges = response.data.challenges,
                            lastUpdated = response.data.lastRefreshedAt ?: "Just now"
                        )
                        handled = true
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(error = throwable.message)
                }
            )

            if (!handled) {
                loadDummyChallenges()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun joinChallenge(challengeId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
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
                        // Update the challenge in the list to mark it as joined
                        val updatedChallenges = _uiState.value.challenges.map { challenge ->
                            if (challenge.challengeId == challengeId) {
                                challenge.copy(isJoined = true)
                            } else {
                                challenge
                            }
                        }
                        _uiState.value = _uiState.value.copy(
                            challenges = updatedChallenges,
                            joiningChallengeIds = _uiState.value.joiningChallengeIds - challengeId
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

    private fun loadDummyChallenges() {
        _uiState.value = ChallengesUiState(
            isLoading = false,
            challenges = dummyChallenges,
            lastUpdated = "Just now",
            error = null
        )
    }

    private val dummyChallenges = listOf(
        ChallengeAppRanking(
            challengeId = "yt-watchless",
            appName = "YouTube",
            packageName = "com.google.android.youtube",
            iconUrl = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop",
            description = "Keep daily watch time under two hours",
            metricLabel = "Daily usage",
            metricUnit = "min",
            goalValue = 120,
            userRank = 5,
            totalParticipants = 128,
            userMetricValue = 95,
            percentile = 92.0,
            trend = ChallengeTrend(direction = "up", delta = 4.0),
            topCompetitors = listOf(
                ChallengeCompetitor(username = "ZenMaster", rank = 1, metricValue = 40, displayValue = "40 min"),
                ChallengeCompetitor(username = "FocusFox", rank = 2, metricValue = 52, displayValue = "52 min"),
                ChallengeCompetitor(username = "ProductivePanda", rank = 3, metricValue = 60, displayValue = "60 min")
            ),
            isJoined = true,
            rewards = listOf(
                ChallengeReward(
                    type = "badge",
                    title = "Focus Master Badge",
                    description = "Earned for maintaining watch time under 2 hours",
                    points = 100,
                    tier = "gold"
                ),
                ChallengeReward(
                    type = "points",
                    title = "Bonus Points",
                    description = "500 points for top 10 finish",
                    points = 500
                )
            )
        ),
        ChallengeAppRanking(
            challengeId = "insta-focus",
            appName = "Instagram",
            packageName = "com.instagram.android",
            iconUrl = "https://images.unsplash.com/photo-1611162616305-c69b3fa7fbe0?w=800&h=400&fit=crop",
            description = "Beat your friends by staying under 60 minutes",
            metricLabel = "Daily usage",
            metricUnit = "min",
            goalValue = 60,
            userRank = 12,
            totalParticipants = 210,
            userMetricValue = 48,
            percentile = 78.0,
            trend = ChallengeTrend(direction = "steady", delta = 0.0),
            topCompetitors = listOf(
                ChallengeCompetitor(username = "PhotoPro", rank = 1, metricValue = 20, displayValue = "20 min"),
                ChallengeCompetitor(username = "QuietMode", rank = 2, metricValue = 25, displayValue = "25 min"),
                ChallengeCompetitor(username = "MindfulMike", rank = 3, metricValue = 30, displayValue = "30 min")
            ),
            isJoined = false,
            rewards = listOf(
                ChallengeReward(
                    type = "trophy",
                    title = "Social Media Champion",
                    description = "Trophy for completing the Instagram challenge",
                    tier = "silver"
                ),
                ChallengeReward(
                    type = "points",
                    title = "Challenge Points",
                    description = "300 points for participation",
                    points = 300
                )
            )
        )
    )
}
