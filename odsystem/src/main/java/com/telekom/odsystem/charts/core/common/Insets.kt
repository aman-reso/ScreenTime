package com.telekom.odsystem.charts.core.common

import androidx.compose.runtime.Immutable

/**
 * Stores inset sizes for the sides of a rectangle. Used for margins and padding.
 *
 * @param startDp the start inset’s size (in dp).
 * @param topDp the top inset’s size (in dp).
 * @param endDp the end inset’s size (in dp).
 * @param bottomDp the bottom inset’s size (in dp).
 */
@Immutable
class Insets(
  val startDp: Float = 0f,
  val topDp: Float = 0f,
  val endDp: Float = 0f,
  val bottomDp: Float = 0f,
) {
  /** The sum of [startDp] and [endDp]. */
  val horizontalDp: Float
    get() = startDp + endDp

  /** The sum of [topDp] and [bottomDp]. */
  val verticalDp: Float
    get() = topDp + bottomDp

  /** Creates an [Insets] instance with [startDp] = [endDp] and [topDp] = [bottomDp]. */
  constructor(
    horizontalDp: Float = 0f,
    verticalDp: Float = 0f,
  ) : this(horizontalDp, verticalDp, horizontalDp, verticalDp)

  /** Creates an [Insets] instance with a common size for all four insets. */
  constructor(allDp: Float = 0f) : this(allDp, allDp, allDp, allDp)

  /** Returns the left inset’s size. */
  fun getLeft(context: MeasuringContext): Float =
    with(context) { (if (isLtr) startDp else endDp).pixels }

  /** Returns the right inset’s size. */
  fun getRight(context: MeasuringContext): Float =
    with(context) { (if (isLtr) endDp else startDp).pixels }

  override fun equals(other: Any?): Boolean =
    this === other ||
      other is Insets &&
        startDp == other.startDp &&
        topDp == other.topDp &&
        endDp == other.endDp &&
        bottomDp == other.bottomDp

  override fun hashCode(): Int {
    var result = startDp.hashCode()
    result = 31 * result + topDp.hashCode()
    result = 31 * result + endDp.hashCode()
    result = 31 * result + bottomDp.hashCode()
    return result
  }

  /** Houses an [Insets] singleton. */
  companion object {
    /** An [Insets] instance with a size of zero for all four insets. */
    val Zero: Insets = Insets(0f, 0f, 0f, 0f)
  }
}
