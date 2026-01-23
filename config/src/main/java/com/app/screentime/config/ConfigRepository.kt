package com.app.screentime.config

import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import com.app.screentime.core.network.model.AppConfig
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.service.ConfigService
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val context: Context,
    private val configService: ConfigService,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val TAG = "ConfigRepository"
        private const val KEY_CONFIG = "app_config"
    }

    @Volatile
    private var cachedConfig: AppConfig? = null

    /**
     * Initialize config - first call API, if success override SharedPreferences
     * If API fails, check SharedPreferences, if no data then use default_config.json
     */
    suspend fun initialize() {
        val apiSuccess = fetchConfigFromApi()
        if (!apiSuccess) {
            val sharedPrefConfig = loadConfigFromSharedPreferences()
            if (sharedPrefConfig != null) {
                cachedConfig = sharedPrefConfig
                Log.d(TAG, "API failed, using config from SharedPreferences")
            } else {
                val defaultConfig = loadDefaultConfig()
                cachedConfig = defaultConfig
                Log.d(TAG, "API failed and no SharedPreferences data, using default config from raw resource")
            }
        }
    }

    private fun loadDefaultConfig(): AppConfig {
        return try {
            val resources = context.resources
            val packageName = context.packageName

            val resourceId = resources.getIdentifier(
                "default_config",
                "raw",
                packageName
            )

            if (resourceId == 0) {
                Log.w(TAG, "default_config resource not found, using default values")
                return AppConfig()
            }

            resources.openRawResource(resourceId).use { inputStream ->
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val config = Json.decodeFromString<AppConfig>(jsonString)
                Log.d(TAG, "Default config loaded successfully from raw resource")
                config
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading default config", e)
            AppConfig()
        }
    }


    /**
     * Get app version from package manager
     */
    private fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager?.getPackageInfo(context.packageName, 0)
            packageInfo?.versionName ?: "unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "unknown"
        }
    }

    /**
     * Get country code from device
     */
    private fun getCountryCode(): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        // Try to get country code from SIM first
        telephonyManager?.simCountryIso?.uppercase(Locale.getDefault())?.let {
            if (it.isNotEmpty()) return it
        }
        // Then try to get from network
        telephonyManager?.networkCountryIso?.uppercase(Locale.getDefault())?.let {
            if (it.isNotEmpty()) return it
        }
        // Fallback to device locale
        return Locale.getDefault().country.uppercase(Locale.getDefault()).ifEmpty { "US" }
    }

    /**
     * Fetch config from API and save to SharedPreferences if successful
     * If API fails, return false (will check SharedPreferences then default_config.json)
     */
    suspend fun fetchConfigFromApi(): Boolean {
        return try {
            // Use the new features API with query parameters
            val country = getCountryCode()
            val appVersion = getAppVersion()
            val language = ""//TODO("fix")
            
            val result = configService.getFeatures(
                country = country,
                appVersion = appVersion,
                language = language
            )
            
            if (result.isSuccess) {
                val apiConfig = result.getOrNull()?.data
                if (apiConfig != null) {
                    saveConfigToSharedPreferences(apiConfig)
                    cachedConfig = apiConfig
                    Log.d(TAG, "Config fetched from API and saved to SharedPreferences successfully")
                    true
                } else {
                    Log.w(TAG, "API response data is null, will check SharedPreferences then default_config.json")
                    false
                }
            } else {
                val error = result.exceptionOrNull()
                Log.e(TAG, "Error fetching config from API, will check SharedPreferences then default_config.json", error)
                false
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Exception while fetching config, will check SharedPreferences then default_config.json", e)
            false
        }
    }


    /**
     * Save config to SharedPreferences
     */
    private fun saveConfigToSharedPreferences(config: AppConfig) {
        try {
            preferencesManager.saveSerializable(KEY_CONFIG, config, AppConfig.serializer())
            Log.d(TAG, "Config saved to SharedPreferences")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving config to SharedPreferences: ${e.message}", e)
        }
    }

    /**
     * Load config from SharedPreferences
     */
    private fun loadConfigFromSharedPreferences(): AppConfig? {
        return try {
            preferencesManager.getSerializable(KEY_CONFIG, AppConfig.serializer())
        } catch (e: Exception) {
            Log.e(TAG, "Error loading config from SharedPreferences: ${e.message}", e)
            null
        }
    }


    /**
     * Get current config (from cache or SharedPreferences or default)
     */
    fun getConfig(): AppConfig {
        cachedConfig?.let { return it }
        loadConfigFromSharedPreferences()?.let {
            cachedConfig = it
            return it
        }
        val defaultConfig = loadDefaultConfig()
        cachedConfig = defaultConfig
        return defaultConfig
    }

    /**
     * Refresh config from API
     * If API succeeds, override SharedPreferences; if fails, check SharedPreferences then default_config.json
     */
    suspend fun refresh(): Boolean {
        val apiSuccess = fetchConfigFromApi()
        if (!apiSuccess) {
            // API failed - check SharedPreferences
            val sharedPrefConfig = loadConfigFromSharedPreferences()
            if (sharedPrefConfig != null) {
                cachedConfig = sharedPrefConfig
                Log.d(TAG, "Refresh failed, using config from SharedPreferences")
            } else {
                // No data in SharedPreferences - use default_config.json
                val defaultConfig = loadDefaultConfig()
                cachedConfig = defaultConfig
                Log.d(TAG, "Refresh failed and no SharedPreferences data, using default config from raw resource")
            }
        }

        return apiSuccess
    }

}

