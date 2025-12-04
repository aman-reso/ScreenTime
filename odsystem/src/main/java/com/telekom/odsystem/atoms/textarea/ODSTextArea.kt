package com.telekom.odsystem.atoms.textarea

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.utils.buildLabelAnnotatedString

/**
 * ODSTextArea composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param onValueChange Callback triggered when action occurs.
 * @param props Visual configuration for the component.
 * @param keyboardOptions Parameter for customization.
 * @param keyboardActions Parameter for customization.
 * @param visualTransformation Parameter for customization of the visual transformation of the input text.
 */
@Composable
fun ODSTextArea(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onValueChange: (String) -> Unit = {},
    props: ODSTextAreaProps = ODSTextAreaProps(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()
    // Update props.isFocused before getStyle runs; not in SideEffect to ensure it's updated during composition, not after
    props.isFocused = isFocused
    val style = ODSTextAreaStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (isHovered && !props.disabled && !props.readOnly) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    val internalInputValueTextStyle = style.inputValueStyle?.toTextStyle() ?: TextStyle()
    val internalInputValueColor = style.inputValueColor?.getColor() ?: Color.Transparent
    val internalInputCursorColor = style.inputCursorColor?.getColor() ?: Color.Transparent
    val maxCharacterReached = props.counterText == props.inputText?.length
    val maxCharacterText = if (maxCharacterReached) {
        context.getString(R.string.semantic_max_character_reached)
    } else {
        ""
    }
    BasicTextField(
        modifier = modifier
            .hoverable(interactionSource)
            .moveFocusOnTab(focusManager = focusManager)
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
        visualTransformation = visualTransformation,
        onValueChange = {
            onValueChange(checkForTextFieldCounter(props, it))
        },
        interactionSource = interactionSource,
        readOnly = props.readOnly,
        decorationBox = {
            ODSTextAreaContainer(
                style = style,
                props = props,
                scheme = scheme,
                innerTextField = it
            )
        }
    )
}

@Composable
fun ODSTextAreaContainer(
    style: ODSTextAreaStyle,
    props: ODSTextAreaProps,
    scheme: ODSTheme,
    innerTextField: @Composable () -> Unit,
) {
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
        ODSInputFieldContainer(
            style = style,
            props = props,
            innerTextField = innerTextField
        )
        if (props.mode != ODSTextAreaMode.STANDARD || (props.counterText ?: 0) > 0) {
            ODSSupportTextContainer(
                scheme = scheme, style = style, props = props
            )
        }
    }
}

@Composable
private fun ODSInputFieldContainer(
    style: ODSTextAreaStyle,
    props: ODSTextAreaProps,
    innerTextField: @Composable () -> Unit,
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .sizeWithinBounds(
                minWidth = MIN_WIDTH.dp,
                minHeight = style.inputFieldMinHeight ?: MIN_WIDTH.dp
            ),
        gap = style.inputFieldGap,
        height = style.inputFieldMinHeight,
        padding = style.inputFieldPadding,
        cornerRadius = style.inputFieldCornerRadius,
        background = style.inputFieldBackground,
        border = ODSBorder(
            width = style.inputFieldBorder, colorList = style.inputFieldBorderColor
        ),
        verticalAlignment = style.inputFieldVerticalAlignment,
        horizontalAlignment = style.inputFieldHorizontalAlignment,
        horizontalArrangement = style.inputFieldHorizontalArrangement
    ) {
        ODSContentContainer(
            modifier = Modifier.weight(1f),
            style = style,
            props = props,
            innerTextField = innerTextField
        )
    }
}

@Composable
fun ODSContentContainer(
    modifier: Modifier,
    style: ODSTextAreaStyle,
    props: ODSTextAreaProps,
    innerTextField: @Composable () -> Unit,
) {

    val labelAnimationDuration = DEFAULT_ANIMATION_DURATION

    ODSBox(modifier = modifier, contentAlignment = style.contentContainerAlignment) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.contentGap,
            verticalAlignment = style.contentVerticalAlignment,
            horizontalAlignment = style.contentHorizontalAlignment,
            verticalArrangement = style.contentVerticalArrangement,
            clipContent = style.contentClipContent != false
        ) {
            AnimatedVisibility(
                visible = props.filled,
                enter = fadeIn(animationSpec = tween(durationMillis = labelAnimationDuration)) + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight - fullHeight / 2 },
                    animationSpec = tween(durationMillis = labelAnimationDuration)
                ),
                exit = ExitTransition.None
            ) {
                ODSLabelContainer(style = style, props = props)
            }
            innerTextField()
        }
        if (!props.filled) ODSLabelContainer(style = style, props = props)
    }
}

@Composable
private fun ODSLabelContainer(
    style: ODSTextAreaStyle,
    props: ODSTextAreaProps,
) {
    val context = LocalContext.current
    if (props.labelText.isNullOrEmpty()) {
        return
    }
    ODSRow(
        gap = style.eyebrowGap,
        verticalAlignment = style.eyebrowVerticalAlignment,
        horizontalAlignment = style.eyebrowHorizontalAlignment,
        horizontalArrangement = style.eyebrowHorizontalArrangement
    ) {
        ODSText(
            modifier = if (props.required) {
                Modifier.semantics {
                    this.contentDescription =
                        "${props.labelText}, ${context.getString(R.string.semantic_input_required)}"
                }
            } else {
                Modifier
            },
            text = buildLabelAnnotatedString(
                label = props.labelText,
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
private fun ODSSupportTextContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    style: ODSTextAreaStyle,
    props: ODSTextAreaProps,
) {
    ODSRow(
        modifier = modifier,
        padding = style.supportTextPadding,
        verticalAlignment = style.supportTextVerticalAlignment,
        horizontalAlignment = style.supportTextHorizontalAlignment,
        horizontalArrangement = style.supportTextHorizontalArrangement
    ) {
        if (props.mode != ODSTextAreaMode.STANDARD) {
            props.supportMessageProps?.let {
                ODSSupportMessage(
                    modifier = Modifier.weight(1f),
                    props = it.toODSSupportMessageProps(
                        mode = props.mode,
                        disabled = props.disabled
                    ),
                    scheme = scheme
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
        if ((props.counterText ?: 0) > 0) {
            ODSText(
                text = "${props.inputText?.length ?: 0}/${props.counterText}",
                style = style.counterStyle,
                color = style.counterColor,
                textAlign = style.counterTextAlign
            )
        }
    }
}

private fun checkForTextFieldCounter(
    props: ODSTextAreaProps,
    inputText: String,
): String {
    if ((props.counterText ?: 0) > 0) {
        return if (inputText.length > (props.counterText ?: 0)) {
            inputText.substring(0, props.counterText ?: 0)
        } else {
            inputText
        }
    }

    return inputText
}

fun Modifier.moveFocusOnTab(focusManager: FocusManager) =
    onPreviewKeyEvent {
        if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
            focusManager.moveFocus(
                if (it.isShiftPressed) FocusDirection.Previous else FocusDirection.Next
            )
            true
        } else {
            false
        }
    }
