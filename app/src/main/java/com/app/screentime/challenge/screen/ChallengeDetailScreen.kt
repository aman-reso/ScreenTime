package com.app.screentime.challenge.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.challenge.component.detail.ChallengeErrorState
import com.app.screentime.challenge.component.detail.ChallengeHeader
import com.app.screentime.challenge.component.detail.ChallengeImageSection
import com.app.screentime.challenge.component.detail.ChallengeInfoCardsSection
import com.app.screentime.challenge.component.detail.ChallengeTagsSection
import com.app.screentime.challenge.component.detail.ChallengeTitleSection
import com.app.screentime.challenge.component.detail.JoinButtonSection
import com.app.screentime.challenge.component.detail.ParticipantsSection
import com.app.screentime.challenge.component.detail.PrizeBreakdownSection
import com.app.screentime.challenge.component.detail.RulesSection
import com.app.screentime.challenge.component.detail.SponsorSection
import com.app.screentime.challenge.model.ChallengeDetailUiProps
import com.app.screentime.challenge.viewmodel.ChallengeDetailViewModel
import com.app.screentime.leaderboard.screen.LeaderboardContent
import com.app.screentime.reward.component.RewardCardV2
import com.app.screentime.ui.theme.ColorPalette
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.headerTheme
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import kotlinx.coroutines.launch

/**
 * Challenge Detail Screen using pure ODS components.
 * Displays challenge information, details, rankings, and allows joining.
 *
 * @param challengeId The ID of the challenge to display
 * @param modifier Modifier to be applied to the component
 * @param onBackClick Callback for back navigation
 * @param viewModel ViewModel for challenge data
 * @param scheme ODS theme scheme
 */
@Composable
fun ChallengeDetailScreen(
    challengeId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: ChallengeDetailViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme,
    headerScheme: ODSTheme = headerTheme.current
) {
    val uiState by viewModel.uiState.collectAsState()
    val activity = LocalActivity.current
    // Load challenge details when screen opens
    LaunchedEffect(challengeId) {
        viewModel.loadChallengeDetails(challengeId)
    }
    val useDarkTheme = LocalThemeMode.current

    // Set edge-to-edge with header scheme color
    SideEffect {
        if (activity is ComponentActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(headerScheme.basicBackgroundCard.getIntColor())
                } else {
                    SystemBarStyle.light(
                        headerScheme.basicBackgroundCard.getIntColor(),
                        darkScrim = headerScheme.basicBackgroundCard.getIntColor()
                    )
                },
                navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                )
            )
        }
    }
    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth(),
            background = listOf(ODSColorModel(headerScheme.basicBackgroundCard))
        ) {}

        when {
            uiState.isLoading -> {
                ODSBox(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        scheme = scheme, props = ODSLoadingSpinnerProps(
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL,
                            labelText = stringResource(R.string.loading)
                        )
                    )
                }
            }

            uiState.error != null -> {
                ChallengeErrorState(
                    message = uiState.error ?: "Failed to load challenge details.", onRetry = {
                        viewModel.loadChallengeDetails(challengeId)
                    }, scheme = scheme
                )
            }

            uiState.uiProps == null -> {
                ChallengeErrorState(
                    message = "Challenge not found.", onRetry = {
                        viewModel.loadChallengeDetails(challengeId)
                    }, scheme = scheme
                )
            }

            else -> {
                ChallengeContent(
                    uiProps = uiState.uiProps!!,
                    isJoining = uiState.isJoining,
                    onRefresh = {
                        viewModel.loadChallengeDetails(challengeId)
                    },
                    onJoinChallenge = {
                        viewModel.joinChallenge(
                            challengeId, onSuccess = {
                                viewModel.loadChallengeDetails(challengeId)
                            })
                    },
                    onBackClick = onBackClick,
                    viewModel = viewModel,
                    scheme = scheme,
                    headerScheme = headerScheme
                )
            }
        }
    }
}


/**
 * Header and Image section using header scheme.
 */
