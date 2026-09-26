package com.app.screentime.feature.discover.domain.usecase

import com.app.screentime.core.model.DiscoveryMatch
import com.app.screentime.feature.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 10): Result<List<DiscoveryMatch>> {
        return repository.getMatches(page = page, limit = limit)
    }
}
