package com.app.screentime.network.service.notification

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.NotificationSettings
import com.app.screentime.network.service.NotificationData
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of NotificationService using Ktor
 */
@Singleton
class NotificationServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : NotificationService {

    private val httpClient = networkClient.httpClient


    override suspend fun getNotificationHistory(userId: String): Result<ApiResponse<List<NotificationData>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.Notifications.HISTORY) {
                parameter("userId", userId)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<NotificationData>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get notification history: ${response.status}"))
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

    override suspend fun updateNotificationSettings(settings: NotificationSettings): Result<ApiResponse<NotificationSettings>> {
        return try {
            val response: HttpResponse = httpClient.put(ApiEndpoints.Notifications.SETTINGS) {
                setBody(settings)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<NotificationSettings> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update notification settings: ${response.status}"))
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

