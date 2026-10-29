package com.app.screentime.feature.profile.domain.repository

import android.net.Uri
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.model.User
import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.core.network.dto.ProfileVisibilityResponseData

import kotlinx.coroutines.flow.Flow
import com.app.screentime.feature.profile.data.local.NotificationEntity

/**
 * Domain repository interface for Profile operations.
 * Decouples profile business logic from concrete data sources and network APIs.
 */
interface ProfileRepository {
    fun getCurrentUser(): User
    suspend fun fetchUserProfile(userId: String? = null): Result<User>
    suspend fun getLikedProfiles(): Result<List<ModelProfile>>
    suspend fun getWhoLikedMeProfiles(): Result<List<ModelProfile>>
    suspend fun getMatchesProfiles(): Result<List<ModelProfile>>
    suspend fun updateProfile(request: ProfileUpdateRequest): Result<Boolean>
    suspend fun updateProfileVisibility(visibility: String): Result<ProfileVisibilityResponseData>
    suspend fun uploadImages(uris: List<Uri>): Result<List<String>>
    suspend fun uploadVideo(uri: Uri): Result<String>
    suspend fun submitModelOnboarding(bio: String, voiceRate: Double, chatRate: Double): Result<Boolean>
    suspend fun deleteAccount(): Result<Boolean>
    suspend fun logout()

    // Notification DAO methods
    fun getNotificationsFlow(): Flow<List<NotificationEntity>>
    suspend fun markNotificationAsRead(id: String)
    suspend fun clearAllNotifications()
}
