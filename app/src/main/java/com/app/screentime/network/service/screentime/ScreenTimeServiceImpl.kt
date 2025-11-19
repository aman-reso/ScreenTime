package com.app.screentime.network.service.screentime

import com.app.screentime.network.ApiEndpoints
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.model.*
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
}

