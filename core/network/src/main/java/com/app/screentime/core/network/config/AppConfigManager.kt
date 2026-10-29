package com.app.screentime.core.network.config

import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.dto.AppConfigData
import com.app.screentime.core.network.dto.AppConfigResponse
import com.app.screentime.core.network.preferences.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

/**
 * Centralized App Configuration Manager backed by the custom Backend Config API.
 * Completely replaces Firebase Remote Config.
 * Sends App Version, Version Code, Platform, and Country in all requests.
 */
@Singleton
class AppConfigManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val TAG = "AppConfigManager"
        const val DEFAULT_API_KEY = ""
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val initialFetchDeferred = CompletableDeferred<String>()

    var onConfigUpdated: ((AppConfigManager) -> Unit)? = null

    val appVersion: String by lazy {
        try {
            val packageInfo = context.packageManager?.getPackageInfo(context.packageName, 0)
            packageInfo?.versionName ?: "unknown"
        } catch (_: PackageManager.NameNotFoundException) {
            "unknown"
        }
    }

    val appVersionCode: Long by lazy {
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

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    @Volatile
    private var cachedApiKey: String = DEFAULT_API_KEY

    @Volatile
    private var cachedAdsEnabled: Boolean = true

    @Volatile
    private var cachedSwipeAdEnabled: Boolean = true

    @Volatile
    private var cachedMessageAdEnabled: Boolean = true

    @Volatile
    private var cachedSwipeAdInterval: Int = 10

    @Volatile
    private var cachedSwipeAdMinInterval: Int = 10

    @Volatile
    private var cachedSwipeAdMaxInterval: Int = 10

    @Volatile
    private var cachedMessageAdInterval: Int = 5

    @Volatile
    private var cachedMinVersion: String? = null

    @Volatile
    private var cachedLatestVersion: String? = null

    @Volatile
    private var cachedForceUpdate: Boolean = false

    @Volatile
    private var cachedUpdateUrl: String? = null

    init {
        val savedKey = preferencesManager.getApiKey()
        if (savedKey.isNotBlank()) {
            cachedApiKey = savedKey
        }
        cachedAdsEnabled = preferencesManager.getAdsEnabled()
        cachedSwipeAdEnabled = preferencesManager.getSwipeAdEnabled()
        cachedMessageAdEnabled = preferencesManager.getMessageAdEnabled()
        cachedSwipeAdInterval = preferencesManager.getSwipeAdInterval()
        cachedSwipeAdMinInterval = preferencesManager.getSwipeAdMinInterval()
        cachedSwipeAdMaxInterval = preferencesManager.getSwipeAdMaxInterval()
        cachedMessageAdInterval = preferencesManager.getMessageAdInterval()
        cachedMinVersion = preferencesManager.getMinVersion()
        cachedLatestVersion = preferencesManager.getLatestVersion()
        cachedForceUpdate = preferencesManager.getForceUpdate()
        cachedUpdateUrl = preferencesManager.getUpdateUrl()
    }

    private fun getLanguageCode(): String {
        val locale = AppCompatDelegate.getApplicationLocales().get(0)
        return locale?.let {
            val language = it.language
            val country = it.country
            if (country.isNotEmpty()) {
                "$language-r${country.uppercase()}"
            } else {
                language
            }
        } ?: "en"
    }

    private fun getCountryCode(): String {
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
        } catch (_: Exception) {
            Locale.getDefault().country.uppercase().takeIf { it.isNotBlank() } ?: "IN"
        }
    }

    /**
     * Synchronously returns the currently cached API key.
     */
    fun getApiKey(): String {
        return cachedApiKey.ifBlank { DEFAULT_API_KEY }
    }

    /**
     * Asynchronously triggers fetch from backend in background IO scope.
     */
    fun fetchConfigAsync() {
        scope.launch {
            fetchConfig()
        }
    }

    /**
     * Fetches configuration from backend API (GET /api/config).
     * Sends X-App-Version, X-App-Version-Code, X-Platform, and other diagnostic headers.
     */
    suspend fun fetchConfig(): AppConfigData? = withContext(Dispatchers.IO) {
        val url = "${ApiEndpoints.getBaseUrl()}${ApiEndpoints.Config.CONFIG}"
        Log.i(TAG, "Fetching app config from API: $url (appVersion=$appVersion, versionCode=$appVersionCode)")

        try {
            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-App-Version", appVersion)
                .addHeader("X-App-Version-Code", appVersionCode.toString())
                .addHeader("X-Platform", "android")
                .addHeader("X-Country-Code", getCountryCode())
                .addHeader("X-App-Language", getLanguageCode())
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w(TAG, "Config API returned HTTP ${response.code}: ${response.message}")
                if (!initialFetchDeferred.isCompleted) {
                    initialFetchDeferred.complete(cachedApiKey)
                }
                return@withContext null
            }

            val body = response.body?.string()
            if (body.isNullOrBlank()) {
                Log.w(TAG, "Config API returned empty body")
                if (!initialFetchDeferred.isCompleted) {
                    initialFetchDeferred.complete(cachedApiKey)
                }
                return@withContext null
            }

            Log.d(TAG, "Config API raw response: $body")
            preferencesManager.setAppConfigJson(body)
            val configResponse = json.decodeFromString<AppConfigResponse>(body)
            val resolved = configResponse.resolveConfig()
            applyConfig(resolved)

            if (!initialFetchDeferred.isCompleted) {
                initialFetchDeferred.complete(cachedApiKey)
            }
            resolved
        } catch (e: Throwable) {
            Log.e(TAG, "Error fetching app config from API: ${e.message}", e)
            if (!initialFetchDeferred.isCompleted) {
                initialFetchDeferred.complete(cachedApiKey)
            }
            null
        }
    }

    private fun applyConfig(config: AppConfigData) {
        config.apikey?.takeIf { it.isNotBlank() }?.let {
            cachedApiKey = it
            preferencesManager.setApiKey(it)
        }

        config.adsEnabled?.let {
            cachedAdsEnabled = it
            preferencesManager.setAdsEnabled(it)
        }

        config.swipeAdEnabled?.let {
            cachedSwipeAdEnabled = it
            preferencesManager.setSwipeAdEnabled(it)
        }

        config.messageAdEnabled?.let {
            cachedMessageAdEnabled = it
            preferencesManager.setMessageAdEnabled(it)
        }

        config.swipeAdMinInterval?.takeIf { it > 0 }?.let {
            cachedSwipeAdMinInterval = it
            preferencesManager.setSwipeAdMinInterval(it)
        }

        config.swipeAdMaxInterval?.takeIf { it > 0 }?.let {
            cachedSwipeAdMaxInterval = it
            preferencesManager.setSwipeAdMaxInterval(it)
        }

        config.swipeAdInterval?.takeIf { it > 0 }?.let {
            cachedSwipeAdInterval = it
            preferencesManager.setSwipeAdInterval(it)
        }

        config.messageAdInterval?.takeIf { it > 0 }?.let {
            cachedMessageAdInterval = it
            preferencesManager.setMessageAdInterval(it)
        }

        cachedMinVersion = config.minVersion
        preferencesManager.setMinVersion(config.minVersion)

        cachedLatestVersion = config.latestVersion
        preferencesManager.setLatestVersion(config.latestVersion)

        cachedForceUpdate = config.forceUpdate ?: false
        preferencesManager.setForceUpdate(cachedForceUpdate)

        cachedUpdateUrl = config.updateUrl
        preferencesManager.setUpdateUrl(config.updateUrl)

        Log.i(
            TAG,
            "✅ App Config applied from API: adsEnabled=$cachedAdsEnabled, swipeEnabled=$cachedSwipeAdEnabled, swipeIntervals=[$cachedSwipeAdMinInterval..$cachedSwipeAdMaxInterval], msgEnabled=$cachedMessageAdEnabled, msgInterval=$cachedMessageAdInterval"
        )
        onConfigUpdated?.invoke(this)
    }

    /**
     * Ensures that on cold app launch, if initial fetch is running, we wait briefly (up to 2s)
     * so that the very first API request has the live config key.
     */
    suspend fun ensureApiKey(): String {
        if (initialFetchDeferred.isCompleted) {
            return cachedApiKey
        }
        return try {
            withTimeoutOrNull(2000L.milliseconds) {
                initialFetchDeferred.await()
            } ?: cachedApiKey
        } catch (_: Throwable) {
            cachedApiKey
        }
    }

    fun areAdsEnabled(): Boolean = cachedAdsEnabled
    fun isSwipeAdEnabled(): Boolean = cachedAdsEnabled && cachedSwipeAdEnabled
    fun isMessageAdEnabled(): Boolean = cachedAdsEnabled && cachedMessageAdEnabled
    fun getSwipeAdInterval(): Int = cachedSwipeAdInterval
    fun getSwipeAdMinInterval(): Int = cachedSwipeAdMinInterval
    fun getSwipeAdMaxInterval(): Int = cachedSwipeAdMaxInterval
    fun getMessageAdInterval(): Int = cachedMessageAdInterval
    fun getMinVersion(): String? = cachedMinVersion
    fun getLatestVersion(): String? = cachedLatestVersion
    fun isForceUpdate(): Boolean = cachedForceUpdate
    fun getUpdateUrl(): String? = cachedUpdateUrl
}
