package com.app.screentime.ui.atom

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
// Import the stringResource function and your R file
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.theme.LocalAppColors

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

    val totalDataUsage = wifiDataUsage + cellularDataUsage
    val wifiPercentage = if (totalDataUsage > 0) {
        (wifiDataUsage.toFloat() / totalDataUsage.toFloat() * 100f).toInt()
    } else 0
    val cellularPercentage = if (totalDataUsage > 0) {
        (cellularDataUsage.toFloat() / totalDataUsage.toFloat() * 100f).toInt()
    } else 0

    AppGlassyCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    // Use string resource for the title
                    text = stringResource(R.string.network_card_title),
                    style = AppTextStyle.Title,
                    color = colors.textPrimary
                )
                totalDataDisplayName?.let {
                    Spacer(modifier = Modifier.weight(1f))
                    AppText(
                        text = it,
                        style = AppTextStyle.Body,
                        color = colors.textPrimary
                    )
                }
            }

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
                imageVector = Icons.Rounded.Wifi
            )

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
                imageVector = Icons.Default.SwapVert
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NetworkUsageSection(
    label: String,
    percentage: Int,
    progress: Float,
    imageVector: ImageVector
) {
    var animatedTarget by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(progress) {
        animatedTarget = progress
    }

    val animatedProgress by
    animateFloatAsState(
        targetValue = animatedTarget,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    val colors = LocalAppColors.current ?: return
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            // Use string resource for content description
            contentDescription = stringResource(R.string.content_description_network_icon),
            tint = colors.tint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        AppText(
            text = label,
            style = AppTextStyle.Label,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.weight(1f))
        AppText(
            text = "$percentage%",
            style = AppTextStyle.Label,
            color = colors.textPrimary
        )
    }

    LinearWavyProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(androidx.compose.material3.MaterialTheme.shapes.extraSmall),
        color = colors.accent,
        trackColor = colors.textPrimary.copy(alpha = 0.3f)
    )
}
