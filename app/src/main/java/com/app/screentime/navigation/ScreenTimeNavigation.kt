package com.app.screentime.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.call.ActiveCallGlobalBanner
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallType
import com.app.screentime.feature.call.CallViewModel
import com.app.screentime.feature.call.IncomingCallGlobalOverlay
import com.app.screentime.feature.call.VideoCallScreen
import com.app.screentime.feature.call.VoiceCallScreen
import com.app.screentime.feature.chat.ChatListScreen
import com.app.screentime.feature.chat.ChatScreen
import com.app.screentime.feature.discover.DiscoverMapScreen
import com.app.screentime.feature.discover.HomeFeedScreen
import com.app.screentime.feature.discover.TermsOfServiceScreen
import com.app.screentime.feature.preferences.PreferencesScreen
import com.app.screentime.feature.profile.EditProfileScreen
import com.app.screentime.feature.profile.NotificationsScreen
import com.app.screentime.feature.profile.ProfileDetailScreen
import com.app.screentime.feature.profile.UserProfileScreen
import com.app.screentime.feature.wallet.AddFundsScreen
import com.app.screentime.feature.wallet.TransactionsScreen
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

// ── 4 Main Bottom Navigation Tabs: Home, Discover, Chats, Profile ────────────
data class BottomNavTab(
    val screen: Screen,
    val label: String,
    val imageVector: ImageVector? = null,
    @DrawableRes val iconRes: Int? = null
)

val bottomNavTabs = listOf(
    BottomNavTab(Screen.Home, "Home", iconRes = R.drawable.ic_home),
    BottomNavTab(Screen.DiscoverMap, "Discover", iconRes = R.drawable.ic_map_pin),
    BottomNavTab(Screen.ChatList, "Chats", iconRes = R.drawable.ic_message_circle),
    BottomNavTab(Screen.Account, "Profile", iconRes = R.drawable.ic_user)
)

private val bottomNavRoutes: Set<Screen> = setOf(
    Screen.Home, Screen.DiscoverMap, Screen.ChatList, Screen.Account
)

