package com.app.screentime.location.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.LocationData
import com.app.screentime.network.model.LocationResponse
import com.app.screentime.network.model.LocationSyncRequest
import com.app.screentime.network.model.LocationSyncResponse
import com.app.screentime.network.model.ShareLocationRequest
import com.app.screentime.network.model.UserLastLocationResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationServiceImpl @Inject constructor(
    networkClient: NetworkClient
) : LocationService {

    private val httpClient = networkClient.httpClient


    override suspend fun syncLocation(request: LocationSyncRequest): Result<ApiResponse<LocationSyncResponse>> {
        return try {
            val response = httpClient.post(ApiEndpoints.Location.SYNC) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<LocationSyncResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to sync location: ${response.status}"))
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

    override suspend fun getUserLastLocation(username: String): Result<ApiResponse<UserLastLocationResponse>> {
        return try {
            val endpoint = ApiEndpoints.Location.GET_USER_LAST_LOCATION.replace("{username}", username)
            val response = httpClient.get(endpoint) {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<UserLastLocationResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get user last location: ${response.status}"))
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

