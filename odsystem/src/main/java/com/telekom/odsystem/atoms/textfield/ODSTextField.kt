package com.telekom.odsystem.atoms.textfield

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.slots.textfieldicon.ODSTextFieldIcon
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.utils.buildLabelAnnotatedString

/**
 * ODSTextField composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param keyboardOptions Parameter for customization of the software keyboard options.
 * @param keyboardActions Parameter for customization of the software keyboard actions.
 * @param visualTransformation Parameter for customization of the visual transformation of the input text.
 * @param onRightIconClick Callback triggered when the right icon is clicked.
 * @param onValueChange Callback triggered when the input text changes.
 */
@Suppress("LongMethod")
@Composable
fun ODSTextField(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTextFieldProps,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onRightIconClick: (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    // Update props.isFocused before getStyle runs; not in SideEffect to ensure it's updated during composition, not after
    props.isFocused = isFocused
    val style = ODSTextFieldStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (isHovered && !props.disabled && !props.readOnly) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    val internalInputValueTextStyle = style.inputValueStyle?.toTextStyle() ?: TextStyle()
    val internalInputValueColor = style.inputValueColor?.getColor() ?: Color.Transparent
    val internalInputCursorColor = style.inputCursorColor?.getColor() ?: Color.Transparent

    val counter = props.counterText?.toIntOrNull()
    val maxCharacterReached = counter != null && counter == props.inputText?.length
    val maxCharacterText = if (maxCharacterReached) {
        context.getString(R.string.semantic_max_character_reached)
    } else {
        ""
    }
    BasicTextField(
        modifier = modifier
            .hoverable(interactionSource)
            .semantics {
                this.liveRegion = LiveRegionMode.Assertive
                this.contentDescription = maxCharacterText
            },
        enabled = !props.disabled,
        textStyle = internalInputValueTextStyle.copy(color = internalInputValueColor),
        value = props.inputText.orEmpty(),
        cursorBrush = SolidColor(internalInputCursorColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onValueChange = { newValue ->
            if (newValue.length <= (counter ?: Int.MAX_VALUE)) {
                onValueChange?.invoke(newValue)
            }
        },
        interactionSource = interactionSource,
        singleLine = true,
        readOnly = props.readOnly,
        visualTransformation = getVisualTransformation(
            props = props,
            visualTransformation = visualTransformation
        ),
        decorationBox = { innerTextField ->
            ODSColumn(
                modifier = Modifier
                    .sizeWithinBounds(
                        minWidth = MIN_WIDTH.dp,
                        minHeight = style.inputFieldMinHeight ?: MIN_WIDTH.dp
                    )
                    .fillMaxWidth(),
                gap = style.gap,
                verticalAlignment = style.verticalAlignment,
                horizontalAlignment = style.horizontalAlignment,
                verticalArrangement = style.verticalArrangement
            ) {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeWithinBounds(
                            minWidth = MIN_WIDTH.dp,
                            minHeight = style.inputFieldMinHeight ?: MIN_WIDTH.dp
                        ),
                    padding = style.inputFieldPadding,
                    cornerRadius = style.inputFieldCornerRadius,
                    border = ODSBorder(
                        width = style.inputFieldBorder,
                        colorList = style.inputFieldBorderColor
                    ),
                    background = style.inputFieldBackground,
                    verticalAlignment = style.inputFieldVerticalAlignment,
                    horizontalAlignment = style.inputFieldHorizontalAlignment,
                    horizontalArrangement = style.inputFieldHorizontalArrangement,
                    clipContent = style.inputFieldClipContent != false
                ) {
                    ODSContentContainer(
                        modifier = Modifier.weight(1f),
                        style = style,
                        props = props,
                        isFocused = isFocused,
                        innerTextField = innerTextField,
                    )
                    if (props.showRightIcon) {
                        ODSIconContainer(
                            scheme = scheme,
                            style = style,
                            props = props,
                            onRightIconClick = onRightIconClick,
                        )
                    } else {
                        ODSIconContainerDeprecated(
                            style = style,
                            props = props,
                            onRightIconClick = onRightIconClick,
                        )
                    }
                }
                if (props.mode != ODSTextFieldMode.STANDARD || (props.counterText?.toIntOrNull()
                        ?: 0) > 0
                ) {
                    ODSSupportTextContainer(
                        scheme = scheme,
                        style = style,
                        props = props
                    )
                }
            }
        }
    )
}

@Composable
fun ODSContentContainer(
    modifier: Modifier = Modifier,
    style: ODSTextFieldStyle,
    props: ODSTextFieldProps,
    isFocused: Boolean,
    innerTextField: @Composable () -> Unit,
) {
    val labelAnimationDuration = DEFAULT_ANIMATION_DURATION
    ODSBox(modifier = modifier, contentAlignment = style.contentContainerAlignment) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.contentGap,
            padding = style.contentPadding,
            verticalAlignment = style.contentVerticalAlignment,
            horizontalAlignment = style.contentHorizontalAlignment,
            verticalArrangement = style.contentVerticalArrangement
        ) {
            AnimatedVisibility(
                visible = isFocused || !props.inputText.isNullOrEmpty() || !props.placeholderText.isNullOrEmpty(),
                enter = fadeIn(animationSpec = tween(durationMillis = labelAnimationDuration)) + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight - fullHeight / 2 },
                    animationSpec = tween(durationMillis = labelAnimationDuration)
                ),
                exit = ExitTransition.None
            ) {
                ODSLabelContainer(
                    style = style,
                    props = props
                )
            }
            ODSInputContainer(
                style = style,
                props = props,
                innerTextField = innerTextField,
            )
        }
        if ((props.inputText.isNullOrEmpty() && isFocused.not()) && props.placeholderText.isNullOrEmpty()) {
            ODSLabelContainer(
                style = style,
                props = props,
            )
        }
    }
}

