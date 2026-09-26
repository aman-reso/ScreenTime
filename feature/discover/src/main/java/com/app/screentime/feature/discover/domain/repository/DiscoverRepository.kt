package com.app.screentime.feature.discover.domain.repository

import com.app.screentime.core.model.ModelProfile

/**
 * Domain repository abstraction for discovery and dating profile interactions.
 * Adheres to Clean Architecture principles by decoupling business logic from data sources.
 */
interface DiscoverRepository {
    /**
     * Fetches the discovery deck of dating profiles with pagination support.
     */
    suspend fun getDiscoveryDeck(page: Int = 1, limit: Int = 10): Result<List<ModelProfile>>

    /**
     * Fetches the user's liked and favorite profiles.
     */
    suspend fun getFavoriteProfiles(): Result<List<ModelProfile>>

    /**
     * Likes a profile by its ID. Returns true if it results in a mutual match.
     */
    suspend fun likeProfile(profileId: String): Result<Boolean>

    /**
     * Super-likes a profile by its ID.
     */
    suspend fun superLikeProfile(profileId: String): Result<Boolean>

    /**
     * Records passing/disliking a profile so it is not immediately presented again.
     */
    suspend fun dislikeProfile(profileId: String): Result<Unit>

    /**
     * Retrieves detailed profile information for a specific user/model.
     */
    suspend fun getProfileDetails(profileId: String): Result<ModelProfile>

    /**
     * Fetches confirmed mutual matches from the discovery/matches API.
     */
    suspend fun getMatches(page: Int = 1, limit: Int = 10): Result<List<com.app.screentime.core.model.DiscoveryMatch>>
}
