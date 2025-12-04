package com.telekom.odsystem.charts.core.cartesian.marker

import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayer
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerMarginUpdater

/** Marks [CartesianChart] objects. */
@Immutable
public interface CartesianMarker : CartesianLayerMarginUpdater<CartesianChartModel> {
    /** Draws content under the [CartesianLayer]s. */
    public fun drawUnderLayers(context: CartesianDrawingContext, targets: List<Target>) {}

    /** Draws content over the [CartesianLayer]s. */
    public fun drawOverLayers(context: CartesianDrawingContext, targets: List<Target>) {}

    /** Houses information on an object to be marked. */
    public interface Target {
        /** The _x_ value. */
        public val x: Double

        /** The pixel _x_ coordinate. */
        public val canvasX: Float
    }
}
