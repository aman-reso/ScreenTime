package com.app.screentime.feature.newdesign.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R

/**
 * WinterModelsTopBar - Brand header with coin balance & notification action (Figma Node 81:1229 "Brand header").
 *
 * @param brandName The title displayed on the left (default "winter").
 * @param coinBalance The user's current coin balance (default 240).
 * @param onCoinBalanceClick Invoked when the coin pill is tapped.
 * @param onNotificationClick Invoked when the bell icon is tapped.
 */
@Composable
fun WinterModelsTopBar(
    modifier: Modifier = Modifier,
    brandName: String = "winter",
    coinBalance: Int = 240,
    onCoinBalanceClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Brand Title ("winter")
        Text(
            text = brandName,
            color = WinterTokens.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Pacifico,
            letterSpacing = (-0.5).sp
        )

        // Quick Actions Row (Coins balance + Bell icon)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coin Balance Pill
            val coinClickableModifier = if (onCoinBalanceClick != null) {
                Modifier.clickable(
                    role = Role.Button,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onCoinBalanceClick
                )
            } else {
                Modifier
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.Transparent)
                    .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(100.dp))
                    .then(coinClickableModifier)
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_coins),
                        contentDescription = "Coins",
                        tint = WinterTokens.actionPrimary,
                        modifier = Modifier.size(13.dp)
                    )

                    Text(
                        text = coinBalance.toString(),
                        color = WinterTokens.textPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp
                    )
                }
            }

            // Notification Bell Action
            val bellClickableModifier = if (onNotificationClick != null) {
                Modifier.clickable(
                    role = Role.Button,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onNotificationClick
                )
            } else {
                Modifier
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.neutral1000)
                    .border(1.dp, WinterTokens.borderSubtle, CircleShape)
                    .then(bellClickableModifier),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = "Notifications",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WinterModelsTopBarPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        WinterModelsTopBar(
            brandName = "winter",
            coinBalance = 240
        )
    }
}
