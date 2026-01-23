package com.app.screentime.core.network.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.WallpaperApiResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of WallpaperService using Ktor
 */

class WallpaperServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : WallpaperService {

    private val client = networkClient.httpClient

    override suspend fun getWallpapers(): Result<ApiResponse<List<WallpaperApiResponse>>> {
        return try {
            val response: HttpResponse = client.get(ApiEndpoints.WALLPAPER.GET_WALLPAPERS)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<WallpaperApiResponse>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get wallpapers: ${response.status}"))
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
