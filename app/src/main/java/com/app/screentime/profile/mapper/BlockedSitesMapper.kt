package com.app.screentime.profile.mapper

import com.app.screentime.profile.model.BlockedSitesUiProps
import javax.inject.Inject

/**
 * Mapper that converts use case results to UI Props for Blocked Sites
 */
class BlockedSitesMapper @Inject constructor() {

    /**
     * Map blocked sites list to UI Props
     */
    fun toUiProps(
        blockedSites: List<String>,
        isLoading: Boolean = false,
        error: String? = null
    ): BlockedSitesUiProps {
        return BlockedSitesUiProps(
            blockedSites = blockedSites.sorted(),
            isLoading = isLoading,
            error = error
        )
    }
}

