package com.app.screentime.network.repository.urlsearch

import com.app.screentime.network.model.*
import com.app.screentime.network.service.urlsearch.URLSearchService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for URL Search (VPN tracking) operations
 */
@Singleton
class URLSearchRepository @Inject constructor(
    private val urlSearchService: URLSearchService
) {
    suspend fun submitURLSearch(request: URLSearchSubmissionRequest): Result<ApiResponse<URLSearchSubmissionResponse>> {
        return urlSearchService.submitURLSearch(request)
    }

    suspend fun batchSubmitURLSearch(request: BatchURLSearchSubmissionRequest): Result<ApiResponse<URLSearchSubmissionResponse>> {
        return urlSearchService.batchSubmitURLSearch(request)
    }

    suspend fun getURLHistory(request: URLSearchHistoryRequest): Result<ApiResponse<URLSearchHistoryResponse>> {
        return urlSearchService.getURLHistory(request)
    }
}

