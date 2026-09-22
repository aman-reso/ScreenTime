package com.telekom.odsystem.organisms.cardchoice

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIcon
import com.telekom.odsystem.atoms.radioicon.ODSRadioIcon
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIcon
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.scaleAnimationSpec
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-10 (v1.33.1) - uid: 22378211
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=84-11470
 */

@Composable
fun ODSCardChoice(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardChoiceProps = ODSCardChoiceProps(),
    contentSlot: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        isPressed && !props.disabled && !props.readOnly -> ODSActions.PRESSED
        isHovered && !props.disabled && !props.readOnly -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSCardChoiceStyle().getStyle(scheme = scheme, props = props)

    ODSCardChoiceContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
        contentSlot = contentSlot,
        interactionSource = interactionSource,
        isPressed = { isPressed = it },
        pressed = isPressed,
        isHovered = isHovered,
        state = state,
        onClick = onClick
    )
}

@Composable
private fun ODSCardChoiceContainer(
    modifier: Modifier,
    scheme: ODSTheme = neutralScheme,
    style: ODSCardChoiceStyle,
    props: ODSCardChoiceProps,
    contentSlot: (@Composable () -> Unit)?,
    interactionSource: MutableInteractionSource,
    isPressed: (Boolean) -> Unit,
    pressed: Boolean,
    isHovered: Boolean,
    state: ODSActions,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = scaleAnimationSpec,
        label = ""
    )

    ODSBox(
        modifier = modifier
            .applySemantics(
                props = props,
                context = context
            )
            .height(IntrinsicSize.Min)
            .customClickable(
                disabled = props.disabled,
                readOnly = props.readOnly,
                interactionSource = interactionSource,
                isPressed = isPressed,
                onClick = onClick,
                role = getRole(props = props)
            ),
        contentAlignment = style.zStackContentAlignment
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
            cornerRadius = style.cardBgCornerRadius,
            border = ODSBorder(width = style.cardBgBorder, colorList = style.cardBgBorderColor),
            verticalAlignment = style.cardBgVerticalAlignment,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            verticalArrangement = style.cardBgVerticalArrangement,
            background = style.cardBgBackground
        ) {
        }
        ODSColumn(
            modifier = Modifier.fillMaxHeight(),
            gap = style.gap,
            padding = style.padding,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            verticalArrangement = style.verticalArrangement,
        ) {
            ODSContentContainer(
                scheme = scheme,
                style = style,
                props = props,
                contentSlot = contentSlot,
                state = state
            )
        }
    }
}

@Composable
private fun ODSContentContainer(
    scheme: ODSTheme = neutralScheme,
    style: ODSCardChoiceStyle,
    props: ODSCardChoiceProps,
    contentSlot: (@Composable () -> Unit)?,
    state: ODSActions
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        gap = style.contentGap,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalArrangement = style.contentHorizontalArrangement
    ) {
        if (props.selectorPosition == ODSCardChoiceSelectorPosition.LEFT) {
            ODSSelectorContainer(
                scheme = scheme,
                horizontalAlignment = style.selectorContainerLeftHorizontalAlignment,
                verticalAlignment = style.selectorContainerLeftVerticalAlignment,
                horizontalArrangement = style.selectorContainerLeftHorizontalArrangement,
                props = props,
                state = state
            )
        }
        ODSColumn(
            modifier = Modifier.weight(1f),
            verticalAlignment = style.contentContainerVerticalAlignment,
            horizontalAlignment = style.contentContainerHorizontalAlignment,
            verticalArrangement = style.contentContainerVerticalArrangement
        ) {
            contentSlot?.invoke()
        }
        if (props.selectorPosition == ODSCardChoiceSelectorPosition.RIGHT) {
            ODSSelectorContainer(
                scheme = scheme,
                horizontalAlignment = style.selectorContainerRightHorizontalAlignment,
                verticalAlignment = style.selectorContainerRightVerticalAlignment,
                horizontalArrangement = style.selectorContainerRightHorizontalArrangement,
                props = props,
                state = state
            )
        }
    }
}

@Composable
private fun ODSSelectorContainer(
    scheme: ODSTheme = neutralScheme,
    horizontalAlignment: Alignment.Horizontal? = null,
    verticalAlignment: Alignment.Vertical? = null,
    horizontalArrangement: Arrangement.Horizontal? = null,
    props: ODSCardChoiceProps,
    state: ODSActions
) {
    ODSRow(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement
    ) {
        when (props.type) {
            ODSCardChoiceType.RADIO_CHOICE -> {
                ODSRadioIcon(
                    scheme = scheme,
                    props = props.radioIconProps.toODSRadioIconProps(
                        disabled = props.disabled,
                        selected = props.selected,
                        state = state
                    )
                )
            }

            ODSCardChoiceType.CHECKBOX_CHOICE -> {
                ODSCheckboxIcon(
                    scheme = scheme,
                    props = props.checkboxIconProps.toODSCheckboxIconProps(
                        disabled = props.disabled,
                        selected = props.selected,
                        state = state
                    )
                )
            }

            ODSCardChoiceType.SWITCH_CHOICE -> {
                ODSSwitchIcon(
                    scheme = scheme,
                    props = props.switchIconProps.toODSSwitchIconProps(
                        disabled = props.disabled,
                        selected = props.selected,
                        state = state
                    )
                )
            }
        }
    }
}

private fun getRole(props: ODSCardChoiceProps): Role {
    return when (props.type) {
        ODSCardChoiceType.CHECKBOX_CHOICE -> Role.Checkbox
        ODSCardChoiceType.RADIO_CHOICE -> Role.RadioButton
        ODSCardChoiceType.SWITCH_CHOICE -> Role.Switch
    }
}

private fun Modifier.applySemantics(
    props: ODSCardChoiceProps,
    context: Context
): Modifier {
    val isReadOnly = props.readOnly
    var contentDescription = ""
    if (isReadOnly) {
        contentDescription += "${context.getString(R.string.semantic_read_only)}\n"
    }
    return this.semantics {
        when (props.type) {
            ODSCardChoiceType.CHECKBOX_CHOICE -> this.toggleableState =
                ToggleableState(props.selected)

            ODSCardChoiceType.RADIO_CHOICE -> this.selected = props.selected
            ODSCardChoiceType.SWITCH_CHOICE -> this.toggleableState =
                ToggleableState(props.selected)
        }
        this.contentDescription = contentDescription
    }
}
