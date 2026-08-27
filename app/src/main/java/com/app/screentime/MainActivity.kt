package com.app.screentime

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.screentime.core.ui.security.BiometricAuthManager
import com.app.screentime.core.ui.security.BiometricLockScreen
import com.app.screentime.core.ui.theme.ChattyTheme
import com.app.screentime.feature.auth.AuthGateScreen
import com.app.screentime.feature.auth.AuthViewModel
import com.app.screentime.feature.call.ActiveCallManager
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallUiState
import com.app.screentime.feature.call.receiver.CallActionReceiver
import com.app.screentime.messaging.ScreenTimeFirebaseMessagingService
import com.app.screentime.navigation.ScreenTimeNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var activeCallManager: ActiveCallManager

    private var incomingCallData by mutableStateOf<Pair<String, String>?>(null)
    private var isInPipMode by mutableStateOf(false)

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
        observeCallStateForPiP()

        setContent {
            val currentTheme by com.app.screentime.core.ui.theme.AppThemeManager.currentTheme.collectAsState()
            val isUnlocked by BiometricAuthManager.isUnlocked.collectAsState()
            val isFingerprintEnabled = remember {
                BiometricAuthManager.isFingerprintLockEnabled(this@MainActivity)
            }

            ChattyTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        activeCallManager.ensureConnected()
                    }
                }

                if (isFingerprintEnabled && !isUnlocked && !isInPipMode) {
                    BiometricLockScreen(
                        modifier = Modifier.fillMaxSize(),
                        scheme = currentTheme,
                        onUnlocked = {
                            BiometricAuthManager.setUnlocked(true)
                        }
                    )
                } else if (!isLoggedIn && !isInPipMode) {
                    AuthGateScreen(
                        modifier = Modifier.fillMaxSize(),
                        scheme = currentTheme
                    )
                } else {
                    ScreenTimeNavigation(
                        modifier = Modifier.fillMaxSize(),
                        incomingCall = incomingCallData,
                        onClearIncomingCall = { incomingCallData = null },
                        scheme = currentTheme,
                        isInPipMode = isInPipMode
                    )
                }
            }
        }
    }

    private fun observeCallStateForPiP() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                activeCallManager.callState.collect { callState ->
                    updatePiPParams(callState)
                    if (callState.status == CallStatus.INCOMING && incomingCallData == null) {
                        incomingCallData = Pair(
                            callState.remoteUserId.ifBlank { "unknown" },
                            callState.remoteUserName.ifBlank { "Incoming Call" }
                        )
                    }
                }
            }
        }
    }

    private fun updatePiPParams(callState: CallUiState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val params = buildPipParams(callState)
                setPictureInPictureParams(params)
            } catch (e: Exception) {
                // Ignore if PiP is not supported on device
            }
        }
    }

    private fun buildPipParams(callState: CallUiState): PictureInPictureParams {
        val isCallActive = callState.status == CallStatus.ACTIVE || callState.status == CallStatus.DIALING
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(9, 16))
        } else {
            return null as PictureInPictureParams
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(isCallActive)
            builder.setSeamlessResizeEnabled(true)
        }

        if (isCallActive) {
            val actions = mutableListOf<RemoteAction>()

            // 1. Mute Action
            val muteIntent = Intent(CallActionReceiver.ACTION_TOGGLE_MUTE).apply {
                setPackage(packageName)
            }
            val mutePendingIntent = PendingIntent.getBroadcast(
                this,
                201,
                muteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val muteIcon = Icon.createWithResource(this, android.R.drawable.stat_notify_chat)
            val muteTitle = if (callState.isMuted) "Unmute" else "Mute"
            actions.add(RemoteAction(muteIcon, muteTitle, muteTitle, mutePendingIntent))

            // 2. Hang Up Action
            val hangupIntent = Intent(CallActionReceiver.ACTION_HANGUP).apply {
                setPackage(packageName)
            }
            val hangupPendingIntent = PendingIntent.getBroadcast(
                this,
                202,
                hangupIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val hangupIcon = Icon.createWithResource(this, android.R.drawable.ic_menu_close_clear_cancel)
            actions.add(RemoteAction(hangupIcon, "End Call", "End Call", hangupPendingIntent))

            builder.setActions(actions)
        }

        return builder.build()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val state = activeCallManager.callState.value
        if (state.status == CallStatus.ACTIVE || state.status == CallStatus.DIALING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    enterPictureInPictureMode(buildPipParams(state))
                } catch (e: Exception) {
                    // Fallback
                }
            }
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isInPipMode = isInPictureInPictureMode
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