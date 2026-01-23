package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * Saved claim details for future reference
 */
@Serializable
data class SavedClaimDetails(
    val name: String,
    val email: String,
    val phone: String,
    val upiId: String,
    val address: String? = null,
    val postalCode: String? = null
)

