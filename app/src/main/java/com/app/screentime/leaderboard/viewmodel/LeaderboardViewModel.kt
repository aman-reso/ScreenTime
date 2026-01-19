package com.app.screentime.leaderboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.leaderboard.repository.LeaderboardRepository
import com.app.screentime.network.model.LeaderboardEntry
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.leaderboard.service.LeaderboardService
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.analytics.AnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LeaderboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val dailyEntries: List<LeaderboardEntry> = emptyList(),
    val weeklyEntries: List<LeaderboardEntry> = emptyList(),
    val userDailyRank: Int? = null,
    val userWeeklyRank: Int? = null,
    val userDailyDuration: Long? = null,
    val userWeeklyDuration: Long? = null,
    val currentUsername: String? = null,
    val currentUserId: String? = null,
    val shouldShowAd: Boolean = false // Flag to trigger ad display after successful API call
)

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val preferencesManager: PreferencesManager,
    private val analyticsUseCase: AnalyticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        analyticsUseCase.trackLeaderboardView()
        loadLeaderboardData(showAd = true) // Show ad on initial load
    }

    fun loadLeaderboardData(date: String? = null, showAd: Boolean = false) {
        val userId = preferencesManager.getUserId()
        val username = preferencesManager.getUsername() ?: "You"
        _uiState.value = _uiState.value.copy(
            currentUsername = username,
            currentUserId = userId,
            isLoading = true,
            error = null,
            shouldShowAd = false // Reset ad flag
        )

        viewModelScope.launch {
            // Load daily leaderboard
            leaderboardRepository.getDailyLeaderboard(date).fold(
                onSuccess = { apiResponse ->
                    if (apiResponse.success == true && apiResponse.data != null && apiResponse.data?.entries != null) {
                        val leaderboardData = apiResponse.data
                        val dailyEntries = leaderboardData!!.entries

                        // Find user's entry in the list or use userRank
                        val userEntry = dailyEntries.find { it.userId == userId }
                            ?: leaderboardData.userRank

                        _uiState.value = _uiState.value.copy(
                            dailyEntries = dailyEntries,
                            userDailyRank = userEntry?.rank,
                            userDailyDuration = userEntry?.totalScreenTime
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            error = apiResponse.message ?: "Failed to load daily leaderboard"
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to load daily leaderboard"
                    )
                }
            )

            // Note: isLoading is set to false only after weekly leaderboard loads
            // This ensures both requests complete before hiding the loader

            // Load weekly leaderboard
            leaderboardRepository.getWeeklyLeaderboard().fold(
                onSuccess = { apiResponse ->
                    if (apiResponse.success == true && apiResponse.data != null) {
                        val leaderboardData = apiResponse.data!!
                        val weeklyEntries = leaderboardData.entries

                        // Find user's entry in the list or use userRank
                        val userEntry = weeklyEntries.find { it.userId == userId }
                            ?: leaderboardData.userRank

                        _uiState.value = _uiState.value.copy(
                            weeklyEntries = weeklyEntries,
                            userWeeklyRank = userEntry?.rank,
                            userWeeklyDuration = userEntry?.totalScreenTime,
                            isLoading = false,
                            shouldShowAd = showAd // Trigger ad display only if showAd is true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            error = apiResponse.message ?: "Failed to load weekly leaderboard",
                            isLoading = false
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to load weekly leaderboard",
                        isLoading = false
                    )
                }
            )
        }
    }


    fun refresh() {
        loadLeaderboardData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun onAdShown() {
        _uiState.value = _uiState.value.copy(shouldShowAd = false)
    }
}

