package com.app.screentime.feature.discover.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.ConnectionLostContent
import com.app.screentime.feature.discover.ForYouFeedViewModel
import com.app.screentime.feature.discover.HomeFeedShimmerContent
import com.app.screentime.feature.discover.HomeStoryItem
import com.app.screentime.feature.discover.components.HomeFeedCard
import com.app.screentime.feature.discover.components.HomeFeedEmptyCard
import com.app.screentime.feature.discover.components.MatchModal
import com.app.screentime.feature.discover.components.QuickViewProfileDialog
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ForYouTabContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: ForYouFeedViewModel = hiltViewModel(),
    onNavigateToChat: (modelId: String, modelName: String) -> Unit = { _, _ -> },
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedStoryForQuickView by remember { mutableStateOf<HomeStoryItem?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            HomeFeedShimmerContent(modifier = Modifier.fillMaxSize())
        } else if (uiState.error != null) {
            ConnectionLostContent(
                errorMessage = uiState.error,
                onTryAgain = { viewModel.loadDeck() },
                onGoBackHome = { viewModel.resetDeck() },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            ODSLazyColumn(
                modifier = Modifier.fillMaxSize(),
                gap = ODSVariables.spacingComponent3
            ) {
                if (uiState.models.isEmpty()) {
                    item {
                        HomeFeedEmptyCard(onRefreshFeed = { viewModel.resetDeck() })
                    }
                } else {
                    itemsIndexed(
                        items = uiState.models,
                        key = { _, profile -> profile.id }
                    ) { index, profile ->
                        if (index >= uiState.models.size - 2 && uiState.canLoadMore && !uiState.isLoadingMore && uiState.models.size >= 5) {
                            androidx.compose.runtime.LaunchedEffect(uiState.models.size) {
                                viewModel.loadNextPage()
                            }
                        }
                        val isLiked = profile.id in uiState.likedModelIds
                        HomeFeedCard(
                            profile = profile,
                            scheme = scheme,
                            isLiked = isLiked,
                            modifier = Modifier.animateItem(),
                            onCardClick = {
                                onNavigateToProfile(profile.id, profile.name)
                            },
                            onChat = {
                                onNavigateToChat(profile.id, profile.name)
                            },
                            onLike = {
                                viewModel.onLikeById(profile.id)
                            },
                            onDislike = {
                                viewModel.onDislikeById(profile.id)
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

        AnimatedVisibility(
            visible = uiState.isMatched && uiState.matchedModel != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            val match = uiState.matchedModel
            if (match != null) {
                MatchModal(
                    matchedModel = match,
                    onChat = {
                        viewModel.dismissMatchDialog()
                        onNavigateToChat(match.id, match.name)
                    },
                    onKeepBrowsing = {
                        viewModel.dismissMatchDialog()
                    }
                )
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
                onLike = { modelId ->
                    viewModel.onLikeById(modelId)
                },
                scheme = scheme
            )
        }
    }
}
