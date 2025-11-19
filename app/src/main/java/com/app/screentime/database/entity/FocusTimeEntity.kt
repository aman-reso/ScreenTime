package com.app.screentime.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing focus time sessions
 */
@Entity(tableName = "focus_time")
data class FocusTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long? = null,
    val duration: Long = 0, // Duration in milliseconds
    val completed: Boolean = false,
    val countdownMode: Boolean = false,
    val countdownDuration: Long = 0, // Original countdown duration if countdown mode
    val createdAt: Long = System.currentTimeMillis()
)

