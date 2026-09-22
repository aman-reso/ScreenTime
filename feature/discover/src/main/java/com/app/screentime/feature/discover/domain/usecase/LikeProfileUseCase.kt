package com.app.screentime.feature.discover.domain.usecase

import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

/**
 * UseCase to like a profile and determine if it triggers a mutual match.
 */
class LikeProfileUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(profileId: String): Result<Boolean> {
        return repository.likeProfile(profileId)
    }
}
