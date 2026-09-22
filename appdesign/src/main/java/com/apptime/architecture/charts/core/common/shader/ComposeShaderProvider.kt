package com.telekom.odsystem.charts.core.common.shader

import android.graphics.BlendMode
import android.graphics.ComposeShader
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import com.telekom.odsystem.charts.core.common.DrawingContext

internal data class ComposeShaderProvider(
  private val first: ShaderProvider,
  private val second: ShaderProvider,
  private val mode: Mode,
) : ShaderProvider {
  override fun getShader(
    context: DrawingContext,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  ) =
    mode.createShader(
      first.getShader(context, left, top, right, bottom),
      second.getShader(context, left, top, right, bottom),
    )

  interface Mode {
    fun createShader(first: Shader, second: Shader): ComposeShader

    @RequiresApi(Build.VERSION_CODES.Q)
    data class Blend(private val mode: BlendMode) : Mode {
      override fun createShader(first: Shader, second: Shader) = ComposeShader(first, second, mode)
    }

    data class PorterDuff(private val mode: android.graphics.PorterDuff.Mode) : Mode {
      override fun createShader(first: Shader, second: Shader) = ComposeShader(first, second, mode)
    }
  }
}
