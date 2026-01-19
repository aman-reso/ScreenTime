package com.app.screentime.landing.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.landing.model.UsageDonutData
import com.app.screentime.landing.model.UsageSegment
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners

import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme

@Composable
fun UsageDonutComponent(
    usageDonutData: UsageDonutData?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    title: String? = null,
    scheme: ODSTheme = neutralScheme
) {
    if (usageDonutData == null || usageDonutData.segments.isEmpty()) return

    ODSColumn(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        ODSText(
            text = title ?: stringResource(R.string.today_screen_time),
            style = DSTextStyles.bodyMBold,
            color = scheme.basicText
        )
        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val maxSize = minOf(maxWidth, 170.dp)
            val donutSize = min(maxSize, 170.dp)

            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DonutChart(
                    data = usageDonutData.segments,
                    modifier = Modifier
                        .size(donutSize)
                        .clickable(onClick = onClick),
                    scheme = scheme,
                    centerContent = {
                        ODSText(
                            text = usageDonutData.formattedTotalTime,
                            style = DSTextStyles.subtitle,
                            color = scheme.basicText
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        UsageLegend(
            modifier = Modifier.fillMaxWidth(),
            data = usageDonutData.segments,
            scheme = scheme
        )
    }
}


@Composable
private fun DonutChart(
    data: List<UsageSegment>,
    modifier: Modifier = Modifier,
    centerContent: @Composable () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    ODSBox(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.minDimension - 20f) / 2f
            val strokeWidth = 60f // Better thickness for donut

            var startAngle = -90f

            data.forEach { usageData ->
                val sweepAngle = (usageData.percentage / 100f) * 360f * 1f

                drawArc(
                    color = usageData.color.getColor(),
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UsageLegend(
    data: List<UsageSegment>, modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme
) {
    ODSWrap(
        modifier = modifier.fillMaxWidth(),
        horizontalGap = DSVariables.spacingComponent3,
        verticalGap = DSVariables.spacingComponent2,
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Top
    ) {
        data.forEach { usageData ->
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSBox(
                    modifier = Modifier.size(12.dp),
                    background = listOf(ODSColorModel(usageData.color)),
                    cornerRadius = ODSCorners(all = 6.dp)
                ) {}
                Spacer(modifier = Modifier.width(DSVariables.spacingComponent2))
                ODSText(
                    text = usageData.name.take(8),
                    style = DSTextStyles.linkSRegular,
                    color = scheme.basicTextRecessive,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                ODSText(
                    text = " ${usageData.percentage.toInt()}%",
                    style = DSTextStyles.linkSRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }
    }
}


@Composable
fun CircularDonutText(time: String, scheme: ODSTheme = neutralScheme) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val maxSize = minOf(maxWidth, 170.dp)
        val donutSize = min(maxSize, 170.dp)

        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ODSBox(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = (size.minDimension - 20f) / 2f
                    val strokeWidth = 30f // Thickness for donut
                    drawArc(
                        color = lagoonSecondaryScheme.basicBackgroundCard.getColor(),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )
                }
                ODSText(
                    text = time,
                    style = DSTextStyles.subtitle,
                    color = scheme.basicText
                )
            }
        }
    }
}