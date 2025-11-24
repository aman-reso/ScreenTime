package com.app.screentime.ui.atom

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.ui.theme.LocalAppColors


/**
 * App Usage List UI Component
 * Displays a list of app usage cards with conditional rounded corners
 * First item has top rounded corners, last item has bottom rounded corners
 */
@Composable
fun LazyItemScope.AppUsageListUi(
    appUsage: AppUsage,
    index: Int,
    totalCount: Int,
    onClick: (() -> Unit)? = null,
    showIcon: Boolean = true
) {
    val largeShape = androidx.compose.material3.MaterialTheme.shapes.large
    val shape = remember(index, totalCount, largeShape) {
        when {
            totalCount == 1 -> largeShape // Single item: all corners rounded
            index == 0 -> RoundedCornerShape(
                topStart = 24.dp, topEnd = 24.dp
            ) // First item: top rounded
            index == totalCount - 1 -> RoundedCornerShape(
                bottomStart = 24.dp, bottomEnd = 24.dp
            )

            else -> androidx.compose.foundation.shape.RoundedCornerShape(0.dp) // Middle items: no rounded corners
        }
    }

    AppUsageCard(
        appUsage = appUsage,
        modifier = Modifier.fillParentMaxWidth(),
        shape = shape,
        onClick = onClick,
        showIcon = showIcon
    )
}

/**
 * App Usage Card Component
 * Displays app icon, app name, network usage, and screen time
 */
@Composable
fun AppUsageCard(
    appUsage: AppUsage,
    modifier: Modifier = Modifier,
    shape: Shape = androidx.compose.material3.MaterialTheme.shapes.large,
    onClick: (() -> Unit)? = null,
    showIcon: Boolean = true
) {
    val colors = LocalAppColors.current ?: return
    AppUsageItemGlassyCard(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showIcon) {
                appUsage.applicationInfo?.let {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AppIcon(
                            appInfo = it, size = 28.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (!appUsage.appName.isNullOrBlank()) {
                    AppText(
                        text = appUsage.appName,
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (appUsage.wifiDataUsage >= 0 || appUsage.mobileDataUsage >= 0) {

                    Spacer(modifier = Modifier.padding(vertical = 2.dp))

                    val totalDataUsage =
                        remember(appUsage.wifiDataUsage, appUsage.mobileDataUsage) {
                            appUsage.wifiDataUsage + appUsage.mobileDataUsage
                        }
                    val dataUsageText = remember(totalDataUsage) {
                        if (totalDataUsage > 0) {
                            totalDataUsage.toReadableDataSize()
                        } else {
                            null
                        }
                    }

                    if (dataUsageText != null) {
                        AppText(
                            text = dataUsageText,
                            style = AppTextStyle.Label,
                            color = colors.textMuted
                        )
                    } else {
                        AppText(
                            text = stringResource(R.string.zero_bytes),
                            style = AppTextStyle.Label,
                            color = colors.textLight
                        )
                    }
                }
            }
            appUsage.displayFormatScreenTime?.let {
                AppText(
                    text = it, style = AppTextStyle.Body
                )
            }
        }
    }
}

@Composable
private fun AppUsageItemGlassyCard(
    modifier: Modifier = Modifier,
    shape: Shape = androidx.compose.material3.MaterialTheme.shapes.large,
    content: @Composable () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(shape)
            .border(
                1.dp, color = colors.border,
                shape = shape
            )
    ) {
        content()
    }
}
