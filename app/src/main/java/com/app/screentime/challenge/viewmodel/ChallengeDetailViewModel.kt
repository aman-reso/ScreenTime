package com.app.screentime.challenge.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.challenge.model.ChallengeDetailUiProps
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.challenge.usecase.ChallengeDetailUseCase
import com.app.screentime.challenge.util.ChallengeShareUtil
import com.app.screentime.network.model.Challenge
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.BatchChallengeStatsRequest
import com.app.screentime.network.model.ChallengeStatsRequest
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.ScreenUsageHelper
import com.app.screentime.record.repository.NetworkUsageHelper
import com.app.screentime.sync.ChallengeSyncWorker
import com.app.screentime.sync.ChallengeSyncManager
import com.app.screentime.utils.DateUtils
import com.app.screentime.analytics.AnalyticsUseCase
import com.app.screentime.config.R
import com.app.screentime.network.model.ChallengeRankingsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI State for Challenge Detail Screen
 */
data class ChallengeDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val leaderboardError: String? = null,
    val challengeDetails: ChallengeDetails? = null,
    val challengeRankings: ChallengeRankingsResponse? = null,
    val isJoining: Boolean = false,
    val uiProps: ChallengeDetailUiProps? = null,
    val lastSyncTime: String? = null, // Formatted last sync time, null if never synced
    val isSyncing: Boolean = false
)

/**
 * ViewModel for Challenge Detail Screen
 * Handles loading challenge details, rankings, and joining challenges
 */
