package com.telekom.odsystem.charts.core.common.data

/** Interpolates two [CartesianLayerDrawingModel]s. */
interface CartesianLayerDrawingModelInterpolator<
  T : CartesianLayerDrawingModel.Entry,
  R : CartesianLayerDrawingModel<T>,
> {
  /** Sets the initial and target [CartesianLayerDrawingModel]s. */
  fun setModels(old: R?, new: R?)

  /**
   * Interpolates the two [CartesianLayerDrawingModel]s. [fraction] is the balance between the
   * initial and target [CartesianLayerDrawingModel]s, with 0 corresponding to the initial
   * [CartesianLayerDrawingModel], and 1 corresponding to the target [CartesianLayerDrawingModel].
   */
  suspend fun transform(fraction: Float): R?

  /** Houses a [CartesianLayerDrawingModelInterpolator] factory function. */
  companion object {
    /**
     * Creates an instance of the default [CartesianLayerDrawingModelInterpolator] implementation.
     */
    fun <T : CartesianLayerDrawingModel.Entry, R : CartesianLayerDrawingModel<T>> default():
      CartesianLayerDrawingModelInterpolator<T, R> = DefaultCartesianLayerDrawingModelInterpolator()
  }
}
