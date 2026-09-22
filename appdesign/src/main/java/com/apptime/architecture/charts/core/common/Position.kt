package com.telekom.odsystem.charts.core.common

import android.graphics.RectF

/** Defines the relative position of an object. */
object Position {
  /** Defines the relative horizontal position of an object. */
  enum class Horizontal {
    Start,
    Center,
    End,
  }

  /** Defines the relative vertical position of an object. */
  enum class Vertical {
    Top,
    Center,
    Bottom,
  }
}

internal operator fun Position.Horizontal.unaryMinus() =
  when (this) {
    Position.Horizontal.Start -> Position.Horizontal.End
    Position.Horizontal.Center -> Position.Horizontal.Center
    Position.Horizontal.End -> Position.Horizontal.Start
  }

internal operator fun Position.Vertical.unaryMinus() =
  when (this) {
    Position.Vertical.Top -> Position.Vertical.Bottom
    Position.Vertical.Center -> Position.Vertical.Center
    Position.Vertical.Bottom -> Position.Vertical.Top
  }

internal fun Position.Vertical.inBounds(
  bounds: RectF,
  componentHeight: Float,
  referenceY: Float,
  referenceDistance: Float = 0f,
): Position.Vertical {
  val topFits = referenceY - referenceDistance - componentHeight >= bounds.top
  val centerFits =
    referenceY - componentHeight.half >= bounds.top &&
      referenceY + componentHeight.half <= bounds.bottom
  val bottomFits = referenceY + referenceDistance + componentHeight <= bounds.bottom
  return when (this) {
    Position.Vertical.Top -> if (topFits) this else Position.Vertical.Bottom
    Position.Vertical.Bottom -> if (bottomFits) this else Position.Vertical.Top
    Position.Vertical.Center ->
      when {
        centerFits -> this
        topFits -> Position.Vertical.Top
        else -> Position.Vertical.Bottom
      }
  }
}
