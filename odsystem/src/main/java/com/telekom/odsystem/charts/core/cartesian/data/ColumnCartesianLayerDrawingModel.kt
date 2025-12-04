package com.telekom.odsystem.charts.core.cartesian.data

import com.telekom.odsystem.charts.core.cartesian.layer.ColumnCartesianLayer
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.common.lerp
import com.telekom.odsystem.charts.core.common.orZero

/** Houses drawing information for a [ColumnCartesianLayer]. [opacity] is the columns’ opacity. */
public class ColumnCartesianLayerDrawingModel(
    private val entries: List<Map<Double, Entry>>,
    public val opacity: Float = 1f,
) : CartesianLayerDrawingModel<ColumnCartesianLayerDrawingModel.Entry>(entries) {
    override fun transform(
        entries: List<Map<Double, Entry>>,
        from: CartesianLayerDrawingModel<Entry>?,
        fraction: Float,
    ): CartesianLayerDrawingModel<Entry> {
        val oldOpacity = (from as ColumnCartesianLayerDrawingModel?)?.opacity.orZero
        return ColumnCartesianLayerDrawingModel(entries, oldOpacity.lerp(opacity, fraction))
    }

    override fun equals(other: Any?): Boolean =
        this === other ||
                other is ColumnCartesianLayerDrawingModel &&
                entries == other.entries &&
                opacity == other.opacity

    override fun hashCode(): Int = 31 * entries.hashCode() + opacity.hashCode()

    /**
     * Houses positional information for a [ColumnCartesianLayer]’s column. [height] expresses the
     * column’s height as a fraction of the [ColumnCartesianLayer]’s height.
     */
    public class Entry(public val height: Float) : CartesianLayerDrawingModel.Entry {
        override fun transform(
            from: CartesianLayerDrawingModel.Entry?,
            fraction: Float,
        ): CartesianLayerDrawingModel.Entry {
            val oldHeight = (from as? Entry)?.height.orZero
            return Entry(oldHeight.lerp(height, fraction))
        }

        override fun equals(other: Any?): Boolean =
            this === other || other is Entry && height == other.height

        override fun hashCode(): Int = height.hashCode()
    }
}
