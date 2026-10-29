package com.app.screentime.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import kotlin.random.Random

/**
 * Manages preloading and serving Native Video Ads directly inside the Tinder-style swipe deck.
 * Ensures an ad is pre-buffered in memory with muted video autoplay for seamless zero-latency cards.
 */
object NativeAdCardManager {
    private const val TAG = "NativeAdCardManager"

    @Volatile
    private var preloadedNativeAd: NativeAd? = null

    @Volatile
    private var isLoading: Boolean = false

    @Volatile
    private var swipeCount: Int = 0

    @Volatile
    private var nextThreshold: Int = pickNextThreshold()

    private fun pickNextThreshold(): Int {
        val min = AdConfig.swipeAdMinInterval.coerceAtLeast(1)
        val max = AdConfig.swipeAdMaxInterval.coerceAtLeast(min)
        return if (min >= max) min else Random.nextInt(min, max + 1)
    }

    /**
     * Preload a Native Video Ad into memory.
     */
    fun preloadAd(context: Context) {
        if (!AdConfig.areAdsEnabled() || !AdConfig.isSwipeAdEnabled()) return
        if (preloadedNativeAd != null || isLoading) return

        isLoading = true
        val appContext = context.applicationContext
        val adUnitId = AdConfig.getNativeAdvancedAdUnitId()

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .setCustomControlsRequested(false)
            .build()

        val nativeAdOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT)
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            .build()

        val adLoader = AdLoader.Builder(appContext, adUnitId)
            .forNativeAd { nativeAd ->
                Log.d(TAG, "Native Video Ad preloaded successfully")
                preloadedNativeAd?.destroy()
                preloadedNativeAd = nativeAd
                isLoading = false
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Failed to preload Native Ad: ${error.message} (code ${error.code})")
                    isLoading = false
                }
            })
            .withNativeAdOptions(nativeAdOptions)
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    /**
     * Checks if a preloaded native ad is ready in memory.
     */
    fun hasPreloadedAd(): Boolean = preloadedNativeAd != null

    /**
     * Consumes and returns the preloaded native ad, then triggers background preload for the next ad.
     */
    fun consumePreloadedAd(context: Context): NativeAd? {
        val ad = preloadedNativeAd
        preloadedNativeAd = null
        preloadAd(context)
        return ad
    }

    /**
     * Called whenever a user profile is swiped in the deck.
     * Returns true when the swipe threshold is met and a preloaded ad is ready to insert.
     */
    fun onProfileSwiped(): Boolean {
        if (!AdConfig.areAdsEnabled() || !AdConfig.isSwipeAdEnabled()) {
            return false
        }
        swipeCount++
        Log.d(TAG, "Profile swiped: count=$swipeCount, threshold=$nextThreshold, hasAd=${hasPreloadedAd()}")
        if (swipeCount >= nextThreshold && hasPreloadedAd()) {
            swipeCount = 0
            nextThreshold = pickNextThreshold()
            return true
        }
        return false
    }

    /**
     * Clean up resources when leaving the deck.
     */
    fun clear() {
        preloadedNativeAd?.destroy()
        preloadedNativeAd = null
        isLoading = false
    }
}
