package com.app.screentime.network.service.screentime

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.*
import com.app.screentime.network.model.AppStatsGetResponse
import com.app.screentime.network.model.AppStatsRequest
import com.app.screentime.network.model.AppStatsResponse
import com.app.screentime.network.model.BatchUsageEventsRequest
import com.app.screentime.network.model.UsageLastSyncResponse
import com.app.screentime.network.model.UsageStatsResponse
import com.app.screentime.network.model.SummaryScreenTimeRequest
import com.app.screentime.network.model.SummaryScreenTimeResponseData
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ScreenTimeService using Ktor
 */
@Singleton
class ScreenTimeServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : ScreenTimeService {

    private val httpClient = networkClient.httpClient

    override suspend fun syncBatchUsageEvents(request: BatchUsageEventsRequest): Result<ApiResponse<Unit>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.AppUsage.BATCH_EVENTS) {
                setBody(request)
            }
            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to sync batch usage events: ${response.status}"))
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

    override suspend fun getDailyUsageStats(
        date: String,
        targetUserId: String
    ): Result<ApiResponse<UsageStatsResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.AppUsage.GET_DAILY_STATS) {
                parameter("date", date)
                parameter("targetUserId", targetUserId)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UsageStatsResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get daily usage stats: ${response.status}"))
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

    override suspend fun getUsageLastSyncTime(): Result<ApiResponse<UsageLastSyncResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.AppUsage.LAST_SYNC)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UsageLastSyncResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get usage last sync time: ${response.status}"))
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

    override suspend fun getSummaryScreenTime(request: SummaryScreenTimeRequest): Result<ApiResponse<SummaryScreenTimeResponseData>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.AppUsage.SUMMARY_SCREENTIME) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<SummaryScreenTimeResponseData> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get summary screen time: ${response.status}"))
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

    override suspend fun submitAppStats(request: AppStatsRequest): Result<ApiResponse<AppStatsResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.AppUsage.APP_STATS) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<AppStatsResponse> = response.body()
                Result.success(apiResponse)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Failed to submit app stats: ${response.status}, $errorBody"))
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

    override suspend fun getAppStats(date: String, targetUserName: String): Result<ApiResponse<AppStatsGetResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.AppUsage.APP_STATS) {
                parameter("date", date)
                parameter("targetUserName", targetUserName)
            }

            if (response.status.isSuccess()) {
                // API returns ApiResponse<AppStatsGetResponse>
                val apiResponse: ApiResponse<AppStatsGetResponse> = response.body()
                Result.success(apiResponse)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Failed to get app stats: ${response.status}, $errorBody"))
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

