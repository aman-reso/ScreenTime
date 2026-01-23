package com.app.screentime.ads

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.neutralScheme
import kotlinx.coroutines.delay

@Composable
fun NativeAdvancedAd(
    adState: NativeAdState,
    modifier: Modifier = Modifier
) {
    val nativeAdHeight = 350.dp

    when {
        adState.isFailed -> return

        adState.isLoading || adState.nativeAd == null -> {
            ODSSkeleton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(nativeAdHeight)
                    .padding(8.dp),
                scheme = neutralScheme,
                props = ODSSkeletonProps(
                    variant = ODSSkeletonVariant.LARGE
                )
            )
        }

        else -> {
            AndroidView(
                factory = { context ->
                    NativeAdView(context).apply {
                        populateNativeAdView(adState.nativeAd!!, this)
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

class NativeAdState {
    var isLoading by mutableStateOf(true)
    var isFailed by mutableStateOf(false)
    var nativeAd: NativeAd? by mutableStateOf(null)
    var lastLoadedAt by mutableLongStateOf(0L)
}

@Composable
fun rememberNativeAd(
    adUnitId: String
): NativeAdState? {
    if (!AdConfig.areAdsEnabled()) return null

    val context = LocalContext.current
    val adState = remember { NativeAdState() }

    fun loadAd() {
        adState.isLoading = true
        adState.isFailed = false

        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                adState.nativeAd?.destroy()
                adState.nativeAd = nativeAd
                adState.isLoading = false
                adState.lastLoadedAt = System.currentTimeMillis()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    adState.isLoading = false
                    adState.isFailed = true
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    LaunchedEffect(adUnitId) {
        loadAd()
    }

    LaunchedEffect(adState.lastLoadedAt) {
        if (adState.lastLoadedAt == 0L) return@LaunchedEffect

        val minDuration = 45_000L
        while (true) {
            val timeSinceLastLoad = System.currentTimeMillis() - adState.lastLoadedAt
            if (timeSinceLastLoad < minDuration) {
                delay(minDuration - timeSinceLastLoad)
            }
            val currentTimeSinceLastLoad = System.currentTimeMillis() - adState.lastLoadedAt
            if (currentTimeSinceLastLoad >= minDuration && !adState.isLoading && !adState.isFailed) {
                loadAd()
                delay(minDuration)
            } else {
                delay(5_000)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            adState.nativeAd?.destroy()
            adState.nativeAd = null
        }
    }

    return adState
}

private fun populateNativeAdView(
    nativeAd: NativeAd,
    adView: NativeAdView
) {
    val context = adView.context

    adView.removeAllViews()

    val container = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setPadding(
            8.dp.px(context),
            8.dp.px(context),
            8.dp.px(context),
            8.dp.px(context)
        )
    }

    val headerLayout = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 8.dp.px(context))
        }
    }

    val iconView = ImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            48.dp.px(context),
            48.dp.px(context)
        ).apply {
            setMargins(0, 0, 8.dp.px(context), 0)
        }
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

    val headlineView = TextView(context).apply {
        textSize = 16f
        layoutParams = LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            1f
        )
    }

    headerLayout.addView(iconView)
    headerLayout.addView(headlineView)

    val bodyView = TextView(context).apply {
        textSize = 14f
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 8.dp.px(context))
        }
    }

    val mediaView = MediaView(context).apply {
        val minSize = 120.dp.px(context)
        minimumWidth = minSize
        minimumHeight = minSize
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            180.dp.px(context)
        ).apply {
            setMargins(0, 0, 0, 8.dp.px(context))
        }
    }

    val ctaView = TextView(context).apply {
        textSize = 14f
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setPadding(
            16.dp.px(context),
            8.dp.px(context),
            16.dp.px(context),
            8.dp.px(context)
        )
    }

    container.addView(headerLayout)
    container.addView(bodyView)

    nativeAd.mediaContent?.let {
        container.addView(mediaView)
    }

    nativeAd.callToAction?.let {
        container.addView(ctaView)
    }

    adView.addView(container)

    adView.headlineView = headlineView
    adView.bodyView = bodyView
    adView.iconView = iconView
    adView.mediaView = mediaView
    adView.callToActionView = ctaView

    headlineView.text = nativeAd.headline
    bodyView.text = nativeAd.body

    nativeAd.icon?.let {
        iconView.setImageDrawable(it.drawable)
    }

    nativeAd.callToAction?.let {
        ctaView.text = it
    }

    nativeAd.mediaContent?.let {
        mediaView.mediaContent = it
    }

    adView.setNativeAd(nativeAd)
}

private fun Dp.px(context: Context): Int =
    (value * context.resources.displayMetrics.density).toInt()
