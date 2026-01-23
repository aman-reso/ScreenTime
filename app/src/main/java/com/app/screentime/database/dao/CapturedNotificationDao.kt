package com.app.screentime.database.dao

import androidx.room.*
import com.app.screentime.database.entity.CapturedNotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for CapturedNotificationEntity
 */
@Dao
interface CapturedNotificationDao {
    
    @Query("SELECT * FROM captured_notifications ORDER BY timestamp DESC")
    fun getAllCapturedNotifications(): Flow<List<CapturedNotificationEntity>>
    
    @Query("SELECT DISTINCT packageName FROM captured_notifications ORDER BY packageName ASC")
    fun getAllPackageNames(): Flow<List<String>>
    
    @Query("SELECT * FROM captured_notifications WHERE packageName = :packageName ORDER BY timestamp DESC")
    fun getNotificationsByPackage(packageName: String): Flow<List<CapturedNotificationEntity>>
    
    @Insert
    suspend fun insertNotification(notification: CapturedNotificationEntity): Long
    
    @Delete
    suspend fun deleteNotification(notification: CapturedNotificationEntity)
    
    @Query("DELETE FROM captured_notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)
    
    @Query("DELETE FROM captured_notifications")
    suspend fun clearHistory()
}
