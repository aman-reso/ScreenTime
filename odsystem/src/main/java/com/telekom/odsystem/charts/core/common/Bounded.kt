package com.telekom.odsystem.charts.core.common

import android.graphics.RectF

/** Defines an abstract component that has some physical bounds. */
public interface Bounded {
  /** The bounds of the abstract component. */
  public val bounds: RectF

  /** Sets the coordinates of the bounds to the provided values. */
  public fun setBounds(left: Number, top: Number, right: Number, bottom: Number) {
    bounds.set(left, top, right, bottom)
  }
}
