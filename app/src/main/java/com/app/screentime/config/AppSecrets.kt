package com.app.screentime.config

import com.app.screentime.BuildConfig

/**
 * Application Secrets and Configuration
 *
 * Production-ready secret management:
 * - Secrets are loaded from BuildConfig (set in build.gradle.kts)
 * - BuildConfig reads from local.properties (gitignored)
 * - For CI/CD, set secrets as environment variables or in CI config
 *
 * To add secrets:
 * 1. Add to local.properties: API_BASE_URL=https://your-api.com
 * 2. Add buildConfigField in build.gradle.kts
 * 3. Reference via BuildConfig.API_BASE_URL
 */
object AppSecrets {

    /**
     * API Base URLs
     * Loaded from BuildConfig (which reads from local.properties)
     */
    object Api {
        // Production API URL
        val DEFAULT_BASE_URL: String = BuildConfig.API_BASE_URL
    }


    /**
     * Encryption Keys
     * Key aliases and algorithm names
     */
    object Encryption {
        const val KEY_ALIAS = "device_encryption_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_SIZE = 32 // 256-bit key
        const val IV_SIZE = 12  // GCM standard
    }

    /**
     * Firebase Configuration Keys
     */
    object Firebase {
        const val REMOTE_CONFIG_KEY_BASE_URL = "api_base_url"
        const val REMOTE_CONFIG_CACHE_EXPIRATION_SECONDS: Long = 3600L // 1 hour
    }

    /**
     * Preference Keys
     */
    object Preferences {
        const val PREF_FCM_TOKEN = "fcm_token"
    }
}