@Composable
private fun ChallengeHeaderAndImageSection(
    uiProps: ChallengeDetailUiProps,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    headerScheme: ODSTheme,
    imageHeight: androidx.compose.ui.unit.Dp
) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(headerScheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(
            bottomLeft = DSVariables.spacingComponent4,
            bottomRight = DSVariables.spacingComponent4
        )
    ) {
        ODSColumn {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent2
                ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChallengeHeader(
                    title = uiProps.title,
                    onBackClick = onBackClick,
                    onShareClick = onShareClick,
                    scheme = headerScheme
                )
            }

            ChallengeImageSection(
                thumbnail = uiProps.thumbnail,
                height = imageHeight
            )
        }
    }
}

/**
 * Main challenge content using ODS components.
 * Uses only UI props - no business logic.
 */
@Composable
internal fun ChallengeContent(
    uiProps: ChallengeDetailUiProps,
    isJoining: Boolean,
    onRefresh: () -> Unit,
    onJoinChallenge: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ChallengeDetailViewModel,
    scheme: ODSTheme = neutralScheme,
    headerScheme: ODSTheme = macawSecondaryScheme
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var rulesExpanded by remember { mutableStateOf(false) }

    // Tab state
    val tabElements = listOf(
        ODSTabItemModel(label = "About"),
        ODSTabItemModel(label = "Prize"),
        ODSTabItemModel(label = "Leaderboard"),
        ODSTabItemModel(label = "Reward")
    )
    val pagerState = rememberPagerState(pageCount = { tabElements.size }, initialPage = 0)
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

    // Track scroll offset for image height reduction
    var scrollOffset by remember { mutableStateOf(0f) }
    var isContentScrollable by remember { mutableStateOf(false) }

    // Shared scroll state for all tabs
    val sharedScrollState = remember { mutableIntStateOf(0) }
    val sharedIsScrollable = remember { mutableStateOf(false) }

    // Calculate image height based on scroll (min 120dp, max 240dp)
    val maxImageHeight = 240.dp
    val minImageHeight = 120.dp
    val scrollThreshold = 200f // Scroll distance to fully collapse

    // Update scroll offset when shared scroll state changes, only if content is scrollable
    LaunchedEffect(sharedScrollState.value, sharedIsScrollable.value) {
        if (sharedIsScrollable.value) {
            scrollOffset = sharedScrollState.value.toFloat().coerceAtLeast(0f)
        } else {
            scrollOffset = 0f
        }
    }

    val imageHeight by animateDpAsState(
        targetValue = when {
            !sharedIsScrollable.value || scrollOffset <= 0f -> maxImageHeight
            scrollOffset >= scrollThreshold -> minImageHeight
            else -> {
                val progress = scrollOffset / scrollThreshold
                maxImageHeight - (maxImageHeight - minImageHeight) * progress
            }
        },
        animationSpec = tween(durationMillis = 100),
        label = "imageHeight"
    )

    // NestedScrollConnection to handle nested scrolling
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            // Allow vertical scrolling to be consumed by child scrollables first
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return Offset.Zero
            }
        }
    }

    ODSColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // Header and Image section with header scheme
        ChallengeHeaderAndImageSection(
            uiProps = uiProps,
            onBackClick = onBackClick,
            onShareClick = {
                coroutineScope.launch {
                    viewModel.shareChallenge(
                        challengeId = uiProps.id,
                        title = uiProps.title,
                        prize = uiProps.displayPrize,
                        imageUrl = uiProps.thumbnail,
                        context = context
                    )
                }
            },
            headerScheme = headerScheme,
            imageHeight = imageHeight
        )

        // Sticky Tabs
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(scheme.basicBackground))
        ) {
            ODSTabs(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSTabsProps(
                    tabElements = tabElements,
                    size = ODSTabsSize.SMALL,
                    variant = ODSTabsVariant.FILL,
                    showDividerFrame = true
                ),
                selectedTabIndex = selectedTabIndex,
                onSelectedTabChange = { index ->
                    selectedTabIndex = index
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }

        // Tab Content with Pager (scrollable)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .nestedScroll(nestedScrollConnection)
        ) { page ->
            when (page) {
                0 -> {
                    // About Tab
                    AboutTab(
                        uiProps = uiProps,
                        rulesExpanded = rulesExpanded,
                        onRulesExpandedChange = { rulesExpanded = it },
                        scheme = scheme,
                        onScrollChange = { offset, isScrollable ->
                            sharedScrollState.value = offset
                            sharedIsScrollable.value = isScrollable
                        }
                    )
                }

                1 -> {
                    // Prize Tab
                    PrizeTab(
                        uiProps = uiProps,
                        scheme = scheme,
                        onScrollChange = { offset, isScrollable ->
                            sharedScrollState.value = offset
                            sharedIsScrollable.value = isScrollable
                        }
                    )
                }

                2 -> {
                    // Leaderboard Tab
                    LeaderboardTab(
                        uiProps = uiProps,
                        scheme = scheme,
                        headerScheme = headerScheme,
                        onScrollChange = { offset, isScrollable ->
                            sharedScrollState.value = offset
                            sharedIsScrollable.value = isScrollable
                        }
                    )
                }

                3 -> {
                    // Reward Tab
                    RewardTab(
                        uiProps = uiProps,
                        scheme = scheme,
                        onScrollChange = { offset, isScrollable ->
                            sharedScrollState.value = offset
                            sharedIsScrollable.value = isScrollable
                        }
                    )
                }
            }
        }

        // Fixed Join Button at bottom
        if (uiProps.showJoinButton) {
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                background = listOf(ODSColorModel(scheme.basicBackground))
            ) {
                JoinButtonSection(
                    isJoining = isJoining,
                    onJoinClick = onJoinChallenge,
                    scheme = scheme
                )
            }
        }
    }
}

