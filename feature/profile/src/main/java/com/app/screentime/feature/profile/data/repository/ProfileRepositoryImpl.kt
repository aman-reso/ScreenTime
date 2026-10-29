package com.app.screentime.feature.profile.data.repository

import android.content.Context
import android.net.Uri
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.DiscoveryApi
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.core.network.dto.ProfileVisibilityResponseData
import com.app.screentime.core.network.dto.UploadImagesResponse
import com.app.screentime.core.network.dto.toModelProfile
import com.app.screentime.core.network.dto.toUser
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.util.ImageCompressor
import com.app.screentime.core.network.util.VideoCompressor
import com.app.screentime.feature.profile.data.local.NotificationDao
import com.app.screentime.feature.profile.data.local.NotificationEntity
import com.app.screentime.feature.profile.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

import com.app.screentime.feature.chat.data.local.LocalChatStorage
import android.util.Log

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val api: WinterApi,
    private val discoveryApi: DiscoveryApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val notificationDao: NotificationDao,
    private val localChatStorage: LocalChatStorage
) : ProfileRepository {

    override fun getCurrentUser(): User {
        val phone = sessionManager.userPhone ?: preferencesManager.getPhone() ?: ""
        val name = sessionManager.userName ?: preferencesManager.getUsername() ?: "User"
        val id = sessionManager.userId ?: preferencesManager.getUserId() ?: ""
        val role = sessionManager.userRole
        val walletBalance = if (role == UserRole.USER) 1000.0 else 0.0
        return User(
            id = id,
            phone = phone,
            name = name,
            role = role,
            walletBalance = walletBalance
        )
    }

    override suspend fun fetchUserProfile(userId: String?): Result<User> {
        val token = sessionManager.getToken() ?: preferencesManager.getToken() ?: ""
        if (token.isBlank()) {
            return Result.failure(Exception("Not logged in"))
        }
        return try {
            val dto = api.getUserProfile(token, userId)
            val user = dto.toUser()
            if (userId.isNullOrBlank() || userId == "me" || userId == sessionManager.userId) {
                sessionManager.saveSession(token, user)
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLikedProfiles(): Result<List<ModelProfile>> {
        val token = sessionManager.getToken() ?: preferencesManager.getToken() ?: ""
        if (token.isBlank()) {
            return Result.success(emptyList())
        }
        return try {
            val response = discoveryApi.getFollowing(token)
            if (response.success) {
                val items = response.data?.items.orEmpty()
                Result.success(items.map { it.toModelProfile() })
            } else {
                Result.failure(Exception(response.message.ifBlank { "Failed to load liked profiles" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWhoLikedMeProfiles(): Result<List<ModelProfile>> {
        val token = sessionManager.getToken() ?: preferencesManager.getToken() ?: ""
        if (token.isBlank()) {
            return Result.success(emptyList())
        }
        return try {
            val response = discoveryApi.getFollowers(token)
            if (response.success) {
                val items = response.data?.items.orEmpty()
                Result.success(items.map { it.toModelProfile() })
            } else {
                Result.failure(Exception(response.message.ifBlank { "Failed to load profiles who liked you" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMatchesProfiles(): Result<List<ModelProfile>> {
        val token = sessionManager.getToken() ?: preferencesManager.getToken() ?: ""
        if (token.isBlank()) {
            return Result.success(emptyList())
        }
        return try {
            val response = discoveryApi.getMatches(token)
            if (response.success) {
                val items = response.data?.items.orEmpty()
                val mapped = items.map { match ->
                    val photo = match.primary_photo_url.orEmpty()
                    val locStr = when {
                        match.area.isNotBlank() && match.city.isNotBlank() -> "${match.area}, ${match.city}"
                        match.city.isNotBlank() -> match.city
                        match.area.isNotBlank() -> match.area
                        else -> ""
                    }
                    ModelProfile(
                        id = match.matched_user_id,
                        name = match.display_name.ifBlank { "Member" },
                        age = match.age,
                        location = locStr,
                        bio = match.bio,
                        avatarUrl = photo,
                        coverUrl = photo,
                        galleryUrls = if (photo.isNotBlank()) listOf(photo) else emptyList(),
                        tags = if (match.dating_intent.isNotBlank()) listOf(match.dating_intent) else emptyList()
                    )
                }
                Result.success(mapped)
            } else {
                Result.failure(Exception(response.message.ifBlank { "Failed to load matches" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(request: ProfileUpdateRequest): Result<Boolean> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Not logged in"))
        return try {
            val response = api.updateProfile(token, request)
            if (response.success) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message.ifBlank { "Profile update failed" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfileVisibility(visibility: String): Result<ProfileVisibilityResponseData> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Not logged in"))
        return api.updateProfileVisibility(token, visibility)
    }

    override suspend fun uploadImages(uris: List<Uri>): Result<List<String>> =
        withContext(Dispatchers.IO) {
            val token = sessionManager.getToken() ?: preferencesManager.getToken() ?: ""
            if (token.isBlank()) {
                return@withContext Result.failure(Exception("Not logged in"))
            }
            try {
                val parts = uris.mapIndexedNotNull { index, uri ->
                    ImageCompressor.compressUriToPart(context, uri, index)
                }
                if (parts.isEmpty()) {
                    return@withContext Result.failure(IllegalArgumentException("No valid image files found"))
                }

                val response: UploadImagesResponse = api.uploadImageParts(token, parts)
                val urls = response.getUploadedUrls()
                if (urls.isNotEmpty()) {
                    Result.success(urls)
                } else if (response.success) {
                    Result.success(emptyList())
                } else {
                    Result.failure(Exception(response.message.ifBlank { "Upload failed" }))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun uploadVideo(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        val token = sessionManager.getToken() ?: preferencesManager.getToken() ?: ""
        if (token.isBlank()) {
            return@withContext Result.failure(Exception("Not logged in"))
        }
        try {
            val videoPart = VideoCompressor.compressVideoToPart(context, uri)
            val response: UploadImagesResponse = api.uploadVideo(token, videoPart)
            val url = response.getUploadedUrls().firstOrNull() ?: response.data?.url
            if (!url.isNullOrBlank()) {
                Result.success(url)
            } else if (!response.isSuccess) {
                Result.failure(Exception(response.message.ifBlank { "Video upload failed" }))
            } else {
                Result.failure(Exception("No video URL returned from server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitModelOnboarding(
        bio: String,
        voiceRate: Double,
        chatRate: Double
    ): Result<Boolean> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Not logged in"))
        return try {
            api.submitOnboarding(
                token = token,
                bio = bio,
                voiceRate = voiceRate,
                chatRate = chatRate
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            sessionManager.clearSession(isAccountDeletion = true)
            preferencesManager.clearAll()
            notificationDao.clearAllNotifications()
            localChatStorage.clearAllMessages()
            com.app.screentime.core.model.ActiveChatTracker.activePartnerId = null
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        sessionManager.clearSession()
        preferencesManager.clearAll()
        try {
            notificationDao.clearAllNotifications()
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Failed to clear notifications Room DB", e)
        }
        try {
            localChatStorage.clearAllMessages()
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Failed to clear chat Room DB", e)
        }
        com.app.screentime.core.model.ActiveChatTracker.activePartnerId = null
    }

    override fun getNotificationsFlow(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotificationsFlow()
    }

    override suspend fun markNotificationAsRead(id: String) {
        notificationDao.markAsRead(id)
    }

    override suspend fun clearAllNotifications() {
        notificationDao.clearAllNotifications()
    }
}
