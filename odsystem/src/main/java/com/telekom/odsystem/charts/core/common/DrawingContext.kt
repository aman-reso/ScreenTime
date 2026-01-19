package com.telekom.odsystem.charts.core.common

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.data.ExtraStore
import kotlin.math.roundToInt

/** A [MeasuringContext] extension with a [Canvas] reference. */
interface DrawingContext : MeasuringContext {
  /** The canvas to draw the chart on. */
  val canvas: Canvas

  /**
   * Updates the value of [DrawingContext.canvas] to [canvas], runs [block], and restores the
   * previous [DrawingContext.canvas] value.
   */
  fun withCanvas(canvas: Canvas, block: () -> Unit)
}

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun DrawingContext(
  canvas: Canvas,
  density: Float = 1f,
  isLtr: Boolean = true,
  canvasBounds: RectF = RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()),
  spToPx: (Float) -> Float = { it },
): DrawingContext =
  object : DrawingContext {
    override val canvasBounds: RectF = canvasBounds

    override var canvas: Canvas = canvas

    override val density: Float = density

    override val extraStore: ExtraStore = ExtraStore.Empty

    override val isLtr: Boolean = isLtr

    override val cacheStore: CacheStore = CacheStore()

    override fun withCanvas(canvas: Canvas, block: () -> Unit) {
      val originalCanvas = this.canvas
      this.canvas = canvas
      block()
      this.canvas = originalCanvas
    }

    override fun spToPx(sp: Float): Float = spToPx(sp)
  }

internal fun DrawingContext.getBitmap(
  cacheKeyNamespace: CacheStore.KeyNamespace,
  vararg cacheKeyComponents: Any,
): Bitmap {
  val width = canvasBounds.width().roundToInt()
  val height = canvasBounds.height().roundToInt()
  return cacheStore
    .getOrNull<Bitmap>(cacheKeyNamespace, *cacheKeyComponents, width, height)
    ?.apply { eraseColor(Color.TRANSPARENT) }
    ?: Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
      cacheStore.set(cacheKeyNamespace, *cacheKeyComponents, width, height, value = it)
    }
}
