package com.app.screentime.focus.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.repository.FocusTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit

data class FocusSession(
    val id: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long,
    val completed: Boolean
)

data class FocusModeUiState(
    val isRunning: Boolean = false,
    val elapsedTime: Long = 0L,
    val startTime: Long = 0L,
    val totalDayTime: Long = 0L,
    val history: List<FocusSession> = emptyList()
)

const val DEFAULT_SESSION_DURATION = 25 * 60 * 1000L // 25 minutes in milliseconds

@HiltViewModel
class FocusModeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FocusModeUiState())
    val uiState: StateFlow<FocusModeUiState> = _uiState.asStateFlow()

    private var timerJob: kotlinx.coroutines.Job? = null

    init {
        startTimer()
    }

    fun checkServiceStatus(context: Context) {
//        viewModelScope.launch {
//            // Check if service is running
//            val isRunning = isServiceRunning(context)
//
//            // Load history from Room database
//            val history = loadHistoryFromRoom(context)
//
//            if (isRunning) {
//                // Load state from preferences
//                val prefs = context.getSharedPreferences("focus_mode_prefs", Context.MODE_PRIVATE)
//                val lastResetTime = prefs.getLong("focus_last_reset_time", 0L)
//                val startTime = prefs.getLong("focus_start_time", 0L)
//                val totalDayTime = prefs.getLong("focus_total_day_time", 0L)
//
//                if (lastResetTime > 0 && startTime > 0) {
//                    val currentTime = System.currentTimeMillis()
//                    val elapsed = currentTime - lastResetTime
//
//                    _uiState.value = _uiState.value.copy(
//                        isRunning = true,
//                        startTime = lastResetTime,
//                        elapsedTime = elapsed,
//                        totalDayTime = totalDayTime,
//                        history = history
//                    )
//                    startTimer()
//                } else {
//                    // If no saved state, start fresh
//                    _uiState.value = _uiState.value.copy(
//                        isRunning = true,
//                        startTime = System.currentTimeMillis(),
//                        elapsedTime = 0L,
//                        totalDayTime = totalDayTime,
//                        history = history
//                    )
//                    startTimer()
//                }
//            } else {
//                // If service is not running, reset session but keep total day time
//                val prefs = context.getSharedPreferences("focus_mode_prefs", Context.MODE_PRIVATE)
//                val totalDayTime = prefs.getLong("focus_total_day_time", 0L)
//
//                _uiState.value = _uiState.value.copy(
//                    isRunning = false,
//                    elapsedTime = 0L,
//                    startTime = 0L,
//                    totalDayTime = totalDayTime,
//                    history = history
//                )
//                stopTimer()
//            }
//        }
    }

    fun startFocusMode() {
        val currentTime = System.currentTimeMillis()
        
        _uiState.value = _uiState.value.copy(
            isRunning = true,
            startTime = currentTime
        )
        startTimer()
    }

    fun stopFocusMode(context: Context? = null) {
        val wasRunning = _uiState.value.isRunning
        val startTime = _uiState.value.startTime
        val endTime = System.currentTimeMillis()
        val duration = if (startTime > 0) endTime - startTime else 0L

        // Save to Room database - always end active sessions
        if (wasRunning && context != null) {
            viewModelScope.launch {
                try {
                    val database = ScreenTimeDatabase.getDatabase(context)
                    val focusTimeRepository = FocusTimeRepository(database.focusTimeDao())
                    
                    // Get active session first
                    val activeSession = focusTimeRepository.getActiveFocusSession()
                    if (activeSession != null) {
                        // End the active session with endTime
                        val wasCompleted = duration >= 60 * 1000 // At least 1 minute
                        focusTimeRepository.endFocusSession(activeSession.id, completed = wasCompleted)
                    } else {
                        // If no active session found, check if there's a session that matches our startTime
                        // Find sessions that started around the same time (within 10 seconds) and don't have endTime
                        val allSessions = focusTimeRepository.getRecentSessions(20)
                        val matchingSession = allSessions.firstOrNull { 
                            kotlin.math.abs(it.startTime - startTime) < 10000 && it.endTime == null
                        }
                        
                        if (matchingSession != null) {
                            // End the matching session
                            val wasCompleted = duration >= 60 * 1000
                            focusTimeRepository.endFocusSession(matchingSession.id, completed = wasCompleted)
                        } else if (duration >= 60 * 1000) {
                            // Only create a new session if it was meaningful (at least 1 minute)
                            // This handles cases where the service didn't create a session
                            val sessionId = focusTimeRepository.startFocusSession()
                            // Update the startTime to match the actual start time
                            val session = focusTimeRepository.getFocusSessionById(sessionId)
                            if (session != null) {
                                val updatedSession = session.copy(startTime = startTime)
                                focusTimeRepository.updateFocusSession(updatedSession)
                                focusTimeRepository.endFocusSession(sessionId, completed = true)
                            }
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("FocusModeViewModel", "Error saving to Room database", e)
                }
                
                // Reload history from Room database
                val history = loadHistoryFromRoom(context)
                _uiState.value = _uiState.value.copy(history = history)
            }
        } else {
            // If session was too short or not running, still try to clean up any active sessions
            if (context != null) {
                viewModelScope.launch {
                    try {
                        val database = ScreenTimeDatabase.getDatabase(context)
                        val focusTimeRepository = FocusTimeRepository(database.focusTimeDao())
                        val activeSession = focusTimeRepository.getActiveFocusSession()
                        if (activeSession != null) {
                            // End the session even if it was short
                            focusTimeRepository.endFocusSession(activeSession.id, completed = false)
                            
                            // Reload history
                            val history = loadHistoryFromRoom(context)
                            _uiState.value = _uiState.value.copy(history = history)
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("FocusModeViewModel", "Error cleaning up session", e)
                    }
                }
            }
        }

        _uiState.value = _uiState.value.copy(
            isRunning = false,
            elapsedTime = 0L,
            startTime = 0L
        )
        stopTimer()

        // Reload history if not already done above
        if (context != null && (wasRunning && duration < 60 * 1000)) {
            viewModelScope.launch {
                val history = loadHistoryFromRoom(context)
                _uiState.value = _uiState.value.copy(history = history)
            }
        }
    }

    fun setSessionDuration(context: Context, durationMinutes: Int) {
        val durationMs = durationMinutes * 60 * 1000L
        val prefs = context.getSharedPreferences("focus_mode_prefs", Context.MODE_PRIVATE)
        prefs.edit {
            putLong("focus_countdown_duration", durationMs)
        }
    }

    fun loadSessionDuration(context: Context): Int {
        val prefs = context.getSharedPreferences("focus_mode_prefs", Context.MODE_PRIVATE)
        val durationMs = prefs.getLong("focus_countdown_duration", DEFAULT_SESSION_DURATION)
        return (durationMs / (60 * 1000L)).toInt()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.isRunning) {
                val currentTime = System.currentTimeMillis()
                val startTime = _uiState.value.startTime
                if (startTime > 0) {
                    val elapsed = currentTime - startTime

                    _uiState.value = _uiState.value.copy(
                        elapsedTime = elapsed
                    )
                }
                delay(1000) // Update every second
            }
        }
    }

    /**
     * Load history from Room database
     */
    private suspend fun loadHistoryFromRoom(context: Context): List<FocusSession> {
        return try {
            val database = ScreenTimeDatabase.getDatabase(context)
            val focusTimeRepository = FocusTimeRepository(database.focusTimeDao())
            
            // Get recent sessions (last 50)
            val recentSessions = focusTimeRepository.getRecentSessions(50)
            
            // Convert to FocusSession format, only include completed sessions with endTime
            recentSessions
                .filter { it.completed && it.endTime != null }
                .map { entity ->
                    FocusSession(
                        id = entity.id.toString(),
                        startTime = entity.startTime,
                        endTime = entity.endTime ?: 0L,
                        duration = entity.duration,
                        completed = entity.completed
                    )
                }
                .sortedByDescending { it.startTime } // Most recent first
        } catch (e: Exception) {
            android.util.Log.e("FocusModeViewModel", "Error loading history from Room", e)
            emptyList()
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

//    private fun isServiceRunning(context: Context): Boolean {
//        val activityManager =
//            context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
//        return activityManager.getRunningServices(Integer.MAX_VALUE)
//            .any { it.service.className == FocusModeService::class.java.name }
//    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}

