package com.telekom.odsystem.charts.core.common.shader

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.common.DrawingContext
import java.util.Objects

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class LinearGradientShaderProvider(
  private val colors: IntArray,
  private val positions: FloatArray?,
  private val isHorizontal: Boolean,
) : CachingShaderProvider() {
  override fun createShader(
    context: DrawingContext,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  ): Shader =
    if (isHorizontal) {
      LinearGradient(left, top, right, top, colors, positions, Shader.TileMode.CLAMP)
    } else {
      LinearGradient(left, top, left, bottom, colors, positions, Shader.TileMode.CLAMP)
    }

  override fun createKey(left: Float, top: Float, right: Float, bottom: Float): String =
    "$this$left,$top,$right,$bottom"

  override fun equals(other: Any?): Boolean =
    this === other ||
      other is LinearGradientShaderProvider &&
        colors.contentEquals(other.colors) &&
        positions.contentEquals(other.positions)

  override fun hashCode(): Int = Objects.hash(colors, positions)

  @OptIn(ExperimentalStdlibApi::class)
  override fun toString(): String =
    "LinearGradientShader(colors=" +
      "${colors.joinToString(prefix = "[", postfix = "]") { it.toHexString(HexFormat.UpperCase) } }, " +
      "positions=${positions?.joinToString(prefix = "[", postfix = "]") { it.toString() }}, " +
      "isHorizontal=$isHorizontal)"
}
