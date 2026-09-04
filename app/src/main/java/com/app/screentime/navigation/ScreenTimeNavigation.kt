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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.app.screentime.feature.call.ActiveCallGlobalBanner
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallViewModel
import com.app.screentime.feature.call.IncomingCallGlobalOverlay
import com.app.screentime.feature.call.LiveListScreen
import com.app.screentime.feature.call.LiveStreamHostScreen
import com.app.screentime.feature.call.LiveStreamViewerScreen
import com.app.screentime.feature.call.VideoCallScreen
import com.app.screentime.feature.call.VoiceCallScreen
import com.app.screentime.feature.chat.ChatListScreen
import com.app.screentime.feature.chat.ChatScreen
import com.app.screentime.feature.discover.DiscoverScreen
import com.app.screentime.feature.discover.ModelProfileScreen
import com.app.screentime.feature.discover.SocialSpaceDemoScreen
import com.app.screentime.feature.profile.UserProfileScreen
import com.app.screentime.feature.wallet.WalletScreen
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

// Bottom nav tab definition
data class BottomNavTab(
    val screen: Screen,
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
)

val bottomNavTabs = listOf(
    BottomNavTab(
        Screen.Discover, "Discover", R.drawable.discovery, R.drawable.discovery
    ),
    BottomNavTab(
        Screen.LiveList, "Live", R.drawable.video, R.drawable.video
    ),
    BottomNavTab(
        Screen.ChatList, "Chats", R.drawable.message, R.drawable.message
    ),
    BottomNavTab(
        Screen.Wallet, "Wallet", R.drawable.wallet, R.drawable.wallet
    ),
    BottomNavTab(Screen.Profile, "Profile", R.drawable.profile, R.drawable.profile),
)

private val bottomNavRoutes: Set<Screen> = setOf(
    Screen.Discover, Screen.LiveList, Screen.ChatList, Screen.Wallet, Screen.Profile
)

