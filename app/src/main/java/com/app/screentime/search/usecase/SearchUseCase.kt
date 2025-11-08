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
     * Search users by username
     * @param username The username to search for
     * @return Result of search results
     */
    suspend fun searchUsers(username: String): Result<List<UserSearchResult>> {
        return searchRepository.searchUsers(username).map { apiResponse ->
            apiResponse.data ?: emptyList()
        }
    }
}
