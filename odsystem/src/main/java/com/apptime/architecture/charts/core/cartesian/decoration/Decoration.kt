package com.telekom.odsystem.charts.core.cartesian.decoration

import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayer

/**
 * A [Decoration] presents additional information on a [CartesianChart].
 *
 * @see [HorizontalBox]
 * @see [HorizontalLine]
 */
@Immutable
interface Decoration {
    /** Draws content under the [CartesianLayer]s. */
    fun drawUnderLayers(context: CartesianDrawingContext) {}

    /** Draws content over the [CartesianLayer]s. */
    fun drawOverLayers(context: CartesianDrawingContext) {}
}
