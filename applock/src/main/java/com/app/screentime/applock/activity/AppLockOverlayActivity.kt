package com.app.screentime.applock.activity

import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.app.screentime.applock.component.PINPad
import com.app.screentime.applock.component.PatternLockView
import com.app.screentime.applock.manager.AppLockManager
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.applock.repository.AppLockRepository.LockType
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private var blockReason: String? = null
    private var launchLimit: Int = 0
    private var launchCount: Int = 0

    companion object {
        private const val TAG = "AppLockOverlayActivity"
        const val BLOCK_REASON_LAUNCH_LIMIT = "launch_limit"
        const val BLOCK_REASON_TIME_LIMIT = "time_limit"
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockedPackageName = intent.getStringExtra("locked_package")
        blockReason = intent.getStringExtra("block_reason")
        launchLimit = intent.getIntExtra("launch_limit", 0)
        launchCount = intent.getIntExtra("launch_count", 0)
        
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
            when (blockReason) {
                BLOCK_REASON_LAUNCH_LIMIT -> {
                    // Show launch limit exceeded screen (no PIN required)
                    LaunchLimitExceededScreen(
                        appName = appName,
                        launchLimit = launchLimit,
                        launchCount = launchCount,
                        onGoBack = { closeLockedApp() }
                    )
                }
                BLOCK_REASON_TIME_LIMIT -> {
                    // Show time limit exceeded screen (no PIN required)
                    TimeLimitExceededScreen(
                        appName = appName,
                        onGoBack = { closeLockedApp() }
                    )
                }
                else -> {
                    // Regular app lock - show PIN/Pattern screen
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
        // Don't moveTaskToBack - it can cause issues on some devices
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Reset lock screen flag when activity is destroyed
        AppLockManager.isLockScreenShown.set(false)
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

                // Kill background processes of the locked app
                activityManager.killBackgroundProcesses(packageName)

                Log.d(TAG, "Closed locked app: $packageName")
            } catch (e: Exception) {
                Log.e(TAG, "Error closing locked app: ${e.message}", e)
            }
        }

        // Navigate to home screen to ensure we leave the blocked app
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(homeIntent)

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

/**
 * Screen shown when app launch limit is exceeded
 */
@Composable
private fun LaunchLimitExceededScreen(
    appName: String,
    launchLimit: Int,
    launchCount: Int,
    onGoBack: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
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
            // Icon
            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = Icons.Default.RocketLaunch,
                    tint = scheme.functionalDestructiveStandard,
                    contentDescription = "Launch limit"
                ),
                width = 64.dp,
                height = 64.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            ODSText(
                text = "Launch Limit Reached",
                style = DSTextStyles.titleM,
                color = scheme.basicText
            )

            // App name
            ODSText(
                text = appName,
                style = DSTextStyles.bodyL,
                color = scheme.basicTextRecessive
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Message
            ODSText(
                text = "You've opened this app $launchCount times today.",
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )
            
            ODSText(
                text = "Daily limit: $launchLimit launches",
                style = DSTextStyles.bodyMBold,
                color = scheme.functionalDestructiveStandard
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Go Back button
            ODSButton(
                modifier = Modifier.fillMaxWidth(0.6f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Go Back",
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.LARGE,
                    buttonType = ODSButtonButtonType.STANDARD
                ),
                onClick = onGoBack
            )
        }
    }
}

/**
 * Screen shown when app time limit is exceeded
 */
@Composable
private fun TimeLimitExceededScreen(
    appName: String,
    onGoBack: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
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
            // Icon
            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = Icons.Default.Timer,
                    tint = scheme.functionalDestructiveStandard,
                    contentDescription = "Time limit"
                ),
                width = 64.dp,
                height = 64.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            ODSText(
                text = "Time Limit Reached",
                style = DSTextStyles.titleM,
                color = scheme.basicText
            )

            // App name
            ODSText(
                text = appName,
                style = DSTextStyles.bodyL,
                color = scheme.basicTextRecessive
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Message
            ODSText(
                text = "You've reached your daily time limit for this app.",
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Go Back button
            ODSButton(
                modifier = Modifier.fillMaxWidth(0.6f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Go Back",
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.LARGE,
                    buttonType = ODSButtonButtonType.STANDARD
                ),
                onClick = onGoBack
            )
        }
    }
}
