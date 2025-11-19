package com.app.screentime.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.screentime.appdetail.screen.SingleAppUsageDetailScreen
import com.app.screentime.landing.screen.LandingScreen
import com.app.screentime.record.screen.RecordDetailScreen
import com.app.screentime.search.screen.SearchScreen
import com.app.screentime.profile.screen.ProfileScreen
import com.app.screentime.statistics.screen.StatisticsScreen
import com.app.screentime.focus.screen.FocusModeScreen
import com.app.screentime.blocking.screen.AppBlockingScreen
import com.app.screentime.blocking.screen.BlockedLinksScreen
import com.app.screentime.leaderboard.screen.LeaderboardScreen
import com.app.screentime.ui.atom.AppPermissionCard
import com.app.screentime.ui.bottomnavigation.AppBottomNavigation
import com.app.screentime.ui.bottomnavigation.NavigationItem
import com.app.screentime.ui.theme.LocalAppColors

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenTimeNavigation(
    navController: NavHostController = rememberNavController()
) {
    val appColors = LocalAppColors.current ?: return
    // Define bottom navigation items
    val navigationItems = listOf(
        NavigationItem(
            label = "Home",
            icon = Icons.Default.Home,
            selectedIcon = Icons.Default.Home,
            route = Screen.Landing.route,
            badge = null
        ),
        NavigationItem(
            label = "Statistics",
            icon = Icons.Default.BarChart,
            selectedIcon = Icons.Default.Analytics,
            route = Screen.Statistics.route,
            badge = null
        ),
        NavigationItem(
            label = "Focus",
            icon = Icons.Default.Timer,
            selectedIcon = Icons.Default.Timer,
            route = Screen.FocusMode.route,
            badge = null
        ),
        NavigationItem(
            label = "Profile",
            icon = Icons.Default.Person,
            selectedIcon = Icons.Default.Person,
            route = Screen.Profile.route,
            badge = null
        )
    )

    // Get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Landing.route

    val navigateToItem: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    Scaffold(
        containerColor = appColors.background,
        topBar = {

        }, bottomBar = {
            if (currentRoute in listOf(
                    Screen.Landing.route,
                    Screen.Statistics.route,
                    Screen.Profile.route,
                    Screen.FocusMode.route,
                )
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AppBottomNavigation(
                        items = navigationItems,
                        selectedRoute = currentRoute,
                        onItemClick = navigateToItem,
                    )
                }
            }
        }, modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = Screen.Permission.route
        ) {
            composable(Screen.Landing.route) {
                LandingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    navController = navController,
                    openSearchScreen = {
                        navigateToItem.invoke(Screen.Search.route)
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    navController = navController
                )
            }

            composable(Screen.Statistics.route) {
                StatisticsScreen(
                    modifier = Modifier
                        .background(appColors.background)
                        .padding(bottom = paddingValues.calculateBottomPadding()),
                    navController = navController
                )
            }

            composable(Screen.FocusMode.route) {
                FocusModeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background)
                )
            }

            composable(Screen.AppBlocking.route) {
                AppBlockingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    navController = navController
                )
            }

            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    navController = navController
                )
            }

            composable(Screen.BlockedLinks.route) {
                BlockedLinksScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    navController = navController
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    navController = navController
                )
            }

            composable(Screen.RecordDetail.route) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: "test-user"
                RecordDetailScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    navController = navController,
                    username = username
                )
            }

            composable(Screen.AppDetails.route) { backStackEntry ->
                val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
                // AppDetailsScreen(navController = navController, packageName = packageName)
                // TODO: Implement AppDetailsScreen
            }

            composable(Screen.SingleAppUsageDetail.route) { backStackEntry ->
                val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
                SingleAppUsageDetailScreen(
                    packageName = packageName,
                    navController = navController
                )
            }
            composable(Screen.Permission.route) {
                AppPermissionCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingValues.calculateBottomPadding())
                        .background(appColors.background),
                    onAllPermissionsGranted = {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(Screen.Permission.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    })
            }
        }
    }
}
