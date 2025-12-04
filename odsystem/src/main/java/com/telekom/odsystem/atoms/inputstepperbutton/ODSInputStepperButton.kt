package com.telekom.odsystem.atoms.inputstepperbutton

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
@Composable
fun ODSInputStepperButton(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSInputStepperButtonProps = ODSInputStepperButtonProps(),
    onClick: () -> Unit,
) {

    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        isPressed && !props.disabled -> ODSActions.PRESSED
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSInputStepperButtonStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )

    ODSBox(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: Dp.Unspecified,
                minHeight = style.minHeight ?: Dp.Unspecified,
                maxWidth = style.maxWidth ?: Dp.Unspecified,
                maxHeight = style.maxHeight ?: Dp.Unspecified
            )
            .customClickable(
                interactionSource = interactionSource,
                isPressed = {
                    isPressed = it
                },
                onClick = onClick,
                role = Role.Button,
                disabled = props.disabled
            ),
        contentAlignment = style.contentAlignment,
    ) {
        ODSBox(
            modifier = Modifier
                .align(
                    alignment = style.buttonBgContentAlignment ?: Alignment.TopStart
                ),
            cornerRadius = style.buttonBgBorderRadius,
            background = style.buttonBgBackgroundColor,
            width = style.buttonBgWidth,
            height = style.buttonBgHeight,
        ) {
        }
        ODSRow(
            modifier = Modifier.sizeWithinBounds(
                minWidth = style.minWidth ?: Dp.Unspecified,
                minHeight = style.minHeight ?: Dp.Unspecified,
                maxWidth = style.maxWidth ?: Dp.Unspecified,
                maxHeight = style.maxHeight ?: Dp.Unspecified
            ),
            horizontalArrangement = style.horizontalArrangement,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
        ) {
            ODSIcon(
                width = style.buttonIconWidth,
                height = style.buttonIconHeight,
                tint = style.buttonIconColor?.getColor(),
                iconModel = props.buttonIcon
            )
        }
    }
}
