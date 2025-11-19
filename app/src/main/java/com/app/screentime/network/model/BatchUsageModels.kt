package com.app.screentime.network.model

import kotlinx.serialization.Serializable

/**
 * Batch usage record for API
 */
@Serializable
data class BatchUsageRecord(
    val appName: String,
    val packageName: String,
    val usageTimeMilliseconds: Long,
    val usageStart: String, // ISO 8601 format: "2023-10-25T09:00:00"
    val usageEnd: String,   // ISO 8601 format: "2023-10-25T10:00:00"
    val isSystemApp: Boolean,
    val date: String        // Format: "2023-10-25"
)

/**
 * Batch usage request - array of records
 */
@Serializable
data class BatchUsageRequest(
    val records: List<BatchUsageRecord>
)

