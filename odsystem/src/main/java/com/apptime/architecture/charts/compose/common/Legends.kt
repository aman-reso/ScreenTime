package com.telekom.odsystem.charts.compose.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.common.AdditionScope
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.DrawingContext
import com.telekom.odsystem.charts.core.common.HorizontalLegend
import com.telekom.odsystem.charts.core.common.Insets
import com.telekom.odsystem.charts.core.common.LegendItem
import com.telekom.odsystem.charts.core.common.MeasuringContext
import com.telekom.odsystem.charts.core.common.VerticalLegend
import com.telekom.odsystem.charts.core.common.data.ExtraStore

/** Creates and remembers a [VerticalLegend]. */
@Composable
fun <M : MeasuringContext, D : DrawingContext> rememberVerticalLegend(
    items: AdditionScope<LegendItem>.(ExtraStore) -> Unit,
    iconSize: Dp = Defaults.LEGEND_ICON_SIZE.dp,
    iconLabelSpacing: Dp = Defaults.LEGEND_ICON_LABEL_SPACING.dp,
    rowSpacing: Dp = Defaults.LEGEND_ROW_SPACING.dp,
    padding: Insets = Insets.Zero,
): VerticalLegend<M, D> =
    remember(items, iconSize, iconLabelSpacing, rowSpacing, padding) {
        VerticalLegend(items, iconSize.value, iconLabelSpacing.value, rowSpacing.value, padding)
    }

/** Creates and remembers a [HorizontalLegend]. */
@Composable
fun <M : MeasuringContext, D : DrawingContext> rememberHorizontalLegend(
    items: AdditionScope<LegendItem>.(ExtraStore) -> Unit,
    iconSize: Dp = Defaults.LEGEND_ICON_SIZE.dp,
    iconLabelSpacing: Dp = Defaults.LEGEND_ICON_LABEL_SPACING.dp,
    rowSpacing: Dp = Defaults.LEGEND_ROW_SPACING.dp,
    columnSpacing: Dp = Defaults.LEGEND_COLUMN_SPACING.dp,
    padding: Insets = Insets.Zero,
): HorizontalLegend<M, D> =
    remember(items, iconSize, iconLabelSpacing, rowSpacing, columnSpacing, padding) {
        HorizontalLegend(
            items,
            iconSize.value,
            iconLabelSpacing.value,
            rowSpacing.value,
            columnSpacing.value,
            padding,
        )
    }
