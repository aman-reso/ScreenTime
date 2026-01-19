package com.app.screentime.challenge.screen

import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.config.data.Feature
import com.app.screentime.config.featureflag.FeatureFlagHelper
import com.app.screentime.challenge.screen.ChallengesTab
import com.app.screentime.challenge.screen.JoinedChallengesTab
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeListScreen(
    modifier: Modifier = Modifier,
    onNavigateToChallengeDetail: (String) -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {

    val activity = LocalActivity.current
    val useDarkTheme = LocalThemeMode.current

    SideEffect {
        if (activity is AppCompatActivity) {
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

    val tabs = listOf(stringResource(R.string.challenges), stringResource(R.string.joined))
    val pagerState = rememberPagerState(pageCount = { tabs.size }, initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Sync tab selection with pager state
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    // Sync pager with tab selection
    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(selectedTabIndex)
            }
        }
    }

    // Tab elements
    val tabElements = listOf(
        ODSTabItemModel(label = stringResource(R.string.challenges)),
        ODSTabItemModel(label = stringResource(R.string.joined))
    )

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        ODSBox(
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSText(
                text = stringResource(R.string.challenges),
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
        }

        ODSTabs(
            modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSTabsProps(
                tabElements = tabElements,
                size = ODSTabsSize.SMALL,
                variant = ODSTabsVariant.FILL,
                showDividerFrame = true
            ), selectedTabIndex = selectedTabIndex, onSelectedTabChange = { index ->
                selectedTabIndex = index
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            })

        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 2 // 👈 CACHE 2 pages on each side
        ) { page ->
            when (page) {
                0 -> {
                    ChallengesTab(
                        onNavigateToChallengeDetail = onNavigateToChallengeDetail,
                        scheme = scheme
                    )
                }

                1 -> {
                    JoinedChallengesTab(
                        onNavigateToChallengeDetail = onNavigateToChallengeDetail,
                        scheme = scheme
                    )
                }
            }
        }
    }
}