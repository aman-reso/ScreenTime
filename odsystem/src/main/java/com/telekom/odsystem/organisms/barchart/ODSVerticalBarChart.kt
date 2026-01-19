@file:Suppress("TopLevelPropertyNaming")

package com.telekom.odsystem.organisms.barchart

import android.graphics.Typeface
import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.telekom.odsystem.atoms.chartannotation.odsChartAnnotation
import com.telekom.odsystem.charts.compose.cartesian.CartesianChartHost
import com.telekom.odsystem.charts.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.telekom.odsystem.charts.compose.cartesian.axis.rememberBottom
import com.telekom.odsystem.charts.compose.cartesian.axis.rememberEnd
import com.telekom.odsystem.charts.compose.cartesian.axis.rememberStart
import com.telekom.odsystem.charts.compose.cartesian.axis.rememberTop
import com.telekom.odsystem.charts.compose.cartesian.layer.rememberColumnCartesianLayer
import com.telekom.odsystem.charts.compose.cartesian.rememberCartesianChart
import com.telekom.odsystem.charts.compose.cartesian.rememberVicoScrollState
import com.telekom.odsystem.charts.compose.cartesian.rememberVicoZoomState
import com.telekom.odsystem.charts.compose.common.component.rememberLineComponent
import com.telekom.odsystem.charts.compose.common.component.rememberTextComponent
import com.telekom.odsystem.charts.compose.common.fill
import com.telekom.odsystem.charts.compose.common.insets
import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.axis.HorizontalAxis
import com.telekom.odsystem.charts.core.cartesian.axis.VerticalAxis
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModelProducer
import com.telekom.odsystem.charts.core.cartesian.data.CartesianValueFormatter
import com.telekom.odsystem.charts.core.cartesian.data.ColumnCartesianLayerModel
import com.telekom.odsystem.charts.core.cartesian.data.columnSeries
import com.telekom.odsystem.charts.core.cartesian.layer.ColumnCartesianLayer
import com.telekom.odsystem.charts.core.cartesian.marker.CartesianMarker
import com.telekom.odsystem.charts.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.common.shape.CorneredShape
import com.telekom.odsystem.charts.core.common.shape.Shape
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
Created by sarthakgupta on 17/09/25
 **/

private const val SQUARED_ROUNDED = 6f

