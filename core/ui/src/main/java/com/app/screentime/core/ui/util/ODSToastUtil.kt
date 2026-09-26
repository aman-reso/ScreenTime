package com.app.screentime.core.ui.util

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.organisms.toast.ODSToast
import com.telekom.odsystem.organisms.toast.ODSToastMode
import com.telekom.odsystem.organisms.toast.ODSToastProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Data model for ODS Toast events.
 */
data class ODSToastData(
    val message: String,
    val title: String? = null,
    val mode: ODSToastMode = ODSToastMode.SUCCESS
)

/**
 * Manager to publish ODS Toast events reactively to Compose UI.
 */
object ODSToastManager {
    private val _toastEvents = MutableSharedFlow<ODSToastData>(extraBufferCapacity = 64)
    val toastEvents = _toastEvents.asSharedFlow()

    fun show(message: String, title: String? = null, mode: ODSToastMode = ODSToastMode.SUCCESS) {
        _toastEvents.tryEmit(ODSToastData(message, title, mode))
    }
}

/**
 * Extension function to display an ODS Toast via Compose ODSToastManager without native android.widget.Toast.
 */
fun Context.showODSToast(
    message: String,
    title: String? = null,
    mode: ODSToastMode = ODSToastMode.SUCCESS,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSToastManager.show(message = message, title = title, mode = mode)
}

/**
 * ODSSnackbarHost displays ODS-styled toast notifications in Jetpack Compose.
 * It observes ODSToastManager events and renders the library's ODSToast composable.
 */
@Composable
fun ODSSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    LaunchedEffect(hostState) {
        ODSToastManager.toastEvents.collect { toastData ->
            hostState.showSnackbar(
                message = toastData.message,
                withDismissAction = true
            )
        }
    }

    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            ODSToast(
                scheme = scheme,
                props = ODSToastProps(
                    text = snackbarData.visuals.message,
                    mode = ODSToastMode.SUCCESS,
                    showCloseButton = snackbarData.visuals.withDismissAction
                ),
                onDismiss = { snackbarData.dismiss() }
            )
        }
    )
}

/**
 * CompositionLocal for passing a SnackbarHostState down the Compose hierarchy.
 */
val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState?> { null }

/**
 * Suspend extension function to trigger an ODS Toast notification using Compose SnackbarHostState.
 */
suspend fun SnackbarHostState.showODSSnackbar(
    message: String,
    mode: ODSToastMode = ODSToastMode.SUCCESS,
    duration: SnackbarDuration = SnackbarDuration.Short,
    withDismissAction: Boolean = true
): SnackbarResult {
    return showSnackbar(
        message = message,
        duration = duration,
        withDismissAction = withDismissAction
    )
}
