package com.app.screentime.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.repository.JoinedChallengeRepository
import com.app.screentime.network.model.BatchChallengeStatsRequest
import com.app.screentime.network.model.ChallengeStatsRequest
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.record.repository.ScreenUsageHelper
import com.app.screentime.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * WorkManager Worker that syncs challenge stats to the server
 * Runs hourly for each active challenge from challenge start time until end time
 */
class ChallengeSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val database by lazy {
        ScreenTimeDatabase.getDatabase(applicationContext)
    }

    private val joinedChallengeRepository by lazy {
        JoinedChallengeRepository(database.joinedChallengeDao())
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

    private val preferencesManager by lazy {
        PreferencesManager(applicationContext)
    }

    override suspend fun doWork(): Result {
        return try {
            // Google Play Compliance: Only send data if user has accepted consent
            if (!preferencesManager.isConsentScreenShown()) {
                Log.d(TAG, "ChallengeSyncWorker: Consent not given, skipping challenge stats sync")
                return Result.success() // Don't retry, just skip
            }
            
            Log.d(TAG, "ChallengeSyncWorker: Starting sync")

            // Get all active challenges
            val activeChallenges = joinedChallengeRepository.getActiveChallenges()
            
            if (activeChallenges.isEmpty()) {
                Log.d(TAG, "ChallengeSyncWorker: No active challenges found")
                return Result.success()
            }

            val currentTime = System.currentTimeMillis()
            var hasErrors = false

            for (challenge in activeChallenges) {
                try {
                    // Parse challenge times
                    val startTime = parseISO8601(challenge.startTime)
                    val endTime = parseISO8601(challenge.endTime)

                    // Check if challenge has ended
                    if (currentTime > endTime) {
                        Log.d(TAG, "ChallengeSyncWorker: Challenge ${challenge.challengeId} has ended, marking as inactive")
                        joinedChallengeRepository.updateSyncScheduled(challenge.challengeId, false)
                        cancelChallengeWork(applicationContext, challenge.challengeId)
                        continue
                    }

                    // Check if challenge has started
                    if (currentTime < startTime) {
                        Log.d(TAG, "ChallengeSyncWorker: Challenge ${challenge.challengeId} hasn't started yet")
                        continue
                    }

                    // Fetch last sync time from server
                    var serverLastSyncTime: Long? = null
                    try {
                        val lastSyncResult = challengeRepository.getChallengeLastSyncTime(challenge.challengeId)
                        lastSyncResult.fold(
                            onSuccess = { response ->
                                if (response.success == true && response.data != null) {
                                    val lastSyncTimeStr = response.data!!.lastSyncTime
                                    if (lastSyncTimeStr != null) {
                                        serverLastSyncTime = parseISO8601(lastSyncTimeStr)
                                        Log.d(TAG, "ChallengeSyncWorker: Server last sync time for challenge ${challenge.challengeId}: $serverLastSyncTime")
                                    } else {
                                        Log.d(TAG, "ChallengeSyncWorker: No previous sync found on server for challenge ${challenge.challengeId}")
                                    }
                                }
                            },
                            onFailure = { throwable ->
                                Log.w(TAG, "ChallengeSyncWorker: Failed to fetch last sync time from server for challenge ${challenge.challengeId}, using local value", throwable)
                            }
                        )
                    } catch (e: Exception) {
                        Log.w(TAG, "ChallengeSyncWorker: Error fetching last sync time from server for challenge ${challenge.challengeId}, using local value", e)
                    }

                    // Determine sync time range - use the maximum of server last sync, local last sync, and challenge start time
                    val localLastSyncTime = if (challenge.lastSyncTime > 0) challenge.lastSyncTime else 0L
                    val effectiveLastSyncTime = when {
                        serverLastSyncTime != null -> maxOf(serverLastSyncTime!!, localLastSyncTime, startTime)
                        localLastSyncTime > 0 -> maxOf(localLastSyncTime, startTime)
                        else -> startTime
                    }

                    // Sync from last sync time to now (or end time, whichever is earlier)
                    val syncEndTime = minOf(currentTime, endTime)

                    if (syncEndTime <= effectiveLastSyncTime) {
                        Log.d(TAG, "ChallengeSyncWorker: No new data to sync for challenge ${challenge.challengeId} (last sync: $effectiveLastSyncTime, end: $syncEndTime)")
                        continue
                    }

                    Log.d(TAG, "ChallengeSyncWorker: Syncing challenge ${challenge.challengeId} from $effectiveLastSyncTime to $syncEndTime")

                    // Get allowed package names for this challenge
                    val allowedPackageNames = if (challenge.packageNames.isNullOrBlank()) {
                        emptySet()
                    } else {
                        challenge.packageNames.split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                            .toSet()
                    }

                    if (allowedPackageNames.isEmpty()) {
                        Log.d(TAG, "ChallengeSyncWorker: No package names specified for challenge ${challenge.challengeId}, skipping sync")
                        continue
                    }

                    Log.d(TAG, "ChallengeSyncWorker: Allowed package names for challenge ${challenge.challengeId}: $allowedPackageNames")

                    // Collect app usage data
                    val appUsageList = localAppUsageRepository.getAppsUsageForInterval(
                        effectiveLastSyncTime,
                        syncEndTime
                    )

                    if (appUsageList.isEmpty()) {
                        Log.d(TAG, "ChallengeSyncWorker: No app usage data found for challenge ${challenge.challengeId}")
                        // Update last sync time even if no data
                        joinedChallengeRepository.updateLastSyncTime(challenge.challengeId, syncEndTime)
                        continue
                    }

                    // Filter app usage to only include allowed package names
                    val filteredAppUsage = appUsageList.filter { appUsage ->
                        val packageName = appUsage.packageName?.trim() ?: ""
                        packageName.isNotEmpty() && allowedPackageNames.contains(packageName)
                    }

                    if (filteredAppUsage.isEmpty()) {
                        Log.d(TAG, "ChallengeSyncWorker: No matching app usage found for challenge ${challenge.challengeId} with allowed package names")
                        // Update last sync time even if no matching data
                        joinedChallengeRepository.updateLastSyncTime(challenge.challengeId, syncEndTime)
                        continue
                    }

                    Log.d(TAG, "ChallengeSyncWorker: Filtered ${filteredAppUsage.size} app usage entries from ${appUsageList.size} total for challenge ${challenge.challengeId}")

                    // Group app usage by package name and aggregate durations
                    val groupedByPackage = filteredAppUsage.groupBy { it.packageName ?: "" }
                        .mapValues { (_, usages) ->
                            usages.sumOf { it.appScreenTime }
                        }
                        .filter { (packageName, duration) ->
                            packageName.isNotEmpty() && duration > 0
                        }

                    if (groupedByPackage.isEmpty()) {
                        Log.d(TAG, "ChallengeSyncWorker: No valid package usage found for challenge ${challenge.challengeId}")
                        joinedChallengeRepository.updateLastSyncTime(challenge.challengeId, syncEndTime)
                        continue
                    }

                    // Create a single request with comma-separated package names
                    // Aggregate all durations for the combined request
                    val allPackageNames = groupedByPackage.keys.sorted().joinToString(",")
                    val totalDuration = groupedByPackage.values.sum()
                    
                    // Get app names - use first app name or combine if needed
                    val firstPackageName = groupedByPackage.keys.first()
                    val firstUsage = filteredAppUsage.firstOrNull { it.packageName == firstPackageName }
                    val appName = firstUsage?.appName ?: "Unknown"

                    val statsRequests = listOf(
                        ChallengeStatsRequest(
                            challengeId = challenge.challengeId,
                            appName = appName,
                            packageName = allPackageNames, // Comma-separated package names in single event
                            startSyncTime = DateUtils.formatISO8601(
                                DateUtils.fromMillis(
                                    effectiveLastSyncTime
                                )
                            ),
                            endSyncTime = DateUtils.formatISO8601(DateUtils.fromMillis(syncEndTime)),
                            duration = totalDuration
                        )
                    )

                    if (statsRequests.isEmpty()) {
                        Log.d(TAG, "ChallengeSyncWorker: No valid stats to submit for challenge ${challenge.challengeId}")
                        joinedChallengeRepository.updateLastSyncTime(challenge.challengeId, syncEndTime)
                        continue
                    }

                    // Submit batch stats
                    val batchRequest = BatchChallengeStatsRequest(
                        challengeId = challenge.challengeId,
                        stats = statsRequests
                    )

                    val result = challengeRepository.submitBatchChallengeStats(batchRequest)
                    result.fold(
                        onSuccess = { response ->
                            if (response.success == true) {
                                Log.d(TAG, "ChallengeSyncWorker: Successfully synced ${statsRequests.size} stats for challenge ${challenge.challengeId}")
                                // Update last sync time
                                joinedChallengeRepository.updateLastSyncTime(challenge.challengeId, syncEndTime)
                            } else {
                                Log.e(TAG, "ChallengeSyncWorker: Failed to sync challenge ${challenge.challengeId}: ${response.message}")
                                hasErrors = true
                            }
                        },
                        onFailure = { throwable ->
                            Log.e(TAG, "ChallengeSyncWorker: Error syncing challenge ${challenge.challengeId}", throwable)
                            hasErrors = true
                        }
                    )

                } catch (e: Exception) {
                    Log.e(TAG, "ChallengeSyncWorker: Error processing challenge ${challenge.challengeId}", e)
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
        private const val WORK_NAME_PREFIX = "challenge_sync_"
        private const val SYNC_INTERVAL_HOURS = 1L

        /**
         * Schedule periodic sync for a specific challenge
         * Starts from challenge start time and runs hourly until end time
         */
        fun scheduleChallengeSync(
            context: Context,
            challengeId: String,
            startTime: String, // ISO 8601 format
            endTime: String
        ) {
            try {
                val startTimeMs = parseISO8601(startTime)
                val endTimeMs = parseISO8601(endTime)
                val currentTime = System.currentTimeMillis()

                // If challenge has already ended, don't schedule
                if (currentTime > endTimeMs) {
                    Log.d(TAG, "Challenge $challengeId has already ended, not scheduling sync")
                    return
                }

                // Calculate initial delay (from now until start time, or 0 if already started)
                val initialDelay = maxOf(0L, startTimeMs - currentTime)

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val workRequest = PeriodicWorkRequestBuilder<ChallengeSyncWorker>(
                    SYNC_INTERVAL_HOURS, TimeUnit.HOURS
                )
                    .setConstraints(constraints)
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .addTag("$WORK_NAME_PREFIX$challengeId")
                    .build()

                val workName = "$WORK_NAME_PREFIX$challengeId"

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    workName,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )

                Log.d(TAG, "Scheduled hourly sync for challenge $challengeId, starting in ${initialDelay / 1000 / 60} minutes")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to schedule challenge sync for challenge $challengeId", e)
            }
        }

        /**
         * Cancel sync for a specific challenge
         */
        fun cancelChallengeSync(context: Context, challengeId: String) {
            val workName = "$WORK_NAME_PREFIX$challengeId"
            WorkManager.getInstance(context).cancelUniqueWork(workName)
            Log.d(TAG, "Cancelled sync for challenge $challengeId")
        }

        /**
         * Sync pre-existing joined challenges from server to database
         * This handles challenges that were joined before the database was created
         */
        fun syncPreExistingChallenges(context: Context) {
            try {
                val database = ScreenTimeDatabase.getDatabase(context)
                val repository = JoinedChallengeRepository(database.joinedChallengeDao())
                val challengeRepository = ChallengeRepository(
                    com.app.screentime.challenge.service.ChallengeServiceImpl(
                        NetworkClient(context, PreferencesManager(context))
                    )
                )
                
                // Use a coroutine scope to access suspend function
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Fetch user challenges from server
                        val result = challengeRepository.getUserChallenges()
                        result.fold(
                            onSuccess = { response ->
                                if (response.success == true && response.data != null) {
                                    val serverChallenges = response.data!!.challenges
                                    Log.d(TAG, "Fetched ${serverChallenges.size} user challenges from server")
                                    
                                    // Get existing challenges from database
                                    val existingChallenges = repository.getAllJoinedChallengesSync()
                                    val existingIds = existingChallenges.map { it.challengeId }.toSet()
                                    
                                    var syncedCount = 0
                                    var scheduledCount = 0
                                    
                                    // Process each server challenge
                                    for (userChallenge in serverChallenges) {
                                        // Only sync active challenges (not past ones)
                                        if (!userChallenge.isActive) {
                                            continue
                                        }
                                        
                                        // Fetch challenge details to get package names
                                        var packageNames: String? = null
                                        try {
                                            val detailsResult = challengeRepository.getChallengeDetails(userChallenge.id)
                                            detailsResult.fold(
                                                onSuccess = { detailsResponse ->
                                                    if (detailsResponse.success == true && detailsResponse.data != null) {
                                                        packageNames = detailsResponse.data!!.packageNames
                                                    }
                                                },
                                                onFailure = {
                                                    Log.w(TAG, "Failed to fetch challenge details for ${userChallenge.id}, continuing without package names")
                                                }
                                            )
                                        } catch (e: Exception) {
                                            Log.w(TAG, "Error fetching challenge details for ${userChallenge.id}", e)
                                        }
                                        
                                        // Check if already in database
                                        if (existingIds.contains(userChallenge.id)) {
                                            // Already exists, update package names if needed and ensure sync is scheduled
                                            val existing = existingChallenges.find { it.challengeId == userChallenge.id }
                                            if (existing != null) {
                                                // Update package names if they're missing or different
                                                if (existing.packageNames != packageNames) {
                                                    repository.updatePackageNames(userChallenge.id, packageNames)
                                                    val updated = existing.copy(packageNames = packageNames)
                                                    repository.updateJoinedChallenge(updated)
                                                    Log.d(TAG, "Updated package names for challenge ${userChallenge.id}: '$packageNames'")
                                                }
                                                
                                                if (!existing.syncScheduled) {
                                                    scheduleChallengeSync(
                                                        context,
                                                        userChallenge.id,
                                                        userChallenge.startTime,
                                                        userChallenge.endTime
                                                    )
                                                    repository.updateSyncScheduled(userChallenge.id, true)
                                                    scheduledCount++
                                                }
                                            }
                                            continue
                                        }
                                        
                                        // New challenge, save to database
                                        val joinedAt = userChallenge.joinedAt ?: DateUtils.formatISO8601(DateUtils.now())
                                        val entity = com.app.screentime.database.entity.JoinedChallengeEntity(
                                            challengeId = userChallenge.id,
                                            title = userChallenge.title,
                                            description = userChallenge.description,
                                            reward = userChallenge.reward,
                                            startTime = userChallenge.startTime,
                                            endTime = userChallenge.endTime,
                                            thumbnail = userChallenge.thumbnail,
                                            joinedAt = joinedAt,
                                            lastSyncTime = 0L,
                                            syncScheduled = true,
                                            packageNames = packageNames
                                        )
                                        
                                        repository.insertJoinedChallenge(entity)
                                        
                                        // Schedule sync
                                        scheduleChallengeSync(
                                            context,
                                            userChallenge.id,
                                            userChallenge.startTime,
                                            userChallenge.endTime
                                        )
                                        
                                        syncedCount++
                                        Log.d(TAG, "Synced pre-existing challenge: ${userChallenge.id} - ${userChallenge.title} with package names: $packageNames")
                                    }
                                    
                                    Log.d(TAG, "Sync complete: $syncedCount new challenges synced, $scheduledCount syncs rescheduled")
                                } else {
                                    Log.w(TAG, "Failed to fetch user challenges: ${response.message}")
                                }
                            },
                            onFailure = { throwable ->
                                Log.e(TAG, "Error fetching user challenges from server", throwable)
                            }
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in syncPreExistingChallenges", e)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync pre-existing challenges", e)
            }
        }

        /**
         * Reschedule all active challenges on app start
         * This ensures sync workers are scheduled even after app restart
         */
        fun rescheduleActiveChallenges(context: Context) {
            try {
                val database = ScreenTimeDatabase.getDatabase(context)
                val repository = JoinedChallengeRepository(database.joinedChallengeDao())
                
                CoroutineScope(Dispatchers.IO).launch {
                    val activeChallenges = repository.getActiveChallenges()
                    for (challenge in activeChallenges) {
                        scheduleChallengeSync(
                            context,
                            challenge.challengeId,
                            challenge.startTime,
                            challenge.endTime
                        )
                    }
                    Log.d(TAG, "Rescheduled ${activeChallenges.size} active challenges")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to reschedule active challenges", e)
            }
        }

        /**
         * Cancel all challenge sync work
         */
        fun cancelAll(context: Context) {
            // Note: This cancels all work with the prefix tag
            // WorkManager doesn't have a direct way to cancel all work with a prefix
            // You might need to track challenge IDs separately
            Log.d(TAG, "ChallengeSyncWorker: Cancel all not fully implemented")
        }

        private fun parseISO8601(isoString: String): Long {
            return try {
                DateUtils.toMillis(isoString)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse ISO8601: $isoString", e)
                DateUtils.now().millis
            }
        }

        private fun cancelChallengeWork(context: Context, challengeId: String) {
            cancelChallengeSync(context, challengeId)
        }
    }
}

