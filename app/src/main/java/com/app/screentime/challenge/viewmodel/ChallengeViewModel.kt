package com.app.screentime.challenge.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.challenge.mapper.ChallengeMapper
import com.app.screentime.challenge.model.GroupedChallenges
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.network.model.Challenge
import com.app.screentime.analytics.AnalyticsUseCase
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
    val groupedChallenges: GroupedChallenges? = null,
    val lastUpdated: String? = null,
    val joiningChallengeIds: Set<String> = emptySet()
)

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val challengeMapper: ChallengeMapper,
    private val analyticsUseCase: AnalyticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengesUiState())
    val uiState: StateFlow<ChallengesUiState> = _uiState.asStateFlow()

    init {
        analyticsUseCase.trackChallengeListScreen()
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            // Use server data
            challengeRepository.getActiveChallenges().fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        val challenges = response.data!!.challenges
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            challenges = challenges,
                            groupedChallenges = challengeMapper.groupChallengesByType(challenges),
                            lastUpdated = "Just now",
                            error = null
                        )
                    } else {
                        val errorMsg = response.message ?: "Failed to load challenges"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg,
                            challenges = emptyList(),
                            groupedChallenges = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Failed to load challenges",
                        challenges = emptyList(),
                        groupedChallenges = null
                    )
                }
            )
        }
    }


    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

}