// Tab Content Composables

@Composable
private fun AboutTab(
    uiProps: ChallengeDetailUiProps,
    rulesExpanded: Boolean,
    onRulesExpandedChange: (Boolean) -> Unit,
    scheme: ODSTheme,
    onScrollChange: (Int, Boolean) -> Unit = { _, _ -> }
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    // Check if content is scrollable
    LaunchedEffect(listState.layoutInfo.totalItemsCount, listState.layoutInfo.visibleItemsInfo) {
        val layoutInfo = listState.layoutInfo
        val totalHeight = layoutInfo.visibleItemsInfo.sumOf { it.size.toLong() }
        val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
        val isScrollable = totalHeight > viewportHeight

        // Track scroll and notify parent
        val scrollOffset =
            listState.firstVisibleItemIndex * 1000 + listState.firstVisibleItemScrollOffset
        onScrollChange(scrollOffset, isScrollable)
    }

    // Track scroll changes
    LaunchedEffect(listState.firstVisibleItemScrollOffset, listState.firstVisibleItemIndex) {
        val layoutInfo = listState.layoutInfo
        val totalHeight = layoutInfo.visibleItemsInfo.sumOf { it.size.toLong() }
        val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
        val isScrollable = totalHeight > viewportHeight

        val scrollOffset =
            listState.firstVisibleItemIndex * 1000 + listState.firstVisibleItemScrollOffset
        onScrollChange(scrollOffset, isScrollable)
    }

    ODSLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        gap = DSVariables.spacingComponent3,
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        )
    ) {
        item {
            ChallengeTitleSection(
                title = uiProps.title,
                description = uiProps.description,
                scheme = scheme
            )
        }

        item {
            ChallengeTagsSection(
                tags = uiProps.tags,
                scheme = scheme
            )
        }

        item {
            ChallengeInfoCardsSection(
                dateRange = uiProps.dateRange,
                duration = uiProps.duration,
                prize = uiProps.displayPrize,
                scheme = scheme
            )
        }

        item {
            ParticipantsSection(
                participantCount = uiProps.participantCount,
                scheme = scheme
            )
        }

        // Rules
        if (!uiProps.rules.isNullOrEmpty()) {
            item {
                RulesSection(
                    rules = uiProps.rules,
                    scheme = scheme,
                    expanded = rulesExpanded,
                    onExpandedChange = onRulesExpandedChange
                )
            }
        }

        // Sponsor
        if (uiProps.sponsor != null) {
            item {
                SponsorSection(
                    sponsor = uiProps.sponsor,
                    scheme = scheme
                )
            }
        }
    }
}

