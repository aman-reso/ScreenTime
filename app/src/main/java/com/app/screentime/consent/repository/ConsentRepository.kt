package com.app.screentime.consent.repository

import com.app.screentime.network.model.ApiConsentItem
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.ConsentRequest
import com.app.screentime.network.model.ConsentResponse
import com.app.screentime.network.model.ConsentSubmissionRequest
import com.app.screentime.network.model.ConsentSubmissionResponseItem
import com.app.screentime.network.repository.NetworkRepository
import javax.inject.Inject

/**
 * Repository for consent/privacy operations
 */
class ConsentRepository @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    /**
     * Get all consents from API
     */
    suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>> {
        return networkRepository.getConsents()
    }

    /**
     * Submit consents with new structure
     */
    suspend fun submitConsents(request: ConsentSubmissionRequest): Result<ApiResponse<List<ConsentSubmissionResponseItem>>> {
        return networkRepository.submitConsents(request)
    }

    /**
     * Submit consent data (legacy)
     */
    suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse> {
        return networkRepository.submitConsent(consentRequest)
    }

    /**
     * Get consent status for a user
     */
    suspend fun getConsentStatus(username: String): Result<ConsentResponse> {
        return networkRepository.getConsentStatus(username)
    }
}

