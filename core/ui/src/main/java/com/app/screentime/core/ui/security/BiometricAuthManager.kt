package com.app.screentime.core.ui.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

sealed interface BiometricStatus {
    object Available : BiometricStatus
    data class Unavailable(val reason: String) : BiometricStatus
    object NoneEnrolled : BiometricStatus
}

object BiometricAuthManager {

    private const val PREFS_NAME = "screentime_security_prefs"
    private const val KEY_FINGERPRINT_LOCK = "is_fingerprint_lock_enabled"

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    fun isFingerprintLockEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FINGERPRINT_LOCK, false)
    }

    fun setFingerprintLockEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_FINGERPRINT_LOCK, enabled) }
        if (!enabled) {
            _isUnlocked.value = true
        }
    }

    fun setUnlocked(unlocked: Boolean) {
        _isUnlocked.value = unlocked
    }

    fun checkBiometricStatus(context: Context): BiometricStatus {
        val biometricManager = BiometricManager.from(context)
        val authenticators = BIOMETRIC_STRONG or BIOMETRIC_WEAK

        return when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.Available
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NoneEnrolled
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.Unavailable("No biometric hardware found on this device")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.Unavailable("Biometric hardware currently unavailable")
            else -> BiometricStatus.Unavailable("Biometric authentication not supported")
        }
    }

    fun authenticate(
        activity: FragmentActivity,
        title: String = "Fingerprint Authentication",
        subtitle: String = "Verify your fingerprint to continue",
        description: String = "Scan your fingerprint on the sensor",
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                _isUnlocked.value = true
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                    errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON
                ) {
                    onError(errString.toString())
                } else {
                    onError("Authentication cancelled")
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onError("Fingerprint not recognized. Please try again.")
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
            .build()

        val prompt = BiometricPrompt(activity, executor, callback)
        prompt.authenticate(promptInfo)
    }
}
