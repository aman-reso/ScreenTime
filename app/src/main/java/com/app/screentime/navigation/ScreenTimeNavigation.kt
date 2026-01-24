package com.app.screentime.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.app.screentime.appdetail.screen.SingleAppUsageDetailScreen
import com.app.screentime.applock.screen.AppLockScreen
import com.app.screentime.challenge.screen.ChallengeDetailScreen
import com.app.screentime.challenge.screen.ChallengeListScreen
// import com.app.screentime.challenge.screen.ChallengeDetailScreen // Removed - Challenge feature disabled
// import com.app.screentime.challenge.screen.ChallengeListScreen // Removed - Challenge feature disabled
import com.app.screentime.landing.screen.AdaptiveLandingScreen
import com.app.screentime.leaderboard.screen.LeaderboardScreen
import com.app.screentime.notifications.screen.CapturedNotificationsScreen
import com.app.screentime.permission.AppPermissionScreen
import com.app.screentime.permission.checkUsageStatsPermission
import com.app.screentime.profile.screen.ProfileScreen
import com.app.screentime.controlcenter.screen.ControlCenterScreen
import com.app.screentime.location.screen.LocationManagementScreen
import com.app.screentime.record.screen.RecordDetailScreen
import com.app.screentime.reward.screen.CoinHistoryScreen
import com.app.screentime.reward.screen.RewardScreen
import com.app.screentime.reward.screen.RewardTransactionScreen
import com.app.screentime.search.screen.SearchScreen
import com.app.screentime.statistics.screen.StatisticsScreen
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.wallpaper.screen.WallpaperScreen
import com.app.screentime.wallpaper.screen.FullScreenWallpaperScreen
import com.app.screentime.wallpaper.screen.WallpaperSearchScreen
import com.app.screentime.customisation.screen.CustomisationScreen
import com.app.screentime.navigation.Screen
// import com.app.screentime.wallpaper.screen.WallpaperScreen // Removed - Wallpaper feature disabled
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButton
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonProps
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonSize
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonType
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigation
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationItemProps
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Main navigation composable for ScreenTime app.
 *
 * @param scheme ODS theme scheme for styling.
 * @param props Configuration properties for navigation.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    scheme: ODSTheme = neutralScheme,
    props: ScreenTimeNavigationProps = ScreenTimeNavigationProps(),
    style: ScreenTimeNavigationStyle = ScreenTimeNavigationStyle().getStyle(scheme),
    isUserInIndia: Boolean = true,
    deeplinkUri: Uri? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val isUsagePermission = checkUsageStatsPermission(context)
    val navigationTokens = getNavigationTokens(isUserInIndia)
    val navigationItems = getNavigationItems(isUserInIndia)
    val backStack = rememberNavBackStack(
        if (isUsagePermission) {
            Screen.Landing
        } else {
            Screen.Permission
        }
    )
    var selectedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(backStack.toList(), navigationTokens) {
        val top = backStack.lastOrNull()
        val newIndex = navigationTokens.bottomNavigationRoutes.indexOf(top)
        selectedIndex = if (newIndex >= 0) newIndex else -1
    }

    LaunchedEffect(deeplinkUri) {
        if (deeplinkUri != null) {
            val deeplinkScreen = DeeplinkParser.parseDeeplink(deeplinkUri)
            if (deeplinkScreen != null) {
                if (DeeplinkParser.shouldClearBackStack(deeplinkScreen)) {
                    backStack.clear()
                    backStack.add(deeplinkScreen)
                } else if (DeeplinkParser.shouldAddLandingToBackStack(deeplinkScreen)) {
                    backStack.clear()
                    backStack.add(Screen.Landing)
                    backStack.add(deeplinkScreen)
                } else {
                    backStack.clear()
                    backStack.add(deeplinkScreen)
                }
            }
        }
    }

    val navigationHandlers = remember(navigationTokens) {
        object {
            fun onIndexChanged(index: Int) {
                val route = navigationTokens.bottomNavigationRoutes.getOrNull(index)
                if (route != null) {
                    val currentScreen = backStack.lastOrNull()
                    if (currentScreen == Screen.Landing && route != Screen.Landing) {
                        backStack.add(route)
                    } else if (backStack.isNotEmpty()) {
                        backStack[backStack.lastIndex] = route
                    } else {
                        backStack.add(route)
                    }
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            val currentScreen = backStack.lastOrNull()
            // Only show Reward FAB if user is in India AND on allowed screens
            if (isUserInIndia && (currentScreen == Screen.Landing || currentScreen == Screen.Challenges || currentScreen is Screen.ChallengeDetail || currentScreen == Screen.Profile)) {
                ODSFloatingActionButton(
                    modifier = Modifier
                        .padding(
                            end = DSVariables.spacingComponent4,
                            bottom = DSVariables.spacingComponent4
                        )
                        .navigationBarsPadding(),
                    scheme = scheme,
                    props = ODSFloatingActionButtonProps(
                        icon = ODSIconModel(
                            imageVector = Icons.Outlined.CardGiftcard,
                            contentDescription = "Reward"
                        ),
                        size = ODSFloatingActionButtonSize.SMALL,
                        type = ODSFloatingActionButtonType.STANDARD,
                        variant = ODSFloatingActionButtonVariant.SECONDARY
                    ),
                    onClick = {
                        // Tracking happens in onNavigateToReward callback for Landing screen
                        // For other screens, the floating button is only shown on Landing
                        backStack.add(Screen.Reward)
                    }
                )
            }
        },
        containerColor = style.scaffoldBackground?.firstOrNull()?.hexColor?.getColor()
            ?: scheme.basicBackground.getColor(),
        topBar = {},
        bottomBar = {
            val currentScreen = backStack.lastOrNull()
            val currentRoutes = if (isUserInIndia) {
                navigationTokens.bottomNavigationRoutes
            } else {
                navigationTokens.bottomNavigationRoutes.filter { it != Screen.Challenges }
            }

            if (
                props.showBottomNavigation &&
                currentScreen != null &&
                currentScreen in currentRoutes
            ) {
                BottomNavigationBar(
                    scheme = scheme,
                    navigationItems = navigationItems,
                    selectedIndex = selectedIndex,
                    onIndexChanged = navigationHandlers::onIndexChanged
                )
            }
        },
        snackbarHost = {
            ToastSnackbarHost(
                snackbarHostState = snackbarHostState,
                scheme = scheme,
                modifier = Modifier.padding(
                    bottom = if (props.showBottomNavigation && backStack.lastOrNull() in navigationTokens.bottomNavigationRoutes) {
                        // Add padding to avoid overlap with bottom navigation
                        DSVariables.spacingComponent8
                    } else {
                        DSVariables.spacingComponent4
                    }
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) { paddingValues ->
        NavigationHost(
            scheme = scheme,
            paddingValues = paddingValues,
            backStack = backStack,
            tokens = navigationTokens,
            navigationItems = navigationItems
        )
    }
}

/**
 * Bottom navigation bar composable.
 */
@Composable
private fun BottomNavigationBar(
    scheme: ODSTheme,
    navigationItems: List<ODSBottomNavigationItemProps>,
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier.wrapContentHeight(), contentAlignment = Alignment.BottomCenter
    ) {
        ODSBottomNavigation(
            scheme = scheme, props = ODSBottomNavigationProps(
                items = navigationItems, labels = true
            ), selectedIndex = selectedIndex, onIndexChanged = onIndexChanged
        )
    }
}

/**
 * Navigation host containing all route definitions.
 */
@Composable
private fun NavigationHost(
    scheme: ODSTheme,
    paddingValues: PaddingValues,
    backStack: NavBackStack<NavKey>,
    tokens: ScreenTimeNavigationTokens,
    navigationItems: List<ODSBottomNavigationItemProps>,
) {
    val activity = LocalActivity.current
    NavDisplay(backStack = backStack, onBack = {
        val top = backStack.lastOrNull()
        if (top in tokens.bottomNavigationRoutes && top != Screen.Landing) {
            backStack[backStack.lastIndex] = Screen.Landing
            return@NavDisplay
        }

        if (backStack.size > 1) {
            backStack.removeLastOrNull()
            return@NavDisplay
        }
    }, entryProvider = entryProvider {
        entry<Screen.Permission> {
            AppPermissionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                onAllPermissionsGranted = {
                    backStack.clear()
                    backStack.add(Screen.Landing)
                },
                scheme = neutralScheme
            )
        }

        entry<Screen.Landing> {
            AdaptiveLandingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding()),
                    onNavigateToLeaderboard = {
                        backStack.add(Screen.Leaderboard)
                    },
                    onNavigateToReward = {
                        backStack.add(Screen.Reward)
                    },
                    onNavigateToSearch = { backStack.add(Screen.Search) },
                    onNavigateToStatistics = {
                        backStack.add(Screen.Statistics)
                    },
                    onNavigateToSingleAppUsageDetail = { packageName ->
                        backStack.add(
                            Screen.SingleAppUsageDetail(
                                SingleAppUsageDetailParams(
                                    packageName
                                )
                            )
                        )
                    },
                    onNavigateToChallengeDetail = { challengeId ->
                        backStack.add(
                            Screen.ChallengeDetail(
                                ChallengeDetailParams(
                                    challengeId
                                )
                            )
                        )
                },
                onNavigateToChallenges = {
                    backStack.add(Screen.Challenges)
                },
                onNavigateToControlCenter = { backStack.add(Screen.ControlCenter) },
                onNavigateToManageLocation = { backStack.add(Screen.ManageLocation) },
                onNavigateToRecoverNotification = { backStack.add(Screen.CapturedNotifications) },
                onNavigateToAppLock = { backStack.add(Screen.AppLock) },
                onNavigateToFileManager = { backStack.add(Screen.FileManager) },
                onNavigateToWallpaper = { backStack.add(Screen.Wallpaper) },
                onNavigateToCustomisation = { backStack.add(Screen.Customisation) },
                openSearchScreen = {
                    backStack.add(Screen.Search)
                },
                scheme = scheme
            )
        }

        entry<Screen.Profile> {
            ProfileScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                onNavigateToAppLock = { backStack.add(Screen.AppLock) },
                scheme = scheme,
                onNavigateToCapturedNotifications = { backStack.add(Screen.CapturedNotifications) },
                onNavigateToWallpaper = { backStack.add(Screen.Wallpaper) },
                onNavigateToRecoverNotification = { backStack.add(Screen.CapturedNotifications) },
                onNavigateToControlCenter = { backStack.add(Screen.ControlCenter) },
                onNavigateToManageLocation = { backStack.add(Screen.ManageLocation) },
                onNavigateToFileManager = { backStack.add(Screen.FileManager) }
            )
        }

        entry<Screen.CapturedNotifications> {
            CapturedNotificationsScreen(
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                scheme = scheme
            )
        }

        entry<Screen.ControlCenter> {
            ControlCenterScreen(
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                onNavigateToRecordDetail = { username ->
                    backStack.add(Screen.RecordDetail(RecordDetailParams(username)))
                },
                scheme = scheme
            )
        }

        entry<Screen.ManageLocation> {
            LocationManagementScreen(
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                scheme = scheme
            )
        }

        entry<Screen.AppLock> {
            AppLockScreen(
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                scheme = scheme,
                useDarkTheme = LocalThemeMode.current
            )
        }

        entry<Screen.FileManager> {
//            FileManagerScreen(
//                onBackClick = {
//                    if (backStack.size > 1) {
//                        backStack.removeLastOrNull()
//                    }
//                },
//                scheme = scheme
//            )
        }

        entry<Screen.Statistics> {
            StatisticsScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateToSingleAppUsageDetail = { packageName ->
                    backStack.add(
                        Screen.SingleAppUsageDetail(
                            SingleAppUsageDetailParams(
                                packageName
                            )
                        )
                    )
                },
                scheme = scheme
            )
        }

        entry<Screen.Leaderboard> {
            LeaderboardScreen(
                modifier = Modifier
                    .fillMaxSize(),
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                scheme = scheme,
                isInBottomNav = tokens.bottomNavigationRoutes.any { it == Screen.Leaderboard }
            )
        }

        entry<Screen.Challenges> {
            ChallengeListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                onNavigateToChallengeDetail = { challengeId ->
                    backStack.add(
                        Screen.ChallengeDetail(
                            ChallengeDetailParams(
                                challengeId
                            )
                        )
                    )
                },
                scheme = scheme
            )
        }

        entry<Screen.Reward> {
            RewardScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()), onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }, onNavigateToCoinHistory = {
                    backStack.add(Screen.CoinHistory)
                }, onNavigateToRewardHistory = { transactionId ->
                    backStack.add(Screen.RewardTransaction(transactionId = transactionId))
                }, scheme = scheme
            )
        }

        entry<Screen.CoinHistory> {
            CoinHistoryScreen(
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateBottomPadding()), scheme = scheme
            )
        }

        entry<Screen.RewardTransaction> { screen ->
            RewardTransactionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateBottomPadding()), onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }, scheme = scheme
            )
        }

        entry<Screen.Search> {
            SearchScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateBottomPadding()), onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }, onNavigateToRecordDetail = { username ->
                    backStack.add(Screen.RecordDetail(RecordDetailParams(username)))
                }, scheme = scheme
            )
        }

        entry<Screen.ChallengeDetail> { key ->
            key.params?.challengeId?.let {
                ChallengeDetailScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    challengeId = it,
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    scheme = scheme
                )
            }
        }

        entry<Screen.RecordDetail> { key ->
            key.params?.username?.let {
                RecordDetailScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    username = it,
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    onNavigateToAppDetails = { packageName ->
                        backStack.add(
                            Screen.SingleAppUsageDetail(
                                SingleAppUsageDetailParams(
                                    packageName
                                )
                            )
                        )
                    })
            }
        }

        entry<Screen.SingleAppUsageDetail> { key ->
            key.params?.packageName?.let {
                SingleAppUsageDetailScreen(
                    packageName = it, onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }, scheme = scheme
                )
            }
        }

        // BlockedLinks feature disabled
        /*
        entry<Screen.BlockedLinks> {
            BlockedLinksScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateBottomPadding()),
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                scheme = scheme
            )
        }
        */

        entry<Screen.Wallpaper> {
            WallpaperScreen(
                modifier = Modifier
                    .fillMaxSize(),
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                onNavigateToFullScreen = { wallpaperId, imageItem ->
                    backStack.add(
                        Screen.FullScreenWallpaper(
                            FullScreenWallpaperParams(
                                wallpaperId = wallpaperId,
                                imageItemJson = imageItem
                            )
                        )
                    )
                },
                onNavigateToSearch = {
                    backStack.add(
                        Screen.WallPaperSearch
                    )
                }
            )
        }

        entry<Screen.FullScreenWallpaper> { key ->
            key.params?.let { params ->
                FullScreenWallpaperScreen(
                    imageItem = params.imageItemJson,
                    modifier = Modifier.fillMaxSize(),
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    scheme = scheme
                )
            }
        }

        entry<Screen.WallPaperSearch> {
            WallpaperSearchScreen(
                onNavigateToFullScreen = { wallpaperId, imageItem ->
                    backStack.add(
                        Screen.FullScreenWallpaper(
                            FullScreenWallpaperParams(
                                wallpaperId = wallpaperId,
                                imageItemJson = imageItem
                            )
                        )
                    )
                },
                modifier = Modifier.fillMaxSize(),
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                scheme = scheme
            )
        }

        entry<Screen.Customisation> {
            CustomisationScreen(
                onNavigateBack = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                modifier = Modifier.fillMaxSize(),
                scheme = scheme
            )
        }
    })
}
