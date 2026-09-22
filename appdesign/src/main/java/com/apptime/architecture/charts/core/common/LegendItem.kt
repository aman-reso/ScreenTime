package com.telekom.odsystem.charts.core.common

import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.component.TextComponent

/**
 * Defines the appearance of an item of a [Legend].
 *
 * @param icon used as the icon.
 * @param labelComponent the label [TextComponent].
 * @param label the label text.
 */
open class LegendItem(
  open val icon: Component,
  open val labelComponent: TextComponent,
  open val label: CharSequence,
) {
  /**
   * Measures the height of the label.
   *
   * @param iconSizeDp the [LegendItem.icon] size (in dp).
   * @param iconLabelSpacingDp the spacing between [LegendItem.icon] and [LegendItem.labelComponent]
   *   (in dp).
   * @param maxWidth the maximum [LegendItem] width.
   */
  fun getLabelHeight(
    context: MeasuringContext,
    iconSizeDp: Float,
    iconLabelSpacingDp: Float,
    maxWidth: Float,
  ): Float =
    labelComponent.getHeight(
      context = context,
      text = label,
      maxWidth = (maxWidth - context.run { iconSizeDp.pixels + iconLabelSpacingDp.pixels }).toInt(),
    )

  /**
   * Measures the width of the label.
   *
   * @param iconSizeDp the [LegendItem.icon] size (in dp).
   * @param iconLabelSpacingDp the spacing between [LegendItem.icon] and [LegendItem.labelComponent]
   *   (in dp).
   * @param maxWidth the maximum [LegendItem] width.
   */
  fun getLabelWidth(
    context: MeasuringContext,
    iconSizeDp: Float,
    iconLabelSpacingDp: Float,
    maxWidth: Float,
  ): Float =
    labelComponent.getWidth(
      context = context,
      text = label,
      maxWidth = (maxWidth - context.run { iconSizeDp.pixels + iconLabelSpacingDp.pixels }).toInt(),
    )

  /**
   * Measures the width of this [LegendItem].
   *
   * @param iconSizeDp the [LegendItem.icon] size (in dp).
   * @param iconLabelSpacingDp the spacing between [LegendItem.icon] and [LegendItem.labelComponent]
   *   (in dp).
   * @param maxWidth the maximum [LegendItem] width.
   */
  fun getWidth(
    context: MeasuringContext,
    iconSizeDp: Float,
    iconLabelSpacingDp: Float,
    maxWidth: Float,
  ): Float =
    getLabelWidth(context, iconSizeDp, iconLabelSpacingDp, maxWidth) +
      context.run { (iconSizeDp + iconLabelSpacingDp).pixels }
}
