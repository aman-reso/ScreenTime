package com.app.screentime.challenge.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.Rule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.challenge.component.detail.ChallengeErrorState
import com.app.screentime.challenge.component.detail.ChallengeHeader
import com.app.screentime.challenge.component.detail.ChallengeImageSection
import com.app.screentime.challenge.component.detail.JoinButtonSection
import com.app.screentime.challenge.model.ChallengeDetailUiProps
import com.app.screentime.challenge.viewmodel.ChallengeDetailViewModel
import com.app.screentime.ui.atom.AppScreenShimmer
import com.app.screentime.leaderboard.screen.LeaderboardItem
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
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
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
 * Format participant count for display (e.g., 2100 -> "2.1K")
 */
private fun formatParticipantCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

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
                }, navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT, Color.TRANSPARENT
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
                .height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .fillMaxWidth(),
            background = listOf(ODSColorModel(headerScheme.basicBackgroundCard))
        ) {}

        when {
            uiState.isLoading -> {
                ODSLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
                    gap = DSVariables.spacingComponent3
                ) {
                    item {
                        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                    }
                    item {
                        AppScreenShimmer(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme
                        )
                    }
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
                        viewModel.joinChallenge(challengeId, onSuccess = {
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
    headerScheme: ODSTheme
) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(headerScheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(
            bottomLeft = DSVariables.spacingComponent4, bottomRight = DSVariables.spacingComponent4
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

    ODSBox(
        modifier = Modifier.fillMaxSize()
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            ChallengeHeaderAndImageSection(
                uiProps = uiProps, onBackClick = onBackClick, onShareClick = {
                    coroutineScope.launch {
                        viewModel.shareChallenge(
                            challengeId = uiProps.id,
                            title = uiProps.title,
                            prize = uiProps.displayPrize,
                            imageUrl = uiProps.thumbnail,
                            context = context
                        )
                    }
                }, headerScheme = headerScheme
            )

            AboutTab(
                uiProps = uiProps, 
                scheme = scheme,
                bottomPadding = if (uiProps.showJoinButton) DSVariables.spacingLayout10 else 0.dp
            )
        }

        if (uiProps.showJoinButton) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                background = listOf(ODSColorModel(scheme.basicBackground))
            ) {
                JoinButtonSection(
                    isJoining = isJoining, onJoinClick = onJoinChallenge, scheme = scheme
                )
            }
        }
    }
}

@Composable
private fun AboutTab(
    uiProps: ChallengeDetailUiProps,
    scheme: ODSTheme,
    bottomPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    var showRulesBottomSheet by remember { mutableStateOf(false) }
    val headerScheme = headerTheme.current

    ODSLazyColumn(
        state = listState, 
        modifier = Modifier.fillMaxSize(), 
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4, 
            vertical = DSVariables.spacingComponent3,
            bottom = bottomPadding
        )
    ) {
        item {
            ChallengeImageSection(
                thumbnail = uiProps.thumbnail
            )
        }
        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent5, top = DSVariables.spacingComponent5
                )
            ) {
                ODSText(
                    text = uiProps.title,
                    style = DSTextStyles.subtitle,
                    color = scheme.basicText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        if (uiProps.tags.isNotEmpty()) {
            item {
                ODSWrap(
                    modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent5
                    )
                ) {
                    uiProps.tags.forEach { tag ->
                        ODSTagStatic(
                            scheme = scheme, props = ODSTagStaticProps(
                                label = tag, type = ODSTagStaticType.STRONG
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = "Total Prize Pool",
                        labelText = uiProps.prize,
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = "Reward",
                        labelText = uiProps.reward,
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = "Date Range",
                        labelText = uiProps.dateRange,
                        showDescriptionTitle = false,
                        icon = ODSIconModel(
                            imageVector = Icons.Default.DateRange,
                            tint = scheme.basicTextRecessive,
                            contentDescription = "Date"
                        ),
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = "Description",
                        labelText = uiProps.description,
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = "Joined Member",
                        labelText = "${formatParticipantCount(uiProps.participantCount)} Participants",
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        if (!uiProps.sponsor.isNullOrEmpty()) {
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                        vertical = DSVariables.spacingComponent5
                    )
                ) {
                    ODSListRowStandard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DSVariables.spacingComponent5)
                            .semantics(mergeDescendants = true) {},
                        scheme = scheme,
                        props = ODSListRowStandardProps(
                            label = "Sponsor",
                            labelText = uiProps.sponsor,
                            showDescriptionTitle = false,
                            variant = ODSListRowStandardVariant.STANDARD
                        ),
                    )
                }
                ODSDivider(
                    scheme = scheme, props = ODSDividerProps(
                        inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                    )
                )
            }
        }
        if (!uiProps.rules.isNullOrEmpty()) {
            item {
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent7))
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = ODSPadding(all = 0.dp),
                    contentSlot = {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showRulesBottomSheet = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSListRowStandard(
                                modifier = Modifier.weight(0.9f),
                                scheme = scheme,
                                props = ODSListRowStandardProps(
                                    variant = ODSListRowStandardVariant.ICON,
                                    label = "Rules",
                                    icon = ODSIconModel(
                                        imageVector = Icons.Outlined.Rule,
                                        tint = scheme.basicTextRecessive,
                                        contentDescription = "Rules"
                                    )
                                )
                            )
                            ODSIcon(
                                modifier = Modifier.weight(0.1f), iconModel = ODSIconModel(
                                    tint = scheme.basicText,
                                    drawableRes = com.telekom.odsystem.R.drawable.right_condensed_type_standard,
                                    contentDescription = "View Rules"
                                )
                            )
                        }
                    })
            }
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent5,
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSText(
                    text = "Leaderboard",
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        leaderboardTab(
            uiProps = uiProps, scheme = scheme, headerScheme = headerScheme
        )
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
        }

    }

