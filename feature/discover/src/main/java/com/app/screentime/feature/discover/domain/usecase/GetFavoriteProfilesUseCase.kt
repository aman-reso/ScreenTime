package com.app.screentime.feature.discover.domain.usecase

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

/**
 * UseCase to fetch the list of profiles that the current user has liked or favorited.
 */
class GetFavoriteProfilesUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(): Result<List<ModelProfile>> {
        return repository.getFavoriteProfiles()
    }
}
