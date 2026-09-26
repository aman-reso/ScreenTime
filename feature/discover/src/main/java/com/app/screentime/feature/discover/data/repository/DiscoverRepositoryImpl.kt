package com.app.screentime.feature.discover.data.repository

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.network.api.DiscoveryApi
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.toDiscoveryMatch
import com.app.screentime.core.network.dto.toModelProfile
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

/**
 * Data-layer implementation of DiscoverRepository.
 * Handles remote API communication, token resolution, data transformation,
 * and robust caching/fallback strategy.
 */
class DiscoverRepositoryImpl @Inject constructor(
    private val winterApi: WinterApi,
    private val discoveryApi: com.app.screentime.core.network.api.DiscoveryApi,
    private val preferencesManager: PreferencesManager
) : DiscoverRepository {

    private val passedProfileIds = mutableSetOf<String>()

    override suspend fun getDiscoveryDeck(page: Int, limit: Int): Result<List<ModelProfile>> {
        return try {
            val token = preferencesManager.getToken().orEmpty()
            val userId = resolveUserId(token)

            val feedResponse = discoveryApi.getFeed(
                token = token,
                page = page,
                limit = limit,
                userId = userId
            )

            if (!feedResponse.success || feedResponse.data == null) {
                return Result.failure(Exception(feedResponse.message?.ifBlank { "Failed to load feed" }))
            }

            val candidates = feedResponse.data!!.items.map { dto ->
                dto.toModelProfile()
            }

            Result.success(candidates)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteProfiles(): Result<List<ModelProfile>> {
        return try {
            val token = preferencesManager.getToken().orEmpty()
            val response = winterApi.getFavorites(token)
            val favorites = response.models.map { dto ->
                val avatar = dto.avatar_url.orEmpty()
                ModelProfile(
                    id = dto.id,
                    name = dto.name.ifBlank { dto.display_name ?: "User" },
                    age = 23,
                    distance = "Nearby",
                    location = "Online",
                    matchedPreferences = "Matched Preferences",
                    bio = dto.bio.orEmpty(),
                    avatarUrl = avatar,
                    coverUrl = avatar,
                    galleryUrls = if (avatar.isNotBlank()) listOf(avatar) else emptyList(),
                    isOnline = dto.is_online,
                    isBusy = dto.is_busy,
                    ratePerMinute = dto.voice_rate_per_min.toInt().coerceAtLeast(10),
                    chatRate = dto.chat_rate_per_msg.toInt().coerceAtLeast(1)
                )
            }
            Result.success(favorites)
        } catch (e: Exception) {
            Result.success(emptyList())
        }
    }

    private fun resolveUserId(token: String): String? {
        val uid = preferencesManager.getUserId()
        if (!uid.isNullOrBlank()) return uid
        if (token.startsWith("token_")) {
            return token.removePrefix("token_")
        }
        return null
    }

    override suspend fun likeProfile(profileId: String): Result<Boolean> {
        return try {
            val token = preferencesManager.getToken().orEmpty()
            val userId = resolveUserId(token)
            val response = discoveryApi.interact(
                token = token,
                targetUserId = profileId,
                action = "like",
                userId = userId
            )
            val isMatch = response.data?.is_match ?: false
            Result.success(isMatch)
        } catch (e: Exception) {
            Result.success(false)
        }
    }

    override suspend fun superLikeProfile(profileId: String): Result<Boolean> {
        return try {
            val token = preferencesManager.getToken().orEmpty()
            val userId = resolveUserId(token)
            val response = discoveryApi.interact(
                token = token,
                targetUserId = profileId,
                action = "superlike",
                userId = userId
            )
            val isMatch = response.data?.is_match ?: true
            Result.success(isMatch)
        } catch (e: Exception) {
            Result.success(true)
        }
    }

    override suspend fun dislikeProfile(profileId: String): Result<Unit> {
        passedProfileIds.add(profileId)
        return try {
            val token = preferencesManager.getToken().orEmpty()
            val userId = resolveUserId(token)
            discoveryApi.interact(
                token = token,
                targetUserId = profileId,
                action = "dismiss",
                userId = userId
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.success(Unit)
        }
    }

    override suspend fun getMatches(
        page: Int,
        limit: Int
    ): Result<List<com.app.screentime.core.model.DiscoveryMatch>> {
        return try {
            val token = preferencesManager.getToken().orEmpty()
            val userId = resolveUserId(token)
            val response = discoveryApi.getMatches(
                token = token,
                page = page,
                limit = limit,
                userId = userId
            )
            if (response.success && response.data != null) {
                val matches = response.data!!.items.map { it.toDiscoveryMatch() }
                Result.success(matches)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfileDetails(profileId: String): Result<ModelProfile> {
        return try {
            val token = preferencesManager.getToken().orEmpty()
            val dto = winterApi.getModelProfile(token, profileId)
            val avatar = dto.avatar_url.orEmpty()
            Result.success(
                ModelProfile(
                    id = dto.id,
                    name = dto.name.ifBlank { dto.display_name ?: "Member" },
                    age = 24,
                    distance = "Nearby",
                    location = "Online",
                    matchedPreferences = "Matched Preferences",
                    bio = dto.bio ?: "",
                    avatarUrl = avatar,
                    coverUrl = avatar,
                    galleryUrls = if (avatar.isNotBlank()) listOf(avatar) else emptyList(),
                    ratePerMinute = dto.voice_rate_per_min.toInt().coerceAtLeast(15),
                    chatRate = dto.chat_rate_per_msg.toInt().coerceAtLeast(2),
                    isOnline = dto.is_online,
                    isBusy = dto.is_busy,
                    tags = emptyList()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
