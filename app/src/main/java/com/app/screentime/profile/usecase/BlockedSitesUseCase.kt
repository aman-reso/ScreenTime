package com.app.screentime.profile.usecase

import com.app.screentime.database.repository.BlockedLinkRepository
import com.app.screentime.profile.mapper.BlockedSitesMapper
import com.app.screentime.profile.model.BlockedSitesUiProps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Use case for blocked sites operations
 * Contains all business logic for blocked sites
 */
class BlockedSitesUseCase @Inject constructor(
    private val blockedLinkRepository: BlockedLinkRepository,
    private val blockedSitesMapper: BlockedSitesMapper
) {
    /**
     * Get all blocked sites as UI Props
     */
    suspend fun getBlockedSitesUiProps(
        isLoading: Boolean = false,
        error: String? = null
    ): BlockedSitesUiProps {
        return try {
            val blockedSites = withContext(Dispatchers.IO) {
                blockedLinkRepository.getAllBlockedLinkStrings()
            }
            blockedSitesMapper.toUiProps(
                blockedSites = blockedSites,
                isLoading = false,
                error = null
            )
        } catch (e: Exception) {
            blockedSitesMapper.toUiProps(
                blockedSites = emptyList(),
                isLoading = false,
                error = e.message ?: "Failed to load blocked sites"
            )
        }
    }

    /**
     * Get blocked sites count
     */
    suspend fun getBlockedSitesCount(): Int {
        return withContext(Dispatchers.IO) {
            blockedLinkRepository.getBlockedLinksCount()
        }
    }
}

