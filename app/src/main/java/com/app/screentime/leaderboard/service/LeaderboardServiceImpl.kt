package com.app.screentime.leaderboard.service

import com.app.screentime.network.ApiEndpoints
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.LeaderboardResponse
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
@Singleton
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

    override suspend fun getMonthlyLeaderboard(): Result<ApiResponse<LeaderboardResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Leaderboard.MONTHLY)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<LeaderboardResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get monthly leaderboard: ${response.status}"))
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

