package com.app.screentime.database.dao

import androidx.room.*
import com.app.screentime.database.entity.BlockedLinkEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for BlockedLinkEntity
 */
@Dao
interface BlockedLinkDao {
    
    @Query("SELECT * FROM blocked_links ORDER BY createdAt DESC")
    fun getAllBlockedLinks(): Flow<List<BlockedLinkEntity>>
    
    @Query("SELECT * FROM blocked_links ORDER BY createdAt DESC")
    suspend fun getAllBlockedLinksSync(): List<BlockedLinkEntity>
    
    @Query("SELECT link FROM blocked_links")
    suspend fun getAllBlockedLinkStrings(): List<String>
    
    @Query("SELECT link FROM blocked_links")
    fun getAllBlockedLinkStringsFlow(): Flow<List<String>>
    
    @Query("SELECT COUNT(*) FROM blocked_links")
    suspend fun getBlockedLinksCount(): Int
    
    @Query("SELECT COUNT(*) FROM blocked_links")
    fun getBlockedLinksCountFlow(): Flow<Int>
    
    @Query("SELECT * FROM blocked_links WHERE link = :link LIMIT 1")
    suspend fun getBlockedLinkByLink(link: String): BlockedLinkEntity?
    
    @Query("SELECT * FROM blocked_links WHERE id = :id")
    suspend fun getBlockedLinkById(id: Long): BlockedLinkEntity?
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBlockedLink(blockedLink: BlockedLinkEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBlockedLink(blockedLink: BlockedLinkEntity): Long
    
    @Update
    suspend fun updateBlockedLink(blockedLink: BlockedLinkEntity)
    
    @Query("UPDATE blocked_links SET blockedCount = blockedCount + 1 WHERE link = :link")
    suspend fun incrementBlockedCount(link: String)
    
    @Delete
    suspend fun deleteBlockedLink(blockedLink: BlockedLinkEntity)
    
    @Query("DELETE FROM blocked_links WHERE id = :id")
    suspend fun deleteBlockedLinkById(id: Long)
    
    @Query("DELETE FROM blocked_links WHERE link = :link")
    suspend fun deleteBlockedLinkByLink(link: String)
    
    @Query("DELETE FROM blocked_links")
    suspend fun deleteAllBlockedLinks()
    
    @Query("SELECT * FROM blocked_links WHERE link LIKE :pattern ORDER BY createdAt DESC")
    suspend fun searchBlockedLinks(pattern: String): List<BlockedLinkEntity>
}

