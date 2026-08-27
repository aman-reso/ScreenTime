package com.app.screentime.feature.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthGateScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onAuthSuccess: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isLoginScreenVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onAuthSuccess()
    }

    if (isLoginScreenVisible) {
        AuthLoginScreen(
            modifier = modifier.fillMaxSize(),
            scheme = scheme,
            uiState = uiState,
            onBackClick = {
                when (uiState.step) {
                    AuthStep.CREATOR_DETAILS -> viewModel.resetToPhoneInput()
                    AuthStep.OTP_INPUT -> viewModel.resetToPhoneInput()
                    AuthStep.PHONE_INPUT -> isLoginScreenVisible = false
                }
            },
            onRoleChange = { viewModel.onRoleChanged(it) },
            onPhoneChange = { viewModel.onPhoneChanged(it) },
            onOtpChange = { viewModel.onOtpChanged(it) },
            onSendOtp = { viewModel.sendOtp() },
            onVerifyOtp = { viewModel.verifyOtp() },
            onEditPhone = { viewModel.resetToPhoneInput() },
            onNameChange = { viewModel.onNameChanged(it) },
            onBioChange = { viewModel.onBioChanged(it) },
            onVoiceRateChange = { viewModel.onVoiceRateChanged(it) },
            onAvatarUrlChange = { viewModel.onAvatarUrlChanged(it) },
            onSubmitCreatorDetails = { viewModel.submitCreatorDetails() }
        )
    } else {
        AuthLandingScreen(
            modifier = modifier.fillMaxSize(),
            scheme = scheme,
            onGetStartedClick = { isLoginScreenVisible = true },
            onGuestClick = { viewModel.loginAsGuest() }
        )
    }
}
