package com.app.screentime.profile.model

/**
 * UI Props for Blocked Sites Bottom Sheet
 * Contains all the data needed to render the blocked sites bottom sheet
 */
data class BlockedSitesUiProps(
    val blockedSites: List<String>,
    val isLoading: Boolean = false,
    val error: String? = null
)

