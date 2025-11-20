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
import com.app.screentime.network.sync.SyncResult
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat
import java.util.Calendar
import java.util.Date
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
            val currentTime = System.currentTimeMillis()
            val lastSyncTime = preferencesManager.getLastSyncTime()

            // 1. If synced recently (< 15 min) → skip
            if (lastSyncTime > 0 &&
                currentTime - lastSyncTime < 15 * 60 * 1000L
            ) {
                Log.d(TAG, "Skipping sync (last sync < 15 minutes)")
                return Result.success()
            }

            // 2. Never sync more than last 3 days
            val threeDaysMs = 3 * 24 * 60 * 60 * 1000L

            // ❌ First-time sync
            if (lastSyncTime <= 0) {
                Log.d(TAG, "First-time sync → syncing last 3 days")
                return syncLastNDaysSequentially(3, currentTime)
            }

            // ❌ Too old, older than 3 days
            if (currentTime - lastSyncTime > threeDaysMs) {
                Log.d(TAG, "Last sync older than 3 days → syncing only last 3 days")
                return syncLastNDaysSequentially(3, currentTime)
            }

            // 3. Normal incremental sync
            return incrementalSync(lastSyncTime, currentTime)

        } catch (e: Exception) {
            Log.e(TAG, "Sync failed: ${e.message}", e)
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

    private suspend fun syncLastNDaysSequentially(
        days: Int,
        currentTime: Long
    ): Result {

        val oneDay = 24 * 60 * 60 * 1000L
        var lastSuccessfulTime = 0L

        // Build list for last N days
        val dayRanges = (days downTo 1).map { dayOffset ->
            val approx = currentTime - dayOffset * oneDay
            val dayStart = getMidnight(approx)
            val dayEnd = minOf(dayStart + oneDay - 1, currentTime)
            dayStart to dayEnd
        }

        for ((index, range) in dayRanges.withIndex()) {
            val (start, end) = range

            Log.d(TAG, "Syncing day ${index + 1}/$days → ${Date(start)}")

            val events = localAppUsageRepository.collectEventsForSync(start, end)

            // Convert events
            val usageEvents = events.map {
                UsageEvent(
                    packageName = it.packageName,
                    appName = it.appName,
                    isSystemApp = false,
                    eventType = it.event,
                    eventTimestamp = ISODateTimeFormat.dateTime().print(DateTime(it.timestamp)),
                    duration = it.duration
                )
            }

            val request = BatchUsageEventsRequest(
                syncTime = ISODateTimeFormat.dateTime().print(DateTime(end)),
                events = usageEvents
            )

            // Sync this specific day
            when (val result = dataSyncService.syncBatchUsageEvents(request)) {
                is SyncResult.Success -> {
                    Log.d(TAG, "Day ${index + 1} synced OK")
                    preferencesManager.setLastSyncTime(end)  // update immediately
                    lastSuccessfulTime = end
                }

                is SyncResult.NoNetwork,
                is SyncResult.Error -> {
                    Log.e(TAG, "Failed day ${index + 1}, stopping…")
                    if (lastSuccessfulTime > 0) {
                        preferencesManager.setLastSyncTime(lastSuccessfulTime)
                    }
                    return Result.retry()
                }

                else -> {
                    if (lastSuccessfulTime > 0) {
                        preferencesManager.setLastSyncTime(lastSuccessfulTime)
                    }
                }
            }
        }

        Log.d(TAG, "All $days days synced successfully")
        return Result.success()
    }


    private suspend fun incrementalSync(
        lastSyncTime: Long,
        currentTime: Long
    ): Result {

        val events = localAppUsageRepository.collectEventsForSync(lastSyncTime, currentTime)

        if (events.isEmpty()) return Result.success()

        val usageEvents = events.map {
            UsageEvent(
                packageName = it.packageName,
                appName = it.appName,
                isSystemApp = false,
                eventType = it.event,
                eventTimestamp = ISODateTimeFormat.dateTime().print(DateTime(it.timestamp)),
                duration = it.duration
            )
        }

        val request = BatchUsageEventsRequest(
            syncTime = ISODateTimeFormat.dateTime().print(DateTime(currentTime)),
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

    private fun getMidnight(time: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }


}

