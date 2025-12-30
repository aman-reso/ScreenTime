package com.app.screentime.consent.mapper

import com.app.screentime.consent.model.ConsentUiModel
import com.app.screentime.core.network.model.*
import com.app.screentime.network.model.*
import javax.inject.Inject

/**
 * Mapper for consent data between API models and UI models
 */
class ConsentMapper @Inject constructor() {
    
    /**
     * Map ConsentResponse to ConsentUiModel
     */
    fun toUiModel(response: ConsentResponse): ConsentUiModel {
        return ConsentUiModel(
            username = response.username ?: "",
            hasConsent = response.hasConsent ?: false,
            dataSharing = response.dataSharing ?: false,
            analytics = response.analytics ?: false,
            marketing = response.marketing ?: false,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt
        )
    }

    /**
     * Map ConsentUiModel to ConsentRequest
     */
    fun toRequest(uiModel: ConsentUiModel): ConsentRequest {
        return ConsentRequest(
            username = uiModel.username,
            hasConsent = uiModel.hasConsent,
            dataSharing = uiModel.dataSharing,
            analytics = uiModel.analytics,
            marketing = uiModel.marketing
        )
    }

    /**
     * Map ConsentResponse to ConsentRequest (for updates)
     */
    fun toRequest(response: ConsentResponse, newValues: ConsentUiModel): ConsentRequest {
        return ConsentRequest(
            username = response.username ?: "",
            hasConsent = newValues.hasConsent,
            dataSharing = newValues.dataSharing,
            analytics = newValues.analytics,
            marketing = newValues.marketing
        )
    }
}

