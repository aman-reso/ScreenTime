package com.telekom.odsystem.charts.core.common.shader

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Shader
import com.telekom.odsystem.charts.core.common.DrawingContext

internal data class BitmapShaderProvider(
  private val bitmap: Bitmap,
  private val xTileMode: Shader.TileMode,
  private val yTileMode: Shader.TileMode,
) : ShaderProvider {
  override fun getShader(
    context: DrawingContext,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  ) = BitmapShader(bitmap, xTileMode, yTileMode)
}
