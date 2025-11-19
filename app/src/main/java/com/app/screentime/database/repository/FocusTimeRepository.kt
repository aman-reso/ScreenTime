package com.app.screentime.database.repository

import com.app.screentime.database.dao.FocusTimeDao
import com.app.screentime.database.entity.FocusTimeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for FocusTime operations
 */
@Singleton
class FocusTimeRepository @Inject constructor(
    private val focusTimeDao: FocusTimeDao
) {
    
    fun getAllFocusSessions(): Flow<List<FocusTimeEntity>> {
        return focusTimeDao.getAllFocusSessions()
    }
    
    suspend fun getFocusSessionById(id: Long): FocusTimeEntity? {
        return focusTimeDao.getFocusSessionById(id)
    }
    
    suspend fun getFocusSessionsByDateRange(startDate: Long, endDate: Long): List<FocusTimeEntity> {
        return focusTimeDao.getFocusSessionsByDateRange(startDate, endDate)
    }
    
    suspend fun getActiveFocusSession(): FocusTimeEntity? {
        return focusTimeDao.getActiveFocusSession()
    }
    
    suspend fun getTotalFocusTimeByDateRange(startDate: Long, endDate: Long): Long {
        return focusTimeDao.getTotalFocusTimeByDateRange(startDate, endDate) ?: 0L
    }
    
    suspend fun getCompletedSessionsCount(startDate: Long, endDate: Long): Int {
        return focusTimeDao.getCompletedSessionsCount(startDate, endDate)
    }
    
    suspend fun insertFocusSession(focusTime: FocusTimeEntity): Long {
        return focusTimeDao.insertFocusSession(focusTime)
    }
    
    suspend fun updateFocusSession(focusTime: FocusTimeEntity) {
        focusTimeDao.updateFocusSession(focusTime)
    }
    
    suspend fun deleteFocusSession(focusTime: FocusTimeEntity) {
        focusTimeDao.deleteFocusSession(focusTime)
    }
    
    suspend fun deleteFocusSessionById(id: Long) {
        focusTimeDao.deleteFocusSessionById(id)
    }
    
    suspend fun deleteOldSessions(beforeDate: Long) {
        focusTimeDao.deleteOldSessions(beforeDate)
    }
    
    suspend fun getRecentSessions(limit: Int = 50): List<FocusTimeEntity> {
        return focusTimeDao.getRecentSessions(limit)
    }
    
    /**
     * Start a new focus session
     */
    suspend fun startFocusSession(
        countdownMode: Boolean = false,
        countdownDuration: Long = 0
    ): Long {
        val startTime = System.currentTimeMillis()
        val session = FocusTimeEntity(
            startTime = startTime,
            endTime = null,
            duration = 0,
            completed = false,
            countdownMode = countdownMode,
            countdownDuration = countdownDuration
        )
        return insertFocusSession(session)
    }
    
    /**
     * End an active focus session
     */
    suspend fun endFocusSession(sessionId: Long, completed: Boolean = true): Boolean {
        val session = getFocusSessionById(sessionId) ?: return false
        val endTime = System.currentTimeMillis()
        val duration = endTime - session.startTime
        
        val updatedSession = session.copy(
            endTime = endTime,
            duration = duration,
            completed = completed
        )
        updateFocusSession(updatedSession)
        return true
    }
    
    /**
     * Update active session duration
     */
    suspend fun updateActiveSessionDuration(sessionId: Long, duration: Long) {
        val session = getFocusSessionById(sessionId) ?: return
        val updatedSession = session.copy(duration = duration)
        updateFocusSession(updatedSession)
    }
}

