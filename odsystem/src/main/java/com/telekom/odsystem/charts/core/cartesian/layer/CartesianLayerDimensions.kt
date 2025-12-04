package com.telekom.odsystem.charts.core.cartesian.layer

import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext

/** Stores shared [CartesianLayer] dimensions. */
public interface CartesianLayerDimensions {
    /** The distance between neighboring major _x_ values (in pixels). This can be scaled. */
    public val xSpacing: Float

    /**
     * The scalable part of the distance between the start of the content area and the first entry (in
     * pixels).
     */
    public val scalableStartPadding: Float

    /**
     * The scalable part of the distance between the end of the content area and the last entry (in
     * pixels).
     */
    public val scalableEndPadding: Float

    /**
     * The unscalable part of the distance between the start of the content area and the first entry
     * (in pixels).
     */
    public val unscalableStartPadding: Float

    /**
     * The unscalable part of the distance between the end of the content area and the last entry (in
     * pixels).
     */
    public val unscalableEndPadding: Float

    /** The total start padding (in pixels). */
    public val startPadding: Float
        get() = scalableStartPadding + unscalableStartPadding

    /** The total end padding (in pixels). */
    public val endPadding: Float
        get() = scalableEndPadding + unscalableEndPadding

    /** The total scalable horizontal padding (in pixels). */
    public val scalablePadding: Float
        get() = scalableStartPadding + scalableEndPadding

    /** The total unscalable horizontal padding (in pixels). */
    public val unscalablePadding: Float
        get() = unscalableStartPadding + unscalableEndPadding

    /** The total horizontal padding (in pixels). */
    public val padding: Float
        get() = startPadding + endPadding

    /** Calculates the width of the [CartesianChart]’s scalable content (in pixels). */
    public fun getScalableContentWidth(context: CartesianMeasuringContext): Float =
        with(context) { xSpacing * (ranges.xLength / ranges.xStep).toFloat() + scalablePadding }

    /** Calculates the width of the [CartesianChart]’s content (in pixels). */
    public fun getContentWidth(context: CartesianMeasuringContext): Float =
        getScalableContentWidth(context) + unscalablePadding
}
