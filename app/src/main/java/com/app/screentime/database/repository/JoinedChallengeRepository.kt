package com.app.screentime.database.repository

import com.app.screentime.database.dao.JoinedChallengeDao
import com.app.screentime.database.entity.JoinedChallengeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for JoinedChallenge operations
 */
@Singleton
class JoinedChallengeRepository @Inject constructor(
    private val joinedChallengeDao: JoinedChallengeDao
) {
    
    fun getAllJoinedChallenges(): Flow<List<JoinedChallengeEntity>> {
        return joinedChallengeDao.getAllJoinedChallenges()
    }
    
    suspend fun getJoinedChallengeById(challengeId: String): JoinedChallengeEntity? {
        return joinedChallengeDao.getJoinedChallengeById(challengeId)
    }
    
    suspend fun getActiveChallenges(): List<JoinedChallengeEntity> {
        return joinedChallengeDao.getActiveChallenges()
    }
    
    suspend fun getAllJoinedChallengesSync(): List<JoinedChallengeEntity> {
        return joinedChallengeDao.getAllJoinedChallengesSync()
    }
    
    suspend fun insertJoinedChallenge(challenge: JoinedChallengeEntity) {
        joinedChallengeDao.insertJoinedChallenge(challenge)
    }
    
    suspend fun updateJoinedChallenge(challenge: JoinedChallengeEntity) {
        joinedChallengeDao.updateJoinedChallenge(challenge)
    }
    
    suspend fun deleteJoinedChallenge(challengeId: String) {
        joinedChallengeDao.deleteJoinedChallengeById(challengeId)
    }
    
    suspend fun updateLastSyncTime(challengeId: String, syncTime: Long) {
        joinedChallengeDao.updateLastSyncTime(challengeId, syncTime)
    }
    
    suspend fun updateSyncScheduled(challengeId: String, scheduled: Boolean) {
        joinedChallengeDao.updateSyncScheduled(challengeId, scheduled)
    }
    
    suspend fun updatePackageNames(challengeId: String, packageNames: String?) {
        joinedChallengeDao.updatePackageNames(challengeId, packageNames)
    }
}

