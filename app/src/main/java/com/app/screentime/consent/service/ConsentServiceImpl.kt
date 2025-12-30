package com.app.screentime.consent.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.*
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
 * Implementation of ConsentService using Ktor
 */
@Singleton
class ConsentServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : ConsentService {

    private val httpClient = networkClient.httpClient

    override suspend fun getConsents(): Result<ApiResponse<List<ApiConsentItem>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Consent.GET_CONSENTS)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<ApiConsentItem>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get consents: ${response.status}"))
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

    override suspend fun submitConsents(request: ConsentSubmissionRequest): Result<ApiResponse<List<ConsentSubmissionResponseItem>>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Consent.SUBMIT) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<ConsentSubmissionResponseItem>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to submit consents: ${response.status}"))
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

    override suspend fun submitConsent(consentRequest: ConsentRequest): Result<ConsentResponse> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.Consent.SUBMIT) {
                setBody(consentRequest)
            }

            if (response.status.isSuccess()) {
                val consentResponse: ConsentResponse = response.body()
                Result.success(consentResponse)
            } else {
                Result.failure(Exception("Failed to submit consent: ${response.status}"))
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

    override suspend fun getConsentStatus(username: String): Result<ConsentResponse> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Consent.STATUS) {
                parameter("username", username)
            }

            if (response.status.isSuccess()) {
                val consentResponse: ConsentResponse = response.body()
                Result.success(consentResponse)
            } else {
                Result.failure(Exception("Failed to get consent status: ${response.status}"))
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

