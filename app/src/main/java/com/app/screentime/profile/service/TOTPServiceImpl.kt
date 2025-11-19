package com.app.screentime.profile.service

import com.app.screentime.network.ApiEndpoints
import com.app.screentime.network.NetworkClient
import com.app.screentime.network.model.ApiResponse
import com.app.screentime.network.model.TOTPVerifyResponse
import com.app.screentime.network.model.UsernameTOTPVerifyRequest
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of TOTPService using Ktor
 */
@Singleton
class TOTPServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : TOTPService {

    private val httpClient = networkClient.httpClient


    override suspend fun verifyTOTPByUsername(username: String, request: UsernameTOTPVerifyRequest): Result<ApiResponse<TOTPVerifyResponse>> {
        return try {
            val endpoint = ApiEndpoints.TOTP.VERIFY_BY_USERNAME.replace("{username}", username)
            val response: HttpResponse = httpClient.post(endpoint) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<TOTPVerifyResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to verify TOTP: ${response.status}"))
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

