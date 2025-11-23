package com.app.screentime.challenge.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Outbound
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import com.app.screentime.ui.atom.SegmentedControl
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.shadow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import kotlin.math.min
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.navigation.Screen
import com.app.screentime.network.model.Challenge
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ThemeType
import com.app.screentime.ui.theme.Typography
import com.app.screentime.ui.theme.getThemeColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ChallengeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: ChallengeViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf("Challenges", "Joined")
    val pagerState = rememberPagerState(pageCount = { tabs.size }, initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    val upcomingChallenges = remember { sampleUpcomingChallenges }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Segmented Control
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SegmentedControl(
                items = tabs,
                selectedIndex = pagerState.currentPage,
                onItemSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }

        // Tab Content with Pager
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    // All Challenges Tab
                    ChallengesTab(
                        uiState = uiState,
                        navController = navController,
                        viewModel = viewModel,
                        upcomingChallenges = upcomingChallenges
                    )
                }

                1 -> {
                    // Joined Challenges Tab
                    JoinedChallengesTab(
                        uiState = uiState, navController = navController, viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengesTab(
    uiState: com.app.screentime.challenge.viewmodel.ChallengesUiState,
    navController: NavController?,
    viewModel: ChallengeViewModel,
    upcomingChallenges: List<UpcomingChallenge>
) {
    val colors = LocalAppColors.current ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionTitle("Current challenges", colors.tint)
        }

        when {
            uiState.isLoading -> {
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                    AppLoader(color = colors.success)
                }
            }

            uiState.error != null -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppText(
                            text = uiState.error, style = AppTextStyle.Body, color = colors.error
                        )
                        TextButton(onClick = viewModel::refresh) {
                            AppText(
                                text = "Retry", style = AppTextStyle.Label, color = colors.success
                            )
                        }
                    }
                }
            }

            uiState.challenges.isEmpty() -> {
                item {
                    AppText(
                        text = "No current challenges available right now.",
                        style = AppTextStyle.Label,
                        color = colors.textMuted
                    )
                }
            }

            else -> {
                itemsIndexed(uiState.challenges, key = { _, challenge -> challenge.id }) { index, challenge ->
                    CurrentChallengeCard(
                        challenge = challenge,
                        index = index,
                        isJoining = uiState.joiningChallengeIds.contains(challenge.id),
                        onViewDetails = {
                            navController?.navigate(
                                Screen.ChallengeDetail.createRoute(challenge.id.toString())
                            )
                        },
                        onJoin = {
                            // Navigate to detail screen when join button is clicked
                            navController?.navigate(
                                Screen.ChallengeDetail.createRoute(challenge.id.toString())
                            )
                        })
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionTitle("Upcoming challenges", colors.textSecondary)
        }

        items(upcomingChallenges, key = { it.id }) { challenge ->
            UpcomingChallengeCard(challenge = challenge)
        }
    }
}

@Composable
private fun JoinedChallengesTab(
    uiState: com.app.screentime.challenge.viewmodel.ChallengesUiState,
    navController: NavController?,
    viewModel: ChallengeViewModel
) {
    val colors = LocalAppColors.current ?: return
    val joinedChallenges = uiState.challenges.filter { it.hasJoined }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                    AppLoader(color = colors.success)
                }
            }

            uiState.error != null -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppText(
                            text = uiState.error ?: "Unable to load challenges.",
                            style = AppTextStyle.Body,
                            color = colors.error
                        )
                        TextButton(onClick = viewModel::refresh) {
                            AppText(
                                text = "Retry", style = AppTextStyle.Label, color = colors.success
                            )
                        }
                    }
                }
            }

            joinedChallenges.isEmpty() -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = colors.textMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        AppText(
                            text = "No joined challenges",
                            style = AppTextStyle.SubTitle,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        AppText(
                            text = "Join challenges from the Challenges tab to see them here.",
                            style = AppTextStyle.Body,
                            color = colors.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                itemsIndexed(joinedChallenges, key = { _, challenge -> challenge.id }) { index, challenge ->
                    CurrentChallengeCard(
                        challenge = challenge,
                        index = index,
                        isJoining = uiState.joiningChallengeIds.contains(challenge.id),
                        onViewDetails = {
                            navController?.navigate(
                                Screen.ChallengeDetail.createRoute(challenge.id.toString())
                            )
                        },
                        onJoin = {
                            viewModel.joinChallenge(challenge.id)
                        })
                }
            }
        }
    }
}

