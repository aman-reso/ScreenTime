package com.telekom.odsystem.atoms.radiobutton

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
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.radioicon.ODSRadioIcon
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconProps
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconSize
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSRadioButton composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSRadioButton(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSRadioButtonProps = ODSRadioButtonProps(),
    onClick: (() -> Unit)? = null
) {
    val pressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSRadioButtonStyle().getStyle(
        scheme = scheme,
        props = props,
    )

    ODSRadioButtonContainer(
        modifier = modifier,
        pressed = pressed,
        onClick = onClick,
        style = style,
        props = props,
        interactionSource = interactionSource,
        isHovered = isHovered,
        scheme = scheme
    )
}

@Composable
private fun ODSRadioButtonContainer(
    modifier: Modifier = Modifier,
    pressed: MutableState<Boolean>,
    onClick: (() -> Unit)? = null,
    style: ODSRadioButtonStyle,
    interactionSource: MutableInteractionSource,
    isHovered: Boolean,
    props: ODSRadioButtonProps = ODSRadioButtonProps(),
    scheme: ODSTheme
) {
    val isError =
        props.mode == ODSRadioButtonMode.ERROR && !props.disabled && !props.readOnly
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .sizeWithinBounds(
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .applySemantics(props, LocalContext.current)
            .customClickable(
                isPressed = {
                    pressed.value = it
                },
                interactionSource = interactionSource,
                onClick = onClick,
                disabled = props.disabled,
                readOnly = props.readOnly,
                role = Role.RadioButton
            ),
        gap = style.gap,
        padding = style.padding,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSRadioIcon(
            props = ODSRadioIconProps(
                size = getRadioIconSize(props.size),
                state = if (pressed.value && !props.disabled && !props.readOnly) {
                    ODSActions.PRESSED
                } else if (isHovered && !props.disabled && !props.readOnly) {
                    ODSActions.HOVERED
                } else {
                    ODSActions.DEFAULT
                },
                selected = props.selected,
                disabled = props.disabled,
                readonly = props.readOnly,
                error = isError
            ),
            scheme = scheme
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
    props: ODSRadioButtonProps,
    style: ODSRadioButtonStyle,
    scheme: ODSTheme
) {
    ODSColumn(
        modifier = modifier,
        gap = style.labelMessageGap,
        padding = style.labelMessagePadding,
        verticalAlignment = style.labelMessageVerticalAlignment,
        horizontalAlignment = style.labelMessageHorizontalAlignment,
        verticalArrangement = style.labelMessageVerticalArrangement
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = props.label,
            style = style.labelTextStyle,
            color = style.labelColor,
            textAlign = style.labelTextAlign
        )
        if (props.mode != ODSRadioButtonMode.STANDARD) {
            props.supportMessageProps?.let {
                ODSSupportMessage(
                    scheme = scheme,
                    props = it.toODSSupportMessageProps(
                        mode = props.mode,
                        disabled = props.disabled,
                    )
                )
            }
        }
    }
}

private fun getRadioIconSize(size: ODSRadioButtonSize): ODSRadioIconSize {
    return when (size) {
        ODSRadioButtonSize.SMALL -> ODSRadioIconSize.SMALL
        ODSRadioButtonSize.LARGE -> ODSRadioIconSize.LARGE
    }
}

private fun Modifier.applySemantics(
    props: ODSRadioButtonProps,
    context: Context
): Modifier {
    val isReadOnly = props.readOnly
    var contentDescription = ""
    if (isReadOnly) {
        contentDescription += "${context.getString(R.string.semantic_read_only)}\n"
    }
    return this.semantics {
        this.selected = props.selected
        this.contentDescription = contentDescription
    }
}
