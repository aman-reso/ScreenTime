package com.app.screentime.feature.profile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val category: String, // "Matches", "Messages", "Activity"
    val timestamp: Long,
    val isUnread: Boolean = true,
    val actionText: String? = null,
    val avatarUrl: String? = null,
    val iconResName: String? = null, // "ic_info", "coin_icon", "ic_heart", "ic_zap"
    val targetScreen: String? = null, // "chat", "profile", "discover", "wallet"
    val targetId: String? = null
)
