package com.app.screentime.network.service.focus

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.*
import com.app.screentime.network.model.FocusDurationHistoryRequest
import com.app.screentime.network.model.FocusDurationHistoryResponse
import com.app.screentime.network.model.FocusDurationStatsResponse
import com.app.screentime.network.model.FocusDurationSubmissionRequest
import com.app.screentime.network.model.FocusDurationSubmissionResponse
import com.app.screentime.network.model.FocusModeStatsResponse
import com.app.screentime.network.model.FocusModeStatsSyncRequest
import com.app.screentime.network.model.FocusModeStatsSyncResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of FocusService using Ktor
 */
@Singleton
class FocusServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : FocusService {

    private val httpClient = networkClient.httpClient

    override suspend fun submitFocusDuration(request: FocusDurationSubmissionRequest): Result<ApiResponse<FocusDurationSubmissionResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Focus.SUBMIT_FOCUS) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<FocusDurationSubmissionResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to submit focus duration: ${response.status}"))
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

    override suspend fun getFocusHistory(request: FocusDurationHistoryRequest): Result<ApiResponse<FocusDurationHistoryResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Focus.GET_FOCUS_HISTORY) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<FocusDurationHistoryResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get focus history: ${response.status}"))
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

    override suspend fun getFocusStats(): Result<ApiResponse<FocusDurationStatsResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Focus.GET_FOCUS_STATS)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<FocusDurationStatsResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get focus stats: ${response.status}"))
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

    override suspend fun syncFocusModeStats(request: FocusModeStatsSyncRequest): Result<ApiResponse<FocusModeStatsSyncResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Focus.SYNC_FOCUS_MODE_STATS) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<FocusModeStatsSyncResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to sync focus mode stats: ${response.status}"))
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

    override suspend fun getFocusModeStats(startTimeMs: Long, endTimeMs: Long): Result<ApiResponse<FocusModeStatsResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Focus.GET_FOCUS_MODE_STATS) {
                parameter("startTimeMs", startTimeMs)
                parameter("endTimeMs", endTimeMs)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<FocusModeStatsResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get focus mode stats: ${response.status}"))
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