// Rules Bottom Sheet
    if (!uiProps.rules.isNullOrEmpty()) {
        RulesBottomSheet(
            showBottomSheet = showRulesBottomSheet,
            rules = uiProps.rules,
            onDismiss = { showRulesBottomSheet = false },
            scheme = scheme
        )
    }
}

private fun LazyListScope.leaderboardTab(
    uiProps: ChallengeDetailUiProps, scheme: ODSTheme, headerScheme: ODSTheme
) {
    val rank1Scheme = ColorPalette.schemeGet(headerScheme)
    val rank2Scheme = ColorPalette.schemeGet(rank1Scheme)
    val rank3Scheme = ColorPalette.schemeGet(rank2Scheme)

    if (uiProps.topRankings.isNotEmpty()) {
        uiProps.topRankings.forEachIndexed { index, entry ->
            val itemScheme = when (entry.rank) {
                1 -> rank1Scheme
                2 -> rank2Scheme
                3 -> rank3Scheme
                else -> scheme
            }
            item {
                LeaderboardItem(
                    entry = entry,
                    isCurrentUser = entry.userId == "##Current User id",
                    scheme = itemScheme
                )
                if (index < uiProps.topRankings.size - 1) {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                }
            }
        }
    } else {
        item {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DSVariables.spacingComponent5),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                gap = DSVariables.spacingComponent3
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.EmojiEvents,
                        tint = scheme.basicTextRecessive,
                        contentDescription = null
                    ), modifier = Modifier.size(64.dp)
                )
                ODSText(
                    text = stringResource(R.string.no_leaderboard_data_available),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
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
            horizontal = DSVariables.spacingComponent4, vertical = DSVariables.spacingComponent3
        )
    ) {
        if (uiProps.availableRewards.isNotEmpty()) {
            item {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(), gap = DSVariables.spacingComponent2
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
                                        url = it, contentDescription = reward.title
                                    )
                                },
                                tag = reward.tagUrl?.let {
                                    ODSImageModel(
                                        url = it, contentDescription = "Reward tag"
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
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
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

/**
 * Bottom sheet for displaying challenge rules
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RulesBottomSheet(
    showBottomSheet: Boolean, rules: String, onDismiss: () -> Unit, scheme: ODSTheme = neutralScheme
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = showBottomSheet,
        bottomSheetState = bottomSheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSText(
                text = "Rules", style = DSTextStyles.titleS, color = scheme.basicText
            )
        },
        contentSlot = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DSVariables.spacingComponent4),
                verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
            ) {
                ODSText(
                    text = AnnotatedString.fromHtml(rules),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )
            }
        })
}


