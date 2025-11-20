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
