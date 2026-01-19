package com.app.screentime.challenge.model

import androidx.compose.runtime.Stable
import com.app.screentime.network.model.AppDetail
import com.app.screentime.network.model.LeaderboardEntry
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
    val topRankings: List<LeaderboardEntry>,
    val userRank: LeaderboardEntry?,
    val showLeaderboard: Boolean,
    
    // Content
    val rules: String?,
    val sponsor: String?,
    
    // Rewards
    val availableRewards: List<ChallengeReward>,
    
    // App Details
    val appDetails: List<AppDetail>? = null, // List of app details with name and URL
    
    // Actions
    val showJoinButton: Boolean
)

/**
 * Reward item for challenge detail screen
 */
@Stable
data class ChallengeReward(
    val id: String,
    val title: String,
    val description: String? = null,
    val coin: String? = null,
    val imageUrl: String? = null,
    val tagUrl: String? = null
)

