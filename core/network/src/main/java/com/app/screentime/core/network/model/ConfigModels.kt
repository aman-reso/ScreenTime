package com.app.screentime.core.network.model

import kotlinx.serialization.Serializable

/**
 * App configuration model
 */
@Serializable
data class AppConfig(
    val features: FeaturesConfig = FeaturesConfig(),
    val challengeBannerURL: String? = null,
    val wallpaperBannerURL: String? = null,
    val adWeightage: Int? = 1,
    val wallpaperBaseURL: String? = null
)

/**
 * Features configuration
 * Supports both old format (individual booleans) and new format (enabled array)
 */
@Serializable
data class FeaturesConfig(
    val enabled: List<String> = emptyList(),
) {
    /**
     * Check if a feature is enabled
     * First checks the enabled array, then falls back to individual boolean fields
     */
    fun isFeatureEnabled(featureName: String): Boolean {
        val normalizedName = featureName.lowercase().replace("_", "").replace("-", "")
        if (enabled.isNotEmpty()) {
            val flattened = enabled.flatMap { entry ->
                entry.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            }
            return flattened.any { enabledFeature ->
                val normalizedEnabled = enabledFeature.lowercase().replace("_", "").replace("-", "")
                normalizedEnabled == normalizedName ||
                        normalizedName.contains(normalizedEnabled) ||
                        normalizedEnabled.contains(normalizedName)
            }
        }
        return false
    }
}

