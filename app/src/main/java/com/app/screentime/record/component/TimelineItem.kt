package com.app.screentime.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.AppColors
import com.app.screentime.utils.DateUtils
import org.joda.time.DateTime
import org.joda.time.Duration

@Composable
fun TimelineItem(
    stat: AppUsageStatsData, isFirst: Boolean, isLast: Boolean, colors: AppColors
) {
    // Calculate duration and times
    val (startTime, endTime, durationText) = remember(stat.eventTimestamp, stat.duration) {
        val start = stat.eventTimestamp?.let { timestamp ->
            try {
                DateUtils.parseISO8601(timestamp)
            } catch (e: Exception) {
                null
            }
        }

        val duration = stat.duration ?: 0L
        val end = start?.plus(Duration(duration))

        val startTimeStr = start?.let { DateUtils.format(it, "HH:mm") } ?: ""
        val endTimeStr = end?.let { DateUtils.format(it, "HH:mm") } ?: ""

        // Format duration as "Duration: Xm" or "Duration: Xh Ym"
        val durationMinutes = duration / (1000 * 60)
        val durationText = when {
            durationMinutes >= 60 -> {
                val hours = durationMinutes / 60
                val minutes = durationMinutes % 60
                if (minutes > 0) {
                    "Duration: ${hours}h ${minutes}m"
                } else {
                    "Duration: ${hours}h"
                }
            }

            durationMinutes > 0 -> "Duration: ${durationMinutes}m"
            else -> "Duration: 0m"
        }

        Triple(startTimeStr, endTimeStr, durationText)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(colors.card)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val iconColor = remember(stat.packageName) {
                    val colorsList = listOf(
                        androidx.compose.ui.graphics.Color(0xFFE1BEE7), // Light purple
                        androidx.compose.ui.graphics.Color(0xFFF8BBD0), // Light pink
                        androidx.compose.ui.graphics.Color(0xFFBBDEFB), // Light blue
                        androidx.compose.ui.graphics.Color(0xFFC5E1A5), // Light green
                        androidx.compose.ui.graphics.Color(0xFFFFE0B2), // Light orange
                    )
                    colorsList[stat.packageName.hashCode().absoluteValue % colorsList.size]
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // App name and duration
                Column {
                    AppText(
                        text = stat.appName,
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AppText(
                        text = durationText,
                        style = AppTextStyle.Label,
                        color = colors.textSecondary
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                AppText(
                    text = startTime,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary
                )
                if (endTime.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    AppText(
                        text = "→ $endTime",
                        style = AppTextStyle.Label,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}

