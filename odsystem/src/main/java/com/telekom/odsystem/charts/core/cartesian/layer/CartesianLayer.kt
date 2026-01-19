package com.telekom.odsystem.charts.core.cartesian.layer

import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext
import com.telekom.odsystem.charts.core.cartesian.marker.CartesianMarker
import com.telekom.odsystem.charts.core.common.data.MutableExtraStore
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartRanges
import com.telekom.odsystem.charts.core.cartesian.data.CartesianLayerModel
import com.telekom.odsystem.charts.core.cartesian.data.MutableCartesianChartRanges

/**
 * Visualizes data on a Cartesian plane. [CartesianLayer]s are combined and drawn by
 * [CartesianChart]s.
 */
interface CartesianLayer<M : CartesianLayerModel> : CartesianLayerMarginUpdater<M> {
    /** Links _x_ values to [CartesianMarker.Target]s. */
    val markerTargets: Map<Double, List<CartesianMarker.Target>>

    /** Draws the [CartesianLayer]. */
    fun draw(context: CartesianDrawingContext, model: M)

    /** Updates [dimensions] to match this [CartesianLayer]’s dimensions. */
    fun updateDimensions(
      context: CartesianMeasuringContext,
      dimensions: MutableCartesianLayerDimensions,
      model: M,
    )

    /** Updates [chartRanges] in accordance with [model]. */
    fun updateChartRanges(chartRanges: MutableCartesianChartRanges, model: M)

    /** Prepares the [CartesianLayer] for a difference animation. */
    fun prepareForTransformation(
        model: M?,
        ranges: CartesianChartRanges,
        extraStore: MutableExtraStore,
    )

    /** Carries out the pending difference animation. */
    suspend fun transform(extraStore: MutableExtraStore, fraction: Float)
}
