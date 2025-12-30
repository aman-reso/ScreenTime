package com.app.screentime.search.repository

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.UserSearchResult
import com.app.screentime.search.service.SearchService
import javax.inject.Inject

/**
 * Repository for search operations
 */
class SearchRepository @Inject constructor(
    private val searchService: SearchService
) {
    /**
     * Search users by query string
     * @param query Search query (username)
     */
    suspend fun searchUsers(query: String): Result<ApiResponse<List<UserSearchResult>>> {
        return searchService.searchUsers(query)
    }
}
