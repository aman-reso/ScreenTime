package com.app.screentime.record.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.network.model.DailyUsage
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.record.usecase.RecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import com.app.screentime.utils.DateUtils

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val recordUseCase: RecordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState.asStateFlow()

    /**
     * Get daily usage stats for a target user
     * This is called after TOTP verification to view another user's usage
     */
    fun getDailyUsageStats(
        targetUserId: String,
        date: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Use today's date if not provided
            val selectedDate = date ?: getTodayDate()

            // Get raw stats data first
            val rawStatsResult = recordUseCase.getRawDailyStats(selectedDate, targetUserId)
            val stats = rawStatsResult.getOrNull() ?: emptyList()

            // Sort by eventTimestamp for timeline (descending - newest first)
            val sortedStats = stats.sortedByDescending { stat ->
                stat.eventTimestamp?.let {
                    DateUtils.toMillis(it)
                } ?: 0L
            }

            // Process timeline data
            val timelineItems = recordUseCase.processTimelineData(sortedStats)

            recordUseCase.getDailyUsageStats(selectedDate, targetUserId)
                .fold(
                    onSuccess = { dailyUsage ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            stats = sortedStats,
                            timeLines = timelineItems,
                            dailyUsage = dailyUsage,
                            selectedDate = selectedDate,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to load daily usage stats: ${exception.message}",
                            records = emptyList(),
                            stats = sortedStats, // Still show stats even if dailyUsage fails
                            timeLines = timelineItems
                        )
                    }
                )
        }
    }


    /**
     * Get today's date in yyyy-MM-dd format
     */
    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(calendar.time)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }


}

data class RecordDetailUiState(
    val isLoading: Boolean = false,
    val records: List<UsageRecordResponse> = emptyList(),
    val stats: List<AppUsageStatsData> = emptyList(), // Raw stats from API
    val timeLines: List<TimelineListItem> = emptyList(),
    val startDate: String = "2023-10-01",
    val endDate: String = "2023-10-31",
    val selectedDate: String? = null,
    val dailyUsage: DailyUsage? = null,
    val error: String? = null
)

