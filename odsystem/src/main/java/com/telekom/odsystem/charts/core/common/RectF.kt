package com.telekom.odsystem.charts.core.common

import android.graphics.RectF
import androidx.annotation.RestrictTo
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun RectF.set(left: Number, top: Number, right: Number, bottom: Number) {
  set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
}

internal fun RectF.clear() {
  set(0, 0, 0, 0)
}

internal fun RectF.copy(): RectF = RectF(this)

internal fun RectF.rotate(degrees: Float): RectF {
  when {
    degrees % PI_RAD == 0f -> Unit
    degrees % 0.5f.piRad == 0f -> {
      if (width() != height()) {
        set(
          left = centerX() - height().half,
          top = centerY() - width().half,
          right = centerX() + height().half,
          bottom = centerY() + width().half,
        )
      }
    }
    else -> {
      val alpha = Math.toRadians(degrees.toDouble())
      val sinAlpha = sin(alpha)
      val cosAlpha = cos(alpha)

      val newWidth = abs(width() * cosAlpha) + abs(height() * sinAlpha)
      val newHeight = abs(width() * sinAlpha) + abs(height() * cosAlpha)

      set(
        left = centerX() - newWidth.half,
        top = centerY() - newHeight.half,
        right = centerX() + newWidth.half,
        bottom = centerY() + newHeight.half,
      )
    }
  }

  return this
}

internal fun RectF.translate(x: Float, y: Float): RectF = apply {
  left += x
  top += y
  right += x
  bottom += y
}

internal fun RectF.getStart(isLtr: Boolean): Float = if (isLtr) left else right

internal fun RectF.getEnd(isLtr: Boolean): Float = if (isLtr) right else left
