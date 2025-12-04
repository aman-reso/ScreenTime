package com.telekom.odsystem.charts.core.cartesian.marker

import com.telekom.odsystem.charts.core.cartesian.data.CandlestickCartesianLayerModel
import com.telekom.odsystem.charts.core.cartesian.layer.CandlestickCartesianLayer

/**
 * Houses information on a [CandlestickCartesianLayer] candle to be marked.
 *
 * @property entry the [CandlestickCartesianLayerModel.Entry].
 * @property openingCanvasY the pixel _y_ coordinate of the body edge corresponding to
 *   [CandlestickCartesianLayerModel.Entry.opening].
 * @property closingCanvasY the pixel _y_ coordinate of the body edge corresponding to
 *   [CandlestickCartesianLayerModel.Entry.closing].
 * @property lowCanvasY the pixel _y_ coordinate of the bottom wick’s bottom edge, which corresponds
 *   to [CandlestickCartesianLayerModel.Entry.low].
 * @property highCanvasY the pixel _y_ coordinate of the top wick’s top edge, which corresponds to
 *   [CandlestickCartesianLayerModel.Entry.high].
 * @property openingColor the color of [CandlestickCartesianLayer.Candle.body].
 * @property closingColor the color of [CandlestickCartesianLayer.Candle.body].
 * @property lowColor the color of [CandlestickCartesianLayer.Candle.bottomWick].
 * @property highColor the color of [CandlestickCartesianLayer.Candle.topWick].
 */
public data class CandlestickCartesianLayerMarkerTarget(
    override val x: Double,
    override val canvasX: Float,
    public val entry: CandlestickCartesianLayerModel.Entry,
    public val openingCanvasY: Float,
    public val closingCanvasY: Float,
    public val lowCanvasY: Float,
    public val highCanvasY: Float,
    public val openingColor: Int,
    public val closingColor: Int,
    public val lowColor: Int,
    public val highColor: Int,
) : CartesianMarker.Target
