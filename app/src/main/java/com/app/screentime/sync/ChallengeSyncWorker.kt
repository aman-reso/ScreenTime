package com.app.screentime.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.network.model.BatchChallengeStatsRequest
import com.app.screentime.network.model.ChallengeStatsRequest
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import com.app.screentime.sync.DataSyncWorker.Companion.periodicWorkRequest
import com.app.screentime.utils.DateUtils

/**
 * WorkManager Worker that syncs challenge stats to the server
 * Runs hourly for each active challenge from challenge start time until end time
 */
class ChallengeSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val preferencesManager by lazy {
        PreferencesManager(applicationContext)
    }

    private val challengeRepository by lazy {
        ChallengeRepository(
            com.app.screentime.challenge.service.ChallengeServiceImpl(
                NetworkClient(applicationContext, preferencesManager)
            )
        )
    }

    private val localAppUsageRepository by lazy {
        LocalAppUsageRepository(
            applicationContext,
            ScreenUsageHelper(applicationContext),
            NetworkUsageHelper(applicationContext)
        )
    }


    override suspend fun doWork(): Result {
        return try {
            if (!preferencesManager.isConsentScreenShown()) {
                Log.d(TAG, "ChallengeSyncWorker: Consent not given, skipping challenge stats sync")
                return Result.success()
            }

            // Always fetch joined challenges from server
            val userChallengesResult = challengeRepository.getUserChallenges()
            val userChallenges = userChallengesResult.fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        response.data!!.challenges
                    } else {
                        Log.w(TAG, "Failed to fetch user challenges: ${response.message}")
                        emptyList()
                    }
                },
                onFailure = { throwable ->
                    Log.e(TAG, "Error fetching user challenges from server", throwable)
                    emptyList()
                }
            )

            if (userChallenges.isEmpty()) {
                Log.d(TAG, "ChallengeSyncWorker: No joined challenges found from server")
                return Result.success()
            }

            Log.d(
                TAG,
                "ChallengeSyncWorker: Processing ${userChallenges.size} joined challenges from server"
            )

            val currentTime = DateUtils.nowMillis()
            var hasErrors = false

            for (userChallenge in userChallenges) {
                try {
                    val challengeId = userChallenge.id
                    val startTime = parseISO8601(userChallenge.startTime)
                    val endTime = parseISO8601(userChallenge.endTime)

                    if (currentTime < startTime) {
                        Log.d(
                            TAG,
                            "ChallengeSyncWorker: Challenge $challengeId hasn't started yet (start: $startTime, current: $currentTime)"
                        )
                        continue
                    }

                    if (currentTime > endTime) {
                        Log.d(
                            TAG,
                            "ChallengeSyncWorker: Challenge $challengeId has ended (end: $endTime, current: $currentTime)"
                        )
                        continue
                    }

                    var serverLastSyncTime: Long? = null
                    val lastSyncResult = challengeRepository.getChallengeLastSyncTime(challengeId)
                    val lastSyncSuccess = lastSyncResult.fold(
                        onSuccess = { response ->
                            if (response.success == true && response.data != null) {
                                val lastSyncTimeStr = response.data!!.lastSyncTime
                                if (lastSyncTimeStr != null) {
                                    serverLastSyncTime = parseISO8601(lastSyncTimeStr)
                                    Log.d(
                                        TAG,
                                        "ChallengeSyncWorker: Server last sync time for challenge $challengeId: $serverLastSyncTime"
                                    )
                                    true
                                } else {
                                    Log.d(
                                        TAG,
                                        "ChallengeSyncWorker: No previous sync found on server for challenge $challengeId"
                                    )
                                    true
                                }
                            } else {
                                false
                            }
                        },
                        onFailure = { throwable ->
                            Log.e(
                                TAG,
                                "ChallengeSyncWorker: Failed to fetch last sync time from server for challenge $challengeId",
                                throwable
                            )
                            false
                        }
                    )

                    if (!lastSyncSuccess) {
                        Log.e(
                            TAG,
                            "ChallengeSyncWorker: Failed to get last sync time for challenge $challengeId, marking as failed and continuing to next challenge"
                        )
                        hasErrors = true
                        continue
                    }

                    var packageNames: String? = null
                    val detailsResult = challengeRepository.getChallengeDetails(challengeId)
                    detailsResult.fold(
                        onSuccess = { detailsResponse ->
                            if (detailsResponse.success == true && detailsResponse.data != null) {
                                packageNames = detailsResponse.data!!.packageNames
                            }
                        },
                        onFailure = { throwable ->
                            Log.w(
                                TAG,
                                "Failed to fetch challenge details for $challengeId, continuing without package names",
                                throwable
                            )
                        }
                    )

                    val allowedPackageNames = if (packageNames.isNullOrBlank()) {
                        emptySet()
                    } else {
                        packageNames.split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                            .toSet()
                    }

                    if (allowedPackageNames.isEmpty()) {
                        Log.d(
                            TAG,
                            "ChallengeSyncWorker: No package names specified for challenge $challengeId, skipping sync"
                        )
                        continue
                    }

                    Log.d(
                        TAG,
                        "ChallengeSyncWorker: Allowed package names for challenge $challengeId: $allowedPackageNames"
                    )

                    // Calculate effective last sync time (only use server sync time or start time)
                    val effectiveLastSyncTime = if (serverLastSyncTime != null) {
                        maxOf(serverLastSyncTime, startTime)
                    } else {
                        startTime
                    }
                    val syncEndTime = minOf(currentTime, endTime)

                    if (syncEndTime <= effectiveLastSyncTime) {
                        Log.d(
                            TAG,
                            "ChallengeSyncWorker: No new data to sync for challenge $challengeId (last sync: $effectiveLastSyncTime, end: $syncEndTime)"
                        )
                        continue
                    }

                    Log.d(
                        TAG,
                        "ChallengeSyncWorker: Syncing challenge $challengeId from $effectiveLastSyncTime to $syncEndTime"
                    )

                    val appUsageList = localAppUsageRepository.getAppsUsageForInterval(
                        effectiveLastSyncTime,
                        syncEndTime
                    )

                    if (appUsageList.isEmpty()) {
                        Log.d(
                            TAG,
                            "ChallengeSyncWorker: No app usage data found for challenge $challengeId"
                        )
                        continue
                    }

                    val filteredAppUsage = appUsageList.filter { appUsage ->
                        val packageName = appUsage.packageName?.trim() ?: ""
                        packageName.isNotEmpty() && allowedPackageNames.contains(packageName)
                    }

                    if (filteredAppUsage.isEmpty()) {
                        Log.d(
                            TAG,
                            "ChallengeSyncWorker: No matching app usage found for challenge $challengeId with allowed package names"
                        )
                        continue
                    }

                    Log.d(
                        TAG,
                        "ChallengeSyncWorker: Filtered ${filteredAppUsage.size} app usage entries from ${appUsageList.size} total for challenge $challengeId"
                    )

                    val groupedByPackage = filteredAppUsage.groupBy { it.packageName ?: "" }
                        .mapValues { (_, usages) ->
                            usages.sumOf { it.appScreenTime }
                        }
                        .filter { (packageName, duration) ->
                            packageName.isNotEmpty() && duration > 0
                        }

                    if (groupedByPackage.isEmpty()) {
                        Log.d(
                            TAG,
                            "ChallengeSyncWorker: No valid package usage found for challenge $challengeId"
                        )
                        continue
                    }

                    val allPackageNames = groupedByPackage.keys.sorted().joinToString(",")
                    val totalDuration = groupedByPackage.values.sum()
                    val firstPackageName = groupedByPackage.keys.first()
                    val firstUsage =
                        filteredAppUsage.firstOrNull { it.packageName == firstPackageName }
                    val appName = firstUsage?.appName ?: "Unknown"

                    val statsRequests = listOf(
                        ChallengeStatsRequest(
                            challengeId = challengeId,
                            appName = appName,
                            packageName = allPackageNames,
                            startSyncTime = DateUtils.formatISO8601(
                                DateUtils.fromMillis(effectiveLastSyncTime)
                            ),
                            endSyncTime = DateUtils.formatISO8601(DateUtils.fromMillis(syncEndTime)),
                            duration = totalDuration
                        )
                    )

                    val batchRequest = BatchChallengeStatsRequest(
                        challengeId = challengeId,
                        stats = statsRequests
                    )

                    val result = challengeRepository.submitBatchChallengeStats(batchRequest)
                    result.fold(
                        onSuccess = { response ->
                            if (response.success == true) {
                                Log.d(
                                    TAG,
                                    "ChallengeSyncWorker: Successfully synced ${statsRequests.size} stats for challenge $challengeId"
                                )
                            } else {
                                Log.e(
                                    TAG,
                                    "ChallengeSyncWorker: Failed to sync challenge $challengeId: ${response.message}"
                                )
                                hasErrors = true
                            }
                        },
                        onFailure = { throwable ->
                            Log.e(
                                TAG,
                                "ChallengeSyncWorker: Error syncing challenge $challengeId",
                                throwable
                            )
                            hasErrors = true
                        }
                    )

                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "ChallengeSyncWorker: Error processing challenge ${userChallenge.id}",
                        e
                    )
                    hasErrors = true
                }
            }

            if (hasErrors) {
                Result.retry()
            } else {
                Result.success()
            }

        } catch (e: Exception) {
            Log.e(TAG, "ChallengeSyncWorker: Fatal error", e)
            Result.retry()
        }
    }

    private fun parseISO8601(isoString: String): Long {
        return try {
            DateUtils.toMillis(isoString)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse ISO8601: $isoString", e)
            DateUtils.now().millis
        }
    }

    companion object {
        private const val TAG = "ChallengeSyncWorker"
        private const val WORK_NAME_PREFIX = "challenge_sync"
        private const val SYNC_INTERVAL_HOURS = 1L


        private fun parseISO8601(isoString: String): Long {
            return try {
                DateUtils.toMillis(isoString)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse ISO8601: $isoString", e)
                DateUtils.now().millis
            }
        }

        fun schedule(context: Context) {
            val workRequest = periodicWorkRequest()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME_PREFIX, ExistingPeriodicWorkPolicy.UPDATE, workRequest
            )
        }
    }
}

