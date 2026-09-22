package com.app.screentime.feature.discover.domain.usecase

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

/**
 * UseCase to retrieve the deck of dating profiles for discovery swiping.
 */
class GetDiscoveryDeckUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 10): Result<List<ModelProfile>> {
        return repository.getDiscoveryDeck(page = page, limit = limit)
    }
}
