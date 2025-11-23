package com.app.screentime.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.compose.foundation.layout.Box
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.AppColors
import com.app.screentime.utils.DateUtils

@Composable
fun TimelineItem(
    stat: AppUsageStatsData, isFirst: Boolean, isLast: Boolean, colors: AppColors
) {
    val cardShape = remember(isFirst, isLast) {
        when {
            isFirst && isLast -> RoundedCornerShape(12.dp) // Single item: all corners rounded
            isFirst -> RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp
            ) // First: top corners rounded
            isLast -> RoundedCornerShape(
                bottomStart = 12.dp,
                bottomEnd = 12.dp
            ) // Last: bottom corners rounded
            else -> RectangleShape
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(colors.card, cardShape)
    ) {
        val (topLine, indicator, bottomLine, content) = createRefs()

        // ------- Top Line -------
        if (!isFirst) {
            VerticalDivider(
                thickness = 2.dp,
                color = colors.success.copy(alpha = 0.3f),
                modifier = Modifier
                    .height(12.dp)
                    .constrainAs(topLine) {
                        top.linkTo(parent.top)
                        bottom.linkTo(indicator.top)
                        start.linkTo(indicator.start)
                        end.linkTo(indicator.end)
                    })
        }

        // ------- Indicator -------
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(colors.success.copy(alpha = 0.2f))
                .constrainAs(indicator) {
                    top.linkTo(if (isFirst) parent.top else topLine.bottom)
                    start.linkTo(parent.start)
                }, contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = colors.success,
                modifier = Modifier.size(16.dp)
            )
        }

        if (!isLast) {
            VerticalDivider(
                thickness = 2.dp,
                color = colors.success.copy(alpha = 0.3f),
                modifier = Modifier
                    .height(12.dp)
                    .constrainAs(bottomLine) {
                        top.linkTo(indicator.bottom)
                        start.linkTo(indicator.start)
                        end.linkTo(indicator.end)
                    })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
                .constrainAs(content) {
                    start.linkTo(indicator.end, margin = 12.dp)
                    end.linkTo(parent.end)
                    top.linkTo(indicator.top)
                    bottom.linkTo(indicator.bottom)
                    width = Dimension.fillToConstraints
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App name on the left
            AppText(
                text = stat.appName ?: "Unknown App",
                style = AppTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Exact time on the right
            val exactTime = remember(stat.eventTimestamp) {
                stat.eventTimestamp?.let { timestamp ->
                    try {
                        DateUtils.formatTime(timestamp)
                    } catch (e: Exception) {
                        timestamp // Fallback to original if parsing fails
                    }
                } ?: ""
            }

            AppText(
                text = exactTime,
                style = AppTextStyle.Label,
                color = colors.textSecondary,
                maxLines = 1
            )
        }
    }
}

