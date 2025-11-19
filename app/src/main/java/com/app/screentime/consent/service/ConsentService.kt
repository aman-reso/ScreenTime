package com.app.screentime.consent.service

import com.app.screentime.network.model.*

/**
 * API Service interface for Consent operations
 */
interface ConsentService {
    suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>>
    suspend fun submitConsents(request: ConsentSubmissionRequest): Result<ApiResponse<List<ConsentSubmissionResponseItem>>>
    suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse>
    suspend fun getConsentStatus(username: String): Result<ConsentResponse>
}

