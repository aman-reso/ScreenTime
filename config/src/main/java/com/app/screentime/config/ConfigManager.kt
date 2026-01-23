package com.app.screentime.config

import com.app.screentime.config.data.Feature
import com.app.screentime.core.network.model.AppConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigManager @Inject constructor(
    private val configRepository: ConfigRepository
) {
    /**
     * Get current config (synchronous)
     * Returns default config if API config is not available
     */
    fun getConfig(): AppConfig {
        return configRepository.getConfig()
    }

    /**
     * Initialize config (load default, then fetch from API)
     * Should be called at app startup
     */
    suspend fun initialize() {
        configRepository.initialize()
    }

    /**
     * Refresh config from API
     */
    suspend fun refresh(): Boolean {
        return configRepository.refresh()
    }

    /**
     * Check if a feature is enabled
     * Supports both new format (enabled array) and legacy format (individual booleans)
     */
    fun isFeatureEnabled(feature: Feature): Boolean {
        val config = getConfig()
        return config.features.isFeatureEnabled(feature.key)
    }
}

