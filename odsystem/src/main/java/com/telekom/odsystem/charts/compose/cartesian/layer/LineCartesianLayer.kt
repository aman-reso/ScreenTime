package com.telekom.odsystem.charts.compose.cartesian.layer

import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.compose.common.fill
import com.telekom.odsystem.charts.compose.common.vicoTheme
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.data.CartesianLayerRangeProvider
import com.telekom.odsystem.charts.core.cartesian.data.CartesianValueFormatter
import com.telekom.odsystem.charts.core.cartesian.data.LineCartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.cartesian.layer.LineCartesianLayer
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.Position
import com.telekom.odsystem.charts.core.common.ValueWrapper
import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModelInterpolator
import com.telekom.odsystem.charts.core.common.getValue
import com.telekom.odsystem.charts.core.common.setValue

/** Creates and remembers a [LineCartesianLayer]. */
@Composable
public fun rememberLineCartesianLayer(
    lineProvider: LineCartesianLayer.LineProvider =
        LineCartesianLayer.LineProvider.series(
            vicoTheme.lineCartesianLayerColors.map { color ->
                LineCartesianLayer.rememberLine(LineCartesianLayer.LineFill.single(fill(color)))
            }
        ),
    pointSpacing: Dp = Defaults.POINT_SPACING.dp,
    rangeProvider: CartesianLayerRangeProvider = remember { CartesianLayerRangeProvider.auto() },
    verticalAxisPosition: Axis.Position.Vertical? = null,
    drawingModelInterpolator:
    CartesianLayerDrawingModelInterpolator<
            LineCartesianLayerDrawingModel.Entry,
            LineCartesianLayerDrawingModel,
            > =
        remember {
            CartesianLayerDrawingModelInterpolator.default()
        },
): LineCartesianLayer {
    var lineCartesianLayerWrapper by remember { ValueWrapper<LineCartesianLayer?>(null) }
    return remember(
        lineProvider,
        pointSpacing,
        rangeProvider,
        verticalAxisPosition,
        drawingModelInterpolator,
    ) {
        val lineCartesianLayer =
            lineCartesianLayerWrapper?.copy(
                lineProvider,
                pointSpacing.value,
                rangeProvider,
                verticalAxisPosition,
                drawingModelInterpolator,
            )
                ?: LineCartesianLayer(
                    lineProvider,
                    pointSpacing.value,
                    rangeProvider,
                    verticalAxisPosition,
                    drawingModelInterpolator,
                )
        lineCartesianLayerWrapper = lineCartesianLayer
        lineCartesianLayer
    }
}

/** Creates and remembers a [LineCartesianLayer.Line]. */
@Composable
public fun LineCartesianLayer.Companion.rememberLine(
    fill: LineCartesianLayer.LineFill =
        vicoTheme.lineCartesianLayerColors.first().let { color ->
            remember(color) { LineCartesianLayer.LineFill.single(fill(color)) }
        },
    stroke: LineCartesianLayer.LineStroke = LineCartesianLayer.LineStroke.continuous(),
    areaFill: LineCartesianLayer.AreaFill? = null,
    pointProvider: LineCartesianLayer.PointProvider? = null,
    pointConnector: LineCartesianLayer.PointConnector = LineCartesianLayer.PointConnector.Sharp,
    dataLabel: TextComponent? = null,
    dataLabelPosition: Position.Vertical = Position.Vertical.Top,
    dataLabelValueFormatter: CartesianValueFormatter = CartesianValueFormatter.Default,
    dataLabelRotationDegrees: Float = 0f,
): LineCartesianLayer.Line =
    remember(
        fill,
        stroke,
        areaFill,
        pointProvider,
        pointConnector,
        dataLabel,
        dataLabelPosition,
        dataLabelRotationDegrees,
        dataLabelRotationDegrees,
    ) {
        LineCartesianLayer.Line(
            fill,
            stroke,
            areaFill,
            pointProvider,
            pointConnector,
            dataLabel,
            dataLabelPosition,
            dataLabelValueFormatter,
            dataLabelRotationDegrees,
        )
    }

/** Creates a [LineCartesianLayer.Point]. */
public fun LineCartesianLayer.Companion.point(
    component: Component,
    size: Dp = Defaults.POINT_SIZE.dp,
): LineCartesianLayer.Point = LineCartesianLayer.Point(component, size.value)

private val StrokeCap.paintCap: Paint.Cap
    get() =
        when (this) {
            StrokeCap.Butt -> Paint.Cap.BUTT
            StrokeCap.Round -> Paint.Cap.ROUND
            StrokeCap.Square -> Paint.Cap.SQUARE
            else ->
                throw IllegalArgumentException(
                    "Not `StrokeCap.Butt`, `StrokeCap.Round`, or `StrokeCap.Square`."
                )
        }

/** Creates a [LineCartesianLayer.LineStroke.Continuous] instance. */
public fun LineCartesianLayer.LineStroke.Companion.continuous(
    thickness: Dp = Defaults.LINE_SPEC_THICKNESS_DP.dp,
    cap: StrokeCap = StrokeCap.Butt,
): LineCartesianLayer.LineStroke.Continuous =
    LineCartesianLayer.LineStroke.Continuous(thickness.value, cap.paintCap)

/** Creates a [LineCartesianLayer.LineStroke.Dashed] instance. */
public fun LineCartesianLayer.LineStroke.Companion.dashed(
    thickness: Dp = Defaults.LINE_SPEC_THICKNESS_DP.dp,
    cap: StrokeCap = StrokeCap.Butt,
    dashLength: Dp = Defaults.LINE_DASH_LENGTH.dp,
    gapLength: Dp = Defaults.LINE_GAP_LENGTH.dp,
): LineCartesianLayer.LineStroke.Dashed =
    LineCartesianLayer.LineStroke.Dashed(
        thickness.value,
        cap.paintCap,
        dashLength.value,
        gapLength.value,
    )
