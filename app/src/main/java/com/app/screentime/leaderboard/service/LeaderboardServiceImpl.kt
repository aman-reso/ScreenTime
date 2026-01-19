package com.app.screentime.leaderboard.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.LeaderboardResponse
import com.app.screentime.network.model.LeaderboardStatsUpdateRequest
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of LeaderboardService
 */
class LeaderboardServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : LeaderboardService {

    private val httpClient = networkClient.httpClient

    override suspend fun getDailyLeaderboard(date: String?): Result<ApiResponse<LeaderboardResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Leaderboard.DAILY) {
                date?.let { parameter("date", it) }
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<LeaderboardResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get daily leaderboard: ${response.status}"))
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

    override suspend fun getWeeklyLeaderboard(): Result<ApiResponse<LeaderboardResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Leaderboard.WEEKLY)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<LeaderboardResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get weekly leaderboard: ${response.status}"))
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
    
    override suspend fun updateStats(request: LeaderboardStatsUpdateRequest): Result<ApiResponse<Unit>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Leaderboard.UPDATE_STATS) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update leaderboard stats: ${response.status}"))
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

