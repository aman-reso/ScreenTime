package com.app.screentime.feature.profile.domain.usecase

import com.app.screentime.feature.profile.data.local.NotificationEntity
import com.app.screentime.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Single-responsibility UseCase to manage notifications.
 * Connects notifications domain logic with local Room DB via ProfileRepository.
 */
class GetNotificationsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<List<NotificationEntity>> {
        return profileRepository.getNotificationsFlow()
    }

    suspend fun markAsRead(id: String) {
        profileRepository.markNotificationAsRead(id)
    }

    suspend fun clearAll() {
        profileRepository.clearAllNotifications()
    }
}
