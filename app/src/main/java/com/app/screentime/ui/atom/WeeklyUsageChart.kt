package com.app.screentime.ui.atom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.screentime.landing.screen.DailyUsageEntry
import androidx.compose.ui.res.stringResource
import androidx.graphics.shapes.RoundedPolygon
import com.app.screentime.R
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.ui.theme.LocalAppColors

@Composable
fun WeeklyUsageChart(
    weeklyReports: List<com.app.screentime.data.uiModel.WeeklyDataReport>,
    selectedDayIndex: Int? = null,
    onBarClick: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Build chart entries from real weekly reports
    val weeklyUsage = weeklyReports.map {
        DailyUsageEntry(
            dayLabel = it.dayName!!.take(1),
            usageMinutes = (it.totalScreenTime!! / (1000 * 60)).toInt()
        )
    }
    val totalMinutes = weeklyUsage.sumOf { it.usageMinutes }
    val avgMinutes = if (weeklyUsage.isNotEmpty()) totalMinutes / weeklyUsage.size else 0

    val colors = LocalAppColors.current ?: return
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        AppText(
            text = stringResource(R.string.this_week),
            style = AppTextStyle.Body,
            color = colors.textPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        AppText(
            text = "Average ${formatDuration(avgMinutes.toLong() * 1000 * 60)}",
            style = AppTextStyle.Body,
            color = colors.error
        )
        Spacer(Modifier.height(16.dp))

        WeeklyUsageBarChart(
            usageEntries = weeklyUsage,
            selectedDayIndex = selectedDayIndex,
            onBarClick = onBarClick
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeeklyUsageBarChart(
    usageEntries: List<DailyUsageEntry>,
    selectedDayIndex: Int? = null,
    onBarClick: ((Int) -> Unit)? = null
) {
    val colors = LocalAppColors.current ?: return
    val numberOfYSteps = 4
    val maxMinutes = usageEntries.maxOfOrNull { it.usageMinutes } ?: 0
    val yStepValue = (maxMinutes / numberOfYSteps).coerceAtLeast(1)
    val scaleSteps = (0..numberOfYSteps).map { it * yStepValue }

    val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(bottom = 20.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                scaleSteps.forEachIndexed { index, _ ->
                    if (index == 0) {
                        return@forEachIndexed
                    }
                    val y = size.height - (size.height / numberOfYSteps) * index
                    drawLine(
                        color = colors.textLight,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f,
                        pathEffect = dashedEffect
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                usageEntries.forEachIndexed { index, entry ->
                    val barHeightRatio =
                        if (maxMinutes > 0) entry.usageMinutes.toFloat() / maxMinutes else 0f
                    val animatedHeight = (200 * barHeightRatio * 1).dp

                    val isSelected = selectedDayIndex == index
                    val barColor = if (isSelected) {
                        colors.success.copy(alpha = 0.9f)
                    } else {
                        colors.accent
                    }

                    val barModifier = if (onBarClick != null) {
                        Modifier
                            .fillMaxHeight()
                            .clickable { onBarClick(index) }
                    } else {
                        Modifier.fillMaxHeight()
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = barModifier
                    ) {
                        Column(
                            modifier = Modifier
                                .height(animatedHeight)
                                .width(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(barColor),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialShapes.Sunny.toShape()
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        AppText(
                            text = entry.dayLabel,
                            style = AppTextStyle.Label,
                            color = if (isSelected) colors.success else colors.textPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            scaleSteps.reversed().forEach { minuteValue ->
                if (minuteValue != 0) {
                    AppText(
                        text = formatDuration(minuteValue.toLong() * 1000 * 60),
                        style = AppTextStyle.Label,
                        color = colors.textPrimary
                    )
                } else {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }
        }
    }
}
