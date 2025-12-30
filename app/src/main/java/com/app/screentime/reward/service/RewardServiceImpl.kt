package com.app.screentime.reward.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiError
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.reward.model.CoinHistoryResponse
import com.app.screentime.reward.model.RewardCatalogResponse
import com.app.screentime.reward.model.RewardClaimRequest
import com.app.screentime.reward.model.RewardClaimResponse
import com.app.screentime.reward.model.RewardTransactionResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of RewardService using Ktor
 */
@Singleton
class RewardServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : RewardService {

    private val httpClient = networkClient.httpClient

    override suspend fun getCoinHistory(): Result<ApiResponse<CoinHistoryResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Rewards.COINS)

            if (response.status.isSuccess()) {
                // The API returns CoinHistoryResponse directly, not wrapped in ApiResponse
                val coinHistoryResponse: CoinHistoryResponse = response.body()
                // Wrap it in ApiResponse for consistency
                val apiResponse = ApiResponse(
                    success = coinHistoryResponse.success,
                    status = coinHistoryResponse.status,
                    data = coinHistoryResponse,
                    message = coinHistoryResponse.message,
                    timestamp = coinHistoryResponse.timestamp,
                    error = coinHistoryResponse.error?.let { ApiError(message = it) }
                )
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get coin history: ${response.status}"))
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


    override suspend fun getRewardCatalog(): Result<ApiResponse<RewardCatalogResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Rewards.CATALOG)

            if (response.status.isSuccess()) {
                // The API returns RewardCatalogResponse directly, not wrapped in ApiResponse
                val catalogResponse: RewardCatalogResponse = response.body()
                // Wrap it in ApiResponse for consistency
                val apiResponse = ApiResponse(
                    success = catalogResponse.success,
                    status = catalogResponse.status,
                    data = catalogResponse,
                    message = catalogResponse.message,
                    timestamp = catalogResponse.timestamp,
                    error = catalogResponse.error?.let { ApiError(message = it) }
                )
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get reward catalog: ${response.status}"))
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

    override suspend fun claimReward(request: RewardClaimRequest): Result<ApiResponse<RewardClaimResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Rewards.CLAIM) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                // The API returns RewardClaimResponse directly, not wrapped in ApiResponse
                val claimResponse: RewardClaimResponse = response.body()
                // Wrap it in ApiResponse for consistency - the data field contains RewardClaimData
                val apiResponse = ApiResponse(
                    success = claimResponse.success,
                    status = claimResponse.status,
                    data = claimResponse,
                    message = claimResponse.message,
                    timestamp = claimResponse.timestamp,
                    error = claimResponse.error?.let { ApiError(message = it) }
                )
                Result.success(apiResponse)
            } else {
                // Try to parse error response - extract only message
                val errorMessage = try {
                    val errorResponse: ApiResponse<*> = response.body()
                    errorResponse.error?.message ?: errorResponse.message ?: "Failed to claim reward"
                } catch (e: Exception) {
                    val errorBody = response.bodyAsText()
                    // Try to extract message from JSON string
                    errorBody?.let { body ->
                        try {
                            val messageMatch = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(body)
                            messageMatch?.groupValues?.get(1) ?: "Failed to claim reward"
                        } catch (e: Exception) {
                            "Failed to claim reward"
                        }
                    } ?: "Failed to claim reward"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: ClientRequestException) {
            // Extract only the error message, not the full response
            val errorMessage = try {
                val errorResponse: ApiResponse<*> = e.response.body()
                errorResponse.error?.message ?: errorResponse.message ?: "Failed to claim reward"
            } catch (parseError: Exception) {
                val errorBody = e.response.bodyAsText()
                // Try to extract message from JSON string if parsing failed
                errorBody?.let { body ->
                    try {
                        val messageMatch = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(body)
                        messageMatch?.groupValues?.get(1) ?: "Failed to claim reward"
                    } catch (e: Exception) {
                        "Failed to claim reward"
                    }
                } ?: "Failed to claim reward"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: ServerResponseException) {
            // Extract only the error message, not the full response
            val errorMessage = try {
                val errorResponse: ApiResponse<*> = e.response.body()
                errorResponse.error?.message ?: errorResponse.message ?: "Failed to claim reward"
            } catch (parseError: Exception) {
                val errorBody = e.response.bodyAsText()
                // Try to extract message from JSON string if parsing failed
                errorBody?.let { body ->
                    try {
                        val messageMatch = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(body)
                        messageMatch?.groupValues?.get(1) ?: "Failed to claim reward"
                    } catch (e: Exception) {
                        "Failed to claim reward"
                    }
                } ?: "Failed to claim reward"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRewardTransactions(): Result<ApiResponse<RewardTransactionResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Rewards.TRANSACTIONS)

            if (response.status.isSuccess()) {
                // The API returns RewardTransactionResponse directly, not wrapped in ApiResponse
                val transactionResponse: RewardTransactionResponse = response.body()
                // Wrap it in ApiResponse for consistency
                val apiResponse = ApiResponse(
                    success = transactionResponse.success,
                    status = transactionResponse.status,
                    data = transactionResponse,
                    message = transactionResponse.message,
                    timestamp = transactionResponse.timestamp,
                    error = transactionResponse.error?.let { ApiError(message = it) }
                )
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get reward transactions: ${response.status}"))
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

