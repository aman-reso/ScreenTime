package com.app.screentime.network.sync

import com.app.screentime.data.entity.AppUsage
import com.app.screentime.network.repository.NetworkRepository
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.record.repository.AppEvent
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for syncing data with remote server
 */
@Singleton
class DataSyncService @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val networkUtils: NetworkUtils
) {

    /**
     * Sync app usage data to server
     */
    suspend fun syncAppUsageData(
        userId: String,
        appEvent: List<AppEvent>
    ): SyncResult {
        if (!networkUtils.isNetworkAvailable()) {
            return SyncResult.NoNetwork
        }

        return try {
            val result = networkRepository.syncAppUsageData(
                userId = userId,
                appEvent = appEvent,
                timezone = DateTime.now().toString()
            )

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

    /**
     * Sync hourly usage data to server
     */
    suspend fun syncHourlyUsageData(
        userId: String,
        deviceId: String,
        hourlyAppUsage: Map<Int, List<AppUsage>>
    ): SyncResult {
        if (!networkUtils.isNetworkAvailable()) {
            return SyncResult.NoNetwork
        }

        return try {
            val today = DateTime.now().toString("yyyy-MM-dd")
            val result = networkRepository.syncHourlyUsageData(
                userId = userId,
                deviceId = deviceId,
                hourlyAppUsage = hourlyAppUsage,
                date = today
            )

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
