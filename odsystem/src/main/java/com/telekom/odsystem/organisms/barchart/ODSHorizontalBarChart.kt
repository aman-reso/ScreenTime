package com.telekom.odsystem.organisms.barchart

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
Created by sarthakgupta on 17/09/25
 **/

@Suppress("All")
@Composable
internal fun ODSHorizontalBarChart(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    style: ODSBarChartStyle = ODSBarChartStyle(),
    props: ODSBarChartProps = ODSBarChartProps(),
    valueFormatter: (x: Double, y: Double) -> String = { x, y -> "x = $x, y = $y" },
    horizontalAxisFormatter: (Double) -> String = { it.toString() },
    onBarSelected: ((Int) -> Unit)? = null,
    onBarDeSelected: (() -> Unit)? = null,
) {
    ODSColumn(modifier = modifier) {
        var yAxisWidthDp by remember { mutableStateOf(0.dp) }
        var chartWidth by remember { mutableIntStateOf(0) }
        var scale by remember { mutableFloatStateOf(1f) }
        var availableHeight by remember { mutableFloatStateOf(0f) }

        val minScale = remember(
            props.barItemsList.size,
            style.barThickness,
            style.barSpacing,
            availableHeight
        ) {
            if (availableHeight > 0f && props.barItemsList.isNotEmpty()) {
                val totalContentHeight =
                    props.barItemsList.size * (style.barThickness.value + (style.barSpacing
                        ?: 0.dp).value)
                val minScaleNeeded = availableHeight / totalContentHeight
                minScaleNeeded.coerceAtLeast(0.5f)
            } else {
                0.5f
            }
        }
        // Calculate maximum scale so that a single bar fills the viewport height (no spacing considered for max zoom)
        val maxScale = remember(availableHeight, style.barThickness, minScale) {
            if (availableHeight > 0f) {
                val totalContentHeight = style.barThickness.value + (style.barSpacing ?: 0.dp).value
                val candidate = availableHeight / totalContentHeight
                // Ensure not lower than minScale
                candidate.coerceAtLeast(minScale)
            } else {
                // Fallback to previous cap if we don't yet know height
                3f
            }
        }
        val localDensity = LocalDensity.current
        val transformableState = rememberTransformableState { zoomChange, _, _ ->
            scale = (scale * zoomChange).coerceIn(minScale, maxScale)
        }
        // Adjust scale when limits change
        LaunchedEffect(minScale, maxScale) {
            if (scale < minScale) scale = minScale
            if (scale > maxScale) scale = maxScale
        }
        if (props.showTopLabels) {
            ODSXAxisLabels(
                scheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (props.showLeftLabels) yAxisWidthDp + 8.dp else 0.dp,
                        bottom = 8.dp,
                        end = if (props.showRightLabels) yAxisWidthDp + 8.dp else 0.dp
                    ),
                chartWidth = chartWidth.toFloat(),
                props = props,
                formatter = horizontalAxisFormatter,
                style = style
            )
        }
        ODSRow(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned { coordinates ->
                    availableHeight = with(localDensity) { coordinates.size.height.toDp().value }
                }) {
            val scrollState = rememberScrollState()
            if (props.showLeftLabels) {
                ODSYAxisLabels(
                    scheme,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 8.dp)
                        .verticalScroll(scrollState, props.scrollEnabled)
                        .onGloballyPositioned { coordinates ->
                            yAxisWidthDp = with(localDensity) {
                                coordinates.size.width.toDp()
                            }
                        },
                    props = props,
                    style = style,
                    scale = scale
                )
            }
            var selectedBarIndex by remember { mutableIntStateOf(-1) }
            var isDragging by remember { mutableStateOf(false) }
            var lastPressedIndex by remember { mutableIntStateOf(-1) }
            ODSBox(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .selectiveBorder(
                        style = style, props = props
                    )
                    .clipToBounds()
                    .transformable(transformableState, enabled = props.zoomEnabled)
                    .pointerInput(
                        scale,
                        props.barItemsList.size,
                        style.barThickness,
                        style.barSpacing,
                        selectedBarIndex
                    ) {
                        val barSlotHeightPx =
                            ((style.barThickness + (style.barSpacing ?: 0.dp)) * scale).toPx()
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val changes = event.changes
                                if (changes.isEmpty()) continue
                                if (changes.size > 1) {
                                    // Multi-touch detected, clear selection and dragging state
                                    isDragging = false
                                    continue
                                }
                                val change = changes.first()

                                when (event.type) {
                                    PointerEventType.Press -> {
                                        isDragging = false
                                        val globalY = scrollState.value + change.position.y
                                        val index = (globalY / barSlotHeightPx).toInt()
                                        lastPressedIndex = index

                                        selectedBarIndex =
                                            if (index in props.barItemsList.indices) {
                                                // Only toggle if pressing the same bar that's already selected
                                                if (index == selectedBarIndex) -1 else index
                                            } else {
                                                // Clicked outside valid bar area, deselect
                                                -1
                                            }
                                        if (selectedBarIndex != -1) {
                                            onBarSelected?.invoke(selectedBarIndex)
                                        } else {
                                            onBarDeSelected?.invoke()
                                        }
                                    }

                                    PointerEventType.Move -> {
                                        val globalY = scrollState.value + change.position.y
                                        val index = (globalY / barSlotHeightPx).toInt()

                                        // Only consider it dragging if we've moved significantly
                                        if (!isDragging && kotlin.math.abs(index - lastPressedIndex) > 0) {
                                            isDragging = true
                                        }

                                        // Update selection during drag only if we're actually dragging
                                        if (isDragging && index in props.barItemsList.indices && index != selectedBarIndex) {
                                            selectedBarIndex = index
                                            onBarSelected?.invoke(index)
                                        }
                                    }

                                    PointerEventType.Release -> {
                                        isDragging = false
                                        lastPressedIndex = -1
                                    }

                                    else -> Unit
                                }
                            }
                        }
                    }
                    .verticalScroll(scrollState, props.scrollEnabled)) {
                val context = LocalContext.current
                val canvasHeightDp =
                    (props.barItemsList.size * (style.barThickness.value + (style.barSpacing
                        ?: 0.dp).value) * scale).dp
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(canvasHeightDp)
                        .onGloballyPositioned { coordinates ->
                            chartWidth = coordinates.size.width
                        },
                ) {
                    drawGrid(
                        style, size.width, scale, props.barItemsList.size, props
                    )

                    drawHorizontalBars(
                        props.barItemsList.map { it.xValue ?: 0.0 },
                        style,
                        scale,
                        selectedBarIndex,
                        props
                    )

                    if (selectedBarIndex >= 0 && selectedBarIndex < props.barItemsList.size) {
                        val maxValue = props.barItemsList.maxOf { it.xValue ?: 0.0 }
                        drawTooltip(
                            context = context,
                            scheme = scheme,
                            barData = props.barItemsList[selectedBarIndex],
                            barIndex = selectedBarIndex,
                            style = style,
                            valueFormatter = valueFormatter,
                            scale = scale,
                            maxValue = maxValue,
                            props = props
                        )
                    }
                }
            }
            if (props.showRightLabels) {
                ODSYAxisLabels(
                    scheme,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .wrapContentWidth()
                        .verticalScroll(scrollState, props.scrollEnabled),
                    props = props,
                    style = style,
                    scale = scale
                )
            }
        }
        if (props.showBottomLabels) {
            ODSXAxisLabels(
                scheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (props.showLeftLabels) yAxisWidthDp + 8.dp else 0.dp,
                        top = 8.dp,
                        end = if (props.showRightLabels) yAxisWidthDp + 8.dp else 0.dp
                    ),
                chartWidth = chartWidth.toFloat(),
                props = props,
                formatter = horizontalAxisFormatter,
                style = style
            )
        }
    }
}

