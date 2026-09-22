package com.app.screentime.feature.discover.domain.usecase

import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

/**
 * UseCase to super-like a profile with high priority notification.
 */
class SuperLikeProfileUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(profileId: String): Result<Boolean> {
        return repository.superLikeProfile(profileId)
    }
}
