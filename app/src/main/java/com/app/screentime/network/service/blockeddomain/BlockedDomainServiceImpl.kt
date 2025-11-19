package com.app.screentime.network.service.blockeddomain

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
 * Implementation of BlockedDomainService using Ktor
 */
@Singleton
class BlockedDomainServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : BlockedDomainService {

    private val httpClient = networkClient.httpClient

    override suspend fun getBlockedDomains(): Result<ApiResponse<GetBlockedDomainsResponse>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.BlockedDomain.GET_BLOCKED_DOMAINS)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<GetBlockedDomainsResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get blocked domains: ${response.status}"))
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

    override suspend fun getDomainGroups(): Result<ApiResponse<List<BlockedDomainGroup>>> {
        return try {
            val response: HttpResponse = httpClient.get(ApiEndpoints.BlockedDomain.GET_DOMAIN_GROUPS)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<List<BlockedDomainGroup>> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to get domain groups: ${response.status}"))
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

    override suspend fun submitBlockedDomain(request: SubmitBlockedDomainRequest): Result<ApiResponse<SubmitBlockedDomainResponse>> {
        return try {
            val response: HttpResponse = httpClient.post(ApiEndpoints.BlockedDomain.SUBMIT_BLOCKED_DOMAIN) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<SubmitBlockedDomainResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to submit blocked domain: ${response.status}"))
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

    override suspend fun updateBlockedDomain(id: Int, request: SubmitBlockedDomainRequest): Result<ApiResponse<SubmitBlockedDomainResponse>> {
        return try {
            val endpoint = ApiEndpoints.BlockedDomain.UPDATE_BLOCKED_DOMAIN.replace("{id}", id.toString())
            val response: HttpResponse = httpClient.put(endpoint) {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<SubmitBlockedDomainResponse> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to update blocked domain: ${response.status}"))
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

    override suspend fun deleteBlockedDomain(id: Int): Result<ApiResponse<Unit>> {
        return try {
            val endpoint = ApiEndpoints.BlockedDomain.DELETE_BLOCKED_DOMAIN.replace("{id}", id.toString())
            val response: HttpResponse = httpClient.delete(endpoint)

            if (response.status.isSuccess()) {
                val apiResponse: ApiResponse<Unit> = response.body()
                Result.success(apiResponse)
            } else {
                Result.failure(Exception("Failed to delete blocked domain: ${response.status}"))
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

