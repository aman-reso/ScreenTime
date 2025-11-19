package com.app.screentime.network.service.urlsearch

import com.app.screentime.network.model.*

/**
 * Service interface for URL Search (VPN tracking) related API operations
 */
interface URLSearchService {
    suspend fun submitURLSearch(request: URLSearchSubmissionRequest): Result<ApiResponse<URLSearchSubmissionResponse>>
    suspend fun batchSubmitURLSearch(request: BatchURLSearchSubmissionRequest): Result<ApiResponse<URLSearchSubmissionResponse>>
    suspend fun getURLHistory(request: URLSearchHistoryRequest): Result<ApiResponse<URLSearchHistoryResponse>>
}

