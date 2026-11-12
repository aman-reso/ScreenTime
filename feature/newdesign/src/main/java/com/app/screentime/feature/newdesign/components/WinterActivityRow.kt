package com.app.screentime.feature.newdesign.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R

/**
 * Activity types supported by [WinterActivityRow] mapped to Figma variants.
 */
enum class WinterActivityType(@get:DrawableRes val iconRes: Int) {
    VIDEO_CALL(R.drawable.ic_activity_video),
    AUDIO_CALL(R.drawable.ic_activity_phone),
    WALLET_RECHARGE(R.drawable.ic_activity_plus),
    PAYOUT(R.drawable.ic_activity_arrow_up_right)
}

object WinterActivityRowDefaults {
    val Height = 70.dp
    val IconContainerSize = 42.dp
    val IconCornerRadius = 14.dp
    val IconSize = 19.dp

    // Figma Design Tokens (Node 51:1638 "Luma / Activity row")
    val IconBackground = WinterTokens.Default.surfaceCard
    val IconTint = WinterTokens.Default.iconAccent
    val TitleColor = WinterTokens.Default.textPrimary
    val SubtitleColor = WinterTokens.Default.textSecondary
    val DividerColor = WinterTokens.Default.borderSubtle
    val AmountSuccessColor = WinterTokens.Default.statusSuccess
    val AmountNeutralColor = WinterTokens.Default.textPrimary
}

/**
 * WinterActivityRow - Pure Jetpack Compose implementation of Figma "Luma / Activity row" (Node 51:1638).
 *
 * Exact specifications from Figma Dev Mode:
 * - Height: 70dp
 * - Icon container: 42dp x 42dp, corner radius 14dp, fill #241A30
 * - Icon: 19dp, stroke #D7BEFF
 * - Title: 14sp Medium, #FFF7FF
 * - Subtitle: 11sp Regular, #BBAAC5
 * - Amount / Status: 14sp SemiBold, #ACE2C6 (success/recharge) or #FFF7FF (call/payout)
 * - Divider: 1dp #48334F at the bottom
 *
 * @param title The primary title text (e.g. "Video call · Maya Rose").
 * @param subtitle The secondary time / details text (e.g. "Today, 7:40 PM · 8 min × 20 coins").
 * @param amountOrStatus The trailing value or status text (e.g. "−160", "+500").
 * @param type The [WinterActivityType] representing the activity icon.
 * @param isPositive Whether the amount is positive/credit (renders green #ACE2C6).
 * @param showDivider Whether to render the bottom border divider.
 * @param onClick Optional click listener for the row.
 */
@Composable
fun WinterActivityRow(
    title: String,
    subtitle: String,
    amountOrStatus: String,
    type: WinterActivityType,
    modifier: Modifier = Modifier,
    isPositive: Boolean = false,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(WinterActivityRowDefaults.Height)
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Activity Symbol Container
            Box(
                modifier = Modifier
                    .size(WinterActivityRowDefaults.IconContainerSize)
                    .clip(RoundedCornerShape(WinterActivityRowDefaults.IconCornerRadius))
                    .background(WinterActivityRowDefaults.IconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = type.iconRes),
                    contentDescription = null,
                    tint = WinterActivityRowDefaults.IconTint,
                    modifier = Modifier.size(WinterActivityRowDefaults.IconSize)
                )
            }

            // Title & Subtitle Column
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = WinterActivityRowDefaults.TitleColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    color = WinterActivityRowDefaults.SubtitleColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Trailing Amount / Status
            Text(
                text = amountOrStatus,
                color = if (isPositive) {
                    WinterActivityRowDefaults.AmountSuccessColor
                } else {
                    WinterActivityRowDefaults.AmountNeutralColor
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )
        }

        // Bottom Divider
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                thickness = 1.dp,
                color = WinterActivityRowDefaults.DividerColor
            )
        }
    }
}

@Preview(name = "WinterActivityRow - Variants Preview", showBackground = true)
@Composable
private fun WinterActivityRowPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        Column(modifier = Modifier.padding(16.dp)) {
            WinterActivityRow(
                title = "Video call · Maya Rose",
                subtitle = "Today, 7:40 PM · 8 min × 20 coins",
                amountOrStatus = "−160",
                type = WinterActivityType.VIDEO_CALL,
                isPositive = false
            )
            WinterActivityRow(
                title = "Audio call · Maya Rose",
                subtitle = "Yesterday, 4:15 PM · 12 min",
                amountOrStatus = "−120",
                type = WinterActivityType.AUDIO_CALL,
                isPositive = false
            )
            WinterActivityRow(
                title = "Wallet recharge",
                subtitle = "Nov 5, 2:10 PM · UPI / Card",
                amountOrStatus = "+500",
                type = WinterActivityType.WALLET_RECHARGE,
                isPositive = true
            )
            WinterActivityRow(
                title = "Payout completed",
                subtitle = "Nov 1, 10:00 AM · Bank transfer",
                amountOrStatus = "Completed",
                type = WinterActivityType.PAYOUT,
                isPositive = false,
                showDivider = false
            )
        }
    }
}
