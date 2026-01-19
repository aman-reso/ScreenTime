package com.app.screentime.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import com.app.screentime.BuildConfig
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.leaderboard.service.LeaderboardServiceImpl
import com.app.screentime.network.repository.screentime.ScreenTimeRepository
import com.app.screentime.network.service.screentime.ScreenTimeServiceImpl
import com.app.screentime.network.sync.DataSyncService
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.Dispatcher
import java.util.concurrent.TimeUnit

/**
 * WorkManager Worker that syncs app usage data to the server
 * Runs every 15 minutes and whenever network becomes available
 * Uses DataSyncManager for all sync operations
 */
class DataSyncWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    // Create DataSyncManager using object creation (WorkManager doesn't support Hilt injection)
    private val dataSyncManager by lazy {
        val preferencesManager = PreferencesManager(applicationContext)
        val networkClient = NetworkClient(applicationContext, preferencesManager)
        val localAppUsageRepository = LocalAppUsageRepository(
            applicationContext,
            ScreenUsageHelper(applicationContext),
            NetworkUsageHelper(applicationContext)
        )
        val screenTimeService = ScreenTimeServiceImpl(networkClient)
        val screenTimeRepository = ScreenTimeRepository(screenTimeService)
        val networkUtils = NetworkUtils(applicationContext)
        val dataSyncService = DataSyncService(screenTimeRepository, networkUtils)
        val leaderboardService = LeaderboardServiceImpl(networkClient)

        DataSyncManager(
            context = applicationContext,
            preferencesManager = preferencesManager,
            localAppUsageRepository = localAppUsageRepository,
            screenTimeRepository = screenTimeRepository,
            dataSyncService = dataSyncService,
            leaderboardService = leaderboardService
        )
    }

    override suspend fun doWork(): Result {
        return try {
            val appStatsSync = CoroutineScope(coroutineContext).async(Dispatchers.Default) {
                dataSyncManager.syncAppStats()
            }

            val leaderboardSync = CoroutineScope(coroutineContext).async(Dispatchers.Default) {
                dataSyncManager.syncLeaderboardStats()
            }

            appStatsSync.await()
            leaderboardSync.await()
            return Result.success()
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Sync failed: ${e.message}", e)
            Result.success()
        }
    }

    companion object {
        private const val TAG = "DataSyncWorker"
        private const val WORK_NAME = "data_sync_work"
        private const val SYNC_INTERVAL_MINUTES = 15L

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
            if (BuildConfig.DEBUG) Log.d(
                TAG, "DataSyncWorker: Scheduled periodic sync every $SYNC_INTERVAL_MINUTES minutes"
            )
        }
    }

}

