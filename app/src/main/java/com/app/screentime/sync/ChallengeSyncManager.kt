package com.app.screentime.sync

import android.content.Context
import android.util.Log
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.network.model.BatchChallengeStatsRequest
import com.app.screentime.network.model.ChallengeStatsRequest
import com.app.screentime.network.model.UserChallenge
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.utils.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for challenge synchronization
 * Extracted from ChallengeSyncWorker to allow independent usage from any screen
 * Handles all challenge sync operations: syncing stats for joined challenges
 */
@Singleton
class ChallengeSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val challengeRepository: ChallengeRepository,
    private val localAppUsageRepository: LocalAppUsageRepository
) {

    companion object {
        private const val TAG = "ChallengeSyncManager"
    }

    /**
     * Result of a challenge sync operation
     */
    sealed class ChallengeSyncResult {
        data object Success : ChallengeSyncResult()
        data object Skipped : ChallengeSyncResult()
        data class Error(val message: String) : ChallengeSyncResult()
    }

    /**
     * Sync all joined challenges
     * Fetches all user challenges and syncs stats for each active challenge
     *
     * @return ChallengeSyncResult indicating the result of the sync
     */
    suspend fun syncAllChallenges(): ChallengeSyncResult {
        return try {
            // Google Play Compliance: Only send data if user has accepted consent
            if (!preferencesManager.isConsentScreenShown()) {
                Log.d(TAG, "Consent not given, skipping challenge stats sync")
                return ChallengeSyncResult.Skipped
            }

            val userChallengesResult = challengeRepository.getUserChallenges()
            val joinedChallenges = userChallengesResult.fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        response.data!!.challenges
                    } else {
                        emptyList()
                    }
                },
                onFailure = { throwable ->
                    Log.e(TAG, "Failed to get user challenges: ${throwable.message}", throwable)
                    emptyList()
                }
            )

            if (joinedChallenges.isEmpty()) {
                Log.d(TAG, "No joined challenges found from server")
                return ChallengeSyncResult.Success
            }

            val currentTime = DateUtils.nowMillis()
            var totalProcessed = 0
            var totalSuccessful = 0
            var totalFailed = 0

            for (userChallenge in joinedChallenges) {
                totalProcessed++
                val challengeResult = try {
                    syncChallenge(userChallenge, currentTime)
                } catch (e: Exception) {
                    Log.e(TAG, "Error syncing challenge ${userChallenge.id}: ${e.message}", e)
                    false
                }

                if (challengeResult) {
                    totalSuccessful++
                } else {
                    totalFailed++
                }
            }

            Log.d(
                TAG,
                "Challenge sync completed: $totalSuccessful/$totalProcessed successful, $totalFailed failed"
            )
            ChallengeSyncResult.Success

        } catch (e: Exception) {
            Log.e(TAG, "Challenge sync failed: ${e.message}", e)
            ChallengeSyncResult.Error(e.message ?: "Unknown error")
        }
    }


    /**
     * Sync a single challenge
     * Processes the challenge sync logic: filters by package names, calculates durations, and submits stats
     *
     * @param userChallenge The UserChallenge object containing challenge details
     * @param currentTime Current time in milliseconds
     * @return true if sync was successful, false otherwise
     */
    suspend fun syncChallenge(
        userChallenge: UserChallenge,
        currentTime: Long = DateUtils.nowMillis()
    ): Boolean {
        return try {
            val challengeId = userChallenge.id
            val startTime = parseISO8601(userChallenge.startTime)
            val endTime = parseISO8601(userChallenge.endTime)
            val packageNames = userChallenge.packageNames
            val serverLastSyncTime = userChallenge.lastSyncTime?.let {
                parseISO8601(it)
            }
            val joinedAt = userChallenge.joinedAt?.let {
                parseISO8601(it)
            }

            // Check if challenge is active
            if (currentTime < startTime) {
                Log.d(TAG, "Challenge $challengeId hasn't started yet")
                return true
            }

            if (currentTime > endTime) {
                Log.d(TAG, "Challenge $challengeId has ended")
                return true
            }

            // Check if challenge already started when user joined (user joined later)
            val userJoinedAfterChallengeStarted = joinedAt != null && joinedAt > startTime

            // Calculate effective last sync time - server is source of truth
            val effectiveLastSyncTime = when {
                userJoinedAfterChallengeStarted -> {
                    Log.d(
                        TAG,
                        "Challenge $challengeId: Challenge started at ${
                            DateUtils.formatISO8601(DateUtils.fromMillis(startTime))
                        }, user joined at ${userChallenge.joinedAt}. Syncing from challenge start date."
                    )
                    startTime
                }

                serverLastSyncTime != null -> {
                    maxOf(serverLastSyncTime, startTime)
                }

                else -> {
                    startTime
                }
            }

            val allowedPackageNames = if (packageNames.isNullOrBlank()) {
                Log.d(TAG, "Challenge $challengeId has no package names specified")
                return true
            } else {
                packageNames.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toSet()
            }

            if (allowedPackageNames.isEmpty()) {
                Log.d(TAG, "Challenge $challengeId has empty package names list")
                return true
            }

            val syncEndTime = minOf(currentTime, endTime)

            if (syncEndTime <= effectiveLastSyncTime) {
                Log.d(TAG, "Challenge $challengeId: No new data to sync")
                return true
            }

            // Use server's last sync time as source of truth, or challenge start date if not found
            val syncStartDate = if (serverLastSyncTime != null) {
                DateUtils.startOfDay(DateUtils.fromMillis(serverLastSyncTime))
            } else {
                DateUtils.startOfDay(DateUtils.fromMillis(effectiveLastSyncTime))
            }
            
            val syncEndDate = DateUtils.startOfDay(DateUtils.fromMillis(syncEndTime))

            val daysToSync = mutableListOf<DateTime>()
            var currentDay = syncStartDate
            while (!currentDay.isAfter(syncEndDate)) {
                daysToSync.add(currentDay)
                currentDay = DateUtils.addDays(currentDay, 1)
            }

            if (daysToSync.isEmpty()) {
                Log.d(TAG, "Challenge $challengeId: No days to sync")
                return true
            }

            Log.d(TAG, "Challenge $challengeId: Preparing to sync ${daysToSync.size} days from ${DateUtils.formatISO8601(syncStartDate)} to ${DateUtils.formatISO8601(syncEndDate)}")

            // Collect all daily stats requests
            val allStatsRequests = mutableListOf<ChallengeStatsRequest>()
            
            for (dayStart in daysToSync) {
                val dayEnd = DateUtils.addDays(dayStart, 1).minusMillis(1) // End of day (23:59:59.999)
                val dayStartMillis = dayStart.millis
                val dayEndMillis = minOf(dayEnd.millis, syncEndTime)

                // Skip if day is in the future
                if (dayStartMillis > currentTime) {
                    continue
                }

                // Get app usage data for this day
                val appUsageList = try {
                    localAppUsageRepository.getAppsUsageForInterval(
                        dayStartMillis,
                        dayEndMillis
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error fetching app usage for challenge $challengeId on ${DateUtils.formatISO8601(dayStart)}: ${e.message}", e)
                    continue
                }

                if (appUsageList.isEmpty()) {
                    Log.d(TAG, "Challenge $challengeId: No app usage data found for ${DateUtils.formatISO8601(dayStart)}, skipping")
                    continue
                }

                // Filter by allowed package names
                val filteredAppUsage = appUsageList.filter { appUsage ->
                    val packageName = appUsage.packageName?.trim() ?: ""
                    packageName.isNotEmpty() && allowedPackageNames.contains(packageName)
                }

                if (filteredAppUsage.isEmpty()) {
                    Log.d(TAG, "Challenge $challengeId: No matching app usage found for ${DateUtils.formatISO8601(dayStart)}, skipping")
                    continue
                }

                // Group by package and calculate totals for this day
                val groupedByPackage = filteredAppUsage.groupBy { it.packageName ?: "" }
                    .mapValues { (_, usages) ->
                        usages.sumOf { it.appScreenTime }
                    }
                    .filter { (packageName, duration) ->
                        packageName.isNotEmpty() && duration > 0
                    }

                if (groupedByPackage.isEmpty()) {
                    Log.d(TAG, "Challenge $challengeId: No valid grouped usage data for ${DateUtils.formatISO8601(dayStart)}, skipping")
                    continue
                }

                val allPackageNames = groupedByPackage.keys.sorted().joinToString(",")
                val totalDuration = groupedByPackage.values.sum()
                
                // Skip if duration is zero
                if (totalDuration <= 0) {
                    Log.d(TAG, "Challenge $challengeId: Duration is zero for ${DateUtils.formatISO8601(dayStart)}, skipping")
                    continue
                }
                
                val firstPackageName = groupedByPackage.keys.firstOrNull()
                val firstUsage = filteredAppUsage.firstOrNull { it.packageName == firstPackageName }
                val appName = firstUsage?.appName ?: "Unknown"

                // Create stats request for this day
                val dayStatsRequest = ChallengeStatsRequest(
                    challengeId = challengeId,
                    appName = appName,
                    packageName = allPackageNames,
                    startSyncTime = DateUtils.formatISO8601(dayStart),
                    endSyncTime = DateUtils.formatISO8601(DateUtils.fromMillis(dayEndMillis)),
                    duration = totalDuration
                )
                
                allStatsRequests.add(dayStatsRequest)
            }

            if (allStatsRequests.isEmpty()) {
                Log.d(TAG, "Challenge $challengeId: No stats requests to send")
                return true
            }

            // Send all days in a single batch request
            Log.d(TAG, "Challenge $challengeId: Sending ${allStatsRequests.size} daily stats in batch")
            
            val result = try {
                challengeRepository.submitBatchChallengeStats(
                    BatchChallengeStatsRequest(
                        stats = allStatsRequests,
                        challengeId = challengeId
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error submitting batch challenge stats for $challengeId: ${e.message}", e)
                return false
            }

            return result.fold(
                onSuccess = { response ->
                    val success = response.success == true
                    if (success) {
                        Log.d(TAG, "Successfully synced ${allStatsRequests.size} days for challenge $challengeId")
                        // Server is source of truth, no need to save to preferences
                    } else {
                        Log.w(TAG, "Challenge sync returned success=false for $challengeId")
                    }
                    success
                },
                onFailure = { throwable ->
                    Log.e(
                        TAG,
                        "Failed to sync challenge $challengeId: ${throwable.message}",
                        throwable
                    )
                    false
                }
            )
        } catch (e: Throwable) {
            Log.e(TAG, "Exception syncing challenge: ${e.message}", e)
            false
        }
    }

    /**
     * Parse ISO 8601 string to milliseconds
     */
    private fun parseISO8601(isoString: String): Long {
        return try {
            DateUtils.toMillis(isoString)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse ISO8601: $isoString", e)
            DateUtils.now().millis
        }
    }
}

