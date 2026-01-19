package com.telekom.odsystem.charts.core.common

import androidx.compose.runtime.Immutable

/** Defines the functions required by the library to draw a chart legend. */
@Immutable
interface Legend<M : MeasuringContext, D : DrawingContext> : Bounded {
  /** Returns the height of the legend. */
  fun getHeight(context: M, maxWidth: Float): Float

  /** Draws the legend. */
  fun draw(context: D)
}
