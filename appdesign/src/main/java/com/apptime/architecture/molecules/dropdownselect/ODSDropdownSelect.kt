package com.telekom.odsystem.molecules.dropdownselect

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.PopupProperties
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.flyoutlistitemlarge.ODSFlyoutListItemLarge
import com.telekom.odsystem.atoms.flyoutlistitemlarge.ODSFlyoutListItemLargeProps
import com.telekom.odsystem.atoms.flyoutlistitemsmall.ODSFlyoutListItemSmall
import com.telekom.odsystem.atoms.flyoutlistitemsmall.ODSFlyoutListItemSmallProps
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.REQUIRED
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.utils.buildLabelAnnotatedString

/**
 * ODSDropdownSelect composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 * @param selectedOption Parameter for customization.
 */
@Composable
fun ODSDropdownSelect(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSDropdownSelectProps = ODSDropdownSelectProps(),
    onClick: () -> Unit = { },
    selectedOption: (ODSDropdownSelectOptions) -> Unit = { },
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isActionable = !props.disabled && !props.readOnly
    val state = when {
        isPressed && isActionable -> ODSActions.PRESSED
        isHovered && isActionable -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }
    val style = ODSDropdownSelectStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )
    ODSDropdownSelectContainer(
        modifier = modifier,
        scheme = scheme,
        props = props,
        style = style,
        onClick = onClick,
        isPressed = { isPressed = it },
        interactionSource = interactionSource,
        selectedValue = selectedOption
    )
}

@Composable
private fun ODSDropdownSelectContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSDropdownSelectProps,
    style: ODSDropdownSelectStyle,
    onClick: () -> Unit,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    selectedValue: (ODSDropdownSelectOptions) -> Unit,
) {
    val isDismissing = remember { mutableStateOf(false) }
    val pressed = remember { mutableStateOf(false) }
    LaunchedEffect(isDismissing.value, pressed.value) {
        if (isDismissing.value && !pressed.value) {
            onClick()
            isDismissing.value = false
            pressed.value = false
        }
    }
    ODSColumn(
        modifier = modifier.sizeWithinBounds(
            minWidth = MIN_WIDTH.dp,
            minHeight = style.inputFieldMinHeight ?: MIN_HEIGHT.dp
        ),
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        ODSDropdownContainer(
            modifier = Modifier.customClickable(
                isPressed = {
                    isPressed(it)
                    pressed.value = it
                },
                interactionSource = interactionSource,
                onClick = {
                    onClick()
                    isDismissing.value = false
                },
                disabled = props.disabled || props.readOnly,
                role = Role.DropdownList
            ),
            props = props,
            style = style,
            scheme = scheme,
            onDismissRequest = {
                isDismissing.value = true
            },
            selectedValue = {
                selectedValue(it)
            })

        if (props.mode != ODSDropdownSelectMode.STANDARD) {
            props.supportMessageProps?.let {
                ODSupportMessageContainer(
                    style = style,
                    supportMessageProps = it.toODSSupportMessageProps(
                        mode = props.mode,
                        disabled = props.disabled
                    ),
                    scheme = scheme
                )
            }
        }
    }
}

@Composable
private fun ODSupportMessageContainer(
    style: ODSDropdownSelectStyle,
    supportMessageProps: ODSSupportMessageProps,
    scheme: ODSTheme,
) {
    ODSRow(
        padding = style.supportTextPadding,
        verticalAlignment = style.supportTextVerticalAlignment,
        horizontalAlignment = style.supportTextHorizontalAlignment,
        horizontalArrangement = style.supportTextHorizontalArrangement
    ) {
        ODSSupportMessage(
            modifier = Modifier.clearAndSetSemantics { /*Handle Semantics in ODSInputFieldContainer*/ },
            scheme = scheme,
            props = supportMessageProps
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
private fun ODSDropdownContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSDropdownSelectProps,
    onDismissRequest: () -> Unit,
    selectedValue: (ODSDropdownSelectOptions) -> Unit,
    style: ODSDropdownSelectStyle,
) {

    ExposedDropdownMenuBox(expanded = props.expanded, onExpandedChange = {}) {

        ODSInputFieldContainer(
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            props = props,
            style = style
        )

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
            shape = style.inputFieldCornerRadius?.let {
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
            onDismissRequest = { onDismissRequest() },
            scrollState = rememberScrollState(),
            properties = PopupProperties(dismissOnClickOutside = true, focusable = true)
        ) {
            props.options?.forEach {
                if (props.size == ODSDropdownSelectSize.LARGE) {
                    val menuListItemProps = mapToODSMenuListItemLargeProps(it, props.selectedValue)
                    ODSFlyoutListItemLarge(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (!it.disabled) {
                                selectedValue(it)
                            }
                        },
                        props = menuListItemProps,
                        scheme = scheme
                    )
                } else {
                    val menuListItemProps = mapToODSMenuListItemSmallProps(it, props.selectedValue)
                    ODSFlyoutListItemSmall(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (!it.disabled) {
                                selectedValue(it)
                            }
                        },
                        props = menuListItemProps,
                        scheme = scheme
                    )
                }
            }
        }
    }
}

