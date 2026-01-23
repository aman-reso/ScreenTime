package com.app.screentime.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.organisms.toast.ODSToast
import com.telekom.odsystem.organisms.toast.ODSToastMode
import com.telekom.odsystem.organisms.toast.ODSToastProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Custom SnackbarHost that uses ODS Toast instead of Material3 Snackbar
 * This should be placed in the Scaffold's snackbarHost parameter
 */
@Composable
fun ToastSnackbarHost(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    // Register the snackbar host state with the manager
    LaunchedEffect(snackbarHostState) {
        ToastSnackbarManager.setSnackbarHostState(snackbarHostState)
    }
    
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            // Use ODS Toast instead of default Material3 Snackbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DSVariables.spacingComponent4),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
                ) {
                    ODSToast(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSToastProps(
                            mode = ODSToastMode.INFORMATIVE,
                            text = snackbarData.visuals.message,
                            showCloseButton = snackbarData.visuals.withDismissAction
                        ),
                        onDismiss = {
                            snackbarData.dismiss()
                        }
                    )
                }
            }
        }
    )
}
