package com.app.screentime.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.telekom.odsystem.organisms.toast.ODSToastMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Data class representing the state of a toast snackbar message
 */
data class ToastSnackbarState(
    val message: String? = null,
    val mode: ODSToastMode = ODSToastMode.INFORMATIVE,
    val duration: Long = 5000L, // Auto-dismiss duration in milliseconds
    val showCloseButton: Boolean = true
) {
    val isVisible: Boolean
        get() = message != null
}

/**
 * Object to manage toast snackbar state globally
 * This can be accessed from anywhere in the app to show toast messages
 * Uses Material3's SnackbarHostState for proper integration with Scaffold
 */
object ToastSnackbarManager {
    private var snackbarHostState: SnackbarHostState? = null
    
    /**
     * Set the SnackbarHostState to use for showing snackbars
     * This should be called from the Scaffold's snackbarHost composable
     */
    fun setSnackbarHostState(state: SnackbarHostState) {
        snackbarHostState = state
    }
    
    /**
     * Show a toast message using SnackbarHostState
     * @param message The message to display
     * @param mode The toast mode (SUCCESS or INFORMATIVE)
     * @param duration Auto-dismiss duration in milliseconds (default: 5000ms)
     * @param showCloseButton Whether to show close button (default: true)
     */
    suspend fun showToast(
        message: String,
        mode: ODSToastMode = ODSToastMode.INFORMATIVE,
        duration: Long = 5000L,
        showCloseButton: Boolean = true
    ) {
        snackbarHostState?.showSnackbar(
            message = message,
            duration = if (duration > 0) {
                SnackbarDuration.Long
            } else {
                SnackbarDuration.Indefinite
            },
            withDismissAction = showCloseButton
        )
    }
    
    /**
     * Show a success toast message
     */
    suspend fun showSuccess(message: String, duration: Long = 5000L) {
        showToast(message, ODSToastMode.SUCCESS, duration)
    }
    
    /**
     * Show an informative/error toast message
     */
    suspend fun showError(message: String, duration: Long = 5000L) {
        showToast(message, ODSToastMode.INFORMATIVE, duration)
    }
    
    /**
     * Show toast from a non-suspend context (uses CoroutineScope)
     */
    fun showErrorAsync(message: String, duration: Long = 5000L) {
        CoroutineScope(Dispatchers.Main).launch {
            showError(message, duration)
        }
    }
    
    /**
     * Show success toast from a non-suspend context (uses CoroutineScope)
     */
    fun showSuccessAsync(message: String, duration: Long = 5000L) {
        CoroutineScope(Dispatchers.Main).launch {
            showSuccess(message, duration)
        }
    }
    
    /**
     * Dismiss the current snackbar
     */
    suspend fun dismiss() {
        snackbarHostState?.currentSnackbarData?.dismiss()
    }
    
    /**
     * Get the current snackbar host state
     */
    fun getSnackbarHostState(): SnackbarHostState? = snackbarHostState
}

