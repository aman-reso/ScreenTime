package com.telekom.odsystem.charts.core.common

import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.data.ExtraStore

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public open class MutableMeasuringContext(
  override val canvasBounds: RectF,
  override var density: Float,
  override val extraStore: ExtraStore,
  override var isLtr: Boolean,
  private var spToPx: (Float) -> Float,
  override val cacheStore: CacheStore = CacheStore(),
) : MeasuringContext {
  override fun spToPx(sp: Float): Float = spToPx.invoke(sp)
}
