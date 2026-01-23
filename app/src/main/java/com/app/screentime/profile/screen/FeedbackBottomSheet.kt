package com.app.screentime.profile.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.navigation.ToastSnackbarManager
import com.app.screentime.profile.viewmodel.FeedbackViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.textarea.ODSTextArea
import com.telekom.odsystem.atoms.textarea.ODSTextAreaMode
import com.telekom.odsystem.atoms.textarea.ODSTextAreaProps
import com.telekom.odsystem.atoms.textarea.ODSTextAreaSize
import com.telekom.odsystem.atoms.textarea.ODSTextAreaSupportMessageProps
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackBottomSheetContent(
    onDismiss: () -> Unit = {},
    viewModel: FeedbackViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    var feedbackText by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Get string resources
    val feedbackSubmittedText = stringResource(R.string.feedback_submitted)

    // Observe submission result
    val submitResult by viewModel.submitResult.collectAsState()

    // Handle submission result
    LaunchedEffect(submitResult) {
        when (val result = submitResult) {
            is com.app.screentime.profile.viewmodel.FeedbackSubmitResult.Success -> {
                isSubmitting = false
                viewModel.clearResult()
                ToastSnackbarManager.showSuccessAsync(feedbackSubmittedText)
                onDismiss()
            }

            is com.app.screentime.profile.viewmodel.FeedbackSubmitResult.Error -> {
                isSubmitting = false
                errorMessage = result.message
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = result.message
                    )
                }
            }

            is com.app.screentime.profile.viewmodel.FeedbackSubmitResult.Loading -> {
                isSubmitting = true
                errorMessage = null
            }

            null -> {
                // Initial state
            }
        }
    }

    ODSBottomSheet(
        snackbarHostState = snackbarHostState,
        showBottomSheet = true,
        onDismissRequest = onDismiss,
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = stringResource(R.string.feedback),
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = stringResource(R.string.feedback_subtitle),
                    style = DSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSTextArea(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSTextAreaProps(
                        inputText = feedbackText,
                        labelText = stringResource(R.string.feedback_placeholder),
                        size = ODSTextAreaSize.LARGE,
                        disabled = isSubmitting,
                        mode = if (errorMessage != null) ODSTextAreaMode.ERROR else ODSTextAreaMode.STANDARD,
                        supportMessageProps = if (errorMessage != null) {
                            ODSTextAreaSupportMessageProps(message = errorMessage)
                        } else null
                    ),
                    onValueChange = {
                        feedbackText = it
                        errorMessage = null // Clear error when user types
                    }
                )
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))
            }
        },
        actionSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        size = ODSButtonSize.SMALL,
                        variant = ODSButtonVariant.SECONDARY,
                        label = stringResource(R.string.submit),
                        disabled = feedbackText.trim().isEmpty() || isSubmitting
                    ),
                    onClick = {
                        if (feedbackText.trim().isNotEmpty() && !isSubmitting) {
                            viewModel.submitFeedback(feedbackText.trim())
                        }
                    }
                )
            }
        },
        onCloseClicked = onDismiss
    )
}
