package com.app.screentime.feature.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthGateScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onAuthSuccess: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.resetAuthFlow()
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onAuthSuccess()
        }
    }

    val webClientId = remember(context) {
        val nativeId = try {
            com.app.screentime.core.network.security.NativeSecurityVault.getGoogleWebClientId()
        } catch (_: Throwable) {
            ""
        }
        if (nativeId.isNotBlank()) {
            nativeId
        } else {
            try {
                val defaultResId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
                val customResId = context.resources.getIdentifier("google_web_client_id", "string", context.packageName)
                val raw = when {
                    defaultResId != 0 -> context.getString(defaultResId)
                    customResId != 0 -> context.getString(customResId)
                    else -> ""
                }
                // A valid Google OAuth Web Client ID has format "<project_number>-<hash>.apps.googleusercontent.com"
                val parts = raw.substringBefore(".apps.googleusercontent.com").split("-")
                if (raw.endsWith(".apps.googleusercontent.com") && parts.size >= 2 && parts[1].length >= 10 && !raw.startsWith("YOUR_")) {
                    raw
                } else {
                    ""
                }
            } catch (_: Exception) {
                ""
            }
        }
    }

    val googleSignInClient = remember(context, webClientId) {
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
        if (webClientId.isNotBlank()) {
            builder.requestIdToken(webClientId)
        }
        GoogleSignIn.getClient(context, builder.build())
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        viewModel.handleGoogleSignInResult(task)
    }

    fun launchGoogleSignIn() {
        try {
            // Sign out any previously cached local session to ensure account picker shows
            googleSignInClient.signOut().addOnCompleteListener {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        } catch (e: Exception) {
            viewModel.showGoogleSignInError(e.localizedMessage ?: "Failed to launch Google Sign-In")
        }
    }

    when (uiState.step) {
        AuthStep.PHONE_INPUT, AuthStep.OTP_INPUT -> {
            AuthPhoneSignInScreen(
                modifier = modifier.fillMaxSize(),
                scheme = scheme,
                uiState = uiState,
                onPhoneChange = { viewModel.onPhoneChange(it) },
                onCountryCodeChange = { code, flag -> viewModel.onCountryCodeChange(code, flag) },
                onOtpChange = { viewModel.onOtpChange(it) },
                onContinueClick = { phone ->
                    if (uiState.step == AuthStep.OTP_INPUT) {
                        viewModel.loginWithPhone(phone)
                    } else {
                        viewModel.requestPhoneOtp(phone)
                    }
                },
                onBackClick = {
                    if (uiState.step == AuthStep.OTP_INPUT) {
                        viewModel.setAuthStep(AuthStep.PHONE_INPUT)
                    } else {
                        viewModel.setAuthStep(AuthStep.GOOGLE_AUTH)
                    }
                }
            )
        }
        else -> {
            AuthLandingScreen(
                modifier = modifier.fillMaxSize(),
                scheme = scheme,
                uiState = uiState,
                onLanguageChange = { languageCode ->
                    viewModel.changeLanguage(context, languageCode)
                },
                onGoogleSignInClick = { launchGoogleSignIn() },
                onPhoneSignInClick = { viewModel.setAuthStep(AuthStep.PHONE_INPUT) },
                onRetryClick = {
                    viewModel.clearError()
                    launchGoogleSignIn()
                }
            )
        }
    }
}
