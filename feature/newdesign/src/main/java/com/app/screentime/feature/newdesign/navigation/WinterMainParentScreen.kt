package com.app.screentime.feature.newdesign.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.newdesign.call.WinterAudioCallScreen
import com.app.screentime.feature.newdesign.chats.WinterChatConversation
import com.app.screentime.feature.newdesign.chats.WinterChatsScreen
import com.app.screentime.feature.newdesign.components.WinterFixedBottomNavigation
import com.app.screentime.feature.newdesign.components.WinterNavItem
import com.app.screentime.feature.newdesign.components.WinterNavRole
import com.app.screentime.feature.newdesign.components.WinterTokens
import com.app.screentime.feature.newdesign.earnings.WinterEarningsScreen
import com.app.screentime.feature.newdesign.models.CreatorModel
import com.app.screentime.feature.newdesign.models.WinterModelsScreen
import com.app.screentime.feature.newdesign.profile.WinterUserSpaceScreen
import com.app.screentime.feature.newdesign.wallet.WinterWalletScreen

/**
 * WinterMainParentScreen - Central parent container hosting all main tab screens:
 * - Discover: [WinterModelsScreen]
 * - Wallet: [WinterWalletScreen]
 * - Earnings: [WinterEarningsScreen]
 * - Chats: [WinterChatsScreen]
 * - Calls: [WinterAudioCallScreen]
 * - Profile: [WinterUserSpaceScreen]
 *
 * Uses [Scaffold] with a fixed bottom navigation bar ([WinterFixedBottomNavigation])
 * so child screens cleanly fit into the content area without floating overlap.
 *
 * Rules:
 * 1. Multiples of 4dp for all padding, margins, and sizes.
 * 2. Clean modular sub-composables.
 * 3. Dynamic color tokens (WinterTokens).
 * 4. Content-driven layout with Scaffold inner padding.
 */
@Composable
fun WinterMainParentScreen(
    modifier: Modifier = Modifier,
    initialTab: WinterNavItem = WinterNavItem.DISCOVER,
    currentTab: WinterNavItem? = null,
    coinBalance: Int = 240,
    userRole: WinterNavRole = WinterNavRole.USER,
    onNavItemSelected: (WinterNavItem) -> Unit = {},
    onCreatorClick: (CreatorModel) -> Unit = {},
    onConversationClick: (WinterChatConversation) -> Unit = {},
    onComposeClick: () -> Unit = {},
    onRechargeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onStartAudioCall: (String) -> Unit = {},
    onStartVideoCall: (String) -> Unit = {},
    onManageMembershipClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    var activeNavItem by remember { mutableStateOf(initialTab) }

    LaunchedEffect(currentTab) {
        if (currentTab != null && currentTab != activeNavItem) {
            activeNavItem = currentTab
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = WinterTokens.backgroundCanvas,
        bottomBar = {
            WinterFixedBottomNavigation(
                selectedItem = activeNavItem,
                onItemSelected = { item ->
                    activeNavItem = item
                    onNavItemSelected(item)
                },
                role = if (activeNavItem == WinterNavItem.EARNINGS) WinterNavRole.CREATOR else userRole
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            Crossfade(
                targetState = activeNavItem,
                label = "WinterMainTabTransition"
            ) { targetTab ->
                when (targetTab) {
                    WinterNavItem.DISCOVER -> {
                        WinterModelsScreen(
                            coinBalance = coinBalance,
                            onCreatorClick = onCreatorClick,
                            onCoinBalanceClick = onRechargeClick,
                            onSearchClick = onSearchClick,
                            onAcceptCall = { onStartVideoCall("Priya") },
                            onDeclineCall = { /* Handled in screen */ },
                            onJoinLiveClick = { topModel -> onStartVideoCall(topModel.name) },
                            onStartAudioCall = onStartAudioCall,
                            onStartVideoCall = onStartVideoCall
                        )
                    }

                    WinterNavItem.WALLET -> {
                        WinterWalletScreen(
                            showBottomNav = false,
                            contentBottomPadding = 16.dp,
                            onBackClick = { activeNavItem = WinterNavItem.DISCOVER },
                            onRechargeClick = onRechargeClick
                        )
                    }

                    WinterNavItem.EARNINGS -> {
                        WinterEarningsScreen(
                            showBottomNav = false,
                            contentBottomPadding = 16.dp,
                            onBackClick = { activeNavItem = WinterNavItem.DISCOVER },
                            onWithdrawClick = onRechargeClick
                        )
                    }

                    WinterNavItem.CHATS -> {
                        WinterChatsScreen(
                            showBottomNav = false,
                            contentBottomPadding = 16.dp,
                            onComposeClick = onComposeClick,
                            onConversationClick = onConversationClick
                        )
                    }

                    WinterNavItem.CALLS -> {
                        WinterAudioCallScreen(
                            callerName = "Ananya Sharma",
                            onMinimizeClick = { activeNavItem = WinterNavItem.DISCOVER },
                            onToggleVideoClick = { onStartVideoCall("Ananya Sharma") },
                            onEndCallClick = { activeNavItem = WinterNavItem.DISCOVER }
                        )
                    }

                    WinterNavItem.PROFILE -> {
                        WinterUserSpaceScreen(
                            showBottomNav = false,
                            contentBottomPadding = 16.dp,
                            onWalletRechargeClick = onRechargeClick,
                            onManageMembershipClick = onManageMembershipClick,
                            onSettingsClick = onSettingsClick,
                            onSignOutClick = onSignOutClick
                        )
                    }
                }
            }
        }
    }
}
