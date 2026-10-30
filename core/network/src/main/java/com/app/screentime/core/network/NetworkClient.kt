package com.app.screentime.core.network

import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.app.screentime.core.network.preferences.PreferencesManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
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
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
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
    private val firebaseConfigManager: com.app.screentime.core.network.config.FirebaseConfigManager,
    private val playIntegrityManager: com.app.screentime.core.network.security.PlayIntegrityManager
) {
    companion object {
        @Volatile
        private var serverTimeOffsetMs: Long = 0L

        /**
         * Returns server-synchronized timestamp in milliseconds.
         * Eliminates phone clock drift and desync.
         */
        fun getSyncedTimestamp(): Long {
            return System.currentTimeMillis() + serverTimeOffsetMs
        }

        fun updateServerTime(serverEpochMs: Long) {
            val drift = serverEpochMs - System.currentTimeMillis()
            serverTimeOffsetMs = drift
            if (kotlin.math.abs(drift) > 5000) {
                Log.i("NetworkClient", "🕒 Clock synchronized with server. Offset: ${drift}ms")
            }
        }

        private fun parseHttpDate(dateStr: String): Long {
            return try {
                val sdf = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.US)
                sdf.timeZone = java.util.TimeZone.getTimeZone("GMT")
                sdf.parse(dateStr)?.time ?: 0L
            } catch (_: Exception) {
                0L
            }
        }
    }

    private val appVersion: String by lazy {
        try {
            val packageInfo = context.packageManager?.getPackageInfo(context.packageName, 0)
            packageInfo?.versionName ?: "unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "unknown"
        }
    }

    private val appVersionCode: Long by lazy {
        try {
            val packageInfo = context.packageManager?.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo?.longVersionCode ?: 1L
            } else {
                @Suppress("DEPRECATION")
                packageInfo?.versionCode?.toLong() ?: 1L
            }
        } catch (_: Exception) {
            1L
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

    val baseUrl: String
        get() = ApiEndpoints.getBaseUrl()

    val httpClient: HttpClient by lazy {
        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("KtorApi", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                url(baseUrl)
                header("Content-Type", "application/json")
                header("X-App-Version", appVersion)
                header("X-App-Version-Code", appVersionCode.toString())
                header("X-Platform", "android")
                header("X-App-Language", getLanguageCode())
                header("X-Country-Code", countryCode)
                header("apikey", firebaseConfigManager.getApiKey())
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }

            HttpResponseValidator {
                validateResponse { response ->
                    // Synchronize client clock with server clock from HTTP Date header
                    val dateHeader = response.headers[HttpHeaders.Date]
                    if (!dateHeader.isNullOrBlank()) {
                        val serverTimeMs = parseHttpDate(dateHeader)
                        if (serverTimeMs > 0) {
                            updateServerTime(serverTimeMs)
                        }
                    }

                    if (response.status == HttpStatusCode.Unauthorized || response.status.value == 401) {
                        Log.w("NetworkClient", "🔴 401 Unauthorized received. Logging out and redirecting to login screen.")
                        NetworkAuthBridge.unauthorizedHandler?.onUnauthorized()
                        throw UnauthorizedException()
                    }
                    if (response.status == HttpStatusCode.Forbidden || response.status.value == 403) {
                        Log.w("NetworkClient", "⚠️ 403 Forbidden received. Invalidating Play Integrity token cache.")
                        playIntegrityManager.invalidateCache()
                    }
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }

        client.requestPipeline.intercept(HttpRequestPipeline.State) {
            context.headers.remove("X-App-Version")
            context.header("X-App-Version", appVersion)
            context.headers.remove("X-App-Version-Code")
            context.header("X-App-Version-Code", appVersionCode.toString())
            context.headers.remove("X-Platform")
            context.header("X-Platform", "android")
            context.headers.remove("X-Country-Code")
            context.header("X-Country-Code", countryCode)
            context.headers.remove("X-App-Language")
            context.header("X-App-Language", getLanguageCode())

            val apiKey = firebaseConfigManager.ensureApiKey()
            if (apiKey.isNotBlank()) {
                context.headers.remove("apikey")
                context.header("apikey", apiKey)
            }
            if (!context.headers.contains(HttpHeaders.Authorization)) {
                val token = preferencesManager.getToken()
                if (!token.isNullOrBlank()) {
                    context.header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            if (!context.headers.contains("X-User-Id")) {
                val userId = preferencesManager.getUserId()
                if (!userId.isNullOrBlank()) {
                    context.header("X-User-Id", userId)
                }
            }

            val timestamp = getSyncedTimestamp()
            val securityToken = com.app.screentime.core.network.security.NativeSecurityVault.getToken(timestamp)
            if (securityToken.isNotBlank()) {
                context.header("X-App-Token", securityToken)
                context.header("X-App-Timestamp", timestamp.toString())
            }

            // Google Play Integrity App Attestation Token (Release mandatory, debug omitted)
            if (!com.app.screentime.core.network.BuildConfig.DEBUG) {
                var attestToken = playIntegrityManager.getCachedToken()
                if (attestToken.isNullOrBlank()) {
                    try {
                        attestToken = playIntegrityManager.fetchAttestationToken()
                    } catch (e: Throwable) {
                        Log.e("NetworkClient", "Mandatory Play Integrity attestation failed: ${e.message}")
                    }
                }
                if (!attestToken.isNullOrBlank()) {
                    context.header("X-App-Attestation-Token", attestToken)
                }
            }
        }

        client
    }

}

class UnauthorizedException : Exception("Unauthorized")
