package com.app.screentime.network.model

import kotlinx.serialization.Serializable

/**
 * Represents the challenge overview payload coming from the backend.
 * Each entry summarizes the user's standing for a specific app challenge.
 */
@Serializable
data class ChallengeOverviewResponse(
    val challenges: List<ChallengeAppRanking> = emptyList(),
    val lastRefreshedAt: String? = null
)

/**
 * Active challenges response from /api/challenges/active
 */
@Serializable
data class ActiveChallengesResponse(
    val challenges: List<Challenge> = emptyList()
)

/**
 * User challenges response from /api/challenges/user
 */
@Serializable
data class UserChallengesResponse(
    val challenges: List<UserChallenge> = emptyList()
)

/**
 * Challenge model from active challenges endpoint
 */
@Serializable
data class Challenge(
    val id: Int,
    val title: String,
    val description: String,
    val reward: String,
    val prize: String? = null, // HTML content with prize information
    val rules: String? = null, // HTML content with rules
    val displayType: String? = null, // SPECIAL, TRENDING, QUICK_JOIN, FEATURE
    val tags: List<String>? = null, // List of tags like ["social media", "wellness"]
    val tag: String? = null, // Legacy single tag field
    val sponsor: String? = null, // Sponsor name like "AppTime"
    val startTime: String, // ISO 8601 format
    val endTime: String, // ISO 8601 format
    val thumbnail: String? = null,
    val packageNames: String? = null, // Comma-separated package names
    val participantCount: Int = 0,
    val hasJoined: Boolean = false
)

/**
 * User challenge model from user challenges endpoint
 */
@Serializable
data class UserChallenge(
    val id: Int,
    val title: String,
    val description: String,
    val reward: String,
    val startTime: String, // ISO 8601 format
    val endTime: String, // ISO 8601 format
    val thumbnail: String? = null,
    val challengeType: String? = null, // "LESS_SCREENTIME" or "MORE_SCREENTIME"
    val isActive: Boolean,
    val joinedAt: String? = null, // ISO 8601 format
    val isPast: Boolean
)

/**
 * Challenge details response from /api/challenges/{challengeId}
 */
@Serializable
data class ChallengeDetails(
    val id: Int,
    val title: String,
    val description: String,
    val reward: String,
    val prize: String? = null, // HTML content with prize information
    val rules: String? = null, // HTML content with rules
    val tag: String? = null, // Tag like "Wellness"
    val sponsor: String? = null, // Sponsor name like "AppTime"
    val startTime: String, // ISO 8601 format
    val endTime: String, // ISO 8601 format
    val thumbnail: String? = null,
    val challengeType: String? = null,
    val isActive: Boolean,
    val participantCount: Int,
    val createdAt: String? = null, // ISO 8601 format
    val packageNames: String? = null // Comma-separated package names for this challenge
)

/**
 * Join challenge request
 */
@Serializable
data class JoinChallengeRequest(
    val challengeId: Int
)

/**
 * Join challenge response
 */
@Serializable
data class JoinChallengeResponse(
    val challengeId: Int,
    val userId: String? = null,
    val joinedAt: String? = null, // ISO 8601 format
    val message: String? = null
)

/**
 * Challenge rankings response from /api/challenges/{challengeId}/rankings
 */
@Serializable
data class ChallengeRankingsResponse(
    val challengeId: Int,
    val challengeTitle: String,
    val challengeType: String, // "LESS_SCREENTIME" or "MORE_SCREENTIME"
    val rankings: List<ChallengeRanking> = emptyList(),
    val userRank: ChallengeRanking? = null,
    val totalParticipants: Int
)

/**
 * Individual ranking entry
 */
@Serializable
data class ChallengeRanking(
    val rank: Int,
    val userId: String,
    val totalDuration: Long, // milliseconds
    val appCount: Int
)

/**
 * Submit challenge stats request
 */
@Serializable
data class ChallengeStatsRequest(
    val challengeId: Int,
    val appName: String,
    val packageName: String,
    val startSyncTime: String, // ISO 8601 format
    val endSyncTime: String, // ISO 8601 format
    val duration: Long // milliseconds
)

/**
 * Batch challenge stats request
 */
@Serializable
data class BatchChallengeStatsRequest(
    val challengeId: Int,
    val stats: List<ChallengeStatsRequest>
)

/**
 * Batch challenge stats response
 */
@Serializable
data class BatchChallengeStatsResponse(
    val submitted: Int,
    val totalDuration: Long // milliseconds
)

/**
 * Challenge last sync time response
 */
@Serializable
data class ChallengeLastSyncResponse(
    val challengeId: Int,
    val lastSyncTime: String? = null // ISO 8601 format, null if never synced
)

/**
 * Individual challenge ranking info for a single app.
 */
@Serializable
data class ChallengeAppRanking(
    val challengeId: String,
    val appName: String,
    val packageName: String,
    val iconUrl: String? = null,
    val description: String? = null,
    val metricLabel: String? = null,
    val metricUnit: String? = null,
    val goalValue: Long? = null,
    val userRank: Int,
    val totalParticipants: Int,
    val userMetricValue: Long? = null,
    val percentile: Double? = null,
    val trend: ChallengeTrend? = null,
    val topCompetitors: List<ChallengeCompetitor> = emptyList(),
    val isJoined: Boolean = false,
    val rewards: List<ChallengeReward> = emptyList()
)

/**
 * Short summary of another competitor inside the challenge list.
 */
@Serializable
data class ChallengeCompetitor(
    val username: String,
    val rank: Int,
    val metricValue: Long? = null,
    val displayValue: String? = null
)

/**
 * Lightweight trend descriptor to highlight performance movement.
 */
@Serializable
data class ChallengeTrend(
    val direction: String, // up, down, steady
    val delta: Double? = null
)

/**
 * Reward information for a challenge
 */
@Serializable
data class ChallengeReward(
    val type: String, // "badge", "points", "trophy", "medal", etc.
    val title: String,
    val description: String? = null,
    val iconUrl: String? = null,
    val points: Int? = null,
    val tier: String? = null // "gold", "silver", "bronze", etc.
)
