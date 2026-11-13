package com.app.screentime.feature.newdesign.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallType
import com.app.screentime.feature.call.CallViewModel
import com.app.screentime.feature.newdesign.auth.WinterWelcomeAuthScreen
import com.app.screentime.feature.newdesign.call.WinterAudioCallScreen
import com.app.screentime.feature.newdesign.call.WinterVideoCallScreen
import com.app.screentime.feature.newdesign.chats.WinterChatThreadScreen
import com.app.screentime.feature.newdesign.components.WinterNavItem
import com.app.screentime.feature.newdesign.components.WinterTheme
import com.app.screentime.feature.newdesign.components.WinterTokens
import com.app.screentime.feature.newdesign.dialogs.GenderOption
import com.app.screentime.feature.newdesign.dialogs.WinterGenderDialog
import com.app.screentime.feature.newdesign.models.WinterFindingModelScreen
import com.app.screentime.feature.newdesign.profile.CreatorProfileDetails
import com.app.screentime.feature.newdesign.profile.WinterModelProfileScreen
import com.app.screentime.feature.newdesign.profile.WinterModelSetupScreen
import com.app.screentime.feature.newdesign.profile.WinterProfileDetailScreen
import com.app.screentime.feature.newdesign.wallet.WinterRechargeCoinsScreen
import com.app.screentime.feature.newdesign.wallet.WinterRechargeProcessingScreen
import kotlinx.serialization.Serializable

/**
 * Navigation 3 Keys for the new Winter Design System.
 */
@Serializable
sealed class WinterNav3Screen : NavKey {

    @Serializable
    data object Welcome : WinterNav3Screen()

    /**
     * Parent screen hosting the main tab destinations (Discover, Wallet, Earnings, Chats, Calls, Profile)
     * with a single, shared [WinterFloatingNavigation].
     */
    @Serializable
    data class Main(
        val initialTab: String = "DISCOVER"
    ) : WinterNav3Screen()

    @Serializable
    data object Models : WinterNav3Screen()

    @Serializable
    data class ProfileDetail(
        val creatorId: String = "",
        val creatorName: String = "Elena Rostova",
        val creatorHandle: String = "@elena_dance"
    ) : WinterNav3Screen()

    @Serializable
    data object Chats : WinterNav3Screen()

    @Serializable
    data class ChatThread(
        val recipientName: String = "Ananya Sharma"
    ) : WinterNav3Screen()

    @Serializable
    data object Wallet : WinterNav3Screen()

    @Serializable
    data object RechargeCoins : WinterNav3Screen()

    @Serializable
    data class RechargeProcessing(
        val packageId: String = "",
        val packCoins: String = "490",
        val packPrice: String = "₹49",
        val currentBalance: String = ""
    ) : WinterNav3Screen()

    @Serializable
    data object FindingModel : WinterNav3Screen()

    @Serializable
    data object Earnings : WinterNav3Screen()

    @Serializable
    data object UserSpace : WinterNav3Screen()

    @Serializable
    data object ModelProfile : WinterNav3Screen()

    @Serializable
    data object ModelSetup : WinterNav3Screen()

    @Serializable
    data class AudioCall(
        val callerName: String = "Ananya Sharma"
    ) : WinterNav3Screen()

    @Serializable
    data class VideoCall(
        val callerName: String = "Ananya Sharma"
    ) : WinterNav3Screen()
}

/**
 * WinterNav3Navigation - Navigation 3 Root Graph for all new Figma screens.
 *
 * Implements a backstack with smooth routing across all designed screens:
 * - Welcome / Auth Gate (`Welcome`)
 * - Main Parent Container (`Main`) hosting Discover, Wallet, Earnings, Chats, Calls, and Profile
 *   with ONE single shared [WinterFloatingNavigation]
 * - Creator Profile Details (`ProfileDetail`)
 * - Direct 1-on-1 Chat Thread (`ChatThread` - Node 149:7)
 * - Buy / Recharge Coins (`RechargeCoins` - Node 176:2)
 * - Recharge Processing (`RechargeProcessing` - Node 176:7)
 * - Finding a Model Radar (`FindingModel` - Node 176:12)
 * - Model Profile (`ModelProfile` - Node 134:9)
 * - Audio Call (`AudioCall` - Node 149:11)
 * - Video Call (`VideoCall` - Node 149:15)
 *
 * Includes an interactive quick switcher header so you can preview any screen instantly.
 */
