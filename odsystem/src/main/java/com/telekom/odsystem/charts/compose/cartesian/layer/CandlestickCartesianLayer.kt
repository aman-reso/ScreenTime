package com.telekom.odsystem.charts.compose.cartesian.layer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.data.CandlestickCartesianLayerDrawingModel
import com.telekom.odsystem.charts.core.cartesian.data.CartesianLayerRangeProvider
import com.telekom.odsystem.charts.core.cartesian.layer.CandlestickCartesianLayer
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.ValueWrapper
import com.telekom.odsystem.charts.core.common.data.CartesianLayerDrawingModelInterpolator
import com.telekom.odsystem.charts.core.common.getValue
import com.telekom.odsystem.charts.core.common.setValue

/** Creates and remembers a [CandlestickCartesianLayer]. */
@Composable
fun rememberCandlestickCartesianLayer(
    candleProvider: CandlestickCartesianLayer.CandleProvider =
        CandlestickCartesianLayer.CandleProvider.absolute(),
    minCandleBodyHeight: Dp = Defaults.MIN_CANDLE_BODY_HEIGHT_DP.dp,
    candleSpacing: Dp = Defaults.CANDLE_SPACING_DP.dp,
    scaleCandleWicks: Boolean = false,
    rangeProvider: CartesianLayerRangeProvider = remember { CartesianLayerRangeProvider.auto() },
    verticalAxisPosition: Axis.Position.Vertical? = null,
    drawingModelInterpolator:
    CartesianLayerDrawingModelInterpolator<
            CandlestickCartesianLayerDrawingModel.Entry,
            CandlestickCartesianLayerDrawingModel,
            > =
        CartesianLayerDrawingModelInterpolator.default(),
): CandlestickCartesianLayer {
    var candlestickCartesianLayerWrapper by remember {
        ValueWrapper<CandlestickCartesianLayer?>(null)
    }
    return remember(
        candleProvider,
        minCandleBodyHeight,
        candleSpacing,
        scaleCandleWicks,
        rangeProvider,
        verticalAxisPosition,
        drawingModelInterpolator,
    ) {
        val candlestickCartesianLayer =
            candlestickCartesianLayerWrapper?.copy(
                candleProvider,
                minCandleBodyHeight.value,
                candleSpacing.value,
                scaleCandleWicks,
                rangeProvider,
                verticalAxisPosition,
                drawingModelInterpolator,
            )
                ?: CandlestickCartesianLayer(
                    candleProvider,
                    minCandleBodyHeight.value,
                    candleSpacing.value,
                    scaleCandleWicks,
                    rangeProvider,
                    verticalAxisPosition,
                    drawingModelInterpolator,
                )
        candlestickCartesianLayerWrapper = candlestickCartesianLayer
        candlestickCartesianLayer
    }
}
