package com.app.screentime.applock.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldMode
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSupportMessageProps
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Bottom sheet for PIN entry using ODS system
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PINBottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    isSettingPin: Boolean = false,
    scheme: ODSTheme = neutralScheme,
    onDismiss: () -> Unit,
    onPinEntered: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(showBottomSheet) {
        if (!showBottomSheet) {
            pin = ""
            confirmPin = ""
            errorMessage = null
        }
    }

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = showBottomSheet,
        bottomSheetState = sheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        snackbarHostState = null,
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = if (isSettingPin) "Set PIN" else "Enter PIN",
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )
                ODSText(
                    text = if (isSettingPin) {
                        "Create a PIN to secure your apps"
                    } else {
                        "Enter your PIN to lock this app"
                    },
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                // PIN Input
                ODSTextField(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        inputText = pin,
                        size = ODSTextFieldSize.SMALL,
                        label = "PIN",
                        placeholderText = "Enter PIN",
                        mode = if (errorMessage != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                        supportMessageProps = errorMessage?.let {
                            ODSTextFieldSupportMessageProps(message = it)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        pin = it
                        errorMessage = null
                    }
                )

                // Confirm PIN (only when setting PIN)
                if (isSettingPin) {
                    ODSTextField(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSTextFieldProps(
                            size = ODSTextFieldSize.SMALL,
                            inputText = confirmPin,
                            label = "Confirm PIN",
                            placeholderText = "Confirm PIN",
                            mode = if (errorMessage != null) ODSTextFieldMode.ERROR else ODSTextFieldMode.STANDARD,
                            supportMessageProps = errorMessage?.let {
                                ODSTextFieldSupportMessageProps(message = it)
                            }
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = {
                            confirmPin = it
                            errorMessage = null
                        }
                    )
                }
            }
        },
        actionSlot = {
            ODSButton(
                modifier = Modifier,
                scheme = scheme,
                props = ODSButtonProps(
                    label = if (isSettingPin) "Set PIN" else "Confirm",
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL
                ),
                onClick = {
                    when {
                        pin.isEmpty() -> {
                            errorMessage = "PIN cannot be empty"
                        }

                        isSettingPin && pin.length < 4 -> {
                            errorMessage = "PIN must be at least 4 digits"
                        }

                        isSettingPin && pin != confirmPin -> {
                            errorMessage = "PINs do not match"
                        }

                        else -> {
                            onPinEntered(pin)
                            pin = ""
                            confirmPin = ""
                        }
                    }
                }
            )
        }
    )
}
