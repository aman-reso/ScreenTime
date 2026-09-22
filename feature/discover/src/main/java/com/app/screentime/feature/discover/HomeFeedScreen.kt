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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.components.FeedTabsHeader
import com.app.screentime.feature.discover.components.FollowingFeedCard
import com.app.screentime.feature.discover.components.HomeFeedCard
import com.app.screentime.feature.discover.components.HomeFeedEmptyCard
import com.app.screentime.feature.discover.components.QuickViewProfileDialog
import com.app.screentime.feature.discover.components.StoriesRow
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
 * - stories-row (My Match, Maya, Jordan, Elena, Rohan with glowing rings)
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
    viewModel: DatingDiscoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: For You, 1: Following
    var selectedStoryForQuickView by remember { mutableStateOf<HomeStoryItem?>(null) }

    // Curated stories row matching mockup
    val stories = remember {
        listOf(
            HomeStoryItem(
                id = "my_match",
                name = "My Match",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                isHighlighted = true
            ), HomeStoryItem(
                id = "maya_patel",
                name = "Maya",
                avatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
                isHighlighted = true
            ), HomeStoryItem(
                id = "jordan_lee",
                name = "Jordan",
                avatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=400&q=80",
                isHighlighted = false
            ), HomeStoryItem(
                id = "elena_rostova",
                name = "Elena",
                avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=400&q=80",
                isHighlighted = false
            ), HomeStoryItem(
                id = "rohan_sharma",
                name = "Rohan",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
                isHighlighted = false
            )
        )
    }


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
        ) {
            FeedTabsHeader(
                modifier = Modifier,
                selectedTab = selectedTab,
                onTabSelect = { selectedTab = it },
                scheme = scheme
            )
            if (uiState.isLoading) {
                DiscoverLoadingScreen(
                    modifier = modifier,
                    scheme = scheme
                )
            } else {
                ODSLazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    gap = ODSVariables.spacingComponent3,
                )
                {
                    when {

                        uiState.error != null -> {
                            item {
                                ConnectionLostContent(
                                    errorMessage = uiState.error,
                                    onTryAgain = { viewModel.loadDiscoveryDeck() },
                                    onGoBackHome = { viewModel.resetDeck() },
                                    modifier = Modifier
                                )
                            }
                        }

                        uiState.models.isEmpty() -> {
                            item {
                                StoriesRow(
                                    stories = stories, onStoryClick = { story ->
                                        selectedStoryForQuickView = story
                                    }, scheme = scheme
                                )
                            }
                            item {
                                HomeFeedEmptyCard(
                                    onRefreshFeed = { viewModel.resetDeck() })
                            }
                        }

                        else -> {
                            item {
                                StoriesRow(
                                    stories = stories, onStoryClick = { story ->
                                        selectedStoryForQuickView = story
                                    }, scheme = scheme
                                )
                            }
                            if (selectedTab == 0) {
                                // "For you" feed tab
                                itemsIndexed(
                                    items = uiState.models,
                                    key = { _, profile -> profile.id }
                                ) { index, profile ->
                                    if (index >= uiState.models.size - 2) {
                                        viewModel.loadNextPage()
                                    }
                                    HomeFeedCard(
                                        profile = profile,
                                        scheme = scheme,
                                        onCardClick = {
                                            onNavigateToProfile(profile.id, profile.name)
                                        },
                                        onDislike = {
                                            viewModel.onDislikeById(profile.id)
                                        },
                                        onChat = {
                                            onNavigateToChat(profile.id, profile.name)
                                        },
                                        onLike = {
                                            viewModel.onLikeById(profile.id)
                                        }
                                    )
                                }
                            } else {
                                itemsIndexed(
                                    items = uiState.models,
                                    key = { _, profile -> profile.id }
                                ) { index, profile ->
                                    if (index >= uiState.models.size - 2) {
                                        viewModel.loadNextPage()
                                    }
                                    FollowingFeedCard(
                                        profile = profile,
                                        scheme = scheme,
                                        onCardClick = {
                                            onNavigateToProfile(profile.id, profile.name)
                                        },
                                        onChat = {
                                            onNavigateToChat(profile.id, profile.name)
                                        }
                                    )
                                }
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

                            item {
                                ODSBox(
                                    modifier = Modifier.height(ODSVariables.spacingComponent10 + ODSVariables.spacingComponent8)
                                )
                            }
                        }
                    }
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
                MatchModal(matchedModel = match, onChat = {
                    viewModel.dismissMatchDialog()
                    onNavigateToChat(match.id, match.name)
                }, onKeepBrowsing = {
                    viewModel.dismissMatchDialog()
                })
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
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            gap = 16.dp,
            padding = ODSPadding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until 5) {
                val ringColor = if (i < 2) ZonaColors.ActionPrimary else ZonaColors.Border
                ODSColumn(
                    horizontalAlignment = Alignment.CenterHorizontally, gap = 6.dp
                ) {
                    ODSBox(
                        modifier = Modifier.size(64.dp),
                        cornerRadius = ODSCorners(all = 32.dp),
                        border = ODSBorder(
                            width = 2.dp, colorList = listOf(
                                ODSColorModel(ringColor)
                            )
                        ),
                        padding = ODSPadding(all = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .size(54.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 27.dp),
                            clipContent = true
                        )
                    }

                    // Name shimmer pill placeholder
                    ODSBox(
                        modifier = Modifier
                            .width(42.dp)
                            .height(10.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = 5.dp),
                        clipContent = true
                    )
                }
            }
        }

        // 2. feed-list shimmer (Profile Card)
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
        modifier = Modifier.size(170.dp), background = listOf(
            ODSColorModel(ZonaColors.SurfaceElevated)
        ), cornerRadius = ODSCorners(all = 85.dp), border = ODSBorder(
            width = 1.5.dp, colorList = listOf(ODSColorModel(ZonaColors.Border))
        ), contentAlignment = Alignment.Center
    ) {
        // Center Broken Heart (Coral outline / icon from heart-crack.svg)
        ODSIcon(
            iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_heart_crack),
            tint = ZonaColors.ActionPrimary.getColor(),
            modifier = Modifier.size(76.dp)
        )

        // Overlaid top pill badge "⚡ 503"
        ODSBox(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 26.dp),
            background = listOf(
                ODSColorModel(ZonaColors.ActionPrimary)
            ),
            cornerRadius = ODSCorners(all = 12.dp),
            padding = ODSPadding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            ODSRow(
                gap = 3.dp, verticalAlignment = Alignment.CenterVertically
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_zap),
                    tint = ZonaColors.TextInverse.getColor(),
                    modifier = Modifier.size(13.dp)
                )
                ODSText(
                    text = "503", style = ODSTextStyles.bodySBold, color = ZonaColors.TextInverse
                )
            }
        }
    }
}

