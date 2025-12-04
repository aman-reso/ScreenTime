package com.telekom.odsystem.atoms.sliderinputfield

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSSliderInputField is a composable that displays an input field for a slider.
 * It allows users to input a numeric value, which can be used to update the slider's position.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming. [ODSTheme]
 * @param props Visual configuration for the component. [ODSSliderInputFieldProps]
 * @param onValueChange Callback triggered when the input value changes. It receives the new value as a String.
 * @param onValueSubmit Callback triggered when the user submits the value, typically by pressing "Done" on the keyboard.
 */
@Composable
fun ODSSliderInputField(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSliderInputFieldProps = ODSSliderInputFieldProps(),
    onValueChange: (String) -> Unit = { },
    onValueSubmit: () -> Unit = {},
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSSliderInputFieldStyle().getStyle(
        scheme = scheme,
        state = if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    val internalInputValueTextStyle = style.inputValueTextStyle?.toTextStyle() ?: TextStyle.Default
    val internalInputValueColor = style.inputValueColor?.getColor() ?: Color.Transparent
    val internalInputCursorColor = style.inputValueCursorColor?.getColor() ?: Color.Transparent
    val textState = remember(props) {
        TextFieldValue(
            text = props.inputValue.orEmpty(),
            selection = TextRange(props.inputValue?.length ?: 0)
        )
    }
    BasicTextField(
        modifier = modifier
            .hoverable(interactionSource),
        interactionSource = interactionSource,
        value = textState,
        textStyle = internalInputValueTextStyle.copy(
            color = internalInputValueColor,
            textAlign = style.inputValueTextAlign ?: TextAlign.Center
        ),
        cursorBrush = SolidColor(internalInputCursorColor),
        singleLine = true,
        enabled = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onValueSubmit()
            }
        ),
        onValueChange = {
            onValueChange(it.text)
        },
        decorationBox = {
            ODSInputFieldContainer(
                style = style,
                props = props,
                innerTextField = it
            )
        }
    )
}

@Composable
private fun ODSInputFieldContainer(
    style: ODSSliderInputFieldStyle,
    props: ODSSliderInputFieldProps,
    innerTextField: @Composable () -> Unit
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .sizeWithinBounds(
                minWidth = style.contentMinWidth ?: MIN_WIDTH.dp,
                minHeight = MIN_HEIGHT.dp
            ),
        gap = style.contentGap,
        padding = style.contentPadding,
        cornerRadius = style.contentBorderRadius,
        border = ODSBorder(
            width = style.contentBorder,
            colorList = style.contentBorderColor
        ),
        background = style.contentBackgroundColor,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalAlignment = style.contentHorizontalAlignment,
        horizontalArrangement = style.contentHorizontalArrangement
    ) {

        if (!props.prefix.isNullOrEmpty()) {
            ODSText(
                text = props.prefix,
                style = style.prefixTextStyle,
                color = style.prefixColor,
                textAlign = style.prefixTextAlign,
                overflow = style.prefixTextOverflow
            )
        }

        ODSInputContainer(
            style = style,
            innerTextField = innerTextField
        )
    }
}

@Composable
private fun ODSInputContainer(
    style: ODSSliderInputFieldStyle,
    innerTextField: @Composable () -> Unit
) {
    ODSRow(
        verticalAlignment = style.inputValueVerticalAlignment,
        horizontalAlignment = style.inputValueHorizontalAlignment,
        horizontalArrangement = style.inputValueHorizontalArrangement
    ) {
        innerTextField()
    }
}
