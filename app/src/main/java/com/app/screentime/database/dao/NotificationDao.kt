package com.app.screentime.database.dao

import androidx.room.*
import com.app.screentime.database.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for NotificationEntity
 */
@Dao
interface NotificationDao {
    
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    suspend fun getAllNotificationsSync(): List<NotificationEntity>
    
    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: Long): NotificationEntity?
    
    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY createdAt DESC")
    suspend fun getUnreadNotificationsSync(): List<NotificationEntity>
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    suspend fun getUnreadCountSync(): Int
    
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentNotifications(limit: Int): List<NotificationEntity>
    
    @Query("SELECT * FROM notifications WHERE createdAt >= :startDate AND createdAt <= :endDate ORDER BY createdAt DESC")
    suspend fun getNotificationsByDateRange(startDate: Long, endDate: Long): List<NotificationEntity>
    
    @Insert
    suspend fun insertNotification(notification: NotificationEntity): Long
    
    @Insert
    suspend fun insertNotifications(notifications: List<NotificationEntity>): List<Long>
    
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    @Query("UPDATE notifications SET isRead = 1, readAt = :readAt WHERE id = :id")
    suspend fun markAsRead(id: Long, readAt: Long = System.currentTimeMillis())
    
    @Query("UPDATE notifications SET isRead = 1, readAt = :readAt WHERE isRead = 0")
    suspend fun markAllAsRead(readAt: Long = System.currentTimeMillis())
    
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
    
    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)
    
    @Query("DELETE FROM notifications WHERE createdAt < :beforeDate")
    suspend fun deleteOldNotifications(beforeDate: Long)
    
    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
}