@Composable
private fun ODSInputFieldContainer(
    modifier: Modifier,
    props: ODSDropdownSelectProps,
    style: ODSDropdownSelectStyle,
) {
    ODSRow(
        gap = style.inputFieldGap,
        modifier = modifier
            .applySemantics(props, LocalContext.current)
            .sizeWithinBounds(
                minWidth = MIN_WIDTH.dp,
                minHeight = style.inputFieldMinHeight ?: MIN_HEIGHT.dp
            )
            .fillMaxWidth(),
        padding = style.inputFieldPadding,
        cornerRadius = style.inputFieldCornerRadius,
        border = ODSBorder(
            width = style.inputFieldBorder,
            style.inputFieldBorderColor
        ),
        background = style.inputFieldBackground,
        verticalAlignment = style.inputFieldVerticalAlignment,
        horizontalAlignment = style.inputFieldHorizontalAlignment,
        horizontalArrangement = style.inputFieldHorizontalArrangement,
    ) {
        ODSContentContainer(modifier = Modifier.weight(1f), props = props, style = style)
        ODSExpandAndCollapseIcon(props = props, style = style)
    }
}

@Composable
private fun ODSContentContainer(
    modifier: Modifier,
    props: ODSDropdownSelectProps,
    style: ODSDropdownSelectStyle,
) {
    val labelAnimationDuration = DEFAULT_ANIMATION_DURATION
    ODSBox(modifier = modifier) {
        ODSColumn(
            modifier = Modifier,
            gap = style.contentGap,
            verticalAlignment = style.contentVerticalAlignment,
            horizontalAlignment = style.contentHorizontalAlignment,
            verticalArrangement = style.contentVerticalArrangement
        ) {
            val isLabelVisible =
                !props.selectedValue?.label.isNullOrEmpty() && !props.label.isNullOrEmpty()
            AnimatedVisibility(
                visible = isLabelVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = labelAnimationDuration)) + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight - fullHeight / 2 },
                    animationSpec = tween(durationMillis = labelAnimationDuration)
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = labelAnimationDuration)) + slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight - fullHeight / 2 },
                    animationSpec = tween(durationMillis = labelAnimationDuration)
                )
            ) {
                ODSEyebrowContainer(style = style, props = props)
            }
            if (!props.selectedValue?.label.isNullOrEmpty()) {
                ODSInputContainer(style = style, props = props)
            }
        }
        if (props.selectedValue?.label.isNullOrEmpty() && !props.label.isNullOrEmpty()) {
            ODSEyebrowContainer(style = style, props = props)
        }
    }
}

@Composable
private fun ODSInputContainer(style: ODSDropdownSelectStyle, props: ODSDropdownSelectProps) {
    ODSRow(
        modifier = Modifier.sizeWithinBounds(
            minHeight = style.inputMinHeight ?: Dp.Unspecified
        ),
        gap = style.inputGap,
        verticalAlignment = style.inputVerticalAlignment,
        horizontalAlignment = style.inputHorizontalAlignment,
        horizontalArrangement = style.inputHorizontalArrangement,
        clipContent = style.inputClipContent != false
    ) {
        props.icon?.let {
            ODSLabelIconContainer(style = style, props = props)
        }
        ODSText(
            text = props.selectedValue?.label,
            style = style.valueStyle,
            color = style.valueColor,
            textAlign = style.valueTextAlign,
            overflow = style.valueOverflow,
            maxLines = style.valueMaxLines
        )
    }
}

