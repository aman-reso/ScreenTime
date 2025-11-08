package com.app.screentime.landing.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.ui.atom.AppGlassyCard
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.lightTextColor

@Composable
fun UsageDonutComponent(
    report: List<AppUsage>?,
    totalScreenTime: Long,
    modifier: Modifier = Modifier
) {
    val usageData = report?.toUsageData() ?: return

    Column(modifier = modifier.fillMaxWidth()) {
        AppText(text = stringResource(R.string.today_screen_time), style = AppTextStyle.SubTitle)
        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val maxSize = minOf(maxWidth, 200.dp)
            val donutSize = min(maxSize, 200.dp)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DonutChart(
                    data = usageData,
                    modifier = Modifier.size(donutSize),
                    centerContent = {
                        AppText(text = formatTotalTime(totalScreenTime))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                UsageLegend(
                    modifier = Modifier.fillMaxWidth(),
                    data = usageData
                )
            }
        }
    }
}


@Composable
private fun DonutChart(
    data: List<UsageData>, modifier: Modifier = Modifier, centerContent: @Composable () -> Unit = {}
) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f, animationSpec = tween(
                durationMillis = 1000, easing = FastOutLinearInEasing
            )
        )
    }

    Box(
        modifier = modifier.size(200.dp), contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.minDimension - 20f) / 2f
            val strokeWidth = 60f // Better thickness for donut

            var startAngle = -90f

            data.forEach { usageData ->
                val sweepAngle = (usageData.percentage / 100f) * 360f * animationProgress.value

                drawArc(
                    color = usageData.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(
                        width = strokeWidth, cap = StrokeCap.Round
                    )
                )

                startAngle += sweepAngle
            }
        }
        centerContent()
    }
}


@Composable
private fun UsageLegend(
    data: List<UsageData>, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Group data into chunks of 3 items per row
        data.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { usageData ->
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(usageData.color)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        AppText(
                            text = usageData.name.take(12),
                            style = AppTextStyle.Label,
                            color = lightTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        AppText(
                            text = "${usageData.percentage.toInt()}%",
                            style = AppTextStyle.Label,
                            color = lightTextColor
                        )
                    }
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


data class UsageData(
    val name: String, val percentage: Float, val color: Color
)

private fun List<AppUsage>.toUsageData(): List<UsageData> {
    if (isEmpty()) return emptyList()

    val total = sumOf { it.appScreenTime }.coerceAtLeast(1L)
    val palette = listOf(
        Color(0xFF4CAF50), // green
        Color(0xFF2196F3), // blue
        Color(0xFFE91E63), // pink
        Color(0xFFFF9800), // orange
        Color(0xFF9C27B0), // purple
        Color(0xFF00BCD4), // cyan
        Color(0xFF8BC34A)  // light green
    )

    val sorted = this.sortedByDescending { it.appScreenTime }
    val top = sorted.take(5)
    val othersTime = sorted.drop(5).sumOf { it.appScreenTime }

    val topSegments = top.mapIndexed { index, app ->
        val percent = (app.appScreenTime.toFloat() / total.toFloat()) * 100f
        UsageData(app.appName ?: app.packageName.orEmpty(), percent, palette[index % palette.size])
    }

    val withOthers = if (othersTime > 0) {
        topSegments + UsageData(
            "Others", (othersTime.toFloat() / total.toFloat()) * 100f, Color(0xFF9E9E9E)
        )
    } else topSegments
    return withOthers.filter { it.percentage > 0.1f }
}

private fun formatTotalTime(totalMs: Long): String {
    val totalMinutes = totalMs / (1000 * 60)
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}