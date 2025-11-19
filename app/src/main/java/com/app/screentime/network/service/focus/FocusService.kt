package com.app.screentime.network.service.focus

import com.app.screentime.network.model.*

/**
 * Service interface for Focus Duration related API operations
 */
interface FocusService {
    suspend fun submitFocusDuration(request: FocusDurationSubmissionRequest): Result<ApiResponse<FocusDurationSubmissionResponse>>
    suspend fun getFocusHistory(request: FocusDurationHistoryRequest): Result<ApiResponse<FocusDurationHistoryResponse>>
    suspend fun getFocusStats(): Result<ApiResponse<FocusDurationStatsResponse>>
    suspend fun syncFocusModeStats(request: FocusModeStatsSyncRequest): Result<ApiResponse<FocusModeStatsSyncResponse>>
    suspend fun getFocusModeStats(startTimeMs: Long, endTimeMs: Long): Result<ApiResponse<FocusModeStatsResponse>>
}

