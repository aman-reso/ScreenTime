package com.app.screentime

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.screentime.core.network.NetworkAuthBridge
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.ui.theme.WinterTheme
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.auth.AuthGateScreen
import com.app.screentime.navigation.Screen
import com.app.screentime.navigation.ScreenTimeNavigation
import com.telekom.odsystem.ODSystem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.app.screentime.core.ui.security.ScreenshotProtectionManager
import com.app.screentime.core.ui.util.ODSSnackbarHost
import com.telekom.odsystem.atoms.ODSBox
import com.app.screentime.update.InAppUpdateManager
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.app.screentime.core.network.UnauthorizedHandler
import com.app.screentime.core.network.config.AppConfigManager
import com.app.screentime.core.network.security.PlayIntegrityManager
import com.app.screentime.core.network.session.FcmTokenManager
import com.app.screentime.feature.auth.WinterSplashScreen
import com.app.screentime.feature.chat.domain.usecase.RealtimeChatSyncManager
import com.app.screentime.security.SecurityBlockScreen
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    @Inject
    lateinit var realtimeChatSyncManager: Lazy<RealtimeChatSyncManager>

    @Inject
    lateinit var fcmTokenManager: Lazy<FcmTokenManager>

    @Inject
    lateinit var appConfigManager: Lazy<AppConfigManager>

    @Inject
    lateinit var playIntegrityManager: Lazy<PlayIntegrityManager>

    @Inject
    lateinit var e2eCryptoManager: Lazy<com.app.screentime.core.network.security.E2ECryptoManager>

    private var pendingNotificationScreen by mutableStateOf<Screen?>(null)
    private lateinit var screenshotProtectionManager: ScreenshotProtectionManager

    private var isIntegrityVerified by mutableStateOf(com.app.screentime.core.network.BuildConfig.DEBUG)
    private var integrityCheckFailed by mutableStateOf(false)
    private var integrityErrorText by mutableStateOf("")

    private fun verifyAppIntegrityMandatory() {
        if (com.app.screentime.core.network.BuildConfig.DEBUG) {
            isIntegrityVerified = true
            return
        }
        integrityCheckFailed = false
        integrityErrorText = ""
        val mgr = playIntegrityManager.get()
        if (!mgr.getCachedToken().isNullOrEmpty()) {
            isIntegrityVerified = true
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                mgr.fetchAttestationToken()
                withContext(Dispatchers.Main) {
                    isIntegrityVerified = true
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    integrityCheckFailed = true
                    integrityErrorText = t.message ?: "Google Play Integrity verification failed"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val t0 = System.currentTimeMillis()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)

        screenshotProtectionManager = ScreenshotProtectionManager(this)
        screenshotProtectionManager.applySecureFlag()

        try {
            inAppUpdateManager.initialize(this)
        } catch (e: Exception) {
            Log.e("MainActivity", "InAppUpdate initialize failed: ${e.message}")
        }

        try {
            appConfigManager.get().fetchConfigAsync()
        } catch (_: Throwable) {
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        ODSystem.colors.value = zonaODSTheme

        pendingNotificationScreen = extractScreenFromIntent(intent)

        if (!com.app.screentime.core.network.BuildConfig.DEBUG) {
            verifyAppIntegrityMandatory()
        }

        NetworkAuthBridge.unauthorizedHandler =
            object : UnauthorizedHandler {
                override fun onUnauthorized() {
                    runOnUiThread {
                        Log.w(
                            "MainActivity",
                            "🔴 401 Unauthorized -> Clearing session and returning to Login screen"
                        )
                        sessionManager.clearSession()
                    }
                }
            }

        setContent {

            WinterTheme {
                val isLoggedIn by sessionManager.isLoggedInFlow.collectAsState()
                Log.i(
                    "STARTUP_TRACE",
                    "👤 [${System.currentTimeMillis() - t0}ms] isLoggedIn=$isLoggedIn, integrityVerified=$isIntegrityVerified"
                )

                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        withContext(Dispatchers.IO) {
                            realtimeChatSyncManager.get().ensureConnected()
                            fcmTokenManager.get().syncFcmToken()
                            val token = sessionManager.getToken().orEmpty()
                            if (token.isNotBlank()) {
                                e2eCryptoManager.get().registerPublicKeyIfNeeded(token)
                            }
                        }
                    }
                }

                if (!isIntegrityVerified && !com.app.screentime.core.network.BuildConfig.DEBUG) {
                    if (integrityCheckFailed) {
                        SecurityBlockScreen(
                            errorMessage = integrityErrorText,
                            onRetry = { verifyAppIntegrityMandatory() }
                        )
                    } else {
                        WinterSplashScreen()
                    }
                } else if (!isLoggedIn) {
                    ODSBox(modifier = Modifier.fillMaxSize()) {
                        AuthGateScreen(
                            modifier = Modifier.fillMaxSize(),
                            scheme = zonaODSTheme
                        )
                        ODSSnackbarHost(
                            hostState = remember { SnackbarHostState() },
                            scheme = zonaODSTheme,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                } else {
                    ScreenTimeNavigation(
                        modifier = Modifier.fillMaxSize(),
                        pendingNotificationScreen = pendingNotificationScreen,
                        onClearPendingNotificationScreen = { pendingNotificationScreen = null },
                        onLogout = { sessionManager.clearSession() },
                        scheme = zonaODSTheme
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        screenshotProtectionManager.startListening()
    }

    override fun onStop() {
        super.onStop()
        screenshotProtectionManager.stopListening()
    }

    override fun onPause() {
        super.onPause()
        screenshotProtectionManager.onAppBackgrounded()
    }

    override fun onResume() {
        super.onResume()
        screenshotProtectionManager.onAppForegrounded()
        try {
            inAppUpdateManager.onResume(this)
        } catch (_: Exception) {
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            screenshotProtectionManager.onAppForegrounded()
        } else {
            screenshotProtectionManager.onAppBackgrounded()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val targetScreen = extractScreenFromIntent(intent)
        if (targetScreen != null) {
            pendingNotificationScreen = targetScreen
        }
    }

    private fun extractScreenFromIntent(intent: Intent?): Screen? {
        if (intent == null) return null
        val screenKey = (intent.getStringExtra("screen")
            ?: intent.getStringExtra("route")
            ?: intent.getStringExtra("type")
            ?: intent.getStringExtra("event")
            ?: intent.getStringExtra("action")
            ?: "").trim()

        val partnerId = (intent.getStringExtra("partner_id")
            ?: intent.getStringExtra("sender_id")
            ?: intent.getStringExtra("model_id")
            ?: intent.getStringExtra("user_id")
            ?: intent.getStringExtra("from_user_id")
            ?: "").trim()

        val partnerName = (intent.getStringExtra("partner_name")
            ?: intent.getStringExtra("sender_name")
            ?: intent.getStringExtra("model_name")
            ?: intent.getStringExtra("user_name")
            ?: intent.getStringExtra("name")
            ?: "Friend").trim()

        val conversationId = intent.getStringExtra("conversation_id")?.trim()?.ifBlank { null }

        val callType = (intent.getStringExtra("call_type") ?: "").trim()
        val isVideoCall = callType.contains("video", ignoreCase = true) ||
                screenKey.contains("video", ignoreCase = true)

        if (intent.action == "com.app.screentime.ACTION_ACCEPT_CALL") {
            val callerId = (intent.getStringExtra("caller_id") ?: partnerId).trim()
            val callerName = (intent.getStringExtra("caller_name") ?: partnerName).trim()
            return if (isVideoCall) {
                Screen.VideoCall(modelId = callerId, modelName = callerName)
            } else {
                Screen.VoiceCall(modelId = callerId, modelName = callerName)
            }
        }

        return when (screenKey.lowercase()) {
            "video_call" -> {
                Screen.VideoCall(modelId = partnerId, modelName = partnerName)
            }
            "voice_call", "call", "incoming_call" -> {
                if (isVideoCall) {
                    Screen.VideoCall(modelId = partnerId, modelName = partnerName)
                } else {
                    Screen.VoiceCall(modelId = partnerId, modelName = partnerName)
                }
            }
            "chat", "direct_message", "incoming_message", "message", "chat_message", "chat_message_received" -> {
                if (partnerId.isNotBlank()) {
                    Screen.Chat(
                        modelId = partnerId,
                        modelName = partnerName,
                        conversationId = conversationId
                    )
                } else {
                    Screen.ChatList
                }
            }

            "chats", "chat_list", "inbox", "incoming_message_request", "message_request", "message_request_accepted", "request_accepted" -> {
                if (partnerId.isNotBlank()) {
                    Screen.Chat(
                        modelId = partnerId,
                        modelName = partnerName,
                        conversationId = conversationId
                    )
                } else {
                    Screen.ChatList
                }
            }

            "discover", "discover_map", "map" -> {
                Screen.Home
            }

            "home", "feed", "like_received", "new_like", "likes" -> {
                Screen.Home
            }

            "profile", "user_profile", "profile_detail" -> {
                if (partnerId.isNotBlank()) {
                    Screen.ProfileDetail(userId = partnerId, userName = partnerName)
                } else {
                    Screen.Account
                }
            }

            "notifications", "notification" -> {
                Screen.Notifications
            }

            "account", "my_profile" -> {
                Screen.Account
            }

            "edit_profile" -> {
                Screen.EditProfile
            }

            else -> {
                if (partnerId.isNotBlank()) {
                    Screen.Chat(
                        modelId = partnerId,
                        modelName = partnerName,
                        conversationId = conversationId
                    )
                } else if (screenKey.isNotBlank()) {
                    Screen.Notifications
                } else {
                    null
                }
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

    override fun onDestroy() {
        try {
            inAppUpdateManager.onDestroy()
        } catch (_: Exception) {
        }
        super.onDestroy()
    }
}