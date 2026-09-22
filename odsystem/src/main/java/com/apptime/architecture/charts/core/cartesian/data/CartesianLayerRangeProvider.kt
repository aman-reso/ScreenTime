package com.telekom.odsystem.charts.core.cartesian.data

import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayer
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sign

/** Defines a [CartesianLayer]’s _x_ and _y_ ranges. */
@Immutable
interface CartesianLayerRangeProvider {
    /** Returns the minimum _x_ value. */
    fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore): Double = minX

    /** Returns the maximum _x_ value. */
    fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore): Double = maxX

    /** Returns the minimum _y_ value. */
    fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double =
        minY.coerceAtMost(0.0)

    /** Returns the maximum _y_ value. */
    fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double =
        if (minY == 0.0 && maxY == 0.0) 1.0 else maxY.coerceAtLeast(0.0)

    companion object {
        private object Auto : CartesianLayerRangeProvider {
            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                if (minY == 0.0 && maxY == 0.0 || minY >= 0.0) 0.0 else minY.round(maxY)

            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                when {
                    minY == 0.0 && maxY == 0.0 -> 1.0
                    maxY <= 0.0 -> 0.0
                    else -> maxY.round(minY)
                }

            private fun Double.round(other: Double): Double {
                val absoluteValue = abs(this)
                val base = 10.0.pow(floor(log10(max(absoluteValue, abs(other)))) - 1)
                return sign * ceil(absoluteValue / base) * base
            }
        }

        private data class Fixed(
            private val minX: Double? = null,
            private val maxX: Double? = null,
            private val minY: Double? = null,
            private val maxY: Double? = null,
        ) : CartesianLayerRangeProvider {
            override fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore) =
                this.minX ?: super.getMinX(minX, maxX, extraStore)

            override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore) =
                this.maxX ?: super.getMaxX(minX, maxX, extraStore)

            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                this.minY ?: super.getMinY(minY, maxY, extraStore)

            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                this.maxY ?: super.getMaxY(minY, maxY, extraStore)
        }

        /** Uses dynamic rounding. */
        fun auto(): CartesianLayerRangeProvider = Auto

        /** Overrides the defaults with the provided values. */
        fun fixed(
            minX: Double? = null,
            maxX: Double? = null,
            minY: Double? = null,
            maxY: Double? = null,
        ): CartesianLayerRangeProvider = Fixed(minX, maxX, minY, maxY)
    }
}
