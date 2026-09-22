package com.telekom.odsystem.charts.core.cartesian.data

import com.telekom.odsystem.charts.core.cartesian.layer.LineCartesianLayer
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.common.lerp
import com.telekom.odsystem.charts.core.common.orZero

/** Houses [LineCartesianLayer] drawing information. [opacity] is the lines’ opacity. */
class LineCartesianLayerDrawingModel(
    private val entries: List<Map<Double, Entry>>,
    val opacity: Float = 1f,
) : CartesianLayerDrawingModel<LineCartesianLayerDrawingModel.Entry>(entries) {
    override fun transform(
        entries: List<Map<Double, Entry>>,
        from: CartesianLayerDrawingModel<Entry>?,
        fraction: Float,
    ): CartesianLayerDrawingModel<Entry> =
        LineCartesianLayerDrawingModel(
            entries,
            (from as LineCartesianLayerDrawingModel?)?.opacity.orZero.lerp(opacity, fraction),
        )

    override fun equals(other: Any?): Boolean =
        this === other ||
                other is LineCartesianLayerDrawingModel &&
                entries == other.entries &&
                opacity == other.opacity

    override fun hashCode(): Int = 31 * entries.hashCode() + opacity.hashCode()

    /**
     * Houses positional information for a [LineCartesianLayer]’s point. [y] expresses the distance of
     * the point from the bottom of the [LineCartesianLayer] as a fraction of the
     * [LineCartesianLayer]’s height.
     */
    class Entry(val y: Float) : CartesianLayerDrawingModel.Entry {
        override fun transform(
            from: CartesianLayerDrawingModel.Entry?,
            fraction: Float,
        ): CartesianLayerDrawingModel.Entry {
            val oldY = (from as? Entry)?.y.orZero
            return Entry(oldY.lerp(y, fraction))
        }

        override fun equals(other: Any?): Boolean = this === other || other is Entry && y == other.y

        override fun hashCode(): Int = y.hashCode()
    }
}
