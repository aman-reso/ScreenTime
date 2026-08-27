package com.app.screentime.feature.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthPhoneOtpSection(
    scheme: ODSTheme,
    uiState: AuthUiState,
    onRoleChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onEditPhone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOtpStep = uiState.step == AuthStep.OTP_INPUT
    val isModel = uiState.role == "model"

    ODSColumn(modifier = modifier.fillMaxWidth(), gap = 12.dp) {
        // Discover-style Segmented Dual Option Tabs
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 16.dp),
            padding = ODSPadding(all = 4.dp)
        ) {
            ODSRow(modifier = Modifier.fillMaxWidth(), gap = 4.dp) {
                listOf(
                    "user" to "👤 User (Caller)",
                    "model" to "✦ Model (Creator)"
                ).forEach { (roleKey, label) ->
                    val isSelected = uiState.role == roleKey
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onRoleChange(roleKey) },
                        background = listOf(ODSColorModel(hexColor = if (isSelected) scheme.basicBackground else scheme.basicBackgroundCard)),
                        cornerRadius = ODSCorners(all = 12.dp),
                        border = if (isSelected) ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        ) else null,
                        padding = ODSPadding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = label,
                            style = if (isSelected) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
                            color = if (isSelected) scheme.basicText else scheme.basicTextRecessive
                        )
                    }
                }
            }
        }

        // Phone Input Field
        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Mobile Number",
                inputText = uiState.phone,
                disabled = isOtpStep
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            onValueChange = onPhoneChange
        )

        // OTP Input Field (Visible on OTP_INPUT step)
        if (isOtpStep) {
            ODSTextField(
                scheme = scheme,
                props = ODSTextFieldProps(
                    label = "Enter OTP",
                    inputText = uiState.otp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                onValueChange = onOtpChange
            )

            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onEditPhone),
                horizontalArrangement = Arrangement.End
            ) {
                ODSText(
                    text = "Edit phone number",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicAccent
                )
            }
        }

        if (!uiState.error.isNullOrEmpty()) {
            ODSText(
                text = uiState.error,
                style = ODSTextStyles.bodySRegular,
                color = scheme.functionalDestructiveStandard
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Action Button: Send OTP vs Verify OTP
        ODSButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            scheme = scheme,
            props = ODSButtonProps(
                label = when {
                    uiState.isLoading -> "Verifying..."
                    isOtpStep && isModel -> "Verify & Continue to Profile"
                    isOtpStep -> "Verify & Enter Connect"
                    else -> "Send OTP Verification"
                },
                variant = ODSButtonVariant.PRIMARY
            ),
            onClick = if (isOtpStep) onVerifyOtp else onSendOtp
        )
    }
}
