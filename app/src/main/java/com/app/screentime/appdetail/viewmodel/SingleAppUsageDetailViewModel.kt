package com.app.screentime.appdetail.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.config.R
import com.app.screentime.appdetail.model.SingleAppUsageUiState
import com.app.screentime.appdetail.usecase.GetSingleAppUsageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleAppUsageDetailViewModel @Inject constructor(
    private val getSingleAppUsageUseCase: GetSingleAppUsageUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SingleAppUsageUiState())
    val uiState: StateFlow<SingleAppUsageUiState> = _uiState.asStateFlow()

    /**
     * Load app usage data for a specific package
     * @param packageName The package name of the app
     * @param appName The display name of the app
     */
    fun loadAppUsageData(packageName: String, appName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                packageName = packageName,
                appName = appName
            )

            getSingleAppUsageUseCase.getWeeklyAppUsage(packageName)
                .fold(
                    onSuccess = { weeklyUsageData ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            weeklyUsageData = weeklyUsageData,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = context.getString(R.string.failed_to_load_app_usage, exception.message ?: ""),
                            weeklyUsageData = emptyList()
                        )
                    }
                )
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

