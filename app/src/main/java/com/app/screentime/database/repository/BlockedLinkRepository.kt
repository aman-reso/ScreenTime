package com.app.screentime.database.repository

import com.app.screentime.database.dao.BlockedLinkDao
import com.app.screentime.database.entity.BlockedLinkEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for BlockedLink operations
 */
@Singleton
class BlockedLinkRepository @Inject constructor(
    private val blockedLinkDao: BlockedLinkDao
) {
    
    fun getAllBlockedLinks(): Flow<List<BlockedLinkEntity>> {
        return blockedLinkDao.getAllBlockedLinks()
    }
    
    suspend fun getAllBlockedLinksSync(): List<BlockedLinkEntity> {
        return blockedLinkDao.getAllBlockedLinksSync()
    }
    
    suspend fun getAllBlockedLinkStrings(): List<String> {
        return blockedLinkDao.getAllBlockedLinkStrings()
    }
    
    fun getAllBlockedLinkStringsFlow(): Flow<List<String>> {
        return blockedLinkDao.getAllBlockedLinkStringsFlow()
    }
    
    suspend fun getBlockedLinksCount(): Int {
        return blockedLinkDao.getBlockedLinksCount()
    }
    
    fun getBlockedLinksCountFlow(): Flow<Int> {
        return blockedLinkDao.getBlockedLinksCountFlow()
    }
    
    suspend fun getBlockedLinkByLink(link: String): BlockedLinkEntity? {
        return blockedLinkDao.getBlockedLinkByLink(link.lowercase().trim())
    }
    
    suspend fun getBlockedLinkById(id: Long): BlockedLinkEntity? {
        return blockedLinkDao.getBlockedLinkById(id)
    }
    
    /**
     * Add a blocked link (normalizes to lowercase, removes protocol and trailing slashes)
     * Examples:
     * - "https://aajtak.in/" -> "aajtak.in"
     * - "http://www.aajtak.in" -> "www.aajtak.in"
     * - "aajtak.in" -> "aajtak.in"
     */
    suspend fun addBlockedLink(link: String): Long {
        // Normalize: remove protocol, trailing slashes, and convert to lowercase
        var normalizedLink = link.lowercase().trim()
        
        // Remove protocol (http://, https://)
        normalizedLink = normalizedLink.removePrefix("http://")
            .removePrefix("https://")
        
        // Remove trailing slashes, dots, and spaces
        normalizedLink = normalizedLink.trimEnd('/', '.', ' ')
        
        if (normalizedLink.isEmpty()) return -1
        
        val existing = getBlockedLinkByLink(normalizedLink)
        return if (existing != null) {
            // Link already exists, increment count
            blockedLinkDao.incrementBlockedCount(normalizedLink)
            existing.id
        } else {
            // New link
            val blockedLink = BlockedLinkEntity(
                link = normalizedLink,
                blockedCount = 1
            )
            blockedLinkDao.insertBlockedLink(blockedLink)
        }
    }
    
    /**
     * Check if a link is blocked
     */
    suspend fun isLinkBlocked(link: String): Boolean {
        val normalizedLink = link.lowercase().trim()
        return getBlockedLinkByLink(normalizedLink) != null
    }
    
    /**
     * Remove a blocked link
     */
    suspend fun removeBlockedLink(link: String): Boolean {
        val normalizedLink = link.lowercase().trim()
        val existing = getBlockedLinkByLink(normalizedLink)
        return if (existing != null) {
            blockedLinkDao.deleteBlockedLink(existing)
            true
        } else {
            false
        }
    }
    
    suspend fun removeBlockedLinkById(id: Long) {
        blockedLinkDao.deleteBlockedLinkById(id)
    }
    
    suspend fun clearAllBlockedLinks() {
        blockedLinkDao.deleteAllBlockedLinks()
    }
    
    suspend fun searchBlockedLinks(query: String): List<BlockedLinkEntity> {
        val pattern = "%${query.lowercase().trim()}%"
        return blockedLinkDao.searchBlockedLinks(pattern)
    }
    
    /**
     * Track that a link was blocked (increment count)
     */
    suspend fun trackBlockedLink(link: String) {
        val normalizedLink = link.lowercase().trim()
        val existing = getBlockedLinkByLink(normalizedLink)
        if (existing != null) {
            blockedLinkDao.incrementBlockedCount(normalizedLink)
        } else {
            // If link doesn't exist, add it
            addBlockedLink(normalizedLink)
        }
    }
}

