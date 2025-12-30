package com.app.screentime.widget

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.app.screentime.landing.mapper.LandingUiMapper
import com.app.screentime.landing.usecase.LandingUsecase
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.preferences.usecase.PreferencesUseCase
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * Background worker that periodically updates the widget with latest screen time data.
 * Runs even when the app is closed.
 */
@HiltWorker
class WidgetUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

//    private val landingUsecase by lazy {
//        val localAppUsageRepository = LocalAppUsageRepository(
//            applicationContext,
//            ScreenUsageHelper(applicationContext),
//            NetworkUsageHelper(applicationContext)
//        )
//        val preferencesManager = PreferencesManager(applicationContext)
//        val preferencesUseCase = PreferencesUseCase(preferencesManager)
//
//        LandingUsecase(
//            localAppUsageRepository = localAppUsageRepository,
//            landingUiMapper = LandingUiMapper(),
//            preferencesUseCase = preferencesUseCase,
//            context = applicationContext,
//            preferencesManager = preferencesManager,
//            dataSyncService = null
//        )
//    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "WidgetUpdateWorker: Starting widget update")

//            // Fetch today's usage data
//            val todayUsageData = landingUsecase.getTodayUsageData()
//
//            todayUsageData.fold(
//                onSuccess = { usageData ->
//                    // Update widget with latest data
//                    val dailyLimit = 3 * 60 * 60 * 1000L // Default 3 hours in milliseconds
//                    ScreenTimeWidgetHelper.updateWidgetFromAppUsages(
//                        context = applicationContext,
//                        appUsages = usageData.topUsedApps,
//                        dailyLimit = dailyLimit
//                    )
//                    Log.d(
//                        TAG,
//                        "WidgetUpdateWorker: Widget updated successfully. Total usage: ${usageData.todayTotalScreenTime}ms"
//                    )
//                    Result.success()
//                },
//                onFailure = { exception ->
//                    Log.e(
//                        TAG,
//                        "WidgetUpdateWorker: Failed to fetch usage data: ${exception.message}",
//                        exception
//                    )
//                    // Don't retry on permission errors or other persistent failures
//                    if (exception is SecurityException) {
//                        Result.success() // Don't retry permission errors
//                    } else {
//                        Result.retry() // Retry on other errors
//                    }
//                }
//            )
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "WidgetUpdateWorker: Unexpected error: ${e.message}", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "WidgetUpdateWorker"
        private const val WORK_NAME = "widget_update_work"

        // Minimum interval for periodic work is 15 minutes
        // We'll update every 30 minutes to balance battery usage and freshness
        private const val UPDATE_INTERVAL_MINUTES = 15L

        /**
         * Create a periodic work request to update the widget
         */
        fun periodicWorkRequest(): PeriodicWorkRequest {
            // No specific constraints needed - we want it to run even without network
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false) // Allow even on low battery
                .build()

            return PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
                UPDATE_INTERVAL_MINUTES,
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(WORK_NAME)
                .build()
        }

        /**
         * Schedule the periodic widget update work
         */
        fun schedule(context: Context) {
            val workRequest = periodicWorkRequest()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if already scheduled
                workRequest
            )
            Log.d(
                TAG,
                "WidgetUpdateWorker: Scheduled periodic widget updates every $UPDATE_INTERVAL_MINUTES minutes"
            )
        }

        /**
         * Trigger immediate widget update (one-time work)
         */
        fun triggerImmediateUpdate(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<WidgetUpdateWorker>()
                .addTag(WORK_NAME)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
            Log.d(TAG, "WidgetUpdateWorker: Triggered immediate widget update")
        }

        /**
         * Cancel the widget update work
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            Log.d(TAG, "WidgetUpdateWorker: Cancelled widget update work")
        }
    }
}

