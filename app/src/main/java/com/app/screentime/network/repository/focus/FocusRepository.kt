package com.app.screentime.network.repository.focus

import com.app.screentime.network.model.*
import com.app.screentime.network.service.focus.FocusService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Focus Duration operations
 */
@Singleton
class FocusRepository @Inject constructor(
    private val focusService: FocusService
) {
    suspend fun submitFocusDuration(request: FocusDurationSubmissionRequest): Result<ApiResponse<FocusDurationSubmissionResponse>> {
        return focusService.submitFocusDuration(request)
    }

    suspend fun getFocusHistory(request: FocusDurationHistoryRequest): Result<ApiResponse<FocusDurationHistoryResponse>> {
        return focusService.getFocusHistory(request)
    }

    suspend fun getFocusStats(): Result<ApiResponse<FocusDurationStatsResponse>> {
        return focusService.getFocusStats()
    }

    suspend fun syncFocusModeStats(request: FocusModeStatsSyncRequest): Result<ApiResponse<FocusModeStatsSyncResponse>> {
        return focusService.syncFocusModeStats(request)
    }

    suspend fun getFocusModeStats(startTimeMs: Long, endTimeMs: Long): Result<ApiResponse<FocusModeStatsResponse>> {
        return focusService.getFocusModeStats(startTimeMs, endTimeMs)
    }
}

