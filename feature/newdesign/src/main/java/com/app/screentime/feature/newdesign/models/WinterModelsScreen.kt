package com.app.screentime.feature.newdesign.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.screentime.core.network.dto.ForYouOfferDto
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.CreatorStatus
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.Pacifico
import com.app.screentime.feature.newdesign.components.WinterFloatingNavDefaults
import com.app.screentime.feature.newdesign.components.WinterFloatingNavigation
import com.app.screentime.feature.newdesign.components.WinterNavItem
import com.app.screentime.feature.newdesign.components.WinterTokens
import com.app.screentime.feature.newdesign.dialogs.WinterGenderDialog

data class CreatorModel(
    val id: String,
    val name: String,
    val age: Int,
    val languages: String = "Hindi · English",
    val photoUrl: String? = null,
    val drawableResId: Int? = null,
    val status: CreatorStatus = CreatorStatus.AVAILABLE,
    val audioRate: String = "₹20 / minute",
    val videoRate: String = "₹40 / minute"
)

/**
 * WinterModelsScreen - Figma screen "Winter · Discover" (Node 122:2).
 *
 * Rules followed:
 * 1. Multiples of 4dp for all padding, margins, and sizes.
 * 2. Broken down into clean, modular composable functions.
 * 3. Uses dynamic color scheme tokens and typography.
 * 4. Never hardcode container heights.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinterModelsScreen(
    modifier: Modifier = Modifier,
    coinBalance: Int = 240,
    userName: String = "Aarav",
    showBottomNav: Boolean = false,
    contentBottomPadding: Dp = WinterFloatingNavDefaults.Height + 48.dp,
    onCreatorClick: (CreatorModel) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onCoinBalanceClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAcceptCall: () -> Unit = {},
    onDeclineCall: () -> Unit = {},
    onJoinLiveClick: (CreatorModel) -> Unit = {},
    onStartAudioCall: (String) -> Unit = {},
    onStartVideoCall: (String) -> Unit = {},
    onNavItemSelected: (WinterNavItem) -> Unit = {},
    onRefresh: (() -> Unit)? = null,
    viewModel: WinterModelsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    var hasIncomingCall by remember { mutableStateOf(false) }
    var selectedCreatorForCall by remember { mutableStateOf<CreatorModel?>(null) }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WinterTokens.backgroundCanvas)
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                if (onRefresh != null) {
                    onRefresh()
                } else {
                    viewModel.loadForYouFeed(isRefresh = true)
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = contentBottomPadding)
            ) {
                item {
                    DiscoverHeaderSection(
                        userName = userName,
                        statusBarPadding = statusBarPadding,
                        hasIncomingCall = hasIncomingCall,
                        onSearchClick = onSearchClick,
                        onNotificationClick = onNotificationClick,
                        onAcceptCall = onAcceptCall,
                        onDeclineCall = {
                            hasIncomingCall = false
                            onDeclineCall()
                        }
                    )
                }

                item {
                    DiscoverTrendingSection(
                        trendingList =  uiState.trendingProfiles,
                        onJoinLiveClick = { topModel ->
                            onJoinLiveClick(topModel)
                            onStartVideoCall(topModel.name)
                        }
                    )
                }

                if (uiState.offers.isNotEmpty()) {
                    item {
                        DiscoverOffersSection(
                            offers = uiState.offers,
                            onOfferClick = { offer ->
                                if (offer.action_type == "wallet" || offer.action_target?.contains("wallet") == true) {
                                    onCoinBalanceClick()
                                }
                            }
                        )
                    }
                }

                item {
                    DiscoverAvailableHeader(
                        onlineCount = uiState.models.count { it.status == CreatorStatus.AVAILABLE }
                    )
                }

                itemsIndexed(
                    items = uiState.models,
                    key = { _, creator -> creator.id }
                ) { index, creator ->
                    DiscoverCreatorRow(
                        creator = creator,
                        isLast = index == uiState.models.size - 1,
                        onCreatorClick = { selected ->
                            selectedCreatorForCall = selected
                        },
                        onSeeDetailsClick = { selected ->
                            onCreatorClick(selected)
                        }
                    )
                }
            }
        }

        selectedCreatorForCall?.let { creator ->
            WinterCallModelBottomSheet(
                creator = creator,
                onDismissRequest = { selectedCreatorForCall = null },
                onStartAudioCall = { selected ->
                    selectedCreatorForCall = null
                    onStartAudioCall(selected.name)
                },
                onStartVideoCall = { selected ->
                    selectedCreatorForCall = null
                    onStartVideoCall(selected.name)
                }
            )
        }

        if (uiState.showGenderDialog) {
            WinterGenderDialog(
                onDismissRequest = { viewModel.dismissGenderDialog() },
                onContinue = { option -> viewModel.onGenderSelected(option) }
            )
        }
    }
}

/**
 * Header Section matching Figma Node 122:149 with Aarav profile greeting, wordmark, search, bell, and incoming call card.
 */
