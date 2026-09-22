package com.telekom.odsystem.atoms.checkbox

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIcon
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconProps
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSelected
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSize
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.extensions.invokeWith
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCheckbox composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick callback invoked with the new [ODSCheckboxSelected] state
 */
@Composable
fun ODSCheckbox(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCheckboxProps = ODSCheckboxProps(),
    onClick: ((ODSCheckboxSelected) -> Unit)? = null
) {
    val style = ODSCheckboxStyle().getStyle(
        scheme = scheme,
        props = props,
    )

    ODSCheckBoxContainer(
        modifier = modifier,
        props = props,
        style = style,
        scheme = scheme,
        onClick = onClick?.invokeWith {
            when (props.selected) {
                ODSCheckboxSelected.SELECTED -> ODSCheckboxSelected.UNSELECTED
                ODSCheckboxSelected.UNSELECTED -> ODSCheckboxSelected.SELECTED
                ODSCheckboxSelected.INDETERMINATE -> ODSCheckboxSelected.SELECTED
            }
        }
    )
}

@Composable
private fun ODSCheckBoxContainer(
    modifier: Modifier,
    props: ODSCheckboxProps,
    style: ODSCheckboxStyle,
    scheme: ODSTheme,
    onClick: (() -> Unit)? = null
) {
    val pressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
            .applySemantics(props, LocalContext.current)
            .customClickable(
                interactionSource = interactionSource,
                isPressed = { pressed.value = it },
                onClick = onClick,
                disabled = props.disabled,
                readOnly = props.readOnly,
                role = Role.Checkbox
            ),
        gap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        ODSCheckboxIcon(
            scheme = scheme,
            props = ODSCheckboxIconProps(
                size = getCheckboxIconSize(props.size),
                readOnly = props.readOnly,
                selected = getCheckboxIconSelected(props.selected),
                state = getCheckboxIconState(
                    isHovered = isHovered,
                    pressed = pressed,
                    props = props
                ),
                disabled = props.disabled,
                error = props.mode == ODSCheckboxMode.ERROR && !props.disabled && !props.readOnly
            )
        )
        ODSLabelAndSupportTextContainer(
            modifier = Modifier.weight(1f),
            props = props,
            style = style,
            scheme = scheme
        )
    }
}

@Composable
private fun ODSLabelAndSupportTextContainer(
    modifier: Modifier,
    props: ODSCheckboxProps,
    style: ODSCheckboxStyle,
    scheme: ODSTheme
) {
    ODSColumn(
        modifier = modifier,
        gap = style.labelMessageGap,
        padding = style.labelMessagePadding,
        verticalArrangement = style.labelMessageVerticalArrangement,
        verticalAlignment = style.labelMessageVerticalAlignment,
        horizontalAlignment = style.labelMessageHorizontalAlignment,
    ) {

        ODSText(
            text = props.label,
            style = style.labelStyle,
            color = style.labelColor
        )

        if (props.mode != ODSCheckboxMode.STANDARD) {
            props.supportMessageProps?.let {
                ODSSupportMessage(
                    props = it.toODSSupportMessageProps(
                        disabled = props.disabled,
                        mode = props.mode
                    ),
                    scheme = scheme
                )
            }
        }
    }
}

private fun getCheckboxIconState(
    isHovered: Boolean = false,
    pressed: MutableState<Boolean> = mutableStateOf(false),
    props: ODSCheckboxProps
): ODSActions {
    return when {
        pressed.value && !props.disabled && !props.readOnly -> ODSActions.PRESSED
        isHovered && !props.disabled && !props.readOnly -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }
}

private fun getCheckboxIconSize(size: ODSCheckboxSize): ODSCheckboxIconSize {
    return when (size) {
        ODSCheckboxSize.SMALL -> ODSCheckboxIconSize.SMALL
        ODSCheckboxSize.LARGE -> ODSCheckboxIconSize.LARGE
    }
}

private fun getCheckboxIconSelected(selected: ODSCheckboxSelected): ODSCheckboxIconSelected {
    return when (selected) {
        ODSCheckboxSelected.SELECTED -> ODSCheckboxIconSelected.SELECTED
        ODSCheckboxSelected.UNSELECTED -> ODSCheckboxIconSelected.UNSELECTED
        ODSCheckboxSelected.INDETERMINATE -> ODSCheckboxIconSelected.INDETERMINATE
    }
}

private fun Modifier.applySemantics(
    props: ODSCheckboxProps,
    context: Context
): Modifier {
    val isReadOnly = props.readOnly
    var contentDescription = ""
    if (isReadOnly) {
        contentDescription += "${context.getString(R.string.semantic_read_only)}\n"
    }
    return this.semantics {
        this.toggleableState = if (props.selected == ODSCheckboxSelected.INDETERMINATE) {
            ToggleableState.Indeterminate
        } else {
            ToggleableState(props.selected == ODSCheckboxSelected.SELECTED)
        }
        this.contentDescription = contentDescription
    }
}
