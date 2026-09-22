package com.telekom.odsystem.charts.core.cartesian.layer

import com.telekom.odsystem.charts.core.cartesian.CartesianChart
import com.telekom.odsystem.charts.core.cartesian.CartesianMeasuringContext

/**
 * Enables a [CartesianChart] component to make room for itself around the [CartesianLayer] area.
 */
interface CartesianLayerMarginUpdater<M> {
  /** Ensures that there are sufficient [CartesianLayer]-area margins. */
  fun updateLayerMargins(
    context: CartesianMeasuringContext,
    layerMargins: CartesianLayerMargins,
    layerDimensions: CartesianLayerDimensions,
    model: M,
  ) {}

  /** Ensures that there are sufficient horizontal [CartesianLayer]-area margins. */
  fun updateHorizontalLayerMargins(
    context: CartesianMeasuringContext,
    horizontalLayerMargins: HorizontalCartesianLayerMargins,
    layerHeight: Float,
    model: M,
  ) {}
}
