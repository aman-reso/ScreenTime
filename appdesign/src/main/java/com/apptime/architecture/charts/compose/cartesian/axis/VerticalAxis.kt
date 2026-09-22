package com.telekom.odsystem.charts.compose.cartesian.axis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.axis.BaseAxis
import com.telekom.odsystem.charts.core.cartesian.axis.VerticalAxis
import com.telekom.odsystem.charts.core.cartesian.data.CartesianValueFormatter
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.Position
import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.common.component.TextComponent

/** Creates and remembers a start [VerticalAxis]. */
@Composable
fun VerticalAxis.Companion.rememberStart(
    line: LineComponent? = rememberAxisLineComponent(),
    label: TextComponent? = rememberAxisLabelComponent(),
    labelRotationDegrees: Float = Defaults.AXIS_LABEL_ROTATION_DEGREES,
    horizontalLabelPosition: VerticalAxis.HorizontalLabelPosition =
        VerticalAxis.HorizontalLabelPosition.Outside,
    verticalLabelPosition: Position.Vertical = Position.Vertical.Center,
    valueFormatter: CartesianValueFormatter = CartesianValueFormatter.Default,
    tick: LineComponent? = rememberAxisTickComponent(),
    tickLength: Dp = Defaults.AXIS_TICK_LENGTH.dp,
    guideline: LineComponent? = rememberAxisGuidelineComponent(),
    itemPlacer: VerticalAxis.ItemPlacer = remember { VerticalAxis.ItemPlacer.step() },
    size: BaseAxis.Size = BaseAxis.Size.auto(),
    titleComponent: TextComponent? = null,
    title: CharSequence? = null,
): VerticalAxis<Axis.Position.Vertical.Start> =
    remember(
        line,
        label,
        labelRotationDegrees,
        horizontalLabelPosition,
        verticalLabelPosition,
        valueFormatter,
        tick,
        tickLength.value,
        guideline,
        itemPlacer,
        size,
        titleComponent,
        title,
    ) {
        start(
            line,
            label,
            labelRotationDegrees,
            horizontalLabelPosition,
            verticalLabelPosition,
            valueFormatter,
            tick,
            tickLength.value,
            guideline,
            itemPlacer,
            size,
            titleComponent,
            title,
        )
    }

/** Creates and remembers an end [VerticalAxis]. */
@Composable
fun VerticalAxis.Companion.rememberEnd(
    line: LineComponent? = rememberAxisLineComponent(),
    label: TextComponent? = rememberAxisLabelComponent(),
    labelRotationDegrees: Float = Defaults.AXIS_LABEL_ROTATION_DEGREES,
    horizontalLabelPosition: VerticalAxis.HorizontalLabelPosition =
        VerticalAxis.HorizontalLabelPosition.Outside,
    verticalLabelPosition: Position.Vertical = Position.Vertical.Center,
    valueFormatter: CartesianValueFormatter = CartesianValueFormatter.Default,
    tick: LineComponent? = rememberAxisTickComponent(),
    tickLength: Dp = Defaults.AXIS_TICK_LENGTH.dp,
    guideline: LineComponent? = rememberAxisGuidelineComponent(),
    itemPlacer: VerticalAxis.ItemPlacer = remember { VerticalAxis.ItemPlacer.step() },
    size: BaseAxis.Size = BaseAxis.Size.auto(),
    titleComponent: TextComponent? = null,
    title: CharSequence? = null,
): VerticalAxis<Axis.Position.Vertical.End> =
    remember(
        line,
        label,
        labelRotationDegrees,
        horizontalLabelPosition,
        verticalLabelPosition,
        valueFormatter,
        tick,
        tickLength.value,
        guideline,
        itemPlacer,
        size,
        titleComponent,
        title,
    ) {
        end(
            line,
            label,
            labelRotationDegrees,
            horizontalLabelPosition,
            verticalLabelPosition,
            valueFormatter,
            tick,
            tickLength.value,
            guideline,
            itemPlacer,
            size,
            titleComponent,
            title,
        )
    }
