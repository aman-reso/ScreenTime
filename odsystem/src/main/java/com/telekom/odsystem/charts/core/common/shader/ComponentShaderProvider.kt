package com.telekom.odsystem.charts.core.common.shader

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Shader
import com.telekom.odsystem.charts.core.common.DrawingContext
import com.telekom.odsystem.charts.core.common.half
import com.telekom.odsystem.charts.core.common.component.Component

internal data class ComponentShaderProvider(
  private val component: Component,
  private val componentSizeDp: Float,
  private val checker: Boolean = true,
  private val xTileMode: Shader.TileMode = Shader.TileMode.REPEAT,
  private val yTileMode: Shader.TileMode = xTileMode,
) : CachingShaderProvider() {
  override fun createShader(
    context: DrawingContext,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  ): Shader =
    with(context) {
      val size = componentSizeDp.pixels.toInt() * if (checker) 2 else 1
      val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

      withCanvas(Canvas(bitmap)) {
        if (checker) {
          val halfSize = componentSizeDp.pixels.half
          with(component) {
            draw(context, -halfSize, -halfSize, componentSizeDp.pixels)
            draw(context, -halfSize, size - halfSize, componentSizeDp.pixels)
            draw(context, size - halfSize, -halfSize, componentSizeDp.pixels)
            draw(context, size - halfSize, size - halfSize, componentSizeDp.pixels)
            draw(context, halfSize, halfSize, componentSizeDp.pixels)
          }
        } else {
          component.draw(context, 0f, 0f, componentSizeDp.pixels, componentSizeDp.pixels)
        }
      }
      return BitmapShader(bitmap, xTileMode, yTileMode)
    }

  private fun Component.draw(context: DrawingContext, x: Float, y: Float, size: Float) {
    draw(context, x, y, x + size, y + size)
  }
}
