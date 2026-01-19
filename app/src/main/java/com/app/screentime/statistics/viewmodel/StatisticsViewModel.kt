package com.app.screentime.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.statistics.model.StatisticsUiProps
import com.app.screentime.statistics.usecase.StatisticsUseCase
import com.app.screentime.analytics.AnalyticsUseCase
import com.telekom.odsystem.organisms.barchart.ODSBarItemDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsUseCase: StatisticsUseCase,
    private val analyticsUseCase: AnalyticsUseCase
) : ViewModel() {

    private val _uiProps = MutableStateFlow<StatisticsUiProps?>(null)
    val uiProps: StateFlow<StatisticsUiProps?> = _uiProps.asStateFlow()

    init {
        analyticsUseCase.trackStatisticsScreen()
        loadWeeklyData()
    }

    fun loadWeeklyData() {
        viewModelScope.launch {
            val currentOrientation = _uiProps.value?.chartOrientation ?: ODSBarItemDirection.VERTICAL
            _uiProps.value = statisticsUseCase.getStatisticsUiProps(
                isLoading = true,
                chartOrientation = currentOrientation
            )
            val props = statisticsUseCase.getStatisticsUiProps(
                isLoading = false,
                chartOrientation = currentOrientation
            )
            _uiProps.value = props
        }
    }

    fun clearError() {
        val currentProps = _uiProps.value
        if (currentProps != null) {
            _uiProps.value = currentProps.copy(error = null)
        }
    }

    fun selectDay(dayIndex: Int) {
        viewModelScope.launch {
            val currentOrientation = _uiProps.value?.chartOrientation ?: ODSBarItemDirection.VERTICAL
            val props = statisticsUseCase.getStatisticsUiProps(
                selectedDayIndex = dayIndex,
                chartOrientation = currentOrientation
            )
            _uiProps.value = props
        }
    }

    fun toggleExpandedState() {
        val currentIndex = _uiProps.value?.selectedDayIndex
        val currentWeeklyReports = _uiProps.value?.weeklyReports
        val currentOrientation = _uiProps.value?.chartOrientation ?: ODSBarItemDirection.VERTICAL
        viewModelScope.launch {
            val props = statisticsUseCase.getStatisticsUiProps(
                selectedDayIndex = if (currentIndex == null) {
                    // Expand to latest day if collapsed
                    if (currentWeeklyReports != null && currentWeeklyReports.isNotEmpty()) {
                        currentWeeklyReports.size - 1
                    } else {
                        // Need to load data first to get the last index
                        val tempProps = statisticsUseCase.getStatisticsUiProps(
                            chartOrientation = currentOrientation
                        )
                        if (tempProps.barChartData.isNotEmpty()) {
                            tempProps.barChartData.size - 1
                        } else null
                    }
                } else {
                    // Collapse if expanded
                    null
                },
                chartOrientation = currentOrientation
            )
            _uiProps.value = props
        }
    }

    fun toggleChartOrientation() {
        val currentProps = _uiProps.value
        if (currentProps != null) {
            val newOrientation = when (currentProps.chartOrientation) {
                ODSBarItemDirection.VERTICAL -> ODSBarItemDirection.HORIZONTAL
                ODSBarItemDirection.HORIZONTAL -> ODSBarItemDirection.VERTICAL
            }
            viewModelScope.launch {
                val props = statisticsUseCase.getStatisticsUiProps(
                    selectedDayIndex = currentProps.selectedDayIndex,
                    chartOrientation = newOrientation
                )
                _uiProps.value = props
            }
        }
    }

    fun getChartFormatterProps() = statisticsUseCase.getChartFormatterProps()
}

