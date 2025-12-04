package com.telekom.odsystem.atoms.segments

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSSegments composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
fun ODSSegments(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSegmentsProps = ODSSegmentsProps(),
    onClick: () -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }
    val pressed = remember { mutableStateOf(false) }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSSegmentsStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (pressed.value && !props.disabled) ODSActions.PRESSED else if (isHovered && !props.disabled) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    ODSRow(
        modifier = modifier
            .semantics { this.selected = props.selected }
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .customClickable(
                interactionSource = interactionSource,
                onClick = onClick,
                isPressed = { pressed.value = it },
                role = Role.Tab,
                disabled = props.disabled
            ),
        cornerRadius = style.borderRadius,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        ODSContentFrame(style = style, props = props)
    }
}

@Composable
private fun ODSContentFrame(
    style: ODSSegmentsStyle,
    props: ODSSegmentsProps
) {
    ODSRow(
        modifier = Modifier
            .sizeWithinBounds(
                minWidth = style.contentFrameMinWidth ?: Dp.Unspecified,
                minHeight = style.contentFrameMinHeight ?: Dp.Unspecified
            )
            .applyFillMaxWidthIfFillVariant(props.variant),
        gap = style.contentFrameGap,
        padding = style.contentFramePadding,
        cornerRadius = style.contentFrameBorderRadius,
        horizontalArrangement = style.contentFrameHorizontalArrangement,
        horizontalAlignment = style.contentFrameHorizontalAlignment,
        verticalAlignment = style.contentFrameVerticalAlignment,
        background = style.contentFrameBackgroundColor,
    ) {
        if (props.icon != null) {
            ODSIconFrame(style = style, props = props)
        }
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                overflow = TextOverflow.Ellipsis,
                maxLines = MAX_LINES
            )
        }
    }
}

@Composable
private fun ODSIconFrame(
    style: ODSSegmentsStyle,
    props: ODSSegmentsProps
) {
    ODSIcon(
        iconModel = props.icon,
        tint = style.iconColor?.getColor(),
        width = style.iconWidth,
        height = style.iconHeight
    )
}

private fun Modifier.applyFillMaxWidthIfFillVariant(variant: ODSSegmentsVariant): Modifier =
    if (variant == ODSSegmentsVariant.FILL) fillMaxWidth() else this

private const val MAX_LINES = 1
