package com.app.screentime.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for blocked links/domains
 * Kept for future use - currently not in use
 */
@Entity(tableName = "blocked_links")
data class BlockedLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val link: String,
    val blockedCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

