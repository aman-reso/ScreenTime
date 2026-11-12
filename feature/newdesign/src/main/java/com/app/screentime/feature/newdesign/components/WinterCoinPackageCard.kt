package com.app.screentime.feature.newdesign.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

/**
 * Data model for a coin recharge package.
 */
data class WinterCoinPackage(
    val id: String,
    val coins: String,
    val price: String,
    val badge: String? = null
)

object WinterCoinPackageDefaults {
    val Width: Dp = 170.dp
    val Height: Dp = 175.dp
    val CornerRadius: Dp = 22.dp
    val Padding: Dp = 18.dp

    // Figma Design Tokens (Node 51:1640 "Luma / Coin package")
    val SelectedBackground: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceRaised
    val UnselectedBackground: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceCard
    val SelectedBorder: Color @Composable @ReadOnlyComposable get() = WinterTokens.borderSelected
    val UnselectedBorder: Color @Composable @ReadOnlyComposable get() = WinterTokens.borderSubtle
    val TextPrimary: Color @Composable @ReadOnlyComposable get() = WinterTokens.textPrimary
    val TextSecondary: Color @Composable @ReadOnlyComposable get() = WinterTokens.textSecondary
    val AccentPink: Color @Composable @ReadOnlyComposable get() = WinterTokens.textAccent
}

/**
 * WinterCoinPackageCard - Pure Jetpack Compose implementation of Figma "Luma / Coin package" (Node 51:1640).
 *
 * Exact specifications from Figma Dev Mode:
 * - Dimensions: 170dp (W) x 175dp (H)
 * - Corner Radius: 22dp
 * - Padding: 18dp
 * - Selected State: Background #302139, 2dp border #F2A4DA, Checkmark icon
 * - Unselected State: Background #241A30, 1dp border #48334F, Circle outline
 * - Coins Value: 26sp SemiBold, #FFF7FF
 * - Label: 12sp Regular, #BBAAC5
 * - Price: 17sp Medium, #FFF7FF
 * - Badge Text: 10sp Regular, #F2A4DA
 *
 * @param coins Number of coins (e.g. "500").
 * @param price Price tag (e.g. "$50.00").
 * @param selected Whether this package is currently selected.
 * @param onClick Callback invoked when tapped.
 * @param modifier Applied to the card box.
 * @param label Unit label below coins, defaults to "coins".
 * @param badge Optional badge text on bottom right (e.g. "Selected" or "Best value").
 */
@Composable
fun WinterCoinPackageCard(
    coins: String,
    price: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "coins",
    badge: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) WinterCoinPackageDefaults.SelectedBackground else WinterCoinPackageDefaults.UnselectedBackground,
        animationSpec = tween(durationMillis = 200),
        label = "CoinPackageBackground"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) WinterCoinPackageDefaults.SelectedBorder else WinterCoinPackageDefaults.UnselectedBorder,
        animationSpec = tween(durationMillis = 200),
        label = "CoinPackageBorder"
    )

    val shape = RoundedCornerShape(WinterCoinPackageDefaults.CornerRadius)
    val borderWidth = if (selected) 2.dp else 1.dp

    Box(
        modifier = modifier
            .height(WinterCoinPackageDefaults.Height)
            .clip(shape)
            .background(backgroundColor, shape)
            .border(borderWidth, borderColor, shape)
            .clickable(
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(WinterCoinPackageDefaults.Padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Coin Icon & Radio Check Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_coins),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(28.dp)
                )

                Icon(
                    painter = painterResource(
                        id = if (selected) R.drawable.ic_circle_checked else R.drawable.ic_circle_unselected
                    ),
                    contentDescription = if (selected) "Selected" else "Unselected",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Value Section: Coins & Unit
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = coins,
                    color = WinterCoinPackageDefaults.TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp
                )
                Text(
                    text = label,
                    color = WinterCoinPackageDefaults.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Row: Price & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = price,
                    color = WinterCoinPackageDefaults.TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )

                if (badge != null) {
                    Text(
                        text = badge,
                        color = WinterCoinPackageDefaults.AccentPink,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(name = "WinterCoinPackageCard - Grid Preview", showBackground = true)
@Composable
private fun WinterCoinPackageCardPreview() {
    var selectedPackageId by remember { mutableStateOf("03") }

    Surface(color = WinterTokens.backgroundCanvas) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WinterCoinPackageCard(
                coins = "100",
                price = "$10.00",
                selected = selectedPackageId == "01",
                onClick = { selectedPackageId = "01" },
                modifier = Modifier.weight(1f)
            )

            WinterCoinPackageCard(
                coins = "500",
                price = "$50.00",
                badge = if (selectedPackageId == "03") "Selected" else null,
                selected = selectedPackageId == "03",
                onClick = { selectedPackageId = "03" },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

