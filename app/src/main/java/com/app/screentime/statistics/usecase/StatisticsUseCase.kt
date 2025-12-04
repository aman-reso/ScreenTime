package com.app.screentime.statistics.usecase

import android.content.Context
import com.app.screentime.R
import com.app.screentime.data.uiModel.WeeklyDataReport
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.statistics.mapper.StatisticsUiMapper
import com.app.screentime.statistics.model.StatisticsUiProps
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Use case for statistics operations
 */
class StatisticsUseCase @Inject constructor(
    private val localAppUsageRepository: LocalAppUsageRepository,
    private val statisticsUiMapper: StatisticsUiMapper,
    @ApplicationContext private val context: Context
) {

    /**
     * Get Statistics UI Props
     */
    suspend fun getStatisticsUiProps(
        selectedDayIndex: Int? = null,
        chartOrientation: com.telekom.odsystem.organisms.barchart.ODSBarItemDirection = com.telekom.odsystem.organisms.barchart.ODSBarItemDirection.VERTICAL,
        isLoading: Boolean = false,
        error: String? = null
    ): StatisticsUiProps {
        if (isLoading) {
            return statisticsUiMapper.toUiProps(
                weeklyReports = emptyList(),
                selectedDayIndex = null,
                chartOrientation = chartOrientation,
                isLoading = true,
                error = null
            )
        }

        if (error != null) {
            return statisticsUiMapper.toUiProps(
                weeklyReports = emptyList(),
                selectedDayIndex = null,
                chartOrientation = chartOrientation,
                isLoading = false,
                error = error
            )
        }

        return try {
            val weeklyReports = withContext(Dispatchers.Default) {
                localAppUsageRepository.getOneWeekReport()
            }

            // Default to latest day (today) - last index in the list
            val defaultDayIndex = if (selectedDayIndex == null && weeklyReports.isNotEmpty()) {
                weeklyReports.size - 1  // Last day (today/latest)
            } else {
                selectedDayIndex
            }

            statisticsUiMapper.toUiProps(
                weeklyReports = weeklyReports,
                selectedDayIndex = defaultDayIndex,
                chartOrientation = chartOrientation,
                isLoading = false,
                error = null
            )
        } catch (e: Exception) {
            val errorMessage = context.getString(
                R.string.failed_to_load_weekly_data,
                e.message ?: ""
            )
            statisticsUiMapper.toUiProps(
                weeklyReports = emptyList(),
                selectedDayIndex = null,
                chartOrientation = chartOrientation,
                isLoading = false,
                error = errorMessage
            )
        }
    }

    /**
     * Get chart formatter props
     */
    fun getChartFormatterProps() = statisticsUiMapper.createChartFormatterProps()
}

