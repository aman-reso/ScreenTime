package com.telekom.odsystem.charts.compose.cartesian.layer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.compose.common.component.rememberLineComponent
import com.telekom.odsystem.charts.compose.common.fill
import com.telekom.odsystem.charts.compose.common.vicoTheme
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.data.CartesianLayerRangeProvider
import com.telekom.odsystem.charts.core.cartesian.data.CartesianValueFormatter
import com.telekom.odsystem.charts.core.cartesian.data.ColumnCartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.cartesian.layer.ColumnCartesianLayer
import com.telekom.odsystem.charts.core.cartesian.layer.ColumnCartesianLayer.MergeMode
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.Position
import com.telekom.odsystem.charts.core.common.ValueWrapper
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModelInterpolator
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.common.getValue
import com.telekom.odsystem.charts.core.common.setValue

/** Creates and remembers a [ColumnCartesianLayer]. */
@Composable
public fun rememberColumnCartesianLayer(
    columnProvider: ColumnCartesianLayer.ColumnProvider =
        ColumnCartesianLayer.ColumnProvider.series(
            vicoTheme.columnCartesianLayerColors.map { color ->
                rememberLineComponent(fill(color), Defaults.COLUMN_WIDTH.dp)
            }
        ),
    columnCollectionSpacing: Dp = Defaults.COLUMN_COLLECTION_SPACING.dp,
    mergeMode: (ExtraStore) -> MergeMode = { MergeMode.grouped() },
    dataLabel: TextComponent? = null,
    dataLabelPosition: Position.Vertical = Position.Vertical.Top,
    dataLabelValueFormatter: CartesianValueFormatter = CartesianValueFormatter.Default,
    dataLabelRotationDegrees: Float = 0f,
    rangeProvider: CartesianLayerRangeProvider = remember { CartesianLayerRangeProvider.auto() },
    verticalAxisPosition: Axis.Position.Vertical? = null,
    drawingModelInterpolator:
    CartesianLayerDrawingModelInterpolator<
            ColumnCartesianLayerDrawingModel.Entry,
            ColumnCartesianLayerDrawingModel,
            > =
        remember {
            CartesianLayerDrawingModelInterpolator.default()
        },
): ColumnCartesianLayer {
    var columnCartesianLayerWrapper by remember { ValueWrapper<ColumnCartesianLayer?>(null) }
    return remember(
        columnProvider,
        columnCollectionSpacing,
        mergeMode,
        dataLabel,
        dataLabelPosition,
        dataLabelValueFormatter,
        dataLabelRotationDegrees,
        rangeProvider,
        verticalAxisPosition,
        drawingModelInterpolator,
    ) {
        val columnCartesianLayer =
            columnCartesianLayerWrapper?.copy(
                columnProvider,
                columnCollectionSpacing.value,
                mergeMode,
                dataLabel,
                dataLabelPosition,
                dataLabelValueFormatter,
                dataLabelRotationDegrees,
                rangeProvider,
                verticalAxisPosition,
                drawingModelInterpolator,
            )
                ?: ColumnCartesianLayer(
                    columnProvider,
                    columnCollectionSpacing.value,
                    mergeMode,
                    dataLabel,
                    dataLabelPosition,
                    dataLabelValueFormatter,
                    dataLabelRotationDegrees,
                    rangeProvider,
                    verticalAxisPosition,
                    drawingModelInterpolator,
                )
        columnCartesianLayerWrapper = columnCartesianLayer
        columnCartesianLayer
    }
}

/** Creates a [MergeMode.Grouped] instance. */
public fun MergeMode.Companion.grouped(
    columnSpacing: Dp = Defaults.GROUPED_COLUMN_SPACING.dp,
): MergeMode.Grouped = MergeMode.Grouped(columnSpacing.value)

/** Returns a [MergeMode.Stacked] instance. */
public fun MergeMode.Companion.stacked(): MergeMode.Stacked = MergeMode.Stacked
