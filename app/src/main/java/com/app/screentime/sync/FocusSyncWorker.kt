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
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.repository.FocusTimeRepository
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.network.model.FocusModeStatsSyncRequest
import com.app.screentime.network.repository.focus.FocusRepository
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.network.service.focus.FocusService
import com.app.screentime.network.service.focus.FocusServiceImpl
import com.app.screentime.network.sync.SyncResult
import com.app.screentime.network.utils.NetworkUtils
import java.util.concurrent.TimeUnit

/**
 * WorkManager Worker that syncs focus mode stats to the server
 * Runs every 15 minutes and whenever network becomes available
 */
class FocusSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val focusTimeRepository by lazy {
        val database = ScreenTimeDatabase.getDatabase(appContext)
        FocusTimeRepository(database.focusTimeDao())
    }

    private val preferencesManager by lazy {
        PreferencesManager(appContext)
    }

    private val focusRepository by lazy {
        val networkClient = NetworkClient(appContext, preferencesManager)
        val focusService = FocusServiceImpl(networkClient)
        FocusRepository(focusService)
    }

    private val networkUtils by lazy {
        NetworkUtils(appContext)
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "FocusSyncWorker: Starting focus mode stats sync")

            if (!networkUtils.isNetworkAvailable()) {
                Log.d(TAG, "FocusSyncWorker: No network available")
                return Result.retry()
            }

            val currentTime = System.currentTimeMillis()
            val sevenDaysInMillis = 7L * 24 * 60 * 60 * 1000
            val sevenDaysAgo = currentTime - sevenDaysInMillis

            // Get all completed focus sessions from last 7 days
            val focusSessions = focusTimeRepository.getFocusSessionsByDateRange(
                startDate = sevenDaysAgo,
                endDate = currentTime
            ).filter { it.completed && it.endTime != null }

            if (focusSessions.isEmpty()) {
                Log.d(TAG, "FocusSyncWorker: No focus sessions to sync")
                return Result.success()
            }

            Log.d(TAG, "FocusSyncWorker: Found ${focusSessions.size} focus sessions to sync")

            // Get the last sync time
            val lastSyncTime = preferencesManager.getLastFocusSyncTime() ?: sevenDaysAgo

            // Sync each session that hasn't been synced yet
            var syncedCount = 0
            var failedCount = 0

            for (session in focusSessions) {
                // Only sync sessions that ended after the last sync time
                if (session.endTime != null && session.endTime > lastSyncTime) {
                    val syncRequest = FocusModeStatsSyncRequest(
                        startTime = session.startTime,
                        endTime = session.endTime
                    )

                    val syncResult = focusRepository.syncFocusModeStats(syncRequest)

                    when (syncResult) {
                        is Result.Success -> {
                            val apiResponse = syncResult.getOrNull()
                            if (apiResponse?.data?.success == true) {
                                syncedCount++
                                Log.d(
                                    TAG,
                                    "FocusSyncWorker: Synced session ${session.id} successfully"
                                )
                            } else {
                                failedCount++
                                Log.e(
                                    TAG,
                                    "FocusSyncWorker: Failed to sync session ${session.id}: ${apiResponse?.data?.message}"
                                )
                            }
                        }

                        is Result.Failure -> {
                            failedCount++
                            Log.e(
                                TAG,
                                "FocusSyncWorker: Failed to sync session ${session.id}: ${syncResult.exceptionOrNull()?.message}"
                            )
                        }
                    }
                }
            }

            if (syncedCount > 0) {
                // Update last sync time to current time
                preferencesManager.setLastFocusSyncTime(currentTime)
                Log.d(TAG, "FocusSyncWorker: Successfully synced $syncedCount focus sessions")
            }

            if (failedCount > 0) {
                Log.w(TAG, "FocusSyncWorker: Failed to sync $failedCount focus sessions")
                return Result.retry()
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "FocusSyncWorker: Unexpected error: ${e.message}", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "FocusSyncWorker"
        private const val WORK_NAME = "focus_sync_work"
        private const val SYNC_INTERVAL_MINUTES = 15L

        fun oneTimeWorkRequest(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return OneTimeWorkRequestBuilder<FocusSyncWorker>()
                .setConstraints(constraints)
                .addTag(WORK_NAME)
                .build()
        }

        fun periodicWorkRequest(): PeriodicWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return PeriodicWorkRequestBuilder<FocusSyncWorker>(
                SYNC_INTERVAL_MINUTES,
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(WORK_NAME)
                .build()
        }

        /**
         * Schedule periodic focus stats sync work
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
                "FocusSyncWorker: Scheduled periodic sync every $SYNC_INTERVAL_MINUTES minutes"
            )
        }

        /**
         * Manually trigger a one-time sync (useful for testing)
         */
        fun triggerSync(context: Context) {
            val workRequest = oneTimeWorkRequest()
            WorkManager.getInstance(context).enqueue(workRequest)
            Log.d(TAG, "FocusSyncWorker: Manually triggered one-time sync")
        }

        /**
         * Cancel all focus sync work
         */
        fun cancelAll(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            Log.d(TAG, "FocusSyncWorker: All focus sync work cancelled")
        }
    }
}

