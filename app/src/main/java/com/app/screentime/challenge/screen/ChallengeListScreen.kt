package com.app.screentime.challenge.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues

import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.challenge.component.QuickJoinCard
import com.app.screentime.challenge.component.SpecialEventCardV
import com.app.screentime.challenge.component.SpecialEventCardV1
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.challenge.viewmodel.ChallengesUiState
import com.app.screentime.challenge.viewmodel.JoinedChallengeViewModel
import com.app.screentime.challenge.viewmodel.ChallengeFilter
import com.app.screentime.navigation.Screen
import com.app.screentime.ui.theme.LocalThemeMode

import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSLazyRow
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@Composable
fun ChallengeListScreen(
    modifier: Modifier = Modifier,
    onNavigateToChallengeDetail: (String) -> Unit = {},
    viewModel: ChallengeViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
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
    val uiState by viewModel.uiState.collectAsState()

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
                style = DSTextStyles.subtitle,
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
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    // All Challenges Tab
                    ChallengesTab(
                        uiState = uiState,
                        onNavigateToChallengeDetail = onNavigateToChallengeDetail,
                        viewModel = viewModel,
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


@Composable
private fun ChallengesTab(
    uiState: ChallengesUiState,
    onNavigateToChallengeDetail: (String) -> Unit = {},
    viewModel: ChallengeViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val groupedChallenges = uiState.groupedChallenges

    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf(
        stringResource(R.string.all),
        "Active",
        "Past",
    )

    when {
        uiState.isLoading -> {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ODSLoadingSpinner(
                    scheme = scheme,
                    props = ODSLoadingSpinnerProps(
                        size = ODSLoadingSpinnerSize.SMALL,
                        variant = ODSLoadingSpinnerVariant.STANDARD,
                        labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL,
                        labelText = stringResource(R.string.loading)
                    )
                )
            }
        }

        uiState.error != null -> {
            ODSColumn(
                modifier = Modifier.fillMaxSize(),
                padding = ODSPadding(all = DSVariables.spacingComponent4)
            ) {
                ODSInlineNotification(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSInlineNotificationProps(
                        mode = ODSInlineNotificationMode.ERROR,
                        title = stringResource(R.string.error),
                        text = uiState.error,
                        link1Props = ODSLinkProps(
                            label = stringResource(R.string.retry)
                        ),
                        showCloseButton = false
                    ),
                    onFirstLinkClicked = {
                        viewModel.refresh()
                    }
                )
            }
        }

        else -> {
            ODSLazyColumn(
                modifier = Modifier.fillMaxSize(),
                gap = DSVariables.spacingComponent4,
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent2,
                    vertical = DSVariables.spacingComponent3
                ),
            ) {
                item {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = DSVariables.spacingComponent2
                    ) {
                        filters.forEachIndexed { index, filter ->
                            ODSToggleChip(
                                modifier = Modifier.wrapContentWidth(),
                                scheme = scheme, props = ODSToggleChipProps(
                                    label = filter, selected = selectedFilter == index
                                ), onToggle = { if (it) selectedFilter = index })
                        }
                    }
                }

                groupedChallenges?.featuredChallenge?.let { featuredChallenge ->
                    item {
                        ODSText(
                            text = stringResource(R.string.featured_challenge),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    item {
                        SpecialEventCardV(
                            challenge = featuredChallenge, onView = {
                                onNavigateToChallengeDetail(featuredChallenge.id)
                            }, scheme = featuredChallenge.getTheme()
                        )
                    }
                }


                if (groupedChallenges?.trendingChallenges?.isNotEmpty() == true) {
                    item {
                        ODSBox(modifier = Modifier.height(DSVariables.spacingComponent3)) { }
                    }
                    item {
                        ODSText(
                            text = stringResource(R.string.trending_now),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    item {
                        ODSLazyRow(
                            modifier = Modifier.fillMaxWidth(), gap = DSVariables.spacingComponent5
                        ) {
                            items(
                                items = groupedChallenges.trendingChallenges,
                                key = { it.id }) { challenge ->
                                SpecialEventCardV1(
                                    challenge = challenge,
                                    modifier = Modifier.fillParentMaxWidth(0.75f),
                                    onView = {
                                        onNavigateToChallengeDetail(challenge.id)
                                    },
                                    scheme = challenge.getTheme()
                                )
                            }
                        }
                    }
                }

                if (groupedChallenges?.specialEvents?.isNotEmpty() == true) {
                    item {
                        ODSBox(modifier = Modifier.height(DSVariables.spacingComponent3)) { }
                    }
                    item {
                        ODSText(
                            text = stringResource(R.string.special_events),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    items(groupedChallenges.specialEvents) { challenge ->
                        SpecialEventCardV(
                            challenge = challenge, modifier = Modifier.fillMaxWidth(), onView = {
                                onNavigateToChallengeDetail(challenge.id)
                            }, scheme = challenge.getTheme()
                        )
                    }
                }

                if (groupedChallenges?.quickJoinChallenges?.isNotEmpty() == true) {
                    item {
                        ODSBox(modifier = Modifier.height(DSVariables.spacingComponent3)) { }
                    }
                    item {
                        ODSText(
                            text = stringResource(R.string.quick_join),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    items(
                        items = groupedChallenges.quickJoinChallenges,
                        key = { it.id }) { challenge ->
                        QuickJoinCard(
                            challenge = challenge, modifier = Modifier.fillMaxWidth(), onJoin = {
                                onNavigateToChallengeDetail(challenge.id)
                            }, scheme = challenge.getTheme()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JoinedChallengesTab(
    onNavigateToChallengeDetail: (String) -> Unit = {},
    viewModel: JoinedChallengeViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val uiState by viewModel.uiState.collectAsState()
    val filters = listOf(
        stringResource(R.string.current),
        stringResource(R.string.expired)
    )

    when {
        uiState.isLoading -> {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ODSLoadingSpinner(
                    scheme = scheme,
                    props = ODSLoadingSpinnerProps(
                        size = ODSLoadingSpinnerSize.SMALL,
                        variant = ODSLoadingSpinnerVariant.STANDARD,
                        labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL,
                        labelText = stringResource(R.string.loading)
                    )
                )
            }
        }

        uiState.error != null -> {
            ODSColumn(
                modifier = Modifier.fillMaxSize(),
                padding = ODSPadding(all = DSVariables.spacingComponent4)
            ) {
                ODSInlineNotification(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSInlineNotificationProps(
                        mode = ODSInlineNotificationMode.ERROR,
                        title = stringResource(R.string.error),
                        text = uiState.error,
                        link1Props = ODSLinkProps(
                            label = stringResource(R.string.retry)
                        ),
                        showCloseButton = false
                    ),
                    onFirstLinkClicked = {
                        viewModel.loadJoinedChallenges()
                    }
                )
            }
        }

        uiState.filteredChallenges.isEmpty() -> {
            ODSColumn(
                modifier = Modifier.fillMaxSize(),
                padding = ODSPadding(all = DSVariables.spacingComponent4),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ODSText(
                    text = if (uiState.selectedFilter == ChallengeFilter.CURRENT) {
                        stringResource(R.string.no_current_challenges)
                    } else {
                        stringResource(R.string.no_expired_challenges)
                    },
                    style = DSTextStyles.subtitle,
                    color = scheme.basicText
                )
                ODSBox(modifier = Modifier.height(DSVariables.spacingComponent3)) { }
                ODSText(
                    text = if (uiState.selectedFilter == ChallengeFilter.CURRENT) {
                        stringResource(R.string.no_active_challenges_message)
                    } else {
                        stringResource(R.string.no_expired_challenges_message)
                    },
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }

        else -> {
            ODSLazyColumn(
                modifier = Modifier.fillMaxSize(),
                gap = DSVariables.spacingComponent4,
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent2,
                    vertical = DSVariables.spacingComponent3
                ),
            ) {
                item {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = DSVariables.spacingComponent2
                    ) {
                        filters.forEachIndexed { index, filter ->
                            val isSelected = when (index) {
                                0 -> uiState.selectedFilter == ChallengeFilter.CURRENT
                                1 -> uiState.selectedFilter == ChallengeFilter.EXPIRED
                                else -> false
                            }
                            ODSToggleChip(
                                scheme = scheme,
                                props = ODSToggleChipProps(
                                    label = filter,
                                    selected = isSelected
                                ),
                                onToggle = {
                                    if (it) {
                                        val newFilter = when (index) {
                                            0 -> ChallengeFilter.CURRENT
                                            1 -> ChallengeFilter.EXPIRED
                                            else -> ChallengeFilter.CURRENT
                                        }
                                        viewModel.setFilter(newFilter)
                                    }
                                }
                            )
                        }
                    }
                }

                items(
                    items = uiState.filteredChallenges,
                    key = { it.id }
                ) { challenge ->
                    QuickJoinCard(
                        challenge = challenge,
                        modifier = Modifier.fillMaxWidth(),
                        onJoin = {
                            onNavigateToChallengeDetail(challenge.id)
                        },
                        scheme = challenge.getTheme()
                    )
                }
            }
        }
    }
}
