package com.app.screentime.database.dao

import androidx.room.*
import com.app.screentime.database.entity.JoinedChallengeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for JoinedChallengeEntity
 */
@Dao
interface JoinedChallengeDao {
    
    @Query("SELECT * FROM joined_challenges ORDER BY joinedAt DESC")
    fun getAllJoinedChallenges(): Flow<List<JoinedChallengeEntity>>
    
    @Query("SELECT * FROM joined_challenges WHERE challengeId = :challengeId")
    suspend fun getJoinedChallengeById(challengeId: String): JoinedChallengeEntity?
    
    @Query("SELECT * FROM joined_challenges WHERE syncScheduled = 1")
    suspend fun getActiveChallenges(): List<JoinedChallengeEntity>
    
    @Query("SELECT * FROM joined_challenges")
    suspend fun getAllJoinedChallengesSync(): List<JoinedChallengeEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoinedChallenge(challenge: JoinedChallengeEntity)
    
    @Update
    suspend fun updateJoinedChallenge(challenge: JoinedChallengeEntity)
    
    @Delete
    suspend fun deleteJoinedChallenge(challenge: JoinedChallengeEntity)
    
    @Query("DELETE FROM joined_challenges WHERE challengeId = :challengeId")
    suspend fun deleteJoinedChallengeById(challengeId: String)
    
    @Query("UPDATE joined_challenges SET lastSyncTime = :syncTime WHERE challengeId = :challengeId")
    suspend fun updateLastSyncTime(challengeId: String, syncTime: Long)
    
    @Query("UPDATE joined_challenges SET syncScheduled = :scheduled WHERE challengeId = :challengeId")
    suspend fun updateSyncScheduled(challengeId: String, scheduled: Boolean)
    
    @Query("UPDATE joined_challenges SET packageNames = :packageNames WHERE challengeId = :challengeId")
    suspend fun updatePackageNames(challengeId: String, packageNames: String?)
}

