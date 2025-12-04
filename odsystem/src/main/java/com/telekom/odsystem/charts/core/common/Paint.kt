package com.telekom.odsystem.charts.core.common

import android.graphics.Paint

internal var Paint.opacity: Float
  get() = alpha / MAX_HEX_VALUE
  set(value) {
    alpha = (value * MAX_HEX_VALUE).toInt()
  }

internal fun Paint.withOpacity(opacity: Float, action: (Paint) -> Unit) {
  val previousOpacity = this.alpha
  color = color.copyColor(opacity * previousOpacity / MAX_HEX_VALUE)
  action(this)
  this.alpha = previousOpacity
}
