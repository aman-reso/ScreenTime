package com.app.screentime.reward.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.toast.ODSToast
import com.telekom.odsystem.organisms.toast.ODSToastMode
import com.telekom.odsystem.organisms.toast.ODSToastProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay

/**
 * Error Toast Component using ODSToast
 * Displays error messages in a toast-like overlay at the bottom
 */
@Composable
fun RewardErrorSnackbar(
    message: String?,
    onDismiss: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    // Auto-dismiss after 5 seconds
    LaunchedEffect(message) {
        if (message != null) {
            delay(5000)
            onDismiss()
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = message != null,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            if (message != null) {
                ODSToast(
                    modifier = Modifier
                        .fillMaxWidth(),
                    scheme = scheme,
                    props = ODSToastProps(
                        mode = ODSToastMode.INFORMATIVE,
                        text = message,
                        showCloseButton = true
                    ),
                    onDismiss = onDismiss
                )
            }
        }
    }
}

