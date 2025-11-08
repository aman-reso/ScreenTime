package com.app.screentime.record.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.UsageRecordResponse
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

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val recordUseCase: RecordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState.asStateFlow()

    /**
     * Get usage records for a user within a date range
     */
    fun getUsageRecords(
        username: String,
        startDate: String? = null,
        endDate: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Hardcoded date range for now
            val start = startDate ?: "2023-10-01"
            val end = endDate ?: "2023-10-31"

            recordUseCase.getUsageRecordsByUsername(username, start, end)
                .fold(
                    onSuccess = { records ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            records = records,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to load usage records: ${exception.message}",
                            records = emptyList()
                        )
                    }
                )
        }
    }

    /**
     * Update the date range and refresh records
     */
    fun updateDateRange(startDate: String, endDate: String, username: String) {
        _uiState.value = _uiState.value.copy(
            startDate = startDate,
            endDate = endDate
        )
        getUsageRecords(username, startDate, endDate)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

// Commented out - using hardcoded dates for now
// /**
//  * Get default start date (30 days ago)
//  */
// private fun getDefaultStartDate(): String {
//     val calendar = Calendar.getInstance()
//     calendar.add(Calendar.DAY_OF_MONTH, -30)
//     val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//     return dateFormat.format(calendar.time)
// }

// /**
//  * Get default end date (today)
//  */
// private fun getDefaultEndDate(): String {
//     val calendar = Calendar.getInstance()
//     val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//     return dateFormat.format(calendar.time)
// }

    /**
     * Get formatted total usage time
     */
    fun getFormattedTotalUsageTime(): String {
        // Try to use usageTimeMinutes first, fall back to milliseconds
        val totalMinutes = _uiState.value.records.sumOf { record ->
            record.usageTimeMinutes?.toLong()
                ?: (record.usageTimeMilliseconds ?: 0L) / 60000
        }

        return if (totalMinutes > 0) {
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            when {
                hours > 0 -> "${hours}h ${minutes}m"
                else -> "${minutes}m"
            }
        } else {
            "0m"
        }
    }

// Note: formatUsageTime is now in the Screen composable
}

data class RecordDetailUiState(
    val isLoading: Boolean = false,
    val records: List<UsageRecordResponse> = emptyList(),
    val startDate: String = "2023-10-01",
    val endDate: String = "2023-10-31",
    val error: String? = null
)

