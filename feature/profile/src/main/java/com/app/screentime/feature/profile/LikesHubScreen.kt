package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R as ConfigR
import com.app.screentime.core.ui.components.GenericHeader
import com.app.screentime.core.ui.components.GenericHeaderProps
import com.app.screentime.core.ui.components.HeaderTitleAlignment
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * Likes Hub Screen containing two tabs:
 * 1. "Who Liked Me"
 * 2. "Whom I Like" (Liked Profiles)
 *
 * Implements HorizontalPager with ODSTabs navigation.
 */
@Composable
fun LikesHubScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    initialTab: Int = 0,
    onNavigateToProfile: (String, String) -> Unit = { _, _ -> },
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToDiscover: () -> Unit = {}
) {
    val pagerState = rememberPagerState(
        initialPage = initialTab.coerceIn(0, 1),
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf("Liked Me", "I Liked")

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            ODSTabs(
                modifier = Modifier.fillMaxWidth().padding(horizontal = DSVariables.spacingComponent4),
                scheme = scheme,
                selectedTabIndex = pagerState.currentPage,
                props = ODSTabsProps(
                    showDividerFrame = true,
                    size = ODSTabsSize.SMALL,
                    variant = ODSTabsVariant.FILL,
                    tabElements = tabTitles.map { ODSTabItemModel(label = it) }
                ),
                onSelectedTabChange = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> {
                        WhoLikedMeScreen(
                            modifier = Modifier.fillMaxSize(),
                            scheme = scheme,
                            showHeader = false,
                            onNavigateToProfile = onNavigateToProfile,
                            onNavigateToChat = onNavigateToChat,
                            onNavigateToDiscover = onNavigateToDiscover
                        )
                    }

                    1 -> {
                        LikedProfilesScreen(
                            modifier = Modifier.fillMaxSize(),
                            scheme = scheme,
                            showHeader = false,
                            onNavigateToProfile = onNavigateToProfile,
                            onNavigateToChat = onNavigateToChat,
                            onNavigateToDiscover = onNavigateToDiscover
                        )
                    }
                }
            }
        }
    }
}
