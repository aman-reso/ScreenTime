package com.telekom.odsystem.charts.core.cartesian.layer

/**
 * Stores the sizes of [CartesianLayer]-area margins.
 *
 * @see CartesianLayerMarginUpdater
 */
public class CartesianLayerMargins : HorizontalCartesianLayerMargins {
  /** The start margin’s size. */
  public override var start: Float = 0f
    private set

  /** The top margin’s size. */
  public var top: Float = 0f
    private set

  /** The end margin’s size. */
  public override var end: Float = 0f
    private set

  /** The bottom margin’s size. */
  public var bottom: Float = 0f
    private set

  /** The sum of [top] and [bottom]. */
  public val vertical: Float
    get() = top + bottom

  /** The largest of [start], [top], [end], and [bottom]. */
  public val max: Float
    get() = maxOf(start, top, end, bottom)

  override fun ensureValuesAtLeast(start: Float, end: Float) {
    this.start = this.start.coerceAtLeast(start)
    this.end = this.end.coerceAtLeast(end)
  }

  /** Ensures that the stored values are no smaller than those provided. */
  public fun ensureValuesAtLeast(
    start: Float = this.start,
    top: Float = this.top,
    end: Float = this.end,
    bottom: Float = this.bottom,
  ) {
    this.start = this.start.coerceAtLeast(start)
    this.top = this.top.coerceAtLeast(top)
    this.end = this.end.coerceAtLeast(end)
    this.bottom = this.bottom.coerceAtLeast(bottom)
  }

  /** Clears the stored values. */
  public fun clear() {
    start = 0f
    top = 0f
    end = 0f
    bottom = 0f
  }
}
