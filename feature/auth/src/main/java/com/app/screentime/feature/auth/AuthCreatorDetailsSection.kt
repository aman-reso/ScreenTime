package com.app.screentime.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.auth.util.PhotoVerificationUtil
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthCreatorDetailsSection(
    scheme: ODSTheme,
    uiState: AuthUiState,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onVoiceRateChange: (String) -> Unit,
    onAvatarUrlChange: (String) -> Unit,
    onSubmitDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(modifier = modifier.fillMaxWidth(), gap = 12.dp) {
        ODSText(
            text = "Complete your creator profile to appear on Discover and start receiving paid calls.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )

        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Creator Display Name",
                inputText = uiState.name,
                size = ODSTextFieldSize.SMALL,
                leftIcon = ODSIconModel(imageVector = Icons.Filled.Person)
            ),
            onValueChange = onNameChange
        )

        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Bio / About You",
                inputText = uiState.bio,
                size = ODSTextFieldSize.SMALL,
                leftIcon = ODSIconModel(imageVector = Icons.Filled.Description)
            ),
            onValueChange = onBioChange
        )

        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Voice Call Rate (₹ / min)",
                inputText = uiState.voiceRate,
                size = ODSTextFieldSize.SMALL,
                leftIcon = ODSIconModel(imageVector = Icons.Filled.CurrencyRupee)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onVoiceRateChange
        )

        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Profile Photo URL (Female Creator Verification)",
                inputText = uiState.avatarUrl,
                size = ODSTextFieldSize.SMALL,
                leftIcon = ODSIconModel(imageVector = Icons.Filled.CameraAlt)
            ),
            onValueChange = onAvatarUrlChange
        )

        when (val photo = uiState.photoStatus) {
            is PhotoVerificationUtil.VerificationStatus.Verified -> {
                ODSRow(gap = 6.dp, verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = scheme.functionalSuccessStandard.getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                    ODSText(
                        text = photo.message,
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.functionalSuccessStandard
                    )
                }
            }
            is PhotoVerificationUtil.VerificationStatus.Rejected -> {
                ODSText(
                    text = photo.reason,
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.functionalDestructiveStandard
                )
            }
            else -> Unit
        }

        if (!uiState.error.isNullOrEmpty()) {
            ODSText(
                text = uiState.error,
                style = ODSTextStyles.bodySRegular,
                color = scheme.functionalDestructiveStandard
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        ODSButton(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            scheme = scheme,
            props = ODSButtonProps(
                label = if (uiState.isLoading) "Saving Creator Profile..." else "Complete & Join as Creator",
                variant = ODSButtonVariant.PRIMARY,
                size = ODSButtonSize.SMALL
            ),
            onClick = onSubmitDetails
        )
    }
}
