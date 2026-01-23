package com.app.screentime.sync

import android.content.Context
import android.util.Log
import com.app.screentime.BuildConfig
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.leaderboard.service.LeaderboardService
import com.app.screentime.network.model.AppStatItem
import com.app.screentime.network.model.AppStatsRequest
import com.app.screentime.network.model.BatchUsageEventsRequest
import com.app.screentime.network.model.LeaderboardStatsUpdateRequest
import com.app.screentime.network.model.UsageEvent
import com.app.screentime.network.repository.screentime.ScreenTimeRepository
import com.app.screentime.network.sync.DataSyncService
import com.app.screentime.network.sync.SyncResult
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.utils.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import org.joda.time.Minutes
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for data synchronization
 * Extracted from DataSyncWorker to allow independent usage from any screen
 * Handles all sync operations: usage data, leaderboard stats, etc.
 */
@Singleton
class DataSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val localAppUsageRepository: LocalAppUsageRepository,
    private val screenTimeRepository: ScreenTimeRepository,
    private val dataSyncService: DataSyncService,
    private val leaderboardService: LeaderboardService
) {

    companion object {
        private const val TAG = "DataSyncManager"
        private const val MIN_SYNC_INTERVAL_MINUTES = 1L
    }

    /**
     * Result of a sync operation
     */
    sealed class SyncOperationResult {
        data object Success : SyncOperationResult()
        data object Skipped : SyncOperationResult()
        data class Error(val message: String) : SyncOperationResult()
        data object Retry : SyncOperationResult()
    }

    /**
     * Perform full data sync operation
     * Checks consent, fetches last sync time, and performs incremental sync
     * Also syncs leaderboard stats in parallel
     *
     * @return SyncOperationResult indicating the result of the sync
     */
    suspend fun performFullSync(): SyncOperationResult {
        return try {
            // Google Play Compliance: Only send data if user has accepted consent
            if (!preferencesManager.isConsentScreenShown()) {
                if (BuildConfig.DEBUG) Log.d(TAG, "Consent not given, skipping data sync")
                return SyncOperationResult.Skipped
            }

            val serverLastSyncTime = fetchServerLastSyncTime()

            if (serverLastSyncTime == null) {
                if (BuildConfig.DEBUG) Log.e(TAG, "Failed to fetch server last sync time")
                return SyncOperationResult.Retry
            }

            val nowIst = DateUtils.now()

            val syncStartTime = if (serverLastSyncTime == 0L) {
                if (BuildConfig.DEBUG) Log.d(TAG, "First-time sync → syncing today only (midnight IST to now)")
                DateUtils.startOfToday().millis
            } else {
                val lastSyncIst = DateUtils.fromMillis(serverLastSyncTime)
                val minutesGap = Minutes.minutesBetween(lastSyncIst, nowIst).minutes
                if (BuildConfig.DEBUG) Log.d(TAG, "Gap since last sync: $minutesGap minutes (IST)")

                if (minutesGap < MIN_SYNC_INTERVAL_MINUTES) {
                    if (BuildConfig.DEBUG) Log.d(
                        TAG,
                        "Skipping sync because last sync < $MIN_SYNC_INTERVAL_MINUTES min ago"
                    )
                    return SyncOperationResult.Skipped
                }

                lastSyncIst.millis
            }

            val syncResult = performIncrementalSync(syncStartTime, nowIst.millis)

            // Sync leaderboard stats in parallel (non-blocking)
            syncLeaderboardStats()

            syncResult

        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Sync failed: ${e.message}", e)
            SyncOperationResult.Error(e.message ?: "Unknown error")
        }
    }

    /**
     * Fetch the last sync time from server
     *
     * @return Last sync time in milliseconds, or 0L if first sync, or null if error
     */
    suspend fun fetchServerLastSyncTime(): Long? {
        return try {
            val result = screenTimeRepository.getUsageLastSyncTime()

            if (!result.isSuccess) {
                if (BuildConfig.DEBUG) Log.e(TAG, "API error: ${result.exceptionOrNull()?.message}")
                return null
            }

            val apiResponse = result.getOrNull()
            val data = apiResponse?.data

            if (apiResponse == null || data == null) {
                if (BuildConfig.DEBUG) Log.e(TAG, "API response or data is null")
                return null
            }

            // If server has never synced, lastSyncTime = null
            val lastSyncString = data.lastSyncTime ?: run {
                if (BuildConfig.DEBUG) Log.d(TAG, "Server has no last sync time → returning 0L")
                return 0L
            }

            // Parse ISO 8601 → millis using DateUtils
            return try {
                val timeMs = DateUtils.toMillis(lastSyncString)
                if (timeMs > 0) {
                    if (BuildConfig.DEBUG) Log.d(
                        TAG,
                        "Parsed last sync time: $lastSyncString ($timeMs ms) using DateUtils"
                    )
                    timeMs
                } else {
                    null
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) Log.e(TAG, "Failed to parse lastSyncTime: $lastSyncString", e)
                null
            }

        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Exception while fetching last sync time", e)
            null
        }
    }

    /**
     * Perform incremental sync between two timestamps
     *
     * @param lastSyncTime Start time in milliseconds
     * @param currentTime End time in milliseconds
     * @return SyncOperationResult indicating success or failure
     */
    suspend fun performIncrementalSync(
        lastSyncTime: Long,
        currentTime: Long
    ): SyncOperationResult {
        return try {
            val events = localAppUsageRepository.collectEventsForSync(lastSyncTime, currentTime)

            if (events.isEmpty()) {
                if (BuildConfig.DEBUG) Log.d(TAG, "No events to sync")
                return SyncOperationResult.Success
            }

            val usageEvents = events.map {
                UsageEvent(
                    packageName = it.packageName,
                    appName = it.appName,
                    event = it.event,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    duration = it.duration
                )
            }

            val currentDateTime = DateUtils.fromMillis(currentTime)
            val request = BatchUsageEventsRequest(
                syncTime = DateUtils.formatISO8601(currentDateTime),
                events = usageEvents
            )

            when (val result = dataSyncService.syncBatchUsageEvents(request)) {
                is SyncResult.Success -> {
                    preferencesManager.setLastSyncTime(currentTime)
                    if (BuildConfig.DEBUG) Log.d(TAG, "Successfully synced ${usageEvents.size} events")
                    SyncOperationResult.Success
                }

                else -> {
                    if (BuildConfig.DEBUG) Log.w(TAG, "Sync completed with warnings")
                    SyncOperationResult.Success
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Incremental sync failed: ${e.message}", e)
            SyncOperationResult.Error(e.message ?: "Sync failed")
        }
    }


    suspend fun syncLeaderboardStats(): Boolean {
        return try {
            val now = DateUtils.now()
            val periodDate = DateUtils.format(now, "yyyy-MM-dd")
            val startOfToday = DateUtils.startOfToday()
            val startMillis = startOfToday.millis
            val currentMillis = now.millis

            if (BuildConfig.DEBUG) Log.d(TAG, "Syncing leaderboard stats for date: $periodDate")
            if (BuildConfig.DEBUG) Log.d(
                TAG,
                "Time range: ${DateUtils.formatISO8601(startOfToday)} to ${
                    DateUtils.formatISO8601(now)
                }"
            )

            val appUsageList = localAppUsageRepository.getAppsUsageForInterval(
                startMillis,
                currentMillis
            )

            val totalScreenTime = appUsageList.sumOf { it.appScreenTime }

            if (BuildConfig.DEBUG) Log.d(
                TAG,
                "Total screen time calculated: $totalScreenTime ms (${totalScreenTime / 1000 / 60} minutes)"
            )

            if (totalScreenTime <= 0) {
                if (BuildConfig.DEBUG) Log.d(TAG, "No screen time to sync, skipping")
                return true
            }

            val request = LeaderboardStatsUpdateRequest(
                period = "daily",
                periodDate = periodDate,
                totalScreenTime = totalScreenTime,
                replace = true
            )

            val result = leaderboardService.updateStats(request)

            result.fold(
                onSuccess = {
                    if (BuildConfig.DEBUG) Log.d(TAG, "Successfully synced leaderboard stats for $periodDate")
                    true
                },
                onFailure = { exception ->
                    if (BuildConfig.DEBUG) Log.w(TAG, "Failed to sync leaderboard stats: ${exception.message}")
                    false
                }
            )
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Error syncing leaderboard stats: ${e.message}", e)
            false
        }
    }

    /**
     * Sync app stats using the new /api/app-stats endpoint
     * For the same date, always fetches from 00:00:00 to current time
     * Completely independent sync - doesn't depend on last sync time
     */
    suspend fun syncAppStats(): SyncOperationResult {
        return try {
            // Google Play Compliance: Only send data if user has accepted consent
            if (!preferencesManager.isConsentScreenShown()) {
                if (BuildConfig.DEBUG) Log.d(TAG, "Consent not given, skipping app stats sync")
                return SyncOperationResult.Skipped
            }

            val now = DateUtils.now()
            val todayDate = DateUtils.format(now, "yyyy-MM-dd")
            val startOfToday = DateUtils.startOfToday()
            val startMillis = startOfToday.millis
            val currentMillis = now.millis

            if (BuildConfig.DEBUG) Log.d(TAG, "Syncing app stats for date: $todayDate")
            if (BuildConfig.DEBUG) Log.d(
                TAG,
                "Time range: ${DateUtils.formatISO8601(startOfToday)} to ${DateUtils.formatISO8601(now)}"
            )

            // Fetch app usage from 00:00:00 of today until now
            val appUsageList = localAppUsageRepository.getAppsUsageForInterval(
                startMillis,
                currentMillis
            )

            if (appUsageList.isEmpty()) {
                if (BuildConfig.DEBUG) Log.d(TAG, "No app usage data to sync")
                return SyncOperationResult.Success
            }

            // Filter out apps with zero duration
            val appsWithUsage = appUsageList.filter { it.appScreenTime > 0 }

            if (appsWithUsage.isEmpty()) {
                if (BuildConfig.DEBUG) Log.d(TAG, "No apps with usage time to sync")
                return SyncOperationResult.Success
            }

            // Format stats according to API format
            val stats = appsWithUsage.map { appUsage ->
                AppStatItem(
                    appName = appUsage.appName ?: appUsage.packageName ?: "Unknown",
                    packageName = appUsage.packageName ?: "",
                    duration = appUsage.appScreenTime// Convert to string as required by API
                )
            }

            val request = AppStatsRequest(
                date = todayDate,
                stats = stats
            )

            if (BuildConfig.DEBUG) Log.d(TAG, "Submitting ${stats.size} app stats for date $todayDate")

            val result = screenTimeRepository.submitAppStats(request)

            result.fold(
                onSuccess = { apiResponse ->
                    if (apiResponse.success == true) {
                        if (BuildConfig.DEBUG) Log.d(TAG, "Successfully synced app stats for $todayDate")
                        SyncOperationResult.Success
                    } else {
                        if (BuildConfig.DEBUG) Log.w(TAG, "App stats sync returned success=false: ${apiResponse.message}")
                        SyncOperationResult.Error(apiResponse.message ?: "Unknown error")
                    }
                },
                onFailure = { exception ->
                    if (BuildConfig.DEBUG) Log.e(TAG, "Failed to sync app stats: ${exception.message}", exception)
                    SyncOperationResult.Error(exception.message ?: "Sync failed")
                }
            )
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Error syncing app stats: ${e.message}", e)
            SyncOperationResult.Error(e.message ?: "Unknown error")
        }
    }


}

