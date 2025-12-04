package com.app.screentime.profile.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.profile.viewmodel.ProfileViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSupportMessageProps
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUsernameBottomSheetContent(
    currentUsername: String?,
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    var usernameText by remember { mutableStateOf(currentUsername ?: "") }
    val uiProps by viewModel.uiProps.collectAsState()

    ODSBottomSheet(
        actionSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSButtonProps(
                        label = stringResource(R.string.save),
                        disabled = (uiProps?.isUpdating
                            ?: false) || usernameText.isBlank() || usernameText == currentUsername
                    ), onClick = {
                        viewModel.updateUsername(usernameText.trim()) {
                            onDismiss()
                        }
                    })
            }
        }, titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = stringResource(R.string.edit_username),
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )

                ODSText(
                    text = stringResource(R.string.username_change_description),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }, showBottomSheet = true, onDismissRequest = onDismiss, contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSTextField(
                    scheme = scheme, props = ODSTextFieldProps(
                        label = stringResource(R.string.username),
                        inputText = usernameText,
                        placeholderText = stringResource(R.string.enter_username),
                        disabled = uiProps?.isUpdating ?: false,
                        supportMessageProps = ODSTextFieldSupportMessageProps(message = uiProps?.error)
                    ), onValueChange = { usernameText = it })
            }
        }, onCloseClicked = onDismiss
    )
}


