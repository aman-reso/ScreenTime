package com.telekom.odsystem.molecules.codeinput

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.password
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.inputitem.ODSInputItem
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Input field for entering a fixed length verification code.
 *
 * Displays a row of input items and handles focus and value changes.
 *
 * @param modifier Modifier applied to the container.
 * @param scheme Color scheme used for styling.
 * @param props holding the current properties.
 * @param onCodeFilled Callback invoked when all items are filled.
 * @param onValueChange Called with the updated code value.
 * @param keyboardOptions Options used for the underlying text field.
 * @param keyboardActions Keyboard actions handling IME events.
 */
@Suppress("LongMethod")
@Composable
fun ODSCodeInput(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCodeInputProps = ODSCodeInputProps(),
    onCodeFilled: ((String) -> Unit) = {},
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val style = ODSCodeInputStyle().getStyle()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val inputItems = props.inputItems
    val inputItemsText = props.inputItems.joinToString("") { it.inputText.orEmpty() }
    val placeHolderText = inputItems.joinToString("") { it.placeHolder.orEmpty() }

    BasicTextField(
        modifier = modifier.semantics {
            if (props.masked) {
                password()
            }
            if (placeHolderText.isNotEmpty() && inputItemsText.isEmpty()) {
                this.contentDescription = placeHolderText
            }
        },
        value = TextFieldValue(
            text = inputItemsText,
            selection = TextRange(inputItemsText.length)
        ),
        readOnly = props.readOnly,
        enabled = !props.disabled,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        interactionSource = interactionSource,
        singleLine = true,
        onValueChange = {
            if (it.text.length <= (inputItems.size)) {
                onValueChange(it.text)
            }
            if (it.text.length == props.inputItems.size) {
                onCodeFilled(it.text)
            }
        }
    ) {
        ODSColumn(
            gap = style.gap,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
            verticalArrangement = style.verticalArrangement
        ) {
            ODSRow(
                gap = style.inputContainerGap,
                horizontalAlignment = style.inputContainerHorizontalAlignment,
                horizontalArrangement = style.inputContainerHorizontalArrangement,
                verticalAlignment = style.inputContainerVerticalAlignment
            ) {
                repeat(props.inputItems.size) { i ->
                    val inputItemIsFocused =
                        if (inputItemsText.length == props.inputItems.size && isFocused) {
                            i == props.inputItems.lastIndex
                        } else {
                            inputItemsText.length == i && isFocused
                        }
                    ODSInputItem(
                        modifier = Modifier.clearAndSetSemantics {},
                        scheme = scheme,
                        props = props.inputItems[i].toODSInputItemProps(props)
                            .copy(isFocused = inputItemIsFocused),
                    )
                }
            }
            if (props.mode == ODSCodeInputMode.ERROR && !props.disabled && !props.readOnly) {
                ODSSupportMessage(
                    scheme = scheme,
                    props = ODSSupportMessageProps(
                        helperText = props.errorMessage,
                        mode = ODSSupportMessageMode.ERROR,
                    )
                )
            }
        }
    }
}
