package com.app.screentime.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.screentime.R
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppGlassyCard
import com.app.screentime.ui.theme.LocalAppColors

@Composable
fun SettingsItemCard(data: ProfileSettingsUi, onClick: () -> Unit = {}) {
    val colors = LocalAppColors.current ?: return
    AppGlassyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick.invoke()
                }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Show icon if provided, otherwise use default
                val iconVector = when {
                    data.key == "block_app" -> Icons.Default.Block
                    data.key == "set_app_limit" -> Icons.Default.Timer
                    data.key == "set_app_launch_limit" -> Icons.Default.PlayArrow
                    data.key == "ad_blocking" -> Icons.Default.AdsClick
                    data.key == "theme" -> Icons.Default.Palette
                    data.key == "language" -> Icons.Default.Language
                    data.key == "widget" -> Icons.Default.Widgets
                    data.key == "totp" -> Icons.Default.Security
                    data.key == "vpn_service" -> Icons.Default.VpnLock
                    data.key == "help_support" -> Icons.Default.Help
                    data.key == "privacy_policy" -> Icons.Default.PrivacyTip
                    data.key == "terms_of_service" -> Icons.Default.Description
                    data.text.contains("Privacy", ignoreCase = true) -> Icons.Default.PrivacyTip
                    data.text.contains("Terms", ignoreCase = true) -> Icons.Default.Description
                    else -> Icons.AutoMirrored.Filled.ShowChart
                }
                
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = when {
                        data.key == "block_app" -> colors.error
                        data.key == "set_app_limit" || data.key == "set_app_launch_limit" -> colors.success
                        data.key == "ad_blocking" -> colors.success
                        data.key == "theme" || data.key == "language" || data.key == "widget" -> colors.success
                        data.key == "totp" -> colors.accent
                        data.key == "vpn_service" -> colors.success
                        // About App section - use textSecondary for a more subtle look
                        data.key == "help_support" || data.key == "privacy_policy" || data.key == "terms_of_service" -> colors.textMuted
                        data.text.contains("Privacy", ignoreCase = true) -> colors.textMuted
                        data.text.contains("Terms", ignoreCase = true) -> colors.textMuted
                        else -> colors.textSecondary
                    },
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                )

                Spacer(Modifier.width(14.dp))

                AppText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = data.text,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    painter = painterResource(R.drawable.chevron_right),
                    contentDescription = null,
                    tint = colors.textLight,
                    modifier = Modifier
                        .width(10.dp)
                        .height(16.dp)
                )
            }
        }
    }
}