@Composable
private fun Header(
    onRefresh: () -> Unit, useMockData: Boolean, onToggleMockData: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = colors.success.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = colors.success,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    AppText(
                        text = "Challenges",
                        style = AppTextStyle.Title,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    AppText(
                        text = "Join a challenge to improve your ranking",
                        style = AppTextStyle.Label,
                        color = colors.textSecondary
                    )
                }
            }
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = colors.success
                )
            }
        }

        // Mock Data Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = "Use Mock Data", style = AppTextStyle.Label, color = colors.textSecondary
            )
            Switch(
                checked = useMockData, onCheckedChange = { onToggleMockData() })
        }
    }
}

@Composable
private fun SectionTitle(title: String, tint: Color) {
    AppText(
        text = title, style = AppTextStyle.SubTitle, fontWeight = FontWeight.Bold, color = tint
    )
}

@Composable
private fun ChallengeImage(
    imageUrl: String, appName: String, modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.border.copy(alpha = 0.1f)), contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Challenge image for $appName",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun CurrentChallengeCard(
    challenge: Challenge, 
    index: Int = 0,
    isJoining: Boolean = false, 
    onViewDetails: () -> Unit, 
    onJoin: () -> Unit
) {
    // Alternate between two designs based on index to ensure both are visible
    val useOverlayDesign = remember(index) { index % 2 == 0 }
    
    if (useOverlayDesign) {
        CurrentChallengeCardOverlay(
            challenge = challenge,
            isJoining = isJoining,
            onViewDetails = onViewDetails,
            onJoin = onJoin
        )
    } else {
        CurrentChallengeCardGradient(
            challenge = challenge,
            isJoining = isJoining,
            onViewDetails = onViewDetails,
            onJoin = onJoin
        )
    }
}

