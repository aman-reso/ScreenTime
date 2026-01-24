package com.app.screentime.landing.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.Coil.imageLoader
import coil.annotation.ExperimentalCoilApi
import com.app.screentime.statistics.screen.StatisticsScreen
import com.app.screentime.statistics.viewmodel.StatisticsViewModel
import com.app.screentime.landing.viewmodel.LandingViewModel
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Adaptive landing screen that shows:
 * - Single pane (LandingScreen) on phones/compact screens
 * - Two panes (LandingScreen + StatisticsScreen) on tablets/expanded screens
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun AdaptiveLandingScreen(
    modifier: Modifier = Modifier,
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToReward: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToSingleAppUsageDetail: (String) -> Unit = {},
    onNavigateToChallengeDetail: (String) -> Unit = {},
    onNavigateToChallenges: () -> Unit = {},
    onNavigateToControlCenter: () -> Unit = {},
    onNavigateToManageLocation: () -> Unit = {},
    onNavigateToRecoverNotification: () -> Unit = {},
    onNavigateToAppLock: () -> Unit = {},
    onNavigateToFileManager: () -> Unit = {},
    onNavigateToWallpaper: () -> Unit = {},
    onNavigateToCustomisation: () -> Unit = {},
    landingViewModel: LandingViewModel = hiltViewModel(),
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    openSearchScreen: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    // Get theme mode for status bar styling
    val useDarkTheme = LocalThemeMode.current
    SideEffect {
        if (activity is ComponentActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(scheme.basicBackground.getIntColor())
                } else {
                    SystemBarStyle.light(
                        scheme.basicBackground.getIntColor(),
                        darkScrim = scheme.basicBackground.getIntColor()
                    )
                },
                navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                )
            )
        }
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)

    // Show two-pane layout on screens >= 840dp (Expanded size class)
    // Show single pane on smaller screens

    if (isExpandedScreen) {
        ODSRow(
            modifier = modifier.fillMaxSize(),
            background = listOf(ODSColorModel(scheme.basicBackground))
        ) {
            // Left pane: Landing Screen (50% width)
            ODSBox(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                LandingScreenV2(
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToLeaderboard = onNavigateToLeaderboard,
                    onNavigateToReward = {
                        onNavigateToReward()
                    },
                    onNavigateToSearch = onNavigateToSearch,
                    onNavigateToStatistics = onNavigateToStatistics,
                    onNavigateToSingleAppUsageDetail = onNavigateToSingleAppUsageDetail,
                    onNavigateToChallengeDetail = onNavigateToChallengeDetail,
                    onNavigateToChallenges = onNavigateToChallenges,
                    onNavigateToControlCenter = onNavigateToControlCenter,
                    onNavigateToManageLocation = onNavigateToManageLocation,
                    onNavigateToRecoverNotification = onNavigateToRecoverNotification,
                    onNavigateToAppLock = onNavigateToAppLock,
                    onNavigateToFileManager = onNavigateToFileManager,
                    onNavigateToWallpaper = onNavigateToWallpaper,
                    onNavigateToCustomisation = onNavigateToCustomisation,
                    viewModel = landingViewModel,
                    scheme = scheme
                )
            }

            ODSBox(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                background = listOf(ODSColorModel(scheme.basicStrokeSubtle))
            ) {}

            // Right pane: Statistics Screen (50% width)
            ODSBox(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                StatisticsScreen(
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToSingleAppUsageDetail = onNavigateToSingleAppUsageDetail,
                    viewModel = statisticsViewModel,
                    scheme = scheme
                )
            }
        }
    } else {
        LandingScreenV2(
            modifier = modifier.fillMaxSize(),
            onNavigateToLeaderboard = onNavigateToLeaderboard,
            onNavigateToReward = {
                onNavigateToReward()
            },
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToStatistics = onNavigateToStatistics,
            onNavigateToSingleAppUsageDetail = onNavigateToSingleAppUsageDetail,
            onNavigateToChallengeDetail = onNavigateToChallengeDetail,
            onNavigateToChallenges = onNavigateToChallenges,
            onNavigateToControlCenter = onNavigateToControlCenter,
            onNavigateToManageLocation = onNavigateToManageLocation,
            onNavigateToRecoverNotification = onNavigateToRecoverNotification,
            onNavigateToAppLock = onNavigateToAppLock,
            onNavigateToFileManager = onNavigateToFileManager,
            onNavigateToWallpaper = onNavigateToWallpaper,
            onNavigateToCustomisation = onNavigateToCustomisation,
            viewModel = landingViewModel,
            scheme = scheme
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            val imageLoader = imageLoader(context)
            imageLoader.memoryCache?.clear()
            imageLoader.diskCache?.clear()
        }
    }
}

