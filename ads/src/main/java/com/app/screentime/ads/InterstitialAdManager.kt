package com.app.screentime.ads

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Manager for handling interstitial ads.
 * Loads and shows interstitial ads at appropriate times.
 */
object InterstitialAdManager {
    private const val TAG = "InterstitialAdManager"
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var onAdDismissedCallback: (() -> Unit)? = null

    // Counters for swipe and messaging thresholds
    private var swipeCount = 0
    private var messageCount = 0
    private var nextSwipeThreshold = pickNextSwipeThreshold()

    private fun pickNextSwipeThreshold(): Int {
        val min = AdConfig.swipeAdMinInterval
        val max = AdConfig.swipeAdMaxInterval
        return if (max > min) {
            kotlin.random.Random.nextInt(min, max + 1)
        } else {
            min
        }
    }

    /**
     * Called when a swipe action (like, dislike, superlike) occurs.
     * Increments swipe counter and displays an interstitial ad once 5-10 swipes
     * threshold is reached (governed by Remote Config).
     */
    fun onSwipe(activity: Activity) {
        if (!AdConfig.isSwipeAdEnabled()) return
        swipeCount++
        Log.d(TAG, "onSwipe: current count=$swipeCount, threshold=$nextSwipeThreshold")
        if (swipeCount >= nextSwipeThreshold) {
            swipeCount = 0
            nextSwipeThreshold = pickNextSwipeThreshold()
            showInterstitialAd(activity)
        }
    }

    /**
     * Called when a chat message is sent.
     * Increments message counter and displays an interstitial ad once 5 messages
     * threshold is reached (governed by Remote Config).
     */
    fun onMessageSent(activity: Activity) {
        if (!AdConfig.isMessageAdEnabled()) return
        messageCount++
        val interval = AdConfig.messageAdInterval
        Log.d(TAG, "onMessageSent: current count=$messageCount, interval=$interval")
        if (messageCount >= interval) {
            messageCount = 0
            showInterstitialAd(activity)
        }
    }

    fun loadInterstitialAd(activity: Activity, adUnitId: String = AdConfig.getInterstitialAdUnitId()) {
        if (!AdConfig.areAdsEnabled()) return
        if (isLoading || interstitialAd != null) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded")
                    interstitialAd = ad
                    isLoading = false

                    val pendingCallback = onAdDismissedCallback
                    if (pendingCallback != null) {
                        onAdDismissedCallback = null
                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Interstitial ad dismissed")
                                interstitialAd = null
                                pendingCallback.invoke()
                                preloadAd(activity)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(TAG, "Interstitial ad failed to show: ${adError.message}")
                                interstitialAd = null
                                pendingCallback.invoke()
                                preloadAd(activity)
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Interstitial ad showed")
                                interstitialAd = null
                            }
                        }
                        ad.show(activity)
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Interstitial ad failed to load: ${loadAdError.message}")
                    interstitialAd = null
                    isLoading = false
                    val pendingCallback = onAdDismissedCallback
                    onAdDismissedCallback = null
                    pendingCallback?.invoke()
                }
            }
        )
    }

    fun showInterstitialAd(activity: Activity, onAdDismissed: (() -> Unit)? = null): Boolean {
        if (!AdConfig.areAdsEnabled()) {
            onAdDismissed?.invoke()
            return false
        }

        val ad = interstitialAd
        if (ad != null) {
            onAdDismissedCallback = onAdDismissed
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Interstitial ad dismissed")
                    interstitialAd = null
                    val cb = onAdDismissedCallback
                    onAdDismissedCallback = null
                    cb?.invoke()
                    preloadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Interstitial ad failed to show: ${adError.message}")
                    interstitialAd = null
                    val cb = onAdDismissedCallback
                    onAdDismissedCallback = null
                    cb?.invoke()
                    preloadAd(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Interstitial ad showed")
                    interstitialAd = null
                }
            }
            ad.show(activity)
            return true
        } else {
            // If ad is not ready, don't block user experience; trigger preload for next time
            Log.d(TAG, "Interstitial ad not ready in memory, preloading for next event")
            preloadAd(activity)
            onAdDismissed?.invoke()
            return false
        }
    }

    fun isAdLoaded(): Boolean = interstitialAd != null

    fun preloadAd(activity: Activity) {
        if (!AdConfig.areAdsEnabled()) return
        if (interstitialAd == null && !isLoading) {
            loadInterstitialAd(activity)
        }
    }
}
