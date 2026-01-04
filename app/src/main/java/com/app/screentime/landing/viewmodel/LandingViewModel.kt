package com.app.screentime.landing.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.landing.model.LandingUiProps
import com.app.screentime.landing.usecase.LandingUsecase
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.widget.ScreenTimeWidgetHelper
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
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiProps = MutableStateFlow<LandingUiProps?>(null)
    val uiProps: StateFlow<LandingUiProps?> = _uiProps.asStateFlow()

    private val _dailyGoalHours = MutableStateFlow(preferences.getDailyGoalHours())
    val dailyGoalHours: StateFlow<Int> = _dailyGoalHours.asStateFlow()

    init {
        loadLandingData()
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

            // Update widget if we have data
            if (props.topUsedApps.isNotEmpty()) {
                val dailyLimit = 3 * 60 * 60 * 1000L // Default 3 hours in milliseconds
                ScreenTimeWidgetHelper.updateWidgetFromAppUsages(
                    context = context,
                    appUsages = props.topUsedApps,
                    dailyLimit = dailyLimit
                )
            }

            val joinedChallenges = landingUsecase.getJoinedChallenges()
            val recentData = _uiProps.value?.copy(joinedChallenges = joinedChallenges)
            _uiProps.value = recentData
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
        loadLandingData()
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
