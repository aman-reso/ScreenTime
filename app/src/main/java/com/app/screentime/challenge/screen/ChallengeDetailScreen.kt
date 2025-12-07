package com.app.screentime.challenge.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.app.screentime.R
import com.app.screentime.challenge.component.detail.ChallengeErrorState
import com.app.screentime.challenge.component.detail.ChallengeHeader
import com.app.screentime.challenge.component.detail.ChallengeImageSection
import com.app.screentime.challenge.component.detail.ChallengeInfoCardsSection
import com.app.screentime.challenge.component.detail.ChallengeTagsSection
import com.app.screentime.challenge.component.detail.ChallengeTitleSection
import com.app.screentime.challenge.component.detail.JoinButtonSection
import com.app.screentime.challenge.component.detail.LeaderboardSection
import com.app.screentime.challenge.component.detail.ParticipantsSection
import com.app.screentime.challenge.component.detail.PrizeBreakdownSection
import com.app.screentime.challenge.component.detail.RulesSection
import com.app.screentime.challenge.component.detail.SponsorSection
import com.app.screentime.challenge.model.ChallengeDetailUiProps
import com.app.screentime.challenge.viewmodel.ChallengeDetailViewModel
import com.app.screentime.reward.component.RewardCardV2
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

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
    scheme: ODSTheme = neutralScheme
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load challenge details when screen opens
    LaunchedEffect(challengeId) {
        viewModel.loadChallengeDetails(challengeId)
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
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
                    scheme = neutralScheme
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
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var rulesExpanded by remember { mutableStateOf(false) }

    // Helper function to find index by key
    fun findIndexByKey(key: Any): Int {
        val layoutInfo = listState.layoutInfo
        return layoutInfo.visibleItemsInfo.indexOfFirst {
            it.key == key
        }.takeIf { it >= 0 }
            ?: run {
                0
            }
    }

    ODSBox(
        modifier = Modifier.fillMaxSize()
    ) {
        ODSLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            gap = DSVariables.spacingComponent3,
            padding = ODSPadding(
                horizontal = DSVariables.spacingComponent4,
                bottom = if (uiProps.showJoinButton) DSVariables.spacingComponent9 else DSVariables.spacingComponent3
            )
        )
        {
            item(key = "header") {
                ChallengeHeader(
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
                    scheme = scheme
                )
            }

            // Challenge Image
            item(key = "image") {
                ChallengeImageSection(
                    thumbnail = uiProps.thumbnail, reward = uiProps.reward, scheme = scheme
                )
            }

            // Title and Description
            item(key = "title") {
                ChallengeTitleSection(
                    title = uiProps.title, description = uiProps.description, scheme = scheme
                )
            }

            // Tags
            item(key = "tags") {
                ChallengeTagsSection(
                    tags = uiProps.tags, scheme = scheme
                )
            }

            item(key = "infoCards") {
                ChallengeInfoCardsSection(
                    dateRange = uiProps.dateRange,
                    duration = uiProps.duration,
                    prize = uiProps.displayPrize,
                    scheme = scheme
                )
            }

            if (uiProps.prize != null) {
                item(key = "prizeBreakdown") {
                    PrizeBreakdownSection(
                        prize = uiProps.prize, scheme = scheme
                    )
                }
            }

            item(key = "participants") {
                ParticipantsSection(
                    participantCount = uiProps.participantCount, scheme = scheme
                )
            }

            // Leaderboard
            if (uiProps.showLeaderboard) {
                item(key = "leaderboard") {
                    LeaderboardSection(
                        rankings = uiProps.topRankings,
                        userRank = uiProps.userRank,
                        scheme = scheme
                    )
                }
            }

            // Available Rewards
            if (uiProps.availableRewards.isNotEmpty()) {
                item(key = "availableRewards") {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
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
            }

            // Rules
            if (!uiProps.rules.isNullOrEmpty()) {
                item(key = "rules") {
                    RulesSection(
                        rules = uiProps.rules,
                        scheme = scheme,
                        onExpandedChange = { expanded ->
                            rulesExpanded = expanded
                        }
                    )
                }
            }

            // Sponsor
            if (uiProps.sponsor != null) {
                item(key = "sponsor") {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                    SponsorSection(
                        sponsor = uiProps.sponsor, scheme = scheme
                    )
                }
            }
        }

        // Scroll to Rules section when it expands
        LaunchedEffect(rulesExpanded) {
            if (rulesExpanded) {
                coroutineScope.launch {
                    val rulesIndex = findIndexByKey("rules")
                    if (rulesIndex >= 0) {
                        listState.animateScrollToItem(rulesIndex)
                    }
                }
            }
        }

        // Fixed Join Button at bottom
        if (uiProps.showJoinButton) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
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


