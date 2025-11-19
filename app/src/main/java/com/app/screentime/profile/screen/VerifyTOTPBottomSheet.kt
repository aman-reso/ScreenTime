package com.app.screentime.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.profile.viewmodel.VerifyTOTPViewModel
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.LocalAppColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyTOTPBottomSheetContent(
    onDismiss: () -> Unit,
    onVerifySuccess: () -> Unit = {},
    username: String? = null, // Username for username-based verification
    viewModel: VerifyTOTPViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val uiState by viewModel.uiState.collectAsState()

    var otpText by remember { mutableStateOf("") }

    // Reset state when bottom sheet is shown
    LaunchedEffect(Unit) {
        viewModel.resetVerification()
        otpText = ""
    }

    // Handle verification success
    LaunchedEffect(uiState.isValid) {
        if (uiState.isValid && uiState.isVerified) {
            // Small delay to show success state
            delay(500)
            onVerifySuccess()
        }
    }

    val colors = LocalAppColors.current ?: return
    ModalBottomSheet(
        containerColor = colors.background,
        sheetGesturesEnabled = false,
        sheetState = sheetState,
        properties = ModalBottomSheetProperties(
            shouldDismissOnClickOutside = true
        ),
        onDismissRequest = {
            viewModel.resetVerification()
            otpText = ""
            onDismiss()
        },
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassBottomSheetBackground()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Verify TOTP",
                    style = AppTextStyle.SubTitle,
                )
                IconButton(onClick = {
                    viewModel.resetVerification()
                    otpText = ""
                    onDismiss()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.tint
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppText(
                text = if (username != null) {
                    "Enter the OTP code for $username to verify"
                } else {
                    "Enter the OTP code to verify"
                },
                style = AppTextStyle.Label
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = colors.textHint)
            Spacer(modifier = Modifier.height(16.dp))

            // Glassy OTP Input Field
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current
            var isFocused by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(colors.card.copy(alpha = 0.0f))
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                if (uiState.error != null && uiState.isVerified) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    colors.textPrimary
                                },
                                if (uiState.error != null && uiState.isVerified) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    colors.textPrimary
                                },
                                if (uiState.error != null && uiState.isVerified) {
                                    MaterialTheme.colorScheme.error.copy(alpha = 0.42f)
                                } else {
                                    colors.textPrimary.copy(alpha = 0.42f)
                                }
                            )
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                BasicTextField(
                    value = otpText,
                    onValueChange = {
                        // Only allow digits and limit to 6 characters
                        otpText = it.filter { it.isDigit() }.take(6)
                    },
                    enabled = !uiState.isVerifying,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
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
                    modifier = Modifier
                        .matchParentSize()
                        .focusRequester(focusRequester)
                        .onFocusChanged { state ->
                            isFocused = state.isFocused
                        }
                        .padding(horizontal = 16.dp),
                    textStyle = TextStyle(
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        letterSpacing = 4.sp
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (otpText.isEmpty() && !isFocused) {
                                    AppText(
                                        text = "Enter OTP",
                                        color = colors.textHint,
                                        style = AppTextStyle.Label
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )
            }

            // Error message
            if (uiState.error != null && uiState.isVerified) {
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = uiState.error ?: "",
                    style = AppTextStyle.Label,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Button
            AppPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = if (uiState.isVerifying) "Verifying..." else "Verify",
                enabled = !uiState.isVerifying && otpText.length == 6 && username != null,
                onClick = {
                    username?.let {
                        viewModel.verifyTOTPByUsername(it, otpText)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