@Composable
fun ODSInputContainer(
    style: ODSTextFieldStyle,
    props: ODSTextFieldProps,
    innerTextField: @Composable () -> Unit,
) {
    ODSRow(
        gap = style.inputGap,
        verticalAlignment = style.inputVerticalAlignment,
        horizontalAlignment = style.inputHorizontalAlignment,
        horizontalArrangement = style.inputHorizontalArrangement
    ) {
        if (props.leftIcon != null && (!props.inputText.isNullOrEmpty() || !props.placeholderText.isNullOrEmpty())) {
            ODSIcon(
                iconModel = props.leftIcon,
                width = style.leftIconWidth,
                height = style.leftIconHeight,
                tint = style.leftIconColor?.getColor()
            )
        }
        if (!props.prefixText.isNullOrEmpty() && (!props.inputText.isNullOrEmpty() || !props.placeholderText.isNullOrEmpty())) {
            ODSText(
                text = props.prefixText.orEmpty(),
                style = style.prefixStyle,
                color = style.prefixColor,
                textAlign = style.prefixTextAlign
            )
        }
        ODSBox(modifier = Modifier.weight(1f)) {
            if (props.inputText.isNullOrEmpty() && !props.placeholderText.isNullOrEmpty()) {
                ODSText(
                    text = props.placeholderText.orEmpty(),
                    style = style.placeholderStyle,
                    color = style.placeholderColor,
                    textAlign = style.placeholderTextAlign,
                    overflow = style.placeholderOverflow,
                    maxLines = style.placeholderMaxLines
                )
            }
            innerTextField()
        }
        if (!props.suffixText.isNullOrEmpty() && (!props.inputText.isNullOrEmpty() || !props.placeholderText.isNullOrEmpty())) {
            ODSText(
                text = props.suffixText.orEmpty(),
                style = style.suffixStyle,
                color = style.suffixColor,
                textAlign = style.suffixTextAlign
            )
        }
    }
}

