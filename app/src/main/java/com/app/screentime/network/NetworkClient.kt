package com.app.screentime.network

import android.content.Context
import android.util.Log
import com.app.screentime.preferences.PreferencesManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Singleton
class NetworkClient(
    context: Context
) {
    private val preferencesManager: PreferencesManager = PreferencesManager(context)

    val httpClient: HttpClient = HttpClient(Android) {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        // ✅ Logging for requests/responses
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("Ktor", message)
                }
            }
            level = LogLevel.ALL
        }

        // ✅ Default headers and base URL (fetched from Remote Config)
        install(DefaultRequest) {
            url(ApiEndpoints.getBaseUrl())
            header("Content-Type", "application/json")
            header("ngrok-skip-browser-warning", "true")
            val token = preferencesManager.getUserId()
            if (!token.isNullOrBlank()) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }


        // ✅ Auth plugin: Bearer token + custom userId header
        install(Auth) {
            bearer {
                // Optional: handle token refresh automatically
                sendWithoutRequest { request ->
                    // Decide when to send token — usually all authenticated endpoints
                    !request.url.encodedPath.contains("/auth/login")
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 30_000
        }

        // ✅ Retry failed requests
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 1)
            exponentialDelay()
        }
    }
}

