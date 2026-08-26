package com.app.screentime

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.ChattyTheme
import com.app.screentime.feature.auth.AuthGateScreen
import com.app.screentime.feature.auth.AuthViewModel
import com.app.screentime.messaging.ScreenTimeFirebaseMessagingService
import com.app.screentime.navigation.ScreenTimeNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var incomingCallData by mutableStateOf<Pair<String, String>?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        handleIncomingCallIntent(intent)

        setContent {
            val currentTheme by com.app.screentime.core.ui.theme.AppThemeManager.currentTheme.collectAsState()
            val isUnlocked by com.app.screentime.core.ui.security.BiometricAuthManager.isUnlocked.collectAsState()
            val isFingerprintEnabled = remember {
                com.app.screentime.core.ui.security.BiometricAuthManager.isFingerprintLockEnabled(this@MainActivity)
            }

            ChattyTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                if (isFingerprintEnabled && !isUnlocked) {
                    com.app.screentime.core.ui.security.BiometricLockScreen(
                        modifier = Modifier.fillMaxSize(),
                        scheme = currentTheme,
                        onUnlocked = {
                            com.app.screentime.core.ui.security.BiometricAuthManager.setUnlocked(true)
                        }
                    )
                } else if (!isLoggedIn) {
                    AuthGateScreen(
                        modifier = Modifier.fillMaxSize(),
                        scheme = currentTheme
                    )
                } else {
                    ScreenTimeNavigation(
                        modifier = Modifier.fillMaxSize(),
                        incomingCall = incomingCallData,
                        onClearIncomingCall = { incomingCallData = null },
                        scheme = currentTheme
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingCallIntent(intent)
    }

    private fun handleIncomingCallIntent(intent: Intent?) {
        if (intent?.action == ScreenTimeFirebaseMessagingService.ACTION_ACCEPT_CALL) {
            val callerId = intent.getStringExtra(ScreenTimeFirebaseMessagingService.EXTRA_CALLER_ID) ?: ""
            val callerName = intent.getStringExtra(ScreenTimeFirebaseMessagingService.EXTRA_CALLER_NAME) ?: "Caller"
            if (callerId.isNotEmpty()) {
                incomingCallData = Pair(callerId, callerName)
            }
        }
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}