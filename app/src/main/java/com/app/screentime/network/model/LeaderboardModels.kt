package com.app.screentime.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Leaderboard entry model from API
 */
@Serializable
data class LeaderboardEntry @OptIn(ExperimentalSerializationApi::class) constructor(
    val userId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    @SerialName("totalScreenTime") // used when SERIALIZING
    @JsonNames("totalDuration", "totalScreenTime")
    val totalScreenTime: Long? = null, // Total screen time in milliseconds
    val rank: Int,
    val appCount: Int? = null
)

/**
 * Leaderboard response model from API
 */
@Serializable
data class LeaderboardResponse(
    val period: String, // "daily", "weekly", or "monthly"
    val periodDate: String, // e.g., "2025-11-15" for daily, "2025-W46" for weekly
    val entries: List<LeaderboardEntry>,
    val userRank: LeaderboardEntry? = null, // Current user's rank entry (null if not in top list)
    val totalUsers: Int = 0
)

