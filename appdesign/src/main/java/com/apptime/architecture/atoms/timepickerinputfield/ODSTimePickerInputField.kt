package com.telekom.odsystem.atoms.timepickerinputfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSActions

import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.utils.buildLabelAnnotatedString

/**
 * ODSTimePickerInputField is a composable that allows users to input a time.
 *
 * @param modifier Modifier for this component.
 * @param scheme Color scheme.
 * @param props Visual configuration.
 * @param onValueChange Callback when the input value changes.
 * @param onValueSubmit Callback when the "Done" action on the keyboard is pressed.
 * @param onClockIconClick Callback when the clock icon is clicked.
 */
@Suppress("LongMethod")
@Composable
fun ODSTimePickerInputField(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTimePickerInputFieldProps = ODSTimePickerInputFieldProps(),
    onValueChange: (String) -> Unit = {},
    onValueSubmit: () -> Unit = {},
    onClockIconClick: () -> Unit = {},
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        pressed && !props.disabled -> ODSActions.PRESSED
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }
    val isFocused by interactionSource.collectIsFocusedAsState()
    // Update props.isFocused before getStyle runs; not in SideEffect to ensure it's updated during composition, not after
    props.isFocused = isFocused
    val style = ODSTimePickerInputFieldStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )

    val textState = remember(props.inputText) {
        val formattedText = formatText(input = props.inputText.orEmpty())
        TextFieldValue(
            text = formattedText,
            selection = TextRange(formattedText.length)
        )
    }
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        value = textState,
        onValueChange = {
            onValueChange(removeTrailingSeparator(input = it.text, prevInput = textState.text))
        },
        cursorBrush = SolidColor(
            style.inputValueCursorColor?.getColor() ?: scheme.basicAccent.getColor()
        ),
        textStyle = style.dateInputStyle?.toTextStyle()
            ?.copy(
                color = (style.dateInputColor ?: scheme.basicText).getColor(),
                textAlign = style.dateInputTextAlign ?: TextAlign.Unspecified,
            )
            ?: TextStyle.Default,
        singleLine = true,
        enabled = props.disabled.not(),
        readOnly = props.readOnly,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onValueSubmit()
            }
        ),
        decorationBox = {
            ODSColumn(
                gap = style.gap,
                verticalArrangement = style.verticalArrangement,
                verticalAlignment = style.verticalAlignment,
                horizontalAlignment = style.horizontalAlignment,
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSInputFieldContainer(
                    scheme = scheme,
                    style = style,
                    props = props,
                    interactionSource = interactionSource,
                    onClockIconClick = onClockIconClick,
                    innerTextField = it
                )
                val supportMessageProps = props.supportMessageProps
                if (props.mode != ODSTimePickerInputFieldMode.STANDARD && supportMessageProps != null) {
                    ODSSupportMessageContainer(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        style = style,
                        supportMessageProps = supportMessageProps.toODSSupportMessageProps(
                            mode = props.mode,
                            disabled = props.disabled
                        )
                    )
                }
            }
        }
    )
}

@Composable
private fun ODSInputFieldContainer(
    scheme: ODSTheme,
    style: ODSTimePickerInputFieldStyle,
    props: ODSTimePickerInputFieldProps,
    onClockIconClick: () -> Unit,
    interactionSource: MutableInteractionSource,
    innerTextField: @Composable () -> Unit,
) {
    ODSRow(
        clipContent = style.inputFieldClipContent != false,
        cornerRadius = style.inputFieldCornerRadius,
        border = ODSBorder(
            width = style.inputFieldBorder,
            colorList = style.inputFieldBorderColor
        ),
        horizontalArrangement = style.inputFieldHorizontalArrangement,
        horizontalAlignment = style.inputFieldHorizontalAlignment,
        verticalAlignment = style.inputFieldVerticalAlignment,
        background = style.inputFieldBackground,
        padding = style.inputFieldPadding,
        modifier = Modifier
            .sizeWithinBounds(minHeight = style.inputFieldMinHeight ?: MIN_HEIGHT.dp)
            .height(IntrinsicSize.Min) // Added to restrict height of input field
    ) {
        ODSContentContainer(
            modifier = Modifier
                .weight(1f)
                .hoverable(interactionSource = interactionSource)
                .fillMaxHeight(), // Added to fill height of input field
            style = style,
            props = props,
            innerTextField = innerTextField
        )
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                disabled = props.disabled || props.readOnly,
                buttonIcon = ODSIconModel(
                    drawableRes = R.drawable.clock_type_standard,
                    contentDescription = stringResource(R.string.semantics_clock_icon)
                ),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                size = if (props.size == ODSTimePickerInputFieldSize.LARGE) ODSButtonSize.LARGE else ODSButtonSize.SMALL,
                variant = ODSButtonVariant.GHOST
            ),
            onClick = onClockIconClick
        )
    }
}

