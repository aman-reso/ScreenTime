package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * Request model for adding coins
 */
@Serializable
data class AddCoinsRequest(
    val userId: String,
    val amount: Int,
    val source: String, // CoinSource
    val description: String? = null,
    val challengeId: Long? = null,
    val challengeTitle: String? = null,
    val rank: Int? = null,
    val metadata: String? = null,
    val expiresAt: String? = null // ISO 8601 format, null = never expires
)

