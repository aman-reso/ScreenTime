package com.app.screentime.feature.newdesign.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R

enum class CallType(
    val title: String,
    val ctaText: String,
    @get:DrawableRes val iconRes: Int
) {
    AUDIO("Audio call", "Start audio call ↗", R.drawable.ic_activity_phone),
    VIDEO("Video call", "Start video call ↗", R.drawable.ic_activity_video)
}

object WinterCallRateDefaults {
    val Height: Dp = 162.dp
    val CornerRadius: Dp = 20.dp
    val Padding: Dp = 16.dp

    // Figma Design Tokens (Node 51:1218 & 51:1235 "Luma / Call rate")
    val Background: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceCard
    val BorderColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.borderSubtle
    val IconTint: Color @Composable @ReadOnlyComposable get() = WinterTokens.actionPrimary
    val TitleColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.textSecondary
    val RateValueColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.textPrimary
    val RateUnitColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.textSecondary
    val CtaColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.textAccent
}

/**
 * WinterCallRateCard - Pure Jetpack Compose implementation of Figma "Luma / Call rate" (Node 51:1218).
 *
 * @param type Audio or Video call type.
 * @param coinsPerMinute Rate in coins per minute (e.g. 10 or 20).
 * @param onStartCall Callback when the card / CTA is clicked.
 * @param modifier Applied to the card container.
 */
@Composable
fun WinterCallRateCard(
    type: CallType,
    coinsPerMinute: Int,
    onStartCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(WinterCallRateDefaults.CornerRadius)

    Box(
        modifier = modifier
            .height(WinterCallRateDefaults.Height)
            .clip(shape)
            .background(WinterCallRateDefaults.Background, shape)
            .border(1.dp, WinterCallRateDefaults.BorderColor, shape)
            .clickable(
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onStartCall
            )
            .padding(WinterCallRateDefaults.Padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: Call Icon & Title
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(
                    painter = painterResource(id = type.iconRes),
                    contentDescription = type.title,
                    tint = WinterCallRateDefaults.IconTint,
                    modifier = Modifier.size(22.dp)
                )

                Text(
                    text = type.title,
                    color = WinterCallRateDefaults.TitleColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Middle: Rate numbers
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = coinsPerMinute.toString(),
                    color = WinterCallRateDefaults.RateValueColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 30.sp
                )

                Text(
                    text = "coins/min",
                    color = WinterCallRateDefaults.RateUnitColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom CTA
            Text(
                text = type.ctaText,
                color = WinterCallRateDefaults.CtaColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(name = "WinterCallRateCard Preview", showBackground = true)
@Composable
private fun WinterCallRateCardPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WinterCallRateCard(
                type = CallType.AUDIO,
                coinsPerMinute = 10,
                onStartCall = {},
                modifier = Modifier.weight(1f)
            )

            WinterCallRateCard(
                type = CallType.VIDEO,
                coinsPerMinute = 20,
                onStartCall = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