/**
 * Root navigation composable for Chatty.
 * Shows elevated floating bottom nav on main tab screens, smoothly auto-hides on scroll down and reveals on scroll up.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    modifier: Modifier = Modifier,
    deeplinkUri: Uri? = null,
    incomingCall: Pair<String, String>? = null,
    onClearIncomingCall: () -> Unit = {},
    onLogout: () -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    isInPipMode: Boolean = false,
    callViewModel: CallViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(Screen.Discover)
    var selectedIndex by remember { mutableIntStateOf(0) }
    val callState by callViewModel.callState.collectAsState()
    val isModel = callViewModel.isCurrentUserModel()

    val currentScreen = backStack.lastOrNull()
    val isAlreadyOnCallScreen = currentScreen is Screen.VoiceCall

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
        backStack.size > 1 || (currentScreen != null && currentScreen != Screen.Discover)

    BackHandler(enabled = canHandleBack && !isInPipMode) {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        } else if (currentScreen != Screen.Discover) {
            backStack[0] = Screen.Discover
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        NavDisplay(modifier = Modifier.fillMaxSize(), backStack = backStack, onBack = {
            val top = backStack.lastOrNull()
            if (top in bottomNavRoutes && top != Screen.Discover) {
                backStack[backStack.lastIndex] = Screen.Discover
            } else if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        }, entryProvider = entryProvider {
            entry<Screen.Discover> {
                DiscoverScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onNavigateToModelProfile = { modelId ->
                        backStack.add(Screen.ModelProfile(modelId, ""))
                    },
                    onNavigateToSocialDemo = {
                        backStack.add(Screen.SocialSpaceDemo)
                    })
            }

            // ── Chat List ─────────────────────────────────────────────────
            entry<Screen.ChatList> {
                ChatListScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onNavigateToChat = { modelId, modelName ->
                        backStack.add(Screen.Chat(modelId, modelName))
                    })
            }

            // ── Wallet ────────────────────────────────────────────────────
            entry<Screen.Wallet> {
                WalletScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme
                )
            }

            // ── Profile ───────────────────────────────────────────────────
            entry<Screen.Profile> {
                UserProfileScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onLogoutClick = onLogout,
                    onNavigateToTopUp = { backStack.add(Screen.Wallet) }
                )
            }

            // ── Model Profile ─────────────────────────────────────────────
            entry<Screen.ModelProfile> { key ->
                ModelProfileScreen(
                    modelId = key.modelId,
                    modelName = key.modelName,
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onBackClick = { if (backStack.size > 1) backStack.removeLastOrNull() },
                    onStartChat = { id, name ->
                        backStack.add(Screen.Chat(id, name))
                    },
                    onStartVoiceCall = { id, name ->
                        backStack.add(Screen.VoiceCall(id, name))
                    },
                    onStartVideoCall = { id, name ->
                        backStack.add(Screen.VideoCall(id, name))
                    })
            }

            // ── Chat ──────────────────────────────────────────────────────
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
                    })
            }

            // ── Voice Call ────────────────────────────────────────────────
            entry<Screen.VoiceCall> { key ->
                VoiceCallScreen(
                    modelId = key.modelId,
                    modelName = key.modelName,
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    isInPipMode = isInPipMode,
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

            // ── Video Call ────────────────────────────────────────────────
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
                    }
                )
            }

            // ── Live Streams List (Bottom Nav Tab) ─────────────────────────
            entry<Screen.LiveList> {
                LiveListScreen(
                    onNavigateToViewer = { streamId, hostId, hostName, hostAvatar ->
                        backStack.add(Screen.LiveViewer(streamId, hostId, hostName, hostAvatar))
                    },
                    onNavigateToHost = {
                        backStack.add(Screen.LiveHost())
                    },
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme
                )
            }

            // ── Live Stream Viewer ─────────────────────────────────────────
            entry<Screen.LiveViewer> { key ->
                LiveStreamViewerScreen(
                    streamId = key.streamId,
                    hostId = key.hostId,
                    hostName = key.hostName,
                    hostAvatar = key.hostAvatar,
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onExit = { if (backStack.size > 1) backStack.removeLastOrNull() }
                )
            }

            // ── Live Stream Host ───────────────────────────────────────────
            entry<Screen.LiveHost> { key ->
                LiveStreamHostScreen(
                    streamTitle = key.streamTitle,
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onEndStream = { if (backStack.size > 1) backStack.removeLastOrNull() }
                )
            }

            // ── Social Space Showcase Demo ────────────────────────────────
            entry<Screen.SocialSpaceDemo> {
                SocialSpaceDemoScreen(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    onBackClick = { if (backStack.size > 1) backStack.removeLastOrNull() })
            }
        })

        val current = backStack.lastOrNull()
        AnimatedVisibility(
            visible = current in bottomNavRoutes && !isInPipMode,
            enter = slideInVertically { it * 2 } + fadeIn(),
            exit = slideOutVertically { it * 2 } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)) {
            LunaBottomBar(
                scheme = scheme, selectedIndex = selectedIndex, onTabSelected = { index ->
                    val route = bottomNavTabs.getOrNull(index)?.screen ?: return@LunaBottomBar
                    val current2 = backStack.lastOrNull()
                    when (current2) {
                        route -> Unit
                        in bottomNavRoutes -> backStack[backStack.lastIndex] = route
                        else -> backStack.add(route)
                    }
                })
        }

        AnimatedVisibility(
            visible = callState.status == CallStatus.ACTIVE && !isAlreadyOnCallScreen && !isInPipMode,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)) {
            ActiveCallGlobalBanner(callState = callState, scheme = scheme, onExpand = {
                if (callState.callType == com.app.screentime.feature.call.CallType.VIDEO) {
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

        // ── Global Incoming Call Overlay (Shown on ANY screen when an incoming call arrives) ──
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
                    if (callState.callType == com.app.screentime.feature.call.CallType.VIDEO) {
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

@Composable
private fun LunaBottomBar(
    scheme: ODSTheme, selectedIndex: Int, onTabSelected: (Int) -> Unit
) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = HexColor(0xf21e1145))), // Floating translucent container
        cornerRadius = ODSCorners(all = 40.dp),
        border = ODSBorder(
            width = 1.dp, colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavTabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index
                ODSBox(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(index) }), background = listOf(
                        ODSColorModel(
                            hexColor = if (isSelected) scheme.basicAccent else HexColor(0x00000000)
                        )
                    ), cornerRadius = ODSCorners(all = 24.dp), padding = ODSPadding(
                        horizontal = if (isSelected) 16.dp else 12.dp, vertical = 8.dp
                    )
                ) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically, gap = 6.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = tab.selectedIcon),
                            tint = if (isSelected) scheme.basicTextOnAccent.getColor() else scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(20.dp)
                        )
                        if (isSelected) {
                            ODSText(
                                text = tab.label,
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicTextOnAccent
                            )
                        }
                    }
                }
            }
        }
    }
}
