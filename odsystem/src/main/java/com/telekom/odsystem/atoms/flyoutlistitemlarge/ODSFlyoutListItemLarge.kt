package com.telekom.odsystem.atoms.flyoutlistitemlarge

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSFlyoutListItemLarge composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param onClick Callback triggered when action occurs.
 * @param props Visual configuration for the component.
 */
fun ODSFlyoutListItemLarge(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onClick: () -> Unit,
    props: ODSFlyoutListItemLargeProps = ODSFlyoutListItemLargeProps()
) {
    val isPressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSFlyoutListItemLargeStyle().getStyle(
        props = props,
        scheme = scheme,
        state = if (isPressed.value && !props.disabled) ODSActions.PRESSED else if (isHovered && !props.disabled) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    ODSFlyoutListItemLargeContainer(
        modifier = modifier,
        props = props,
        isPressed = { isPressed.value = it },
        onClick = onClick,
        interactionSource = interactionSource,
        style = style
    )
}

@Composable
private fun ODSFlyoutListItemLargeContainer(
    modifier: Modifier,
    props: ODSFlyoutListItemLargeProps,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    style: ODSFlyoutListItemLargeStyle
) {
    ODSRow(
        gap = style.gap,
        modifier = modifier
            .sizeWithinBounds(
                minWidth = MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .semantics {
                if (props.variant == ODSFlyoutListItemLargeVariant.CHECKED) {
                    this.selected = true
                }
            }
            .customClickable(
                isPressed = { isPressed(it) },
                onClick = onClick,
                interactionSource = interactionSource,
                disabled = props.disabled
            ),
        padding = style.padding,
        cornerRadius = style.borderRadius,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        background = style.backgroundColor,
    ) {
        props.iconBefore?.let {
            ODSIcon(
                iconModel = it,
                tint = style.iconBeforeColor?.getColor(),
                width = style.iconBeforeWidth,
                height = style.iconBeforeHeight
            )
        }

        ODSTextContainer(modifier = Modifier.weight(1f), props = props, style = style)
        ODSEndContentContainer(props = props, style = style)
    }
}

@Composable
private fun ODSEndContentContainer(
    props: ODSFlyoutListItemLargeProps,
    style: ODSFlyoutListItemLargeStyle
) {
    if (props.variant == ODSFlyoutListItemLargeVariant.CHECKED) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.checkmark_type_bold,
            ),
            tint = style.checkmarkColor?.getColor(),
            width = style.checkmarkWidth,
            height = style.checkmarkHeight
        )
    } else {
        props.iconAfter?.let {
            ODSIcon(
                iconModel = it,
                tint = style.iconAfterColor?.getColor(),
                width = style.iconAfterWidth,
                height = style.iconAfterHeight
            )
        }
    }
}

@Composable
private fun ODSTextContainer(
    modifier: Modifier,
    props: ODSFlyoutListItemLargeProps,
    style: ODSFlyoutListItemLargeStyle
) {
    ODSColumn(
        modifier = modifier,
        gap = style.textGap,
        verticalAlignment = style.textVerticalAlignment,
        horizontalAlignment = style.textHorizontalAlignment,
        verticalArrangement = style.textVerticalArrangement
    ) {
        ODSText(
            text = props.label,
            style = style.labelTextStyle,
            color = style.labelColor,
            textAlign = style.labelTextAlign,
            overflow = style.labelTextOverflow,
        )
        if (!props.helperText.isNullOrEmpty()) {
            ODSText(
                text = props.helperText,
                style = style.helperTextTextStyle,
                color = style.helperTextColor,
                textAlign = style.helperTextTextAlign,
                overflow = style.helperTextTextOverflow,
            )
        }
    }
}
