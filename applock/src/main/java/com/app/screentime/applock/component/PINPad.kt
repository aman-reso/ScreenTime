package com.app.screentime.applock.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.codeinput.ODSCodeInput
import com.telekom.odsystem.molecules.codeinput.ODSCodeInputMode
import com.telekom.odsystem.molecules.codeinput.ODSCodeInputProps
import com.telekom.odsystem.molecules.codeinput.ODSInputItemModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Custom PIN pad component with buttons for digits 0-9 and X (delete)
 */
@Composable
fun PINPad(
    modifier: Modifier = Modifier,
    pin: String = "",
    onDigitClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    errorMessage: String? = null,
    onPinVerified: () -> Unit = {}
) {
    val inputItems = remember(pin) {
        List(4) { index ->
            ODSInputItemModel(
                inputText = if (index < pin.length) pin[index].toString() else null,
                placeHolder = "-"
            )
        }
    }

    ODSColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = DSVariables.spacingComponent3
    ) {
        ODSCodeInput(
            modifier = Modifier,
            scheme = scheme,
            props = ODSCodeInputProps(
                inputItems = inputItems,
                disabled = false,
                readOnly = true, // Read-only, only allow pad buttons
                mode = if (errorMessage != null) ODSCodeInputMode.ERROR else ODSCodeInputMode.STANDARD,
                errorMessage = errorMessage ?: ""
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(),
            onValueChange = { }
        )

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent7))
        ODSColumn(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = DSVariables.spacingComponent3
        ) {
            ODSRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                gap = DSVariables.spacingComponent4
            ) {
                PINPadButton(
                    text = "1",
                    scheme = scheme,
                    onClick = { onDigitClick("1") }
                )
                PINPadButton(
                    text = "2",
                    scheme = scheme,
                    onClick = { onDigitClick("2") }
                )
                PINPadButton(
                    text = "3",
                    scheme = scheme,
                    onClick = { onDigitClick("3") }
                )
            }

            ODSRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                gap = DSVariables.spacingComponent4
            ) {
                PINPadButton(
                    text = "4",
                    scheme = scheme,
                    onClick = { onDigitClick("4") }
                )
                PINPadButton(
                    text = "5",
                    scheme = scheme,
                    onClick = { onDigitClick("5") }
                )
                PINPadButton(
                    text = "6",
                    scheme = scheme,
                    onClick = { onDigitClick("6") }
                )
            }

            ODSRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                gap = DSVariables.spacingComponent4
            ) {
                PINPadButton(
                    text = "7",
                    scheme = scheme,
                    onClick = { onDigitClick("7") }
                )
                PINPadButton(
                    text = "8",
                    scheme = scheme,
                    onClick = { onDigitClick("8") }
                )
                PINPadButton(
                    text = "9",
                    scheme = scheme,
                    onClick = { onDigitClick("9") }
                )
            }

            // Row 4: X (delete), 0
            ODSRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                gap = DSVariables.spacingComponent4
            ) {
                PINPadButton(
                    text = "0",
                    scheme = scheme,
                    onClick = { onDigitClick("0") }
                )

                PINPadButton(
                    text = "X",
                    scheme = scheme,
                    onClick = onDeleteClick
                )

                PINPadButton(
                    text = "OK",
                    scheme = scheme,
                    onClick = onPinVerified
                )
            }
        }
    }
}

@Composable
private fun PINPadButton(
    text: String,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    ODSBox(
        modifier = Modifier
            .size(80.dp) // Small circular button size
            .clip(CircleShape)
            .clickable(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                    onClick()
                }
            ),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 30.dp),
        contentAlignment = Alignment.Center,
        padding = ODSPadding(all = 0.dp)
    ) {
        ODSText(
            text = text,
            style = DSTextStyles.titleM,
            color = scheme.basicText
        )
    }
}

