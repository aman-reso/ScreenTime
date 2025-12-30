package com.app.screentime.consent.repository

import com.app.screentime.consent.service.ConsentService
import com.app.screentime.core.network.model.*
import com.app.screentime.network.model.*
import javax.inject.Inject

/**
 * Repository for consent/privacy operations
 */
class ConsentRepository @Inject constructor(
    private val consentService: ConsentService
) {
    /**
     * Get all consents from API
     */
    suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>> {
        return consentService.getConsents()
    }

    /**
     * Submit consents with new structure
     */
    suspend fun submitConsents(request: ConsentSubmissionRequest): Result<ApiResponse<List<ConsentSubmissionResponseItem>>> {
        return consentService.submitConsents(request)
    }

    /**
     * Submit consent data (legacy)
     */
    suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse> {
        return consentService.submitConsent(consentRequest)
    }

    /**
     * Get consent status for a user
     */
    suspend fun getConsentStatus(username: String): Result<ConsentResponse> {
        return consentService.getConsentStatus(username)
    }
}

