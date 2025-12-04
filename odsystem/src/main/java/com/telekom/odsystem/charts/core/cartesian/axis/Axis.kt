package com.telekom.odsystem.charts.core.cartesian.axis

import android.graphics.RectF
import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayer
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerMarginUpdater
import com.telekom.odsystem.charts.core.cartesian.layer.MutableCartesianLayerDimensions
import com.telekom.odsystem.charts.core.common.Bounded
import com.telekom.odsystem.charts.core.common.MeasuringContext
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel

/** Draws an axis. */
@Immutable
public interface Axis<P : Axis.Position> :
    Bounded, CartesianLayerMarginUpdater<CartesianChartModel> {
    /** The position of the [Axis]. */
    public val position: P

    /** Draws content under the [CartesianLayer]s. */
    public fun drawUnderLayers(context: CartesianDrawingContext)

    /** Draws content over the [CartesianLayer]s. */
    public fun drawOverLayers(context: CartesianDrawingContext)

    /** The bounds ([RectF]) passed here define the area where the [Axis] shouldn’t draw anything. */
    public fun setRestrictedBounds(vararg bounds: RectF?)

    /** Updates the chart’s [MutableCartesianLayerDimensions] instance. */
    public fun updateLayerDimensions(
        context: CartesianMeasuringContext,
        layerDimensions: MutableCartesianLayerDimensions,
    )

    /** Specifies the position of an [Axis]. */
    public sealed interface Position {
        /** Specifies the position of a horizontal [Axis]. */
        public sealed interface Horizontal : Position {
            /** Denotes that a horizontal [Axis] is at the top of its [CartesianChart]. */
            public data object Top : Horizontal

            /** Denotes that a horizontal [Axis] is at the bottom of its [CartesianChart]. */
            public data object Bottom : Horizontal
        }

        /** Specifies the position of a vertical [Axis]. */
        public sealed interface Vertical : Position {
            /** Denotes that a vertical [Axis] is at the start of its [CartesianChart]. */
            public data object Start : Vertical

            /** Denotes that a vertical [Axis] is at the end of its [CartesianChart]. */
            public data object End : Vertical
        }
    }
}

internal fun Axis.Position.Vertical.isLeft(context: MeasuringContext) =
    when (this) {
        Axis.Position.Vertical.Start -> context.isLtr
        Axis.Position.Vertical.End -> !context.isLtr
    }
