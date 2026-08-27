package com.app.screentime.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthLoginScreen(
    scheme: ODSTheme,
    uiState: AuthUiState,
    onBackClick: () -> Unit,
    onRoleChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onEditPhone: () -> Unit,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onVoiceRateChange: (String) -> Unit,
    onAvatarUrlChange: (String) -> Unit,
    onSubmitCreatorDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleText = when (uiState.step) {
        AuthStep.PHONE_INPUT -> "Direct Login"
        AuthStep.OTP_INPUT -> "Enter OTP"
        AuthStep.CREATOR_DETAILS -> "Creator Onboarding"
    }

    ODSBox(modifier = modifier.fillMaxSize(), background = listOf(ODSColorModel(hexColor = scheme.basicBackground))) {
        ODSColumn(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).statusBarsPadding().navigationBarsPadding().padding(horizontal = 20.dp),
            gap = 12.dp
        ) {
            ODSRow(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = scheme.basicText.getColor())
                }
                PompiereTitle(text = titleText, scheme = scheme, style = ODSTextStyles.pompiereTitleL)
            }

            if (uiState.step == AuthStep.CREATOR_DETAILS) {
                AuthCreatorDetailsSection(
                    scheme = scheme,
                    uiState = uiState,
                    onNameChange = onNameChange,
                    onBioChange = onBioChange,
                    onVoiceRateChange = onVoiceRateChange,
                    onAvatarUrlChange = onAvatarUrlChange,
                    onSubmitDetails = onSubmitCreatorDetails
                )
            } else {
                AuthPhoneOtpSection(
                    scheme = scheme,
                    uiState = uiState,
                    onRoleChange = onRoleChange,
                    onPhoneChange = onPhoneChange,
                    onOtpChange = onOtpChange,
                    onSendOtp = onSendOtp,
                    onVerifyOtp = onVerifyOtp,
                    onEditPhone = onEditPhone
                )
            }
        }
    }
}
