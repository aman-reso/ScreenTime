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
    private val preferencesManager: PreferencesManager,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager
) : DiscoverRepository {

    private val passedProfileIds = mutableSetOf<String>()

    override suspend fun getDiscoveryDeck(page: Int, limit: Int): Result<List<ModelProfile>> {
        val token = preferencesManager.getToken().orEmpty()
        if (token.isBlank()) {
            return Result.failure(Exception("Authentication token required"))
        }
        return try {
            val userId = resolveUserId(token)

            val feedResponse = discoveryApi.getFeed(
                token = token,
                page = page,
                limit = limit,
                userId = userId
            )

            val quotaInfo = feedResponse.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateHomeQuota(quotaInfo)
            }

            if (!feedResponse.success) {
                return Result.failure(Exception(feedResponse.message?.ifBlank { "Failed to load feed" }))
            }

            val candidates = feedResponse.data?.items?.map { dto ->
                dto.toModelProfile()
            } ?: emptyList()

            Result.success(candidates)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteProfiles(): Result<List<ModelProfile>> {
        val token = preferencesManager.getToken().orEmpty()
        if (token.isBlank()) {
            return Result.success(emptyList())
        }
        return try {
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
        val token = preferencesManager.getToken().orEmpty()
        if (token.isBlank()) {
            return Result.failure(Exception("Authentication token required"))
        }
        return try {
            val userId = resolveUserId(token)
            val response = discoveryApi.interact(
                token = token,
                targetUserId = profileId,
                action = "like",
                userId = userId
            )
            val quotaInfo = response.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateHomeQuota(quotaInfo)
            }
            val isMatch = response.data?.is_match ?: false
            Result.success(isMatch)
        } catch (e: Exception) {
            Result.success(false)
        }
    }

    override suspend fun superLikeProfile(profileId: String): Result<Boolean> {
        val token = preferencesManager.getToken().orEmpty()
        if (token.isBlank()) {
            return Result.failure(Exception("Authentication token required"))
        }
        return try {
            val userId = resolveUserId(token)
            val response = discoveryApi.interact(
                token = token,
                targetUserId = profileId,
                action = "superlike",
                userId = userId
            )
            val quotaInfo = response.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateHomeQuota(quotaInfo)
            }
            val isMatch = response.data?.is_match ?: true
            Result.success(isMatch)
        } catch (e: Exception) {
            Result.success(true)
        }
    }

    override suspend fun dislikeProfile(profileId: String): Result<Unit> {
        passedProfileIds.add(profileId)
        val token = preferencesManager.getToken().orEmpty()
        if (token.isBlank()) {
            return Result.success(Unit)
        }
        return try {
            val userId = resolveUserId(token)
            val response = discoveryApi.interact(
                token = token,
                targetUserId = profileId,
                action = "dismiss",
                userId = userId
            )
            val quotaInfo = response.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateHomeQuota(quotaInfo)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.success(Unit)
        }
    }

    override suspend fun getMatches(
        page: Int,
        limit: Int
    ): Result<List<com.app.screentime.core.model.DiscoveryMatch>> {
        val token = preferencesManager.getToken().orEmpty()

        if (token.isNotBlank()) {
            try {
                val userId = resolveUserId(token)
                val response = discoveryApi.getMatches(
                    token = token,
                    page = page,
                    limit = limit,
                    userId = userId
                )
                if (response.success && response.data != null) {
                    val matches = response.data!!.items.map { it.toDiscoveryMatch() }
                    return Result.success(matches)
                }
            } catch (e: Exception) {
                android.util.Log.w("DiscoverRepo", "Primary discoveryApi.getMatches failed: ${e.message}")
                return Result.failure(e)
            }
        }

        return Result.success(emptyList())
    }

    override suspend fun getProfileDetails(profileId: String): Result<ModelProfile> {
        val token = preferencesManager.getToken().orEmpty()
        if (token.isBlank()) {
            return Result.failure(Exception("Authentication token required"))
        }
        return try {
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
