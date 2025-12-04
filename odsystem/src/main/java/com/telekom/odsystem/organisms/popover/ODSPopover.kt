package com.telekom.odsystem.organisms.popover

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tooltip.ODSTriangleShape
import com.telekom.odsystem.atoms.tooltip.ODSTriangleShapeRight
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.invertedScheme

@OptIn(ExperimentalMaterial3Api::class)
/**
 * ODSPopover composable.
 *
 * @param modifier Modifier applied to this component.
 * @param props Visual configuration for the component.
 * @param state Parameter for customization.
 * @param focusable Parameter for customization.
 * @param enableUserInput Parameter for customization.
 * @param content Parameter for customization.
 * @param contentSlot Parameter for customization.
 * @param actionSlot Parameter for customization.
 * @param onDismiss Callback triggered when action occurs.
 */
@Composable
fun ODSPopover(
    modifier: Modifier = Modifier,
    props: ODSPopoverProps = ODSPopoverProps(),
    state: TooltipState = TooltipState(),
    focusable: Boolean = true,
    enableUserInput: Boolean = true,
    content: @Composable () -> Unit,
    contentSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit = { }
) {
    val style = ODSPopoverStyle().getStyle(props = props)
    TooltipBox(
        focusable = focusable,
        enableUserInput = enableUserInput,
        positionProvider = rememberPlainTooltipPositionProvider(
            tooltipPlacement = props.placement,
            tooltipAlignment = props.alignment,
            style = style
        ),
        tooltip = {
            if (props.placement == ODSPopoverPlacement.TOP || props.placement == ODSPopoverPlacement.BOTTOM) {
                ODSVerticalPopover(
                    modifier = modifier.semantics(mergeDescendants = true) { },
                    props = props,
                    style = style,
                    contentSlot = contentSlot,
                    actionSlot = actionSlot,
                    onDismiss = onDismiss
                )
            } else {
                ODSHorizontalPopover(
                    modifier = modifier.semantics(mergeDescendants = true) { },
                    props = props,
                    style = style,
                    contentSlot = contentSlot,
                    actionSlot = actionSlot,
                    onDismiss = onDismiss
                )
            }
        },
        state = state
    ) {
        content()
    }
}

