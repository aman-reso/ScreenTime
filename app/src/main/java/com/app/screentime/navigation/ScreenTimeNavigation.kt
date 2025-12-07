package com.app.screentime.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.app.screentime.appdetail.screen.SingleAppUsageDetailScreen
import com.app.screentime.blocking.screen.AppBlockingScreen
import com.app.screentime.challenge.screen.ChallengeDetailScreen
import com.app.screentime.challenge.screen.ChallengeListScreen
import com.app.screentime.landing.screen.AdaptiveLandingScreen
import com.app.screentime.permission.AppPermissionScreen
import com.app.screentime.profile.screen.ProfileScreen
import com.app.screentime.record.screen.RecordDetailScreen
import com.app.screentime.reward.screen.CoinHistoryScreen
import com.app.screentime.reward.screen.RewardScreen
import com.app.screentime.reward.screen.RewardTransactionScreen
import com.app.screentime.search.screen.SearchScreen
import com.app.screentime.statistics.screen.StatisticsScreen
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    scheme: ODSTheme = neutralScheme,
    props: ScreenTimeNavigationProps = ScreenTimeNavigationProps(),
    tokens: ScreenTimeNavigationTokens = defaultScreenTimeNavigationTokens,
    style: ScreenTimeNavigationStyle = ScreenTimeNavigationStyle().getStyle(scheme)
) {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Permission) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(backStack) {
        val top = backStack.lastOrNull()
        val newIndex = tokens.bottomNavigationRoutes.indexOf(top)
        selectedIndex = if (newIndex >= 0) newIndex else -1
    }

    val navigationHandlers = remember {
        object {
            fun onIndexChanged(index: Int) {
                selectedIndex = index
                val route = tokens.bottomNavigationRoutes.getOrNull(index)
                if (route != null) {
                    if (backStack.isNotEmpty()) {
                        backStack[backStack.lastIndex] = route
                    } else {
                        backStack.add(route)
                    }
                }
            }
        }
    }
    Scaffold(
        containerColor = style.scaffoldBackground?.firstOrNull()?.hexColor?.getColor()
            ?: scheme.basicBackground.getColor(), topBar = {}, bottomBar = {
            val currentScreen = backStack.lastOrNull()
            if (
                props.showBottomNavigation &&
                currentScreen != null &&
                currentScreen in tokens.bottomNavigationRoutes
            ) {
                BottomNavigationBar(
                    scheme = scheme,
                    navigationItems = props.navigationItems,
                    selectedIndex = selectedIndex,
                    onIndexChanged = navigationHandlers::onIndexChanged
                )
            }
        }, modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        NavigationHost(
            scheme = scheme, paddingValues = paddingValues, backStack, tokens
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
    backStack: SnapshotStateList<Screen>,
    tokens: ScreenTimeNavigationTokens
) {
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
                onNavigateToReward = { backStack.add(Screen.Reward) },
                onNavigateToSearch = { backStack.add(Screen.Search) },
                onNavigateToStatistics = { backStack.add(Screen.Statistics) },
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
                onNavigateToChallenges = { backStack.add(Screen.Challenges) },
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
                onNavigateToAppBlocking = { backStack.add(Screen.AppBlocking) },
                onNavigateToBlockedLinks = { backStack.add(Screen.BlockedLinks) },
                scheme = scheme
            )
        }

        entry<Screen.Statistics> {
            StatisticsScreen(
                modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
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

        entry<Screen.AppBlocking> {
            AppBlockingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()), onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }, scheme = scheme
            )
        }

        entry<Screen.Leaderboard> {
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
                    backStack.add(Screen.RewardTransaction(transactionId))
                }, scheme = scheme
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
                    backStack.add(Screen.RewardTransaction(transactionId))
                }, scheme = scheme
            )
        }

        entry<Screen.CoinHistory> {
            CoinHistoryScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()), onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }, scheme = scheme
            )
        }

        entry<Screen.RewardTransaction> { screen ->
            RewardTransactionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                onBackClick = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                transactionId = screen.transactionId,
                scheme = scheme
            )
        }

        entry<Screen.Search> {
            SearchScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()), onBackClick = {
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
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding()),
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
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding()),
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

        // entry<Screen.FocusMode> {
        //     // TODO: Implement FocusModeScreen
        // }

        // entry<Screen.BlockedLinks> {
        //     // TODO: Implement BlockedLinksScreen
        // }
    })
}
