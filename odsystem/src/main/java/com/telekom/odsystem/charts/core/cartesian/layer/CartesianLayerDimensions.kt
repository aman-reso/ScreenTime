package com.telekom.odsystem.charts.core.cartesian.layer

import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext

/** Stores shared [CartesianLayer] dimensions. */
interface CartesianLayerDimensions {
    /** The distance between neighboring major _x_ values (in pixels). This can be scaled. */
    val xSpacing: Float

    /**
     * The scalable part of the distance between the start of the content area and the first entry (in
     * pixels).
     */
    val scalableStartPadding: Float

    /**
     * The scalable part of the distance between the end of the content area and the last entry (in
     * pixels).
     */
    val scalableEndPadding: Float

    /**
     * The unscalable part of the distance between the start of the content area and the first entry
     * (in pixels).
     */
    val unscalableStartPadding: Float

    /**
     * The unscalable part of the distance between the end of the content area and the last entry (in
     * pixels).
     */
    val unscalableEndPadding: Float

    /** The total start padding (in pixels). */
    val startPadding: Float
        get() = scalableStartPadding + unscalableStartPadding

    /** The total end padding (in pixels). */
    val endPadding: Float
        get() = scalableEndPadding + unscalableEndPadding

    /** The total scalable horizontal padding (in pixels). */
    val scalablePadding: Float
        get() = scalableStartPadding + scalableEndPadding

    /** The total unscalable horizontal padding (in pixels). */
    val unscalablePadding: Float
        get() = unscalableStartPadding + unscalableEndPadding

    /** The total horizontal padding (in pixels). */
    val padding: Float
        get() = startPadding + endPadding

    /** Calculates the width of the [CartesianChart]’s scalable content (in pixels). */
    fun getScalableContentWidth(context: CartesianMeasuringContext): Float =
        with(context) { xSpacing * (ranges.xLength / ranges.xStep).toFloat() + scalablePadding }

    /** Calculates the width of the [CartesianChart]’s content (in pixels). */
    fun getContentWidth(context: CartesianMeasuringContext): Float =
        getScalableContentWidth(context) + unscalablePadding
}
