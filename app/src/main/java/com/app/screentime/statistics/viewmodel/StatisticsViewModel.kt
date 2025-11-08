package com.app.screentime.statistics.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.R
import com.app.screentime.statistics.model.StatisticsUiState
import com.app.screentime.record.repository.LocalAppUsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val localAppUsageRepository: LocalAppUsageRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadWeeklyData()
    }

    fun loadWeeklyData() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val weeklyReports = localAppUsageRepository.getOneWeekReport()
                
                // Default to latest day (today) - last index in the list
                val defaultDayIndex = if (weeklyReports.isNotEmpty()) {
                    weeklyReports.size - 1  // Last day (today/latest)
                } else {
                    null
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weeklyReports = weeklyReports,
                    error = null,
                    selectedDayIndex = defaultDayIndex  // Default to latest day
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = context.getString(R.string.failed_to_load_weekly_data, e.message ?: ""),
                    weeklyReports = emptyList()
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun selectDay(dayIndex: Int) {
        _uiState.value = _uiState.value.copy(
            selectedDayIndex = dayIndex
        )
    }

    fun toggleExpandedState() {
        // If collapsed, expand to latest day; if expanded, collapse
        val currentIndex = _uiState.value.selectedDayIndex
        _uiState.value = _uiState.value.copy(
            selectedDayIndex = if (currentIndex == null) {
                // Expand to latest day if collapsed
                if (_uiState.value.weeklyReports.isNotEmpty()) {
                    _uiState.value.weeklyReports.size - 1
                } else null
            } else {
                // Collapse if expanded
                null
            }
        )
    }
}

