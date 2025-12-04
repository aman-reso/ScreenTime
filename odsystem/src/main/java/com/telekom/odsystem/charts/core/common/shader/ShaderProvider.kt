package com.telekom.odsystem.charts.core.common.shader

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.BlendMode
import android.graphics.ComposeShader
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.common.DrawingContext
import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.shader.ShaderProvider.Companion.component

/** Creates [Shader]s on demand. */
@Immutable
public fun interface ShaderProvider {
  /** Returns a [Shader] for the given bounds. */
  public fun getShader(
    context: DrawingContext,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  ): Shader

  public companion object {
    /** Creates a [BitmapShader]-producing [ShaderProvider]. */
    public fun bitmap(
      bitmap: Bitmap,
      xTileMode: Shader.TileMode = Shader.TileMode.REPEAT,
      yTileMode: Shader.TileMode = xTileMode,
    ): ShaderProvider = BitmapShaderProvider(bitmap, xTileMode, yTileMode)

    /** Creates a [ComposeShader]-producing [ShaderProvider]. */
    @RequiresApi(Build.VERSION_CODES.Q)
    public fun compose(
      first: ShaderProvider,
      second: ShaderProvider,
      mode: BlendMode,
    ): ShaderProvider = ComposeShaderProvider(first, second, ComposeShaderProvider.Mode.Blend(mode))

    /** Creates a [ComposeShader]-producing [ShaderProvider]. */
    public fun compose(
      first: ShaderProvider,
      second: ShaderProvider,
      mode: PorterDuff.Mode,
    ): ShaderProvider =
      ComposeShaderProvider(first, second, ComposeShaderProvider.Mode.PorterDuff(mode))

    /** Creates a [ShaderProvider] that produces a horizontal gradient. */
    public fun horizontalGradient(vararg colors: Int): ShaderProvider = horizontalGradient(colors)

    /**
     * Creates a [ShaderProvider] that produces a horizontal gradient. [positions] specifies the
     * color-stop offsets (between 0 and 1), with `null` giving an even distribution.
     */
    public fun horizontalGradient(colors: IntArray, positions: FloatArray? = null): ShaderProvider =
      LinearGradientShaderProvider(colors, positions, true)

    /** Creates a [ShaderProvider] that produces a vertical gradient. */
    public fun verticalGradient(vararg colors: Int): ShaderProvider = verticalGradient(colors)

    /**
     * Creates a [ShaderProvider] that produces a vertical gradient. [positions] specifies the
     * color-stop offsets (between 0 and 1), with `null` giving an even distribution.
     */
    public fun verticalGradient(colors: IntArray, positions: FloatArray? = null): ShaderProvider =
      LinearGradientShaderProvider(colors, positions, false)

    /**
     * Creates a [ShaderProvider] that produces [Shader]s wherein [component] is repeatedly drawn in
     * a grid or checkered pattern.
     */
    public fun component(
      component: Component,
      componentSizeDp: Float,
      checker: Boolean = true,
      xTileMode: Shader.TileMode = Shader.TileMode.REPEAT,
      yTileMode: Shader.TileMode = xTileMode,
    ): ShaderProvider =
      ComponentShaderProvider(component, componentSizeDp, checker, xTileMode, yTileMode)
  }
}

/** Converts this [Shader] to a [ShaderProvider]. */
public fun Shader.toShaderProvider(): ShaderProvider = ShaderProvider { _, _, _, _, _ -> this }

internal fun ShaderProvider.getShader(context: DrawingContext, bounds: RectF) =
  getShader(context, bounds.left, bounds.top, bounds.right, bounds.bottom)
