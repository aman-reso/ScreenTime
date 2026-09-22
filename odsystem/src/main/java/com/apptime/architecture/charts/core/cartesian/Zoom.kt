package com.telekom.odsystem.charts.core.cartesian

import android.graphics.RectF
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerDimensions
import kotlin.math.max
import kotlin.math.min

/** Represents a [CartesianChart]’s zoom factor. */
fun interface Zoom {
  /** Returns the zoom factor. */
  fun getValue(
    context: CartesianMeasuringContext,
    layerDimensions: CartesianLayerDimensions,
    bounds: RectF,
  ): Float

  /** Houses [Zoom] singletons and factory functions. */
  companion object {
    /** Ensures all of the [CartesianChart]’s content is visible. */
    val Content: Zoom = Zoom { context, layerDimensions, bounds ->
      val scalableContentWidth = layerDimensions.getScalableContentWidth(context)
      if (scalableContentWidth == 0f) {
        1f
      } else {
        (bounds.width() - layerDimensions.unscalablePadding) / scalableContentWidth
      }
    }

    /** Uses a zoom factor of [value]. */
    fun fixed(value: Float = 1f): Zoom = Zoom { _, _, _ -> value }

    /** Ensures the specified number of _x_ units is visible. */
    fun x(x: Double): Zoom = Zoom { context, layerDimensions, bounds ->
      bounds.width() * (context.ranges.xStep / x).toFloat() / layerDimensions.xSpacing
    }

    /** Uses the smaller of [a]’s zoom factor and [b]’s zoom factor. */
    fun min(a: Zoom, b: Zoom): Zoom = Zoom { context, layerDimensions, bounds ->
      min(
        a.getValue(context, layerDimensions, bounds),
        b.getValue(context, layerDimensions, bounds),
      )
    }

    /** Uses the greater of [a]’s zoom factor and [b]’s zoom factor. */
    fun max(a: Zoom, b: Zoom): Zoom = Zoom { context, layerDimensions, bounds ->
      max(
        a.getValue(context, layerDimensions, bounds),
        b.getValue(context, layerDimensions, bounds),
      )
    }
  }
}
