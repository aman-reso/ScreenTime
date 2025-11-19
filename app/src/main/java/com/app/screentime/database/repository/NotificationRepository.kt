package com.app.screentime.database.repository

import com.app.screentime.database.dao.NotificationDao
import com.app.screentime.database.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing local notifications
 * Not from API - all operations are local database operations
 */
@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) {
    
    /**
     * Get all notifications as Flow
     */
    fun getAllNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotifications()
    }
    
    /**
     * Get all notifications synchronously
     */
    suspend fun getAllNotificationsSync(): List<NotificationEntity> {
        return notificationDao.getAllNotificationsSync()
    }
    
    /**
     * Get notification by ID
     */
    suspend fun getNotificationById(id: Long): NotificationEntity? {
        return notificationDao.getNotificationById(id)
    }
    
    /**
     * Get unread notifications as Flow
     */
    fun getUnreadNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getUnreadNotifications()
    }
    
    /**
     * Get unread notifications synchronously
     */
    suspend fun getUnreadNotificationsSync(): List<NotificationEntity> {
        return notificationDao.getUnreadNotificationsSync()
    }
    
    /**
     * Get count of unread notifications as Flow
     */
    fun getUnreadCount(): Flow<Int> {
        return notificationDao.getUnreadCount()
    }
    
    /**
     * Get count of unread notifications synchronously
     */
    suspend fun getUnreadCountSync(): Int {
        return notificationDao.getUnreadCountSync()
    }
    
    /**
     * Get recent notifications
     */
    suspend fun getRecentNotifications(limit: Int = 10): List<NotificationEntity> {
        return notificationDao.getRecentNotifications(limit)
    }
    
    /**
     * Get notifications by date range
     */
    suspend fun getNotificationsByDateRange(startDate: Long, endDate: Long): List<NotificationEntity> {
        return notificationDao.getNotificationsByDateRange(startDate, endDate)
    }
    
    /**
     * Insert a new notification
     */
    suspend fun insertNotification(
        title: String,
        text: String,
        image: String? = null,
        deeplink: String? = null
    ): Long {
        val notification = NotificationEntity(
            title = title,
            text = text,
            image = image,
            deeplink = deeplink
        )
        return notificationDao.insertNotification(notification)
    }
    
    /**
     * Insert a notification entity
     */
    suspend fun insertNotification(notification: NotificationEntity): Long {
        return notificationDao.insertNotification(notification)
    }
    
    /**
     * Insert multiple notifications
     */
    suspend fun insertNotifications(notifications: List<NotificationEntity>): List<Long> {
        return notificationDao.insertNotifications(notifications)
    }
    
    /**
     * Update a notification
     */
    suspend fun updateNotification(notification: NotificationEntity) {
        notificationDao.updateNotification(notification)
    }
    
    /**
     * Mark notification as read
     */
    suspend fun markAsRead(id: Long) {
        notificationDao.markAsRead(id)
    }
    
    /**
     * Mark all notifications as read
     */
    suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
    }
    
    /**
     * Delete a notification
     */
    suspend fun deleteNotification(notification: NotificationEntity) {
        notificationDao.deleteNotification(notification)
    }
    
    /**
     * Delete notification by ID
     */
    suspend fun deleteNotificationById(id: Long) {
        notificationDao.deleteNotificationById(id)
    }
    
    /**
     * Delete old notifications before a specific date
     */
    suspend fun deleteOldNotifications(beforeDate: Long) {
        notificationDao.deleteOldNotifications(beforeDate)
    }
    
    /**
     * Delete all notifications
     */
    suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
}

