package com.app.screentime.network.repository.notification

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.*
import com.app.screentime.network.service.notification.NotificationService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Notification operations
 */
@Singleton
class NotificationRepository @Inject constructor(
    private val notificationService: NotificationService
) {


    suspend fun getNotificationHistory(userId: String): Result<ApiResponse<List<NotificationData>>> {
        return notificationService.getNotificationHistory(userId)
    }

    suspend fun updateNotificationSettings(settings: NotificationSettings): Result<ApiResponse<NotificationSettings>> {
        return notificationService.updateNotificationSettings(settings)
    }
}

