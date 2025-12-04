package com.telekom.odsystem.organisms.barchart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.charts.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Renders a horizontal or vertical bar chart based on the provided [ODSBarChartProps].
 *
 * @param modifier The modifier for the chart.
 * @param scheme The [ODSTheme] for styling. Defaults to [neutralScheme].
 * @param props [ODSBarChartProps] for chart configuration.
 * @param valueFormatter Formats the selected bar's value (x, y) into a String.
 * @param onBarSelected Callback when a bar is selected, providing its index.
 * @param onBarDeSelected Callback when a bar is deselected.
 * @param horizontalAxisFormatter Formats horizontal axis labels from Double to String.
 * @param verticalAxisFormatter Formats vertical axis labels from Double to String.
 */
@Suppress("All")
@Composable
fun ODSBarChart(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBarChartProps = ODSBarChartProps(),
    valueFormatter: (x: Double, y: Double) -> String = { x, y -> "x = $x, y = $y" },
    onBarSelected: (Int) -> Unit = { },
    onBarDeSelected: () -> Unit = { },
    horizontalAxisFormatter: (Double) -> String = { it.toString() },
    verticalAxisFormatter: (Double) -> String = { it.toString() },
) {
    val style = ODSBarChartStyle().getStyle(scheme = scheme, props = props)
    when (props.direction) {
        ODSBarItemDirection.HORIZONTAL -> ODSHorizontalBarChart(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props,
            valueFormatter = valueFormatter,
            horizontalAxisFormatter = horizontalAxisFormatter
        )

        ODSBarItemDirection.VERTICAL -> ODSVerticalBarChart(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props,
            format = { _, targets ->
                targets.firstOrNull()?.let { target ->
                    (target as? ColumnCartesianLayerMarkerTarget)?.let { columnCartesianLayerMarkerTarget ->
                        valueFormatter(target.x, target.columns.sumOf { it.entry.y })
                    }
                }.orEmpty()
            },
            onBarDeSelected = onBarDeSelected,
            onBarSelected = onBarSelected,
            verticalAxisFormatter = verticalAxisFormatter
        )
    }
}
