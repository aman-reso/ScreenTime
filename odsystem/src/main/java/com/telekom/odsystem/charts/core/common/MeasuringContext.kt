package com.telekom.odsystem.charts.core.common

import android.graphics.Canvas
import android.graphics.RectF
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.data.ExtraStore

/** Holds data used for measuring and drawing. */
public interface MeasuringContext {
  /** The bounds of the [Canvas]. */
  public val canvasBounds: RectF

  /** The number of pixels corresponding to one density-independent pixel. */
  public val density: Float

  /** Houses auxiliary drawing data. */
  public val extraStore: ExtraStore

  /** The number of pixels corresponding to this number of density-independent pixels. */
  public val Float.pixels: Float
    get() = this * density

  /**
   * The number of pixels corresponding to this number of density-independent pixels, rounded down
   * to an integer.
   */
  public val Float.wholePixels: Int
    get() = pixels.toInt()

  /** Returns the number of pixels corresponding to [dp] density-independent pixels. */
  public fun dpToPx(dp: Float): Float = dp * density

  /** Returns the number of pixels corresponding to [sp] scalable pixels. */
  public fun spToPx(sp: Float): Float

  /** Whether the layout direction is left to right. */
  public val isLtr: Boolean

  /** Caches drawing data. */
  public val cacheStore: CacheStore

  /** 1 if [isLtr] is true; −1 otherwise. */
  public val layoutDirectionMultiplier: Int
    get() = if (isLtr) 1 else -1

  /** Removes all temporary data. */
  public fun reset() {
    cacheStore.purge()
  }
}
