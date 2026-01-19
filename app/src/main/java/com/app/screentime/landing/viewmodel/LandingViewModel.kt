package com.app.screentime.landing.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.landing.model.LandingUiProps
import com.app.screentime.landing.usecase.LandingUsecase
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.analytics.AnalyticsUseCase
import com.app.screentime.sync.DataSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val landingUsecase: LandingUsecase,
    private val preferences: PreferencesUseCase,
    private val preferencesManager: PreferencesManager,
    private val analyticsUseCase: AnalyticsUseCase,
    private val dataSyncManager: DataSyncManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiProps = MutableStateFlow<LandingUiProps?>(null)
    val uiProps: StateFlow<LandingUiProps?> = _uiProps.asStateFlow()

    private val _dailyGoalHours = MutableStateFlow(preferences.getDailyGoalHours())
    val dailyGoalHours: StateFlow<Int> = _dailyGoalHours.asStateFlow()

    init {
        analyticsUseCase.trackHomeScreen()
        loadLandingData()
    }

    fun trackLeaderboardClick() {
        analyticsUseCase.trackLeaderboardClick()
    }

    fun trackRewardClick() {
        analyticsUseCase.trackRewardClick()
    }

    fun trackSearchClick() {
        analyticsUseCase.trackSearchClick()
    }

    fun trackFloatingButtonRewardClick() {
        analyticsUseCase.trackFloatingButtonRewardClick()
    }

    /**
     * Load landing screen data and get UI Props from use case
     */
    fun loadLandingData() {
        viewModelScope.launch(Dispatchers.Default) {
            val username = preferencesManager.getUsername()

            // Show loading state
            _uiProps.value = landingUsecase.getLandingUiProps(
                username = username,
                isLoading = true
            )

            // Load actual data
            val props = landingUsecase.getLandingUiProps(
                username = username,
                isLoading = false
            )
            _uiProps.value = props
            val joinedChallenges = landingUsecase.getJoinedChallenges()
            val recentData = _uiProps.value?.copy(joinedChallenges = joinedChallenges)
            _uiProps.value = recentData

            // Sync leaderboard stats
            landingUsecase.syncLeaderboardStats()
            
            // Sync app stats using new /api/app-stats endpoint
            dataSyncManager.syncAppStats()
        }
    }

    fun clearError() {
        val currentProps = _uiProps.value
        if (currentProps != null) {
            _uiProps.value = currentProps.copy(error = null)
        }
    }

    fun shouldShowConsentScreen(): Boolean {
        return landingUsecase.shouldShowConsentScreen()
    }

    fun markConsentShown() {
        landingUsecase.markConsentShown()
        // Don't refresh home page when consent is accepted
    }

    fun shouldAskForUsageStatsPermission(): Boolean {
        return preferences.shouldAskForUsageStatsPermission()
    }

    fun markUsageStatsPermissionRequested() {
        preferences.markUsageStatsPermissionRequested()
    }

    /**
     * Get formatted daily goal string (e.g., "6h")
     */
    fun getFormattedDailyGoal(): String {
        val hours = _dailyGoalHours.value
        return "${hours}h"
    }

    /**
     * Save daily goal and update state
     */
    fun saveDailyGoal(hours: Int) {
        preferences.setDailyGoalHours(hours)
        _dailyGoalHours.value = hours
    }
}
