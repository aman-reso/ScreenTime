package com.telekom.odsystem.charts.core.common

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.telekom.odsystem.charts.core.common.data.CacheStore
import com.telekom.odsystem.charts.core.common.shader.ShaderProvider

/**
 * Stores fill properties.
 *
 * @property color the color. If [shaderProvider] is not null, this is [Color.BLACK].
 * @property shaderProvider the [ShaderProvider].
 */
public class Fill
private constructor(public val color: Int, public val shaderProvider: ShaderProvider?) {
  /** Creates a color [Fill]. */
  public constructor(color: Int) : this(color = color, shaderProvider = null)

  /** Creates a [ShaderProvider]&#0020;[Fill]. */
  public constructor(shaderProvider: ShaderProvider) : this(Color.BLACK, shaderProvider)

  override fun equals(other: Any?): Boolean =
    this === other ||
      other is Fill && color == other.color && shaderProvider == other.shaderProvider

  override fun hashCode(): Int = 31 * color + shaderProvider?.hashCode().orZero

  /** Houses [Fill] singletons. */
  public companion object {
    /** A black [Fill]. */
    public val Black: Fill = Fill(Color.BLACK)

    /** A transparent [Fill]. */
    public val Transparent: Fill = Fill(Color.TRANSPARENT)
  }
}

private val canvas = Canvas()
private val paint = Paint()
private val cacheKeyNamespace = CacheStore.KeyNamespace()

internal fun Fill.extractColor(
  context: DrawingContext,
  width: Float,
  height: Float,
  side: Int = 1,
): Int =
  if (shaderProvider != null) {
    val bitmap = context.getBitmap(cacheKeyNamespace)
    canvas.setBitmap(bitmap)
    val correctedHeight = if (height <= 0f) canvas.height.toFloat() else height.coerceAtLeast(1f)
    val correctedWidth = if (width <= 0f) canvas.width.toFloat() else width.coerceAtLeast(1f)
    paint.shader = shaderProvider.getShader(context, 0f, 0f, correctedWidth, correctedHeight)
    canvas.drawRect(0f, 0f, correctedWidth, correctedHeight, paint)
    bitmap.getPixel(
      correctedWidth.half.toInt(),
      if (side == 1) 0 else (correctedHeight - 1).toInt(),
    )
  } else {
    color
  }
