package com.telekom.odsystem.charts.core.common

import androidx.compose.runtime.Immutable

/** Defines the functions required by the library to draw a chart legend. */
@Immutable
public interface Legend<M : MeasuringContext, D : DrawingContext> : Bounded {
  /** Returns the height of the legend. */
  public fun getHeight(context: M, maxWidth: Float): Float

  /** Draws the legend. */
  public fun draw(context: D)
}
