package com.telekom.odsystem.charts.core.common.data

import com.telekom.odsystem.charts.core.common.orZero
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlin.math.max

@Suppress("UNCHECKED_CAST")
internal class DefaultCartesianLayerDrawingModelInterpolator<
  T : CartesianLayerDrawingModel.Entry,
  R : CartesianLayerDrawingModel<T>,
> : CartesianLayerDrawingModelInterpolator<T, R> {
  private var transformationMaps = emptyList<Map<Double, TransformationModel<T>>>()
  private var oldDrawingModel: R? = null
  private var newDrawingModel: R? = null

  override fun setModels(old: R?, new: R?) {
    synchronized(this) {
      oldDrawingModel = old
      newDrawingModel = new
      updateTransformationMap()
    }
  }

  override suspend fun transform(fraction: Float): R? =
    newDrawingModel?.transform(
      entries =
        transformationMaps.mapNotNull { map ->
          map
            .mapNotNull { (x, model) ->
              currentCoroutineContext().ensureActive()
              model.transform(fraction)?.let { entry -> x to entry }
            }
            .takeIf { list -> list.isNotEmpty() }
            ?.toMap()
        },
      from = oldDrawingModel,
      fraction = fraction,
    ) as R?

  private fun updateTransformationMap() {
    transformationMaps = buildList {
      repeat(max(oldDrawingModel?.size.orZero, newDrawingModel?.size.orZero)) { index ->
        val map = mutableMapOf<Double, TransformationModel<T>>()
        oldDrawingModel?.getOrNull(index)?.forEach { (x, entry) ->
          map[x] = TransformationModel(entry)
        }
        newDrawingModel?.getOrNull(index)?.forEach { (x, entry) ->
          map[x] = TransformationModel(map[x]?.old, entry)
        }
        add(map)
      }
    }
  }

  private class TransformationModel<T : CartesianLayerDrawingModel.Entry>(
    val old: T?,
    val new: T? = null,
  ) {
    fun transform(fraction: Float): T? = new?.transform(old, fraction) as T?
  }
}