@Suppress("All")
@Composable
fun ODSVerticalBarChart(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    style: ODSBarChartStyle = ODSBarChartStyle(),
    props: ODSBarChartProps = ODSBarChartProps(),
    format: (context: CartesianDrawingContext, targets: List<CartesianMarker.Target>) -> CharSequence = remember { { _, _ -> "" } },
    onBarSelected: ((Int) -> Unit)? = null,
    onBarDeSelected: (() -> Unit)? = null,
    verticalAxisFormatter: (Double) -> String = { it.toString() },
) {
    var selectedXTarget by remember { mutableStateOf<Double?>(null) }
    var lastDeselectedX by remember { mutableStateOf<Double?>(null) }
    val line = rememberLineComponent(
        shape = when (props.shape) {
            ODSBarItemShape.PILLED -> CorneredShape.Pill
            ODSBarItemShape.SQUARED -> CorneredShape.rounded(SQUARED_ROUNDED)
        },
        thickness = 28.dp
    )
    val marker = odsChartAnnotation(scheme = scheme, format = format)
    val persistentMarker = remember(selectedXTarget, props, style) {
        val persistentMarkerScope: CartesianChart.PersistentMarkerScope.(ExtraStore) -> Unit = {
            selectedXTarget?.let { marker at it }
        }
        persistentMarkerScope
    }
    val markerVisibilityListener =
        remember {
            getMarkerVisibilityListener(
                onMarkerShown = { marker, targets ->
                    val target = targets.firstOrNull() ?: return@getMarkerVisibilityListener
                    val clickedX = target.x
                    if (selectedXTarget == clickedX) {
                        selectedXTarget = null
                        lastDeselectedX = clickedX
                    } else {
                        selectedXTarget = clickedX
                        lastDeselectedX = null
                        onBarSelected?.invoke(clickedX.toInt())
                    }
                },
                onMarkerUpdated = { marker, targets ->
                    targets.firstOrNull()?.let {
                        val updatedTargetX = it.x
                        if (updatedTargetX != selectedXTarget && updatedTargetX != lastDeselectedX) {
                            selectedXTarget = updatedTargetX
                            lastDeselectedX = null
                            onBarSelected?.invoke(updatedTargetX.toInt())
                        }
                    }
                },
                onMarkerHidden = { marker ->
                    lastDeselectedX = null
                    onBarDeSelected?.invoke()
                }
            )
        }
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(props.barItemsList) {
        modelProducer.runTransaction {
            columnSeries {
                series(y = props.barItemsList.map { it.yValue ?: 0.0 })
            }
        }
    }
    CartesianChartHost(
        modifier = modifier,
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = props.scrollEnabled),
        zoomState = rememberVicoZoomState(zoomEnabled = props.zoomEnabled),
        animationSpec = null,
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = getColumnProvider(
                    style = style,
                    line = line,
                    selectedXTarget = selectedXTarget
                )
            ),
            startAxis = getStartAxis(
                style = style,
                props = props,
                verticalAxisFormatter = verticalAxisFormatter
            ),
            endAxis = getEndAxis(
                style = style,
                props = props,
                verticalAxisFormatter = verticalAxisFormatter
            ),
            topAxis = getTopAxis(
                style = style,
                props = props
            ),
            bottomAxis = getBottomAxis(
                style = style,
                props = props
            ),
            marker = marker,
            persistentMarkers = persistentMarker,
            markerVisibilityListener = markerVisibilityListener,
        )
    )
}

private fun getColumnProvider(
    style: ODSBarChartStyle,
    line: LineComponent,
    selectedXTarget: Double?,
): ColumnCartesianLayer.ColumnProvider {
    return object : ColumnCartesianLayer.ColumnProvider {
        override fun getColumn(
            entry: ColumnCartesianLayerModel.Entry,
            seriesIndex: Int,
            extraStore: ExtraStore,
        ): LineComponent {
            return line.copy(
                fill = if (selectedXTarget == entry.x) {
                    fill(style.selectedBarBackground ?: Color.Transparent)
                } else {
                    fill(style.unselectedBarBackground ?: Color.Transparent)
                },
            )
        }

        override fun getWidestSeriesColumn(seriesIndex: Int, extraStore: ExtraStore) = line
    }
}

private fun getMarkerVisibilityListener(
    onMarkerShown: (marker: CartesianMarker, targets: List<CartesianMarker.Target>) -> Unit,
    onMarkerUpdated: (marker: CartesianMarker, targets: List<CartesianMarker.Target>) -> Unit,
    onMarkerHidden: (marker: CartesianMarker) -> Unit,
): CartesianMarkerVisibilityListener {
    return object : CartesianMarkerVisibilityListener {
        override fun onShown(
            marker: CartesianMarker,
            targets: List<CartesianMarker.Target>,
        ) {
            super.onShown(marker, targets)
            onMarkerShown(marker, targets)
        }

        override fun onUpdated(
            marker: CartesianMarker,
            targets: List<CartesianMarker.Target>,
        ) {
            super.onUpdated(marker, targets)
            onMarkerUpdated(marker, targets)
        }

        override fun onHidden(marker: CartesianMarker) {
            super.onHidden(marker)
            onMarkerHidden(marker)
        }
    }
}

@Composable
private fun getStartAxis(
    style: ODSBarChartStyle,
    props: ODSBarChartProps,
    verticalAxisFormatter: (Double) -> String,
): VerticalAxis<Axis.Position.Vertical.Start>? {
    if (props.showLeftLabels.not()) return null
    return VerticalAxis.rememberStart(
        tick = null,
        line = getAxisGuideLineComponent(style = style),
        label = getLabel(style = style),
        itemPlacer = getGridSteps(props.stepCount, props.stepValue),
        guideline = getAxisGuideLineComponent(style = style),
        valueFormatter = CartesianValueFormatter { _, y, _ ->
            verticalAxisFormatter(y)
        }
    )
}

