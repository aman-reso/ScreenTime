package com.app.screentime.feature.profile.domain.usecase

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * UseCase to fetch mutual matches for the current user.
 */
class GetMatchesProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<ModelProfile>> {
        return profileRepository.getMatchesProfiles()
    }
}