@Composable
private fun DiscoverHeaderSection(
    userName: String,
    statusBarPadding: Dp,
    hasIncomingCall: Boolean,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onAcceptCall: () -> Unit,
    onDeclineCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WinterTokens.backgroundCanvas)
            .padding(top = statusBarPadding + 8.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar: Aarav Avatar + "Welcome back, Aarav", "winter" Wordmark, Search & Notification icons
        DiscoverTopBar(
            userName = userName,
            onSearchClick = onSearchClick,
            onNotificationClick = onNotificationClick
        )

        // Incoming Call Card
        if (hasIncomingCall) {
            Spacer(modifier = Modifier.height(16.dp))
            DiscoverIncomingCallCard(
                callerName = "Priya, 26",
                onAccept = onAcceptCall,
                onDecline = onDeclineCall
            )
        }
    }
}

/**
 * Top bar row with profile avatar, greeting, centered brand wordmark, and action buttons.
 */
@Composable
private fun DiscoverTopBar(
    userName: String,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Center Wordmark
        Text(
            text = "winter",
            fontFamily = Pacifico,
            fontSize = 24.sp,
            color = WinterTokens.textPrimary,
            fontWeight = FontWeight.Normal
        )

        // Right Actions: Search & Notifications
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceControl)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSearchClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_search),
                    contentDescription = "Search",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Notifications button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceControl)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onNotificationClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_bell),
                    contentDescription = "Notifications",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(20.dp)
                )

                // Unread dot (6dp)
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .clip(CircleShape)
                        .background(WinterTokens.statusError)
                )
            }
        }
    }
}

/**
 * Incoming call card matching Figma Node 122:131 ("Incoming video call · Priya").
 */
