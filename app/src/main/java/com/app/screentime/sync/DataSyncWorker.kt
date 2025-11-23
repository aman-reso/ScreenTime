package com.app.screentime.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.model.BatchUsageEventsRequest
import com.app.screentime.network.model.UsageEvent
import com.app.screentime.network.repository.screentime.ScreenTimeRepository
import com.app.screentime.network.service.screentime.ScreenTimeServiceImpl
import com.app.screentime.network.sync.DataSyncService
import com.app.screentime.network.sync.SyncResult
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import org.joda.time.Minutes
import java.util.concurrent.TimeUnit

/**
 * WorkManager Worker that syncs app usage data to the server
 * Runs every 15 minutes and whenever network becomes available
 * Uses object creation instead of dependency injection
 */
class DataSyncWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val localAppUsageRepository by lazy {
        LocalAppUsageRepository(
            applicationContext,
            ScreenUsageHelper(applicationContext),
            NetworkUsageHelper(applicationContext)
        )
    }

    private val preferencesManager by lazy {
        PreferencesManager(applicationContext)
    }

    // Create DataSyncService using object creation instead of injection
    private val dataSyncService by lazy {
        val networkClient = NetworkClient(applicationContext)
        val screenTimeService = ScreenTimeServiceImpl(networkClient)
        val screenTimeRepository = ScreenTimeRepository(screenTimeService)
        val networkUtils = NetworkUtils(applicationContext)
        DataSyncService(screenTimeRepository, networkUtils)
    }

    // Create ScreenTimeRepository for fetching last sync time
    private val screenTimeRepository by lazy {
        val networkClient = NetworkClient(applicationContext)
        val screenTimeService = ScreenTimeServiceImpl(networkClient)
        ScreenTimeRepository(screenTimeService)
    }

    override suspend fun doWork(): Result {
        return try {
            val serverLastSyncTime = fetchServerLastSyncTime()

            if (serverLastSyncTime == null) {
                Log.e(TAG, "Failed to fetch server last sync time, retrying")
                return Result.retry()
            }

            val nowIst = com.app.screentime.utils.DateUtils.now()
            
            if (serverLastSyncTime == 0L) {
                Log.d(TAG, "First-time sync → syncing today only (midnight IST to now)")
                val midnightIst = com.app.screentime.utils.DateUtils.startOfToday().millis

                return incrementalSync(midnightIst, nowIst.millis)
            }

            val lastSyncIst = com.app.screentime.utils.DateUtils.fromMillis(serverLastSyncTime)

            val minutesGap = org.joda.time.Minutes.minutesBetween(lastSyncIst, nowIst).minutes
            Log.d(TAG, "Gap since last sync: $minutesGap minutes (IST)")

            if (minutesGap < 15) {
                Log.d(TAG, "Skipping sync because last sync < 15 min ago")
                return Result.success()
            }

            return incrementalSync(lastSyncIst.millis, nowIst.millis)

        } catch (e: Exception) {
            Log.e(TAG, "Sync failed: ${e.message}", e)
            Result.retry()
        }
    }

    private suspend fun fetchServerLastSyncTime(): Long? {
        return try {
            val result = screenTimeRepository.getUsageLastSyncTime()

            if (!result.isSuccess) {
                Log.e(TAG, "API error: ${result.exceptionOrNull()?.message}")
                return null
            }

            val apiResponse = result.getOrNull()
            val data = apiResponse?.data

            if (apiResponse == null || data == null) {
                Log.e(TAG, "API response or data is null")
                return null
            }

            // If server has never synced, lastSyncTime = null
            val lastSyncString = data.lastSyncTime ?: run {
                Log.d(TAG, "Server has no last sync time → returning 0L")
                return 0L
            }

            // Parse ISO 8601 → millis using DateUtils
            return try {
                val timeMs = com.app.screentime.utils.DateUtils.toMillis(lastSyncString)
                if (timeMs > 0) {
                    Log.d(TAG, "Parsed last sync time: $lastSyncString ($timeMs ms) using DateUtils")
                    timeMs
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse lastSyncTime: $lastSyncString", e)
                null
            }

        } catch (e: Exception) {
            Log.e(TAG, "Exception while fetching last sync time", e)
            null
        }
    }


    private suspend fun incrementalSync(
        lastSyncTime: Long,
        currentTime: Long
    ): Result {
        val events = localAppUsageRepository.collectEventsForSync(lastSyncTime, currentTime)

        if (events.isEmpty()) return Result.success()

        val usageEvents = events.map {
            val eventDateTime = com.app.screentime.utils.DateUtils.fromMillis(it.timestamp)
            UsageEvent(
                packageName = it.packageName,
                appName = it.appName,
                isSystemApp = false,
                eventType = it.event,
                eventTimestamp = com.app.screentime.utils.DateUtils.formatISO8601(eventDateTime),
                duration = it.duration
            )
        }

        val currentDateTime = com.app.screentime.utils.DateUtils.fromMillis(currentTime)
        val request = BatchUsageEventsRequest(
            syncTime = com.app.screentime.utils.DateUtils.formatISO8601(currentDateTime),
            events = usageEvents
        )

        return when (val result = dataSyncService.syncBatchUsageEvents(request)) {
            is SyncResult.Success -> {
                preferencesManager.setLastSyncTime(currentTime)
                Result.success()
            }

            else -> {
                Result.retry()
            }
        }
    }


    companion object {
        private const val TAG = "DataSyncWorker"
        private const val WORK_NAME = "data_sync_work"
        private const val SYNC_INTERVAL_MINUTES = 15L

        fun oneTimeWorkRequest(): OneTimeWorkRequest {
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            return OneTimeWorkRequestBuilder<DataSyncWorker>().setConstraints(constraints)
                .addTag(WORK_NAME).build()
        }

        fun periodicWorkRequest(): PeriodicWorkRequest {
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            return PeriodicWorkRequestBuilder<DataSyncWorker>(
                SYNC_INTERVAL_MINUTES, TimeUnit.MINUTES
            ).setConstraints(constraints).addTag(WORK_NAME).build()
        }

        /**
         * Schedule periodic data sync work
         */
        fun schedule(context: Context) {
            val workRequest = periodicWorkRequest()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, workRequest
            )
            Log.d(
                TAG, "DataSyncWorker: Scheduled periodic sync every $SYNC_INTERVAL_MINUTES minutes"
            )
        }

        /**
         * Manually trigger a one-time sync (useful for testing)
         */
        fun triggerSync(context: Context) {
            val workRequest = oneTimeWorkRequest()
            WorkManager.getInstance(context).enqueue(workRequest)
            Log.d(TAG, "DataSyncWorker: Manually triggered one-time sync")
        }

        /**
         * Cancel all sync work
         */
        fun cancelAll(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            Log.d(TAG, "DataSyncWorker: All sync work cancelled")
        }
    }

}

