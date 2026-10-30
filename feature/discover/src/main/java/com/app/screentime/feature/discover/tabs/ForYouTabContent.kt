package com.app.screentime.feature.discover.tabs

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.ads.InterstitialAdManager
import com.app.screentime.ads.NativeAdCardManager
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.ConnectionLostContent
import com.app.screentime.feature.discover.DiscoveryDeckItem
import com.app.screentime.feature.discover.ForYouFeedViewModel
import com.app.screentime.feature.discover.HomeFeedShimmerContent
import com.app.screentime.feature.discover.HomeStoryItem
import com.app.screentime.feature.discover.components.HomeFeedActionButtonsRow
import com.app.screentime.feature.discover.components.HomeFeedAdCard
import com.app.screentime.feature.discover.components.HomeFeedCard
import com.app.screentime.feature.discover.components.HomeFeedEmptyCard
import com.app.screentime.feature.discover.components.MatchModal
import com.app.screentime.feature.discover.components.QuickViewProfileDialog
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * For You Feed Tab Screen with Tinder-style swipeable card deck.
 * Supports:
 * - Fluid GPU-accelerated horizontal swipe animations
 * - Seamless in-deck Tinder-style Native Video Ad Cards (with swipe action in the same frame)
 * - Remote Config controlled intervals (5-10 swipes)
 * - 100% Telekom ODS compliance
 */
