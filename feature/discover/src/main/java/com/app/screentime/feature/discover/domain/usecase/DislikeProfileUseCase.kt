package com.app.screentime.feature.discover.domain.usecase

import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

/**
 * UseCase to record passing or disliking a profile.
 */
class DislikeProfileUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(profileId: String): Result<Unit> {
        return repository.dislikeProfile(profileId)
    }
}
