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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
import com.app.screentime.feature.discover.components.FollowingFeedCard
import com.app.screentime.feature.discover.components.HomeFeedCard
import com.app.screentime.feature.discover.components.HomeFeedEmptyCard
import com.app.screentime.feature.discover.components.HomeTopBar
import com.app.screentime.feature.discover.components.MatchFeedCard
import com.app.screentime.feature.discover.components.QuickViewProfileDialog
import com.app.screentime.feature.discover.components.StoriesRow
import com.app.screentime.feature.discover.tabs.ForYouTabContent
import android.content.Context
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.organisms.toast.ODSToastMode
import kotlinx.coroutines.delay
import com.telekom.odsystem.DSVariables
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
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
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
    forYouViewModel: ForYouFeedViewModel = hiltViewModel(),
    onNavigateToChat: (modelId: String, modelName: String) -> Unit = { _, _ -> },
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> },
    onNavigateToPreferences: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onRateApp: () -> Unit = {}
) {
    val uiState by forYouViewModel.uiState.collectAsState()
    val defaultTopBarHeight = 42.dp
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(2500L)
        val prefs = context.getSharedPreferences("rating_toast_prefs", Context.MODE_PRIVATE)
        val lastShown = prefs.getLong("last_rating_toast_shown", 0L)
        val now = System.currentTimeMillis()
        if (now - lastShown > 24 * 60 * 60 * 1000L) {
            prefs.edit().putLong("last_rating_toast_shown", now).apply()
            context.showODSToast(
                message = "Enjoying app please rate us",
                title = "Rate Us",
                mode = ODSToastMode.INFORMATIVE,
                scheme = scheme
            )
            onRateApp()
        }
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
        ) {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth(),
                clipContent = true
            ) {
                HomeTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    canUndo = uiState.canUndo,
                    onUndoClick = { forYouViewModel.onUndo() },
                    onNotificationsClick = onNavigateToNotifications,
                    onPreferencesClick = onNavigateToPreferences
                )
            }

            uiState.quotaLimit?.let { quotaInfo ->
                HomeQuotaLimitBanner(
                    quotaLimitInfo = quotaInfo,
                    scheme = scheme
                )
            }

            ForYouTabContent(
                modifier = Modifier.fillMaxSize(),
                scheme = scheme,
                viewModel = forYouViewModel,
                onNavigateToChat = onNavigateToChat,
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToPreferences = onNavigateToPreferences
            )
        }
    }
}


/**
 * Shimmer Loading State Component.
 * Implemented using 100% Telekom ODSSkeleton components:
 * - Shows 2 skeleton profile cards
 * - Photo placeholder, match badge, profile header, location, bio, and action pills
 */
@Composable
fun HomeFeedShimmerContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth(),
        gap = 16.dp,
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        )
    ) {
        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(DSVariables.spacingComponent4)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.FULL)
        )

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(DSVariables.spacingComponent4)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.MEDIUM)
        )
        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(DSVariables.spacingComponent4)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
        )

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(DSVariables.spacingComponent4)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.FULL)
        )

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(DSVariables.spacingComponent4)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.MEDIUM)
        )
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

@Composable
fun HomeQuotaLimitBanner(
    quotaLimitInfo: com.app.screentime.core.model.QuotaLimitInfo,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    if (quotaLimitInfo.isLimitReached) {
        ODSBox(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DSVariables.spacingComponent4,
                    vertical = DSVariables.spacingComponent2
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            padding = ODSPadding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent3
            )
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                gap = DSVariables.spacingComponent3
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = com.telekom.odsystem.R.drawable.ic_alert_triangle,
                        contentDescription = "Daily Limit"
                    ),
                    tint = scheme.functionalDestructiveStandard.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = ODSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = "Daily Profile Limit Reached",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.functionalDestructiveStandard
                    )
                    ODSText(
                        text = quotaLimitInfo.limitMessage
                            ?: "You've reached your daily profile limit. Come back tomorrow for new matches!",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}


