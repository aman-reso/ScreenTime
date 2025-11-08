package com.app.screentime.consent.model

/**
 * UI model for consent/preferences data
 */
data class ConsentUiModel(
    val username: String,
    val hasConsent: Boolean,
    val dataSharing: Boolean,
    val analytics: Boolean,
    val marketing: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    /**
     * Check if all required consents are given
     */
    fun hasAllRequiredConsents(): Boolean {
        return hasConsent && dataSharing
    }

    /**
     * Check if user has given partial consent
     */
    fun hasPartialConsent(): Boolean {
        return hasConsent || dataSharing || analytics || marketing
    }
}

