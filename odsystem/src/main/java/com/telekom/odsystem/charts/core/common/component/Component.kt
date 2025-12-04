package com.telekom.odsystem.charts.core.common.component

import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.common.DrawingContext

/**
 * [Component] is a generic concept of an object that can be drawn on a canvas at a given pair of
 * coordinates. Its subclasses are used throughout the library.
 */
@Immutable
public interface Component {
  /** Instructs the [Component] to draw itself at the given coordinates. */
  public fun draw(context: DrawingContext, left: Float, top: Float, right: Float, bottom: Float)
}
