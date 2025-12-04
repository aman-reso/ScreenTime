package com.telekom.odsystem.charts.compose.cartesian

import android.graphics.RectF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext
import com.telekom.odsystem.charts.core.cartesian.MutableCartesianMeasuringContext
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartRanges
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerPadding
import com.telekom.odsystem.charts.core.common.Point
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.data.ExtraStore

@Composable
internal fun rememberCartesianMeasuringContext(
    canvasBounds: RectF,
    extraStore: ExtraStore,
    model: CartesianChartModel,
    ranges: CartesianChartRanges,
    scrollEnabled: Boolean,
    zoomEnabled: Boolean,
    layerPadding: CartesianLayerPadding,
    pointerPosition: Point?,
): CartesianMeasuringContext {
    val density = LocalDensity.current
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val cacheStore = remember { CacheStore() }
    return remember(
        canvasBounds,
        density,
        extraStore,
        isLtr,
        model,
        ranges,
        scrollEnabled,
        zoomEnabled,
        layerPadding,
        pointerPosition,
        cacheStore,
    ) {
        MutableCartesianMeasuringContext(
            canvasBounds = canvasBounds,
            density = density.density,
            extraStore = extraStore,
            isLtr = isLtr,
            spToPx = density.run { { it.sp.toPx() } },
            model = model,
            ranges = ranges,
            scrollEnabled = scrollEnabled,
            zoomEnabled = zoomEnabled,
            layerPadding = layerPadding,
            pointerPosition = pointerPosition,
            cacheStore = cacheStore,
        )
    }
}
