package com.app.screentime.profile.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.profile.viewmodel.VerifyTOTPViewModel
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.hintTextColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyTOTPBottomSheetContent(
    onDismiss: () -> Unit,
    onVerifySuccess: () -> Unit = {},
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

    ModalBottomSheet(
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
                .padding(horizontal = 16.dp),
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
                        contentDescription = "Close"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppText(
                text = "Enter the OTP code to verify",
                style = AppTextStyle.Label
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = hintTextColor)
            Spacer(modifier = Modifier.height(16.dp))

            // OTP Input Field
            OutlinedTextField(
                value = otpText,
                onValueChange = {
                    // Only allow digits and limit to 6 characters
                    otpText = it.filter { it.isDigit() }.take(6)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    AppText(
                        text = "Enter OTP",
                        style = AppTextStyle.Label,
                        color = hintTextColor
                    )
                },
                singleLine = true,
                enabled = !uiState.isVerifying,
                isError = uiState.error != null && uiState.isVerified,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = hintTextColor.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

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
                enabled = !uiState.isVerifying && otpText.length == 6,
                onClick = {
                    viewModel.verifyTOTP(otpText)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

