package com.telekom.odsystem.charts.core.cartesian

import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.cartesian.Scroll.Absolute
import com.telekom.odsystem.charts.core.cartesian.Scroll.Relative
import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayerDimensions

/** Represents a [CartesianChart] scroll value or delta. */
public sealed interface Scroll {
  /** Represents a [CartesianChart] scroll value. */
  public fun interface Absolute : Scroll {
    /** Returns the scroll value. */
    public fun getValue(
      context: CartesianMeasuringContext,
      layerDimensions: CartesianLayerDimensions,
      bounds: RectF,
      maxValue: Float,
    ): Float

    /** Houses [Scroll.Absolute] singletons and factory functions. */
    public companion object {
      /** Corresponds to zero. */
      public val Start: Absolute = Absolute { _, _, _, _ -> 0f }

      /** Corresponds to the maximum scroll value. */
      public val End: Absolute = Absolute { _, _, _, maxValue -> maxValue }

      /** Uses a scroll value of the specified number of pixels. */
      public fun pixels(pixels: Float): Absolute = Absolute { _, _, _, _ -> pixels }

      /**
       * Scrolls to the specified _x_ coordinate, positioning it anywhere between the start edge
       * ([bias] = 0) and the end edge ([bias] = 1) of the [CartesianChart].
       */
      public fun x(x: Double, bias: Float = 0f): Absolute =
        Absolute { context, layerDimensions, bounds, _ ->
          layerDimensions.startPadding +
            ((x - context.ranges.minX) / context.ranges.xStep).toFloat() *
              layerDimensions.xSpacing - bias * bounds.width()
        }
    }
  }

  /** Represents a [CartesianChart] scroll delta. */
  public fun interface Relative : Scroll {
    /** Returns the scroll delta. */
    public fun getDelta(
      context: CartesianMeasuringContext,
      layerDimensions: CartesianLayerDimensions,
      bounds: RectF,
      maxValue: Float,
    ): Float

    /** Houses [Scroll.Relative] factory functions. */
    public companion object {
      /** Scrolls by the specified number of pixels. */
      public fun pixels(pixels: Float): Relative = Relative { _, _, _, _ -> pixels }

      /** Scrolls by the specified number of _x_ units. */
      public fun x(x: Double): Relative = Relative { context, layerDimensions, _, _ ->
        (x / context.ranges.xStep).toFloat() * layerDimensions.xSpacing
      }
    }
  }
}

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun Scroll.getDelta(
  context: CartesianMeasuringContext,
  layerDimensions: CartesianLayerDimensions,
  bounds: RectF,
  maxValue: Float,
  value: Float,
): Float =
  when (this) {
    is Absolute -> getValue(context, layerDimensions, bounds, maxValue) - value
    is Relative -> getDelta(context, layerDimensions, bounds, maxValue)
  }
