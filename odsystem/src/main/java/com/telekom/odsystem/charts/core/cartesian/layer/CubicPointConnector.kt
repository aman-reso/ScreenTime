package com.telekom.odsystem.charts.core.cartesian.layer

import android.graphics.Path
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import kotlin.math.abs

internal data class CubicPointConnector(private val curvature: Float) :
  LineCartesianLayer.PointConnector {
  init {
    require(curvature > 0 && curvature <= 1) { "`curvature` must be in (0, 1]." }
  }

  override fun connect(
    context: CartesianDrawingContext,
    path: Path,
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
  ) {
    val xDelta =
      (Y_MULTIPLIER * abs(y2 - y1) / context.layerBounds.height()).coerceAtMost(1f) *
        curvature *
        (x2 - x1)
    path.cubicTo(x1 + xDelta, y1, x2 - xDelta, y2, x2, y2)
  }

  private companion object {
    const val Y_MULTIPLIER = 4
  }
}
