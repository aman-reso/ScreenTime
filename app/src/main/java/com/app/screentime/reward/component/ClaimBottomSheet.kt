package com.app.screentime.reward.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Claim Bottom Sheet Component
 * Displays a form with Name, Phone Number, and Address fields
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    onProceedNext: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    scheme: ODSTheme
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(
            showHandle = true
        ),
        showBottomSheet = showBottomSheet,
        bottomSheetState = bottomSheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSText(
                text = stringResource(R.string.claim_reward),
                style = DSTextStyles.bodyL,
                color = scheme.basicText
            )
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                // Name field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = stringResource(R.string.name),
                        inputText = name,
                        size = ODSTextFieldSize.SMALL
                    ),
                    onValueChange = { name = it }
                )

                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = stringResource(R.string.phone_number),
                        inputText = phoneNumber,
                        size = ODSTextFieldSize.SMALL
                    ),
                    onValueChange = { phoneNumber = it }
                )

                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = stringResource(R.string.pincode),
                        inputText = postalCode,
                        size = ODSTextFieldSize.SMALL
                    ),
                    onValueChange = { postalCode = it }
                )

                // Address field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = stringResource(R.string.address),
                        inputText = address,
                        size = ODSTextFieldSize.SMALL
                    ),
                    onValueChange = { address = it }
                )
            }
        },
        actionSlot = {
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = stringResource(R.string.proceed_next),
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL,
                    disabled = name.isBlank() || phoneNumber.isBlank() || address.isBlank()
                ),
                onClick = {
                    onProceedNext(name, phoneNumber, postalCode, address)
                    onDismiss()
                }
            )
        }
    )
}





















