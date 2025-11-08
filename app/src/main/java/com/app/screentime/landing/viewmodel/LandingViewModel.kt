package com.app.screentime.landing.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.R
import com.app.screentime.landing.model.LandingUiState
import com.app.screentime.landing.usecase.LandingUsecase
import com.app.screentime.record.usecase.RecordUseCase
import com.app.screentime.network.model.BatchUsageRecord
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.widget.ScreenTimeWidgetHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val landingUsecase: LandingUsecase,
    private val recordUseCase: RecordUseCase,
    private val preferences: PreferencesUseCase,
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()

    init {
        loadUsername()
        loadRealUsageDataFromHelper()
    }

    private fun loadUsername() {
        val username = preferencesManager.getUsername()
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Load today's usage data from the repository
     * Delegates business logic to the UseCase
     */

    internal fun loadRealUsageDataFromHelper() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            landingUsecase.getTodayUsageData()
                .fold(
                    onSuccess = { todayUsageData ->
                        _uiState.value = todayUsageData.updateUiState(_uiState.value)
                        val dailyLimit = 3 * 60 * 60 * 1000L // Default 3 hours in milliseconds
                        ScreenTimeWidgetHelper.updateWidgetFromAppUsages(
                            context = context,
                            appUsages = todayUsageData.topUsedApps,
                            dailyLimit = dailyLimit
                        )
                    },
                    onFailure = { exception ->
                        val errorMessage = when (exception) {
                            is SecurityException -> context.getString(
                                R.string.permission_denied,
                                exception.message ?: ""
                            )

                            else -> context.getString(
                                R.string.failed_to_load_usage_data,
                                exception.message ?: ""
                            )
                        }
                        _uiState.value = _uiState.value.copy(
                            error = errorMessage,
                            isLoading = false
                        )
                    }
                )
        }
    }

    fun shouldShowConsentScreen(): Boolean {
        return preferences.shouldShowConsentSheet()
    }

    fun markConsentShown() {
        preferences.markConsentSheetShown()
    }
}
