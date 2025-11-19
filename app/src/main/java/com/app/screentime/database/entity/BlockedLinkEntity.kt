package com.app.screentime.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity for storing blocked links/domains
 */
@Entity(
    tableName = "blocked_links",
    indices = [Index(value = ["link"], unique = true)]
)
data class BlockedLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val link: String, // Normalized lowercase link/domain
    val createdAt: Long = System.currentTimeMillis(),
    val blockedCount: Int = 0 // Number of times this link was blocked
)

