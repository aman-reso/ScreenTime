package com.app.screentime.feature.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.UserRole
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthLoginScreen(
    scheme: ODSTheme,
    uiState: AuthUiState,
    onBackClick: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onVerifyAndLogin: () -> Unit,
    onGoogleLogin: () -> Unit,
    onNameChange: (String) -> Unit = {},
    onBioChange: (String) -> Unit = {},
    onVoiceRateChange: (String) -> Unit = {},
    onAvatarUrlChange: (String) -> Unit = {},
    onSubmitCreatorDetails: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val titleText = when (uiState.step) {
        AuthStep.CREATOR_DETAILS -> "Creator Onboarding"
        else -> "Login to Connect"
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
            gap = 12.dp
        ) {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                verticalAlignment = Alignment.CenterVertically,
                gap = 12.dp
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.arrow_right),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onBackClick
                )
                ODSColumn(gap = 2.dp) {
                    ODSText(
                        text = titleText,
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = if (uiState.role == UserRole.MODEL) "✦ Creator Mode" else "👤 User Mode",
                        style = ODSTextStyles.microcopyRegular,
                        color = if (uiState.role == UserRole.MODEL) scheme.basicAccentSecondary else scheme.basicAccent
                    )
                }
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
                    onPhoneChange = onPhoneChange,
                    onOtpChange = onOtpChange,
                    onVerifyAndLogin = onVerifyAndLogin,
                    onGoogleLogin = onGoogleLogin
                )
            }
        }
    }
}
