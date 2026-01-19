package com.telekom.odsystem.charts.core.cartesian

import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.common.MutableMeasuringContext
import com.telekom.odsystem.charts.core.common.Point
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel
import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartRanges
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerPadding

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class MutableCartesianMeasuringContext(
    override val canvasBounds: RectF,
    override var density: Float,
    override var extraStore: ExtraStore,
    override var isLtr: Boolean,
    spToPx: (Float) -> Float,
    override var model: CartesianChartModel,
    override var ranges: CartesianChartRanges,
    override var scrollEnabled: Boolean,
    override var zoomEnabled: Boolean,
    override var layerPadding: CartesianLayerPadding,
    override var pointerPosition: Point?,
    cacheStore: CacheStore = CacheStore(),
) :
    MutableMeasuringContext(canvasBounds, density, extraStore, isLtr, spToPx, cacheStore),
  CartesianMeasuringContext
