package com.app.screentime.core.network

import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.app.screentime.core.network.preferences.PreferencesManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkClient @Inject constructor(
    private val context: Context,
    private val preferencesManager: PreferencesManager,
) {
    private val appVersion: String by lazy {
        try {
            val packageInfo = context.packageManager?.getPackageInfo(context.packageName, 0)
            packageInfo?.versionName ?: "unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "unknown"
        }
    }

    private val countryCode: String by lazy {
        getCountryCode(context)
    }

    /**
     * Get current language code from AppCompatDelegate
     * Returns language tag in format "language-rCountry" (e.g., "pt-rBR") or just "language" (e.g., "en")
     */
    private fun getLanguageCode(): String {
        val locale = AppCompatDelegate.getApplicationLocales().get(0)
        return locale?.let {
            val language = it.language
            val country = it.country
            if (country.isNotEmpty()) {
                // Format as "language-rCountry" to match LanguageUtils format (e.g., "pt-rBR")
                "$language-r${country.uppercase()}"
            } else {
                language
            }
        } ?: "en"
    }

    /**
     * Get country code from device
     * Tries TelephonyManager first, falls back to Locale
     */
    private fun getCountryCode(context: Context): String {
        return try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager?
            val simCountryCode = telephonyManager?.simCountryIso?.uppercase()
            val networkCountryCode = telephonyManager?.networkCountryIso?.uppercase()
            val countryCode = simCountryCode ?: networkCountryCode
            if (!countryCode.isNullOrBlank()) {
                countryCode
            } else {
                Locale.getDefault().country.uppercase().takeIf { it.isNotBlank() } ?: "IN"
            }
        } catch (e: Exception) {
            Locale.getDefault().country.uppercase().takeIf { it.isNotBlank() } ?: "IN"
        }
    }

    val httpClient: HttpClient = HttpClient(Android) {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }

        // Only enable HTTP logging in DEBUG builds
        if (BuildConfig.DEBUG) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }
        }

        install(DefaultRequest) {
            url(ApiEndpoints.getBaseUrl())
            header("Content-Type", "application/json")
            header("ngrok-skip-browser-warning", "true")
            header("X-App-Version", appVersion)
            header("X-App-Language", getLanguageCode())
            header("X-Country-Code", countryCode)

            val token = preferencesManager.getToken()
            if (!token.isNullOrBlank()) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (response.status == HttpStatusCode.Unauthorized) {
                    NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
                    throw UnauthorizedException()
                }
            }
        }

        install(Auth) {
            bearer {
                sendWithoutRequest { request ->
                    !request.url.encodedPath.contains("/auth/login")
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
    }

}

class UnauthorizedException : Exception("Unauthorized")
