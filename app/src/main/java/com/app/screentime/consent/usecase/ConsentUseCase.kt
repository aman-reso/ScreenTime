package com.app.screentime.consent.usecase

import com.app.screentime.consent.mapper.ConsentMapper
import com.app.screentime.consent.model.ConsentUiModel
import com.app.screentime.consent.repository.ConsentRepository
import com.app.screentime.core.network.model.*
import com.app.screentime.network.model.*
import javax.inject.Inject

/**
 * Use case for consent/privacy operations
 */
class ConsentUseCase @Inject constructor(
    private val consentRepository: ConsentRepository,
    private val consentMapper: ConsentMapper
) {
    /**
     * Get all consents from API
     * @return Result of API response containing list of consent items
     */
    suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>> {
        return consentRepository.getConsents()
    }

    /**
     * Submit consents with new structure
     * @param request The consent submission request
     * @return Result of API response (only check success field, ignore data)
     */
    suspend fun submitConsents(request: ConsentSubmissionRequest): Result<ApiResponse<List<ConsentSubmissionResponseItem>>> {
        return consentRepository.submitConsents(request)
    }

    /**
     * Submit consent data (legacy)
     * @param consentRequest The consent request
     * @return Result of consent response
     */
    suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse> {
        return consentRepository.submitConsent(consentRequest)
    }

    /**
     * Get consent status for a user
     * @param username The username
     * @return Result of consent response
     */
    suspend fun getConsentStatus(username: String): Result<ConsentResponse> {
        return consentRepository.getConsentStatus(username)
    }

    /**
     * Get consent UI model for a user
     * @param username The username
     * @return Result of consent UI model
     */
    suspend fun getConsentUiModel(username: String): Result<ConsentUiModel> {
        return consentRepository.getConsentStatus(username).map { response ->
            consentMapper.toUiModel(response)
        }
    }

    /**
     * Submit consent from UI model
     * @param uiModel The consent UI model
     * @return Result of consent response
     */
    suspend fun submitConsentFromUiModel(uiModel: ConsentUiModel): Result<ConsentResponse> {
        val consentRequest = consentMapper.toRequest(uiModel)
        return consentRepository.submitConsent(consentRequest)
    }
}

