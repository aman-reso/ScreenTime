package com.app.screentime.feature.profile.domain.usecase

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * UseCase to fetch profiles of users who liked/followed the current user.
 */
class GetWhoLikedMeProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<ModelProfile>> {
        return profileRepository.getWhoLikedMeProfiles()
    }
}
