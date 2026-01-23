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
                    Log.d(TAG, "Rewarded ad loaded")
                    rewardedAd = ad
                    isLoading = false

                    val pendingRewardCallback = onRewardEarnedCallback
                    val pendingDismissCallback = onAdDismissedCallback
                    if (pendingRewardCallback != null) {
                        onRewardEarnedCallback = null
                        onAdDismissedCallback = null
                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Rewarded ad dismissed")
                                rewardedAd = null
                                pendingDismissCallback?.invoke()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(TAG, "Rewarded ad failed to show: ${adError.message}")
                                rewardedAd = null
                                pendingDismissCallback?.invoke()
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Rewarded ad showed")
                                rewardedAd = null
                            }
                        }
                        ad.show(activity) { rewardItem ->
                            Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                            pendingRewardCallback.invoke(rewardItem)
                        }
                        return
                    }

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Rewarded ad dismissed")
                            rewardedAd = null
                            onAdDismissedCallback?.invoke()
                            onAdDismissedCallback = null
                            loadRewardedAd(activity, adUnitId)
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.e(TAG, "Rewarded ad failed to show: ${adError.message}")
                            rewardedAd = null
                            onAdDismissedCallback?.invoke()
                            onAdDismissedCallback = null
                            onRewardEarnedCallback = null
                            loadRewardedAd(activity, adUnitId)
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Rewarded ad showed")
                            rewardedAd = null
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Rewarded ad failed to load: ${loadAdError.message}")
                    rewardedAd = null
                    isLoading = false
                }
            }
        )
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