@Composable
fun WinterNav3Navigation(
    modifier: Modifier = Modifier,
    initialScreen: WinterNav3Screen = WinterNav3Screen.Welcome,
    onSignOut: () -> Unit = {},
    callViewModel: CallViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(initialScreen)
    var showQuickSwitcher by remember { mutableStateOf(false) }

    val callState by callViewModel.callState.collectAsStateWithLifecycle()
    val room by callViewModel.roomFlow.collectAsStateWithLifecycle()
    val remoteVideoTrack by callViewModel.remoteVideoTrack.collectAsStateWithLifecycle()
    val localVideoTrack by callViewModel.localVideoTrack.collectAsStateWithLifecycle()

    val currentScreen = backStack.lastOrNull() ?: initialScreen

    // Handle Android system back button
    BackHandler(enabled = backStack.size > 1) {
        val top = backStack.lastOrNull()
        if (top is WinterNav3Screen.AudioCall || top is WinterNav3Screen.VideoCall) {
            callViewModel.endCall("User navigated back")
            callViewModel.resetState()
        }
        backStack.removeLastOrNull()
    }

    // Auto-handle Insufficient Balance or Call End events
    LaunchedEffect(callState.status) {
        if (callState.status == CallStatus.INSUFFICIENT_BALANCE) {
            callViewModel.resetState()
            backStack.add(WinterNav3Screen.RechargeCoins)
        } else if (callState.status == CallStatus.ENDED) {
            val top = backStack.lastOrNull()
            if (top is WinterNav3Screen.AudioCall || top is WinterNav3Screen.VideoCall) {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            }
        }
    }

    WinterTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(WinterTokens.backgroundCanvas)
        ) {
            // Navigation 3 Host
            NavDisplay(
                modifier = Modifier.fillMaxSize(),
                backStack = backStack,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                entryProvider = entryProvider {
                    // 1. Welcome Screen
                    entry<WinterNav3Screen.Welcome> {
                        WinterWelcomeAuthScreen(
                            onAuthSuccess = {
                                while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                                backStack.add(WinterNav3Screen.Main(initialTab = "DISCOVER"))
                            }
                        )
                    }

                    entry<WinterNav3Screen.Main> { key ->
                        WinterMainParentScreen(
                            initialTab = mapTabStringToNavItem(key.initialTab),
                            onCreatorClick = { creator ->
                                backStack.add(
                                    WinterNav3Screen.ProfileDetail(
                                        creatorId = creator.id,
                                        creatorName = creator.name,
                                        creatorHandle = "@${creator.name.lowercase().replace(" ", "_")}"
                                    )
                                )
                            },
                            onConversationClick = { conv ->
                                backStack.add(WinterNav3Screen.ChatThread(recipientName = conv.name))
                            },
                            onComposeClick = {
                                backStack.add(WinterNav3Screen.ChatThread(recipientName = "Ananya Sharma"))
                            },
                            onRechargeClick = {
                                backStack.add(WinterNav3Screen.RechargeCoins)
                            },
                            onSearchClick = {
                                backStack.add(WinterNav3Screen.FindingModel)
                            },
                            onStartAudioCall = { name ->
                                callViewModel.startOutgoingCall(
                                    modelId = name.lowercase().replace(" ", "_"),
                                    modelName = name,
                                    ratePerMin = 20.0,
                                    callType = CallType.VOICE
                                )
                                backStack.add(WinterNav3Screen.AudioCall(callerName = name))
                            },
                            onStartVideoCall = { name ->
                                callViewModel.startOutgoingCall(
                                    modelId = name.lowercase().replace(" ", "_"),
                                    modelName = name,
                                    ratePerMin = 40.0,
                                    callType = CallType.VIDEO
                                )
                                backStack.add(WinterNav3Screen.VideoCall(callerName = name))
                            },
                            onManageMembershipClick = {
                                backStack.add(WinterNav3Screen.Main(initialTab = "EARNINGS"))
                            },
                            onSettingsClick = {
                                backStack.add(WinterNav3Screen.ModelProfile)
                            },
                            onJoinAsModelClick = {
                                backStack.add(WinterNav3Screen.ModelSetup)
                            },
                            onSignOutClick = {
                                onSignOut()
                                while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                                backStack.add(WinterNav3Screen.Welcome)
                            }
                        )
                    }

                    // Backward-compatible entries mapping to Main Parent Screen
                    entry<WinterNav3Screen.Models> {
                        WinterMainParentScreen(
                            initialTab = WinterNavItem.DISCOVER,
                            onCreatorClick = { creator ->
                                backStack.add(
                                    WinterNav3Screen.ProfileDetail(
                                        creatorId = creator.id,
                                        creatorName = creator.name,
                                        creatorHandle = "@${creator.name.lowercase().replace(" ", "_")}"
                                    )
                                )
                            },
                            onRechargeClick = {
                                backStack.add(WinterNav3Screen.RechargeCoins)
                            },
                            onSearchClick = {
                                backStack.add(WinterNav3Screen.FindingModel)
                            },
                            onStartVideoCall = { name ->
                                backStack.add(WinterNav3Screen.VideoCall(callerName = name))
                            }
                        )
                    }

                    entry<WinterNav3Screen.Chats> {
                        WinterMainParentScreen(
                            initialTab = WinterNavItem.CHATS,
                            onConversationClick = { conv ->
                                backStack.add(WinterNav3Screen.ChatThread(recipientName = conv.name))
                            },
                            onComposeClick = {
                                backStack.add(WinterNav3Screen.ChatThread(recipientName = "Ananya Sharma"))
                            }
                        )
                    }

                    entry<WinterNav3Screen.Wallet> {
                        WinterMainParentScreen(
                            initialTab = WinterNavItem.WALLET,
                            onRechargeClick = {
                                backStack.add(WinterNav3Screen.RechargeCoins)
                            }
                        )
                    }

                    entry<WinterNav3Screen.Earnings> {
                        WinterMainParentScreen(
                            initialTab = WinterNavItem.EARNINGS
                        )
                    }

                    entry<WinterNav3Screen.UserSpace> {
                        WinterMainParentScreen(
                            initialTab = WinterNavItem.PROFILE,
                            onRechargeClick = {
                                backStack.add(WinterNav3Screen.RechargeCoins)
                            },
                            onSettingsClick = {
                                backStack.add(WinterNav3Screen.ModelProfile)
                            },
                            onJoinAsModelClick = {
                                backStack.add(WinterNav3Screen.ModelSetup)
                            },
                            onSignOutClick = {
                                onSignOut()
                                while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                                backStack.add(WinterNav3Screen.Welcome)
                            }
                        )
                    }

                    // 3. Creator Profile Detail Screen
                    entry<WinterNav3Screen.ProfileDetail> { key ->
                        WinterProfileDetailScreen(
                            creatorId = key.creatorId,
                            creatorName = key.creatorName,
                            onBackClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onMessageClick = {
                                backStack.add(WinterNav3Screen.ChatThread(recipientName = key.creatorName))
                            },
                            onStartAudioCall = {
                                val targetId = key.creatorId.ifBlank { key.creatorName.lowercase().replace(" ", "_") }
                                callViewModel.startOutgoingCall(
                                    modelId = targetId,
                                    modelName = key.creatorName,
                                    ratePerMin = 20.0,
                                    callType = CallType.VOICE
                                )
                                backStack.add(WinterNav3Screen.AudioCall(callerName = key.creatorName))
                            },
                            onStartVideoCall = {
                                val targetId = key.creatorId.ifBlank { key.creatorName.lowercase().replace(" ", "_") }
                                callViewModel.startOutgoingCall(
                                    modelId = targetId,
                                    modelName = key.creatorName,
                                    ratePerMin = 40.0,
                                    callType = CallType.VIDEO
                                )
                                backStack.add(WinterNav3Screen.VideoCall(callerName = key.creatorName))
                            }
                        )
                    }

                    // 4. 1-on-1 Chat Thread Screen (Figma Node 149:7)
                    entry<WinterNav3Screen.ChatThread> { key ->
                        WinterChatThreadScreen(
                            creatorName = key.recipientName,
                            onBackClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onAudioCallClick = {
                                callViewModel.startOutgoingCall(
                                    modelId = key.recipientName.lowercase().replace(" ", "_"),
                                    modelName = key.recipientName,
                                    ratePerMin = 20.0,
                                    callType = CallType.VOICE
                                )
                                backStack.add(WinterNav3Screen.AudioCall(callerName = key.recipientName))
                            },
                            onVideoCallClick = {
                                callViewModel.startOutgoingCall(
                                    modelId = key.recipientName.lowercase().replace(" ", "_"),
                                    modelName = key.recipientName,
                                    ratePerMin = 40.0,
                                    callType = CallType.VIDEO
                                )
                                backStack.add(WinterNav3Screen.VideoCall(callerName = key.recipientName))
                            }
                        )
                    }

                    // 5. Recharge Coins Screen (Figma Node 176:2)
                    entry<WinterNav3Screen.RechargeCoins> {
                        WinterRechargeCoinsScreen(
                            onBackClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onPayClick = { pack ->
                                backStack.add(
                                    WinterNav3Screen.RechargeProcessing(
                                        packageId = pack.id,
                                        packCoins = "%,d".format(pack.credits),
                                        packPrice = "₹${pack.price_inr}"
                                    )
                                )
                            }
                        )
                    }

                    entry<WinterNav3Screen.RechargeProcessing> { key ->
                        WinterRechargeProcessingScreen(
                            packageId = key.packageId,
                            packCoins = key.packCoins,
                            packPrice = key.packPrice,
                            onBackClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onPaymentSuccess = {
                                while (backStack.isNotEmpty() && (backStack.last() is WinterNav3Screen.RechargeProcessing || backStack.last() is WinterNav3Screen.RechargeCoins)) {
                                    backStack.removeLastOrNull()
                                }
                            }
                        )
                    }

                    // 7. Finding a Model Radar Screen (Figma Node 176:12)
                    entry<WinterNav3Screen.FindingModel> {
                        WinterFindingModelScreen(
                            onBackClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onCancelClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onModelFound = { modelName ->
                                backStack.removeLastOrNull()
                                backStack.add(WinterNav3Screen.VideoCall(callerName = modelName))
                            }
                        )
                    }

                    // 8. Model Profile Screen (Figma Node 134:9)
                    entry<WinterNav3Screen.ModelProfile> {
                        val context = LocalContext.current
                        val prefsManager = remember(context) { PreferencesManager(context.applicationContext) }
                        var showGenderDialog by remember { mutableStateOf(false) }

                        WinterModelProfileScreen(
                            showBottomNav = false,
                            isModel = false,
                            showJoinAsModelBanner = true,
                            onBackClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onJoinAsModelClick = {
                                backStack.add(WinterNav3Screen.ModelSetup)
                            },
                            onEditProfileClick = {
                                backStack.add(WinterNav3Screen.ModelSetup)
                            },
                            onEditRatesClick = {
                                backStack.add(WinterNav3Screen.ModelSetup)
                            },
                            onChangeGenderClick = {
                                showGenderDialog = true
                            },
                            onEarningsClick = {
                                backStack.add(WinterNav3Screen.Main(initialTab = "EARNINGS"))
                            },
                            onSignOutClick = {
                                onSignOut()
                                while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                                backStack.add(WinterNav3Screen.Welcome)
                            }
                        )

                        if (showGenderDialog) {
                            WinterGenderDialog(
                                initialGender = GenderOption.WOMAN,
                                onDismissRequest = { showGenderDialog = false },
                                onContinue = { option ->
                                    val newGender = if (option == GenderOption.WOMAN) "female" else "male"
                                    prefsManager.setUserGender(newGender)
                                    prefsManager.setGenderAsked(true)
                                    showGenderDialog = false
                                    if (newGender == "male") {
                                        if (backStack.size > 1) backStack.removeLastOrNull()
                                        backStack.add(WinterNav3Screen.Main(initialTab = "PROFILE"))
                                    }
                                }
                            )
                        }
                    }

                    // 9. Model Setup Screen (Figma Node 198:2)
                    entry<WinterNav3Screen.ModelSetup> {
                        WinterModelSetupScreen(
                            onBackClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onSaveProfile = { _, _, _, _ ->
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            }
                        )
                    }

                    // 9. Audio Call Screen (Figma Node 149:11)
                    entry<WinterNav3Screen.AudioCall> { key ->
                        WinterAudioCallScreen(
                            callerName = callState.remoteUserName.ifBlank { key.callerName },
                            callRateText = "₹${callState.ratePerMin.toInt()} / min · Audio call",
                            durationSeconds = callState.durationSec,
                            remainingSec = callState.remainingSec,
                            isLowBalanceWarning = callState.isLowBalanceWarning,
                            isMuted = callState.isMuted,
                            isSpeakerOn = callState.isSpeaker,
                            onMinimizeClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onToggleMute = {
                                callViewModel.toggleMute()
                            },
                            onToggleSpeaker = {
                                callViewModel.toggleSpeaker()
                            },
                            onToggleVideoClick = {
                                callViewModel.toggleCamera()
                                backStack.add(WinterNav3Screen.VideoCall(callerName = key.callerName))
                            },
                            onEndCallClick = {
                                callViewModel.endCall("User ended call")
                                callViewModel.resetState()
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onQuickRechargeClick = {
                                backStack.add(WinterNav3Screen.RechargeCoins)
                            }
                        )
                    }

                    // 10. Video Call Screen (Figma Node 149:15)
                    entry<WinterNav3Screen.VideoCall> { key ->
                        WinterVideoCallScreen(
                            callerName = callState.remoteUserName.ifBlank { key.callerName },
                            callRateText = "₹${callState.ratePerMin.toInt()} / min · Video call",
                            durationSeconds = callState.durationSec,
                            remainingSec = callState.remainingSec,
                            isLowBalanceWarning = callState.isLowBalanceWarning,
                            isMuted = callState.isMuted,
                            isCameraOff = !callState.isCameraOn,
                            isFrontCamera = callState.isFrontCamera,
                            room = room ?: callViewModel.room,
                            remoteVideoTrack = remoteVideoTrack,
                            localVideoTrack = localVideoTrack,
                            onMinimizeClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onToggleMute = {
                                callViewModel.toggleMute()
                            },
                            onFlipCamera = {
                                callViewModel.flipCamera()
                            },
                            onToggleCamera = {
                                callViewModel.toggleCamera()
                            },
                            onOpenChatClick = {
                                backStack.add(WinterNav3Screen.ChatThread(recipientName = key.callerName))
                            },
                            onEndCallClick = {
                                callViewModel.endCall("User ended call")
                                callViewModel.resetState()
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            },
                            onQuickRechargeClick = {
                                backStack.add(WinterNav3Screen.RechargeCoins)
                            }
                        )
                    }
                }
            )
        }
    }
}

