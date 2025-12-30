package com.app.screentime.network.repository.urlsearch

import com.app.screentime.core.network.model.ApiResponse
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

}

