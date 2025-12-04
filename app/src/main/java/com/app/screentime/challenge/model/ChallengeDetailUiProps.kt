package com.app.screentime.challenge.model

import androidx.compose.runtime.Stable
import com.app.screentime.network.model.ChallengeRanking
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * UI Props for Challenge Detail Screen
 * Contains all the processed data needed to render the challenge detail screen
 * This is the only data structure the UI layer should use
 */
@Stable
data class ChallengeDetailUiProps(
    // Basic Info
    val id: String,
    val title: String,
    val description: String,
    val reward: String,
    val thumbnail: String?,
    val scheme: ODSTheme,
    
    // Tags and Duration
    val tags: List<String>,
    val duration: String,
    
    // Dates
    val dateRange: String,
    val startDateFormatted: String,
    val endDateFormatted: String,
    
    // Prize
    val prize: String?,
    val displayPrize: String,
    
    // Participants
    val participantCount: Int,
    
    // Status
    val hasJoined: Boolean,
    val isCompleted: Boolean,
    
    // Leaderboard
    val topRankings: List<ChallengeRanking>,
    val userRank: ChallengeRanking?,
    val showLeaderboard: Boolean,
    
    // Content
    val rules: String?,
    val sponsor: String?,
    
    // Actions
    val showJoinButton: Boolean
)

