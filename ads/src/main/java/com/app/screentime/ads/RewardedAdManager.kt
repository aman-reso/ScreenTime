package com.app.screentime.ads

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * Manager for handling rewarded ads.
 * Loads and shows rewarded ads, providing rewards to users.
 */
object RewardedAdManager {
    private const val TAG = "RewardedAdManager"
    private var rewardedAd: RewardedAd? = null
    private var isLoading = false
    private var onRewardEarnedCallback: ((RewardItem) -> Unit)? = null
    private var onAdDismissedCallback: (() -> Unit)? = null

    // Counters for swipe threshold (5-10 swipes)
    private var swipeCount = 0
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
     * Called when a swipe action occurs.
     * When the 5-10 swipe threshold is met, displays a full-screen Video Ad (RewardedAd),
     * and falls back to InterstitialAd if the video is still downloading.
     */
    fun onSwipe(activity: Activity) {
        if (!AdConfig.isSwipeAdEnabled()) return
        swipeCount++
        Log.d(TAG, "RewardedAd onSwipe: current count=$swipeCount, threshold=$nextSwipeThreshold")
        if (swipeCount >= nextSwipeThreshold) {
            swipeCount = 0
            nextSwipeThreshold = pickNextSwipeThreshold()
            val showedVideo = showVideoAd(activity)
            if (!showedVideo) {
                // If video ad is still buffering, fallback to standard interstitial
                Log.d(TAG, "Video ad not ready yet, attempting interstitial fallback")
                InterstitialAdManager.showInterstitialAd(activity)
            }
        }
    }

    fun loadRewardedAd(activity: Activity, adUnitId: String = AdConfig.getRewardedAdUnitId()) {
        if (!AdConfig.areAdsEnabled()) return
        if (isLoading || rewardedAd != null) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            activity,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Rewarded Video ad loaded successfully")
                    rewardedAd = ad
                    isLoading = false

                    val pendingRewardCallback = onRewardEarnedCallback
                    val pendingDismissCallback = onAdDismissedCallback
                    if (pendingRewardCallback != null || pendingDismissCallback != null) {
                        onRewardEarnedCallback = null
                        onAdDismissedCallback = null
                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Rewarded Video ad dismissed")
                                rewardedAd = null
                                pendingDismissCallback?.invoke()
                                preloadAd(activity)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(TAG, "Rewarded Video ad failed to show: ${adError.message}")
                                rewardedAd = null
                                pendingDismissCallback?.invoke()
                                preloadAd(activity)
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Rewarded Video ad showed")
                                rewardedAd = null
                            }
                        }
                        ad.show(activity) { rewardItem ->
                            Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                            pendingRewardCallback?.invoke(rewardItem)
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Rewarded Video ad failed to load: ${loadAdError.message}")
                    rewardedAd = null
                    isLoading = false
                    val pendingDismissCallback = onAdDismissedCallback
                    onAdDismissedCallback = null
                    onRewardEarnedCallback = null
                    pendingDismissCallback?.invoke()
                }
            }
        )
    }

    /**
     * Show full-screen video ad without requiring manual reward handling.
     * Automatically preloads next video ad on completion.
     */
    fun showVideoAd(activity: Activity, onAdDismissed: (() -> Unit)? = null): Boolean {
        if (!AdConfig.areAdsEnabled()) {
            onAdDismissed?.invoke()
            return false
        }

        val ad = rewardedAd
        if (ad != null) {
            onAdDismissedCallback = onAdDismissed
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Video ad dismissed")
                    rewardedAd = null
                    val cb = onAdDismissedCallback
                    onAdDismissedCallback = null
                    cb?.invoke()
                    preloadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Video ad failed to show: ${adError.message}")
                    rewardedAd = null
                    val cb = onAdDismissedCallback
                    onAdDismissedCallback = null
                    cb?.invoke()
                    preloadAd(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Video ad showed")
                    rewardedAd = null
                }
            }
            ad.show(activity) { rewardItem ->
                Log.d(TAG, "Video ad completed: ${rewardItem.amount} ${rewardItem.type}")
            }
            return true
        } else {
            Log.d(TAG, "Video ad not ready in memory, triggering preload")
            preloadAd(activity)
            onAdDismissed?.invoke()
            return false
        }
    }

    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: (RewardItem) -> Unit,
        onAdDismissed: (() -> Unit)? = null
    ): Boolean {
        if (!AdConfig.areAdsEnabled()) {
            onAdDismissed?.invoke()
            return false
        }

        val ad = rewardedAd
        return when {
            ad != null -> {
                onRewardEarnedCallback = onRewardEarned
                onAdDismissedCallback = onAdDismissed
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Rewarded ad dismissed")
                        rewardedAd = null
                        val cb = onAdDismissedCallback
                        onAdDismissedCallback = null
                        cb?.invoke()
                        preloadAd(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, "Rewarded ad failed to show: ${adError.message}")
                        rewardedAd = null
                        val cb = onAdDismissedCallback
                        onAdDismissedCallback = null
                        cb?.invoke()
                        preloadAd(activity)
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Rewarded ad showed")
                        rewardedAd = null
                    }
                }
                ad.show(activity) { rewardItem ->
                    Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                    onRewardEarnedCallback?.invoke(rewardItem)
                    onRewardEarnedCallback = null
                }
                true
            }
            isLoading -> {
                onRewardEarnedCallback = onRewardEarned
                onAdDismissedCallback = onAdDismissed
                true
            }
            else -> {
                onRewardEarnedCallback = onRewardEarned
                onAdDismissedCallback = onAdDismissed
                loadRewardedAd(activity)
                true
            }
        }
    }

    fun isAdLoaded(): Boolean = rewardedAd != null

    fun preloadAd(activity: Activity) {
        if (!AdConfig.areAdsEnabled()) return
        if (rewardedAd == null && !isLoading) {
            loadRewardedAd(activity)
        }
    }
}
