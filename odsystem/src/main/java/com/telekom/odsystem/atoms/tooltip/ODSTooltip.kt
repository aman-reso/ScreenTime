package com.telekom.odsystem.atoms.tooltip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
/**
 * ODSTooltip composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param state Parameter for customization.
 * @param focusable Parameter for customization.
 * @param enableUserInput Parameter for customization.
 * @param content Parameter for customization.
 */
@Composable
fun ODSTooltip(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTooltipProps = ODSTooltipProps(),
    state: TooltipState = TooltipState(),
    focusable: Boolean = true,
    enableUserInput: Boolean = true,
    content: @Composable () -> Unit
) {
    val style = ODSTooltipStyle().getStyle(scheme = scheme, props = props)
    TooltipBox(
        focusable = focusable,
        enableUserInput = enableUserInput,
        positionProvider = rememberPlainTooltipPositionProvider(
            tooltipPlacement = props.placement,
            tooltipAlignment = props.alignment,
            style = style
        ),
        tooltip = {
            if (props.placement == ODSTooltipPlacement.TOP || props.placement == ODSTooltipPlacement.BOTTOM) {
                ODSVerticalTooltip(
                    modifier = modifier.semantics(mergeDescendants = true) { },
                    props = props,
                    style = style
                )
            } else {
                ODSHorizontalTooltip(
                    modifier = modifier.semantics(mergeDescendants = true) { },
                    props = props,
                    style = style
                )
            }
        },
        state = state
    ) {
        content()
    }
}

@Composable
fun ODSVerticalTooltip(
    modifier: Modifier = Modifier,
    props: ODSTooltipProps = ODSTooltipProps(),
    style: ODSTooltipStyle
) {
    ODSColumn(
        modifier = modifier,
        horizontalAlignment = style.containerHorizontalAlignment,
        verticalAlignment = style.containerVerticalAlignment,
        verticalArrangement = style.containerVerticalArrangement,
    ) {
        if (props.placement == ODSTooltipPlacement.TOP) {
            ODSTooltipContent(props = props, style = style)
            ODSVerticalTooltipCaret(
                modifier = Modifier.offset { IntOffset(0, -1) },
                style = style,
                props = props
            )
        } else {
            ODSVerticalTooltipCaret(
                modifier = Modifier.offset { IntOffset(0, 1) },
                style = style,
                props = props
            )
            ODSTooltipContent(props = props, style = style)
        }
    }
}

@Composable
fun ODSHorizontalTooltip(
    modifier: Modifier = Modifier,
    props: ODSTooltipProps,
    style: ODSTooltipStyle
) {
    val lineCount = remember {
        mutableIntStateOf(0)
    }
    ODSRow(
        modifier = modifier,
        horizontalAlignment = style.containerHorizontalAlignment,
        verticalAlignment = if (lineCount.intValue > 1) style.containerVerticalAlignment else Alignment.CenterVertically,
        horizontalArrangement = style.containerHorizontalArrangement,
    ) {
        if (props.placement == ODSTooltipPlacement.LEFT) {
            ODSTooltipContent(props = props, style = style) {
                lineCount.intValue = it
            }
            ODSHorizontalTooltipCaret(
                modifier = Modifier.offset { IntOffset(-1, 0) },
                style = style,
                props = props,
                lineCount = lineCount.intValue
            )
        } else {
            ODSHorizontalTooltipCaret(
                modifier = Modifier.offset { IntOffset(1, 0) },
                style = style,
                props = props,
                lineCount = lineCount.intValue
            )
            ODSTooltipContent(props = props, style = style) {
                lineCount.intValue = it
            }
        }
    }
}

@Composable
private fun ODSTooltipContent(
    props: ODSTooltipProps,
    style: ODSTooltipStyle,
    onTextLayout: (lineCount: Int) -> Unit = {}
) {
    ODSColumn(
        gap = style.gap,
        padding = style.padding,
        cornerRadius = style.borderRadius,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        background = style.backgroundColor
    ) {
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.widthIn(max = style.labelMaxWidth ?: Dp.Unspecified),
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                onTextLayout = {
                    onTextLayout(it.lineCount)
                }
            )
        }
    }
}

@Composable
private fun ODSVerticalTooltipCaret(
    modifier: Modifier = Modifier,
    style: ODSTooltipStyle,
    props: ODSTooltipProps,
) {
    val width = style.caretWidth ?: 0.dp
    val height = style.caretHeight ?: 0.dp
    ODSBox(
        modifier = modifier
            .padding(style.caretPadding?.getPaddingValues() ?: PaddingValues())
            .rotate(if (props.placement == ODSTooltipPlacement.BOTTOM) FULL_ROTATION else 0f)
            .clip(ODSTriangleShape(width = width, height = height)),
        height = height,
        width = width,
        background = style.backgroundColor
    ) {
    }
}

