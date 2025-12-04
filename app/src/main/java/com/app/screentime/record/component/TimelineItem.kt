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
import com.telekom.odsystem.atoms.ODSText
import com.app.screentime.utils.DateUtils
import org.joda.time.DateTime
import org.joda.time.Duration
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.DSTextStyles

import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.neutralScheme

@Composable
fun TimelineItem(
    stat: AppUsageStatsData,
    isFirst: Boolean,
    isLast: Boolean,
    scheme: ODSTheme = neutralScheme
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

    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 12.dp), // Approximate MaterialTheme.shapes.medium
        padding = ODSPadding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val iconColor = remember(stat.packageName) {
                    val colorsList = listOf(
                        HexColor("#E1BEE7"), // Light purple
                        HexColor("#F8BBD0"), // Light pink
                        HexColor("#BBDEFB"), // Light blue
                        HexColor("#C5E1A5"), // Light green
                        HexColor("#FFE0B2"), // Light orange
                    )
                    colorsList[stat.packageName.hashCode().absoluteValue % colorsList.size]
                }

                ODSBox(
                    modifier = Modifier.size(24.dp),
                    background = listOf(ODSColorModel(iconColor)),
                    cornerRadius = ODSCorners(all = 12.dp), // Circle
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Default.CheckCircle,
                            tint = HexColor("#FFFFFF")
                        ),
                        width = 16.dp,
                        height = 16.dp
                    )
                }

                // App name and duration
                ODSColumn {
                    ODSText(
                        text = stat.appName,
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ODSText(
                        text = durationText,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicTextRecessive
                    )
                }
            }

            ODSColumn(
                horizontalAlignment = Alignment.End
            ) {
                ODSText(
                    text = startTime,
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )
                if (endTime.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    ODSText(
                        text = "→ $endTime",
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}

