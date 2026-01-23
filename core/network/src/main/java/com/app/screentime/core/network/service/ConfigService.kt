package com.app.screentime.core.network.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.AppConfig

/**
 * Service interface for Config related API operations
 */
interface ConfigService {

    /**
     * Get features configuration with query parameters
     * @param country Country code (e.g., "US")
     * @param appVersion App version (e.g., "1.2.0")
     * @param language Language code (e.g., "en")
     */
    suspend fun getFeatures(
        country: String? = null,
        appVersion: String? = null,
        language: String? = null
    ): Result<ApiResponse<AppConfig>>
}

