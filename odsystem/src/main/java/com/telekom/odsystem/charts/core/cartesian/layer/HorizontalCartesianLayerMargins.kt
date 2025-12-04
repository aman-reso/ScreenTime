package com.telekom.odsystem.charts.core.cartesian.layer

/**
 * Stores the sizes of horizontal [CartesianLayer]-area margins.
 *
 * @see CartesianLayerMargins
 * @see CartesianLayerMarginUpdater
 */
public interface HorizontalCartesianLayerMargins {
  /** The start margin’s size. */
  public val start: Float

  /** The end margin’s size. */
  public val end: Float

  /** The sum of [start] and [end]. */
  public val horizontal: Float
    get() = start + end

  /** Returns the left margin’s size. */
  public fun getLeft(isLtr: Boolean): Float = if (isLtr) start else end

  /** Returns the right margin’s size. */
  public fun getRight(isLtr: Boolean): Float = if (isLtr) end else start

  /** Ensures that the stored values are no smaller than those provided. */
  public fun ensureValuesAtLeast(start: Float = this.start, end: Float = this.end)
}
