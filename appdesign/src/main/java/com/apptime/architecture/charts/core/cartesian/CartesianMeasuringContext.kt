package com.telekom.odsystem.charts.core.cartesian

import com.telekom.odsystem.charts.core.common.MeasuringContext
import com.telekom.odsystem.charts.core.common.Point
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartRanges
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayer
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerDimensions
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerPadding

/** A [MeasuringContext] extension with [CartesianChart]-specific data. */
interface CartesianMeasuringContext : MeasuringContext {
    /** Stores the [CartesianChart]’s data. */
    val model: CartesianChartModel

    /** Stores the [CartesianChart]’s _x_ and _y_ ranges. */
    val ranges: CartesianChartRanges

    /** Whether scroll is enabled. */
    val scrollEnabled: Boolean

    /** Whether zoom is enabled. */
    val zoomEnabled: Boolean

    /** Stores the [CartesianLayer] padding values. */
    val layerPadding: CartesianLayerPadding

    /** The pointer position. */
    val pointerPosition: Point?
}

internal fun CartesianMeasuringContext.getFullXRange(layerDimensions: CartesianLayerDimensions) =
    layerDimensions.run {
        val start = ranges.minX - startPadding / xSpacing * ranges.xStep
        val end = ranges.maxX + endPadding / xSpacing * ranges.xStep
        start..end
    }
