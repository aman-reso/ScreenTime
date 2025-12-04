package com.telekom.odsystem.atoms.flyoutlistitemsmall

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
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
 * ODSFlyoutListItemSmall composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param onClick Callback triggered when action occurs.
 * @param props Visual configuration for the component.
 */
fun ODSFlyoutListItemSmall(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onClick: () -> Unit,
    props: ODSFlyoutListItemSmallProps = ODSFlyoutListItemSmallProps()
) {
    val isPressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSFlyoutListItemSmallStyle().getStyle(
        props = props,
        scheme = scheme,
        state = if (isPressed.value && !props.disabled) ODSActions.PRESSED else if (isHovered && !props.disabled) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    ODSFlyoutListItemSmallContainer(
        modifier = modifier,
        props = props,
        isPressed = { isPressed.value = it },
        interactionSource = interactionSource,
        onClick = onClick,
        style = style
    )
}

@Composable
private fun ODSFlyoutListItemSmallContainer(
    modifier: Modifier,
    props: ODSFlyoutListItemSmallProps,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    style: ODSFlyoutListItemSmallStyle
) {
    ODSRow(
        gap = style.gap,
        modifier = modifier
            .sizeWithinBounds(
                minWidth = MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .semantics {
                if (props.variant == ODSFlyoutListItemSmallVariant.CHECKED) {
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

        ODSTextContainer(props = props, style = style, modifier = Modifier.weight(1f))
        ODSEndContentContainer(props = props, style = style)
    }
}

@Composable
private fun ODSEndContentContainer(
    props: ODSFlyoutListItemSmallProps,
    style: ODSFlyoutListItemSmallStyle
) {
    if (props.variant == ODSFlyoutListItemSmallVariant.CHECKED) {
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
    props: ODSFlyoutListItemSmallProps,
    style: ODSFlyoutListItemSmallStyle
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
            modifier = Modifier.fillMaxWidth()
        )
        if (!props.helperText.isNullOrEmpty()) {
            ODSText(
                text = props.helperText,
                style = style.helperTextTextStyle,
                color = style.helperTextColor,
                textAlign = style.helperTextTextAlign,
                overflow = style.helperTextTextOverflow,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
