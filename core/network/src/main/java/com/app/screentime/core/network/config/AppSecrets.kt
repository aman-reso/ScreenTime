package com.app.screentime.core.network.config

import com.app.screentime.core.network.BuildConfig
import com.app.screentime.core.network.security.NativeSecurityVault

/**
 * Application Secrets and Configuration
 */
object AppSecrets {

    object Api {
        val isDebug = BuildConfig.DEBUG
        val DEFAULT_BASE_URL: String
            get() = NativeSecurityVault.getBaseUrl(isDebug).trimEnd('/').removeSuffix("/api")
    }
}