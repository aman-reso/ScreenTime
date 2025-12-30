package com.app.screentime.search.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.UserSearchResult

/**
 * API Service interface for Search operations
 */
interface SearchService {
    suspend fun searchUsers(query: String): Result<ApiResponse<List<UserSearchResult>>>
}

