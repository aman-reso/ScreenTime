package com.telekom.odsystem.charts.core.common.data

import com.telekom.odsystem.charts.core.cartesian.layer.CartesianLayer

/** Houses drawing information for a [CartesianLayer]. */
abstract class CartesianLayerDrawingModel<T : CartesianLayerDrawingModel.Entry>(
  private val entries: List<Map<Double, T>>
) : List<Map<Double, T>> by entries {
  /**
   * Returns an intermediate [CartesianLayerDrawingModel] between this one and [from]. The returned
   * drawing model includes the provided [Entry] list. [fraction] is the balance between [from] and
   * this [CartesianLayerDrawingModel], with 0 corresponding to [from], and 1 corresponding to this
   * [CartesianLayerDrawingModel]. The returned object should be an instance of the
   * [CartesianLayerDrawingModel] subclass to which this function belongs.
   */
  abstract fun transform(
    entries: List<Map<Double, T>>,
    from: CartesianLayerDrawingModel<T>?,
    fraction: Float,
  ): CartesianLayerDrawingModel<T>

  abstract override fun equals(other: Any?): Boolean

  abstract override fun hashCode(): Int

  /**
   * Houses positional information for a single [CartesianLayer] entity (e.g., a column or a point).
   */
  interface Entry {
    /**
     * Returns an intermediate [Entry] implementation between this one and [from]. [fraction] is the
     * balance between [from] and this [Entry] implementation, with 0 corresponding to [from], and 1
     * corresponding to this [Entry] implementation. The returned object should be an instance of
     * the [Entry] implementation to which this function belongs.
     */
    fun transform(from: Entry?, fraction: Float): Entry

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
  }
}
