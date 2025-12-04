package com.app.screentime.challenge.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.challenge.mapper.ChallengeMapper
import com.app.screentime.challenge.model.GroupedChallenges
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.database.entity.JoinedChallengeEntity
import com.app.screentime.database.repository.JoinedChallengeRepository
import com.app.screentime.network.model.Challenge
import com.app.screentime.sync.ChallengeSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChallengesUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val challenges: List<Challenge> = emptyList(),
    val groupedChallenges: GroupedChallenges? = null,
    val lastUpdated: String? = null,
    val joiningChallengeIds: Set<String> = emptySet()
)

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val joinedChallengeRepository: JoinedChallengeRepository,
    private val challengeMapper: ChallengeMapper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengesUiState())
    val uiState: StateFlow<ChallengesUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            // Use server data
            challengeRepository.getActiveChallenges().fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        val challenges = response.data.challenges
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            challenges = challenges,
                            groupedChallenges = challengeMapper.groupChallengesByType(challenges),
                            lastUpdated = "Just now",
                            error = null
                        )

                        // Update local DB for joined challenges based on hasJoined flag
                        updateJoinedChallengesFromActiveList(challenges)
                    } else {
                        val errorMsg = response.message ?: "Failed to load challenges"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg,
                            challenges = emptyList(),
                            groupedChallenges = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Failed to load challenges",
                        challenges = emptyList(),
                        groupedChallenges = null
                    )
                }
            )
        }
    }

    /**
     * Update local DB for joined challenges based on active challenges list
     * Only fetches challenge details for challenges missing package names
     */
    private suspend fun updateJoinedChallengesFromActiveList(activeChallenges: List<Challenge>) {
        try {
            // Get all joined challenges from local DB
            val localJoinedChallenges = joinedChallengeRepository.getAllJoinedChallengesSync()
            val localJoinedIds = localJoinedChallenges.map { it.challengeId }.toSet()

            // Find challenges that are marked as joined in active challenges
            val joinedFromActive = activeChallenges.filter { it.hasJoined }
            val joinedIdsFromActive = joinedFromActive.map { it.id }.toSet()

            Log.d(
                "ChallengeViewModel",
                "Updating joined challenges: ${joinedFromActive.size} from active list, ${localJoinedChallenges.size} in local DB"
            )

            // Update or insert joined challenges from active list
            for (challenge in joinedFromActive) {
                val existing = localJoinedChallenges.find { it.challengeId == challenge.id }

                if (existing != null) {
                    // Update existing challenge with data from active list
                    val needsUpdate = existing.title != challenge.title ||
                            existing.description != challenge.description ||
                            existing.reward != challenge.reward ||
                            existing.startTime != challenge.startTime ||
                            existing.endTime != challenge.endTime ||
                            existing.thumbnail != challenge.thumbnail

                    if (needsUpdate) {
                        val updated = existing.copy(
                            title = challenge.title,
                            description = challenge.description,
                            reward = challenge.reward,
                            startTime = challenge.startTime,
                            endTime = challenge.endTime,
                            thumbnail = challenge.thumbnail
                        )
                        joinedChallengeRepository.updateJoinedChallenge(updated)
                        Log.d(
                            "ChallengeViewModel",
                            "Updated challenge ${challenge.id} from active list"
                        )
                    }

                    // Only fetch details if package names are missing
                    if (existing.packageNames.isNullOrBlank()) {
                        fetchAndUpdatePackageNames(challenge.id)
                    }
                } else {
                    // New joined challenge - save to DB
                    val joinedAt =
                        com.app.screentime.utils.DateUtils.formatISO8601(com.app.screentime.utils.DateUtils.now())
                    val entity = JoinedChallengeEntity(
                        challengeId = challenge.id,
                        title = challenge.title,
                        description = challenge.description,
                        reward = challenge.reward,
                        startTime = challenge.startTime,
                        endTime = challenge.endTime,
                        thumbnail = challenge.thumbnail,
                        joinedAt = joinedAt,
                        lastSyncTime = 0L,
                        syncScheduled = true,
                        packageNames = null // Will be fetched below
                    )
                    joinedChallengeRepository.insertJoinedChallenge(entity)

                    // Schedule sync worker
                    ChallengeSyncWorker.scheduleChallengeSync(
                        context = context,
                        challengeId = challenge.id,
                        startTime = challenge.startTime,
                        endTime = challenge.endTime
                    )

                    // Fetch package names
                    fetchAndUpdatePackageNames(challenge.id)
                    Log.d(
                        "ChallengeViewModel",
                        "Saved new joined challenge ${challenge.id} from active list"
                    )
                }
            }

            // Remove challenges that are no longer joined (hasJoined = false)
            val toRemove = localJoinedIds - joinedIdsFromActive
            for (challengeId in toRemove) {
                // Don't remove, just mark as not scheduled
                val existing = localJoinedChallenges.find { it.challengeId == challengeId }
                if (existing != null && existing.syncScheduled) {
                    joinedChallengeRepository.updateSyncScheduled(challengeId, false)
                    ChallengeSyncWorker.cancelChallengeSync(context, challengeId)
                    Log.d("ChallengeViewModel", "Marked challenge $challengeId as not joined")
                }
            }

        } catch (e: Exception) {
            Log.e("ChallengeViewModel", "Error updating joined challenges from active list", e)
        }
    }

    /**
     * Fetch challenge details to get package names and update local DB
     * Only called when package names are missing
     */
    private suspend fun fetchAndUpdatePackageNames(challengeId: String) {
        try {
            challengeRepository.getChallengeDetails(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        val packageNames = response.data.packageNames
                        if (!packageNames.isNullOrBlank()) {
                            joinedChallengeRepository.updatePackageNames(challengeId, packageNames)
                            Log.d(
                                "ChallengeViewModel",
                                "Updated package names for challenge $challengeId: '$packageNames'"
                            )
                        }
                    }
                },
                onFailure = { throwable ->
                    Log.w(
                        "ChallengeViewModel",
                        "Failed to fetch package names for challenge $challengeId",
                        throwable
                    )
                }
            )
        } catch (e: Exception) {
            Log.e(
                "ChallengeViewModel",
                "Error fetching package names for challenge $challengeId",
                e
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Join a challenge (used by challenge list screen)
     * Updates the local state to mark challenge as joined
     */
    fun joinChallenge(
        challengeId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        // Prevent duplicate join requests
        if (_uiState.value.joiningChallengeIds.contains(challengeId)) {
            return
        }

        viewModelScope.launch {
            // Mark as joining
            _uiState.value = _uiState.value.copy(
                joiningChallengeIds = _uiState.value.joiningChallengeIds + challengeId
            )

            challengeRepository.joinChallenge(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true) {
                        // Update local state to mark challenge as joined
                        val updatedChallenges = _uiState.value.challenges.map { c ->
                            if (c.id == challengeId) {
                                c.copy(hasJoined = true)
                            } else {
                                c
                            }
                        }
                        _uiState.value = _uiState.value.copy(
                            joiningChallengeIds = _uiState.value.joiningChallengeIds - challengeId,
                            challenges = updatedChallenges,
                            groupedChallenges = challengeMapper.groupChallengesByType(updatedChallenges)
                        )

                        // Find the challenge in the list and save it
                        val challenge = updatedChallenges.find { it.id == challengeId }
                        if (challenge != null) {
                            saveChallengeAndScheduleSync(challenge)
                        }
                        onSuccess()
                    } else {
                        val errorMsg = response.message ?: "Failed to join challenge"
                        _uiState.value = _uiState.value.copy(
                            error = errorMsg,
                            joiningChallengeIds = _uiState.value.joiningChallengeIds - challengeId
                        )
                        onError(errorMsg)
                    }
                },
                onFailure = { throwable ->
                    val errorMsg = throwable.message ?: "Failed to join challenge"
                    _uiState.value = _uiState.value.copy(
                        error = errorMsg,
                        joiningChallengeIds = _uiState.value.joiningChallengeIds - challengeId
                    )
                    onError(errorMsg)
                }
            )
        }
    }

    private suspend fun saveChallengeAndScheduleSync(challenge: Challenge) {
        try {
            // Check if already saved
            val existing = joinedChallengeRepository.getJoinedChallengeById(challenge.id)

            if (existing != null) {
                // Update existing challenge with package names if missing or different
                val needsUpdate = existing.packageNames != null

                if (needsUpdate) {
                    // Use direct UPDATE query for package names to ensure it works
                    // Also update other fields if needed
                    val updated = existing.copy(
                        packageNames = challenge.packageNames,
                        title = challenge.title,
                        description = challenge.description,
                        reward = challenge.reward,
                        startTime = challenge.startTime,
                        endTime = challenge.endTime,
                        thumbnail = challenge.thumbnail
                    )
                    joinedChallengeRepository.updateJoinedChallenge(updated)

                    // Verify the update
                    val verify = joinedChallengeRepository.getJoinedChallengeById(challenge.id)
                    Log.d(
                        "ChallengeViewModel",
                        "After update, challenge ${challenge.id} packageNames='${verify?.packageNames}'"
                    )
                } else {
                    Log.d(
                        "ChallengeViewModel",
                        "Challenge ${challenge.id} already has correct package names"
                    )
                }
                return
            }

            // Create new entity
            val joinedAt =
                com.app.screentime.utils.DateUtils.formatISO8601(com.app.screentime.utils.DateUtils.now())
            val entity = JoinedChallengeEntity(
                challengeId = challenge.id,
                title = challenge.title,
                description = challenge.description,
                reward = challenge.reward,
                startTime = challenge.startTime,
                endTime = challenge.endTime,
                thumbnail = challenge.thumbnail,
                joinedAt = joinedAt,
                lastSyncTime = 0L,
                syncScheduled = true,
                packageNames = challenge.packageNames
            )

            // Save to database
            joinedChallengeRepository.insertJoinedChallenge(entity)

            // Schedule sync worker
            ChallengeSyncWorker.scheduleChallengeSync(
                context = context,
                challengeId = challenge.id,
                startTime = challenge.startTime,
                endTime = challenge.endTime
            )
        } catch (e: Exception) {
            Log.e("ChallengeViewModel", "Failed to save challenge or schedule sync", e)
        }
    }
}
