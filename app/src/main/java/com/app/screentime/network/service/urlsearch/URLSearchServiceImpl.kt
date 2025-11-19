package com.app.screentime.network.service.urlsearch

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
 * Implementation of URLSearchService using Ktor
 */
@Singleton
class URLSearchServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : URLSearchService {

    private val httpClient = networkClient.httpClient

    override suspend fun submitURLSearch(request: URLSearchSubmissionRequest): Result<ApiResponse<URLSearchSubmissionResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.URLSearch.SUBMIT_URL_SEARCH) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<URLSearchSubmissionResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to submit URL search: ${response.status}"))
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

    override suspend fun batchSubmitURLSearch(request: BatchURLSearchSubmissionRequest): Result<ApiResponse<URLSearchSubmissionResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.URLSearch.BATCH_URL_SEARCH) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<URLSearchSubmissionResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to batch submit URL search: ${response.status}"))
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

    override suspend fun getURLHistory(request: URLSearchHistoryRequest): Result<ApiResponse<URLSearchHistoryResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.URLSearch.GET_URL_HISTORY) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<URLSearchHistoryResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get URL history: ${response.status}"))
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

