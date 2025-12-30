package com.app.screentime.network.service.urlsearch

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
 * Implementation of URLSearchService using Ktor
 */
@Singleton
class URLSearchServiceImpl @Inject constructor(
    private val networkClient: NetworkClient
) : URLSearchService {

    private val httpClient = networkClient.httpClient


}

