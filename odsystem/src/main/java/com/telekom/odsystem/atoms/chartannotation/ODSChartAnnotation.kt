package com.telekom.odsystem.atoms.chartannotation

import android.graphics.Typeface
import android.text.Layout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.charts.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.telekom.odsystem.charts.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.telekom.odsystem.charts.compose.common.component.fixed
import com.telekom.odsystem.charts.compose.common.component.rememberShapeComponent
import com.telekom.odsystem.charts.compose.common.component.rememberTextComponent
import com.telekom.odsystem.charts.compose.common.fill
import com.telekom.odsystem.charts.compose.common.insets
import com.telekom.odsystem.charts.compose.common.shape.markerCorneredShape
import com.telekom.odsystem.charts.core.cartesian.CartesianDrawingContext
import com.telekom.odsystem.charts.core.cartesian.marker.CartesianMarker
import com.telekom.odsystem.charts.core.cartesian.marker.DefaultCartesianMarker
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.shape.CorneredShape
import com.telekom.odsystem.charts.core.common.shape.Shape
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
public fun odsChartAnnotation(
    scheme: ODSTheme = neutralScheme,
    format: (context: CartesianDrawingContext, targets: List<CartesianMarker.Target>) -> CharSequence = remember { { _, _ -> String() } },
): CartesianMarker {
    val context = LocalContext.current
    val style = ODSChartAnnotationStyle().getStyle(scheme = scheme)
    val cornerRadius = style.cornerRadius
    val topLeft = cornerRadius?.topLeft?.value ?: 0f
    val topRight = cornerRadius?.topRight?.value ?: 0f
    val bottomRight = cornerRadius?.bottomRight?.value ?: 0f
    val bottomLeft = cornerRadius?.bottomLeft?.value ?: 0f
    val labelBackgroundShape = markerCorneredShape(
        CorneredShape.rounded(
            topLeftDp = topLeft,
            topRightDp = topRight,
            bottomRightDp = bottomRight,
            bottomLeftDp = bottomLeft
        )
    )
    val labelBackground =
        rememberShapeComponent(
            fill = fill(style.tooltipBackground ?: Color.Transparent),
            shape = labelBackgroundShape,
        )
    val labelPadding = style.padding
    val topPadding = labelPadding?.top ?: 0.dp
    val bottomPadding = labelPadding?.bottom ?: 0.dp
    val leftPadding = labelPadding?.left ?: 0.dp
    val rightPadding = labelPadding?.right ?: 0.dp
    val textStyle = DSTextStyles.bodySBold
    val label =
        rememberTextComponent(
            color = scheme.basicTextOnAccentSecondary.getColor(),
            textSize = textStyle.fontSize.sp,
            typeface = ResourcesCompat.getFont(context, textStyle.fontFamily)
                ?: Typeface.DEFAULT,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            padding = insets(
                start = leftPadding,
                top = topPadding,
                end = rightPadding,
                bottom = bottomPadding
            ),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val guideline = rememberAxisGuidelineComponent(
        fill = fill(style.tooltipBackground ?: Color.Transparent),
        shape = Shape.Rectangle
    )
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = DefaultCartesianMarker.ValueFormatter(format),
        indicatorSize = 0.dp,
        guideline = guideline,
    )
}
