package com.telekom.odsystem.atoms.dismissiblechip

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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
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
 * ODSDismissibleChip composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onDismiss Callback triggered when action occurs.
 */
@Composable
fun ODSDismissibleChip(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSDismissibleChipProps = ODSDismissibleChipProps(),
    onDismiss: () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSDismissibleChipStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (isPressed && !props.disabled) ODSActions.PRESSED else if (isHovered && !props.disabled) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    ODSDismissibleChipTappableContainer(
        modifier = modifier,
        props = props,
        style = style,
        isPressed = { isPressed = it },
        interactionSource = interactionSource,
        onDismiss = onDismiss,
    )
}

@Composable
private fun ODSDismissibleChipTappableContainer(
    modifier: Modifier,
    props: ODSDismissibleChipProps,
    style: ODSDismissibleChipStyle,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    onDismiss: () -> Unit,
) {
    val localView = LocalView.current
    val context = LocalContext.current
    ODSColumn(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .customClickable(
                interactionSource = interactionSource,
                onClick = {
                    localView.announceForAccessibility("${props.label} ${context.getString(R.string.semantic_dismissed_action)}")
                    onDismiss()
                },
                isPressed = { isPressed(it) },
                disabled = props.disabled,
                role = Role.Button,
                onClickLabel = LocalContext.current.getString(R.string.semantic_dismissible_chip_action)
            ),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment
    ) {
        ODSDismissibleChipContainer(
            props = props,
            style = style,
        )
    }
}

@Composable
private fun ODSDismissibleChipContainer(
    props: ODSDismissibleChipProps,
    style: ODSDismissibleChipStyle,
) {
    ODSRow(
        modifier = Modifier.sizeWithinBounds(
            minWidth = style.dismissibleChipMinWidth ?: MIN_WIDTH.dp,
            minHeight = style.dismissibleChipMinHeight ?: MIN_HEIGHT.dp
        ),
        gap = style.dismissibleChipGap,
        padding = style.dismissibleChipPadding,
        cornerRadius = style.dismissibleChipBorderRadius,
        verticalAlignment = style.dismissibleChipVerticalAlignment,
        horizontalAlignment = style.dismissibleChipHorizontalAlignment,
        horizontalArrangement = style.dismissibleChipHorizontalArrangement,
        background = style.dismissibleChipBackgroundColor,
    ) {
        ODSDismissibleChipTypeContainer(
            props = props,
            style = style,
        )

        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.weight(1f, fill = false),
                text = props.label,
                style = style.chipTextStyle,
                color = style.chipColor,
                textAlign = style.chipTextAlign,
                overflow = style.chipTextOverflow,
                maxLines = SINGLE_LINE
            )
        }
        ODSActionContainer(style)
    }
}

@Composable
private fun ODSDismissibleChipTypeContainer(
    props: ODSDismissibleChipProps,
    style: ODSDismissibleChipStyle,
) {
    if (props.variant == ODSDismissibleChipVariant.WITH_ICON && props.icon != null) {
        ODSIcon(
            iconModel = props.icon,
            width = style.iconWidth,
            height = style.iconHeight,
            tint = style.iconColor?.getColor()
        )
    } else if (props.variant == ODSDismissibleChipVariant.WITH_IMAGE && props.image != null) {
        ODSRow(
            cornerRadius = style.imageBorderRadius,
            horizontalArrangement = style.imageHorizontalArrangement,
            horizontalAlignment = style.imageHorizontalAlignment,
            clipContent = style.imageClipContent != false,
            verticalAlignment = style.imageVerticalAlignment
        ) {
            ODSImage(
                cornerRadius = style.image2BorderRadius,
                width = style.image2Width,
                height = style.image2Height,
                imageModel = props.image,
                modifier = Modifier.alpha(if (props.disabled) OPACITY_DISABLED else OPACITY_ENABLED),
                contentScale = style.image2ObjectFit ?: ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ODSActionContainer(
    style: ODSDismissibleChipStyle
) {
    ODSRow(
        cornerRadius = style.actionBorderRadius,
        horizontalArrangement = style.actionHorizontalArrangement,
        horizontalAlignment = style.actionHorizontalAlignment,
        verticalAlignment = style.actionVerticalAlignment,
        background = style.actionBackgroundColor
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.close_type_bold,
                contentDescription = LocalContext.current.getString(R.string.semantic_delete_icon)
            ),
            width = style.closeWidth,
            height = style.closeHeight,
            tint = style.closeColor?.getColor()
        )
    }
}
