package com.app.screentime.sync

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.edit
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
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat
import java.util.Calendar
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

    private val packageManager by lazy {
        applicationContext.packageManager
    }

    // Create DataSyncService using object creation instead of injection
    private val dataSyncService by lazy {
        val networkClient = NetworkClient(applicationContext)
        val screenTimeService = ScreenTimeServiceImpl(networkClient)
        val screenTimeRepository = ScreenTimeRepository(screenTimeService)
        val networkUtils = NetworkUtils(applicationContext)
        DataSyncService(screenTimeRepository, networkUtils)
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "DataSyncWorker: Starting batch events sync")

            val currentTime = System.currentTimeMillis()
            var lastSyncedTime = preferencesManager.getLastSyncTime()
            if (lastSyncedTime <= 0) {
                val midNightCal = Calendar.getInstance()
                midNightCal[Calendar.DATE] = -7
                midNightCal[Calendar.HOUR_OF_DAY] = 0
                midNightCal[Calendar.MINUTE] = 0
                midNightCal[Calendar.SECOND] = 0
                midNightCal[Calendar.MILLISECOND] = 0
                lastSyncedTime = midNightCal.timeInMillis
            }
//            if (currentTime - lastSyncedTime < SYNC_INTERVAL_MINUTES * 60 * 1000) {
//                return Result.success()
//            }

            val allEvents = localAppUsageRepository.collectEventsForSync(
                startMsEpoch = lastSyncedTime, endMsEpoch = currentTime
            )

            if (allEvents.isEmpty()) {
                Log.d(TAG, "DataSyncWorker: No app usage events to sync")
                return Result.success()
            }

            Log.d(TAG, "DataSyncWorker: Found ${allEvents.size} events to sync")


            val appEvents = allEvents.map {
                val eventTimestamp = DateTime(it.timestamp, DateTimeZone.UTC)
                val eventTimestampString = ISODateTimeFormat.dateTime().print(eventTimestamp)
                UsageEvent(
                    packageName = it.packageName,
                    appName = it.appName,
                    isSystemApp = false,
                    eventType = it.event,
                    eventTimestamp = eventTimestampString,
                    duration = it.duration// Duration in milliseconds
                )
            }

            if (appEvents.isEmpty()) {
                Log.d(TAG, "DataSyncWorker: No events to sync after conversion")
                return Result.success()
            }

            // Create batch request
            val syncTime = DateTime(currentTime, DateTimeZone.UTC)
            val syncTimeString = ISODateTimeFormat.dateTime().print(syncTime)

            val batchRequest = BatchUsageEventsRequest(
                syncTime = syncTimeString, events = appEvents
            )

            Log.d(TAG, "DataSyncWorker: Syncing ${appEvents.size} events")

            // Sync batch events to server
            when (val syncResult = dataSyncService.syncBatchUsageEvents(batchRequest)) {
                is com.app.screentime.network.sync.SyncResult.Success -> {
                    Log.d(TAG, "DataSyncWorker: Batch events synced successfully")
                    preferencesManager.setLastSyncTime(currentTime)
                    Result.success()
                }

                is com.app.screentime.network.sync.SyncResult.NoNetwork -> {
                    Log.d(TAG, "DataSyncWorker: No network available")
                    Result.retry()
                }

                is com.app.screentime.network.sync.SyncResult.Error -> {
                    Log.e(TAG, "DataSyncWorker: Batch sync failed: ${syncResult.message}")
                    Result.retry()
                }

                else -> {
                    Log.d(TAG, "DataSyncWorker: Sync in progress or other state")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "DataSyncWorker: Unexpected error: ${e.message}", e)
            Result.retry()
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
                WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest
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