@Composable
fun ForYouTabContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: ForYouFeedViewModel = hiltViewModel(),
    onNavigateToChat: (modelId: String, modelName: String) -> Unit = { _, _ -> },
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> },
    onNavigateToPreferences: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    var selectedStoryForQuickView by remember { mutableStateOf<HomeStoryItem?>(null) }
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        activity?.let {
            NativeAdCardManager.preloadAd(it)
            InterstitialAdManager.preloadAd(it)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            HomeFeedShimmerContent(
                modifier = Modifier.fillMaxSize(),
                scheme = scheme
            )
        } else if (uiState.error != null) {
            ConnectionLostContent(
                errorMessage = uiState.error,
                onTryAgain = { viewModel.loadDeck() },
                onGoBackHome = { viewModel.resetDeck() },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val currentItem = uiState.deckItems.firstOrNull()
            val nextItem = uiState.deckItems.getOrNull(1)
            val thirdItem = uiState.deckItems.getOrNull(2)

            // Trigger pagination early when remaining deck size is low
            LaunchedEffect(uiState.models.size) {
                if (uiState.models.size <= 3 && uiState.canLoadMore && !uiState.isLoadingMore) {
                    viewModel.loadNextPage()
                }
            }

            if (currentItem == null) {
                val isLimit = uiState.quotaLimit?.isLimitReached == true
                HomeFeedEmptyCard(
                    scheme = scheme,
                    title = if (isLimit) "Daily Limit Reached" else androidx.compose.ui.res.stringResource(com.app.screentime.config.R.string.home_no_matches_title),
                    description = if (isLimit) (uiState.quotaLimit?.limitMessage ?: "You've viewed all available profiles for today. Come back tomorrow!") else androidx.compose.ui.res.stringResource(com.app.screentime.config.R.string.home_no_matches_description),
                    onRefreshFeed = { viewModel.resetDeck() },
                    onChangePreferences = onNavigateToPreferences
                )
            } else {
                // Swipe horizontal Animatable offset tracking for active top card
                val animatableOffsetX = remember(currentItem.id) { Animatable(0f) }
                var isAnimatingSwipeOut by remember(currentItem.id) { mutableStateOf(false) }

                val animatedOffsetX = animatableOffsetX.value
                val rotation = (animatedOffsetX / 22f).coerceIn(-18f, 18f)

                val swipeThresholdPx = with(density) { 110.dp.toPx() }

                fun triggerDislike() {
                    if (isAnimatingSwipeOut) return
                    isAnimatingSwipeOut = true
                    coroutineScope.launch {
                        animatableOffsetX.animateTo(
                            targetValue = -1000f,
                            animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing)
                        )
                        when (currentItem) {
                            is DiscoveryDeckItem.ProfileCard -> {
                                viewModel.onDislikeById(currentItem.profile.id, context)
                            }
                            is DiscoveryDeckItem.AdCard -> {
                                viewModel.onAdSwiped(currentItem.id, context)
                            }
                        }
                    }
                }

                fun triggerLike() {
                    if (isAnimatingSwipeOut) return
                    isAnimatingSwipeOut = true
                    coroutineScope.launch {
                        animatableOffsetX.animateTo(
                            targetValue = 1000f,
                            animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing)
                        )
                        when (currentItem) {
                            is DiscoveryDeckItem.ProfileCard -> {
                                viewModel.onLikeById(currentItem.profile.id, context)
                            }
                            is DiscoveryDeckItem.AdCard -> {
                                currentItem.nativeAd.performClick(android.os.Bundle())
                                viewModel.onAdSwiped(currentItem.id, context)
                            }
                        }
                    }
                }

                ODSColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val swipeProgress = (kotlin.math.abs(animatedOffsetX) / 300f).coerceIn(0f, 1f)
                        val offset12Px = with(density) { 12.dp.toPx() }
                        val offset24Px = with(density) { 24.dp.toPx() }

                        // 3rd background card in stack (deepest depth layer - GPU accelerated)
                        if (thirdItem != null) {
                            key(thirdItem.id) {
                                val thirdScale = 0.88f + (0.06f * swipeProgress)
                                val thirdOffsetYPx = offset24Px * (1f - swipeProgress)
                                val thirdModifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        scaleX = thirdScale
                                        scaleY = thirdScale
                                        translationY = thirdOffsetYPx
                                    }
                                when (thirdItem) {
                                    is DiscoveryDeckItem.ProfileCard -> {
                                        HomeFeedCard(
                                            profile = thirdItem.profile,
                                            scheme = scheme,
                                            modifier = thirdModifier,
                                            onCardClick = {}
                                        )
                                    }
                                    is DiscoveryDeckItem.AdCard -> {
                                        HomeFeedAdCard(
                                            nativeAd = thirdItem.nativeAd,
                                            scheme = scheme,
                                            modifier = thirdModifier,
                                            onCardClick = {}
                                        )
                                    }
                                }
                            }
                        }

                        // 2nd background card (immediate next card pre-rendered underneath - GPU accelerated)
                        if (nextItem != null) {
                            key(nextItem.id) {
                                val nextScale = 0.94f + (0.06f * swipeProgress)
                                val nextOffsetYPx = offset12Px * (1f - swipeProgress)
                                val nextModifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        scaleX = nextScale
                                        scaleY = nextScale
                                        translationY = nextOffsetYPx
                                    }
                                when (nextItem) {
                                    is DiscoveryDeckItem.ProfileCard -> {
                                        HomeFeedCard(
                                            profile = nextItem.profile,
                                            scheme = scheme,
                                            modifier = nextModifier,
                                            onCardClick = {}
                                        )
                                    }
                                    is DiscoveryDeckItem.AdCard -> {
                                        HomeFeedAdCard(
                                            nativeAd = nextItem.nativeAd,
                                            scheme = scheme,
                                            modifier = nextModifier,
                                            onCardClick = {}
                                        )
                                    }
                                }
                            }
                        }

                        // 1st active card (top card being swiped - GPU accelerated)
                        key(currentItem.id) {
                            val activeModifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    translationX = animatedOffsetX
                                    rotationZ = rotation
                                }
                                .pointerInput(currentItem.id) {
                                    detectHorizontalDragGestures(
                                        onDragEnd = {
                                            coroutineScope.launch {
                                                if (animatableOffsetX.value > swipeThresholdPx) {
                                                    triggerLike()
                                                } else if (animatableOffsetX.value < -swipeThresholdPx) {
                                                    triggerDislike()
                                                } else {
                                                    animatableOffsetX.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                                                    )
                                                }
                                            }
                                        },
                                        onHorizontalDrag = { change, dragAmount ->
                                            if (!isAnimatingSwipeOut) {
                                                change.consume()
                                                coroutineScope.launch {
                                                    animatableOffsetX.snapTo(animatableOffsetX.value + dragAmount)
                                                }
                                            }
                                        }
                                    )
                                }

                            when (currentItem) {
                                is DiscoveryDeckItem.ProfileCard -> {
                                    HomeFeedCard(
                                        profile = currentItem.profile,
                                        scheme = scheme,
                                        swipeOffsetX = animatedOffsetX,
                                        modifier = activeModifier,
                                        onCardClick = {
                                            onNavigateToProfile(currentItem.profile.id, currentItem.profile.name)
                                        }
                                    )
                                }
                                is DiscoveryDeckItem.AdCard -> {
                                    HomeFeedAdCard(
                                        nativeAd = currentItem.nativeAd,
                                        scheme = scheme,
                                        swipeOffsetX = animatedOffsetX,
                                        modifier = activeModifier,
                                        onCardClick = {
                                            currentItem.nativeAd.performClick(android.os.Bundle())
                                        }
                                    )
                                }
                            }
                        }
                    }

                    val currentProfile = (currentItem as? DiscoveryDeckItem.ProfileCard)?.profile
                    HomeFeedActionButtonsRow(
                        scheme = scheme,
                        isLiked = currentProfile?.id != null && currentProfile.id in uiState.likedModelIds,
                        onDislike = { triggerDislike() },
                        onLike = { triggerLike() },
                        onChat = {
                            if (currentProfile != null) {
                                onNavigateToChat(currentProfile.id, currentProfile.name)
                            }
                        }
                    )
                }
            }
        }

        // Match Modal Dialog
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

        // Quick View Dialog
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
                    viewModel.onLikeById(modelId, context)
                },
                scheme = scheme
            )
        }
    }
}
