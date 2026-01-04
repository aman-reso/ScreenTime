package com.app.screentime.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.screentime.database.entity.BlockedLinkEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for blocked links
 * Kept for future use - currently not in use
 */
@Dao
interface BlockedLinkDao {
    @Query("SELECT * FROM blocked_links ORDER BY createdAt DESC")
    fun getAllBlockedLinks(): Flow<List<BlockedLinkEntity>>

    @Query("SELECT * FROM blocked_links WHERE id = :id")
    suspend fun getBlockedLinkById(id: Long): BlockedLinkEntity?

    @Query("SELECT * FROM blocked_links WHERE link = :link")
    suspend fun getBlockedLinkByLink(link: String): BlockedLinkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedLink(blockedLink: BlockedLinkEntity): Long

    @Delete
    suspend fun deleteBlockedLink(blockedLink: BlockedLinkEntity)

    @Query("DELETE FROM blocked_links WHERE id = :id")
    suspend fun deleteBlockedLinkById(id: Long)

    @Query("SELECT COUNT(*) FROM blocked_links")
    suspend fun getBlockedLinksCount(): Int
}