@Composable
fun ODSVerticalPopover(
    modifier: Modifier = Modifier,
    props: ODSPopoverProps = ODSPopoverProps(),
    style: ODSPopoverStyle,
    contentSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?,
    onDismiss: () -> Unit
) {
    ODSColumn(
        modifier = modifier,
        horizontalAlignment = style.containerHorizontalAlignment,
        verticalAlignment = style.containerVerticalAlignment,
        verticalArrangement = style.containerVerticalArrangement,
    ) {
        if (props.placement == ODSPopoverPlacement.TOP) {
            ODSPopoverContent(
                props = props,
                style = style,
                actionSlot = actionSlot,
                contentSlot = contentSlot,
                onDismiss = onDismiss
            )
            ODSVerticalPopoverCaret(
                modifier = Modifier.offset { IntOffset(0, -1) },
                style = style,
                props = props
            )
        } else {
            ODSVerticalPopoverCaret(
                modifier = Modifier.offset { IntOffset(0, 1) },
                style = style,
                props = props
            )
            ODSPopoverContent(
                props = props,
                style = style,
                actionSlot = actionSlot,
                contentSlot = contentSlot,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun ODSHorizontalPopover(
    modifier: Modifier = Modifier,
    props: ODSPopoverProps,
    style: ODSPopoverStyle,
    contentSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit
) {

    ODSRow(
        modifier = modifier,
        horizontalAlignment = style.containerHorizontalAlignment,
        verticalAlignment = style.containerVerticalAlignment,
        horizontalArrangement = style.containerHorizontalArrangement,
    ) {
        if (props.placement == ODSPopoverPlacement.LEFT) {
            ODSPopoverContent(
                props = props,
                style = style,
                actionSlot = actionSlot,
                contentSlot = contentSlot,
                onDismiss = onDismiss
            )
            ODSHorizontalPopoverCaret(
                modifier = Modifier.offset { IntOffset(-1, 0) },
                style = style,
                props = props,
            )
        } else {
            ODSHorizontalPopoverCaret(
                modifier = Modifier.offset { IntOffset(1, 0) },
                style = style,
                props = props,
            )
            ODSPopoverContent(
                props = props,
                style = style,
                actionSlot = actionSlot,
                contentSlot = contentSlot,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun ODSPopoverContent(
    props: ODSPopoverProps,
    style: ODSPopoverStyle,
    contentSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?,
    onDismiss: () -> Unit,
) {
    ODSColumn(
        modifier = Modifier
            .sizeWithinBounds(maxWidth = style.maxWidth ?: Dp.Unspecified)
            .width(IntrinsicSize.Max),
        padding = style.padding,
        cornerRadius = style.borderRadius,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        background = style.backgroundColor
    ) {
        ODSTitleWithCloseContainer(style = style, props = props, onDismiss = onDismiss)
        ODSContentAndActionsContainer(
            style = style,
            props = props,
            contentSlot = contentSlot,
            actionSlot = actionSlot
        )
    }
}

@Composable
private fun ODSTitleWithCloseContainer(
    style: ODSPopoverStyle,
    props: ODSPopoverProps,
    onDismiss: () -> Unit
) {
    ODSRow(
        gap = style.titleCloseGap,
        horizontalArrangement = style.titleCloseHorizontalArrangement,
        horizontalAlignment = style.titleCloseHorizontalAlignment,
        verticalAlignment = style.titleCloseVerticalAlignment
    ) {
        val context = LocalContext.current
        ODSRow(
            horizontalArrangement = style.titleHorizontalArrangement,
            horizontalAlignment = style.titleHorizontalAlignment,
            verticalAlignment = style.titleVerticalAlignment
        ) {
            if (!props.label.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.sizeWithinBounds(
                        maxWidth = style.labelMaxWidth ?: Dp.Unspecified
                    ),
                    text = props.label,
                    style = style.labelTextStyle,
                    color = style.labelColor,
                    textAlign = style.labelTextAlign
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        ODSButton(
            scheme = invertedScheme,
            props = ODSButtonProps(
                buttonIcon = ODSIconModel(
                    drawableRes = R.drawable.close_type_bold,
                    contentDescription = context.getString(R.string.semantics_close)
                ),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                size = ODSButtonSize.SMALL,
                variant = ODSButtonVariant.GHOST
            ),
            onClick = onDismiss
        )
    }
}

@Composable
private fun ODSContentAndActionsContainer(
    style: ODSPopoverStyle,
    props: ODSPopoverProps,
    contentSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?
) {
    ODSColumn(
        gap = style.contentActionsGap,
        verticalArrangement = style.contentActionsVerticalArrangement,
        verticalAlignment = style.contentActionsVerticalAlignment,
        horizontalAlignment = style.contentActionsHorizontalAlignment
    ) {
        ODSColumn(
            gap = style.contentGap,
            verticalArrangement = style.contentVerticalArrangement,
            verticalAlignment = style.contentVerticalAlignment,
            horizontalAlignment = style.contentHorizontalAlignment
        ) {
            if (!props.text.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.widthIn(max = style.textMaxWidth ?: Dp.Unspecified),
                    text = props.text,
                    style = style.textTextStyle,
                    color = style.textColor,
                    textAlign = style.textTextAlign,
                )
            }
            contentSlot?.let {
                ODSPopoverContentSlotContainer(style = style, contentSlot = it)
            }
            actionSlot?.let {
                ODSPopoverActionSlotContainer(style = style, actionSlot = it)
            }
        }
    }
}

@Composable
private fun ODSPopoverContentSlotContainer(
    style: ODSPopoverStyle,
    contentSlot: @Composable (() -> Unit)
) {
    ODSColumn(
        modifier = Modifier.sizeWithinBounds(
            maxHeight = style.contentSlotContainerMaxHeight ?: Dp.Unspecified,
            maxWidth = style.contentSlotContainerMaxWidth ?: Dp.Unspecified
        ),
        clipContent = style.contentSlotContainerClipContent != false,
        verticalArrangement = style.contentSlotContainerVerticalArrangement,
        verticalAlignment = style.contentSlotContainerVerticalAlignment,
        horizontalAlignment = style.contentSlotContainerHorizontalAlignment,
    ) {
        contentSlot()
    }
}

@Composable
private fun ODSPopoverActionSlotContainer(
    style: ODSPopoverStyle,
    actionSlot: @Composable (() -> Unit)
) {
    ODSRow(
        gap = style.actionSlotContainerGap,
        horizontalArrangement = style.actionSlotContainerHorizontalArrangement,
        horizontalAlignment = style.actionSlotContainerHorizontalAlignment,
        verticalAlignment = style.actionSlotContainerVerticalAlignment
    ) {
        actionSlot()
    }
}

@Composable
private fun ODSVerticalPopoverCaret(
    modifier: Modifier = Modifier,
    style: ODSPopoverStyle,
    props: ODSPopoverProps,
) {
    val width = style.caretWidth ?: 0.dp
    val height = style.caretHeight ?: 0.dp
    ODSBox(
        modifier = modifier
            .padding(style.caretPadding?.getPaddingValues() ?: PaddingValues())
            .rotate(if (props.placement == ODSPopoverPlacement.BOTTOM) FULL_ROTATION else 0f)
            .clip(ODSTriangleShape(width = width, height = height)),
        height = height,
        width = width,
        background = style.backgroundColor
    ) {
    }
}

@Composable
private fun ODSHorizontalPopoverCaret(
    modifier: Modifier = Modifier,
    style: ODSPopoverStyle,
    props: ODSPopoverProps,
) {
    val width = style.caretWidth ?: 0.dp
    val height = style.caretHeight ?: 0.dp
    ODSBox(
        modifier = modifier
            .padding(style.caretPadding?.getPaddingValues() ?: PaddingValues())
            .rotate(if (props.placement == ODSPopoverPlacement.RIGHT) FULL_ROTATION else 0f)
            .clip(ODSTriangleShapeRight(width = width, height = height)),
        height = height,
        width = width,
        background = style.backgroundColor
    ) {
    }
}

@Composable
fun rememberPlainTooltipPositionProvider(
    spacingBetweenTooltipAndAnchor: Dp = 0.dp,
    tooltipPlacement: ODSPopoverPlacement,
    tooltipAlignment: ODSPopoverAlignment = ODSPopoverAlignment.CENTER,
    style: ODSPopoverStyle
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
                    ODSPopoverPlacement.RIGHT, ODSPopoverPlacement.LEFT -> {
                        x = if (tooltipPlacement == ODSPopoverPlacement.RIGHT) {
                            anchorBounds.right + tooltipAnchorSpacing
                        } else {
                            anchorBounds.left - popupContentSize.width - tooltipAnchorSpacing
                        }
                        y = when (tooltipAlignment) {
                            ODSPopoverAlignment.START -> anchorBounds.top + anchorBounds.height / 2 - topPadding
                            ODSPopoverAlignment.CENTER -> anchorBounds.top + (anchorBounds.height - popupContentSize.height) / 2
                            ODSPopoverAlignment.END -> anchorBounds.bottom - anchorBounds.height / 2 - popupContentSize.height + bottomPadding
                        }
                    }

                    ODSPopoverPlacement.BOTTOM, ODSPopoverPlacement.TOP -> {
                        y = if (tooltipPlacement == ODSPopoverPlacement.TOP) {
                            anchorBounds.top - popupContentSize.height - tooltipAnchorSpacing
                        } else {
                            anchorBounds.bottom + tooltipAnchorSpacing
                        }
                        x = when (tooltipAlignment) {
                            ODSPopoverAlignment.START -> anchorBounds.left + anchorBounds.width / 2 - leftPadding
                            ODSPopoverAlignment.CENTER -> anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
                            ODSPopoverAlignment.END -> anchorBounds.right - anchorBounds.width / 2 - popupContentSize.width + rightPadding
                        }
                    }
                }
                return IntOffset(x, y)
            }
        }
    }
}

private const val FULL_ROTATION = 180f