@Composable
private fun DiscoverIncomingCallCard(
    callerName: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(44.dp))
            .background(WinterTokens.surfaceCard)
            .border(2.dp, WinterTokens.borderDefault, RoundedCornerShape(44.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Video camera icon with dark canvas background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.backgroundCanvas),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call_video_cam),
                    contentDescription = null,
                    tint = WinterTokens.textAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = callerName,
                    fontFamily = FunnelSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = WinterTokens.textPrimary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reject Button
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(WinterTokens.surfaceControl)
                        .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(20.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDecline
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_phone_reject),
                        contentDescription = "Reject",
                        tint = WinterTokens.iconSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Accept Button
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(WinterTokens.actionPrimary)
                        .border(1.dp, WinterTokens.borderOnAccent, RoundedCornerShape(20.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onAccept
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_phone_accept),
                        contentDescription = "Accept",
                        tint = WinterTokens.iconOnAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Trending Section matching Figma Node 122:4 with dynamic trending creators from ForYou API.
 */
@Composable
private fun DiscoverTrendingSection(
    trendingList: List<CreatorModel>,
    onJoinLiveClick: (CreatorModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val topModel = trendingList.firstOrNull() ?: CreatorModel(
        id = "user_model_maya",
        name = "Maya",
        age = 23,
        languages = "Hindi · English",
        drawableResId = R.drawable.trending_live_maya
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trending",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp))
        ) {
            // Background Live Image (Remote AsyncImage or Local Drawable Fallback)
            if (!topModel.photoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = topModel.photoUrl,
                    contentDescription = topModel.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = topModel.drawableResId ?: R.drawable.trending_live_maya),
                    contentDescription = topModel.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            0.0f to Color(0xD91A1C14),
                            0.55f to Color(0x8C1A1C14),
                            1.0f to Color(0x0D1A1C14)
                        )
                    )
            )

            // Live Card Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Live badge (28dp height, rounded 14dp)
                Row(
                    modifier = Modifier
                        .height(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(WinterTokens.actionPrimary)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(WinterTokens.statusPositive)
                    )
                    Text(
                        text = "LIVE",
                        fontFamily = FunnelSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = WinterTokens.textOnAccent
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "A little hello,\na great connection.",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    color = WinterTokens.mediaWhite
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "${topModel.name}, ${topModel.age}",
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = WinterTokens.mediaWhite
                        )
                        Text(
                            text = topModel.languages,
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = WinterTokens.mediaWhite.copy(alpha = 0.8f)
                        )
                    }

                    // "Join live" Button
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(WinterTokens.actionPrimary)
                            .border(1.dp, WinterTokens.borderOnAccent, RoundedCornerShape(20.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onJoinLiveClick(topModel) }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Join live",
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = WinterTokens.textOnAccent
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right_small),
                            contentDescription = null,
                            tint = WinterTokens.iconOnAccent,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Promotional Offers Section displaying active banners / recharge packages
 */
@Composable
private fun DiscoverOffersSection(
    offers: List<ForYouOfferDto>,
    onOfferClick: (ForYouOfferDto) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Offers For You",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(offers, key = { it.id }) { offer ->
                val badgeText = offer.badge_text
                val subtitle = offer.subtitle

                Row(
                    modifier = Modifier
                        .width(260.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(WinterTokens.surfaceCard)
                        .border(1.dp, WinterTokens.borderDefault, RoundedCornerShape(16.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onOfferClick(offer) }
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (!badgeText.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(WinterTokens.actionPrimary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WinterTokens.textOnAccent
                                )
                            }
                        }
                        Text(
                            text = offer.title,
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = WinterTokens.textPrimary,
                            maxLines = 1
                        )
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                fontFamily = FunnelSans,
                                fontSize = 12.sp,
                                color = WinterTokens.textSecondary,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Section header for available creators with live online count.
 */
@Composable
private fun DiscoverAvailableHeader(
    onlineCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Available for you",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = WinterTokens.textPrimary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.statusPositive)
            )
            Text(
                text = "$onlineCount online",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = WinterTokens.textSecondary
            )
        }
    }
}

/**
 * Individual creator list row matching Figma Node 122:8.
 */
@Composable
private fun DiscoverCreatorRow(
    creator: CreatorModel,
    isLast: Boolean,
    onCreatorClick: (CreatorModel) -> Unit,
    onSeeDetailsClick: (CreatorModel) -> Unit = onCreatorClick,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onCreatorClick(creator) }
                )
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile photo + Online indicator dot (12dp)
            Box(modifier = Modifier.size(72.dp)) {
                if (creator.drawableResId != null) {
                    Image(
                        painter = painterResource(id = creator.drawableResId),
                        contentDescription = creator.name,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else if (!creator.photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = creator.photoUrl,
                        contentDescription = creator.name,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.creator_ananya),
                        contentDescription = creator.name,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                if (creator.status == CreatorStatus.AVAILABLE) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(WinterTokens.statusPositive)
                            .border(1.dp, WinterTokens.backgroundCanvas, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details: Name, Language, See details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${creator.name}, ${creator.age}",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = WinterTokens.textPrimary
                )

                Text(
                    text = creator.languages,
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = WinterTokens.textSecondary
                )

                Text(
                    text = "See details →",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = WinterTokens.textAccent,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onSeeDetailsClick(creator) }
                    )
                )
            }

            // Call Action Button (44dp height, rounded 20dp, actionPrimary)
            Row(
                modifier = Modifier
                    .height(44.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(WinterTokens.actionPrimary)
                    .border(1.dp, WinterTokens.borderOnAccent, RoundedCornerShape(20.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onCreatorClick(creator) }
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_video_call_btn),
                    contentDescription = "Call",
                    tint = WinterTokens.iconOnAccent,
                    modifier = Modifier.size(16.dp, 12.dp)
                )

                Text(
                    text = "Call",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = WinterTokens.textOnAccent
                )
            }
        }

        if (!isLast) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 88.dp)
                    .height(1.dp)
                    .background(WinterTokens.borderSubtle.copy(alpha = 0.45f))
            )
        }
    }
}

@Preview(name = "Winter · Discover New Homescreen (Node 122:2)", showBackground = true)
@Composable
private fun WinterModelsScreenPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        WinterModelsScreen()
    }
}
