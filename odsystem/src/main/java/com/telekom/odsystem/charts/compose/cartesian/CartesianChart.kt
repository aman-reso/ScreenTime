package com.telekom.odsystem.charts.compose.cartesian

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.telekom.odsystem.charts.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.telekom.odsystem.charts.compose.cartesian.layer.rememberColumnCartesianLayer
import com.telekom.odsystem.charts.compose.cartesian.layer.rememberLineCartesianLayer
import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext
import com.telekom.odsystem.charts.core.cartesian.FadingEdges
import com.telekom.odsystem.charts.core.cartesian.axis.Axis
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel
import com.telekom.odsystem.charts.core.cartesian.decoration.Decoration
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayer
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerPadding
import com.telekom.odsystem.charts.core.cartesian.marker.CartesianMarker
import com.telekom.odsystem.charts.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.telekom.odsystem.charts.core.common.Legend
import com.telekom.odsystem.charts.core.common.ValueWrapper
import com.telekom.odsystem.charts.core.common.data.ExtraStore

/**
 * Creates and remembers a [CartesianChart].
 *
 * @see rememberCandlestickCartesianLayer
 * @see rememberColumnCartesianLayer
 * @see rememberLineCartesianLayer
 */
@Composable
public fun rememberCartesianChart(
    vararg layers: CartesianLayer<*>,
    startAxis: Axis<Axis.Position.Vertical.Start>? = null,
    topAxis: Axis<Axis.Position.Horizontal.Top>? = null,
    endAxis: Axis<Axis.Position.Vertical.End>? = null,
    bottomAxis: Axis<Axis.Position.Horizontal.Bottom>? = null,
    marker: CartesianMarker? = null,
    markerVisibilityListener: CartesianMarkerVisibilityListener? = null,
    layerPadding: ((ExtraStore) -> CartesianLayerPadding) = { cartesianLayerPadding() },
    legend: Legend<CartesianMeasuringContext, CartesianDrawingContext>? = null,
    fadingEdges: FadingEdges? = null,
    decorations: List<Decoration> = emptyList(),
    persistentMarkers: (CartesianChart.PersistentMarkerScope.(ExtraStore) -> Unit)? = null,
    getXStep: ((CartesianChartModel) -> Double) = { it.getXDeltaGcd() },
): CartesianChart {
    val wrapper = remember { ValueWrapper<CartesianChart?>(null) }
    return remember(
        *layers,
        startAxis,
        topAxis,
        endAxis,
        bottomAxis,
        marker,
        markerVisibilityListener,
        layerPadding,
        legend,
        fadingEdges,
        decorations,
        persistentMarkers,
        getXStep,
    ) {
        val cartesianChart =
            wrapper.value?.copy(
                *layers,
                startAxis = startAxis,
                topAxis = topAxis,
                endAxis = endAxis,
                bottomAxis = bottomAxis,
                marker = marker,
                markerVisibilityListener = markerVisibilityListener,
                layerPadding = layerPadding,
                legend = legend,
                fadingEdges = fadingEdges,
                decorations = decorations,
                persistentMarkers = persistentMarkers,
                getXStep = getXStep,
            )
                ?: CartesianChart(
                    *layers,
                    startAxis = startAxis,
                    topAxis = topAxis,
                    endAxis = endAxis,
                    bottomAxis = bottomAxis,
                    marker = marker,
                    markerVisibilityListener = markerVisibilityListener,
                    layerPadding = layerPadding,
                    legend = legend,
                    fadingEdges = fadingEdges,
                    decorations = decorations,
                    persistentMarkers = persistentMarkers,
                    getXStep = getXStep,
                )
        wrapper.value = cartesianChart
        cartesianChart
    }
}