@Composable
private fun ODSHorizontalTooltipCaret(
    modifier: Modifier = Modifier,
    style: ODSTooltipStyle,
    props: ODSTooltipProps,
    lineCount: Int
) {
    val width = style.caretWidth ?: 0.dp
    val height = style.caretHeight ?: 0.dp
    ODSBox(
        modifier = modifier
            .padding(
                if (lineCount > 1) {
                    style.caretPadding?.getPaddingValues()
                        ?: PaddingValues()
                } else {
                    PaddingValues()
                }
            )
            .rotate(if (props.placement == ODSTooltipPlacement.RIGHT) FULL_ROTATION else 0f)
            .clip(ODSTriangleShapeRight(width = width, height = height)),
        height = height,
        width = width,
        background = style.backgroundColor
    ) {
    }
}

class ODSTriangleShape(
    private val width: Dp = 12.dp,
    private val height: Dp = 6.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val widthPx = with(density) { width.toPx() }
        val heightPx = with(density) { height.toPx() }

        return Outline.Generic(Path().apply {
            moveTo(size.width / 2, size.height)
            lineTo(size.width / 2 - widthPx / 2, size.height - heightPx)
            lineTo(size.width / 2 + widthPx / 2, size.height - heightPx)
            close()
        })
    }
}

class ODSTriangleShapeRight(
    private val width: Dp = 6.dp,
    private val height: Dp = 12.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val widthPx = with(density) { width.toPx() }
        val heightPx = with(density) { height.toPx() }

        return Outline.Generic(Path().apply {
            moveTo(size.width, size.height / 2)
            lineTo(size.width - widthPx, size.height / 2 - heightPx / 2)
            lineTo(size.width - widthPx, size.height / 2 + heightPx / 2)
            close()
        })
    }
}

@Composable
fun rememberPlainTooltipPositionProvider(
    spacingBetweenTooltipAndAnchor: Dp = 0.dp,
    tooltipPlacement: ODSTooltipPlacement,
    tooltipAlignment: ODSTooltipAlignment = ODSTooltipAlignment.CENTER,
    style: ODSTooltipStyle
): PopupPositionProvider {
    val tooltipAnchorSpacing = with(LocalDensity.current) {
        spacingBetweenTooltipAndAnchor.roundToPx()
    }
    val leftPadding = with(LocalDensity.current) {
        style.caretPadding?.left?.roundToPx()?.plus((style.caretWidth?.roundToPx() ?: 0) / 2) ?: 0
    }
    val rightPadding = with(LocalDensity.current) {
        style.caretPadding?.right?.roundToPx()?.plus((style.caretWidth?.roundToPx() ?: 0) / 2) ?: 0
    }
    val topPadding = with(LocalDensity.current) {
        style.caretPadding?.top?.roundToPx()?.plus((style.caretHeight?.roundToPx() ?: 0) / 2) ?: 0
    }
    val bottomPadding = with(LocalDensity.current) {
        style.caretPadding?.bottom?.roundToPx()?.plus((style.caretHeight?.roundToPx() ?: 0) / 2)
            ?: 0
    }
    return remember(tooltipAnchorSpacing) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val x: Int
                val y: Int

                when (tooltipPlacement) {
                    ODSTooltipPlacement.RIGHT, ODSTooltipPlacement.LEFT -> {
                        x = if (tooltipPlacement == ODSTooltipPlacement.RIGHT) {
                            anchorBounds.right + tooltipAnchorSpacing
                        } else {
                            anchorBounds.left - popupContentSize.width - tooltipAnchorSpacing
                        }
                        y = when (tooltipAlignment) {
                            ODSTooltipAlignment.START -> anchorBounds.top + anchorBounds.height / 2 - topPadding
                            ODSTooltipAlignment.CENTER -> anchorBounds.top + (anchorBounds.height - popupContentSize.height) / 2
                            ODSTooltipAlignment.END -> anchorBounds.bottom - anchorBounds.height / 2 - popupContentSize.height + bottomPadding
                        }
                    }

                    ODSTooltipPlacement.BOTTOM, ODSTooltipPlacement.TOP -> {
                        y = if (tooltipPlacement == ODSTooltipPlacement.TOP) {
                            anchorBounds.top - popupContentSize.height - tooltipAnchorSpacing
                        } else {
                            anchorBounds.bottom + tooltipAnchorSpacing
                        }
                        x = when (tooltipAlignment) {
                            ODSTooltipAlignment.START -> anchorBounds.left + anchorBounds.width / 2 - leftPadding
                            ODSTooltipAlignment.CENTER -> anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
                            ODSTooltipAlignment.END -> anchorBounds.right - anchorBounds.width / 2 - popupContentSize.width + rightPadding
                        }
                    }
                }
                return IntOffset(x, y)
            }
        }
    }
}

private const val FULL_ROTATION = 180f