private fun mapTabStringToNavItem(tabName: String): WinterNavItem {
    return when (tabName.uppercase()) {
        "WALLET" -> WinterNavItem.WALLET
        "EARNINGS" -> WinterNavItem.EARNINGS
        "CHATS" -> WinterNavItem.CHATS
        "CALLS" -> WinterNavItem.CALLS
        "PROFILE" -> WinterNavItem.PROFILE
        else -> WinterNavItem.DISCOVER
    }
}

private fun screenDisplayName(screen: Any): String {
    return when (screen) {
        is WinterNav3Screen.Welcome -> "Login"
        is WinterNav3Screen.Main -> "Main Hub (${screen.initialTab})"
        is WinterNav3Screen.Models -> "Discover"
        is WinterNav3Screen.FindingModel -> "Finding Model"
        is WinterNav3Screen.ProfileDetail -> "Profile Detail"
        is WinterNav3Screen.Chats -> "Chats"
        is WinterNav3Screen.ChatThread -> "Chat Thread"
        is WinterNav3Screen.Wallet -> "Wallet"
        is WinterNav3Screen.RechargeCoins -> "Recharge"
        is WinterNav3Screen.RechargeProcessing -> "Recharge Processing"
        is WinterNav3Screen.Earnings -> "Earnings"
        is WinterNav3Screen.UserSpace -> "User Profile"
        is WinterNav3Screen.ModelProfile -> "Model Profile"
        is WinterNav3Screen.ModelSetup -> "Model Setup"
        is WinterNav3Screen.AudioCall -> "Audio Call"
        is WinterNav3Screen.VideoCall -> "Video Call"
        else -> "Screen"
    }
}
