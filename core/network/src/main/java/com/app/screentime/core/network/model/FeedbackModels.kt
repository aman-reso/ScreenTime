package com.app.screentime.core.network.model

import kotlinx.serialization.Serializable

/**
 * Request model for submitting feedback
 */
@Serializable
data class FeedbackRequest(
    val message: String
)

/**
 * Response model for feedback submission.
 * Matches API: { "data": { "message": "Feedback submitted successfully" } }
 */
@Serializable
data class FeedbackResponse(
    val message: String? = null
)
