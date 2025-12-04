package com.app.screentime.challenge.service

import com.app.screentime.network.ApiEndpoints
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.ActiveChallengesResponse
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.ChallengeRankingsResponse
import com.app.screentime.network.model.JoinChallengeRequest
import com.app.screentime.network.model.JoinChallengeResponse
import com.app.screentime.network.model.UserChallengesResponse
import com.app.screentime.network.model.BatchChallengeStatsRequest
import com.app.screentime.network.model.BatchChallengeStatsResponse
import com.app.screentime.network.model.ChallengeStatsRequest
import com.app.screentime.network.model.ChallengeLastSyncResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeServiceImpl @Inject constructor(
    networkClient: NetworkClient
) : ChallengeService {

    private val httpClient = networkClient.httpClient

    override suspend fun getActiveChallenges(): Result<ApiResponse<ActiveChallengesResponse>> {
        return try {
            val response = httpClient.get(ApiEndpoints.Challenges.ACTIVE)
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to load active challenges: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserChallenges(): Result<ApiResponse<UserChallengesResponse>> {
        return try {
            val response = httpClient.get(ApiEndpoints.Challenges.USER)
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to load user challenges: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChallengeDetails(challengeId: String): Result<ApiResponse<ChallengeDetails>> {
        return try {
            val endpoint = ApiEndpoints.Challenges.DETAILS.replace("{challengeId}", challengeId)
            val response = httpClient.get(endpoint)
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to load challenge details: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun joinChallenge(challengeId: String): Result<ApiResponse<JoinChallengeResponse>> {
        return try {
            val request = JoinChallengeRequest(challengeId = challengeId)
            val response = httpClient.post(ApiEndpoints.Challenges.JOIN) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to join challenge: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChallengeRankings(challengeId: String): Result<ApiResponse<ChallengeRankingsResponse>> {
        return try {
            val endpoint = ApiEndpoints.Challenges.RANKINGS.replace("{challengeId}", challengeId)
            val response = httpClient.get(endpoint)
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to load challenge rankings: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitChallengeStats(request: ChallengeStatsRequest): Result<ApiResponse<Unit>> {
        return try {
            val response = httpClient.post(ApiEndpoints.Challenges.STATS) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to submit challenge stats: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitBatchChallengeStats(request: BatchChallengeStatsRequest): Result<ApiResponse<BatchChallengeStatsResponse>> {
        return try {
            val response = httpClient.post(ApiEndpoints.Challenges.STATS_BATCH) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to submit batch challenge stats: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChallengeLastSyncTime(challengeId: String): Result<ApiResponse<ChallengeLastSyncResponse>> {
        return try {
            val endpoint = ApiEndpoints.Challenges.LAST_SYNC.replace("{challengeId}", challengeId)
            val response = httpClient.get(endpoint)
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to get last sync time: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(
                IllegalStateException(
                    "Client error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: ServerResponseException) {
            Result.failure(
                IllegalStateException(
                    "Server error ${e.response.status}: ${e.response.bodyAsText()}",
                    e
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
