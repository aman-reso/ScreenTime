package com.app.screentime.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing local notifications
 * Not from API - stored locally in database
 */
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val image: String? = null, // Image URL or local resource path
    val text: String,
    val deeplink: String? = null, // Deep link URL for navigation
    val createdAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val readAt: Long? = null
)

