package com.app.screentime.feature.chat.component

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.screentime.ads.AdConfig
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * In-Chat Native Message Bubble Card (Telegram / Tinder Style).
 * Seamlessly integrates into the chat message stream without blocking typing or closing keyboard.
 * 100% Telekom ODS design system compliance.
 */
@Composable
fun InChatNativeAdBubble(
    adSlotIndex: Int,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    if (!AdConfig.areAdsEnabled() || !AdConfig.isMessageAdEnabled()) return

    val context = LocalContext.current
    var nativeAd by remember(adSlotIndex) { mutableStateOf<NativeAd?>(null) }
    var isFailed by remember(adSlotIndex) { mutableStateOf(false) }

    DisposableEffect(adSlotIndex) {
        val adUnitId = AdConfig.getNativeAdvancedAdUnitId()
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                nativeAd?.destroy()
                nativeAd = ad
                isFailed = false
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isFailed = true
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setVideoOptions(
                        VideoOptions.Builder()
                            .setStartMuted(true)
                            .setCustomControlsRequested(false)
                            .build()
                    )
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            nativeAd?.destroy()
            nativeAd = null
        }
    }

    if (isFailed || nativeAd == null) return

    val ad = nativeAd ?: return

    ODSBox(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ODSBox(
            width = 300.dp,
            cornerRadius = ODSCorners(all = 16.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            clipContent = true
        ) {
            AndroidView(
                factory = { ctx ->
                    createInChatAdView(ctx, ad, scheme)
                },
                update = { adView ->
                    populateInChatAdView(ad, adView)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun createInChatAdView(
    context: Context,
    nativeAd: NativeAd,
    scheme: ODSTheme
): NativeAdView {
    val adView = NativeAdView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val container = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val pad = 12.dp.px(context)
        setPadding(pad, pad, pad, pad)
    }

    // Top Header: Sponsored Badge
    val topHeader = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val sponsorBadge = TextView(context).apply {
        text = "SPONSORED"
        textSize = 9f
        typeface = android.graphics.Typeface.DEFAULT_BOLD
        setTextColor(AndroidColor.parseColor("#FF4B72"))
        background = GradientDrawable().apply {
            setColor(AndroidColor.parseColor("#26FF4B72"))
            cornerRadius = 4.dp.px(context).toFloat()
        }
        val pX = 6.dp.px(context)
        val pY = 2.dp.px(context)
        setPadding(pX, pY, pX, pY)
    }
    topHeader.addView(sponsorBadge)
    container.addView(topHeader)

    // MediaView (for video/rich image preview)
    val mediaView = MediaView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            140.dp.px(context)
        ).apply {
            topMargin = 8.dp.px(context)
            bottomMargin = 8.dp.px(context)
        }
        background = GradientDrawable().apply {
            cornerRadius = 10.dp.px(context).toFloat()
            setColor(AndroidColor.parseColor("#1C1E24"))
        }
        clipToOutline = true
    }
    container.addView(mediaView)

    // Brand icon + Headline row
    val brandRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val iconView = ImageView(context).apply {
        val s = 34.dp.px(context)
        layoutParams = LinearLayout.LayoutParams(s, s).apply {
            marginEnd = 10.dp.px(context)
        }
        scaleType = ImageView.ScaleType.CENTER_CROP
        background = GradientDrawable().apply {
            cornerRadius = 8.dp.px(context).toFloat()
        }
        clipToOutline = true
    }
    brandRow.addView(iconView)

    val headlineCol = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
    }

    val headlineView = TextView(context).apply {
        textSize = 13f
        setTextColor(scheme.basicTextDominant.getIntColor())
        typeface = android.graphics.Typeface.DEFAULT_BOLD
        maxLines = 1
        ellipsize = android.text.TextUtils.TruncateAt.END
    }
    headlineCol.addView(headlineView)

    val bodyView = TextView(context).apply {
        textSize = 11f
        setTextColor(scheme.basicTextRecessive.getIntColor())
        maxLines = 2
        ellipsize = android.text.TextUtils.TruncateAt.END
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 2.dp.px(context)
        }
    }
    headlineCol.addView(bodyView)

    brandRow.addView(headlineCol)
    container.addView(brandRow)

    // CTA Button Row
    val ctaRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.END
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 8.dp.px(context)
        }
    }

    val ctaView = TextView(context).apply {
        textSize = 12f
        setTextColor(AndroidColor.WHITE)
        typeface = android.graphics.Typeface.DEFAULT_BOLD
        background = GradientDrawable().apply {
            setColor(AndroidColor.parseColor("#FF4B72"))
            cornerRadius = 8.dp.px(context).toFloat()
        }
        val pX = 14.dp.px(context)
        val pY = 6.dp.px(context)
        setPadding(pX, pY, pX, pY)
    }
    ctaRow.addView(ctaView)
    container.addView(ctaRow)

    adView.addView(container)

    // Register with AdMob
    adView.mediaView = mediaView
    adView.headlineView = headlineView
    adView.bodyView = bodyView
    adView.iconView = iconView
    adView.callToActionView = ctaView

    populateInChatAdView(nativeAd, adView)

    return adView
}

private fun populateInChatAdView(nativeAd: NativeAd, adView: NativeAdView) {
    val headlineView = adView.headlineView as? TextView
    val bodyView = adView.bodyView as? TextView
    val iconView = adView.iconView as? ImageView
    val mediaView = adView.mediaView
    val ctaView = adView.callToActionView as? TextView

    headlineView?.text = nativeAd.headline ?: "Sponsored"
    bodyView?.text = nativeAd.body ?: ""

    if (nativeAd.icon != null) {
        iconView?.visibility = View.VISIBLE
        iconView?.setImageDrawable(nativeAd.icon?.drawable)
    } else {
        iconView?.visibility = View.GONE
    }

    if (nativeAd.mediaContent != null && mediaView != null) {
        mediaView.mediaContent = nativeAd.mediaContent
        mediaView.visibility = View.VISIBLE
    } else {
        mediaView?.visibility = View.GONE
    }

    if (nativeAd.callToAction != null) {
        ctaView?.visibility = View.VISIBLE
        ctaView?.text = nativeAd.callToAction
    } else {
        ctaView?.visibility = View.GONE
    }

    adView.setNativeAd(nativeAd)
}

private fun Dp.px(context: Context): Int =
    (value * context.resources.displayMetrics.density).toInt()
