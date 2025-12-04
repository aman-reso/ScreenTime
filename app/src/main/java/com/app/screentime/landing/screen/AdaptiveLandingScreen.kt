package com.app.screentime.landing.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.statistics.screen.StatisticsScreen
import com.app.screentime.statistics.viewmodel.StatisticsViewModel
import com.app.screentime.landing.screen.LandingScreenV2
import com.app.screentime.landing.viewmodel.LandingViewModel
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.whiteScheme

/**
 * Adaptive landing screen that shows:
 * - Single pane (LandingScreen) on phones/compact screens
 * - Two panes (LandingScreen + StatisticsScreen) on tablets/expanded screens
 */
@Composable
fun AdaptiveLandingScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    landingViewModel: LandingViewModel = hiltViewModel(),
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    openSearchScreen: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
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
                    navController = navController,
                    viewModel = landingViewModel,
                    openSearchScreen = openSearchScreen,
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
                    navController = navController,
                    viewModel = statisticsViewModel,
                    scheme = scheme
                )
            }
        }
    } else {
        LandingScreenV2(
            modifier = modifier.fillMaxSize(),
            navController = navController,
            viewModel = landingViewModel,
            openSearchScreen = openSearchScreen,
            scheme = scheme
        )
    }
}

