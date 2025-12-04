package com.telekom.odsystem.charts.core.cartesian.data

import com.telekom.odsystem.charts.core.cartesian.layer.CandlestickCartesianLayer
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.common.lerp
import com.telekom.odsystem.charts.core.common.orZero

/**
 * Houses drawing information for a [CandlestickCartesianLayer]. [opacity] is the columns’ opacity.
 */
public class CandlestickCartesianLayerDrawingModel(
    public val entries: Map<Double, Entry>,
    public val opacity: Float = 1f,
) : CartesianLayerDrawingModel<CandlestickCartesianLayerDrawingModel.Entry>(listOf(entries)) {
    override fun transform(
        entries: List<Map<Double, Entry>>,
        from: CartesianLayerDrawingModel<Entry>?,
        fraction: Float,
    ): CartesianLayerDrawingModel<Entry> {
        val oldOpacity = (from as CandlestickCartesianLayerDrawingModel?)?.opacity.orZero
        return CandlestickCartesianLayerDrawingModel(
            entries = entries.first(),
            opacity = oldOpacity.lerp(opacity, fraction),
        )
    }

    override fun equals(other: Any?): Boolean =
        this === other ||
                other is CandlestickCartesianLayerDrawingModel &&
                entries == other.entries &&
                opacity == other.opacity

    override fun hashCode(): Int = 31 * entries.hashCode() + opacity.hashCode()

    /**
     * Houses positional information for a [CandlestickCartesianLayer]’s candle. Each position is
     * stored as a distance from the bottom of the [CandlestickCartesianLayer]. The distances are
     * expressed as fractions of the [CandlestickCartesianLayer]’s height.
     *
     * @property bodyBottomY the position of the body’s bottom edge.
     * @property bodyTopY the position of the body’s top edge.
     * @property bottomWickY the position of the bottom wick’s bottom edge.
     * @property topWickY the position of the top wick’s top edge.
     */
    public class Entry(
        public val bodyBottomY: Float,
        public val bodyTopY: Float,
        public val bottomWickY: Float,
        public val topWickY: Float,
    ) : CartesianLayerDrawingModel.Entry {
        override fun transform(
            from: CartesianLayerDrawingModel.Entry?,
            fraction: Float,
        ): CartesianLayerDrawingModel.Entry {
            val old = from as? Entry
            val oldBodyBottomY = old?.bodyBottomY.orZero
            val oldBodyTopY = old?.bodyTopY.orZero
            val oldBottomWickY = old?.bottomWickY.orZero
            val oldTopWickY = old?.topWickY.orZero
            return Entry(
                oldBodyBottomY.lerp(bodyBottomY, fraction),
                oldBodyTopY.lerp(bodyTopY, fraction),
                oldBottomWickY.lerp(bottomWickY, fraction),
                oldTopWickY.lerp(topWickY, fraction),
            )
        }

        override fun equals(other: Any?): Boolean =
            this === other ||
                    other is Entry &&
                    bodyBottomY == other.bodyBottomY &&
                    bodyTopY == other.bodyTopY &&
                    bottomWickY == other.bottomWickY &&
                    topWickY == other.topWickY

        override fun hashCode(): Int {
            var result = bodyBottomY.hashCode()
            result = 31 * result + bodyTopY.hashCode()
            result = 31 * result + bottomWickY.hashCode()
            result = 31 * result + topWickY.hashCode()
            return result
        }
    }
}
