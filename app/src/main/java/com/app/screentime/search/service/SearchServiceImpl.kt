package com.app.screentime.search.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.UserSearchResult
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of SearchService using Ktor
 */
class SearchServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : SearchService {

    private val httpClient = networkClient.httpClient

    override suspend fun searchUsers(query: String): Result<ApiResponse<List<UserSearchResult>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Search.SEARCH_USERS) {
                parameter("q", query)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<UserSearchResult>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to search users: ${response.status}"))
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

