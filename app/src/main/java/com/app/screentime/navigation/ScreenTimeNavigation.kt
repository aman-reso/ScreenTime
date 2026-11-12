package com.app.screentime.navigation

import android.annotation.SuppressLint
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.app.screentime.feature.call.VoiceCallScreen
import com.app.screentime.feature.call.VideoCallScreen
import com.app.screentime.feature.call.CallViewModel
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallType
import com.app.screentime.feature.call.IncomingCallGlobalOverlay
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.core.ui.util.ODSSnackbarHost
import com.app.screentime.feature.chat.ChatListScreen
import com.app.screentime.feature.chat.ChatScreen
import com.app.screentime.feature.discover.HomeFeedScreen
import com.app.screentime.feature.discover.TermsOfServiceScreen
import com.app.screentime.feature.models.ui.ModelsTabScreen
import com.app.screentime.feature.preferences.EditPreferencesScreen
import com.app.screentime.feature.preferences.NotificationPermissionBottomSheet
import com.app.screentime.feature.preferences.PreferencesScreen
import com.app.screentime.feature.profile.ControlAccountScreen
import com.app.screentime.feature.profile.EditProfileScreen
import com.app.screentime.feature.profile.LikedProfilesScreen
import com.app.screentime.feature.profile.LikesHubScreen
import com.app.screentime.feature.profile.MatchesScreen
import com.app.screentime.feature.profile.NotificationsScreen
import com.app.screentime.feature.profile.ProfileDetailScreen
import com.app.screentime.feature.profile.UserProfileScreen
import com.app.screentime.feature.profile.WhoLikedMeScreen
import com.app.screentime.feature.wallet.AddFundsScreen
import com.app.screentime.feature.wallet.CreatorEarningsScreen
import com.app.screentime.feature.wallet.TransactionsScreen
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

// ── 5 Main Bottom Navigation Tabs: Home, Wallet, Call, Chats, Profile ────────────
// ── 4 Main Bottom Navigation Tabs: Explore (Talk), Wallet/Earnings, Chats, Profile ─
data class BottomNavTab(
    val screen: Screen,
    val label: String,
    val imageVector: ImageVector? = null,
    @DrawableRes val iconRes: Int? = null
)

val bottomNavTabs = listOf(
    BottomNavTab(Screen.Models, "Explore", iconRes = R.drawable.ic_phone),
    BottomNavTab(Screen.Wallet, "Wallet", iconRes = R.drawable.wallet),
    BottomNavTab(Screen.ChatList, "Chats", iconRes = R.drawable.ic_message_circle),
    BottomNavTab(Screen.Account, "Profile", iconRes = R.drawable.ic_user)
)

private val bottomNavRoutes: Set<Screen> = setOf(
    Screen.Models, Screen.Wallet, Screen.ChatList, Screen.Account
)