@Composable
private fun ODSSupportTextContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    style: ODSTextFieldStyle,
    props: ODSTextFieldProps,
) {
    ODSRow(
        modifier = modifier,
        padding = style.supportTextPadding,
        verticalAlignment = style.supportTextVerticalAlignment,
        horizontalAlignment = style.supportTextHorizontalAlignment,
        horizontalArrangement = style.supportTextHorizontalArrangement
    ) {
        if (props.mode != ODSTextFieldMode.STANDARD) {
            props.supportMessageProps?.let {
                ODSSupportMessage(
                    modifier = Modifier.weight(1f),
                    props = it.toODSSupportMessageProps(props.mode, props.disabled),
                    scheme = scheme
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
        if ((props.counterText?.toIntOrNull() ?: 0) > 0 && props.showCounter) {
            ODSText(
                text = "${props.inputText?.length ?: 0}/${props.counterText}",
                style = style.counterStyle,
                color = style.counterColor,
                textAlign = style.counterTextAlign
            )
        }
    }
}

@Composable
@Deprecated("Use ODSTextFieldIcon slot instead")
private fun ODSIconContainerDeprecated(
    style: ODSTextFieldStyle,
    props: ODSTextFieldProps,
    onRightIconClick: (() -> Unit)? = null,
) {
    if (props.rightIcon != null || props.isPasswordField) {
        ODSRow(
            modifier = if (props.isPasswordField) {
                Modifier.customClickable(
                    isPressed = {},
                    onClick = onRightIconClick,
                    disabled = false,
                    role = Role.Button
                )
            } else {
                Modifier
            },
            width = style.iconContainerWidth,
            height = style.iconContainerHeight,
            padding = style.iconContainerPadding,
            verticalAlignment = style.iconContainerVerticalAlignment,
            horizontalAlignment = style.iconContainerHorizontalAlignment,
            horizontalArrangement = style.iconContainerHorizontalArrangement
        ) {
            ODSIcon(
                width = style.rightIconWidth,
                height = style.rightIconHeight,
                iconModel = if (props.isPasswordField) {
                    getPasswordIconModel(props, LocalContext.current)
                } else {
                    props.rightIcon
                },
                tint = style.rightIconColor?.getColor()
            )
        }
    }
}

@Composable
private fun ODSIconContainer(
    scheme: ODSTheme,
    style: ODSTextFieldStyle,
    props: ODSTextFieldProps,
    onRightIconClick: (() -> Unit)? = null,
) {
    props.textFieldIconProps?.let {
        ODSRow(
            verticalAlignment = style.iconContainerVerticalAlignment,
            horizontalAlignment = style.iconContainerHorizontalAlignment,
            horizontalArrangement = style.iconContainerHorizontalArrangement
        ) {
            ODSTextFieldIcon(
                scheme = scheme,
                props = it.toODSTextFieldIconProps(size = props.size),
                onClick = onRightIconClick
            )
        }
    }
}

@Composable
private fun ODSLabelContainer(
    modifier: Modifier = Modifier,
    style: ODSTextFieldStyle,
    props: ODSTextFieldProps,
) {
    val context = LocalContext.current
    if (props.label.isNullOrEmpty()) {
        return
    }
    ODSRow(
        modifier = modifier,
        gap = style.eyebrowGap,
        verticalAlignment = style.eyebrowVerticalAlignment,
        horizontalAlignment = style.eyebrowHorizontalAlignment,
        horizontalArrangement = style.eyebrowHorizontalArrangement
    ) {
        ODSText(
            modifier = if (props.required) {
                Modifier.semantics {
                    this.contentDescription =
                        "${props.label}, ${context.getString(R.string.semantic_input_required)}"
                }
            } else {
                Modifier
            },
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

private fun getVisualTransformation(
    props: ODSTextFieldProps,
    visualTransformation: VisualTransformation,
): VisualTransformation {
    return if (!props.isPasswordField) {
        visualTransformation
    } else {
        if (props.hidePassword) {
            PasswordVisualTransformation()
        } else {
            visualTransformation
        }
    }
}

private fun getPasswordIconModel(props: ODSTextFieldProps, context: Context): ODSIconModel {
    return if (props.hidePassword) {
        ODSIconModel(
            drawableRes = R.drawable.show_password_type_standard,
            contentDescription = context.getString(R.string.semantic_show_password_icon)
        )
    } else {
        ODSIconModel(
            drawableRes = R.drawable.hide_password_type_standard,
            contentDescription = context.getString(R.string.semantic_hide_password_icon)
        )
    }
}
