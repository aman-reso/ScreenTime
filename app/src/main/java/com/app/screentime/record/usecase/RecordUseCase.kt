package com.app.screentime.record.usecase

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.BatchUsageRecord
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.record.repository.RecordRepository
import javax.inject.Inject

/**
 * Use case for recording and retrieving usage data
 */
class RecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    /**
     * Send batch usage records
     * @param records List of batch usage records
     * @return Result of API response
     */
    suspend fun sendBatchUsage(records: List<BatchUsageRecord>): Result<ApiResponse<Unit>> {
        return recordRepository.sendBatchUsage(records)
    }

    /**
     * Get usage records by username and date range
     * @param username The username
     * @param startDate Start date in format "yyyy-MM-dd" (e.g., "2023-10-01")
     * @param endDate End date in format "yyyy-MM-dd" (e.g., "2023-10-31")
     * @return Result of usage records
     */
    suspend fun getUsageRecordsByUsername(
        username: String,
        startDate: String,
        endDate: String
    ): Result<List<UsageRecordResponse>> {
        return recordRepository.getUsageRecordsByUsername(username, startDate, endDate)
    }
}
