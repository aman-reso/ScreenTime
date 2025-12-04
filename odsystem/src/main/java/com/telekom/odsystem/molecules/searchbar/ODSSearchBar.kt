package com.telekom.odsystem.molecules.searchbar

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Composable for rendering an ODS Search Bar.
 *
 * Provides a text input with optional actions for submission, value change, focus change, and an end-button click.
 * Supports customization via theming, keyboard options, and external state handling through [ODSSearchBarProps].
 *
 * @param modifier Modifier for styling and layout.
 * @param scheme Color scheme used for theming. Defaults to [neutralScheme].
 * @param props [ODSSearchBarProps] defining the visual and behavioral configuration.
 * @param keyboardOptions Configuration for the software keyboard (e.g., type and IME action).
 * @param keyboardActions Behavior triggered by keyboard IME actions.
 * @param visualTransformation Transformation to apply to the input text (e.g., for password masking).
 * @param onValueChange Callback when the input value changes.
 * @param onButtonClick Callback for the end-button (e.g., clear or search) click.
 */
@Composable
fun ODSSearchBar(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSearchBarProps = ODSSearchBarProps(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit = {},
    onButtonClick: (String) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }
    val style = ODSSearchBarStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )

    BasicTextField(
        value = props.input.orEmpty(),
        onValueChange = onValueChange,
        modifier = modifier,
        cursorBrush = SolidColor(
            style.inputValueColor?.getColor() ?: scheme.basicAccent.getColor()
        ),
        textStyle = style.inputValueStyle?.toTextStyle()
            ?.copy(
                color = (style.inputValueColor ?: scheme.basicText).getColor(),
                textAlign = style.inputValueTextAlign ?: TextAlign.Unspecified,
            )
            ?: TextStyle.Default,
        singleLine = true,
        enabled = !props.disabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            ODSSearchInputFieldContainer(
                scheme = scheme,
                style = style,
                props = props,
                onButtonClick = onButtonClick,
                interactionSource = interactionSource,
                innerTextField = innerTextField
            )
        }
    )
}

@Composable
private fun ODSSearchInputFieldContainer(
    scheme: ODSTheme,
    style: ODSSearchBarStyle,
    props: ODSSearchBarProps,
    interactionSource: MutableInteractionSource,
    onButtonClick: (String) -> Unit,
    innerTextField: @Composable () -> Unit
) {
    ODSRow(
        gap = style.gap,
        padding = style.padding,
        cornerRadius = style.cornerRadius,
        border = ODSBorder(
            width = style.border,
            colorList = style.borderColor
        ),
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        background = style.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Added to restrict height of input field
            .hoverable(interactionSource = interactionSource)
    ) {
        ODSContentContainer(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            style = style,
            props = props,
            innerTextField = innerTextField
        )
        props.buttonProps?.let {
            ODSButton(
                modifier = Modifier,
                scheme = scheme,
                props = it.toODSButtonProps(props.disabled),
                onClick = { onButtonClick(props.input.orEmpty()) }
            )
        }
    }
}

@Composable
private fun ODSContentContainer(
    modifier: Modifier,
    style: ODSSearchBarStyle,
    props: ODSSearchBarProps,
    innerTextField: @Composable () -> Unit
) {
    ODSRow(
        modifier = modifier,
        clipContent = style.contentClipContent != false,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalArrangement = style.contentHorizontalArrangement
    ) {
        ODSBox {
            if (!props.placeholder.isNullOrEmpty() && props.input.isNullOrEmpty()) {
                ODSText(
                    text = props.placeholder,
                    style = style.placeholderStyle,
                    color = style.placeholderColor,
                    textAlign = style.placeholderTextAlign,
                    overflow = style.placeholderOverflow,
                    maxLines = style.placeholderMaxLines
                )
            }
            innerTextField()
        }
    }
}
