package com.telekom.odsystem.atoms.filterchip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.PopupProperties
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.filterchipdropdownitem.ODSFilterChipDropdownItem
import com.telekom.odsystem.atoms.filterchipdropdownitem.ODSFilterChipDropdownItemProps
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SINGLE_LINE
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSFilterChip composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Theme scheme (defaults to [neutralScheme]).
 * @param props Configuration for label, options, and disabled state.
 * @param onClick Callback for chip click.
 * @param onDismissRequest Called when dropdown is dismissed.
 * @param selectedOption Callback with selected [ODSFilterChipOptions].
 */
@Composable
fun ODSFilterChip(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSFilterChipProps = ODSFilterChipProps(),
    onClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    selectedOption: (ODSFilterChipOptions) -> Unit = {},
) {

    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        isPressed && !props.disabled -> ODSActions.PRESSED
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }
    val style = ODSFilterChipStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )

    ODSFilterDropdownContainer(
        modifier = modifier,
        style = style,
        props = props,
        scheme = scheme,
        isPressed = { isPressed = it },
        interactionSource = interactionSource,
        onClick = onClick,
        selectedOption = selectedOption,
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ODSFilterDropdownContainer(
    modifier: Modifier,
    style: ODSFilterChipStyle,
    props: ODSFilterChipProps,
    scheme: ODSTheme,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    selectedOption: (ODSFilterChipOptions) -> Unit,
    onDismissRequest: () -> Unit
) {
    ExposedDropdownMenuBox(expanded = props.expanded, onExpandedChange = {}) {
        ODSFilterChipContainer(
            modifier = modifier
                .customClickable(
                    interactionSource = interactionSource,
                    onClick = onClick,
                    isPressed = isPressed,
                    disabled = props.disabled,
                    role = Role.DropdownList
                )
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            style = style,
            props = props,
        )

        ODSDropdownContainer(
            props = props,
            scheme = scheme,
            style = style,
            selectedOption = selectedOption,
            onDismissRequest = onDismissRequest,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposedDropdownMenuBoxScope.ODSDropdownContainer(
    props: ODSFilterChipProps,
    scheme: ODSTheme,
    style: ODSFilterChipStyle,
    onDismissRequest: () -> Unit,
    selectedOption: (ODSFilterChipOptions) -> Unit
) {
    DropdownMenu(
        modifier = Modifier
            .exposedDropdownSize(matchAnchorWidth = true)
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
        props.options?.forEach {
            val filterChipDropdownItemProps = filterSelectedOption(it, props.selectedValue)
            ODSFilterChipDropdownItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (!it.disabled) {
                        selectedOption(it)
                    }
                },
                props = filterChipDropdownItemProps,
                scheme = scheme
            )
        }
    }
}

@Composable
private fun ODSFilterChipContainer(
    modifier: Modifier,
    style: ODSFilterChipStyle,
    props: ODSFilterChipProps,
) {
    ODSColumn(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            ),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment
    ) {
        ODSFilterChipLabelContainer(style = style, props = props)
    }
}

@Composable
private fun ODSFilterChipLabelContainer(
    style: ODSFilterChipStyle,
    props: ODSFilterChipProps
) {
    ODSRow(
        modifier = Modifier.sizeWithinBounds(
            minWidth = style.filterChipMinWidth ?: MIN_WIDTH.dp,
            minHeight = style.filterChipMinHeight ?: MIN_HEIGHT.dp
        ),
        gap = style.filterChipGap,
        horizontalArrangement = style.filterChipHorizontalArrangement,
        verticalAlignment = style.filterChipVerticalAlignment,
        horizontalAlignment = style.filterChipHorizontalAlignment,
        padding = style.filterChipPadding,
        cornerRadius = style.filterChipCornerRadius,
        border = ODSBorder(
            width = style.filterChipBorder,
            colorList = style.filterChipBorderColor
        ),
        background = style.filterChipBackground,
    ) {
        ODSText(
            modifier = Modifier.weight(1f, fill = false),
            text = props.selectedValue?.labelText ?: props.label,
            style = style.filterStyle,
            color = style.filterColor,
            textAlign = style.filterTextAlign,
            overflow = style.filterOverflow,
            maxLines = SINGLE_LINE
        )
        if (!props.expanded) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.collapse_down_type_standard,
                ),
                width = style.collapseDownWidth,
                height = style.collapseDownHeight,
                tint = style.collapseDownColor?.getColor()
            )
        } else {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.collapse_up_type_standard,
                ),
                width = style.collapseUpWidth,
                height = style.collapseUpHeight,
                tint = style.collapseUpColor?.getColor()
            )
        }
    }
}

private fun filterSelectedOption(
    options: ODSFilterChipOptions,
    selectedValue: ODSFilterChipOptions?
): ODSFilterChipDropdownItemProps {
    return if (options == selectedValue) {
        ODSFilterChipDropdownItemProps(
            disabled = options.disabled,
            leftIcon = options.iconBefore,
            label = options.labelText,
            selected = true
        )
    } else {
        ODSFilterChipDropdownItemProps(
            disabled = options.disabled,
            leftIcon = options.iconBefore,
            label = options.labelText,
            selected = false
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
