package com.telekom.odsystem.charts.core.cartesian.data

import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.axis.Axis

/** Stores a [CartesianChart]’s _x_ and _y_ ranges. */
public interface CartesianChartRanges {
    /** The minimum _x_ value. */
    public val minX: Double

    /** The maximum _x_ value. */
    public val maxX: Double

    /** The difference between neighboring major _x_ values. */
    public val xStep: Double

    /**
     * Returns the [YRange] associated with the given [Axis.Position.Vertical] subclass. If
     * [axisPosition] is `null` or has no associated [YRange], the global [YRange] is returned.
     */
    public fun getYRange(axisPosition: Axis.Position.Vertical?): YRange

    /** The difference between [maxX] and [minX]. */
    public val xLength: Double
        get() = maxX - minX

    /** Holds information on a _y_ range. */
    public interface YRange {
        /** The minimum _y_ value. */
        public val minY: Double

        /** The maximum _y_ value. */
        public val maxY: Double

        /** The difference between [maxY] and [minY]. */
        public val length: Double
    }

    /** @suppress */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public object Empty : CartesianChartRanges {
        private const val ERROR_MESSAGE = "`CartesianRanges.Empty` shouldn’t be used."

        override val minX: Double
            get() {
                error(ERROR_MESSAGE)
            }

        override val maxX: Double
            get() {
                error(ERROR_MESSAGE)
            }

        override val xStep: Double
            get() {
                error(ERROR_MESSAGE)
            }

        override fun getYRange(axisPosition: Axis.Position.Vertical?): YRange {
            error(ERROR_MESSAGE)
        }
    }
}
