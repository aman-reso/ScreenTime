package com.app.screentime.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
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
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthPhoneOtpSection(
    scheme: ODSTheme,
    uiState: AuthUiState,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onVerifyAndLogin: () -> Unit,
    onGoogleLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(modifier = modifier.fillMaxWidth(), gap = 16.dp) {
        // 1. Mobile Number Input Field
        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Mobile Number",
                inputText = uiState.phone,
                size = ODSTextFieldSize.SMALL,
                leftIcon = ODSIconModel(imageVector = Icons.Filled.Phone)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            onValueChange = onPhoneChange
        )

        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "OTP Code",
                inputText = uiState.otp,
                size = ODSTextFieldSize.SMALL,
                leftIcon = ODSIconModel(imageVector = Icons.Filled.Lock)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            onValueChange = onOtpChange
        )

        if (!uiState.error.isNullOrEmpty()) {
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle)),
                cornerRadius = ODSCorners(all = 8.dp),
                padding = ODSPadding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                ODSText(
                    text = uiState.error,
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.functionalDestructiveStandard
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 3. Verify & Continue Button
        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = if (uiState.isLoading) "Verifying..." else "Verify & Continue",
                variant = ODSButtonVariant.PRIMARY,
                size = ODSButtonSize.SMALL,
                disabled = uiState.isLoading
            ),
            onClick = onVerifyAndLogin
        )

        // 4. Divider
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSBox(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            ) {}
            ODSText(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = "or continue with",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
            ODSBox(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            ) {}
        }

        // 5. Google Sign-In Button at Bottom (Secondary Button - 3rd color #bc96ff)
        ODSButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            scheme = scheme,
            props = ODSButtonProps(
                label = "Continue with Google",
                buttonIcon = ODSIconModel(imageVector = Icons.Filled.AccountCircle),
                leftIcon = true,
                variant = ODSButtonVariant.SECONDARY,
                size = ODSButtonSize.SMALL,
                disabled = uiState.isLoading
            ),
            onClick = onGoogleLogin
        )
    }
}
