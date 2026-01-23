package com.app.screentime.applock.activity

import android.app.ActivityManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.app.screentime.applock.component.PINPad
import com.app.screentime.applock.component.PatternLockView
import com.app.screentime.applock.manager.AppLockManager
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.applock.repository.AppLockRepository.LockType
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Overlay activity that shows PIN entry screen when a locked app is accessed
 */
@AndroidEntryPoint
class AppLockOverlayActivity : ComponentActivity() {

    private val appLockRepository: AppLockRepository by lazy {
        AppLockRepository(applicationContext)
    }

    private var lockedPackageName: String? = null
    private var appName: String = "App"

    companion object {
        private const val TAG = "AppLockOverlayActivity"
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockedPackageName = intent.getStringExtra("locked_package")
        if (lockedPackageName == null) {
            Log.e(TAG, "No locked_package provided in intent. Finishing.")
            finish()
            return
        }
        setupWindow()
        loadAppName()
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(this) {
            // When user tries to close the PIN screen, close the locked app instead
            closeLockedApp()
        }

        setContent {
            @OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
            val lockType = appLockRepository.getLockType()
            AppLockOverlayScreen(
                appName = appName,
                lockedPackageName = lockedPackageName ?: "",
                lockType = lockType,
                onPinVerified = { pin ->
                    if (appLockRepository.validatePin(pin)) {
                        lockedPackageName?.let { pkg ->
                            AppLockManager.unlockApp(pkg)
                            finish()
                        }
                    }
                },
                onPatternVerified = { pattern ->
                    if (appLockRepository.validatePattern(pattern)) {
                        lockedPackageName?.let { pkg ->
                            AppLockManager.unlockApp(pkg)
                            finish()
                        }
                    }
                },
                onDismiss = {
                    // Cannot dismiss - must enter PIN or Pattern
                }
            )
        }
    }

    private fun setupWindow() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SECURE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                window.setHideOverlayWindows(true)
            } catch (e: SecurityException) {
                // Permission denied - this is expected for regular apps
                // HIDE_OVERLAY_WINDOWS is a system-level permission
                Log.d(TAG, "setHideOverlayWindows permission denied (expected for regular apps)")
            } catch (e: Exception) {
                Log.w(TAG, "Error setting hide overlay windows: ${e.message}")
            }
        }

        val layoutParams = window.attributes
        layoutParams.type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        window.attributes = layoutParams
    }

    private fun loadAppName() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                lockedPackageName?.let { pkg ->
                    val appInfo = packageManager.getApplicationInfo(pkg, 0)
                    appName = packageManager.getApplicationLabel(appInfo).toString()
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, "Error loading app name: ${e.message}")
                appName = "App"
            } catch (e: Exception) {
                Log.e(TAG, "Error loading app name: ${e.message}")
                appName = "App"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Keep activity on top even when paused
        if (!isFinishing) {
            moveTaskToBack(false)
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // User tried to leave (home button, recent apps, etc.) - close the locked app
        closeLockedApp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Override back button to close the locked app
        // Note: onBackPressedDispatcher.addCallback is the modern way, but keeping this for compatibility
        closeLockedApp()
    }

    /**
     * Closes the locked app and finishes this overlay activity
     */
    private fun closeLockedApp() {
        lockedPackageName?.let { packageName ->
            try {
                val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager

                // Force stop the locked app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // Android 9+ requires usage of killBackgroundProcesses or forceStopPackage
                    // Note: forceStopPackage requires system app or device admin
                    activityManager.killBackgroundProcesses(packageName)
                } else {
                    @Suppress("DEPRECATION")
                    activityManager.killBackgroundProcesses(packageName)
                }

                Log.d(TAG, "Closed locked app: $packageName")
            } catch (e: Exception) {
                Log.e(TAG, "Error closing locked app: ${e.message}", e)
            }
        }

        // Finish this overlay activity
        finish()
    }
}

@androidx.compose.ui.ExperimentalComposeUiApi
@Composable
private fun AppLockOverlayScreen(
    appName: String,
    lockedPackageName: String,
    lockType: LockType,
    onPinVerified: (String) -> Unit,
    onPatternVerified: (String) -> Unit,
    onDismiss: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    var pin by remember { mutableStateOf("") }
    var pattern by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val appLockRepository = remember { AppLockRepository(context) }

    LaunchedEffect(pin) {
        if (lockType == LockType.PIN && pin.length == 4) {
            if (appLockRepository.validatePin(pin)) {
                onPinVerified(pin)
                pin = ""
            } else {
                errorMessage = "Incorrect PIN"
                pin = ""
                // Clear error after a delay
                kotlinx.coroutines.delay(2000)
                errorMessage = null
            }
        }
    }
    
    LaunchedEffect(pattern) {
        if (lockType == LockType.PATTERN && pattern != null) {
            if (appLockRepository.validatePattern(pattern!!)) {
                onPatternVerified(pattern!!)
                pattern = null
            } else {
                errorMessage = "Incorrect Pattern"
                pattern = null
                // Clear error after a delay
                kotlinx.coroutines.delay(2000)
                errorMessage = null
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = scheme.basicBackground.getColor()
    ) { paddingValues ->
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            gap = DSVariables.spacingComponent4,
            padding = ODSPadding(all = DSVariables.spacingComponent4)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = "Unlock $appName",
                    style = DSTextStyles.bodyL,
                    color = scheme.basicText
                )
                ODSText(
                    text = if (lockType == LockType.PIN) "Enter your PIN" else "Draw your pattern",
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                // Error message
                if (errorMessage != null) {
                    ODSText(
                        text = errorMessage ?: "",
                        style = DSTextStyles.bodySRegular,
                        color = scheme.functionalDestructiveStandard
                    )
                }
            }

            // Show PIN or Pattern based on lock type
            when (lockType) {
                LockType.PIN -> {
                    PINPad(
                        modifier = Modifier,
                        pin = pin,
                        onDigitClick = { digit ->
                            if (pin.length < 4) {
                                pin += digit
                                errorMessage = null
                            }
                        },
                        onDeleteClick = {
                            if (pin.isNotEmpty()) {
                                pin = pin.dropLast(1)
                            }
                        },
                        scheme = scheme,
                        errorMessage = errorMessage,
                        onPinVerified = {
                            onPinVerified(pin)
                        }
                    )
                }
                LockType.PATTERN -> {
                    PatternLockView(
                        modifier = Modifier,
                        scheme = scheme,
                        onPatternComplete = { patternString ->
                            pattern = patternString
                        },
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }
}