@Composable
private fun CurrentChallengeCardOverlay(
    challenge: Challenge, isJoining: Boolean = false, onViewDetails: () -> Unit, onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val hasJoined = challenge.hasJoined

    // Format dates
    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)

    // Interactive states
    val cardInteractionSource = remember { MutableInteractionSource() }
    val isCardPressed by cardInteractionSource.collectIsPressedAsState()
    
    // Animated scale for card hover/press
    val cardScale by animateFloatAsState(
        targetValue = if (isCardPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "cardScale"
    )
    
    // Animated elevation for card press
    val cardElevation by animateFloatAsState(
        targetValue = if (isCardPressed) 8f else 4f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "cardElevation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .scale(cardScale)
            .shadow(
                elevation = cardElevation.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = cardInteractionSource,
                onClick = onViewDetails
            )
    ) {
        // Background Image
        Box(modifier = Modifier.fillMaxSize()) {
            if (challenge.thumbnail != null) {
                AsyncImage(
                    model = challenge.thumbnail,
                    contentDescription = challenge.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.border.copy(alpha = 0.1f))
                )
            }
            
            // Dark Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.9f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section - Badge
            Box {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    AppText(
                        text = if (hasJoined) "Joined" else "Active",
                        style = AppTextStyle.Caption,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
            
            // Bottom Section - Title, Reward, Dates, Button
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                // Reward
                if (challenge.reward.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = Color(0xFFFFD700), // Amber/Gold
                            modifier = Modifier.size(16.dp)
                        )
                        AppText(
                            text = challenge.reward,
                            style = AppTextStyle.Label,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
                
                // Dates
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    AppText(
                        text = "$startDate - $endDate",
                        style = AppTextStyle.Label,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                
                // Participants Stack (only show if joined)
                if (hasJoined) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ParticipantAvatarStack(participantCount = 5) // Using placeholder count for now
                        AppText(
                            text = "5+ participants",
                            style = AppTextStyle.Label,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
                
                // Button
                if (!hasJoined) {
                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                    
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isButtonPressed) 0.98f else 1f,
                        animationSpec = tween(durationMillis = 150),
                        label = "buttonScale"
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .scale(buttonScale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .clickable(
                                interactionSource = buttonInteractionSource,
                                enabled = !isJoining,
                                onClick = onJoin
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AppText(
                            text = "Join Now",
                            style = AppTextStyle.Label,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                } else {
                    // Joined state - show view details button
                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                    
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isButtonPressed) 0.98f else 1f,
                        animationSpec = tween(durationMillis = 150),
                        label = "buttonScale"
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .scale(buttonScale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.success)
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .clickable(
                                interactionSource = buttonInteractionSource,
                                onClick = onViewDetails
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            AppText(
                                text = "View Details",
                                style = AppTextStyle.Label,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentChallengeCardGradient(
    challenge: Challenge, isJoining: Boolean = false, onViewDetails: () -> Unit, onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val hasJoined = challenge.hasJoined

    // Format dates
    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)

    // Interactive states
    val cardInteractionSource = remember { MutableInteractionSource() }
    val isCardPressed by cardInteractionSource.collectIsPressedAsState()
    
    // Animated scale for card hover/press
    val cardScale by animateFloatAsState(
        targetValue = if (isCardPressed) 1.02f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "cardScale"
    )
    
    // Animated elevation for card press
    val cardElevation by animateFloatAsState(
        targetValue = if (isCardPressed) 12f else 8f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "cardElevation"
    )

    // Gradient colors
    val cardGradient = Brush.verticalGradient(
        colors = listOf(
            colors.card,
            colors.card.copy(alpha = 0.95f)
        )
    )
    
    val rewardGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFD700), // Yellow
            Color(0xFFFF6B35)  // Orange
        )
    )
    
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(
            colors.tint,
            colors.accent
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .shadow(
                elevation = cardElevation.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = colors.tint.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(cardGradient)
            .border(1.dp, colors.border.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = cardInteractionSource,
                onClick = onViewDetails
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Thumbnail Section with Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(192.dp) // h-48 equivalent
            ) {
                // Image
                val imageScale by animateFloatAsState(
                    targetValue = if (isCardPressed) 1.1f else 1f,
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                    label = "imageScale"
                )
                
                if (challenge.thumbnail != null) {
                    AsyncImage(
                        model = challenge.thumbnail,
                        contentDescription = challenge.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(imageScale),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors.border.copy(alpha = 0.1f))
                    )
                }
                
                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Black.copy(alpha = 0.6f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
                
                // Joined Badge (top-left) - Show when joined
                if (hasJoined) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            colors.success,
                                            colors.success.copy(alpha = 0.8f)
                                        )
                                    )
                                )
                                .shadow(4.dp, RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                AppText(
                                    text = "Joined",
                                    style = AppTextStyle.Caption,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // Difficulty Badge (top-left) - Using Medium as default
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFFF3CD).copy(alpha = 0.95f))
                                .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            AppText(
                                text = "Medium",
                                style = AppTextStyle.Caption,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }
                
                // Participants Stack (top-right) - Show when joined
                if (hasJoined) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ParticipantAvatarStack(participantCount = 5) // Using placeholder count for now
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.9f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                AppText(
                                    text = "5+",
                                    style = AppTextStyle.Caption,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.textPrimary
                                )
                            }
                        }
                    }
                }
                
                // Reward Badge (bottom-left) - Floating
                if (challenge.reward.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(rewardGradient)
                                .shadow(4.dp, RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                AppText(
                                    text = challenge.reward,
                                    style = AppTextStyle.Label,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            
            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Title
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Description
                AppText(
                    text = challenge.description,
                    style = AppTextStyle.Body,
                    color = colors.textSecondary,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                
                // Date & Duration Info
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    // Challenge Period
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.tint.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = colors.tint,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Column {
                            AppText(
                                text = "Challenge Period",
                                style = AppTextStyle.Caption,
                                color = colors.textMuted
                            )
                            AppText(
                                text = "$startDate - $endDate",
                                style = AppTextStyle.Label,
                                color = colors.textPrimary
                            )
                        }
                    }
                    
                    // Participants Stack (only show if joined)
                    if (hasJoined) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colors.success.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    tint = colors.success,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ParticipantAvatarStack(participantCount = 5) // Using placeholder count for now
                                Column {
                                    AppText(
                                        text = "Participants",
                                        style = AppTextStyle.Caption,
                                        color = colors.textMuted
                                    )
                                    AppText(
                                        text = "5+ joined",
                                        style = AppTextStyle.Label,
                                        color = colors.textPrimary
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Join Button or Joined Status
                if (hasJoined) {
                    // Show "View Details" button when joined
                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                    
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isButtonPressed) 0.98f else 1f,
                        animationSpec = tween(durationMillis = 150),
                        label = "buttonScale"
                    )
                    
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(48.dp)
//                            .scale(buttonScale)
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(
//                                Brush.horizontalGradient(
//                                    colors = listOf(
//                                        colors.success.copy(alpha = 0.9f),
//                                        colors.success.copy(alpha = 0.7f)
//                                    )
//                                )
//                            )
//                            .shadow(4.dp, RoundedCornerShape(12.dp))
//                            .clickable(
//                                interactionSource = buttonInteractionSource,
//                                onClick = onViewDetails
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Check,
//                                contentDescription = null,
//                                tint = Color.White,
//                                modifier = Modifier.size(20.dp)
//                            )
//                            AppText(
//                                text = "View Challenge Details",
//                                style = AppTextStyle.Label,
//                                fontWeight = FontWeight.Bold,
//                                color = Color.White
//                            )
//                            Icon(
//                                imageVector = Icons.Default.KeyboardArrowRight,
//                                contentDescription = null,
//                                tint = Color.White,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//                    }
                } else {
                    // Join Button
                    val buttonInteractionSource = remember { MutableInteractionSource() }
                    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
                    
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isButtonPressed) 0.98f else 1f,
                        animationSpec = tween(durationMillis = 150),
                        label = "buttonScale"
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .scale(buttonScale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(buttonGradient)
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .clickable(
                                interactionSource = buttonInteractionSource,
                                enabled = !isJoining,
                                onClick = onJoin
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppText(
                                text = "Join Challenge Now",
                                style = AppTextStyle.Label,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
        
        // Decorative Element (top-right corner)
        val decorativeScale by animateFloatAsState(
            targetValue = if (isCardPressed) 1.5f else 1f,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            label = "decorativeScale"
        )
        
        Box(
            modifier = Modifier
                .size(128.dp)
                .offset(x = (-64).dp, y = (-64).dp)
                .align(Alignment.TopEnd)
                .scale(decorativeScale)
                .background(
                    colors.tint.copy(alpha = 0.05f),
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun ParticipantAvatarStack(participantCount: Int = 0) {
    val colors = LocalAppColors.current ?: return
    val avatarSize = 32.dp
    val overlap = 18.dp
    
    // Generate sample avatars based on participant count
    val avatarCount = min(3, participantCount)
    val sampleParticipants = listOf(
        Color(0xFFFFC1C1) to "AM",
        Color(0xFFB3E5FC) to "JK",
        Color(0xFFFFF59D) to "LS"
    ).take(avatarCount)
    
    if (sampleParticipants.isEmpty()) return
    
    val totalWidth = avatarSize + overlap * (sampleParticipants.size - 1)
    Box(
        modifier = Modifier
            .width(totalWidth)
            .height(avatarSize)
    ) {
        sampleParticipants.forEachIndexed { index, (bgColor, initials) ->
            Box(
                modifier = Modifier
                    .offset(x = overlap * index)
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                AppText(
                    text = initials,
                    style = AppTextStyle.Caption,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }
        }
    }
}


private fun formatDate(isoDateString: String): String {
    return com.app.screentime.utils.DateUtils.formatDate(isoDateString)
}


@Composable
private fun UpcomingChallengeCard(challenge: UpcomingChallenge) {
    val colors = LocalAppColors.current ?: return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = colors.accent.copy(alpha = 0.15f)
            ),
        colors = CardDefaults.cardColors(containerColor = colors.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title and Description
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                AppText(
                    text = challenge.description,
                    style = AppTextStyle.Body,
                    color = colors.textSecondary
                )
            }

            // Start Date
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.accent.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = colors.accent.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(18.dp)
                    )
                    AppText(
                        text = "Starts ${challenge.startsOn}",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.accent
                    )
                }
            }

            // Waitlist Button
            FilledTonalButton(
                onClick = { /* TODO: waitlist action */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = colors.accent.copy(alpha = 0.15f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                AppText(
                    text = "Join waitlist",
                    style = AppTextStyle.Label,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.accent
                )
            }
        }
    }
}

private data class UpcomingChallenge(
    val id: String, val title: String, val description: String, val startsOn: String
)

private val sampleUpcomingChallenges = listOf(
    UpcomingChallenge(
        id = "digital-detox",
        title = "Digital Detox Weekend",
        description = "Zero entertainment apps for 48 hours to unlock a focus badge.",
        startsOn = "Dec 12"
    ), UpcomingChallenge(
        id = "focus-marathon",
        title = "Focus Marathon",
        description = "Log 20 focused hours in 7 days and climb the leaderboard.",
        startsOn = "Dec 20"
    )
)

private val previewCurrentChallenge = Challenge(
    id = 1,
    title = "Reduce Screen Time Challenge",
    description = "Reduce your daily screen time by 30%",
    reward = "Premium Badge",
    startTime = "2024-01-15T00:00:00Z",
    endTime = "2024-01-31T23:59:59Z",
    thumbnail = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop"
)

private val previewJoinedChallenges = listOf(
    Challenge(
        id = 1,
        title = "Reduce Screen Time Challenge",
        description = "Reduce your daily screen time by 30% and earn rewards",
        reward = "Premium Badge",
        startTime = "2024-01-15T00:00:00Z",
        endTime = "2024-01-31T23:59:59Z",
        thumbnail = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop",
        hasJoined = true
    )
)

private val previewJoinedChallengesUiState = com.app.screentime.challenge.viewmodel.ChallengesUiState(
    isLoading = false,
    error = null,
    challenges = previewJoinedChallenges,
    joiningChallengeIds = emptySet()
)

@Preview(showBackground = true)
@Composable
private fun CurrentChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(
                    LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White
                )
                .padding(16.dp)
        ) {
            CurrentChallengeCard(
                challenge = previewCurrentChallenge,
                onViewDetails = {},
                onJoin = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpcomingChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(
                    LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White
                )
                .padding(16.dp)
        ) {
            UpcomingChallengeCard(challenge = sampleUpcomingChallenges.first())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JoinedChallengesTabPreview() {
    ChallengePreviewTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White
                )
        ) {
            // For preview, we'll show the tab with sample data
            // Note: This preview shows the UI but won't have full ViewModel functionality
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(previewJoinedChallenges, key = { it.id }) { challenge ->
                    CurrentChallengeCard(
                        challenge = challenge,
                        isJoining = false,
                        onViewDetails = {},
                        onJoin = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengePreviewTheme(content: @Composable () -> Unit) {
    val previewColors = remember { getThemeColors(ThemeType.CLASSIC_LIGHT) }
    CompositionLocalProvider(
        LocalThemeMode provides false, LocalAppColors provides previewColors
    ) {
        MaterialTheme(
            typography = Typography, content = content
        )
    }
}