@Composable
private fun ODSYAxisLabels(
    scheme: ODSTheme,
    modifier: Modifier,
    props: ODSBarChartProps,
    style: ODSBarChartStyle,
    scale: Float,
) {
    ODSColumn(
        modifier = modifier,
    ) {
        props.barItemsList.forEach { barData ->
            ODSText(
                text = barData.yLabel,
                style = style.labelTextStyle,
                color = scheme.shadesNeutralShades600,
                textAlign = if (props.showLeftLabels) TextAlign.End else TextAlign.Start,
                maxLines = 2,
                modifier = Modifier
                    .height(
                        ((style.barThickness.value + (style.barSpacing ?: 0.dp).value) * scale).dp
                    )
                    .wrapContentHeight(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
private fun ODSXAxisLabels(
    scheme: ODSTheme,
    modifier: Modifier,
    chartWidth: Float,
    props: ODSBarChartProps,
    formatter: (Double) -> String,
    style: ODSBarChartStyle,
) {
    ODSBox(modifier = modifier) {
        val (gridCount, gridStep) = getGridSteps(props = props)
        val maxValue = props.barItemsList.maxOfOrNull { it.xValue ?: 1.0 } ?: 1.0
        val (labels, count) = remember(maxValue, gridCount, gridStep) {
            calculateXAxisLabelsAndCount(
                maxValue = maxValue,
                gridStepCount = gridCount,
                gridStepValue = gridStep,
                formatter = formatter
            )
        }
        repeat(count) { index ->
            val x = (chartWidth / (count - 1)) * index
            ODSText(
                text = labels[index],
                style = style.labelTextStyle,
                maxLines = 1,
                color = scheme.shadesNeutralShades600,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable =
                        measurable.measure(constraints.copy(maxWidth = (chartWidth / count).toInt()))
                    val xPos = (x - (placeable.width / 2)).roundToInt()
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(x = xPos, y = 0)
                    }
                }
            )
        }
    }
}

private fun calculateXAxisLabelsAndCount(
    maxValue: Double,
    gridStepCount: Int,
    gridStepValue: Double?,
    formatter: (Double) -> String,
): Pair<List<String>, Int> {
    return if (gridStepValue != null && gridStepValue > 0f) {
        val steps = ceil(maxValue / gridStepValue).toInt()
        val count = steps + 1
        val labels = List(count) { i -> formatter(gridStepValue * i) }
        labels to count
    } else {
        val step = if (gridStepCount > 1) maxValue / (gridStepCount - 1) else maxValue
        val labels = List(gridStepCount) { i -> formatter(step * i) }
        labels to gridStepCount
    }
}

private fun Modifier.selectiveBorder(
    style: ODSBarChartStyle,
    props: ODSBarChartProps,
): Modifier = this.drawBehind {
    val (_, _) = getGridSteps(props = props)
    val color = style.guideLineColor ?: Color.Transparent
    val strokePx = style.guideLineThickness?.toPx() ?: 1.dp.toPx()
    val width = size.width
    val height = size.height
    if (props.showTopLabels) {
        drawLine(
            color = color, start = Offset(0f, 0f), end = Offset(width, 0f), strokeWidth = strokePx
        )
    }
    if (props.showBottomLabels) {
        drawLine(
            color = color,
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = strokePx
        )
    }
    if (props.showLeftLabels) {
        drawLine(
            color = color, start = Offset(0f, 0f), end = Offset(0f, height), strokeWidth = strokePx
        )
    }
    if (props.showRightLabels) {
        drawLine(
            color = color,
            start = Offset(width, 0f),
            end = Offset(width, height),
            strokeWidth = strokePx
        )
    }
}

private fun DrawScope.drawGrid(
    style: ODSBarChartStyle,
    chartWidth: Float,
    scale: Float,
    barCount: Int,
    props: ODSBarChartProps,
) {
    val (gridCount, _) = getGridSteps(props = props)
    val gridColor = style.guideLineColor ?: Color.Transparent
    val strokeWidth = 1.dp.toPx()

    // Draw vertical grid lines
    if (props.showTopLabels || props.showBottomLabels) {
        repeat(gridCount) { index ->
            val x = (chartWidth / (gridCount - 1)) * index
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = strokeWidth,
            )
        }
    }

    // Draw horizontal grid lines through the center of each bar
    if (props.showLeftLabels || props.showRightLabels) {
        val barThickness = (style.barThickness * scale).toPx()
        val barSpacing = ((style.barSpacing ?: 0.dp) * scale).toPx()
        val totalbarThickness = barThickness + barSpacing
        repeat(barCount) { index ->
            val centerY = index * totalbarThickness + (barSpacing / 2f) + (barThickness / 2f)
            if (centerY in 0f..size.height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, centerY),
                    end = Offset(chartWidth, centerY),
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
}

private fun getGridSteps(
    props: ODSBarChartProps,
): Pair<Int, Double> {

    val values = props.barItemsList.mapNotNull { it.xValue }.ifEmpty { listOf(1.0) }
    val maxValue = values.maxOrNull() ?: 1.0
    props.stepValue?.takeIf { it > 0.0 }?.let { stepValue ->
        val steps = ceil(maxValue / stepValue).toInt()
        return steps + 1 to stepValue
    }
    // If user sets stepCount → fallback to that
    props.stepCount?.let { count ->
        val stepValue = if (count > 1) maxValue / (count - 1) else maxValue
        return count to stepValue
    }

    return Pair(0, 0.0)
}

private fun DrawScope.drawHorizontalBars(
    data: List<Double>,
    style: ODSBarChartStyle,
    scale: Float = 1f,
    selectedBarIndex: Int = -1,
    props: ODSBarChartProps,
) {
    val (gridCount, gridStep) = getGridSteps(props = props)
    val barHeight = (style.barThickness * scale).toPx()
    val barSpacing = ((style.barSpacing ?: 0.dp) * scale).toPx()
    val totalBarHeight = barHeight + barSpacing
    val chartWidth = size.width
    val radius = when (props.shape) {
        ODSBarItemShape.PILLED -> (style.barCornerRadius * scale)
        ODSBarItemShape.SQUARED -> 6.dp
    }
    val cornerRadius = radius.toPx()

    // Calculate the maximum value that the grid represents
    val gridMaxValue = if (gridStep > 0.0 && gridCount > 0) {
        gridStep * (gridCount - 1) // The rightmost grid line represents this value
    } else {
        data.maxOrNull() ?: 1.0
    }

    // Draw bars
    data.forEachIndexed { index, value ->
        // Calculate bar width based on the grid coordinate system
        val barWidth = if (gridMaxValue > 0) (value / gridMaxValue) * chartWidth else 0f
        // Center the bar within its allocated space (between grid lines)
        val barY = index * totalBarHeight + (barSpacing / 2)

        // Data bar with rounded corners
        if (value > 0) {
            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = 0f,
                        top = barY,
                        right = barWidth.toFloat(),
                        bottom = barY + barHeight,
                        cornerRadius = CornerRadius(cornerRadius),
                    ),
                )
            }
            drawPath(
                path = path,
                color = (if (index == selectedBarIndex) style.selectedBarBackground else style.unselectedBarBackground)
                    ?: Color.Transparent,
            )
        }
    }
}

@Suppress("All")
private fun DrawScope.drawTooltip(
    context: Context,
    scheme: ODSTheme,
    barData: ODSBarItemProps,
    barIndex: Int,
    style: ODSBarChartStyle,
    valueFormatter: (x: Double, y: Double) -> String,
    scale: Float,
    maxValue: Double,
    props: ODSBarChartProps, // Add props parameter
) {
    val (gridCount, gridStep) = getGridSteps(props = props) // Use the actual props
    val barThickness = (style.barThickness * scale).toPx()
    val barSpacing = ((style.barSpacing ?: 0.dp) * scale).toPx()
    val totalbarThickness = barThickness + barSpacing
    // Use the same centered positioning as in drawHorizontalBars
    val barY = barIndex * totalbarThickness + (barSpacing / 2)
    val barCenterY = barY + barThickness / 2

    val chartWidth = size.width

    // Calculate the maximum value that the grid represents (same as in drawHorizontalBars)
    val gridMaxValue = if (gridStep > 0.0 && gridCount > 0) {
        gridStep * (gridCount - 1) // The rightmost grid line represents this value
    } else {
        maxValue
    }

    // Calculate bar width using the same grid coordinate system as bars
    val barWidth = ((barData.xValue ?: 0.0) / gridMaxValue) * chartWidth
    val barFillsWidth = barWidth >= chartWidth - 0.5f // tolerance

    val tooltipText = valueFormatter(barData.xValue ?: 0.0, barIndex.toDouble())
    val textPaint = Paint().apply {
        style.tooltipTextStyle?.let {
            textSize = it.fontSize.sp.toPx()
            val typeface = ResourcesCompat.getFont(context, it.fontFamily)
            setTypeface(typeface)
        }
        color = scheme.basicTextOnAccentSecondary.getColor().hashCode()
        isAntiAlias = true
    }

    val textBounds = Rect()
    textPaint.getTextBounds(tooltipText, 0, tooltipText.length, textBounds)
    val textWidth = textBounds.width().toFloat()
    val textHeight = textBounds.height().toFloat()

    val tooltipWidthPadding = DSVariables.spacingComponent4.toPx()
    val tooltipHeightPadding = DSVariables.spacingComponent3.toPx()
    val tooltipWidth = textWidth + (tooltipWidthPadding * 2)
    val tooltipHeight = textHeight + (tooltipHeightPadding * 2)
    val cornerRadius = 4.dp.toPx()
    val arrowSize = 8.dp.toPx()

    val spacePadding = 16.dp.toPx()
    val availableSpace = size.width - (barWidth + spacePadding)
    val tooltipFits = availableSpace >= tooltipWidth + arrowSize + spacePadding

    val arrowY = barCenterY

    if (barFillsWidth) {
        // ── Case 1: Bar fills full width → place tooltip at the end of the Cartesian axis with RIGHT arrow ──
        val chartAxisEnd = chartWidth // This is where the rightmost grid line is
        val tooltipX = chartAxisEnd - tooltipWidth - arrowSize
        val tooltipY = (barCenterY - tooltipHeight / 2).coerceIn(0f, size.height - tooltipHeight)

        val tooltipPath = Path().apply {
            moveTo(tooltipX + cornerRadius, tooltipY)
            lineTo(tooltipX + tooltipWidth - cornerRadius, tooltipY)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX + tooltipWidth - cornerRadius * 2,
                    tooltipY,
                    tooltipX + tooltipWidth,
                    tooltipY + cornerRadius * 2,
                ), 270f, 90f, false
            )
            // Arrow RIGHT
            lineTo(tooltipX + tooltipWidth, arrowY - arrowSize / 2)
            lineTo(tooltipX + tooltipWidth + arrowSize / 2, arrowY)
            lineTo(tooltipX + tooltipWidth, arrowY + arrowSize / 2)
            lineTo(tooltipX + tooltipWidth, tooltipY + tooltipHeight - cornerRadius)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX + tooltipWidth - cornerRadius * 2,
                    tooltipY + tooltipHeight - cornerRadius * 2,
                    tooltipX + tooltipWidth,
                    tooltipY + tooltipHeight,
                ), 0f, 90f, false
            )
            lineTo(tooltipX + cornerRadius, tooltipY + tooltipHeight)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX,
                    tooltipY + tooltipHeight - cornerRadius * 2,
                    tooltipX + cornerRadius * 2,
                    tooltipY + tooltipHeight,
                ), 90f, 90f, false
            )
            lineTo(tooltipX, tooltipY + cornerRadius)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX,
                    tooltipY,
                    tooltipX + cornerRadius * 2,
                    tooltipY + cornerRadius * 2,
                ), 180f, 90f, false
            )
            close()
        }
        drawPath(tooltipPath, style.tooltipBackground ?: Color.Transparent)

        // No need to draw line for full-width bars

        drawContext.canvas.nativeCanvas.drawText(
            tooltipText,
            tooltipX + tooltipWidthPadding,
            tooltipY + tooltipHeight / 2 + textHeight / 3,
            textPaint,
        )
    } else if (!tooltipFits) {
        // ── Case 2: Bar not full width but tooltip doesn't fit → place tooltip at the end of the Cartesian axis with LEFT arrow ──
        val chartAxisEnd = chartWidth // This is where the rightmost grid line is
        val tooltipX = chartAxisEnd - tooltipWidth
        val tooltipY = (barCenterY - tooltipHeight / 2).coerceIn(0f, size.height - tooltipHeight)

        val tooltipPath = Path().apply {
            moveTo(tooltipX + cornerRadius, tooltipY)
            lineTo(tooltipX + tooltipWidth - cornerRadius, tooltipY)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX + tooltipWidth - cornerRadius * 2,
                    tooltipY,
                    tooltipX + tooltipWidth,
                    tooltipY + cornerRadius * 2,
                ), 270f, 90f, false
            )
            lineTo(tooltipX + tooltipWidth, tooltipY + tooltipHeight - cornerRadius)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX + tooltipWidth - cornerRadius * 2,
                    tooltipY + tooltipHeight - cornerRadius * 2,
                    tooltipX + tooltipWidth,
                    tooltipY + tooltipHeight,
                ), 0f, 90f, false
            )
            lineTo(tooltipX + cornerRadius, tooltipY + tooltipHeight)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX,
                    tooltipY + tooltipHeight - cornerRadius * 2,
                    tooltipX + cornerRadius * 2,
                    tooltipY + tooltipHeight,
                ), 90f, 90f, false
            )
            // Arrow LEFT
            lineTo(tooltipX, arrowY + arrowSize / 2)
            lineTo(tooltipX - arrowSize / 2, arrowY)
            lineTo(tooltipX, arrowY - arrowSize / 2)
            lineTo(tooltipX, tooltipY + cornerRadius)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX,
                    tooltipY,
                    tooltipX + cornerRadius * 2,
                    tooltipY + cornerRadius * 2,
                ), 180f, 90f, false
            )
            close()
        }
        drawPath(tooltipPath, style.tooltipBackground ?: Color.Transparent)

        drawLine(
            color = style.tooltipBackground ?: Color.Transparent,
            start = Offset(barWidth.toFloat(), barCenterY),
            end = Offset(tooltipX - arrowSize / 3, arrowY),
            strokeWidth = (style.tooltipLineThickness ?: 1.dp).toPx(),
        )

        drawContext.canvas.nativeCanvas.drawText(
            tooltipText,
            tooltipX + tooltipWidthPadding,
            tooltipY + tooltipHeight / 2 + textHeight / 3,
            textPaint,
        )
    } else {
        // ── Case 3: Normal fit → place tooltip on RIGHT side with connecting line ──
        val tooltipX = size.width - tooltipWidth
        val tooltipY = (barCenterY - tooltipHeight / 2).coerceIn(0f, size.height - tooltipHeight)

        val tooltipPath = Path().apply {
            moveTo(tooltipX + cornerRadius, tooltipY)
            lineTo(tooltipX + tooltipWidth - cornerRadius, tooltipY)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX + tooltipWidth - cornerRadius * 2,
                    tooltipY,
                    tooltipX + tooltipWidth,
                    tooltipY + cornerRadius * 2,
                ), 270f, 90f, false
            )
            lineTo(tooltipX + tooltipWidth, tooltipY + tooltipHeight - cornerRadius)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX + tooltipWidth - cornerRadius * 2,
                    tooltipY + tooltipHeight - cornerRadius * 2,
                    tooltipX + tooltipWidth,
                    tooltipY + tooltipHeight,
                ), 0f, 90f, false
            )
            lineTo(tooltipX + cornerRadius, tooltipY + tooltipHeight)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX,
                    tooltipY + tooltipHeight - cornerRadius * 2,
                    tooltipX + cornerRadius * 2,
                    tooltipY + tooltipHeight,
                ), 90f, 90f, false
            )
            lineTo(tooltipX, arrowY + arrowSize / 2)
            lineTo(tooltipX - arrowSize / 2, arrowY)
            lineTo(tooltipX, arrowY - arrowSize / 2)
            lineTo(tooltipX, tooltipY + cornerRadius)
            arcTo(
                androidx.compose.ui.geometry.Rect(
                    tooltipX,
                    tooltipY,
                    tooltipX + cornerRadius * 2,
                    tooltipY + cornerRadius * 2,
                ), 180f, 90f, false
            )
            close()
        }
        drawPath(tooltipPath, style.tooltipBackground ?: Color.Transparent)

        drawLine(
            color = style.tooltipBackground ?: Color.Transparent,
            start = Offset(barWidth.toFloat(), barCenterY),
            end = Offset(tooltipX - arrowSize / 3, arrowY),
            strokeWidth = (style.tooltipLineThickness ?: 1.dp).toPx(),
        )

        drawContext.canvas.nativeCanvas.drawText(
            tooltipText,
            tooltipX + tooltipWidthPadding,
            tooltipY + tooltipHeight / 2 + textHeight / 3,
            textPaint,
        )
    }
}
