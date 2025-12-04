package com.app.screentime.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing joined challenges
 */
@Entity(tableName = "joined_challenges")
data class JoinedChallengeEntity(
    @PrimaryKey
    val challengeId: String,
    val title: String,
    val description: String,
    val reward: String,
    val startTime: String, // ISO 8601 format
    val endTime: String, // ISO 8601 format
    val thumbnail: String? = null,
    val joinedAt: String, // ISO 8601 format - when user joined
    val lastSyncTime: Long = 0L, // Last time we synced stats for this challenge (milliseconds)
    val syncScheduled: Boolean = false, // Whether sync worker is scheduled
    val packageNames: String? = null // Comma-separated package names for this challenge
)

