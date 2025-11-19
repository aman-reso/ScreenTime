package com.app.screentime.database.dao

import androidx.room.*
import com.app.screentime.database.entity.FocusTimeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for FocusTimeEntity
 */
@Dao
interface FocusTimeDao {
    
    @Query("SELECT * FROM focus_time ORDER BY startTime DESC")
    fun getAllFocusSessions(): Flow<List<FocusTimeEntity>>
    
    @Query("SELECT * FROM focus_time WHERE id = :id")
    suspend fun getFocusSessionById(id: Long): FocusTimeEntity?
    
    @Query("SELECT * FROM focus_time WHERE startTime >= :startDate AND startTime <= :endDate ORDER BY startTime DESC")
    suspend fun getFocusSessionsByDateRange(startDate: Long, endDate: Long): List<FocusTimeEntity>
    
    @Query("SELECT * FROM focus_time WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveFocusSession(): FocusTimeEntity?
    
    @Query("SELECT SUM(duration) FROM focus_time WHERE startTime >= :startDate AND startTime <= :endDate AND completed = 1")
    suspend fun getTotalFocusTimeByDateRange(startDate: Long, endDate: Long): Long?
    
    @Query("SELECT COUNT(*) FROM focus_time WHERE startTime >= :startDate AND startTime <= :endDate AND completed = 1")
    suspend fun getCompletedSessionsCount(startDate: Long, endDate: Long): Int
    
    @Insert
    suspend fun insertFocusSession(focusTime: FocusTimeEntity): Long
    
    @Update
    suspend fun updateFocusSession(focusTime: FocusTimeEntity)
    
    @Delete
    suspend fun deleteFocusSession(focusTime: FocusTimeEntity)
    
    @Query("DELETE FROM focus_time WHERE id = :id")
    suspend fun deleteFocusSessionById(id: Long)
    
    @Query("DELETE FROM focus_time WHERE startTime < :beforeDate")
    suspend fun deleteOldSessions(beforeDate: Long)
    
    @Query("SELECT * FROM focus_time ORDER BY startTime DESC LIMIT :limit")
    suspend fun getRecentSessions(limit: Int): List<FocusTimeEntity>
}

