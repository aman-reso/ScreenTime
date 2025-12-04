package com.app.screentime.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.app.screentime.appdetail.screen.SingleAppUsageDetailScreen
import com.app.screentime.blocking.screen.AppBlockingScreen
import com.app.screentime.challenge.screen.ChallengeDetailScreen
import com.app.screentime.challenge.screen.ChallengeListScreen
import com.app.screentime.landing.screen.LandingScreenV2
import com.app.screentime.landing.screen.AdaptiveLandingScreen
import com.app.screentime.permission.AppPermissionScreen
import com.app.screentime.profile.screen.ProfileScreen
import com.app.screentime.record.screen.RecordDetailScreen
import com.app.screentime.search.screen.SearchScreen
import com.app.screentime.statistics.screen.StatisticsScreen
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigation
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationItemProps
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme

/**
 * Main navigation composable for ScreenTime app.
 *
 * @param navController Navigation controller for managing navigation state.
 * @param scheme ODS theme scheme for styling.
 * @param props Configuration properties for navigation.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    navController: NavHostController = rememberNavController(),
    scheme: ODSTheme = neutralScheme,
    props: ScreenTimeNavigationProps = ScreenTimeNavigationProps()
) {
    val style = ScreenTimeNavigationStyle().getStyle(scheme)
    val tokens = remember { defaultScreenTimeNavigationTokens }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Landing.route
    val selectedIndex = tokens.routeToIndexMap[currentRoute] ?: 0

    val navigationHandlers = remember(navController, currentRoute, tokens) {
        NavigationHandlers(navController, currentRoute, tokens)
    }

    Scaffold(
        containerColor = style.scaffoldBackground?.firstOrNull()?.hexColor?.getColor()
            ?: scheme.basicBackground.getColor(),
        topBar = {},
        bottomBar = {
            if (props.showBottomNavigation && currentRoute in tokens.bottomNavigationRoutes) {
                BottomNavigationBar(
                    scheme = scheme,
                    navigationItems = props.navigationItems,
                    selectedIndex = selectedIndex,
                    onIndexChanged = navigationHandlers::onIndexChanged
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        NavigationHost(
            navController = navController,
            scheme = scheme,
            paddingValues = paddingValues
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
        modifier = Modifier.wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ODSBottomNavigation(
            scheme = scheme,
            props = ODSBottomNavigationProps(
                items = navigationItems,
                labels = true
            ),
            selectedIndex = selectedIndex,
            onIndexChanged = onIndexChanged
        )
    }
}

/**
 * Navigation handlers for managing navigation logic.
 */
private class NavigationHandlers(
    private val navController: NavHostController,
    private val currentRoute: String,
    private val tokens: ScreenTimeNavigationTokens
) {
    fun navigateToItem(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    }

    fun onIndexChanged(index: Int) {
        tokens.routeToIndexMap.entries.find { it.value == index }?.key
            ?.let { navigateToItem(it) }
    }
}

/**
 * Navigation host containing all route definitions.
 */
@Composable
private fun NavigationHost(
    navController: NavHostController,
    scheme: ODSTheme,
    paddingValues: androidx.compose.foundation.layout.PaddingValues
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Screen.Permission.route
    ) {
        composable(
            route = Screen.Landing.route,
            deepLinks = createDeepLinks(
                "apptime://screen/landing",
                "apptime://screen/home",
                "https://apptime.in/landing",
                "https://apptime.in/home"
            )
        ) {
            AdaptiveLandingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                openSearchScreen = {},
                scheme = scheme
            )
        }

        // Profile route
        composable(Screen.Profile.route) {
            ProfileScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                scheme = scheme
            )
        }

        // Statistics route
        composable(
            route = Screen.Statistics.route,
            deepLinks = createDeepLinks(
                "apptime://screen/statistics",
                "https://apptime.in/statistics"
            )
        ) {
            StatisticsScreen(
                modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                scheme = scheme
            )
        }

        // Focus Mode route
        composable(Screen.FocusMode.route) {
            // FocusModeScreen implementation
        }

        // App Blocking route
        composable(Screen.AppBlocking.route) {
            AppBlockingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                scheme = scheme
            )
        }

        // Leaderboard route
        composable(Screen.Leaderboard.route) {
            // LeaderboardScreen implementation
        }

        // Challenges route
        composable(
            route = Screen.Challenges.route,
            deepLinks = createDeepLinks(
                "apptime://screen/challenges",
                "apptime://screen/challenge_list",
                "https://apptime.in/challenges"
            )
        ) {
            ChallengeListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                scheme = scheme
            )
        }

        // Challenge Detail route
        composable(
            route = Screen.ChallengeDetail.route,
            deepLinks = createDeepLinks(
                "apptime://screen/challenge_detail/{challengeId}",
                "https://apptime.in/challenge_detail/{challengeId}"
            )
        ) { backStackEntry ->
            val challengeId =
                backStackEntry.arguments?.getString("challengeId") ?: return@composable
            ChallengeDetailScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                challengeId = challengeId,
                navController = navController,
                scheme = scheme
            )
        }

        // Blocked Links route
        composable(Screen.BlockedLinks.route) {
            // BlockedLinksScreen implementation
        }

        // Search route
        composable(
            route = Screen.Search.route,
            deepLinks = createDeepLinks(
                "apptime://screen/search",
                "https://apptime.in/search"
            )
        ) {
            SearchScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                scheme = scheme
            )
        }

        // Record Detail route
        composable(Screen.RecordDetail.route) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "test-user"
            RecordDetailScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                username = username
            )
        }

        // App Details route
        composable(Screen.AppDetails.route) { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
            // AppDetailsScreen implementation
        }

        // Single App Usage Detail route
        composable(Screen.SingleAppUsageDetail.route) { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
            SingleAppUsageDetailScreen(
                packageName = packageName,
                navController = navController,
                scheme = scheme
            )
        }

        // Permission route
        composable(Screen.Permission.route) {
            AppPermissionScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                onAllPermissionsGranted = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Permission.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                scheme = neutralScheme
            )
        }
    }
}

/**
 * Helper function to create deep links list.
 */
private fun createDeepLinks(vararg patterns: String) = patterns.map { pattern ->
    navDeepLink { uriPattern = pattern }
}