@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val analyticsUseCase: AnalyticsUseCase,
    private val challengeDetailUseCase: ChallengeDetailUseCase,
    private val shareUtil: ChallengeShareUtil,
    private val preferencesUseCase: com.app.screentime.preferences.usecase.PreferencesUseCase,
    private val challengeSyncManager: ChallengeSyncManager,
    @ApplicationContext
    private val context: Context
) : ViewModel() {

    private val localAppUsageRepository by lazy {
        LocalAppUsageRepository(
            context,
            ScreenUsageHelper(context),
            NetworkUsageHelper(context)
        )
    }

    private val _uiState = MutableStateFlow(ChallengeDetailUiState())
    val uiState: StateFlow<ChallengeDetailUiState> = _uiState.asStateFlow()

    /**
     * Track screen view when challenge details are loaded
     */
    fun trackScreenView() {
        analyticsUseCase.trackChallengeDetailScreen()
    }

    /**
     * Load challenge details and rankings in parallel
     */
    fun loadChallengeDetails(challengeId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                leaderboardError = null,
                challengeDetails = null,
                challengeRankings = null,
                uiProps = null
            )

            val detailsDeferred = async {
                challengeRepository.getChallengeDetails(challengeId)
            }

            val rankingsDeferred = async {
                challengeRepository.getChallengeRankings(challengeId)
            }

            val lastSyncDeferred = async {
                challengeRepository.getChallengeLastSyncTime(challengeId)
            }

            val detailsResult = detailsDeferred.await()
            val rankingsResult = rankingsDeferred.await()
            val lastSyncResult = lastSyncDeferred.await()
            var challengeRankings: ChallengeRankingsResponse? = null
            var leaderboardError: String? = null

            val lastSyncTimeStr = lastSyncResult.getOrNull()?.data?.lastSyncTime
            val formattedSyncTime = if (lastSyncTimeStr != null) {
                try {
                    DateUtils.format(lastSyncTimeStr, "MMM dd, yyyy 'at' HH:mm")
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
            rankingsResult.fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        challengeRankings = response.data
                    } else {
                        leaderboardError = response.message ?: "Failed to load leaderboard"
                    }
                },
                onFailure = { throwable ->
                    leaderboardError = throwable.message ?: "Failed to load leaderboard"
                }
            )

            detailsResult.fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null,
                            uiProps = challengeDetailUseCase.getChallengeDetailUiProps(
                                challengeDetails = response.data!!,
                                challengeRankings = challengeRankings,
                                leaderboardError = leaderboardError,
                                isJoining = false
                            ),
                            leaderboardError = leaderboardError,
                            challengeDetails = response.data,
                            challengeRankings = challengeRankings,
                            lastSyncTime = formattedSyncTime
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = response.error?.message,
                            leaderboardError = leaderboardError,
                            challengeDetails = response.data,
                            challengeRankings = challengeRankings,
                            lastSyncTime = formattedSyncTime
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message,
                        leaderboardError = leaderboardError,
                        challengeDetails = null,
                        challengeRankings = challengeRankings,
                        lastSyncTime = formattedSyncTime
                    )
                }
            )
        }
    }

    /**
     * Join a challenge
     */
    fun joinChallenge(
        challengeId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        // Check if any required app (from packageNames) is installed before joining
        val packageNames = _uiState.value.challengeDetails?.packageNames
        if (!packageNames.isNullOrBlank()) {
            val packages = packageNames.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            if (packages.isNotEmpty()) {
                val pm = context.packageManager
                val anyInstalled = packages.any { pkg ->
                    try {
                        pm.getPackageInfo(pkg, 0)
                        true
                    } catch (_: PackageManager.NameNotFoundException) {
                        false
                    }
                }
                if (!anyInstalled) {
                    val msg = context.getString(R.string.error_joining_required_app_not_installed)
                    onError(msg)
                    return
                }
            }
        }

        if (_uiState.value.isJoining) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isJoining = true)

            challengeRepository.joinChallenge(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true) {
                        loadChallengeDetails(challengeId)
                        onSuccess()
                    } else {
                        val errorMsg = response.message ?: "Failed to join challenge"
                        _uiState.value = _uiState.value.copy(
                            error = errorMsg,
                            isJoining = false
                        )
                        onError(errorMsg)
                    }
                },
                onFailure = { throwable ->
                    val errorMsg = throwable.message ?: "Failed to join challenge"
                    _uiState.value = _uiState.value.copy(
                        error = errorMsg,
                        isJoining = false
                    )
                    onError(errorMsg)
                }
            )
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Check if user has granted consent
     */
    fun hasConsent(): Boolean {
        return preferencesUseCase.isConsentScreenShown()
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return preferencesUseCase.getUserId()
    }

    /**
     * Share challenge
     */
    suspend fun shareChallenge(
        challengeId: String,
        title: String,
        prize: String,
        imageUrl: String?, context: Context
    ) {
        shareUtil.shareChallenge(
            challengeId = challengeId,
            title = title,
            prize = prize,
            imageUrl = imageUrl,
            context
        )
    }

    /**
     * Load last sync time for the challenge
     */
    fun loadLastSyncTime(challengeId: String) {
        viewModelScope.launch {
            challengeRepository.getChallengeLastSyncTime(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        val lastSyncTimeStr = response.data!!.lastSyncTime
                        val formattedTime = if (lastSyncTimeStr != null) {
                            try {
                                DateUtils.format(lastSyncTimeStr, "MMM dd, yyyy 'at' HH:mm")
                            } catch (e: Exception) {
                                null
                            }
                        } else {
                            null
                        }
                        _uiState.value = _uiState.value.copy(lastSyncTime = formattedTime)
                    }
                },
                onFailure = {
                    // Failure is not critical, just log it
                    Log.w("ChallengeDetailViewModel", "Failed to load last sync time", it)
                }
            )
        }
    }

    /**
     * Manually sync challenge stats
     */
    fun syncChallenge(
        challengeId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (_uiState.value.isSyncing) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true)

            try {
                // First, sync all challenges to ensure challenge data is up to date
                challengeSyncManager.syncAllChallenges()
                val challengeDetails = _uiState.value.challengeDetails
                    ?: challengeRepository.getChallengeDetails(challengeId).fold(
                        onSuccess = { response ->
                            if (response.success == true && response.data != null) {
                                response.data
                            } else {
                                null
                            }
                        },
                        onFailure = { null }
                    )

                if (challengeDetails == null) {
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        error = "Failed to load challenge details"
                    )
                    onError("Failed to load challenge details")
                    return@launch
                }

                val startTime = DateUtils.toMillis(challengeDetails.startTime)
                val endTime = DateUtils.toMillis(challengeDetails.endTime)
                val currentTime = DateUtils.nowMillis()

                // Check if challenge is active
                if (currentTime !in startTime..endTime) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    onError("Challenge is not active")
                    return@launch
                }

                // Get last sync time from server
                var serverLastSyncTime: Long? = null
                challengeRepository.getChallengeLastSyncTime(challengeId).fold(
                    onSuccess = { response ->
                        if (response.success == true && response.data != null) {
                            val lastSyncTimeStr = response.data!!.lastSyncTime
                            if (lastSyncTimeStr != null) {
                                serverLastSyncTime = DateUtils.toMillis(lastSyncTimeStr)
                            }
                        }
                    },
                    onFailure = {
                        Log.w("ChallengeDetailViewModel", "Failed to get last sync time", it)
                    }
                )

                // Calculate effective last sync time
                val effectiveLastSyncTime = if (serverLastSyncTime != null) {
                    maxOf(serverLastSyncTime, startTime)
                } else {
                    startTime
                }
                val syncEndTime = minOf(currentTime, endTime)

                if (syncEndTime <= effectiveLastSyncTime) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    onSuccess()
                    loadLastSyncTime(challengeId) // Refresh last sync time display
                    return@launch
                }

                // Get package names
                val packageNames = challengeDetails.packageNames
                val allowedPackageNames = if (packageNames.isNullOrBlank()) {
                    emptySet()
                } else {
                    packageNames.split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .toSet()
                }

                if (allowedPackageNames.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    onError("No apps specified for this challenge")
                    return@launch
                }

                // Get app usage data
                val appUsageList = localAppUsageRepository.getAppsUsageForInterval(
                    effectiveLastSyncTime,
                    syncEndTime
                )

                if (appUsageList.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    onSuccess()
                    loadLastSyncTime(challengeId)
                    return@launch
                }

                // Filter by allowed package names
                val filteredAppUsage = appUsageList.filter { appUsage ->
                    val packageName = appUsage.packageName?.trim() ?: ""
                    packageName.isNotEmpty() && allowedPackageNames.contains(packageName)
                }

                if (filteredAppUsage.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    onSuccess()
                    loadLastSyncTime(challengeId)
                    return@launch
                }

                // Group by package and calculate totals
                val groupedByPackage = filteredAppUsage.groupBy { it.packageName ?: "" }
                    .mapValues { (_, usages) ->
                        usages.sumOf { it.appScreenTime }
                    }
                    .filter { (packageName, duration) ->
                        packageName.isNotEmpty() && duration > 0
                    }

                if (groupedByPackage.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    onSuccess()
                    loadLastSyncTime(challengeId)
                    return@launch
                }

                val allPackageNames = groupedByPackage.keys.sorted().joinToString(",")
                val totalDuration = groupedByPackage.values.sum()
                val firstPackageName = groupedByPackage.keys.first()
                val firstUsage = filteredAppUsage.firstOrNull { it.packageName == firstPackageName }
                val appName = firstUsage?.appName ?: "Unknown"

                // Submit stats
                val statsRequests = listOf(
                    ChallengeStatsRequest(
                        challengeId = challengeId,
                        appName = appName,
                        packageName = allPackageNames,
                        startSyncTime = DateUtils.formatISO8601(
                            DateUtils.fromMillis(
                                effectiveLastSyncTime
                            )
                        ),
                        endSyncTime = DateUtils.formatISO8601(DateUtils.fromMillis(syncEndTime)),
                        duration = totalDuration
                    )
                )

                val batchRequest = BatchChallengeStatsRequest(
                    challengeId = challengeId,
                    stats = statsRequests
                )

                challengeRepository.submitBatchChallengeStats(batchRequest).fold(
                    onSuccess = { response ->
                        if (response.success == true) {
                            _uiState.value = _uiState.value.copy(isSyncing = false)
                            loadLastSyncTime(challengeId)
                            onSuccess()
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isSyncing = false,
                                error = response.message ?: "Failed to sync challenge"
                            )
                            onError(response.message ?: "Failed to sync challenge")
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.value = _uiState.value.copy(
                            isSyncing = false,
                            error = throwable.message ?: "Failed to sync challenge"
                        )
                        onError(throwable.message ?: "Failed to sync challenge")
                    }
                )
            } catch (e: Exception) {
                Log.e("ChallengeDetailViewModel", "Error syncing challenge", e)
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    error = e.message ?: "Failed to sync challenge"
                )
                onError(e.message ?: "Failed to sync challenge")
            }
        }
    }
}

