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
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(TAG, "Interstitial ad failed to show: ${adError.message}")
                                interstitialAd = null
                                pendingCallback.invoke()
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Interstitial ad showed")
                                interstitialAd = null
                            }
                        }
                        ad.show(activity)
                        return
                    }

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Interstitial ad dismissed")
                            interstitialAd = null
                            onAdDismissedCallback?.invoke()
                            onAdDismissedCallback = null
                            loadInterstitialAd(activity, adUnitId)
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.e(TAG, "Interstitial ad failed to show: ${adError.message}")
                            interstitialAd = null
                            onAdDismissedCallback?.invoke()
                            onAdDismissedCallback = null
                            loadInterstitialAd(activity, adUnitId)
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Interstitial ad showed")
                            interstitialAd = null
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Interstitial ad failed to load: ${loadAdError.message}")
                    interstitialAd = null
                    isLoading = false
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
        return when {
            ad != null -> {
                onAdDismissedCallback = onAdDismissed
                ad.show(activity)
                true
            }
            isLoading -> {
                onAdDismissedCallback = onAdDismissed
                true
            }
            else -> {
                onAdDismissedCallback = onAdDismissed
                loadInterstitialAd(activity)
                true
            }
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
