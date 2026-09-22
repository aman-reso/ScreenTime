package com.telekom.odsystem.charts.core.common

import android.graphics.Canvas
import android.graphics.RectF
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.data.ExtraStore

/** Holds data used for measuring and drawing. */
interface MeasuringContext {
  /** The bounds of the [Canvas]. */
  val canvasBounds: RectF

  /** The number of pixels corresponding to one density-independent pixel. */
  val density: Float

  /** Houses auxiliary drawing data. */
  val extraStore: ExtraStore

  /** The number of pixels corresponding to this number of density-independent pixels. */
  val Float.pixels: Float
    get() = this * density

  /**
   * The number of pixels corresponding to this number of density-independent pixels, rounded down
   * to an integer.
   */
  val Float.wholePixels: Int
    get() = pixels.toInt()

  /** Returns the number of pixels corresponding to [dp] density-independent pixels. */
  fun dpToPx(dp: Float): Float = dp * density

  /** Returns the number of pixels corresponding to [sp] scalable pixels. */
  fun spToPx(sp: Float): Float

  /** Whether the layout direction is left to right. */
  val isLtr: Boolean

  /** Caches drawing data. */
  val cacheStore: CacheStore

  /** 1 if [isLtr] is true; −1 otherwise. */
  val layoutDirectionMultiplier: Int
    get() = if (isLtr) 1 else -1

  /** Removes all temporary data. */
  fun reset() {
    cacheStore.purge()
  }
}
