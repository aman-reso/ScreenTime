package com.telekom.odsystem.charts.core.common.component

import android.graphics.Paint
import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.MeasuringContext

/**
 * Stores shadow properties.
 *
 * @param radiusDp the blur radius (in dp).
 * @param xDp the horizontal offset (in dp).
 * @param yDp the vertical offset (in dp).
 * @param color the color.
 */
@Immutable
data class Shadow(
  private val radiusDp: Float,
  private val xDp: Float = 0f,
  private val yDp: Float = 0f,
  private val color: Int = Defaults.SHADOW_COLOR,
) {
  /** Updates [paint]’s shadow layer. */
  fun updateShadowLayer(context: MeasuringContext, paint: Paint) {
    with(context) { paint.setShadowLayer(radiusDp.pixels, xDp.pixels, yDp.pixels, color) }
  }
}
