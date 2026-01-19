package com.telekom.odsystem.charts.core.cartesian.layer

/**
 * Stores the sizes of horizontal [CartesianLayer]-area margins.
 *
 * @see CartesianLayerMargins
 * @see CartesianLayerMarginUpdater
 */
interface HorizontalCartesianLayerMargins {
  /** The start margin’s size. */
  val start: Float

  /** The end margin’s size. */
  val end: Float

  /** The sum of [start] and [end]. */
  val horizontal: Float
    get() = start + end

  /** Returns the left margin’s size. */
  fun getLeft(isLtr: Boolean): Float = if (isLtr) start else end

  /** Returns the right margin’s size. */
  fun getRight(isLtr: Boolean): Float = if (isLtr) end else start

  /** Ensures that the stored values are no smaller than those provided. */
  fun ensureValuesAtLeast(start: Float = this.start, end: Float = this.end)
}
