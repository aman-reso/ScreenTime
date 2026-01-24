package com.app.screentime.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing notifications captured from other apps via accessibility service.
 */
@Entity(tableName = "captured_notifications")
data class CapturedNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val title: String?,
    val text: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val isRemoved: Boolean = false,
    val imagePath: String? = null,  // Local path to saved notification image
    val profileImagePath: String? = null  // Local path to sender profile image (WhatsApp)
)
