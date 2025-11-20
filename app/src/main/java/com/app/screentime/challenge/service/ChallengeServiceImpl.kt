package com.app.screentime.challenge.service

import com.app.screentime.network.ApiEndpoints
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.ChallengeOverviewResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
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

    override suspend fun getChallengeOverview(): Result<ApiResponse<ChallengeOverviewResponse>> {
        return try {
            val response = httpClient.get(ApiEndpoints.Challenges.APP_RANKINGS)
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(IllegalStateException("Failed to load challenges: ${response.status}"))
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

    override suspend fun joinChallenge(challengeId: String): Result<ApiResponse<Unit>> {
        return try {
            val endpoint = ApiEndpoints.Challenges.JOIN.replace("{challengeId}", challengeId)
            val response = httpClient.post(endpoint) {
                contentType(ContentType.Application.Json)
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
}
