package com.app.screentime.network.service.blockeddomain

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.model.*
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

class BlockedDomainServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : BlockedDomainService {

    private val httpClient = networkClient.httpClient


}

