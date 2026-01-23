package com.app.screentime.core.network.service

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.FeedbackRequest
import com.app.screentime.core.network.model.FeedbackResponse

/**
 * Service interface for Feedback related API operations
 */
interface FeedbackService {
    /**
     * Submit user feedback
     * @param message The feedback message from the user
     */
    suspend fun submitFeedback(message: String): Result<ApiResponse<FeedbackResponse>>
}
