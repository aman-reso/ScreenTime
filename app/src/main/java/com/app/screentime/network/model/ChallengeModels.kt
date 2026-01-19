package com.app.screentime.network.model

import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.allSchemes
import com.telekom.odsystem.tokens.tokens.aperitifSecondaryScheme
import com.telekom.odsystem.tokens.tokens.basketballSecondaryScheme
import com.telekom.odsystem.tokens.tokens.blackScheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme
import com.telekom.odsystem.tokens.tokens.dandelionSecondaryScheme
import com.telekom.odsystem.tokens.tokens.eggSecondaryScheme
import com.telekom.odsystem.tokens.tokens.frogSecondaryScheme
import com.telekom.odsystem.tokens.tokens.guacamoleSecondaryScheme
import com.telekom.odsystem.tokens.tokens.hummingbirdSecondaryScheme
import com.telekom.odsystem.tokens.tokens.iguanaSecondaryScheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme
import com.telekom.odsystem.tokens.tokens.kingfisherSecondaryScheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import com.telekom.odsystem.tokens.tokens.magentaScheme
import com.telekom.odsystem.tokens.tokens.nebulaSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme
import com.telekom.odsystem.tokens.tokens.whiteScheme
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
 * Enum for ODS theme schemes that converts string names to actual ODSTheme instances.
 * Uses data objects to hold the actual theme instances.
 */
fun getThemeFromScheme(scheme: String?): ODSTheme {
    if (scheme.isNullOrBlank()) return neutralScheme


    return when (scheme) {
        "blackScheme" -> blackScheme
        "magentaScheme" -> magentaScheme
        "whiteScheme" -> whiteScheme
        "aperitifSecondaryScheme" -> aperitifSecondaryScheme
        "basketballSecondaryScheme" -> basketballSecondaryScheme
        "cheddarSecondaryScheme" -> cheddarSecondaryScheme
        "dandelionSecondaryScheme" -> dandelionSecondaryScheme
        "eggSecondaryScheme" -> eggSecondaryScheme
        "frogSecondaryScheme" -> frogSecondaryScheme
        "guacamoleSecondaryScheme" -> guacamoleSecondaryScheme
        "hummingbirdSecondaryScheme" -> hummingbirdSecondaryScheme
        "iguanaSecondaryScheme" -> iguanaSecondaryScheme
        "jacuzziSecondaryScheme" -> jacuzziSecondaryScheme
        "kingfisherSecondaryScheme" -> kingfisherSecondaryScheme
        "lagoonSecondaryScheme" -> lagoonSecondaryScheme
        "macawSecondaryScheme" -> macawSecondaryScheme
        "nebulaSecondaryScheme" -> nebulaSecondaryScheme
        "orchidSecondaryScheme" -> orchidSecondaryScheme
        else -> neutralScheme
    }
}


/**
 * Challenge model from active challenges endpoint
 */
@Serializable
data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val reward: String,
    val prize: String? = null, // HTML content with prize information
    val rules: String? = null, // HTML content with rules
    val displayType: String? = null, // SPECIAL, TRENDING, QUICK_JOIN, FEATURE
    val tags: List<String>? = null, // List of tags like ["social media", "wellness"]
    val sponsor: String? = null, // Sponsor name like "AppTime"
    val startTime: String, // ISO 8601 format
    val endTime: String, // ISO 8601 format
    val thumbnail: String? = null,
    val packageNames: String? = null, // Comma-separated package names
    val participantCount: Int = 0,
    val hasJoined: Boolean = false,
    val tag: String? = null,
    val scheme: String? = null,
    val variant: String? = null // variant1, variant2, variant3, variant4, variant5, variant6
) {
    /**
     * Get the ODSTheme for this challenge.
     * Returns neutralScheme as default if scheme is null or not found.
     */
    fun getTheme(): ODSTheme {
        return getThemeFromScheme(scheme)
    }
}

/**
 * User challenge model from user challenges endpoint
 */
@Serializable
data class UserChallenge(
    val id: String,
    val title: String,
    val description: String,
    val reward: String,
    val startTime: String, // ISO 8601 format
    val endTime: String, // ISO 8601 format
    val thumbnail: String? = null,
    val challengeType: String? = null, // "LESS_SCREENTIME" or "MORE_SCREENTIME"
    val isActive: Boolean,
    val joinedAt: String? = null, // ISO 8601 format
    val isPast: Boolean,
    val packageNames: String? = null,
    val lastSyncTime: String? = null // ISO 8601 format, null if never synced
)

/**
 * App detail model for challenge app details
 */
@Serializable
data class AppDetail(
    val appname: String,
    val url: String
)

/**
 * Challenge details response from /api/challenges/{challengeId}
 */
@Serializable
data class ChallengeDetails(
    val id: String,
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
    val packageNames: String? = null,
    val hasJoined: Boolean = false,
    val scheme: String? = null,
    val variant: String? = null,
    val appdetail: List<AppDetail>? = null // List of app details with name and URL
) {
    /**
     * Get the ODSTheme for this challenge detail.
     * Returns neutralScheme as default if scheme is null or not found.
     */
    fun getTheme(): ODSTheme {
        return getThemeFromScheme(scheme)
    }
    
    fun getVariant(){
        "variant2"
    }
}

/**
 * Join challenge request
 */
@Serializable
data class JoinChallengeRequest(
    val challengeId: String
)

/**
 * Join challenge response
 */
@Serializable
data class JoinChallengeResponse(
    val challengeId: String,
    val userId: String? = null,
    val joinedAt: String? = null, // ISO 8601 format
    val message: String? = null
)

/**
 * Challenge rankings response from /api/challenges/{challengeId}/rankings
 */
@Serializable
data class ChallengeRankingsResponse(
    val challengeId: String,
    val challengeTitle: String,
    val challengeType: String, // "LESS_SCREENTIME" or "MORE_SCREENTIME"
    val rankings: List<LeaderboardEntry> = emptyList(),
    val userRank: LeaderboardEntry? = null,
    val totalParticipants: Int
)


/**
 * Submit challenge stats request
 */
@Serializable
data class ChallengeStatsRequest(
    val challengeId: String,
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
    val challengeId: String,
    val stats: List<ChallengeStatsRequest>
)

/**
 * Batch challenge stats response
 */
@Serializable
data class BatchChallengeStatsResponse(
    val submitted: String,
    val totalDuration: Long // milliseconds
)

/**
 * Challenge last sync time response
 */
@Serializable
data class ChallengeLastSyncResponse(
    val challengeId: String,
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
