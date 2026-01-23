package com.app.screentime.core.network.model

import kotlinx.serialization.Serializable

/**
 * Consent item from API
 */
@Serializable
data class ApiConsentItem(
    val id: Int,
    val name: String,
    val description: String,
    val isMandatory: Boolean
)

/**
 * Consent submission item model
 */
@Serializable
data class ConsentSubmissionItem(
    val id: Int,
    val value: String // "accepted" or "rejected"
)

/**
 * Consent submission request model
 */
@Serializable
data class ConsentSubmissionRequest(
    val consents: List<ConsentSubmissionItem>
)

/**
 * Consent submission response item model
 */
@Serializable
data class ConsentSubmissionResponseItem(
    val id: Int,
    val consentId: Int? = null,
    val consentName: String? = null,
    val value: String? = null,
    val submittedAt: String? = null
)

/**
 * Consent request model (legacy - kept for backward compatibility)
 */
@Serializable
data class ConsentRequest(
    val username: String,
    val hasConsent: Boolean,
    val dataSharing: Boolean,
    val analytics: Boolean,
    val marketing: Boolean
)

/**
 * Consent response model
 */
@Serializable
data class ConsentResponse(
    val username: String? = null,
    val hasConsent: Boolean? = null,
    val dataSharing: Boolean? = null,
    val analytics: Boolean? = null,
    val marketing: Boolean? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
