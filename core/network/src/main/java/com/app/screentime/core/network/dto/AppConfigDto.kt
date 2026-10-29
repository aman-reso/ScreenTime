package com.app.screentime.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Flexible DTO for the Backend App Config API.
 * Supports both nested structure:
 * {
 *   "success": true,
 *   "data": { ... }
 * }
 * and direct flat structure:
 * {
 *   "apikey": "...",
 *   "ads_enabled": true,
 *   ...
 * }
 */
@Serializable
data class AppConfigResponse(
    @SerialName("success") val success: Boolean? = null,
    @SerialName("data") val data: AppConfigData? = null,

    // Direct root fallback fields if backend sends flat JSON
    @SerialName("apikey") val directApiKey: String? = null,
    @SerialName("apkey") val directApkey: String? = null,
    @SerialName("ads_enabled") val directAdsEnabled: Boolean? = null,
    @SerialName("swipe_ad_enabled") val directSwipeAdEnabled: Boolean? = null,
    @SerialName("message_ad_enabled") val directMessageAdEnabled: Boolean? = null,
    @SerialName("swipe_ad_interval") val directSwipeAdInterval: Int? = null,
    @SerialName("swipe_ad_min_interval") val directSwipeAdMinInterval: Int? = null,
    @SerialName("swipe_ad_max_interval") val directSwipeAdMaxInterval: Int? = null,
    @SerialName("message_ad_interval") val directMessageAdInterval: Int? = null,
    @SerialName("min_version") val directMinVersion: String? = null,
    @SerialName("latest_version") val directLatestVersion: String? = null,
    @SerialName("force_update") val directForceUpdate: Boolean? = null,
    @SerialName("update_url") val directUpdateUrl: String? = null
) {
    fun resolveConfig(): AppConfigData {
        return AppConfigData(
            apikey = data?.apikey ?: data?.apkey ?: directApiKey ?: directApkey,
            apkey = data?.apkey ?: directApkey,
            adsEnabled = data?.adsEnabled ?: directAdsEnabled,
            swipeAdEnabled = data?.swipeAdEnabled ?: directSwipeAdEnabled,
            messageAdEnabled = data?.messageAdEnabled ?: directMessageAdEnabled,
            swipeAdInterval = data?.swipeAdInterval ?: directSwipeAdInterval,
            swipeAdMinInterval = data?.swipeAdMinInterval ?: directSwipeAdMinInterval,
            swipeAdMaxInterval = data?.swipeAdMaxInterval ?: directSwipeAdMaxInterval,
            messageAdInterval = data?.messageAdInterval ?: directMessageAdInterval,
            minVersion = data?.minVersion ?: directMinVersion,
            latestVersion = data?.latestVersion ?: directLatestVersion,
            forceUpdate = data?.forceUpdate ?: directForceUpdate,
            updateUrl = data?.updateUrl ?: directUpdateUrl
        )
    }
}

@Serializable
data class AppConfigData(
    @SerialName("apikey") val apikey: String? = null,
    @SerialName("apkey") val apkey: String? = null,
    @SerialName("ads_enabled") val adsEnabled: Boolean? = null,
    @SerialName("swipe_ad_enabled") val swipeAdEnabled: Boolean? = null,
    @SerialName("message_ad_enabled") val messageAdEnabled: Boolean? = null,
    @SerialName("swipe_ad_interval") val swipeAdInterval: Int? = null,
    @SerialName("swipe_ad_min_interval") val swipeAdMinInterval: Int? = null,
    @SerialName("swipe_ad_max_interval") val swipeAdMaxInterval: Int? = null,
    @SerialName("message_ad_interval") val messageAdInterval: Int? = null,
    @SerialName("min_version") val minVersion: String? = null,
    @SerialName("latest_version") val latestVersion: String? = null,
    @SerialName("force_update") val forceUpdate: Boolean? = null,
    @SerialName("update_url") val updateUrl: String? = null
)
