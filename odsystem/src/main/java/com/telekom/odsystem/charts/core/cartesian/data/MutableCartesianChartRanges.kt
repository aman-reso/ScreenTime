package com.telekom.odsystem.charts.core.cartesian.data

import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.common.orZero
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import kotlin.math.max
import kotlin.math.min

/** A [CartesianChartRanges] implementation whose every property is mutable. */
class MutableCartesianChartRanges : CartesianChartRanges {
    private var _minX: Double? = null

    private var _maxX: Double? = null

    internal var yRanges: MutableMap<Axis.Position.Vertical?, MutableYRange> = mutableMapOf()

    override val minX: Double
        get() = _minX.orZero

    override val maxX: Double
        get() = _maxX.orZero

    override var xStep: Double = 1.0

    override fun getYRange(axisPosition: Axis.Position.Vertical?): CartesianChartRanges.YRange =
        yRanges[axisPosition] ?: yRanges.getValue(null)

    /**
     * Tries to update the stored values. A minimum value can only be decreased. A maximum value can
     * only be increased.
     */
    fun tryUpdate(
        minX: Double,
        maxX: Double,
        minY: Double,
        maxY: Double,
        axisPosition: Axis.Position.Vertical?,
    ) {
        _minX = _minX?.coerceAtMost(minX) ?: minX
        _maxX = _maxX?.coerceAtLeast(maxX) ?: maxX
        yRanges[null]?.tryUpdate(minY, maxY) ?: run { yRanges[null] = MutableYRange(minY, maxY) }
        if (axisPosition != null) {
            yRanges[axisPosition]?.tryUpdate(minY, maxY)
                ?: run { yRanges[axisPosition] = MutableYRange(minY, maxY) }
        }
    }

    /** Clears all values. */
    fun reset() {
        _minX = null
        _maxX = null
        yRanges = mutableMapOf()
        xStep = 1.0
    }

    /** A mutable implementation of [CartesianChartRanges.YRange]. */
    class MutableYRange(override var minY: Double, override var maxY: Double) :
        CartesianChartRanges.YRange {
        override val length: Double
            get() = maxY - minY

        /**
         * Tries to update [MutableYRange.minY] and [MutableYRange.maxY]. [MutableYRange.minY] can only
         * be decreased. [MutableYRange.maxY] can only be increased.
         */
        fun tryUpdate(minY: Double, maxY: Double) {
            this.minY = min(this.minY, minY)
            this.maxY = max(this.maxY, maxY)
        }
    }
}

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun MutableCartesianChartRanges.toImmutable(): CartesianChartRanges =
    object : CartesianChartRanges {
        private val yRanges = this@toImmutable.yRanges
        override val minX: Double = this@toImmutable.minX
        override val maxX: Double = this@toImmutable.maxX
        override val xStep: Double = this@toImmutable.xStep

        override fun getYRange(axisPosition: Axis.Position.Vertical?): CartesianChartRanges.YRange =
            yRanges[axisPosition] ?: yRanges.getValue(null)
    }
