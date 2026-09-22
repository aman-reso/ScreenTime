package com.telekom.odsystem.charts.compose.common

import androidx.annotation.RestrictTo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.telekom.odsystem.charts.compose.common.VicoTheme.Companion.Dark
import com.telekom.odsystem.charts.compose.common.VicoTheme.Companion.Light
import com.telekom.odsystem.charts.core.cartesian.axis.HorizontalAxis
import com.telekom.odsystem.charts.core.cartesian.axis.VerticalAxis
import com.telekom.odsystem.charts.core.cartesian.layer.ColumnCartesianLayer
import com.telekom.odsystem.charts.core.common.DefaultColors
import com.telekom.odsystem.charts.core.common.component.LineComponent

/**
 * Houses default chart colors.
 *
 * @param candlestickCartesianLayerColors houses default [CandlestickCartesianLayer.Candle] colors.
 * @param columnCartesianLayerColors used for [ColumnCartesianLayer]&#0020;[LineComponent]s.
 * @param lineCartesianLayerColors used for [LineCartesianLayer.Line]s.
 * @param lineColor used for [HorizontalAxis] and [VerticalAxis] lines.
 * @param textColor used for [HorizontalAxis] and [VerticalAxis] labels.
 */
data class VicoTheme(
    val candlestickCartesianLayerColors: CandlestickCartesianLayerColors,
    val columnCartesianLayerColors: List<Color>,
    val lineCartesianLayerColors: List<Color> = columnCartesianLayerColors,
    val lineColor: Color,
    val textColor: Color,
) {
    /**
     * Houses default [CandlestickCartesianLayer.Candle] colors.
     *
     * @property bullish used for bullish [CandlestickCartesianLayer.Candle]s.
     * @property neutral used for neutral [CandlestickCartesianLayer.Candle]s.
     * @property bearish used for bearish [CandlestickCartesianLayer.Candle]s.
     */
    data class CandlestickCartesianLayerColors(
        val bullish: Color,
        val neutral: Color,
        val bearish: Color,
    ) {
        /** @suppress */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        companion object {
            fun fromDefaultColors(defaultColors: DefaultColors): CandlestickCartesianLayerColors =
                CandlestickCartesianLayerColors(
                    Color(defaultColors.bullishCandleColor),
                    Color(defaultColors.neutralCandleColor),
                    Color(defaultColors.bearishCandleColor),
                )
        }
    }

    internal companion object {
        val Light = fromDefaultColors(DefaultColors.Light)

        val Dark = fromDefaultColors(DefaultColors.Dark)

        fun fromDefaultColors(defaultColors: DefaultColors) =
            VicoTheme(
                candlestickCartesianLayerColors =
                    CandlestickCartesianLayerColors.fromDefaultColors(defaultColors),
                columnCartesianLayerColors = defaultColors.cartesianLayerColors.map(::Color),
                lineColor = Color(defaultColors.lineColor),
                textColor = Color(defaultColors.textColor),
            )
    }
}

private val LocalVicoTheme = staticCompositionLocalOf<VicoTheme?> { null }

/** The current [VicoTheme]. */
val vicoTheme: VicoTheme
    @Composable get() = LocalVicoTheme.current ?: if (isSystemInDarkTheme()) Dark else Light

/** Provides a [VicoTheme]. */
@Composable
fun ProvideVicoTheme(theme: VicoTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalVicoTheme provides theme, content)
}