/**
 * Root navigation composable for Chatty Live Companion App.
 * Directly launches Explore / Models directory so users can start exploring whom to talk to.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    modifier: Modifier = Modifier,
    deeplinkUri: Uri? = null,
    pendingNotificationScreen: Screen? = null,
    onClearPendingNotificationScreen: () -> Unit = {},
    onLogout: () -> Unit = {},
    onRateApp: () -> Unit = {},
    scheme: ODSTheme = zonaODSTheme
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val initialScreen = pendingNotificationScreen ?: Screen.Models
    val backStack = rememberNavBackStack(initialScreen)
    val snackbarHostState = remember { SnackbarHostState() }
    var isOnboardingDone by remember { mutableStateOf(true) }
    var showExitWarningDialog by remember { mutableStateOf(false) }

    val initialNotifPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }
    var showNotificationPermissionSheet by remember { mutableStateOf(!initialNotifPermission) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                } else {
                    NotificationManagerCompat.from(context).areNotificationsEnabled()
                }
                if (granted) {
                    showNotificationPermissionSheet = false
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val callViewModel: CallViewModel = hiltViewModel()
    val callState by callViewModel.callState.collectAsState()
    val currentScreen = backStack.lastOrNull()
    val selectedIndex = remember(currentScreen) {
        val idx = bottomNavTabs.indexOfFirst { it.screen == currentScreen }
        if (idx >= 0) idx else 0
    }

    LaunchedEffect(pendingNotificationScreen) {
        pendingNotificationScreen?.let { targetScreen ->
            if (backStack.lastOrNull() != targetScreen) {
                if (targetScreen in bottomNavRoutes) {
                    if (backStack.isNotEmpty()) {
                        backStack[backStack.lastIndex] = targetScreen
                    } else {
                        backStack.add(targetScreen)
                    }
                } else {
                    backStack.add(targetScreen)
                }
            }
            onClearPendingNotificationScreen()
        }
    }

    val canHandleBack =
        backStack.size > 1 || (currentScreen != null && currentScreen != Screen.Models)

    BackHandler(enabled = canHandleBack) {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        } else if (currentScreen != Screen.Models) {
            backStack[0] = Screen.Models
        }
    }

    val current = backStack.lastOrNull()
    val showBottomBar = current in bottomNavRoutes

    val incomingCallPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val micGranted = permissions[Manifest.permission.RECORD_AUDIO] == true ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
        val isVideo = callState.callType == CallType.VIDEO
        val cameraGranted = if (isVideo) {
            permissions[Manifest.permission.CAMERA] == true ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
        } else true

        if (micGranted && cameraGranted) {
            callViewModel.acceptIncomingCall()
            val targetRoute = if (isVideo) {
                Screen.VideoCall(callState.remoteUserId, callState.remoteUserName)
            } else {
                Screen.VoiceCall(callState.remoteUserId, callState.remoteUserName)
            }
            backStack.add(targetRoute)
        } else {
            Toast.makeText(
                context,
                if (isVideo) "Camera and microphone permissions required to accept video call"
                else "Microphone permission required to accept call",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val navigateToChat: (modelId: String, modelName: String, conversationId: String?) -> Unit =
        { modelId, modelName, conversationId ->
            val existingIndex = backStack.indexOfLast { it is Screen.Chat && it.modelId == modelId }
            if (existingIndex >= 0) {
                while (backStack.lastIndex > existingIndex) {
                    backStack.removeLastOrNull()
                }
                if (conversationId != null) {
                    val current = backStack[existingIndex] as? Screen.Chat
                    if (current != null && current.conversationId != conversationId) {
                        backStack[existingIndex] = current.copy(conversationId = conversationId)
                    }
                }
            } else if (backStack.lastOrNull() is Screen.ProfileDetail && (backStack.lastOrNull() as Screen.ProfileDetail).userId == modelId) {
                backStack[backStack.lastIndex] = Screen.Chat(modelId, modelName, conversationId)
            } else {
                backStack.add(Screen.Chat(modelId, modelName, conversationId))
            }
        }

    val navigateToProfile: (userId: String, userName: String) -> Unit = { userId, userName ->
        val existingIndex =
            backStack.indexOfLast { it is Screen.ProfileDetail && it.userId == userId }
        if (existingIndex >= 0) {
            while (backStack.lastIndex > existingIndex) {
                backStack.removeLastOrNull()
            }
        } else {
            backStack.add(Screen.ProfileDetail(userId, userName))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = scheme.basicBackground.getColor(),
        bottomBar = {
            if (showBottomBar) {
                ScreenTimeBottomNavigation(
                    scheme = scheme,
                    selectedIndex = selectedIndex,
                    onTabSelected = { index ->
                        bottomNavTabs.getOrNull(index)?.screen?.let { route ->
                            val current2 = backStack.lastOrNull()
                            when (current2) {
                                route -> Unit
                                in bottomNavRoutes -> backStack[backStack.lastIndex] = route
                                else -> backStack.add(route)
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            ODSSnackbarHost(
                hostState = snackbarHostState,
                scheme = scheme
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                CompositionLocalProvider(LocalNavSharedTransitionScope provides this) {
                    NavDisplay(
                        modifier = Modifier.fillMaxSize(),
                        backStack = backStack,
                        transitionSpec = appForwardTransitionSpec(bottomNavRoutes),
                        popTransitionSpec = appPopTransitionSpec(bottomNavRoutes),
                        predictivePopTransitionSpec = appPredictivePopTransitionSpec(),
                        onBack = {
                            if (!isOnboardingDone) {
                                showExitWarningDialog = true
                                return@NavDisplay
                            }
                            val top = backStack.lastOrNull()
                            if (top in bottomNavRoutes && top != Screen.Models) {
                                backStack[backStack.lastIndex] = Screen.Models
                            } else if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        },
                        entryProvider = entryProvider {
                            entry<Screen.DatingPreferences> {
                                LaunchedEffect(Unit) {
                                    while (backStack.isNotEmpty()) {
                                        backStack.removeLastOrNull()
                                    }
                                    backStack.add(Screen.Models)
                                }
                            }

                            // ── Terms of Service ("Legal Energy") ────────────────────────
                            entry<Screen.TermsOfService> {
                                TermsOfServiceScreen(
                                    onBack = {
                                        if (backStack.size > 1) backStack.removeLastOrNull()
                                    }, scheme = scheme
                                )
                            }
                            entry<Screen.Models> {
                                ModelsTabScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onStartVoiceCall = { modelId, modelName, rate, avatarUrl ->
                                        callViewModel.resetState()
                                        callViewModel.startOutgoingCall(
                                            modelId = modelId,
                                            modelName = modelName,
                                            ratePerMin = rate,
                                            callType = CallType.VOICE
                                        )
                                        backStack.add(
                                            Screen.VoiceCall(
                                                modelId = modelId,
                                                modelName = modelName,
                                                avatarUrl = avatarUrl
                                            )
                                        )
                                    },
                                    onStartVideoCall = { modelId, modelName, rate, avatarUrl ->
                                        callViewModel.resetState()
                                        callViewModel.startOutgoingCall(
                                            modelId = modelId,
                                            modelName = modelName,
                                            ratePerMin = rate,
                                            callType = CallType.VIDEO
                                        )
                                        backStack.add(
                                            Screen.VideoCall(
                                                modelId = modelId,
                                                modelName = modelName,
                                                ratePerMin = rate,
                                                avatarUrl = avatarUrl
                                            )
                                        )
                                    },
                                    onNavigateToProfile = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    },
                                    onNavigateToSearch = {
                                        backStack.add(Screen.ModelSearch)
                                    },
                                    onNavigateToNotifications = {
                                        backStack.add(Screen.Notifications)
                                    }
                                )
                            }

                            // ── Screen: Frictionless Live Companion Search Screen ──────────
                            entry<Screen.ModelSearch> {
                                ModelsTabScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onStartVoiceCall = { modelId, modelName, rate, avatarUrl ->
                                        callViewModel.resetState()
                                        callViewModel.startOutgoingCall(
                                            modelId = modelId,
                                            modelName = modelName,
                                            ratePerMin = rate,
                                            callType = CallType.VOICE
                                        )
                                        backStack.add(
                                            Screen.VoiceCall(
                                                modelId = modelId,
                                                modelName = modelName,
                                                avatarUrl = avatarUrl
                                            )
                                        )
                                    },
                                    onStartVideoCall = { modelId, modelName, rate, avatarUrl ->
                                        callViewModel.resetState()
                                        callViewModel.startOutgoingCall(
                                            modelId = modelId,
                                            modelName = modelName,
                                            ratePerMin = rate,
                                            callType = CallType.VIDEO
                                        )
                                        backStack.add(
                                            Screen.VideoCall(
                                                modelId = modelId,
                                                modelName = modelName,
                                                ratePerMin = rate,
                                                avatarUrl = avatarUrl
                                            )
                                        )
                                    },
                                    onNavigateToProfile = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    }
                                )
                            }

                            // ── Tab 3: Conversations List ───────────────────────────────
                            entry<Screen.ChatList> {
                                ChatListScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onNavigateToChat = { modelId, modelName, conversationId ->
                                        navigateToChat(modelId, modelName, conversationId)
                                    },
                                    onNavigateToProfile = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    },
                                    onNavigateToDiscover = {
                                        backStack[backStack.lastIndex] = Screen.Models
                                    })
                            }

                            // ── Screen 3: 1-on-1 Chat ───────────────────────────────────
                            entry<Screen.Chat> { key ->
                                ChatScreen(
                                    modelId = key.modelId,
                                    modelName = key.modelName,
                                    conversationId = key.conversationId,
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onBackClick = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    onOpenProfile = {
                                        navigateToProfile(key.modelId, key.modelName)
                                    },
                                    onVoiceCallClick = {
                                        callViewModel.resetState()
                                        backStack.add(Screen.VoiceCall(key.modelId, key.modelName))
                                    },
                                    onVideoCallClick = {
                                        callViewModel.resetState()
                                        backStack.add(Screen.VideoCall(key.modelId, key.modelName))
                                    }
                                )
                            }

                            // ── Screen: Voice Call (LiveKit SFU) ─────────────────────────
                            entry<Screen.VoiceCall> { key ->
                                val activeCallState by callViewModel.callState.collectAsState()
                                LaunchedEffect(activeCallState.callType) {
                                    if (activeCallState.callType == com.app.screentime.feature.call.CallType.VIDEO &&
                                        backStack.isNotEmpty() && backStack.lastOrNull() is Screen.VoiceCall
                                    ) {
                                        backStack[backStack.lastIndex] = Screen.VideoCall(
                                            modelId = key.modelId,
                                            modelName = key.modelName,
                                            avatarUrl = key.avatarUrl
                                        )
                                    }
                                }
                                VoiceCallScreen(
                                    modelId = key.modelId,
                                    modelName = key.modelName,
                                    avatarUrl = key.avatarUrl,
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onBack = {
                                        callViewModel.resetState()
                                        if (backStack.lastOrNull() is Screen.VoiceCall) {
                                            if (backStack.size > 1) backStack.removeLastOrNull()
                                            else backStack[0] =
                                                Screen.Chat(key.modelId, key.modelName)
                                        }
                                    },
                                    onEndCall = {
                                        callViewModel.resetState()
                                        if (backStack.lastOrNull() is Screen.VoiceCall) {
                                            if (backStack.size > 1) backStack.removeLastOrNull()
                                            else backStack[0] =
                                                Screen.Chat(key.modelId, key.modelName)
                                        }
                                    }
                                )
                            }

                            // ── Screen: Video Call (LiveKit SFU) ─────────────────────────
                            entry<Screen.VideoCall> { key ->
                                VideoCallScreen(
                                    modelId = key.modelId,
                                    modelName = key.modelName,
                                    ratePerMin = key.ratePerMin,
                                    avatarUrl = key.avatarUrl,
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onEndCall = {
                                        callViewModel.resetState()
                                        if (backStack.lastOrNull() is Screen.VideoCall) {
                                            if (backStack.size > 1) backStack.removeLastOrNull()
                                            else backStack[0] =
                                                Screen.Chat(key.modelId, key.modelName)
                                        }
                                    }
                                )
                            }

                            // ── Screen 4: User Profile Detail ─────────────────────────
                            entry<Screen.ProfileDetail> { key ->
                                ProfileDetailScreen(
                                    userId = key.userId,
                                    userName = key.userName,
                                    isMyProfile = key.userId == "me",
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    onNavigateToChat = { modelId, modelName ->
                                        navigateToChat(modelId, modelName, null)
                                    },
                                    onNavigateToEditProfile = {
                                        backStack.add(Screen.EditProfile)
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme
                                )
                            }

                            // ── Earnings Tab (Redesigned Creator Earnings & Payouts) ────────
                            entry<Screen.Wallet> {
                                CreatorEarningsScreen(
                                    onNavigateToStartEarning = { backStack.add(Screen.StartEarning) },
                                    onNavigateToTransactions = { backStack.add(Screen.Transactions) },
                                    scheme = scheme
                                )
                            }

                            entry<Screen.AddFunds> {
                                AddFundsScreen(
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    onNavigateToTransactions = { backStack.add(Screen.Transactions) },
                                    scheme = scheme
                                )
                            }

                            // ── Transactions History ────────────────────────────────────
                            entry<Screen.Transactions> {
                                TransactionsScreen(
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    scheme = scheme
                                )
                            }

                            // ── Edit Profile ────────────────────────────────────────────
                            entry<Screen.EditProfile> {
                                EditProfileScreen(
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    scheme = scheme
                                )
                            }

                            // ── Edit Preferences (Figma node-id 84-1040) ─────────────────
                            entry<Screen.EditPreferences> {
                                EditPreferencesScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }
                                )
                            }

                            // ── Notifications Screen ────────────────────────────────────
                            entry<Screen.Notifications> {
                                NotificationsScreen(
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    onNavigateToChat = { modelId, modelName ->
                                        navigateToChat(modelId, modelName, null)
                                    },
                                    onNavigateToProfile = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    },
                                    scheme = scheme
                                )
                            }

                            entry<Screen.Profile> {
                                ProfileDetailScreen(
                                    userId = "me",
                                    userName = "My Profile",
                                    isMyProfile = true,
                                    onBack = {
                                        if (backStack.size > 1) backStack.removeLastOrNull()
                                        else backStack[0] = Screen.Models
                                    },
                                    onNavigateToChat = { modelId, modelName ->
                                        navigateToChat(modelId, modelName, null)
                                    },
                                    onNavigateToEditProfile = {
                                        backStack.add(Screen.EditProfile)
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme
                                )
                            }

                            entry<Screen.Account> {
                                UserProfileScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onBackClick = {
                                        if (backStack.contains(Screen.Models)) {
                                            while (backStack.lastOrNull() != Screen.Models && backStack.size > 1) {
                                                backStack.removeLastOrNull()
                                            }
                                        } else if (backStack.isNotEmpty()) {
                                            backStack[backStack.lastIndex] = Screen.Models
                                        } else {
                                            backStack.add(Screen.Models)
                                        }
                                    },
                                    onLogoutClick = onLogout,
                                    onRateAppClick = onRateApp,
                                    onNavigateToEditProfile = { backStack.add(Screen.EditProfile) },
                                    onNavigateToLikedProfiles = { backStack.add(Screen.Likes) },
                                    onNavigateToWhoLikedMe = { backStack.add(Screen.WhoLikedMe) },
                                    onNavigateToMatches = { backStack.add(Screen.Matches) },
                                    onNavigateToPreferences = { backStack.add(Screen.EditPreferences) },
                                    onNavigateToNotifications = { backStack.add(Screen.Notifications) },
                                    onNavigateToControlAccount = { backStack.add(Screen.ControlAccount) },
                                    onNavigateToStartEarning = { backStack.add(Screen.StartEarning) },
                                    onNavigateToWallet = { backStack.add(Screen.Wallet) },
                                    onNavigateToTransactions = { backStack.add(Screen.Transactions) },
                                    onNavigateToProfileDetail = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    })
                            }

                            entry<Screen.StartEarning> {
                                com.app.screentime.feature.profile.StartEarningScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onBackClick = {
                                        backStack.removeLastOrNull()
                                    }
                                )
                            }

                            // ── Likes Hub Screen (Who Liked Me & Whom I Like with HorizontalPager + ODSTabs) ──
                            entry<Screen.Likes> {
                                LikesHubScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    initialTab = 0,
                                    onNavigateToProfile = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    },
                                    onNavigateToChat = { modelId, modelName ->
                                        navigateToChat(modelId, modelName, null)
                                    },
                                    onNavigateToDiscover = {
                                        backStack[backStack.lastIndex] = Screen.Models
                                    }
                                )
                            }

                            entry<Screen.WhoLikedMe> {
                                LikesHubScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    initialTab = 0,
                                    onNavigateToProfile = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    },
                                    onNavigateToChat = { modelId, modelName ->
                                        navigateToChat(modelId, modelName, null)
                                    },
                                    onNavigateToDiscover = {
                                        backStack[backStack.lastIndex] = Screen.Models
                                    }
                                )
                            }

                            // ── Matches Screen ──────────────────────────────────────────
                            entry<Screen.Matches> {
                                MatchesScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    scheme = scheme,
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    onNavigateToProfile = { userId, userName ->
                                        navigateToProfile(userId, userName)
                                    },
                                    onNavigateToChat = { modelId, modelName ->
                                        navigateToChat(modelId, modelName, null)
                                    },
                                    onNavigateToDiscover = {
                                        backStack[backStack.lastIndex] = Screen.Models
                                    }
                                )
                            }

                            entry<Screen.ControlAccount> {
                                ControlAccountScreen(
                                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                    onLogout = onLogout,
                                    scheme = scheme
                                )
                            }
                        })
                }
            }

            if (callState.status == CallStatus.INCOMING) {
                IncomingCallGlobalOverlay(
                    callState = callState,
                    isModel = callViewModel.isCurrentUserModel(),
                    scheme = scheme,
                    onAccept = {
                        val isVideo = callState.callType == CallType.VIDEO
                        val hasMic = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                        val hasCam = if (isVideo) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        } else true

                        if (hasMic && hasCam) {
                            callViewModel.acceptIncomingCall()
                            val targetRoute = if (isVideo) {
                                Screen.VideoCall(callState.remoteUserId, callState.remoteUserName)
                            } else {
                                Screen.VoiceCall(callState.remoteUserId, callState.remoteUserName)
                            }
                            backStack.add(targetRoute)
                        } else {
                            val perms = mutableListOf(Manifest.permission.RECORD_AUDIO)
                            if (isVideo) {
                                perms.add(Manifest.permission.CAMERA)
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val hasNotif = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                                if (!hasNotif) {
                                    perms.add(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                            incomingCallPermissionLauncher.launch(perms.toTypedArray())
                        }
                    },
                    onDecline = {
                        callViewModel.rejectIncomingCall()
                    }
                )
            }

            // ── Exit Warning Dialog when profile/preferences not completed ────────
            if (showExitWarningDialog) {
                ODSDialog(
                    scheme = scheme,
                    onDismissRequest = { showExitWarningDialog = false },
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    props = ODSDialogProps(
                        showCloseButton = true,
                        title = "Exit Profile Setup?",
                        bodyText = "Please complete your preferences to start discovering matches on Winter. If you leave now, you cannot browse matches until setup is completed."
                    ),
                    actionSlot = {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = 8.dp
                        ) {
                            ODSButton(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSButtonProps(
                                    label = "Continue Setup",
                                    buttonType = ODSButtonButtonType.STANDARD,
                                    variant = ODSButtonVariant.PRIMARY,
                                    size = ODSButtonSize.SMALL
                                ),
                                onClick = {
                                    showExitWarningDialog = false
                                }
                            )
                            ODSButton(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSButtonProps(
                                    label = "Exit App",
                                    buttonType = ODSButtonButtonType.STANDARD,
                                    variant = ODSButtonVariant.SECONDARY,
                                    size = ODSButtonSize.SMALL
                                ),
                                onClick = {
                                    showExitWarningDialog = false
                                    (context as? android.app.Activity)?.finishAffinity()
                                }
                            )
                        }
                    }
                )
            }

            if (showNotificationPermissionSheet) {
                NotificationPermissionBottomSheet(
                    scheme = scheme,
                    onDismiss = { showNotificationPermissionSheet = false },
                    onPermissionGranted = { showNotificationPermissionSheet = false }
                )
            }

            val prefsManager = remember(context) { com.app.screentime.core.network.preferences.PreferencesManager(context.applicationContext) }
            var showGenderDialog by remember {
                mutableStateOf(!prefsManager.isGenderAsked() && prefsManager.getUserGender().isNullOrBlank())
            }

            if (showGenderDialog && (current == Screen.Models || current == null)) {
                com.app.screentime.feature.newdesign.dialogs.WinterGenderDialog(
                    onDismissRequest = {
                        prefsManager.setGenderAsked(true)
                        showGenderDialog = false
                    },
                    onContinue = { option ->
                        val genderStr = if (option == com.app.screentime.feature.newdesign.dialogs.GenderOption.MAN) "male" else "female"
                        prefsManager.setUserGender(genderStr)
                        prefsManager.setGenderAsked(true)
                        showGenderDialog = false
                    }
                )
            }
        }
    }
}