/**
 * Connection Lost Screen.
 * Faithfully matches Left side of media_1789901587908.png:
 * - Broken heart illustration with ⚡ 503 badge
 * - "Connection Lost" title
 * - Description & "ERR_CODE: API_RETRY_FAILED_TIMEOUT"
 * - "Try Again" Neon Lime CTA
 * - "Go Back Home" Dark Purple secondary button
 */
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
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        gap = 16.dp
    ) {
        Spacer(modifier = Modifier.weight(0.5f))

        BrokenHeartIllustration()

        Spacer(modifier = Modifier.height(6.dp))

        // Headline
        ODSText(
            text = "Connection Lost",
            style = ODSTextStyles.titleL,
            color = ZonaColors.TextPrimary,
            textAlign = TextAlign.Center
        )

        // Subtitle
        ODSText(
            text = "We couldn't reach the matches feed. Please verify your connection or try again.",
            style = ODSTextStyles.bodyMRegular,
            color = ZonaColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Error code tag
        ODSText(text = errorMessage?.ifBlank { "ERR_CODE: API_RETRY_FAILED_TIMEOUT" }
            ?: "ERR_CODE: API_RETRY_FAILED_TIMEOUT",
            style = ODSTextStyles.bodySBold,
            color = ZonaColors.TextSecondary,
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.weight(1f))

        // Button 1: Try Again (Primary CTA)
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

        // Button 2: Go Back Home (Secondary Button)
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onGoBackHome
                ), background = listOf(
                ODSColorModel(ZonaColors.ActionSoft)
            ), cornerRadius = ODSCorners(all = 26.dp), border = ODSBorder(
                width = 1.dp, colorList = listOf(
                    ODSColorModel(ZonaColors.Border)
                )
            ), contentAlignment = Alignment.Center
        ) {
            ODSText(
                text = "Go Back Home",
                style = ODSTextStyles.bodyLBold,
                color = ZonaColors.TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(84.dp))
    }
}

/**
 * Match Modal popup when both users like each other.
 */
@Composable
private fun MatchModal(
    matchedModel: ModelProfile, onChat: () -> Unit, onKeepBrowsing: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onKeepBrowsing
            ), background = listOf(
            ODSColorModel(ZonaColors.MediaOverlay)
        ), contentAlignment = Alignment.Center
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp), background = listOf(
                ODSColorModel(ZonaColors.Surface)
            ), cornerRadius = ODSCorners(all = 28.dp), border = ODSBorder(
                width = 2.dp, colorList = listOf(
                    ODSColorModel(ZonaColors.ActionPrimary)
                )
            ), padding = ODSPadding(all = 24.dp)
        ) {
            ODSColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 14.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSText(
                    text = "It's a Match! ⚡",
                    style = ODSTextStyles.titleM,
                    color = ZonaColors.ActionPrimary
                )

                ODSText(
                    text = "You and ${matchedModel.name} liked each other!",
                    style = ODSTextStyles.bodyMRegular,
                    color = ZonaColors.TextSecondary
                )

                ODSImage(
                    imageModel = ODSImageModel(
                        url = matchedModel.avatarUrl.ifBlank { matchedModel.coverUrl },
                        contentDescription = matchedModel.name
                    ),
                    modifier = Modifier.size(100.dp),
                    cornerRadius = ODSCorners(all = 50.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(10.dp))

                ODSButton(
                    scheme = zonaODSTheme, props = ODSButtonProps(
                        label = "Send Message",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.LARGE
                    ), onClick = onChat, modifier = Modifier.fillMaxWidth()
                )

                ODSButton(
                    scheme = zonaODSTheme, props = ODSButtonProps(
                        label = "Keep Browsing",
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ), onClick = onKeepBrowsing, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
