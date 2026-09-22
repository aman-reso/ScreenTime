package com.telekom.odsystem.atoms.switch

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIcon
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconProps
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconSize
import com.telekom.odsystem.extensions.invokeWith
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSSwitch composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onCheckedChange callback invoked when the switch is toggled
 */
@Composable
fun ODSSwitch(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSwitchProps = ODSSwitchProps(),
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    val style = ODSSwitchStyle().getStyle(scheme = scheme, props = props)

    ODSSwitchContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
        onClick = onCheckedChange.invokeWith { !props.selected }
    )
}

@Composable
private fun ODSSwitchContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSwitchProps = ODSSwitchProps(),
    style: ODSSwitchStyle,
    onClick: (() -> Unit)? = null,
) {

    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val context = LocalContext.current

    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
            .customClickable(
                interactionSource = interactionSource,
                isPressed = {
                    pressed = it
                },
                onClick = onClick,
                role = Role.Switch,
                disabled = props.disabled || props.readOnly
            )
            .applySemantics(props = props, context = context),
        gap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        val label = props.label
        if (!label.isNullOrEmpty()) {
            ODSSwitchLabelContainer(style = style, label = label, modifier = Modifier.weight(1f))
        }
        ODSSwitchIconContainer(
            scheme = scheme,
            style = style,
            pressed = pressed,
            isHovered = isHovered,
            props = props
        )
    }
}

@Composable
private fun ODSSwitchIconContainer(
    scheme: ODSTheme,
    style: ODSSwitchStyle,
    pressed: Boolean,
    isHovered: Boolean,
    props: ODSSwitchProps,
) {
    ODSColumn(
        padding = style.switchIconContainerPadding,
        verticalAlignment = style.switchIconContainerVerticalAlignment,
        verticalArrangement = style.switchIconContainerVerticalArrangement,
        horizontalAlignment = style.switchIconContainerHorizontalAlignment
    ) {
        ODSSwitchIcon(
            scheme = scheme,
            props = ODSSwitchIconProps(
                disabled = props.disabled,
                selected = props.selected,
                readOnly = props.readOnly,
                size = when (props.size) {
                    ODSSwitchSize.LARGE -> ODSSwitchIconSize.LARGE
                    ODSSwitchSize.SMALL -> ODSSwitchIconSize.SMALL
                },
                state = when {
                    pressed && !props.disabled && !props.readOnly -> ODSActions.PRESSED
                    isHovered && !props.disabled && !props.readOnly -> ODSActions.HOVERED
                    else -> ODSActions.DEFAULT
                }
            )
        )
    }
}

@Composable
private fun ODSSwitchLabelContainer(modifier: Modifier, style: ODSSwitchStyle, label: String) {
    ODSRow(
        padding = style.labelContainerPadding,
        horizontalAlignment = style.labelContainerHorizontalAlignment,
        horizontalArrangement = style.labelContainerHorizontalArrangement,
        verticalAlignment = style.labelContainerVerticalAlignment,
        modifier = modifier
    ) {
        ODSText(
            text = label,
            style = style.switchLabelTextStyle,
            color = style.switchLabelColor,
            textAlign = style.switchLabelTextAlign,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun Modifier.applySemantics(
    props: ODSSwitchProps,
    context: Context
): Modifier {
    val isReadOnly = props.readOnly
    var contentDescription = ""

    if (isReadOnly) {
        contentDescription += "${context.getString(R.string.semantic_read_only)}\n"
    }
    return this.semantics {
        this.toggleableState = ToggleableState(props.selected)
        this.contentDescription = contentDescription
    }
}
