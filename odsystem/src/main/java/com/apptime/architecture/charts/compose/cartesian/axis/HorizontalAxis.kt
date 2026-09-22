package com.telekom.odsystem.charts.compose.cartesian.axis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.axis.BaseAxis
import com.telekom.odsystem.charts.core.cartesian.axis.HorizontalAxis
import com.telekom.odsystem.charts.core.cartesian.data.CartesianValueFormatter
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.common.component.TextComponent

/** Creates and remembers a top [HorizontalAxis]. */
@Composable
fun HorizontalAxis.Companion.rememberTop(
    line: LineComponent? = rememberAxisLineComponent(),
    label: TextComponent? = rememberAxisLabelComponent(),
    labelRotationDegrees: Float = Defaults.AXIS_LABEL_ROTATION_DEGREES,
    valueFormatter: CartesianValueFormatter = CartesianValueFormatter.Default,
    tick: LineComponent? = rememberAxisTickComponent(),
    tickLength: Dp = Defaults.AXIS_TICK_LENGTH.dp,
    guideline: LineComponent? = rememberAxisGuidelineComponent(),
    itemPlacer: HorizontalAxis.ItemPlacer = remember { HorizontalAxis.ItemPlacer.aligned() },
    size: BaseAxis.Size = BaseAxis.Size.auto(),
    titleComponent: TextComponent? = null,
    title: CharSequence? = null,
): HorizontalAxis<Axis.Position.Horizontal.Top> =
    remember(
        line,
        label,
        labelRotationDegrees,
        valueFormatter,
        tick,
        tickLength.value,
        guideline,
        itemPlacer,
        size,
        titleComponent,
        title,
    ) {
        top(
            line,
            label,
            labelRotationDegrees,
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

/** Creates and remembers a bottom [HorizontalAxis]. */
@Composable
fun HorizontalAxis.Companion.rememberBottom(
    line: LineComponent? = rememberAxisLineComponent(),
    label: TextComponent? = rememberAxisLabelComponent(),
    labelRotationDegrees: Float = Defaults.AXIS_LABEL_ROTATION_DEGREES,
    valueFormatter: CartesianValueFormatter = CartesianValueFormatter.Default,
    tick: LineComponent? = rememberAxisTickComponent(),
    tickLength: Dp = Defaults.AXIS_TICK_LENGTH.dp,
    guideline: LineComponent? = rememberAxisGuidelineComponent(),
    itemPlacer: HorizontalAxis.ItemPlacer = remember { HorizontalAxis.ItemPlacer.aligned() },
    size: BaseAxis.Size = BaseAxis.Size.auto(),
    titleComponent: TextComponent? = null,
    title: CharSequence? = null,
): HorizontalAxis<Axis.Position.Horizontal.Bottom> =
    remember(
        line,
        label,
        labelRotationDegrees,
        valueFormatter,
        tick,
        tickLength.value,
        guideline,
        itemPlacer,
        size,
        titleComponent,
        title,
    ) {
        bottom(
            line,
            label,
            labelRotationDegrees,
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
