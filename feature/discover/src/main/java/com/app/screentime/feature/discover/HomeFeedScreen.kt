package com.app.screentime.feature.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import androidx.compose.runtime.LaunchedEffect
import com.app.screentime.feature.discover.components.FeedTabsHeader
import com.app.screentime.feature.discover.components.FollowingFeedCard
import com.app.screentime.feature.discover.components.HomeFeedCard
import com.app.screentime.feature.discover.components.HomeFeedEmptyCard
import com.app.screentime.feature.discover.components.HomeTopBar
import com.app.screentime.feature.discover.components.MatchFeedCard
import com.app.screentime.feature.discover.components.QuickViewProfileDialog
import com.app.screentime.feature.discover.components.StoriesRow
import com.app.screentime.feature.discover.tabs.ForYouTabContent
import com.app.screentime.feature.discover.tabs.FollowingTabContent
import com.app.screentime.feature.discover.tabs.MatchesTabContent
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
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

/**
 * Story item definition matching the stories-row in the mockup.
 */
data class HomeStoryItem(
    val id: String, val name: String, val avatarUrl: String, val isHighlighted: Boolean = false
)

/**
 * Exact Shimmer Brush matching media_1789901625814.png:
 * Base: #25115C
 * Linear Gradient: #311873 (0%) -> #FFFFFF at 3.92% (alpha = 0.0392f) -> #311873 (0%)
 */
fun Modifier.zonaShimmer(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "zonaShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -800f, targetValue = 800f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer_anim"
    )

    val baseColor = ZonaColors.LoadingShimmer.getColor()
    val highlightColor = ZonaColors.loadingShimmerHighlight.getColor()
    val shimmerColors = listOf(
        baseColor,
        highlightColor,
        baseColor
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 350f, translateAnim - 350f),
            end = Offset(translateAnim + 350f, translateAnim + 350f)
        )
    )
}

/**
 * Dating Home Feed Screen.
 * Exact visual recreation of media_1789901029586.png, media_1789901085805.png,
 * and loading / error states in media_1789901587908.png:
 * - status-bar
 * - feed-tabs ("For You" active with Neon Lime indicator, "Following" inactive)
 * - stories-row (Dynamic stories from live candidates)
 * - feed-list (Vertical feed of profile cards with photo, match badge, profile header, location, bio, and actions-row)
 * - Shimmer loading state (with exact #25115C, #311873, and 3.92% white linear gradient)
 * - Connection Lost state (with 💔 illustration, ⚡ 503 badge, Try Again, and Go Back Home buttons)
 * 100% strictly built with Telekom ODS components.
 */
@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onNavigateToChat: (modelId: String, modelName: String) -> Unit = { _, _ -> },
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> },
    onNavigateToPreferences: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {}
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val defaultTopBarHeight = 64.dp
    var topBarHeightPx by remember { mutableFloatStateOf(with(density) { defaultTopBarHeight.toPx() }) }
    var topBarOffsetHeightPx by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember(topBarHeightPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                // When scrolling UP (delta < 0), collapse the Winter top bar first
                if (delta < 0f && topBarOffsetHeightPx > -topBarHeightPx) {
                    val newOffset = (topBarOffsetHeightPx + delta).coerceIn(-topBarHeightPx, 0f)
                    val consumed = newOffset - topBarOffsetHeightPx
                    topBarOffsetHeightPx = newOffset
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // When scrolling DOWN (available.y > 0) and the child feed is at the top,
                // smoothly expand the Winter top bar back into view
                if (available.y > 0f && topBarOffsetHeightPx < 0f) {
                    val newOffset =
                        (topBarOffsetHeightPx + available.y).coerceIn(-topBarHeightPx, 0f)
                    val consumedY = newOffset - topBarOffsetHeightPx
                    topBarOffsetHeightPx = newOffset
                    return Offset(0f, consumedY)
                }
                return Offset.Zero
            }
        }
    }

    val currentTopBarHeight = with(density) {
        (topBarHeightPx + topBarOffsetHeightPx).coerceIn(0f, topBarHeightPx).toDp()
    }
    val topBarProgress = if (topBarHeightPx > 0f) {
        ((topBarHeightPx + topBarOffsetHeightPx) / topBarHeightPx).coerceIn(0f, 1f)
    } else 1f

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(
            ODSColorModel(hexColor = scheme.basicBackground)
        )
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .nestedScroll(nestedScrollConnection)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(currentTopBarHeight)
                    .clipToBounds()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(x = 0, y = topBarOffsetHeightPx.roundToInt()) }
                        .alpha(topBarProgress)
                        .onGloballyPositioned { coordinates ->
                            val measured = coordinates.size.height.toFloat()
                            if (measured > 0f && (topBarHeightPx == 0f || (topBarOffsetHeightPx == 0f && topBarHeightPx != measured))) {
                                topBarHeightPx = measured
                            }
                        }
                ) {
                    HomeTopBar(
                        modifier = Modifier,
                        scheme = scheme,
                        onNotificationsClick = onNavigateToNotifications,
                        onPreferencesClick = onNavigateToPreferences
                    )
                }
            }

            // Sticky Tabs Header - stays pinned at the top when scrolling cards
            FeedTabsHeader(
                modifier = Modifier,
                selectedTab = pagerState.currentPage,
                onTabSelect = { index ->
                    if (index == pagerState.currentPage && topBarOffsetHeightPx < 0f) {
                        coroutineScope.launch {
                            androidx.compose.animation.core.animate(
                                initialValue = topBarOffsetHeightPx,
                                targetValue = 0f,
                                animationSpec = androidx.compose.animation.core.tween(durationMillis = 250)
                            ) { value, _ ->
                                topBarOffsetHeightPx = value
                            }
                        }
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                },
                scheme = scheme
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        ForYouTabContent(
                            scheme = scheme,
                            onNavigateToChat = onNavigateToChat,
                            onNavigateToProfile = onNavigateToProfile
                        )
                    }

                    1 -> {
                        FollowingTabContent(
                            scheme = scheme,
                            onNavigateToChat = onNavigateToChat,
                            onNavigateToProfile = onNavigateToProfile,
                            onExploreClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(0) }
                            }
                        )
                    }

                    2 -> {
                        MatchesTabContent(
                            scheme = scheme,
                            onNavigateToChat = onNavigateToChat,
                            onNavigateToProfile = onNavigateToProfile,
                            onExploreClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(0) }
                            }
                        )
                    }
                }
            }
        }
    }
}


