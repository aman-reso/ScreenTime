package com.telekom.odsystem.atoms.filterchipdropdownitem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
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
 * ODSFilterChipDropdownItem composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param onClick Callback triggered when action occurs.
 * @param props Visual configuration for the component.
 */
fun ODSFilterChipDropdownItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onClick: () -> Unit,
    props: ODSFilterChipDropdownItemProps = ODSFilterChipDropdownItemProps()
) {
    val isPressed = remember { mutableStateOf(false) }

    val style = ODSFilterChipDropdownItemStyle().getStyle(
        props = props,
        scheme = scheme,
        state = if (isPressed.value && !props.disabled) ODSActions.PRESSED else ODSActions.DEFAULT
    )

    ODSFilterChipDropdownItemContainer(
        modifier = modifier,
        props = props,
        isPressed = { isPressed.value = it },
        style = style,
        onClick = onClick
    )
}

@Composable
private fun ODSFilterChipDropdownItemContainer(
    modifier: Modifier,
    props: ODSFilterChipDropdownItemProps,
    isPressed: (Boolean) -> Unit,
    style: ODSFilterChipDropdownItemStyle,
    onClick: () -> Unit,
) {
    ODSRow(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .semantics {
                if (props.selected) {
                    this.selected = true
                }
            }
            .customClickable(
                isPressed = { isPressed(it) },
                onClick = onClick,
                disabled = props.disabled
            ),
        padding = style.padding,
        cornerRadius = style.cornerRadius,
        horizontalArrangement = style.horizontalArrangement,
        verticalAlignment = style.verticalAlignment,
        background = style.background
    ) {
        ODSFilterChipDropdownContentFrame(props = props, style = style)
    }
}

@Composable
private fun ODSFilterChipDropdownContentFrame(
    props: ODSFilterChipDropdownItemProps,
    style: ODSFilterChipDropdownItemStyle,
) {
    ODSRow(
        gap = style.contentFrameGap,
        horizontalArrangement = style.contentFrameHorizontalArrangement,
        horizontalAlignment = style.contentFrameHorizontalAlignment,
        verticalAlignment = style.contentFrameVerticalAlignment
    ) {
        if (props.leftIcon != null) {
            ODSIcon(
                iconModel = props.leftIcon,
                width = style.leftIconWidth,
                height = style.leftIconHeight,
                tint = style.leftIconColor?.getColor()
            )
        }
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                text = props.label,
                style = style.labelStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                overflow = style.labelOverflow
            )
        }
    }
    ODSRow(
        padding = style.iconContainerPadding,
        width = style.iconContainerWidth,
        height = style.iconContainerWidth,
        horizontalArrangement = style.iconContainerHorizontalArrangement,
        horizontalAlignment = style.iconContainerHorizontalAlignment,
        verticalAlignment = style.iconContainerVerticalAlignment,
    ) {
        if (props.selected) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = R.drawable.checkmark_type_bold),
                width = style.checkmarkWidth,
                height = style.checkmarkHeight,
                tint = style.checkmarkColor?.getColor()
            )
        }
    }
}
