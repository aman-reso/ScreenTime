package com.app.screentime.core.network.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.AppConfig
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ConfigService using Ktor
 */
@Singleton
class ConfigServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : ConfigService {

    private val client = networkClient.httpClient

    override suspend fun getFeatures(
        country: String?,
        appVersion: String?,
        language: String?
    ): Result<ApiResponse<AppConfig>> {
        return try {
            val response: HttpResponse = client.get(ApiEndpoints.Config.GET_FEATURES) {
                url {
                    country?.let { parameters.append("country", it) }
                    appVersion?.let { parameters.append("appVersion", it) }
                    language?.let { parameters.append("language", it) }
                }
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<AppConfig> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get features: ${response.status}"))
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