@Composable
private fun PrizeTab(
    uiProps: ChallengeDetailUiProps,
    scheme: ODSTheme,
    onScrollChange: (Int, Boolean) -> Unit = { _, _ -> }
) {
    val listState = rememberLazyListState()

    // Check if content is scrollable and track scroll
    LaunchedEffect(
        listState.firstVisibleItemScrollOffset,
        listState.firstVisibleItemIndex,
        listState.layoutInfo
    ) {
        val layoutInfo = listState.layoutInfo
        val totalHeight = layoutInfo.visibleItemsInfo.sumOf { it.size.toLong() }
        val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
        val isScrollable = totalHeight > viewportHeight

        val scrollOffset =
            listState.firstVisibleItemIndex * 1000 + listState.firstVisibleItemScrollOffset
        onScrollChange(scrollOffset, isScrollable)
    }

    ODSLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        gap = DSVariables.spacingComponent3,
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        )
    ) {
        if (uiProps.prize != null) {
            item {
                PrizeBreakdownSection(
                    prize = uiProps.prize,
                    scheme = scheme
                )
            }
        } else {
            item {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "No prize information available",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaderboardTab(
    uiProps: ChallengeDetailUiProps,
    scheme: ODSTheme,
    headerScheme: ODSTheme,
    onScrollChange: (Int, Boolean) -> Unit = { _, _ -> }
) {
    val rank1Scheme = ColorPalette.schemeGet(headerScheme)
    val rank2Scheme = ColorPalette.schemeGet(rank1Scheme)
    val rank3Scheme = ColorPalette.schemeGet(rank2Scheme)
    LeaderboardContent(
        uiProps.topRankings,
        null,
        scheme = scheme,
        rank1Scheme = rank1Scheme,
        rank2Scheme = rank2Scheme,
        rank3Scheme = rank3Scheme
    )
//    if (uiProps.showLeaderboard) {
//
//    } else {
//        ODSBox(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            ODSText(
//                text = "Leaderboard not available",
//                style = DSTextStyles.bodyMRegular,
//                color = scheme.basicTextRecessive
//            )
//        }
//    }
}

@Composable
private fun RewardTab(
    uiProps: ChallengeDetailUiProps,
    scheme: ODSTheme,
    onScrollChange: (Int, Boolean) -> Unit = { _, _ -> }
) {
    val listState = rememberLazyListState()

    // Check if content is scrollable and track scroll
    LaunchedEffect(
        listState.firstVisibleItemScrollOffset,
        listState.firstVisibleItemIndex,
        listState.layoutInfo
    ) {
        val layoutInfo = listState.layoutInfo
        val totalHeight = layoutInfo.visibleItemsInfo.sumOf { it.size.toLong() }
        val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
        val isScrollable = totalHeight > viewportHeight

        val scrollOffset =
            listState.firstVisibleItemIndex * 1000 + listState.firstVisibleItemScrollOffset
        onScrollChange(scrollOffset, isScrollable)
    }

    ODSLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        gap = DSVariables.spacingComponent3,
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        )
    ) {
        if (uiProps.availableRewards.isNotEmpty()) {
            item {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSText(
                        text = "Available rewards",
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText
                    )
                    LazyRow(
                        state = rememberLazyListState(),
                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                        contentPadding = PaddingValues(horizontal = 0.dp)
                    ) {
                        items(uiProps.availableRewards) { reward ->
                            RewardCardV2(
                                title = reward.title,
                                description = reward.description,
                                coin = reward.coin,
                                image = reward.imageUrl?.let {
                                    ODSImageModel(
                                        url = it,
                                        contentDescription = reward.title
                                    )
                                },
                                tag = reward.tagUrl?.let {
                                    ODSImageModel(
                                        url = it,
                                        contentDescription = "Reward tag"
                                    )
                                },
                                onClaimClick = {
                                    // TODO: Handle claim click
                                },
                                onClick = {
                                    // TODO: Handle card click
                                },
                                modifier = Modifier.fillParentMaxWidth(0.6f),
                                scheme = scheme
                            )
                        }
                    }
                }
            }
        } else {
            item {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "No rewards available",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}


