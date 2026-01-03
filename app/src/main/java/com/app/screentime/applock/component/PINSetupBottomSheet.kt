package com.app.screentime.applock.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.codeinput.ODSCodeInput
import com.telekom.odsystem.molecules.codeinput.ODSCodeInputMode
import com.telekom.odsystem.molecules.codeinput.ODSCodeInputProps
import com.telekom.odsystem.molecules.codeinput.ODSInputItemModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Bottom sheet for setting up PIN for app lock
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PINSetupBottomSheet(
    onDismiss: () -> Unit,
    onPINSet: (String) -> Unit,
    scheme: ODSTheme
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var pinText by remember { mutableStateOf("") }
    var confirmPinText by remember { mutableStateOf("") }
    var isConfirming by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Create input items based on current PIN text
    val currentPinText = if (isConfirming) confirmPinText else pinText
    val inputItems = remember(currentPinText) {
        List(4) { index ->
            ODSInputItemModel(
                inputText = if (index < currentPinText.length) currentPinText[index].toString() else null,
                placeHolder = "",
            )
        }
    }

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = true,
        bottomSheetState = bottomSheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSText(
                text = if (isConfirming) "Confirm PIN" else "Set PIN",
                style = DSTextStyles.bodyL,
                color = scheme.basicText
            )
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4,
                padding = ODSPadding(horizontal = DSVariables.spacingComponent3)
            ) {
                ODSText(
                    text = if (isConfirming) {
                        "Re-enter your PIN to confirm"
                    } else {
                        "Enter a 4-digit PIN to secure your apps"
                    },
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                ODSCodeInput(
                    scheme = scheme,
                    props = ODSCodeInputProps(
                        inputItems = inputItems,
                        disabled = false,
                        readOnly = false,
                        mode = if (errorMessage != null) {
                            ODSCodeInputMode.ERROR
                        } else {
                            ODSCodeInputMode.STANDARD
                        },
                        errorMessage = errorMessage ?: "",
                        masked = true
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            if (isConfirming) {
                                if (confirmPinText.length == 4) {
                                    if (pinText == confirmPinText) {
                                        onPINSet(pinText)
                                    } else {
                                        errorMessage = "PINs do not match"
                                        confirmPinText = ""
                                    }
                                }
                            } else {
                                if (pinText.length == 4) {
                                    isConfirming = true
                                    errorMessage = null
                                }
                            }
                        }
                    ),
                    onValueChange = { newValue ->
                        val digits = newValue.filter { it.isDigit() }.take(4)
                        if (isConfirming) {
                            confirmPinText = digits
                        } else {
                            pinText = digits
                        }
                        errorMessage = null
                    },
                    onCodeFilled = { code ->
                        if (isConfirming) {
                            if (pinText == code) {
                                onPINSet(code)
                            } else {
                                errorMessage = "PINs do not match"
                                confirmPinText = ""
                            }
                        } else {
                            isConfirming = true
                            errorMessage = null
                        }
                    }
                )
            }
        },
        actionSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = if (isConfirming) "Confirm" else "Continue",
                        buttonType = ODSButtonButtonType.STANDARD,
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL,
                        disabled = if (isConfirming) confirmPinText.length != 4 else pinText.length != 4
                    ),
                    onClick = {
                        if (isConfirming) {
                            if (pinText == confirmPinText) {
                                onPINSet(pinText)
                            } else {
                                errorMessage = "PINs do not match"
                                confirmPinText = ""
                            }
                        } else {
                            if (pinText.length == 4) {
                                isConfirming = true
                                errorMessage = null
                            }
                        }
                    }
                )
            }
        }
    )
}

