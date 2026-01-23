package com.app.screentime.ads

/**
 * Centralized ad configuration for managing ad unit IDs and ad display.
 * Provides different ad unit IDs for debug and release builds.
 * Master flag to control ad initialization and display.
 */
object AdConfig {
    // App ID
    const val APP_ID = "ca-app-pub-5847819400812479~1194871943"

    /**
     * Check if ads are enabled.
     * @return true if ads should be shown, false otherwise
     */
    fun areAdsEnabled(): Boolean = true

    // Debug ad unit IDs (test ads)
    private const val DEBUG_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    private const val DEBUG_NATIVE_ADVANCED_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    private const val DEBUG_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    private const val DEBUG_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    // Release ad unit IDs (production ads)
    private const val RELEASE_BANNER_AD_UNIT_ID = "ca-app-pub-5847819400812479/1063828240"
    private const val RELEASE_NATIVE_ADVANCED_AD_UNIT_ID = "ca-app-pub-5847819400812479/3889796977"
    private const val RELEASE_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5847819400812479/7438159742"
    private const val RELEASE_REWARDED_AD_UNIT_ID = "ca-app-pub-5847819400812479/7223710780"

    /**
     * Returns the banner ad unit ID based on build type.
     */
    fun getBannerAdUnitId(): String = if (BuildConfig.DEBUG) DEBUG_BANNER_AD_UNIT_ID else RELEASE_BANNER_AD_UNIT_ID

    /**
     * Returns the native advanced ad unit ID based on build type.
     */
    fun getNativeAdvancedAdUnitId(): String = if (BuildConfig.DEBUG) DEBUG_NATIVE_ADVANCED_AD_UNIT_ID else RELEASE_NATIVE_ADVANCED_AD_UNIT_ID

    /**
     * Returns the interstitial ad unit ID based on build type.
     */
    fun getInterstitialAdUnitId(): String = if (BuildConfig.DEBUG) DEBUG_INTERSTITIAL_AD_UNIT_ID else RELEASE_INTERSTITIAL_AD_UNIT_ID

    /**
     * Returns the rewarded ad unit ID based on build type.
     */
    fun getRewardedAdUnitId(): String = if (BuildConfig.DEBUG) DEBUG_REWARDED_AD_UNIT_ID else RELEASE_REWARDED_AD_UNIT_ID
}