@Composable
private fun ODSEyebrowContainer(style: ODSDropdownSelectStyle, props: ODSDropdownSelectProps) {
    if (props.label.isNullOrEmpty()) {
        return
    }
    ODSRow(
        gap = style.eyebrowGap,
        verticalAlignment = style.eyebrowVerticalAlignment,
        horizontalAlignment = style.eyebrowHorizontalAlignment,
        horizontalArrangement = style.eyebrowHorizontalArrangement
    ) {
        ODSText(
            modifier = Modifier.clearAndSetSemantics {},
            text = buildLabelAnnotatedString(
                label = props.label,
                isRequired = props.required,
                labelStyle = style.labelStyle?.toTextStyle() ?: TextStyle(),
                labelTextAlign = style.labelTextAlign ?: TextAlign.Left,
                labelColor = style.labelColor?.getColor() ?: Color.Transparent,
                requiredStyle = style.requiredStyle?.toTextStyle() ?: TextStyle(),
                requiredTextAlign = style.requiredTextAlign ?: TextAlign.Left,
                requiredColor = style.requiredColor?.getColor() ?: Color.Transparent,
            ),
            style = style.labelStyle,
            color = style.labelColor,
            textAlign = style.labelTextAlign,
        )
    }
}

@Composable
private fun ODSExpandAndCollapseIcon(
    style: ODSDropdownSelectStyle,
    props: ODSDropdownSelectProps,
) {
    ODSRow(
        width = style.expandAndCollapseIconWidth,
        height = style.expandAndCollapseIconHeight,
        padding = style.expandAndCollapseIconPadding,
        verticalAlignment = style.expandAndCollapseIconVerticalAlignment,
        horizontalAlignment = style.expandAndCollapseIconHorizontalAlignment,
        horizontalArrangement = style.expandAndCollapseIconHorizontalArrangement,
        clipContent = style.expandAndCollapseIconClipContent != false
    ) {
        if (props.expanded) {
            ODSIcon(
                width = style.collapseUpWidth,
                height = style.collapseUpHeight,
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.collapse_up_type_standard,
                ),
                tint = style.collapseUpColor?.getColor()
            )
        } else {
            ODSIcon(
                width = style.collapseDownWidth,
                height = style.collapseDownHeight,
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.collapse_down_type_standard,
                ),
                tint = style.collapseDownColor?.getColor()
            )
        }
    }
}

@Composable
private fun ODSLabelIconContainer(
    style: ODSDropdownSelectStyle,
    props: ODSDropdownSelectProps,
) {
    ODSIcon(
        width = style.iconWidth,
        height = style.iconHeight,
        iconModel = props.icon,
        tint = style.iconColor?.getColor()
    )
}

private fun calculateHeightInMax(): Dp {
    val menuListMinItemHeight = MIN_HEIGHT.dp
    return MENU_LIST_ITEMS * menuListMinItemHeight + PADDING.dp
}

private fun mapToODSMenuListItemLargeProps(
    options: ODSDropdownSelectOptions,
    selectedValue: ODSDropdownSelectOptions?,
): ODSFlyoutListItemLargeProps {
    return ODSFlyoutListItemLargeProps(
        options = options,
        selected = options == selectedValue
    )
}

private fun mapToODSMenuListItemSmallProps(
    options: ODSDropdownSelectOptions,
    selectedValue: ODSDropdownSelectOptions?,
): ODSFlyoutListItemSmallProps {
    return ODSFlyoutListItemSmallProps(
        options = options,
        selected = options == selectedValue
    )
}

private fun Modifier.applySemantics(
    props: ODSDropdownSelectProps,
    context: Context,
): Modifier {
    val isError = props.mode == ODSDropdownSelectMode.ERROR && !props.disabled
    val isInfo = props.mode == ODSDropdownSelectMode.INFORMATIVE
    val isReadOnly = props.readOnly
    var contentDescription = props.label.orEmpty().removeSuffix(REQUIRED)
    if (isError) {
        contentDescription += "\n${
            context.getString(
                R.string.semantic_error_sentence,
            ) + props.supportMessageProps?.message.orEmpty()
        }"
    }
    if (isInfo) {
        contentDescription += "\n${
            context.getString(
                R.string.semantic_info_sentence,
            ) + props.supportMessageProps?.message.orEmpty()
        }"
    }
    if (props.required) {
        contentDescription += "\n${context.getString(R.string.semantic_input_required)}"
    }
    if (isReadOnly) {
        contentDescription += "\n${context.getString(R.string.semantic_read_only)}"
    }
    return this.semantics {
        this.contentDescription = contentDescription
    }
}

private const val PADDING = 32
private const val MENU_LIST_ITEMS = 6
private const val DEFAULT_DROPDOWN_SHAPE = 4
