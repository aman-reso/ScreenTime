package com.telekom.odsystem.charts.core.common.shader

import android.graphics.Shader
import androidx.annotation.RestrictTo
import com.telekom.odsystem.charts.core.common.DrawingContext

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public abstract class CachingShaderProvider : ShaderProvider {
  private val cache = HashMap<String, Shader>(1)

  override fun getShader(
    context: DrawingContext,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  ): Shader {
    val cacheKey = createKey(left, top, right, bottom)
    return cache[cacheKey]
      ?: createShader(context, left, top, right, bottom).also { gradient ->
        cache.clear()
        cache[cacheKey] = gradient
      }
  }

  public abstract fun createShader(
    context: DrawingContext,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
  ): Shader

  protected open fun createKey(left: Float, top: Float, right: Float, bottom: Float): String =
    "$left,$top,$right,$bottom"
}
