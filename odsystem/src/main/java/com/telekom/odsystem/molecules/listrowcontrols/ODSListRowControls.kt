package com.telekom.odsystem.molecules.listrowcontrols

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconProps
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSelected
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSize
import com.telekom.odsystem.atoms.controls.ODSControls
import com.telekom.odsystem.atoms.controls.ODSControlsProps
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconProps
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconSize
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconProps
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconSize
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.OPACITY_ENABLED
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSListRowControls composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onCheckboxClick Callback triggered when action occurs.
 * @param onSwitchClick Callback triggered when action occurs.
 * @param onRadioClick Callback triggered when action occurs.
 */
@Composable
fun ODSListRowControls(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSListRowControlsProps = ODSListRowControlsProps(),
    onCheckboxClick: ((Boolean) -> Unit)? = null,
    onSwitchClick: ((Boolean) -> Unit)? = null,
    onRadioClick: (() -> Unit)? = null,
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        pressed -> ODSActions.PRESSED
        isHovered -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }
    val style = ODSListRowControlsStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )
    ODSListRowControlsContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        interactionSource = interactionSource,
        state = state,
        props = props,
        isPressed = { pressed = it },
        onClick = {
            when (props.type) {
                ODSControlsType.RADIO_ICON -> onRadioClick?.invoke()
                ODSControlsType.CHECKBOX_ICON -> onCheckboxClick?.invoke(!props.selected)
                ODSControlsType.SWITCH_ICON -> onSwitchClick?.invoke(!props.selected)
            }
        }
    )
}

@Suppress("LongMethod")
@Composable
private fun ODSListRowControlsContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSListRowControlsStyle,
    interactionSource: MutableInteractionSource,
    state: ODSActions,
    props: ODSListRowControlsProps,
    isPressed: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    ODSRow(
        modifier = modifier
            .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
            .applySemantics(props = props)
            .customClickable(
                interactionSource = interactionSource,
                disabled = props.disabled,
                readOnly = props.readOnly,
                role = getRole(type = props.type),
                isPressed = isPressed,
                onClick = onClick
            ),
        cornerRadius = style.cornerRadius,
        gap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        background = style.background,
    ) {
        if (props.variant == ODSListRowControlsVariant.IMAGE) {
            ODSRow(
                modifier = Modifier.alpha(style.imageOpacity ?: OPACITY_ENABLED),
                clipContent = style.imageClipContent != false,
                cornerRadius = style.imageCornerRadius,
                horizontalArrangement = style.imageHorizontalArrangement,
                horizontalAlignment = style.imageHorizontalAlignment,
                verticalAlignment = style.imageVerticalAlignment,
            ) {
                ODSImage(
                    width = style.image2Width,
                    height = style.image2Height,
                    imageModel = props.image,
                    cornerRadius = style.image2CornerRadius,
                    contentScale = style.image2ContentScale ?: ContentScale.Crop,
                )
            }
        }

        if (props.variant == ODSListRowControlsVariant.ICON) {
            ODSRow(
                modifier = Modifier.alpha(style.iconContainerOpacity ?: OPACITY_ENABLED),
                padding = style.iconContainerPadding,
                horizontalArrangement = style.iconContainerHorizontalArrangement,
                horizontalAlignment = style.iconContainerHorizontalAlignment,
                verticalAlignment = style.iconContainerVerticalAlignment,
                width = style.iconContainerWidth,
                height = style.iconContainerHeight
            ) {
                ODSIcon(
                    iconModel = props.icon,
                    tint = style.iconColor?.getColor(),
                    width = style.iconWidth,
                    height = style.iconHeight
                )
            }
        }
        ODSTextContentControlContainer(
            modifier = Modifier.weight(1f), // Not exported by plugin
            scheme = scheme,
            style = style,
            state = state,
            props = props
        )
    }
}

@Composable
private fun ODSTextContentControlContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSListRowControlsStyle,
    state: ODSActions,
    props: ODSListRowControlsProps
) {
    ODSRow(
        modifier = modifier,
        gap = style.textContentControlGap,
        padding = style.textContentControlPadding,
        horizontalArrangement = style.textContentControlHorizontalArrangement,
        horizontalAlignment = style.textContentControlHorizontalAlignment,
        verticalAlignment = style.textContentControlVerticalAlignment
    ) {
        ODSLabelTextContent(
            modifier = Modifier.weight(1f), // Not exported by plugin
            style = style,
            props = props
        )
        ODSControls(
            scheme = scheme,
            props = ODSControlsProps(
                checkboxIconProps = ODSCheckboxIconProps(
                    disabled = props.disabled,
                    readOnly = props.readOnly,
                    selected = if (props.selected) ODSCheckboxIconSelected.SELECTED else ODSCheckboxIconSelected.UNSELECTED,
                    size = ODSCheckboxIconSize.SMALL,
                    state = state
                ),
                radioIconProps = ODSRadioIconProps(
                    disabled = props.disabled,
                    readonly = props.readOnly,
                    selected = props.selected,
                    size = ODSRadioIconSize.SMALL,
                    state = state
                ),
                switchIconProps = ODSSwitchIconProps(
                    disabled = props.disabled,
                    readOnly = props.readOnly,
                    selected = props.selected,
                    size = ODSSwitchIconSize.SMALL,
                    state = state
                ),
                type = props.type
            )
        )
    }
}

@Composable
private fun ODSLabelTextContent(
    modifier: Modifier,
    style: ODSListRowControlsStyle,
    props: ODSListRowControlsProps
) {
    ODSColumn(
        modifier = modifier,
        gap = style.labelTextContentGap,
        padding = style.labelTextContentPadding,
        verticalArrangement = style.labelTextContentVerticalArrangement,
        verticalAlignment = style.labelTextContentVerticalAlignment,
        horizontalAlignment = style.labelTextContentHorizontalAlignment
    ) {
        if (!props.labelTitle.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.labelTitle,
                style = style.labelStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
        if (!props.labelText.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.labelText,
                style = style.labelTextStyle,
                color = style.labelTextColor,
                textAlign = style.labelTextTextAlign
            )
        }
    }
}

private fun getRole(type: ODSControlsType): Role {
    return when (type) {
        ODSControlsType.RADIO_ICON -> Role.RadioButton
        ODSControlsType.CHECKBOX_ICON -> Role.Checkbox
        ODSControlsType.SWITCH_ICON -> Role.Switch
    }
}

private fun Modifier.applySemantics(props: ODSListRowControlsProps): Modifier {
    return this.semantics {
        when (props.type) {
            ODSControlsType.RADIO_ICON -> {
                this.selected = props.selected
            }

            ODSControlsType.CHECKBOX_ICON,
            ODSControlsType.SWITCH_ICON -> {
                this.toggleableState = ToggleableState(props.selected)
            }
        }
    }
}
