package com.telekom.odsystem.charts.core.cartesian.layer

import androidx.compose.runtime.Immutable

/**
 * Stores [CartesianLayer] padding sizes. Scalable padding depends on the zoom factor.
 *
 * @property scalableStartDp the size of the scalable start padding (in dp).
 * @property scalableEndDp the size of the scalable end padding (in dp).
 * @property unscalableStartDp the size of the unscalable start padding (in dp).
 * @property unscalableEndDp the size of the unscalable end padding (in dp).
 */
@Immutable
class CartesianLayerPadding(
  val scalableStartDp: Float = 0f,
  val scalableEndDp: Float = 0f,
  val unscalableStartDp: Float = 0f,
  val unscalableEndDp: Float = 0f,
) {
  override fun equals(other: Any?): Boolean =
    this === other ||
      other is CartesianLayerPadding &&
        scalableStartDp == other.scalableStartDp &&
        scalableEndDp == other.scalableEndDp &&
        unscalableStartDp == other.unscalableStartDp &&
        unscalableEndDp == other.unscalableEndDp

  override fun hashCode(): Int {
    var result = scalableStartDp.hashCode()
    result = 31 * result + scalableEndDp.hashCode()
    result = 31 * result + unscalableStartDp.hashCode()
    result = 31 * result + unscalableEndDp.hashCode()
    return result
  }
}
