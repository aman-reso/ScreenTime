package com.telekom.odsystem.charts.core.common.component

import android.graphics.RectF
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.DrawingContext
import com.telekom.odsystem.charts.core.common.Fill
import com.telekom.odsystem.charts.core.common.Insets
import com.telekom.odsystem.charts.core.common.MeasuringContext
import com.telekom.odsystem.charts.core.common.half
import com.telekom.odsystem.charts.core.common.shape.Shape

/**
 * Draws lines.
 *
 * @param fill the fill.
 * @param shape the [Shape].
 * @param margins the margins.
 * @param strokeFill the stroke fill.
 * @param strokeThicknessDp the stroke thickness (in dp).
 * @param shadow stores the shadow properties.
 * @property thicknessDp the line thickness (in dp).
 */
open class LineComponent(
  fill: Fill,
  val thicknessDp: Float = Defaults.LINE_COMPONENT_THICKNESS_DP,
  shape: Shape = Shape.Rectangle,
  margins: Insets = Insets.Zero,
  strokeFill: Fill = Fill.Transparent,
  strokeThicknessDp: Float = 0f,
  shadow: Shadow? = null,
) : ShapeComponent(fill, shape, margins, strokeFill, strokeThicknessDp, shadow) {
  internal val MeasuringContext.thickness: Float
    get() = thicknessDp.pixels

  /** A convenience function for [draw] that draws the [LineComponent] horizontally. */
  open fun drawHorizontal(
    context: DrawingContext,
    left: Float,
    right: Float,
    y: Float,
    thicknessFactor: Float = 1f,
  ) {
    val halfThickness = (thicknessFactor * context.thickness).half
    draw(
      context = context,
      left = left,
      top = y - halfThickness,
      right = right,
      bottom = y + halfThickness,
    )
  }

  /** A convenience function for [draw] that draws the [LineComponent] vertically. */
  open fun drawVertical(
    context: DrawingContext,
    x: Float,
    top: Float,
    bottom: Float,
    thicknessFactor: Float = 1f,
  ) {
    val halfThickness = (thicknessFactor * context.thickness).half
    draw(
      context = context,
      left = x - halfThickness,
      top = top,
      right = x + halfThickness,
      bottom = bottom,
    )
  }

  /** Creates a new [LineComponent] based on this one. */
  override fun copy(
    fill: Fill,
    shape: Shape,
    margins: Insets,
    strokeFill: Fill,
    strokeThicknessDp: Float,
    shadow: Shadow?,
  ): LineComponent =
    LineComponent(fill, thicknessDp, shape, margins, strokeFill, strokeThicknessDp, shadow)

  /** Creates a new [LineComponent] based on this one. */
  open fun copy(
    fill: Fill = this.fill,
    thicknessDp: Float = this.thicknessDp,
    shape: Shape = this.shape,
    margins: Insets = this.margins,
    strokeFill: Fill = this.strokeFill,
    strokeThicknessDp: Float = this.strokeThicknessDp,
    shadow: Shadow? = this.shadow,
  ): LineComponent =
    LineComponent(fill, thicknessDp, shape, margins, strokeFill, strokeThicknessDp, shadow)

  override fun equals(other: Any?): Boolean =
    super.equals(other) && other is LineComponent && thicknessDp == other.thicknessDp

  override fun hashCode(): Int = 31 * super.hashCode() + thicknessDp.hashCode()
}

internal fun LineComponent.intersectsVertical(
  context: DrawingContext,
  x: Float,
  bounds: RectF,
  thicknessFactor: Float = 1f,
): Boolean {
  val halfThickness = (thicknessFactor * context.thickness).half
  val left = x - halfThickness.half
  val right = x + halfThickness.half
  return bounds.left < right && left < bounds.right
}
