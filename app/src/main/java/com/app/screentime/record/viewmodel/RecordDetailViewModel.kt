package com.app.screentime.record.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.location.repository.LocationRepository
import com.app.screentime.network.model.AppUsageData
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.network.model.DailyUsage
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.network.model.UserLastLocationData
import com.app.screentime.network.model.SummaryScreenTimeRequest
import com.app.screentime.network.model.SummaryScreenTimeUserRequest
import com.app.screentime.network.model.SummaryScreenTimeUserResponse
import com.app.screentime.network.repository.screentime.ScreenTimeRepository
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.record.model.SummaryTabUiProps
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.record.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.app.screentime.utils.DateUtils
import org.joda.time.format.DateTimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val screenTimeRepository: ScreenTimeRepository,
    private val recordRepository: RecordRepository,
    private val preferencesUseCase: PreferencesUseCase
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
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        val selectedDate = date ?: getTodayDate()
        val request = SummaryScreenTimeRequest(
            users = listOf(
                SummaryScreenTimeUserRequest(
                    username = targetUserId,
                    date = selectedDate
                )
            )
        )

        viewModelScope.launch(Dispatchers.Default) {
            // Execute all operations in parallel using async/await
            val locationDeferred = async {
                locationRepository.getUserLastLocation(targetUserId).getOrNull()
            }.await()
            val summaryScreenTimeDeferred = async {
                screenTimeRepository.getSummaryScreenTime(request).getOrNull()
            }.await()
            val appStatsDeferred = async {
                recordRepository.getAppStats(selectedDate, targetUserId)
            }

            appStatsDeferred.await().fold(
                onSuccess = { stats ->
                    val sortedStats = stats.sortedByDescending { it.appScreenTime }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        stats = sortedStats,
                        selectedDate = selectedDate,
                        userLocation = locationDeferred,
                        summaryScreenTime = summaryScreenTimeDeferred?.data?.users?.firstOrNull()?.screenTime,
                        error = null
                    )
                },
                onFailure = { exception ->
                    val errorMessage = "Failed to load app stats: ${exception.message}"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage,
                        stats = emptyList(),
                        timeLines = emptyList()
                    )
                }
            )
        }
    }


    /**
     * Get today's date in yyyy-MM-dd format
     */
    private fun getTodayDate(): String {
        return DateTimeFormat.forPattern("yyyy-MM-dd").print(DateUtils.now())
    }


    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Get formatted daily goal string (e.g., "6h")
     */
    fun getFormattedDailyGoal(): String {
        val hours = preferencesUseCase.getDailyGoalHours()
        return "${hours}h"
    }

    /**
     * Format summary screen time in milliseconds to human-readable string
     */
    fun formatSummaryScreenTime(ms: Long?): String? {
        if (ms == null) return null
        val totalMinutes = ms / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}

data class RecordDetailUiState(
    val isLoading: Boolean = false,
    val stats: List<AppUsage>? = null, // Raw stats from API
    val timeLines: List<TimelineListItem> = emptyList(),
    val startDate: String? = null,
    val endDate: String? = null,
    val selectedDate: String? = null,
    val dailyUsage: DailyUsage? = null,
    val userLocation: UserLastLocationData? = null,
    val summaryScreenTime: Long? = null,
    val error: String? = null
)

