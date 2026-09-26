package com.app.screentime.feature.discover.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.ConnectionLostContent
import com.app.screentime.feature.discover.HomeFeedShimmerContent
import com.app.screentime.feature.discover.HomeStoryItem
import com.app.screentime.feature.discover.MatchesFeedViewModel
import com.app.screentime.feature.discover.components.MatchFeedCard
import com.app.screentime.feature.discover.components.QuickViewProfileDialog
import com.app.screentime.feature.discover.components.StoriesRow
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesTabContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: MatchesFeedViewModel = hiltViewModel(),
    onNavigateToChat: (modelId: String, modelName: String) -> Unit = { _, _ -> },
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> },
    onExploreClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedStoryForQuickView by remember { mutableStateOf<HomeStoryItem?>(null) }

    val stories = remember(uiState.matches) {
        if (uiState.matches.isNotEmpty()) {
            uiState.matches.take(6).mapIndexed { index, match ->
                HomeStoryItem(
                    id = match.matchedUserId,
                    name = match.displayName.split(" ").firstOrNull() ?: match.displayName,
                    avatarUrl = match.primaryPhotoUrl,
                    isHighlighted = index < 2
                )
            }
        } else {
            emptyList()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading && !uiState.isRefreshing) {
            HomeFeedShimmerContent(modifier = Modifier.fillMaxSize())
        } else if (uiState.error != null && !uiState.isRefreshing && uiState.matches.isEmpty()) {
            ConnectionLostContent(
                errorMessage = uiState.error,
                onTryAgain = { viewModel.refresh() },
                onGoBackHome = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                ODSLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    gap = ODSVariables.spacingComponent3
                ) {
                    item {
                        StoriesRow(
                            stories = stories,
                            onStoryClick = { story -> selectedStoryForQuickView = story },
                            scheme = scheme
                        )
                    }
                    if (uiState.matches.isEmpty()) {

                        item {
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = ODSVariables.spacingLayout2),
                                cornerRadius = ODSCorners(all = 20.dp),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                                padding = ODSPadding(all = ODSVariables.spacingLayout3),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent4)
                                ) {
                                    ODSBox(
                                        modifier = Modifier.size(60.dp),
                                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(drawableRes = R.drawable.ic_heart_filled),
                                            tint = scheme.basicAccent.getColor(),
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    ODSText(
                                        text = "No Matches Yet",
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicText
                                    )
                                    ODSText(
                                        modifier = Modifier.padding(horizontal = ODSVariables.spacingComponent6),
                                        text = "Swipe right or like profiles in the \"For you\" tab. When they like you back, your matches will appear here!",
                                        style = ODSTextStyles.bodySRegular,
                                        color = scheme.basicTextRecessive,
                                        textAlign = TextAlign.Center
                                    )
                                    ODSRow(
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null,
                                                onClick = onExploreClick
                                            )
                                            .padding(top = ODSVariables.spacingComponent2),
                                        cornerRadius = ODSCorners(all = 20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                        padding = ODSPadding(
                                            horizontal = ODSVariables.spacingComponent6,
                                            vertical = ODSVariables.spacingComponent3
                                        )
                                    ) {
                                        ODSText(
                                            text = "Explore Profiles",
                                            style = ODSTextStyles.bodySBold,
                                            color = HexColor("#FFFFFF", 1.0f)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        itemsIndexed(
                            items = uiState.matches,
                            key = { _, match -> match.matchId }
                        ) { index, match ->
                            if (index >= uiState.matches.size - 2 && uiState.canLoadMore && !uiState.isLoadingMore && uiState.matches.size >= 5) {
                                androidx.compose.runtime.LaunchedEffect(uiState.matches.size) {
                                    viewModel.loadNextPage()
                                }
                            }
                            MatchFeedCard(
                                match = match,
                                scheme = scheme,
                                modifier = Modifier.animateItem(),
                                onCardClick = {
                                    onNavigateToProfile(
                                        match.matchedUserId,
                                        match.displayName
                                    )
                                },
                                onChat = {
                                    onNavigateToChat(
                                        match.matchedUserId,
                                        match.displayName
                                    )
                                }
                            )
                        }
                        if (uiState.isLoadingMore) {
                            item {
                                ODSBox(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = ODSVariables.spacingComponent5),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSLoadingSpinner(
                                        scheme = scheme,
                                        props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        ODSBox(
                            modifier = Modifier.height(ODSVariables.spacingComponent10 + ODSVariables.spacingComponent8)
                        )
                    }
                }
            }
        }

        selectedStoryForQuickView?.let { story ->
            QuickViewProfileDialog(
                story = story,
                onDismiss = { selectedStoryForQuickView = null },
                onNavigateToProfile = { userId, userName ->
                    selectedStoryForQuickView = null
                    onNavigateToProfile(userId, userName)
                },
                onNavigateToChat = { modelId, modelName ->
                    selectedStoryForQuickView = null
                    onNavigateToChat(modelId, modelName)
                },
                onLike = {},
                scheme = scheme
            )
        }
    }
}
