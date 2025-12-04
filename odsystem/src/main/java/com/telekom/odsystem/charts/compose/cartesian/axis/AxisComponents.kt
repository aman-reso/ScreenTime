package com.telekom.odsystem.charts.compose.cartesian.axis

import android.graphics.Typeface
import android.text.Layout
import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.telekom.odsystem.charts.compose.common.fill
import com.telekom.odsystem.charts.compose.common.insets
import com.telekom.odsystem.charts.compose.common.shape.dashedShape
import com.telekom.odsystem.charts.compose.common.vicoTheme
import com.telekom.odsystem.charts.compose.common.component.rememberLineComponent
import com.telekom.odsystem.charts.compose.common.component.rememberTextComponent
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.Fill
import com.telekom.odsystem.charts.core.common.Insets
import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.common.component.Shadow
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.shape.Shape

/** A [rememberTextComponent] alias with defaults for [Axis] labels. */
@Composable
public fun rememberAxisLabelComponent(
    color: Color = vicoTheme.textColor,
    typeface: Typeface = Typeface.DEFAULT,
    textSize: TextUnit = Defaults.AXIS_LABEL_SIZE.sp,
    textAlignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    lineHeight: TextUnit? = null,
    lineCount: Int = Defaults.AXIS_LABEL_MAX_LINES,
    truncateAt: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    margins: Insets =
        insets(Defaults.AXIS_LABEL_HORIZONTAL_MARGIN.dp, Defaults.AXIS_LABEL_VERTICAL_MARGIN.dp),
    padding: Insets =
        insets(Defaults.AXIS_LABEL_HORIZONTAL_PADDING.dp, Defaults.AXIS_LABEL_VERTICAL_PADDING.dp),
    background: Component? = null,
    minWidth: TextComponent.MinWidth = TextComponent.MinWidth.fixed(),
): TextComponent =
    rememberTextComponent(
        color,
        typeface,
        textSize,
        textAlignment,
        lineHeight,
        lineCount,
        truncateAt,
        margins,
        padding,
        background,
        minWidth,
    )

/** A [rememberLineComponent] alias with defaults for [Axis] lines. */
@Composable
public fun rememberAxisLineComponent(
    fill: Fill = fill(vicoTheme.lineColor),
    thickness: Dp = Defaults.AXIS_LINE_WIDTH.dp,
    shape: Shape = Shape.Rectangle,
    margins: Insets = Insets.Zero,
    strokeFill: Fill = Fill.Transparent,
    strokeThickness: Dp = 0.dp,
    shadow: Shadow? = null,
): LineComponent =
    rememberLineComponent(fill, thickness, shape, margins, strokeFill, strokeThickness, shadow)

/** A [rememberLineComponent] alias with defaults for [Axis] ticks. */
@Composable
public fun rememberAxisTickComponent(
    fill: Fill = fill(vicoTheme.lineColor),
    thickness: Dp = Defaults.AXIS_LINE_WIDTH.dp,
    shape: Shape = Shape.Rectangle,
    margins: Insets = Insets.Zero,
    strokeFill: Fill = Fill.Transparent,
    strokeThickness: Dp = 0.dp,
    shadow: Shadow? = null,
): LineComponent =
    rememberLineComponent(fill, thickness, shape, margins, strokeFill, strokeThickness, shadow)

/** A [rememberLineComponent] alias with defaults for [Axis] guidelines. */
@Composable
public fun rememberAxisGuidelineComponent(
    fill: Fill = fill(vicoTheme.lineColor),
    thickness: Dp = Defaults.AXIS_GUIDELINE_WIDTH.dp,
    shape: Shape = dashedShape(),
    margins: Insets = Insets.Zero,
    strokeFill: Fill = Fill.Transparent,
    strokeThickness: Dp = 0.dp,
    shadow: Shadow? = null,
): LineComponent =
    rememberLineComponent(fill, thickness, shape, margins, strokeFill, strokeThickness, shadow)