/**
 * Root navigation composable for Chatty Dating App.
 * Manages Onboarding Preferences, Discovery Deck, Chats, Audio/Video WebRTC Calls, and Flat Bottom Bar.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    modifier: Modifier = Modifier,
    deeplinkUri: Uri? = null,
    incomingCall: Pair<String, String>? = null,
    onClearIncomingCall: () -> Unit = {},
    onLogout: () -> Unit = {},
    scheme: ODSTheme = zonaODSTheme,
    isInPipMode: Boolean = false,
    callViewModel: CallViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(Screen.Home, Screen.DatingPreferences)
    var selectedIndex by remember { mutableIntStateOf(0) } // Default active on Home (index 0)
    val callState by callViewModel.callState.collectAsState()
    val isModel = callViewModel.isCurrentUserModel()

    val currentScreen = backStack.lastOrNull()
    val isAlreadyOnCallScreen =
        currentScreen is Screen.VoiceCall || currentScreen is Screen.VideoCall

    LaunchedEffect(incomingCall) {
        incomingCall?.let { (callerId, callerName) ->
            if (currentScreen !is Screen.VoiceCall) {
                backStack.add(Screen.VoiceCall(callerId, callerName))
            }
            onClearIncomingCall()
        }
    }

    LaunchedEffect(backStack.toList()) {
        val top = backStack.lastOrNull()
        val idx = bottomNavTabs.indexOfFirst { it.screen == top }
        selectedIndex = if (idx >= 0) idx else -1
    }

    val canHandleBack =
        backStack.size > 1 || (currentScreen != null && currentScreen != Screen.Home)

    BackHandler(enabled = canHandleBack && !isInPipMode) {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        } else if (currentScreen != Screen.Home) {
            backStack[0] = Screen.Home
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        NavDisplay(modifier = Modifier.fillMaxSize(), backStack = backStack, onBack = {
            val top = backStack.lastOrNull()
            if (top in bottomNavRoutes && top != Screen.Home) {
                backStack[backStack.lastIndex] = Screen.Home
            } else if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        }, entryProvider = entryProvider {
            entry<Screen.DatingPreferences> {
                PreferencesScreen (modifier = Modifier.fillMaxSize(), scheme = scheme, onBack = {
                    if (backStack.size > 1) backStack.removeLastOrNull()
                }, onComplete = {
                    backStack.removeLastOrNull()
                    if (backStack.lastOrNull() != Screen.Home) {
                        backStack.add(Screen.Home)
                    }
                })
            }

            entry<Screen.Home> {
                HomeFeedScreen(
                    modifier = modifier,
                    scheme = scheme,
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    },
                    onNavigateToProfile = { userId, userName ->
                        backStack.add(Screen.ProfileDetail(userId, userName))
                    },
                    onNavigateToPreferences = {
                        backStack.add(Screen.DatingPreferences)
                    },
                )
            }

            // ── Legacy Discover alias (kept for backward compat) ─────────
            entry<Screen.Discover> {
                HomeFeedScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    },
                    onNavigateToProfile = { userId, userName ->
                        backStack.add(Screen.ProfileDetail(userId, userName))
                    },
                    onNavigateToPreferences = {
                        backStack.add(Screen.DatingPreferences)
                    },
                )
            }

            // ── Tab 2: Location-Based Discovery Map ─────────────────────
            entry<Screen.DiscoverMap> {
                DiscoverMapScreen(
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    }, onNavigateToList = {
                        backStack[backStack.lastIndex] = Screen.Home
                    }, onNavigateToProfile = { userId, userName ->
                        backStack.add(Screen.ProfileDetail(userId, userName))
                    }, onOpenTerms = {
                        backStack.add(Screen.TermsOfService)
                    }, modifier = Modifier.fillMaxSize(), scheme = scheme
                )
            }

            // ── Terms of Service ("Legal Energy") ────────────────────────
            entry<Screen.TermsOfService> {
                TermsOfServiceScreen(
                    onBack = {
                        if (backStack.size > 1) backStack.removeLastOrNull()
                    }, scheme = scheme
                )
            }


            // ── Tab 3: Conversations List ───────────────────────────────
            entry<Screen.ChatList> {
                ChatListScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    },
                    onNavigateToProfile = { userId, userName ->
                        backStack.add(Screen.ProfileDetail(userId, userName))
                    },
                    onNavigateToDiscover = {
                        backStack[backStack.lastIndex] = Screen.Home
                    })
            }

            // ── Screen 3: 1-on-1 Chat with Anastasia ───────────────────
            entry<Screen.Chat> { key ->
                ChatScreen(
                    modelId = key.modelId,
                    modelName = key.modelName,
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onBackClick = { if (backStack.size > 1) backStack.removeLastOrNull() },
                    onStartVoiceCall = {
                        backStack.add(Screen.VoiceCall(key.modelId, key.modelName))
                    },
                    onStartVideoCall = {
                        backStack.add(Screen.VideoCall(key.modelId, key.modelName))
                    },
                    onOpenProfile = {
                        backStack.add(Screen.ProfileDetail(key.modelId, key.modelName))
                    })
            }

            // ── Screen 4: User Profile Detail ("Jessica's Profile") ─────
            entry<Screen.ProfileDetail> { key ->
                ProfileDetailScreen(
                    userId = key.userId,
                    userName = key.userName,
                    isMyProfile = key.userId == "me" || key.userId == "jessica_maple",
                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    },
                    onNavigateToAccount = {
                        backStack.add(Screen.Account)
                    },
                    onNavigateToEditProfile = {
                        backStack.add(Screen.EditProfile)
                    },
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme
                )
            }

            // ── Voice Call (WebRTC 1-on-1 Audio) ────────────────────────
            entry<Screen.VoiceCall> { key ->
                VoiceCallScreen(
                    modelId = key.modelId,
                    modelName = key.modelName,
                    avatarUrl = key.avatarUrl,
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    isInPipMode = isInPipMode,
                    onBack = {
                        callViewModel.resetState()
                        if (backStack.size > 1) backStack.removeLastOrNull()
                    },
                    onEndCall = {
                        callViewModel.resetState()
                        if (backStack.size > 1) backStack.removeLastOrNull()
                    },
                    onNavigateToTopUp = {
                        callViewModel.resetState()
                        if (backStack.size > 1) backStack.removeLastOrNull()
                        backStack.add(Screen.Wallet)
                    })
            }

            // ── Video Call (WebRTC 1-on-1 Video) ────────────────────────
            entry<Screen.VideoCall> { key ->
                VideoCallScreen(
                    modelId = key.modelId,
                    modelName = key.modelName,
                    modifier = Modifier.fillMaxSize(),
                    ratePerMin = key.ratePerMin,
                    avatarUrl = key.avatarUrl,
                    scheme = scheme,
                    onEndCall = {
                        callViewModel.resetState()
                        if (backStack.size > 1) backStack.removeLastOrNull()
                    },
                    onNavigateToTopUp = {
                        callViewModel.resetState()
                        if (backStack.size > 1) backStack.removeLastOrNull()
                        backStack.add(Screen.Wallet)
                    })
            }

            // ── Wallet & Recharge ("Add Funds") ─────────────────────────
            entry<Screen.Wallet> {
                AddFundsScreen(
                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                    scheme = scheme
                )
            }

            entry<Screen.AddFunds> {
                AddFundsScreen(
                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
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

            // ── Notifications Screen ────────────────────────────────────
            entry<Screen.Notifications> {
                NotificationsScreen(
                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    },
                    onNavigateToProfile = { userId, userName ->
                        backStack.add(Screen.ProfileDetail(userId, userName))
                    },
                    scheme = scheme
                )
            }

            // ── Tab 4: User Profile ("Jessica's Profile") ──────────────
            entry<Screen.Profile> {
                ProfileDetailScreen(
                    userId = "jessica_maple",
                    userName = "Jessica Maple",
                    isMyProfile = true,
                    onBack = {
                        if (backStack.size > 1) backStack.removeLastOrNull()
                        else backStack[0] = Screen.Home
                    },
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    },
                    onNavigateToAccount = {
                        backStack.add(Screen.Account)
                    },
                    onNavigateToEditProfile = {
                        backStack.add(Screen.EditProfile)
                    },
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme
                )
            }

            // ── My Account & Zona Wallet Screen ─────────────────────────
            entry<Screen.Account> {
                UserProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onLogoutClick = onLogout,
                    onNavigateToTopUp = { backStack.add(Screen.AddFunds) },
                    onNavigateToTransactions = { backStack.add(Screen.Transactions) },
                    onNavigateToEditProfile = { backStack.add(Screen.EditProfile) },
                    onNavigateToNotifications = { backStack.add(Screen.Notifications) },
                    onNavigateToProfileDetail = { userId, userName ->
                        backStack.add(Screen.ProfileDetail(userId, userName))
                    })
            }
        })

        val current = backStack.lastOrNull()
        AnimatedVisibility(
            visible = current in bottomNavRoutes,
            enter = slideInVertically { it * 2 } + fadeIn(),
            exit = slideOutVertically { it * 2 } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()) {
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
                })
        }

        // ── Global Active Call Banner ────────────────────────────────────────
        AnimatedVisibility(
            visible = callState.status == CallStatus.ACTIVE && !isAlreadyOnCallScreen && !isInPipMode,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)) {
            ActiveCallGlobalBanner(callState = callState, scheme = scheme, onExpand = {
                if (callState.callType == CallType.VIDEO) {
                    backStack.add(
                        Screen.VideoCall(
                            modelId = callState.remoteUserId.ifBlank { "unknown" },
                            modelName = callState.remoteUserName.ifBlank { "Caller" },
                            ratePerMin = callState.ratePerMin
                        )
                    )
                } else {
                    backStack.add(
                        Screen.VoiceCall(
                            callState.remoteUserId.ifBlank { "unknown" },
                            callState.remoteUserName.ifBlank { "Caller" })
                    )
                }
            }, onEndCall = {
                callViewModel.endCall("Ended from banner")
            }, onToggleMute = {
                callViewModel.toggleMute()
            })
        }

        // ── Global Incoming Call Overlay ─────────────────────────────────────
        AnimatedVisibility(
            visible = callState.status == CallStatus.INCOMING && !isAlreadyOnCallScreen && !isInPipMode,
            enter = fadeIn() + slideInVertically { -it / 2 },
            exit = fadeOut() + slideOutVertically { -it / 2 }) {
            IncomingCallGlobalOverlay(
                callState = callState,
                isModel = isModel,
                scheme = scheme,
                onAccept = {
                    callViewModel.acceptIncomingCall()
                    if (callState.callType == CallType.VIDEO) {
                        backStack.add(
                            Screen.VideoCall(
                                modelId = callState.remoteUserId.ifBlank { "unknown" },
                                modelName = callState.remoteUserName.ifBlank { "Caller" },
                                ratePerMin = callState.ratePerMin
                            )
                        )
                    } else {
                        backStack.add(
                            Screen.VoiceCall(
                                callState.remoteUserId.ifBlank { "unknown" },
                                callState.remoteUserName.ifBlank { "Caller" })
                        )
                    }
                },
                onDecline = {
                    callViewModel.rejectIncomingCall()
                })
        }
    }
}