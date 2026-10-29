package com.app.screentime.feature.discover.components

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Tinder-style Native Video Ad Card rendered in the EXACT same frame as HomeFeedCard.
 * Features:
 * - Autoplay video/rich media inside Google AdMob MediaView
 * - Sponsored/Ad attribution badge
 * - Advertiser brand icon, headline, description, and Call-To-Action button
 * - Animated Like / Pass stamps on horizontal drag
 * - 100% Telekom ODS design system compliance
 */
@Composable
fun HomeFeedAdCard(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    swipeOffsetX: Float = 0f,
    onCardClick: () -> Unit = {}
) {
    val likeAlpha = (swipeOffsetX / 150f).coerceIn(0f, 1f)
    val passAlpha = (-swipeOffsetX / 150f).coerceIn(0f, 1f)

    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = ODSVariables.spacingLayout1,
                vertical = ODSVariables.spacingComponent2
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCardClick
            )
    ) {
        ODSBox(
            modifier = Modifier.fillMaxSize(),
            cornerRadius = ODSCorners(all = 24.dp),
            clipContent = true,
            background = listOf(ODSColorModel(hexColor = HexColor("#111318")))
        ) {
            // Native Ad Layout View via AndroidView
            AndroidView(
                factory = { context ->
                    createTinderAdView(context, nativeAd)
                },
                update = { adView ->
                    populateTinderAdView(nativeAd, adView)
                },
                modifier = Modifier.fillMaxSize()
            )

            // Swipe Stamp Overlay - Like (Heart / Interested)
            if (likeAlpha > 0.05f) {
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(likeAlpha),
                    width = 80.dp,
                    height = 80.dp,
                    cornerRadius = ODSCorners(all = 40.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_heart_filled,
                            contentDescription = "Like"
                        ),
                        tint = scheme.basicTextOnAccent.getColor(),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            // Swipe Stamp Overlay - Pass (Cross / Skip)
            if (passAlpha > 0.05f) {
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(passAlpha),
                    width = 80.dp,
                    height = 80.dp,
                    cornerRadius = ODSCorners(all = 40.dp),
                    border = ODSBorder(
                        width = 2.dp,
                        colorList = listOf(ODSColorModel(hexColor = HexColor("#EF4444")))
                    ),
                    background = listOf(ODSColorModel(hexColor = HexColor("#000000", 0.4f))),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_close,
                            contentDescription = "Pass"
                        ),
                        tint = HexColor("#EF4444").getColor(),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }
    }
}

private fun createTinderAdView(context: Context, nativeAd: NativeAd): NativeAdView {
    val adView = NativeAdView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    val rootFrame = FrameLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(AndroidColor.parseColor("#111318"))
    }

    // MediaView filling full card background
    val mediaView = MediaView(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
    rootFrame.addView(mediaView)

    // Top Sponsored attribution pill
    val sponsoredPill = TextView(context).apply {
        text = "SPONSORED"
        textSize = 10f
        setTextColor(AndroidColor.WHITE)
        background = GradientDrawable().apply {
            setColor(AndroidColor.parseColor("#80000000"))
            cornerRadius = 8.dp.px(context).toFloat()
        }
        val padH = 8.dp.px(context)
        val padV = 4.dp.px(context)
        setPadding(padH, padV, padH, padV)
        val margin = 16.dp.px(context)
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.TOP or Gravity.START
        ).apply {
            setMargins(margin, margin, 0, 0)
        }
    }
    rootFrame.addView(sponsoredPill)

    // Bottom gradient overlay for readability scrim
    val gradientView = View(context).apply {
        background = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP,
            intArrayOf(
                AndroidColor.parseColor("#F2000000"),
                AndroidColor.parseColor("#B3000000"),
                AndroidColor.parseColor("#00000000")
            )
        )
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            240.dp.px(context),
            Gravity.BOTTOM
        )
    }
    rootFrame.addView(gradientView)

    // Bottom details layout
    val detailsLayout = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        val pad = 20.dp.px(context)
        val marginBot = 20.dp.px(context)
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM
        ).apply {
            setMargins(pad, 0, pad, marginBot)
        }
    }

    // Brand icon + headline row
    val brandRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val iconView = ImageView(context).apply {
        val size = 44.dp.px(context)
        layoutParams = LinearLayout.LayoutParams(size, size).apply {
            marginEnd = 12.dp.px(context)
        }
        scaleType = ImageView.ScaleType.CENTER_CROP
        background = GradientDrawable().apply {
            cornerRadius = 10.dp.px(context).toFloat()
        }
        clipToOutline = true
    }
    brandRow.addView(iconView)

    val headlineCol = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
    }

    val headlineView = TextView(context).apply {
        textSize = 16f
        setTextColor(AndroidColor.WHITE)
        typeface = android.graphics.Typeface.DEFAULT_BOLD
        maxLines = 1
        ellipsize = android.text.TextUtils.TruncateAt.END
    }
    headlineCol.addView(headlineView)

    val advertiserView = TextView(context).apply {
        textSize = 12f
        setTextColor(AndroidColor.parseColor("#B3FFFFFF"))
        maxLines = 1
        ellipsize = android.text.TextUtils.TruncateAt.END
    }
    headlineCol.addView(advertiserView)

    brandRow.addView(headlineCol)
    detailsLayout.addView(brandRow)

    // Ad Body description
    val bodyView = TextView(context).apply {
        textSize = 13f
        setTextColor(AndroidColor.parseColor("#E6FFFFFF"))
        maxLines = 2
        ellipsize = android.text.TextUtils.TruncateAt.END
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 8.dp.px(context)
        }
    }
    detailsLayout.addView(bodyView)

    // Call-To-Action Button
    val ctaView = TextView(context).apply {
        textSize = 14f
        setTextColor(AndroidColor.WHITE)
        typeface = android.graphics.Typeface.DEFAULT_BOLD
        background = GradientDrawable().apply {
            setColor(AndroidColor.parseColor("#FF4B72")) // Brand Coral Accent
            cornerRadius = 12.dp.px(context).toFloat()
        }
        val padH = 18.dp.px(context)
        val padV = 8.dp.px(context)
        setPadding(padH, padV, padH, padV)
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 10.dp.px(context)
        }
    }
    detailsLayout.addView(ctaView)

    rootFrame.addView(detailsLayout)
    adView.addView(rootFrame)

    // Register views with Google AdMob
    adView.mediaView = mediaView
    adView.headlineView = headlineView
    adView.bodyView = bodyView
    adView.iconView = iconView
    adView.callToActionView = ctaView

    populateTinderAdView(nativeAd, adView)

    return adView
}

private fun populateTinderAdView(nativeAd: NativeAd, adView: NativeAdView) {
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
