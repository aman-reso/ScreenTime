package com.telekom.odsystem.charts.core.common

import android.graphics.Canvas
import kotlin.math.roundToInt

internal inline fun Canvas.inClip(
  left: Float,
  top: Float,
  right: Float,
  bottom: Float,
  block: () -> Unit,
) {
  val clipRestoreCount = save()
  clipRect(left, top, right, bottom)
  block()
  restoreToCount(clipRestoreCount)
}

internal fun Canvas.saveLayer(): Int = saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

internal fun Canvas.saveLayer(opacity: Float): Int =
  saveLayerAlpha(0f, 0f, width.toFloat(), height.toFloat(), (opacity * MAX_HEX_VALUE).roundToInt())
