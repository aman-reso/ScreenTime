package com.telekom.odsystem.charts.compose.cartesian.marker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.cartesian.marker.DefaultCartesianMarker
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.common.component.TextComponent

/** Creates and remembers a [DefaultCartesianMarker]. */
@Composable
fun rememberDefaultCartesianMarker(
    label: TextComponent,
    valueFormatter: DefaultCartesianMarker.ValueFormatter = remember {
        DefaultCartesianMarker.ValueFormatter.default()
    },
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    indicator: ((Color) -> Component)? = null,
    indicatorSize: Dp = Defaults.MARKER_INDICATOR_SIZE.dp,
    guideline: LineComponent? = null,
): DefaultCartesianMarker =
    remember(label, valueFormatter, labelPosition, indicator, indicatorSize, guideline) {
        DefaultCartesianMarker(
            label = label,
            valueFormatter = valueFormatter,
            labelPosition = labelPosition,
            indicator = if (indicator != null) ({ indicator(Color(it)) }) else null,
            indicatorSizeDp = indicatorSize.value,
            guideline = guideline,
        )
    }
