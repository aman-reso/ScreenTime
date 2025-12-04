@file:Suppress("DeprecatedCallableAddReplaceWith")

package com.telekom.odsystem.charts.compose.common.component

import android.graphics.Typeface
import android.text.Layout
import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.telekom.odsystem.charts.compose.common.pixelSize
import com.telekom.odsystem.charts.core.common.Defaults
import com.telekom.odsystem.charts.core.common.Fill
import com.telekom.odsystem.charts.core.common.Insets
import com.telekom.odsystem.charts.core.common.component.Component
import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.common.component.Shadow
import com.telekom.odsystem.charts.core.common.component.ShapeComponent
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.shape.Shape

/** Creates and remembers a [LineComponent]. */
@Composable
public fun rememberLineComponent(
    fill: Fill = Fill.Black,
    thickness: Dp = Defaults.LINE_COMPONENT_THICKNESS_DP.dp,
    shape: Shape = Shape.Rectangle,
    margins: Insets = Insets.Zero,
    strokeFill: Fill = Fill.Transparent,
    strokeThickness: Dp = 0.dp,
    shadow: Shadow? = null,
): LineComponent =
    remember(fill, shape, thickness, margins, strokeFill, strokeThickness, shadow) {
        LineComponent(
            fill,
            thickness.value,
            shape,
            margins,
            strokeFill,
            strokeThickness.value,
            shadow
        )
    }

/** Creates a [ShapeComponent]. */
public fun shapeComponent(
    fill: Fill = Fill.Black,
    shape: Shape = Shape.Rectangle,
    margins: Insets = Insets.Zero,
    strokeFill: Fill = Fill.Transparent,
    strokeThickness: Dp = 0.dp,
    shadow: Shadow? = null,
): ShapeComponent = ShapeComponent(fill, shape, margins, strokeFill, strokeThickness.value, shadow)

/** Creates and remembers a [ShapeComponent]. */
@Composable
public fun rememberShapeComponent(
    fill: Fill = Fill.Black,
    shape: Shape = Shape.Rectangle,
    margins: Insets = Insets.Zero,
    strokeFill: Fill = Fill.Transparent,
    strokeThickness: Dp = 0.dp,
    shadow: Shadow? = null,
): ShapeComponent =
    remember(fill, shape, margins, strokeFill, strokeThickness, shadow) {
        shapeComponent(fill, shape, margins, strokeFill, strokeThickness, shadow)
    }

/** Creates and remembers a [TextComponent]. */
@Composable
public fun rememberTextComponent(
    color: Color = Color.Black,
    typeface: Typeface = Typeface.DEFAULT,
    textSize: TextUnit = Defaults.TEXT_COMPONENT_TEXT_SIZE.sp,
    textAlignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    lineHeight: TextUnit? = null,
    lineCount: Int = Defaults.TEXT_COMPONENT_LINE_COUNT,
    truncateAt: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    margins: Insets = Insets.Zero,
    padding: Insets = Insets.Zero,
    background: Component? = null,
    minWidth: TextComponent.MinWidth = TextComponent.MinWidth.fixed(),
): TextComponent =
    remember(
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
    ) {
        TextComponent(
            color.toArgb(),
            typeface,
            textSize.pixelSize(),
            textAlignment,
            lineHeight?.pixelSize(),
            lineCount,
            truncateAt,
            margins,
            padding,
            background,
            minWidth,
        )
    }

/** Creates a [Shadow]. */
public fun shadow(radius: Dp, x: Dp = 0.dp, y: Dp = 0.dp, color: Color? = null): Shadow =
    Shadow(radius.value, x.value, y.value, color?.toArgb() ?: Defaults.SHADOW_COLOR)

/** A [Dp] version of [TextComponent.MinWidth.fixed]. */
public fun TextComponent.MinWidth.Companion.fixed(value: Dp = 0.dp): TextComponent.MinWidth =
    fixed(value.value)
