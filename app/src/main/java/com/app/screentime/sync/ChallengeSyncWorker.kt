package com.app.screentime.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import java.util.concurrent.TimeUnit

/**
 * WorkManager Worker that syncs challenge stats to the server
 * Runs periodically for each active challenge from challenge start time until end time
 * Uses ChallengeSyncManager for all sync operations
 */
class ChallengeSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    // Create ChallengeSyncManager using object creation (WorkManager doesn't support Hilt injection)
    private val challengeSyncManager by lazy {
        val preferencesManager = PreferencesManager(applicationContext)
        val networkClient = NetworkClient(applicationContext, preferencesManager)
        val challengeRepository = ChallengeRepository(
            com.app.screentime.challenge.service.ChallengeServiceImpl(networkClient)
        )
        val localAppUsageRepository = LocalAppUsageRepository(
            applicationContext,
            ScreenUsageHelper(applicationContext),
            NetworkUsageHelper(applicationContext)
        )
        
        ChallengeSyncManager(
            context = applicationContext,
            preferencesManager = preferencesManager,
            challengeRepository = challengeRepository,
            localAppUsageRepository = localAppUsageRepository
        )
    }

    override suspend fun doWork(): Result {
        return try {
            val syncResult = challengeSyncManager.syncAllChallenges()

            when (syncResult) {
                is ChallengeSyncManager.ChallengeSyncResult.Success -> Result.success()
                is ChallengeSyncManager.ChallengeSyncResult.Skipped -> Result.success()
                is ChallengeSyncManager.ChallengeSyncResult.Error -> {
                    Log.e(TAG, "Challenge sync error: ${syncResult.message}")
                    Result.success() // Don't retry, just log the error
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Challenge sync failed: ${e.message}", e)
            Result.success() // Don't retry, just log the error
        }
    }

    companion object {
        private const val TAG = "ChallengeSyncWorker"
        private const val WORK_NAME = "challenge_sync_work"
        private const val SYNC_INTERVAL_HOURS = 15L

        /**
         * Create a periodic work request for background syncing
         */
        fun schedule(context: Context) {
            val workRequest = periodicWorkRequest()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, workRequest
            )
            Log.d(TAG, "ChallengeSyncWorker: Scheduled periodic sync every $SYNC_INTERVAL_HOURS minutes")
        }

        private fun periodicWorkRequest(): PeriodicWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return PeriodicWorkRequestBuilder<ChallengeSyncWorker>(
                SYNC_INTERVAL_HOURS, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(WORK_NAME)
                .build()
        }
    }
}