@Composable
private fun ODSContentContainer(
    modifier: Modifier,
    style: ODSTimePickerInputFieldStyle,
    props: ODSTimePickerInputFieldProps,
    innerTextField: @Composable () -> Unit,
) {
    ODSBox(
        modifier = modifier,
        contentAlignment = style.contentContainerAlignment
    ) {
        ODSColumn(
            gap = style.contentGap,
            padding = style.contentPadding,
            verticalArrangement = style.contentVerticalArrangement,
            verticalAlignment = style.contentVerticalAlignment,
            horizontalAlignment = style.contentHorizontalAlignment,
        ) {
            AnimatedVisibility(
                visible = !props.inputText.isNullOrEmpty() || props.isFocused,
                enter = fadeIn(animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION)) + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight - fullHeight / 2 },
                    animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION)
                ),
                exit = ExitTransition.None
            ) {
                ODSEyebrowContainer(style = style, props = props)
            }
            ODSInputValueContainer(
                style = style,
                props = props,
                innerTextField = innerTextField
            )
        }
        if (props.inputText.isNullOrEmpty() && !props.isFocused) {
            // This is custom addition for adding horizontal padding
            val padding = style.contentPadding?.getPaddingValues()
            ODSEyebrowContainer(
                modifier = padding?.let { Modifier.padding(it) } ?: Modifier,
                style = style,
                props = props
            )
        }
    }
}

@Composable
private fun ODSEyebrowContainer(
    modifier: Modifier = Modifier,
    style: ODSTimePickerInputFieldStyle,
    props: ODSTimePickerInputFieldProps,
) {
    val context = LocalContext.current
    if (props.label.isNullOrEmpty()) {
        return
    }
    ODSRow(
        modifier = modifier
            .fillMaxWidth(),
        gap = style.eyebrowGap,
        horizontalArrangement = style.eyebrowHorizontalArrangement,
        horizontalAlignment = style.eyebrowHorizontalAlignment,
        verticalAlignment = style.eyebrowVerticalAlignment
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

@Suppress("LongMethod")
@Composable
private fun ODSInputValueContainer(
    style: ODSTimePickerInputFieldStyle,
    props: ODSTimePickerInputFieldProps,
    innerTextField: @Composable () -> Unit,
) {
    ODSBox {
        ODSRow(
            gap = style.inputValueGap,
            horizontalAlignment = style.inputValueHorizontalAlignment,
            verticalAlignment = style.inputValueVerticalAlignment,
            horizontalArrangement = style.inputValueHorizontalArrangement
        ) {
            if (!props.placeholderText.isNullOrEmpty() && props.inputText.isNullOrEmpty() && props.isFocused) {
                ODSText(
                    text = props.placeholderText,
                    style = style.placeholderStyle,
                    color = style.placeholderColor,
                    textAlign = style.placeholderTextAlign,
                    overflow = style.placeholderOverflow,
                    maxLines = style.placeholderMaxLines
                )
            }
        }
        innerTextField()
    }
}

@Composable
private fun ODSSupportMessageContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSTimePickerInputFieldStyle,
    supportMessageProps: ODSSupportMessageProps,
) {
    ODSRow(
        modifier = modifier,
        padding = style.supportTextPadding,
        horizontalArrangement = style.supportTextHorizontalArrangement,
        horizontalAlignment = style.supportTextHorizontalAlignment,
        verticalAlignment = style.supportTextVerticalAlignment
    ) {
        ODSSupportMessage(
            scheme = scheme,
            props = supportMessageProps
        )
    }
}

private fun formatText(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }.take(MAX_STRING_LENGTH)
    val formattedText = StringBuilder(digitsOnly)
    if (formattedText.length >= SEPARATOR_POSITION) {
        formattedText.insert(SEPARATOR_POSITION, ":")
    }
    return formattedText.toString()
}

private fun removeTrailingSeparator(input: String, prevInput: String): String {
    val separatorSymbol = ":".first()
    if (input.length < prevInput.length && prevInput.last() == separatorSymbol) {
        return input.dropLast(1)
    }
    return input
}

private const val MAX_STRING_LENGTH = 4
private const val SEPARATOR_POSITION = 2
