package com.telekom.odsystem.atoms.togglechip

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.invokeWith
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.OPACITY_DISABLED
import com.telekom.odsystem.foundations.OPACITY_ENABLED
import com.telekom.odsystem.foundations.SINGLE_LINE
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSToggleChip composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onToggle Callback triggered when action occurs.
 */
@Composable
fun ODSToggleChip(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSToggleChipProps = ODSToggleChipProps(),
    onToggle: ((Boolean) -> Unit)? = null,
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val state = when {
        isPressed && !props.disabled -> ODSActions.PRESSED
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSToggleChipStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )

    ODSToggleChipTappableContainer(
        modifier = modifier.semantics {
            this.toggleableState = ToggleableState(props.selected)
        },
        props = props,
        style = style,
        isPressed = { isPressed = it },
        interactionSource = interactionSource,
        onClick = onToggle?.invokeWith { !props.selected },
    )
}

@Composable
fun ODSToggleChipTappableContainer(
    modifier: Modifier = Modifier,
    props: ODSToggleChipProps,
    style: ODSToggleChipStyle,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    onClick: (() -> Unit)? = null,
) {
    ODSColumn(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .customClickable(
                interactionSource = interactionSource,
                onClick = onClick,
                isPressed = { isPressed(it) },
                disabled = props.disabled,
                role = Role.Checkbox
            ),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment
    ) {
        ODSToggleChipContainer(
            props = props,
            style = style,
        )
    }
}

@Composable
private fun ODSToggleChipContainer(
    props: ODSToggleChipProps,
    style: ODSToggleChipStyle,
) {
    ODSRow(
        modifier = Modifier.sizeWithinBounds(
            minWidth = style.toggleChipMinWidth ?: MIN_WIDTH.dp,
            minHeight = style.toggleChipMinHeight ?: MIN_HEIGHT.dp
        ),
        gap = style.toggleChipGap,
        padding = style.toggleChipPadding,
        border = ODSBorder(
            width = style.toggleChipBorder,
            colorList = style.toggleChipBorderColor,
        ),
        cornerRadius = style.toggleChipCornerRadius,
        background = style.toggleChipBackground,
        horizontalArrangement = style.toggleChipHorizontalArrangement,
        verticalAlignment = style.toggleChipVerticalAlignment,
        horizontalAlignment = style.toggleChipHorizontalAlignment
    ) {

        ODSIconContainer(
            props = props,
            style = style,
        )
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.weight(1f, fill = false),
                text = props.label,
                style = style.chipStyle,
                color = style.chipColor,
                textAlign = style.chipTextAlign,
                overflow = style.chipOverflow,
                maxLines = SINGLE_LINE
            )
        }

        if (props.selected) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.checkmark_type_bold,
                    contentDescription = LocalContext.current.getString(R.string.semantic_checkmark_icon)
                ),
                width = style.checkmarkWidth,
                height = style.checkmarkHeight,
                tint = style.checkmarkColor?.getColor()
            )
        }
    }
}

@Composable
private fun ODSIconContainer(
    props: ODSToggleChipProps,
    style: ODSToggleChipStyle,
) {
    if (props.showImage) {
        props.image?.let {
            ODSRow(
                cornerRadius = style.imageCornerRadius,
                clipContent = style.imageClipContent != false,
                horizontalArrangement = style.imageHorizontalArrangement,
                horizontalAlignment = style.imageHorizontalAlignment,
                verticalAlignment = style.imageVerticalAlignment
            ) {
                ODSImage(
                    imageModel = it,
                    cornerRadius = style.imageCornerRadius,
                    width = style.image2Width,
                    height = style.image2Height,
                    modifier = Modifier.alpha(if (props.disabled) OPACITY_DISABLED else OPACITY_ENABLED),
                    contentScale = style.image2ContentScale ?: ContentScale.Crop,
                )
            }
        }
    } else {
        props.icon?.let {
            ODSIcon(
                iconModel = it,
                width = style.iconWidth,
                height = style.iconHeight,
                tint = style.iconColor?.getColor()
            )
        }
    }
}
