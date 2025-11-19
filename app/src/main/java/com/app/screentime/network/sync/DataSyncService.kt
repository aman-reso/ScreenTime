package com.app.screentime.network.sync

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.network.repository.screentime.ScreenTimeRepository
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.record.repository.AppEvent
import com.app.screentime.network.model.ScreenTimeUsageRequest
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for syncing data with remote server
 */
@Singleton
class DataSyncService @Inject constructor(
    private val screenTimeRepository: ScreenTimeRepository,
    private val networkUtils: NetworkUtils
) {

    /**
     * Sync batch usage events to server
     */
    suspend fun syncBatchUsageEvents(
        request: com.app.screentime.network.model.BatchUsageEventsRequest
    ): SyncResult {
        if (!networkUtils.isNetworkAvailable()) {
            return SyncResult.NoNetwork
        }
        return try {
            val result = screenTimeRepository.syncBatchUsageEvents(request)

            when {
                result.isSuccess -> {
                    val apiResponse = result.getOrNull()
                    if (apiResponse?.success == true) {
                        SyncResult.Success
                    } else {
                        SyncResult.Error(apiResponse?.message ?: "API returned error")
                    }
                }

                else -> SyncResult.Error(
                    result.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            SyncResult.Error(e.message ?: "Unknown error")
        }
    }
}

/**
 * Sync result sealed class
 */
sealed class SyncResult {
    object Success : SyncResult()
    object Syncing : SyncResult()
    object NoNetwork : SyncResult()
    object WifiRequired : SyncResult()
    data class Error(val message: String) : SyncResult()
}
