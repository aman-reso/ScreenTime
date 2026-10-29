package com.app.screentime.feature.profile.domain.usecase

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Single-responsibility UseCase to fetch liked/followed profiles for current user.
 */
class GetLikedProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<ModelProfile>> {
        return profileRepository.getLikedProfiles()
    }
}
