package com.app.screentime.core.network.config

import com.app.screentime.core.network.BuildConfig

/**
 * Application Secrets and Configuration
 */
object AppSecrets {

    object Api {
        val isDebug = BuildConfig.DEBUG
        val DEFAULT_BASE_URL: String =
            if (isDebug) BuildConfig.API_BASE_URL else BuildConfig.API_BASE_URL
    }

    object Encryption {
        const val KEY_ALIAS = "device_encryption_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_SIZE = 32
        const val IV_SIZE = 12
    }

    object Preferences {
        const val PREF_FCM_TOKEN = "fcm_token"
    }
}