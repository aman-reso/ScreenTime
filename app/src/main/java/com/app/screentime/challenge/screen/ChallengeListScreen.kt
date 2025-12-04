package com.app.screentime.challenge.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.app.screentime.navigation.Screen

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
    navController: NavController? = null,
    viewModel: ChallengeViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {

    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf("Challenges", "Joined")
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
        ODSTabItemModel(label = "Challenges"), ODSTabItemModel(label = "Joined")
    )

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
    ) {
        ODSBox(
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSText(
                text = "Challenges", style = DSTextStyles.subtitle, color = scheme.basicText
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
                        navController = navController,
                        viewModel = viewModel,
                        scheme = scheme
                    )
                }

                1 -> {
//                    // Joined Challenges Tab
//                    JoinedChallengesTab(
//                        uiState = uiState,
//                        navController = navController,
//                        viewModel = viewModel,
//                        scheme = scheme
//                    )
                }
            }
        }
    }
}


@Composable
private fun ChallengesTab(
    uiState: ChallengesUiState,
    navController: NavController?,
    viewModel: ChallengeViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val groupedChallenges = uiState.groupedChallenges

    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("All", "Fitness", "Mindfulness", "Coding")

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
                                scheme = scheme, props = ODSToggleChipProps(
                                    label = filter, selected = selectedFilter == index
                                ), onToggle = { if (it) selectedFilter = index })
                        }
                    }
                }

                groupedChallenges?.featuredChallenge?.let { featuredChallenge ->
                    item {
                        ODSText(
                            text = "Featured Challenge",
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    item {
                        SpecialEventCardV(
                            challenge = featuredChallenge, onView = {
                                navController?.navigate(
                                    Screen.ChallengeDetail.createRoute(featuredChallenge.id)
                                )
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
                            text = "Trending Now",
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
                                        navController?.navigate(
                                            Screen.ChallengeDetail.createRoute(challenge.id)
                                        )
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
                            text = "Special Events",
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    items(groupedChallenges.specialEvents) { challenge ->
                        SpecialEventCardV(
                            challenge = challenge, modifier = Modifier.fillMaxWidth(), onView = {
                                navController?.navigate(
                                    Screen.ChallengeDetail.createRoute(challenge.id)
                                )
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
                            text = "Quick Join",
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    items(
                        items = groupedChallenges.quickJoinChallenges,
                        key = { it.id }) { challenge ->
                        QuickJoinCard(
                            challenge = challenge, modifier = Modifier.fillMaxWidth(), onJoin = {
                                navController?.navigate(
                                    Screen.ChallengeDetail.createRoute(challenge.id)
                                )
                            }, scheme = challenge.getTheme()
                        )
                    }
                }
            }
        }
    }
}

//
//@Composable
//private fun JoinedChallengesTab(
//    uiState: com.app.screentime.challenge.viewmodel.ChallengesUiState,
//    navController: NavController?,
//    viewModel: ChallengeViewModel,
//    scheme: ODSTheme = neutralScheme
//) {
//
//    val joinedChallenges = uiState.challenges.filter { it.hasJoined }
//
//    ODSColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        when {
//            uiState.isLoading -> {
//                ODSBox(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f),
//                    contentAlignment = Alignment.Center
//                ) {
//                    ODSText(
//                        text = "Loading...",
//                        style = DSTextStyles.bodyMRegular,
//                        color = scheme.basicTextRecessive
//                    )
//                }
//            }
//
//            uiState.error != null -> {
//                ODSColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .padding(12.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    ODSText(
//                        text = uiState.error ?: "Unable to load challenges.",
//                        style = DSTextStyles.bodyMRegular,
//                        color = scheme.functionalDestructiveStandard
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    ODSButton(
//                        scheme = scheme,
//                        props = ODSButtonProps(
//                            label = "Retry",
//                            variant = ODSButtonVariant.OUTLINE
//                        ),
//                        onClick = viewModel::refresh
//                    )
//                }
//            }
//
//            joinedChallenges.isEmpty() -> {
//                ODSColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .padding(32.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Flag,
//                        contentDescription = null,
//                        tint = scheme.basicTextRecessive,
//                        modifier = Modifier.size(48.dp)
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//                    ODSText(
//                        text = "No joined challenges",
//                        style = DSTextStyles.subtitle,
//                        color = scheme.basicText
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//                    ODSText(
//                        text = "Join challenges from the Challenges tab to see them here.",
//                        style = DSTextStyles.bodyMRegular,
//                        color = scheme.basicTextRecessive,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//
//            else -> {
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(2),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f),
//                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    itemsIndexed(
//                        joinedChallenges,
//                        key = { _, challenge -> challenge.id },
//                        span = { index, _ ->
//                            val config = getCardSizeConfig(index)
//                            GridItemSpan(config.span)
//                        }
//                    ) { index, challenge ->
//                        val config = getCardSizeConfig(index)
//                        CurrentChallengeCard(
//                            challenge = challenge,
//                            index = index,
//                            cardHeight = config.height.dp,
//                            useOverlayDesign = config.pattern == 0,
//                            useHorizontalDesign = config.pattern == 2,
//                            isJoining = uiState.joiningChallengeIds.contains(challenge.id),
//                            onViewDetails = {
//                                navController?.navigate(
//                                    Screen.ChallengeDetail.createRoute(challenge.id.toString())
//                                )
//                            },
//                            onJoin = {
//                                viewModel.joinChallenge(challenge.id)
//                            },
//                            scheme = scheme
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
