package com.telekom.odsystem.charts.core.common.data

/** Interpolates two [CartesianLayerDrawingModel]s. */
public interface CartesianLayerDrawingModelInterpolator<
  T : CartesianLayerDrawingModel.Entry,
  R : CartesianLayerDrawingModel<T>,
> {
  /** Sets the initial and target [CartesianLayerDrawingModel]s. */
  public fun setModels(old: R?, new: R?)

  /**
   * Interpolates the two [CartesianLayerDrawingModel]s. [fraction] is the balance between the
   * initial and target [CartesianLayerDrawingModel]s, with 0 corresponding to the initial
   * [CartesianLayerDrawingModel], and 1 corresponding to the target [CartesianLayerDrawingModel].
   */
  public suspend fun transform(fraction: Float): R?

  /** Houses a [CartesianLayerDrawingModelInterpolator] factory function. */
  public companion object {
    /**
     * Creates an instance of the default [CartesianLayerDrawingModelInterpolator] implementation.
     */
    public fun <T : CartesianLayerDrawingModel.Entry, R : CartesianLayerDrawingModel<T>> default():
      CartesianLayerDrawingModelInterpolator<T, R> = DefaultCartesianLayerDrawingModelInterpolator()
  }
}
