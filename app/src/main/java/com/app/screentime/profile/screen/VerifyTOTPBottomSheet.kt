package com.app.screentime.profile.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.profile.viewmodel.VerifyTOTPViewModel
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.codeinput.ODSCodeInput
import com.telekom.odsystem.molecules.codeinput.ODSCodeInputProps
import com.telekom.odsystem.molecules.codeinput.ODSInputItemModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyTOTPBottomSheetContent(
    onDismiss: () -> Unit,
    onVerifySuccess: () -> Unit = {},
    username: String? = null,
    viewModel: VerifyTOTPViewModel = hiltViewModel()
) {
    val scheme = neutralScheme
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var otpText by remember { mutableStateOf("") }

    // Reset state when bottom sheet is shown
    LaunchedEffect(Unit) {
        viewModel.resetVerification()
        otpText = ""
    }

    // Handle verification success
    LaunchedEffect(uiState.isValid) {
        if (uiState.isValid && uiState.isVerified) {
            delay(500)
            onVerifySuccess()
        }
    }

    // Create input items based on current OTP text
    val inputItems = remember(otpText) {
        List(6) { index ->
            ODSInputItemModel(
                inputText = if (index < otpText.length) otpText[index].toString() else null,
                placeHolder = "-"
            )
        }
    }

    ODSBottomSheet(
        showBottomSheet = true,
        onDismissRequest = {
            viewModel.resetVerification()
            otpText = ""
            onDismiss()
        },
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = stringResource(R.string.verify_totp),
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )
                ODSText(
                    text = if (username != null) {
                        stringResource(R.string.enter_otp_for_user, username)
                    } else {
                        stringResource(R.string.enter_otp_to_verify)
                    },
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                ODSCodeInput(
                    scheme = scheme,
                    props = ODSCodeInputProps(
                        inputItems = inputItems,
                        disabled = uiState.isVerifying,
                        readOnly = false,
                        mode = if (uiState.error != null && uiState.isVerified) {
                            com.telekom.odsystem.molecules.codeinput.ODSCodeInputMode.ERROR
                        } else {
                            com.telekom.odsystem.molecules.codeinput.ODSCodeInputMode.STANDARD
                        },
                        errorMessage = uiState.error ?: ""
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            if (otpText.length == 6 && username != null) {
                                viewModel.verifyTOTPByUsername(username, otpText)
                            }
                        }
                    ),
                    onValueChange = { newValue ->
                        // Only allow digits and limit to 6 characters
                        otpText = newValue.filter { it.isDigit() }.take(6)
                    },
                    onCodeFilled = { code ->
                        if (username != null) {
                            viewModel.verifyTOTPByUsername(username, code)
                        }
                    }
                )
            }
        },
        actionSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = if (uiState.isVerifying) stringResource(R.string.verifying) else stringResource(R.string.verify),
                        disabled = uiState.isVerifying || otpText.length != 6 || username == null
                    ),
                    onClick = {
                        username?.let {
                            viewModel.verifyTOTPByUsername(it, otpText)
                        }
                    }
                )
            }
        },
        onCloseClicked = {
            viewModel.resetVerification()
            otpText = ""
            onDismiss()
        }
    )
}
