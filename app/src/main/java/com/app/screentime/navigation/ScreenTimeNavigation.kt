package com.app.screentime.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.app.screentime.feature.call.VoiceCallScreen
import com.app.screentime.feature.chat.ChatListScreen
import com.app.screentime.feature.chat.ChatScreen
import com.app.screentime.feature.discover.DiscoverScreen
import com.app.screentime.feature.discover.ModelProfileScreen
import com.app.screentime.feature.discover.SocialSpaceDemoScreen
import com.app.screentime.feature.profile.UserProfileScreen
import com.app.screentime.feature.wallet.TopUpScreen
import com.app.screentime.feature.wallet.WalletScreen
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigation
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationItemProps
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

// Bottom nav tab definition
data class BottomNavTab(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavTabs = listOf(
    BottomNavTab(Screen.Discover, "Discover", Icons.Filled.Explore, Icons.Outlined.Explore),
    BottomNavTab(Screen.ChatList, "Chats", Icons.AutoMirrored.Filled.Chat,
        Icons.AutoMirrored.Outlined.Chat
    ),
    BottomNavTab(Screen.Wallet, "Wallet", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
    BottomNavTab(Screen.Profile, "Me", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle),
)

private val bottomNavRoutes: Set<Screen> = setOf(
    Screen.Discover, Screen.ChatList, Screen.Wallet, Screen.Profile
)

/**
 * Root navigation composable for Chatty.
 * Shows bottom nav on main tab screens, hides it on detail screens.
 * Pressing back on any secondary tab (Chats, Wallet, Me) navigates to the first tab (Discover).
 * Pressing back on Discover exits the app.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean = true,
    deeplinkUri: Uri? = null,
    incomingCall: Pair<String, String>? = null,
    onClearIncomingCall: () -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    props: Any? = null,
    isUserInIndia: Boolean = true
) {
    val backStack = rememberNavBackStack(Screen.Discover)
    var selectedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(incomingCall) {
        incomingCall?.let { (callerId, callerName) ->
            backStack.add(Screen.VoiceCall(callerId, callerName))
            onClearIncomingCall()
        }
    }

    LaunchedEffect(backStack.toList()) {
        val top = backStack.lastOrNull()
        val idx = bottomNavTabs.indexOfFirst { it.screen == top }
        selectedIndex = if (idx >= 0) idx else -1
    }

    val currentScreen = backStack.lastOrNull()
    val canHandleBack = backStack.size > 1 || (currentScreen != null && currentScreen != Screen.Discover)

    BackHandler(enabled = canHandleBack) {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        } else if (currentScreen != Screen.Discover) {
            backStack[0] = Screen.Discover
        }
    }

    Scaffold(
        containerColor = scheme.basicBackground.getColor(),
        bottomBar = {
            val current = backStack.lastOrNull()
            AnimatedVisibility(
                visible = current in bottomNavRoutes,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                LunaBottomBar(
                    scheme = scheme,
                    selectedIndex = selectedIndex,
                    onTabSelected = { index ->
                        val route = bottomNavTabs.getOrNull(index)?.screen ?: return@LunaBottomBar
                        val current2 = backStack.lastOrNull()
                        when {
                            current2 == route -> Unit // already there
                            current2 in bottomNavRoutes -> backStack[backStack.lastIndex] = route
                            else -> backStack.add(route)
                        }
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) { paddingValues ->
        NavDisplay(
            backStack = backStack,
            onBack = {
                val top = backStack.lastOrNull()
                if (top in bottomNavRoutes && top != Screen.Discover) {
                    backStack[backStack.lastIndex] = Screen.Discover
                } else if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {

                // ── Discover (Home / First Tab) ───────────────────────────────
                entry<Screen.Discover> {
                    DiscoverScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        scheme = scheme,
                        onNavigateToModelProfile = { modelId ->
                            backStack.add(Screen.ModelProfile(modelId, ""))
                        },
                        onNavigateToSocialDemo = {
                            backStack.add(Screen.SocialSpaceDemo)
                        }
                    )
                }

                // ── Chat List ─────────────────────────────────────────────────
                entry<Screen.ChatList> {
                    ChatListScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        scheme = scheme,
                        onNavigateToChat = { modelId, modelName ->
                            backStack.add(Screen.Chat(modelId, modelName))
                        }
                    )
                }

                // ── Wallet ────────────────────────────────────────────────────
                entry<Screen.Wallet> {
                    WalletScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        scheme = scheme,
                        onNavigateToTopUp = { backStack.add(Screen.TopUp) }
                    )
                }

                // ── Top Up ────────────────────────────────────────────────────
                entry<Screen.TopUp> {
                    TopUpScreen(
                        modifier = Modifier.fillMaxSize(),
                        scheme = scheme,
                        onBackClick = { if (backStack.size > 1) backStack.removeLastOrNull() },
                        onTopUpSuccess = { if (backStack.size > 1) backStack.removeLastOrNull() }
                    )
                }

                // ── Profile ───────────────────────────────────────────────────
                entry<Screen.Profile> {
                    UserProfileScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        scheme = scheme,
                        onLogoutClick = { /* logout */ },
                        onNavigateToTopUp = { backStack.add(Screen.TopUp) }
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
                        }
                    )
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
                        }
                    )
                }

                // ── Voice Call ────────────────────────────────────────────────
                entry<Screen.VoiceCall> { key ->
                    VoiceCallScreen(
                        modelId = key.modelId,
                        modelName = key.modelName,
                        modifier = Modifier.fillMaxSize(),
                        scheme = scheme,
                        onEndCall = { if (backStack.size > 1) backStack.removeLastOrNull() }
                    )
                }

                // ── Social Space Showcase Demo ────────────────────────────────
                entry<Screen.SocialSpaceDemo> {
                    SocialSpaceDemoScreen(
                        modifier = Modifier.fillMaxSize(),
                        scheme = scheme,
                        onBackClick = { if (backStack.size > 1) backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}

@Composable
private fun LunaBottomBar(
    scheme: ODSTheme,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ODSBottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSBottomNavigationProps(
            items = bottomNavTabs.mapIndexed { index, tab ->
                ODSBottomNavigationItemProps(
                    active = selectedIndex == index,
                    text = tab.label,
                    textRes = 0,
                    icon = ODSIconModel(imageVector = tab.unselectedIcon),
                    iconActive = ODSIconModel(imageVector = tab.selectedIcon)
                )
            },
            labels = true
        ),
        selectedIndex = selectedIndex.coerceAtLeast(0),
        onIndexChanged = onTabSelected
    )
}
