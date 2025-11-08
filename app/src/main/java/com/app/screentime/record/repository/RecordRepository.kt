package com.app.screentime.record.repository

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.BatchUsageRecord
import com.app.screentime.network.model.UsageRecordResponse
import com.app.screentime.network.repository.NetworkRepository
import javax.inject.Inject

/**
 * Repository for recording and retrieving usage data
 */
class RecordRepository @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    /**
     * Send batch usage records to server
     */
    suspend fun sendBatchUsage(records: List<BatchUsageRecord>): Result<ApiResponse<Unit>> {
        return networkRepository.sendBatchUsage(records)
    }

    /**
     * Get usage records by username and date range
     * @param username The username
     * @param startDate Start date in format "yyyy-MM-dd"
     * @param endDate End date in format "yyyy-MM-dd"
     * @return Result of usage records
     */
    suspend fun getUsageRecordsByUsername(
        username: String,
        startDate: String,
        endDate: String
    ): Result<List<UsageRecordResponse>> {
        return networkRepository.getUsageRecordsByUsername(username, startDate, endDate)
    }
}
