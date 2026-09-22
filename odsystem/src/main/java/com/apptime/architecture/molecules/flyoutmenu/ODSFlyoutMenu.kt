package com.telekom.odsystem.molecules.flyoutmenu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.PopupProperties
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.flyoutlistitemlarge.ODSFlyoutListItemLarge
import com.telekom.odsystem.atoms.flyoutlistitemlarge.ODSFlyoutListItemLargeProps
import com.telekom.odsystem.atoms.flyoutlistitemsmall.ODSFlyoutListItemSmall
import com.telekom.odsystem.atoms.flyoutlistitemsmall.ODSFlyoutListItemSmallProps
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSFlyoutMenu composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback for chip click.
 * @param onDismissRequest Called when dropdown is dismissed.
 * @param onMenuListItemClicked Callback triggered when action occurs.
 */
@Composable
fun ODSFlyoutMenu(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSFlyoutMenuProps = ODSFlyoutMenuProps(),
    onClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onMenuListItemClicked: (Int) -> Unit = { }
) {
    val style = ODSFlyoutMenuStyle().getStyle(props = props, scheme = scheme)
    ODSFlyoutMenuDropDownContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
        onClick = onClick,
        onDismissRequest = onDismissRequest,
        onMenuListItemClicked = onMenuListItemClicked
    )
}

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ODSFlyoutMenuDropDownContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSFlyoutMenuStyle,
    props: ODSFlyoutMenuProps,
    isPressed: (Boolean) -> Unit = { },
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onMenuListItemClicked: (Int) -> Unit
) {
    ExposedDropdownMenuBox(expanded = props.expanded, onExpandedChange = {}) {
        props.buttonProps?.let {
            ODSButtonContainer(
                modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                buttonProps = it,
                scheme = scheme,
                style = style,
                isPressed = isPressed,
                onClick = onClick
            )
        }

        DropdownMenu(
            modifier = Modifier
                .exposedDropdownSize(matchAnchorWidth = false)
                .background(style.dropdownBackgroundColor?.getColor() ?: Color.Transparent)
                .padding(
                    start = style.dropdownPadding?.left ?: 0.dp,
                    end = style.dropdownPadding?.right ?: 0.dp,
                    top = style.dropdownPadding?.top ?: 0.dp,
                    bottom = style.dropdownPadding?.bottom ?: 0.dp
                )
                .heightIn(max = calculateHeightInMax()),
            shape = style.dropdownBorderRadius?.let {
                RoundedCornerShape(
                    topStart = it.topLeft,
                    topEnd = it.topRight,
                    bottomStart = it.bottomLeft,
                    bottomEnd = it.bottomRight
                )
            } ?: RoundedCornerShape(DEFAULT_DROPDOWN_SHAPE.dp),
            border = if (style.dropdownBorderWidth != 0.dp) {
                BorderStroke(
                    width = style.dropdownBorderWidth ?: 0.dp,
                    color = style.dropdownBorderColor?.getColor() ?: Color.Transparent
                )
            } else {
                null
            },
            offset = style.dropdownOffset ?: DpOffset(0.dp, 0.dp),
            expanded = props.expanded,
            onDismissRequest = onDismissRequest,
            scrollState = rememberScrollState(),
            properties = PopupProperties(dismissOnClickOutside = true, focusable = true)
        ) {
            ODSBox(
                contentAlignment = style.flyoutContainerContentAlignment,
            ) {
                ODSColumn(
                    verticalArrangement = style.flyoutContainerVerticalArrangement,
                    verticalAlignment = style.flyoutContainerVerticalAlignment,
                    horizontalAlignment = style.flyoutContainerHorizontalAlignment,
                    modifier = Modifier.align(
                        alignment = style.flyoutContainerContentAlignment
                            ?: Alignment.TopStart
                    )
                ) {
                    props.options?.forEachIndexed { index, it ->
                        if (props.menuSize == ODSFlyoutMenuMenuSize.LARGE) {
                            ODSFlyoutListItemLarge(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onMenuListItemClicked(index)
                                },
                                props = ODSFlyoutListItemLargeProps(it),
                                scheme = scheme
                            )
                        } else {
                            ODSFlyoutListItemSmall(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onMenuListItemClicked(index)
                                },
                                props = ODSFlyoutListItemSmallProps(it),
                                scheme = scheme
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ODSButtonContainer(
    modifier: Modifier,
    buttonProps: ODSFlyoutMenuButtonProps,
    scheme: ODSTheme,
    style: ODSFlyoutMenuStyle,
    isPressed: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    ODSColumn(
        modifier = modifier,
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        ODSButton(
            scheme = scheme,
            props = buttonProps.toODSButtonProps(),
            isPressed = isPressed,
            onClick = onClick
        )
    }
}

private fun calculateHeightInMax(): Dp {
    val menuListMinItemHeight = MIN_HEIGHT.dp
    return MENU_LIST_ITEMS * menuListMinItemHeight + PADDING.dp
}

private const val PADDING = 32
private const val MENU_LIST_ITEMS = 6
private const val DEFAULT_DROPDOWN_SHAPE = 4