@Composable
private fun getEndAxis(
    style: ODSBarChartStyle,
    props: ODSBarChartProps,
    verticalAxisFormatter: (Double) -> String,
): VerticalAxis<Axis.Position.Vertical.End>? {
    if (props.showRightLabels.not()) return null
    return VerticalAxis.rememberEnd(
        tick = null,
        line = getAxisGuideLineComponent(style = style),
        label = getLabel(style = style),
        itemPlacer = getGridSteps(props.stepCount, props.stepValue),
        guideline = getAxisGuideLineComponent(style = style),
        valueFormatter = CartesianValueFormatter { _, y, _ ->
            verticalAxisFormatter(y)
        }
    )
}

@Composable
private fun getTopAxis(
    style: ODSBarChartStyle,
    props: ODSBarChartProps,
): HorizontalAxis<Axis.Position.Horizontal.Top>? {
    if (props.showTopLabels.not()) return null
    return HorizontalAxis.rememberTop(
        tick = null,
        line = getAxisGuideLineComponent(style = style),
        label = getLabel(style = style),
        guideline = getAxisGuideLineComponent(style = style),
        valueFormatter = CartesianValueFormatter { context, x, _ ->
            props.barItemsList.getOrNull(x.toInt())?.xLabel.orEmpty()
        }
    )
}

@Composable
private fun getBottomAxis(
    style: ODSBarChartStyle,
    props: ODSBarChartProps,
): HorizontalAxis<Axis.Position.Horizontal.Bottom>? {
    if (props.showBottomLabels.not()) return null
    return HorizontalAxis.rememberBottom(
        tick = null,
        line = getAxisGuideLineComponent(style = style),
        label = getLabel(style = style),
        guideline = getAxisGuideLineComponent(style = style),
        valueFormatter = CartesianValueFormatter { context, x, _ ->
            props.barItemsList.getOrNull(x.toInt())?.xLabel.orEmpty()
        },
    )
}

@Composable
private fun getLabel(style: ODSBarChartStyle): TextComponent {
    val context = LocalContext.current
    return rememberTextComponent(
        color = style.labelColor ?: Color.Transparent,
        textSize = style.labelTextStyle?.fontSize?.sp ?: Defaults.TEXT_COMPONENT_TEXT_SIZE.sp,
        typeface = style.labelTextStyle?.fontFamily?.let { ResourcesCompat.getFont(context, it) }
            ?: Typeface.DEFAULT,
        lineHeight = null,
        lineCount = Defaults.AXIS_LABEL_MAX_LINES,
        truncateAt = TextUtils.TruncateAt.END,
        margins = insets(
            Defaults.AXIS_LABEL_HORIZONTAL_MARGIN.dp,
            Defaults.AXIS_LABEL_VERTICAL_MARGIN.dp
        ),
        padding = insets(
            8.dp,
            8.dp
        ),
        minWidth = TextComponent.MinWidth.fixed()
    )
}

@Composable
private fun getAxisGuideLineComponent(style: ODSBarChartStyle): LineComponent {
    return rememberAxisGuidelineComponent(
        fill = fill(style.guideLineColor ?: Color.Transparent),
        thickness = style.guideLineThickness ?: Defaults.AXIS_LINE_WIDTH.dp,
        shape = Shape.Rectangle,
    )
}

private fun getGridSteps(
    stepCount: Int?,
    stepValue: Double?,
): VerticalAxis.ItemPlacer {
    return if (stepValue != null && stepValue > 0f) {
        VerticalAxis.ItemPlacer.step({ stepValue })
    } else if (stepCount != null) {
        VerticalAxis.ItemPlacer.count({ stepCount })
    } else {
        VerticalAxis.ItemPlacer.step()
    }
}
