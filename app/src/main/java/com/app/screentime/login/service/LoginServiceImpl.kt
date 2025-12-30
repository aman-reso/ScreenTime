package com.app.screentime.login.service

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.core.network.model.DeviceRegistrationRequest
import com.app.screentime.core.network.model.DeviceRegistrationResponse
import com.app.screentime.core.network.utils.DeviceInfoUtils
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of LoginService using Ktor
 */
@Singleton
class LoginServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : LoginService {

    private val httpClient = networkClient.httpClient

    override suspend fun registerDevice(
        deviceInfo: DeviceInfoUtils.DeviceInfo,
        firebaseToken: String?
    ): Result<ApiResponse<DeviceRegistrationResponse>> {
        return try {
            val request = DeviceRegistrationRequest(
                deviceInfo = deviceInfo,
                firebaseToken = firebaseToken
            )

            val apiResponse: ApiResponse<DeviceRegistrationResponse> =
                httpClient.post(ApiEndpoints.Registration.REGISTER_DEVICE) {
                    setBody(request)
                }.body()

            Result.success(apiResponse)
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

