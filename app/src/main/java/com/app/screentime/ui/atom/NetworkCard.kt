package com.app.screentime.ui.atom

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.app.screentime.R
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode

/**
 * Network data usage card component
 * Displays WiFi and Cellular data usage with percentages and progress bars
 *
 * @param wifiDataUsage Total WiFi data usage in bytes
 * @param cellularDataUsage Total Cellular data usage in bytes
 * @param modifier Modifier for the card
 */
@Composable
fun NetworkCard(
    modifier: Modifier = Modifier,
    wifiDataUsage: Long = 0L,
    wifiDataUsageDisplay: String? = null,
    cellularDataUsage: Long = 0L,
    cellularDataUsageDisplay: String? = null,
    totalDataDisplayName: String? = null,
) {
    val colors = LocalAppColors.current ?: return
    val isDarkMode = LocalThemeMode.current

    val totalDataUsage = wifiDataUsage + cellularDataUsage
    val wifiPercentage = if (totalDataUsage > 0) {
        (wifiDataUsage.toFloat() / totalDataUsage.toFloat() * 100f).toInt()
    } else 0
    val cellularPercentage = if (totalDataUsage > 0) {
        (cellularDataUsage.toFloat() / totalDataUsage.toFloat() * 100f).toInt()
    } else 0

    // Card background: Surface container (#f0f4f9 light, #1e2124 dark)
    val cardBackground = if (isDarkMode) Color(0xFF1E2124) else Color(0xFFF0F4F9)
    // Border: Surface container high (#e9eef6 light, #282b2f dark)
    val borderColor = if (isDarkMode) Color(0xFF282B2F) else Color(0xFFE9EEF6)
    // Primary text: #1a1c1e (light), #e2e2e6 (dark)
    val primaryTextColor = if (isDarkMode) Color(0xFFE2E2E6) else Color(0xFF1A1C1E)
    // Secondary text: #43474e (light), #c4c7c5 (dark)
    val secondaryTextColor = if (isDarkMode) Color(0xFFC4C7C5) else Color(0xFF43474E)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = stringResource(R.string.network_card_title),
                style = AppTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )
            totalDataDisplayName?.let {
                AppText(
                    text = it,
                    style = AppTextStyle.Body,
                    color = primaryTextColor
                )
            }
        }

        // WiFi section
        NetworkUsageSection(
            label = buildString {
                append(stringResource(R.string.network_wifi_label))
                wifiDataUsageDisplay?.let {
                    append(" (")
                    append(it)
                    append(")")
                }
            },
            percentage = wifiPercentage,
            progress = wifiPercentage / 100f,
            imageVector = Icons.Rounded.Wifi,
            isWifi = true,
            isDarkMode = isDarkMode,
            primaryTextColor = primaryTextColor,
            secondaryTextColor = secondaryTextColor
        )

        // Cellular section
        NetworkUsageSection(
            label = buildString {
                append(stringResource(R.string.network_cellular_label))
                cellularDataUsageDisplay?.let {
                    append(" (")
                    append(it)
                    append(")")
                }
            },
            percentage = cellularPercentage,
            progress = cellularPercentage / 100f,
            imageVector = Icons.Default.SignalCellular4Bar,
            isWifi = false,
            isDarkMode = isDarkMode,
            primaryTextColor = primaryTextColor,
            secondaryTextColor = secondaryTextColor
        )
    }
}

@Composable
private fun NetworkUsageSection(
    label: String,
    percentage: Int,
    progress: Float,
    imageVector: ImageVector,
    isWifi: Boolean,
    isDarkMode: Boolean,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    var animatedTarget by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(progress) {
        animatedTarget = progress
    }

    val animatedProgress by animateFloatAsState(
        targetValue = animatedTarget,
        label = "network_progress"
    )

    // Progress bar colors
    // WiFi: #2563eb (light), #3b82f6 (dark)
    // Cellular: #10b981 (light), #34d399 (dark)
    // Inactive: #e5e7eb (light), #374151 (dark)
    val activeBarColor = if (isWifi) {
        if (isDarkMode) Color(0xFF3B82F6) else Color(0xFF2563EB)
    } else {
        if (isDarkMode) Color(0xFF34D399) else Color(0xFF10B981)
    }
    val inactiveBarColor = if (isDarkMode) Color(0xFF374151) else Color(0xFFE5E7EB)
    val glowColor = if (isWifi) {
        Color(0xFF2563EB).copy(alpha = 0.4f)
    } else {
        Color(0xFF10B981).copy(alpha = 0.4f)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = stringResource(R.string.content_description_network_icon),
                tint = primaryTextColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            AppText(
                text = label,
                style = AppTextStyle.Label,
                color = primaryTextColor
            )
            Spacer(modifier = Modifier.weight(1f))
            AppText(
                text = "$percentage%",
                style = AppTextStyle.Label,
                color = primaryTextColor
            )
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(inactiveBarColor)
        ) {
            // Active progress
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(activeBarColor)
            )
        }
    }
}
