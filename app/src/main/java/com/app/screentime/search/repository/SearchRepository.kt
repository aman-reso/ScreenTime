package com.app.screentime.search.repository

import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.UserSearchResult
import com.app.screentime.network.repository.NetworkRepository
import javax.inject.Inject

/**
 * Repository for search operations
 */
class SearchRepository @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    /**
     * Search users by username
     */
    suspend fun searchUsers(username: String): Result<ApiResponse<List<UserSearchResult>>> {
        return networkRepository.searchUsers(username)
    }
}
