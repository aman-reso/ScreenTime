package com.app.screentime.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.neutralScheme
import kotlinx.coroutines.delay

/**
 * Banner Ad composable for displaying Google Mobile Ads banner.
 * Shows shimmer while loading and hides if ad fails to load.
 */
@Composable
fun BannerAd(
    adView: AdView,
    adState: BannerAdState,
    modifier: Modifier = Modifier
) {
    if (adState.isFailed) return
    val bannerHeight = 50.dp
    Box(modifier = modifier.fillMaxWidth()) {
        AndroidView(
            factory = { adView },
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
        )
        if (adState.isLoading) {
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bannerHeight),
                scheme = neutralScheme,
                props = ODSSkeletonProps(
                    variant = ODSSkeletonVariant.MEDIUM
                )
            )
        }
    }
}

@Composable
fun rememberBannerAd(
    adUnitId: String
): Pair<AdView, BannerAdState>? {
    if (!AdConfig.areAdsEnabled()) return null

    val context = LocalContext.current
    val adState = remember { BannerAdState() }

    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = adUnitId

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    adState.isLoading = false
                    adState.isFailed = false
                    adState.lastLoadedAt = System.currentTimeMillis()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    adState.isLoading = false
                    adState.isFailed = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        adState.isLoading = true
        adView.loadAd(AdRequest.Builder().build())
    }

    LaunchedEffect(adState.lastLoadedAt) {
        if (adState.lastLoadedAt == 0L) return@LaunchedEffect

        val minDuration = 30_000L
        while (true) {
            val timeSinceLastLoad = System.currentTimeMillis() - adState.lastLoadedAt
            if (timeSinceLastLoad < minDuration) {
                delay(minDuration - timeSinceLastLoad)
            }
            val currentTimeSinceLastLoad = System.currentTimeMillis() - adState.lastLoadedAt
            if (currentTimeSinceLastLoad >= minDuration && !adState.isLoading && !adState.isFailed) {
                adState.isLoading = true
                adView.loadAd(AdRequest.Builder().build())
                delay(minDuration)
            } else {
                delay(5_000)
            }
        }
    }

    return adView to adState
}

class BannerAdState {
    var isLoading by mutableStateOf(true)
    var isFailed by mutableStateOf(false)
    var lastLoadedAt by mutableLongStateOf(0L)
}
