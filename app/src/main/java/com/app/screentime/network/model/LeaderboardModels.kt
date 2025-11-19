package com.app.screentime.network.model

import kotlinx.serialization.Serializable

/**
 * Leaderboard entry model from API
 */
@Serializable
data class LeaderboardEntry(
    val userId: String,
    val username: String,
    val name: String,
    val avatar: String? = null,
    val totalScreenTime: Long, // Total screen time in milliseconds
    val rank: Int
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

