package com.telekom.odsystem.charts.core.common.shape

import android.graphics.Path
import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.common.MeasuringContext

/** Defines a shape. */
@Immutable
fun interface Shape {
  /**
   * Adds an outline of the [Shape] to [path]. [left], [top], [right], and [bottom] define the
   * outline bounds.
   */
  fun outline(
    context: MeasuringContext,
    path: Path,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  )

  companion object {
    /** A rectangle with sharp corners. */
    val Rectangle: Shape = Shape { _, path, left, top, right, bottom ->
      path.moveTo(left, top)
      path.lineTo(right, top)
      path.lineTo(right, bottom)
      path.lineTo(left, bottom)
      path.close()
    }
  }
}
