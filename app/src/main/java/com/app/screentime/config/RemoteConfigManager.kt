package com.app.screentime.config

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor() {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    companion object {
        private const val TAG = "RemoteConfigManager"
        private const val KEY_BASE_URL = "api_base_url"
        private const val DEFAULT_BASE_URL = "https://3c5bcc423d9e.ngrok-free.app"

        // Cache expiration time (in seconds)
        private const val CACHE_EXPIRATION_SECONDS = 3600L // 1 hour
    }

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(100)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(
            mapOf(
                KEY_BASE_URL to DEFAULT_BASE_URL
            )
        )
    }

    /**
     * Fetch remote config values from Firebase
     */
    suspend fun fetch(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
            Log.d(TAG, "Remote config fetched and activated successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching remote config: ${e.message}", e)
            false
        }
    }

    /**
     * Get the base URL from Remote Config
     */
    fun getBaseUrl(): String {
//        val baseUrl = remoteConfig.getString(KEY_BASE_URL)
//        return baseUrl.ifBlank {
//            Log.w(TAG, "Base URL not found in remote config, using default")
//            DEFAULT_BASE_URL
//        }
        return DEFAULT_BASE_URL
    }

    /**
     * Force fetch remote config (bypasses cache)
     */
    suspend fun forceFetch(): Boolean {
        return try {
            remoteConfig.fetch(0).await()
            remoteConfig.activate()
            Log.d(TAG, "Remote config force fetched and activated successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error force fetching remote config: ${e.message}", e)
            false
        }
    }
}

