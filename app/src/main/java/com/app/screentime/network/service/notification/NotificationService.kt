package com.app.screentime.network.service.notification

import com.app.screentime.network.model.*
import com.app.screentime.network.service.NotificationData

/**
 * API Service interface for Notification operations
 */
interface NotificationService {
    suspend fun sendNotification(
        userId: String,
        message: String,
        type: String
    ): Result<ApiResponse<Unit>>

    suspend fun getNotificationHistory(userId: String): Result<ApiResponse<List<NotificationData>>>
    suspend fun updateNotificationSettings(settings: NotificationSettings): Result<ApiResponse<NotificationSettings>>
}

