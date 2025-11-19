package com.app.screentime.search.usecase

import com.app.screentime.network.model.UserSearchResult
import com.app.screentime.search.repository.SearchRepository
import javax.inject.Inject

/**
 * Use case for search operations
 */
class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    /**
     * Search users by query string
     * @param query Search query (username)
     * @return Result of search results
     */
    suspend fun searchUsers(query: String): Result<List<UserSearchResult>> {
        return searchRepository.searchUsers(query).map { apiResponse ->
            apiResponse.data ?: emptyList()
        }
    }
}
