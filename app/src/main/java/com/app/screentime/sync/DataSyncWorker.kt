package com.app.screentime.sync

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.hilt.work.HiltWorker
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
import com.app.screentime.network.model.DeviceRegistrationResponse
import com.app.screentime.network.repository.NetworkRepository
import com.app.screentime.network.service.ApiServiceImpl
import com.app.screentime.network.sync.DataSyncService
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import com.app.screentime.utils.DeviceInfoUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

/**
 * WorkManager Worker that syncs app usage data to the server
 * Runs every 15 minutes and whenever network becomes available
 */
@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    private val dataSyncService by lazy {
        val networkClient = NetworkClient()
        val apiService = ApiServiceImpl(networkClient)
        val deviceInfoUtils = DeviceInfoUtils(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)
        val networkRepository = NetworkRepository(apiService, deviceInfoUtils, preferencesManager)
        val networkUtils = NetworkUtils(applicationContext)
        DataSyncService(networkRepository, networkUtils)
    }

    private val localAppUsageRepository by lazy {
        LocalAppUsageRepository(
            applicationContext,
            ScreenUsageHelper(applicationContext),
            NetworkUsageHelper(applicationContext)
        )
    }
    private val preferencesManager = PreferencesManager(applicationContext)

    private fun getUserIdFromRegistration(): String? {
        val userId = preferencesManager.getUserId()
        return if (!userId.isNullOrEmpty()) {
            userId
        } else {
            null
        }
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "DataSyncWorker: Starting data sync")

            val userId = getUserIdFromRegistration()

            if (userId.isNullOrEmpty()) {
                Log.d(TAG, "DataSyncWorker: User not registered, skipping sync")
                return Result.success()
            }

            val lastSyncTime = 0L//sharedPreferences.getLong(KEY_LAST_SYNC_TIME, 0L)
            val currentTime = System.currentTimeMillis()

            val startTime = if (lastSyncTime == 0L) {
                val today = currentTime
                today - (today % (24 * 60 * 60 * 1000))
            } else {
                lastSyncTime
            }

            val endTime = currentTime

            Log.d(
                TAG,
                "DataSyncWorker: Fetching data from $startTime to $endTime (last sync: $lastSyncTime)"
            )

            val appUsages = localAppUsageRepository.collectEventsForSync(
                startMsEpoch = startTime,
                endMsEpoch = endTime
            )

            if (appUsages.isEmpty()) {
                Log.d(TAG, "DataSyncWorker: No app usage data to sync")
                // Update last sync time even if no data (to avoid fetching same data again)
                return Result.success()
            }

            Log.d(
                TAG,
                "DataSyncWorker: Syncing ${appUsages.size} app usage records for userId: $userId"
            )

            // Sync data to server
            val syncResult = dataSyncService.syncAppUsageData(userId, appUsages)

            when (syncResult) {
                is com.app.screentime.network.sync.SyncResult.Success -> {
                    Log.d(TAG, "DataSyncWorker: Data synced successfully")
                    // Update last sync time after successful sync
//                    sharedPreferences.edit {
//                        putLong(KEY_LAST_SYNC_TIME, currentTime)
//                    }
                    Result.success()
                }

                is com.app.screentime.network.sync.SyncResult.NoNetwork -> {
                    Log.d(TAG, "DataSyncWorker: No network available")
                    Result.retry()
                }

                is com.app.screentime.network.sync.SyncResult.Error -> {
                    Log.e(TAG, "DataSyncWorker: Sync failed: ${syncResult.message}")
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
        private const val PREFS_NAME = "screentime_prefs"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"

        fun oneTimeWorkRequest(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return OneTimeWorkRequestBuilder<DataSyncWorker>()
                .setConstraints(constraints)
                .addTag(WORK_NAME)
                .build()
        }

        fun periodicWorkRequest(): PeriodicWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return PeriodicWorkRequestBuilder<DataSyncWorker>(
                SYNC_INTERVAL_MINUTES,
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(WORK_NAME)
                .build()
        }

        /**
         * Schedule periodic data sync work
         */
        fun schedule(context: Context) {
            val workRequest = periodicWorkRequest()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
            Log.d(
                TAG,
                "DataSyncWorker: Scheduled periodic sync every $SYNC_INTERVAL_MINUTES minutes"
            )
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