/**
 * Shimmer Loading State Component.
 * Faithfully matches Right side of media_1789901587908.png with colors from media_1789901625814.png:
 * - 5 story avatar circles with rings and name placeholders
 * - 1 card shimmer (photo area + match badge + header + bio + 3 action buttons)
 */
@Composable
fun HomeFeedShimmerContent(modifier: Modifier = Modifier) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth(),
        gap = 16.dp,
        padding = ODSPadding(bottom = 96.dp)
    ) {

        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), background = listOf(
                ODSColorModel(ZonaColors.SurfaceElevated)
            ), cornerRadius = ODSCorners(all = 26.dp), border = ODSBorder(
                width = 1.dp, colorList = listOf(
                    ODSColorModel(ZonaColors.Border)
                )
            ), clipContent = true
        ) {
            ODSColumn(modifier = Modifier.fillMaxWidth()) {
                // Photo Area Shimmer
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .zonaShimmer()
                ) {
                    // Match badge placeholder top-left
                    ODSBox(
                        modifier = Modifier
                            .padding(start = 14.dp, top = 14.dp)
                            .width(78.dp)
                            .height(26.dp), background = listOf(
                            ODSColorModel(ZonaColors.SurfaceElevated)
                        ), cornerRadius = ODSCorners(all = 13.dp), border = ODSBorder(
                            width = 1.dp, colorList = listOf(
                                ODSColorModel(ZonaColors.ActionPrimary)
                            )
                        )
                    )
                }

                // Card Content Shimmer
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    padding = ODSPadding(horizontal = 18.dp, vertical = 16.dp),
                    gap = 12.dp
                ) {
                    // Profile Header (Name bar + verified circle)
                    ODSRow(
                        gap = 8.dp, verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .width(160.dp)
                                .height(20.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 10.dp),
                            clipContent = true
                        )
                        ODSBox(
                            modifier = Modifier
                                .size(18.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 9.dp),
                            clipContent = true
                        )
                    }

                    // Location Tag Bar
                    ODSBox(
                        modifier = Modifier
                            .width(110.dp)
                            .height(14.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = 7.dp),
                        clipContent = true
                    )

                    // Bio multi-line bars
                    ODSColumn(gap = 6.dp) {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 6.dp),
                            clipContent = true
                        )
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .height(12.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 6.dp),
                            clipContent = true
                        )
                    }

                    // Actions Row Shimmer
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .size(54.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 27.dp),
                            clipContent = true
                        )
                        ODSBox(
                            modifier = Modifier
                                .size(64.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 32.dp),
                            clipContent = true
                        )
                        ODSBox(
                            modifier = Modifier
                                .size(54.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 27.dp),
                            clipContent = true
                        )
                    }
                }
            }
        }
    }
}

/**
 * Broken Heart Illustration for Connection Lost Screen.
 * Large circular badge with broken heart icon (💔) and ⚡ 503 pill.
 */
@Composable
private fun BrokenHeartIllustration() {
    ODSBox(
        modifier = Modifier.size(80.dp), background = listOf(
            ODSColorModel(ZonaColors.SurfaceElevated)
        ), cornerRadius = ODSCorners(all = 85.dp), border = ODSBorder(
            width = 1.5.dp, colorList = listOf(ODSColorModel(ZonaColors.Border))
        ), contentAlignment = Alignment.Center
    ) {
        // Center Broken Heart (Coral outline / icon from heart-crack.svg)
        ODSIcon(
            iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_heart_crack),
            tint = ZonaColors.ActionPrimary.getColor(),
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun ConnectionLostContent(
    errorMessage: String? = null,
    onTryAgain: () -> Unit = {},
    onGoBackHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        gap = 16.dp
    ) {

        BrokenHeartIllustration()

        Spacer(modifier = Modifier.height(6.dp))

        // Headline
        ODSText(
            text = "Something went wrong",
            style = ODSTextStyles.titleS,
            color = ZonaColors.TextPrimary,
            textAlign = TextAlign.Center
        )

        // Subtitle
        ODSText(
            text = "We couldn't reach the matches feed. Please verify your connection or try again.",
            style = ODSTextStyles.microcopyRegular,
            color = ZonaColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onTryAgain
                ), background = listOf(
                ODSColorModel(ZonaColors.ActionPrimary)
            ), cornerRadius = ODSCorners(all = 26.dp), contentAlignment = Alignment.Center
        ) {
            ODSText(
                text = "Try Again", style = ODSTextStyles.bodyLBold, color = ZonaColors.TextInverse
            )
        }
    }
}

