package com.app.screentime.core.network.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.FeedbackRequest
import com.app.screentime.core.network.model.FeedbackResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of FeedbackService using Ktor
 */
@Singleton
class FeedbackServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : FeedbackService {

    private val client = networkClient.httpClient

    override suspend fun submitFeedback(message: String): Result<ApiResponse<FeedbackResponse>> {
        return try {
            val request = FeedbackRequest(message = message)
            
            val response: HttpResponse = client.post(ApiEndpoints.Feedback.SUBMIT) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<FeedbackResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to submit feedback: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Client error: ${e.response.status}, $errorBody", e))
        } catch (e: ServerResponseException) {
            val errorBody = e.response.bodyAsText()
            Result.failure(Exception("Server error: ${e.response.status}, $errorBody", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
