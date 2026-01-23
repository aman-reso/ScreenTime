package com.app.screentime.reward.model

import kotlinx.serialization.Serializable

/**
 * Response model for adding coins
 */
@Serializable
data class AddCoinsResponse(
    val success: Boolean,
    val status: Int,
    val data: AddCoinsData? = null,
    val message: String? = null,
    val timestamp: String? = null,
    val error: String? = null
)

/**
 * Data model for add coins response
 */
@Serializable
data class AddCoinsData(
    val userId: String? = null,
    val amount: Long? = null,
    val totalCoins: Long? = null,
    val source: String? = null,
    val description: String? = null
)

