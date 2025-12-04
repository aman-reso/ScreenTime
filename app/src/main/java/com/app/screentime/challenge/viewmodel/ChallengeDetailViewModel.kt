package com.app.screentime.challenge.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.challenge.model.ChallengeDetailUiProps
import com.app.screentime.challenge.repository.ChallengeRepository
import com.app.screentime.challenge.usecase.ChallengeDetailUseCase
import com.app.screentime.challenge.util.ChallengeShareUtil
import com.app.screentime.database.entity.JoinedChallengeEntity
import com.app.screentime.database.repository.JoinedChallengeRepository
import com.app.screentime.network.model.Challenge
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.sync.ChallengeSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
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
    val challengeDetails: ChallengeDetails? = null,
    val challengeRankings: com.app.screentime.network.model.ChallengeRankingsResponse? = null,
    val isJoining: Boolean = false,
    val uiProps: ChallengeDetailUiProps? = null
)

/**
 * ViewModel for Challenge Detail Screen
 * Handles loading challenge details, rankings, and joining challenges
 */
@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val joinedChallengeRepository: JoinedChallengeRepository,
    private val challengeDetailUseCase: ChallengeDetailUseCase,
    private val shareUtil: ChallengeShareUtil,
    @ApplicationContext
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeDetailUiState())
    val uiState: StateFlow<ChallengeDetailUiState> = _uiState.asStateFlow()

    /**
     * Load challenge details and rankings
     */
    fun loadChallengeDetails(challengeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                challengeDetails = null,
                challengeRankings = null,
                uiProps = null
            )

            // Load challenge details
            challengeRepository.getChallengeDetails(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        val challengeDetails = response.data
                        _uiState.value = _uiState.value.copy(
                            challengeDetails = challengeDetails,
                            isLoading = false
                        )
                        // Update UI props after loading details
                        updateUiProps(challengeDetails, _uiState.value.challengeRankings)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = response.message ?: "Failed to load challenge details"
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Failed to load challenge details"
                    )
                }
            )

            // Load challenge rankings (non-blocking)
            challengeRepository.getChallengeRankings(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true && response.data != null) {
                        val rankings = response.data
                        _uiState.value = _uiState.value.copy(
                            challengeRankings = rankings
                        )
                        // Update UI props after loading rankings
                        updateUiProps(_uiState.value.challengeDetails, rankings)
                    }
                },
                onFailure = {
                    // Rankings failure is not critical, just log it
                    Log.w("ChallengeDetailViewModel", "Failed to load rankings", it)
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
        // Prevent duplicate join requests
        if (_uiState.value.isJoining) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isJoining = true)

            challengeRepository.joinChallenge(challengeId).fold(
                onSuccess = { response ->
                    if (response.success == true) {
                        // Save challenge to local DB and schedule sync
                        val challengeDetails = _uiState.value.challengeDetails
                        if (challengeDetails != null && challengeDetails.id == challengeId) {
                            saveChallengeAndScheduleSync(challengeDetails)
                        }

                        // Reload challenge details to get updated state
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
     * Update UI props based on current state
     */
    private fun updateUiProps(
        challengeDetails: ChallengeDetails?,
        challengeRankings: com.app.screentime.network.model.ChallengeRankingsResponse?
    ) {
        if (challengeDetails != null) {
            val uiProps = challengeDetailUseCase.getChallengeDetailUiProps(
                challengeDetails = challengeDetails,
                challengeRankings = challengeRankings,
                isJoining = _uiState.value.isJoining
            )
            _uiState.value = _uiState.value.copy(uiProps = uiProps)
        }
    }

    /**
     * Save challenge to local DB and schedule sync worker
     */
    private suspend fun saveChallengeAndScheduleSync(challengeDetails: ChallengeDetails) {
        try {
            // Check if already saved
            val existing = joinedChallengeRepository.getJoinedChallengeById(challengeDetails.id)

            if (existing != null) {
                // Update existing challenge
                val updated = existing.copy(
                    packageNames = challengeDetails.packageNames,
                    title = challengeDetails.title,
                    description = challengeDetails.description,
                    reward = challengeDetails.reward,
                    startTime = challengeDetails.startTime,
                    endTime = challengeDetails.endTime,
                    thumbnail = challengeDetails.thumbnail
                )
                joinedChallengeRepository.updateJoinedChallenge(updated)
            } else {
                // Create new entity
                val joinedAt = com.app.screentime.utils.DateUtils.formatISO8601(
                    com.app.screentime.utils.DateUtils.now()
                )
                val entity = JoinedChallengeEntity(
                    challengeId = challengeDetails.id,
                    title = challengeDetails.title,
                    description = challengeDetails.description,
                    reward = challengeDetails.reward,
                    startTime = challengeDetails.startTime,
                    endTime = challengeDetails.endTime,
                    thumbnail = challengeDetails.thumbnail,
                    joinedAt = joinedAt,
                    lastSyncTime = 0L,
                    syncScheduled = true,
                    packageNames = challengeDetails.packageNames
                )

                // Save to database
                joinedChallengeRepository.insertJoinedChallenge(entity)

                // Schedule sync worker
                ChallengeSyncWorker.scheduleChallengeSync(
                    context = context,
                    challengeId = challengeDetails.id,
                    startTime = challengeDetails.startTime,
                    endTime = challengeDetails.endTime
                )
            }
        } catch (e: Exception) {
            Log.e("ChallengeDetailViewModel", "Failed to save challenge or schedule sync", e)
        }
    }
}

