package com.app.screentime.challenge.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Outbound
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Outbound
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.network.model.Challenge
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ScreenTimeTheme
import com.app.screentime.ui.theme.ThemeType
import com.app.screentime.ui.theme.Typography
import com.app.screentime.ui.theme.getThemeColors
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun ChallengeDetailScreen(
    challengeId: String,
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: ChallengeViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
    val uiState by viewModel.uiState.collectAsState()
    val challengeIdInt = challengeId.toIntOrNull()
    val selectedChallenge = challengeIdInt?.let { id ->
        uiState.challenges.find { it.id == id }
    }

    LaunchedEffect(challengeIdInt) {
        challengeIdInt?.let { viewModel.loadChallengeDetails(it) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        when {
            uiState.isLoading || uiState.isLoadingDetails -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AppLoader(color = colors.success)
                }
            }

            uiState.error != null || uiState.detailsError != null -> {
                ChallengeErrorState(
                    message = uiState.detailsError ?: uiState.error ?: "Failed to load challenge",
                    onRetry = {
                        challengeIdInt?.let { viewModel.loadChallengeDetails(it) }
                    }
                )
            }

            challengeIdInt == null -> {
                ChallengeErrorState(
                    message = "Invalid challenge ID.",
                    onRetry = { navController?.popBackStack() }
                )
            }

            selectedChallenge == null -> {
                ChallengeErrorState(
                    message = "Challenge not found.",
                    onRetry = viewModel::refresh
                )
            }

            else -> {
                ChallengeContent(
                    challenge = selectedChallenge,
                    challengeDetails = uiState.challengeDetails,
                    challengeRankings = uiState.challengeRankings,
                    isJoining = uiState.joiningChallengeIds.contains(selectedChallenge.id),
                    onRefresh = {
                        challengeIdInt.let { viewModel.loadChallengeDetails(it) }
                    },
                    onJoinChallenge = {
                        challengeIdInt.let { viewModel.joinChallenge(it) }
                    },
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun ChallengeContent(
    challenge: Challenge,
    challengeDetails: com.app.screentime.network.model.ChallengeDetails?,
    challengeRankings: com.app.screentime.network.model.ChallengeRankingsResponse?,
    isJoining: Boolean = false,
    onRefresh: () -> Unit,
    onJoinChallenge: () -> Unit,
    navController: NavController?
) {
    val colors = LocalAppColors.current ?: return
    val hasJoined = challenge.hasJoined
    val listState = rememberLazyListState()

    // Parallax effect calculation
    val scrollOffset = remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) {
                listState.firstVisibleItemScrollOffset.toFloat()
            } else {
                0f
            }
        }
    }

    // Check if challenge is completed
    val isCompleted = remember(challenge.endTime) {
        com.app.screentime.utils.DateUtils.isAfter(challenge.endTime)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colors.background)) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp) // Space for bottom bar
        ) {
            // 1. Parallax Header Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .graphicsLayer {
                            translationY = scrollOffset.value * 0.5f
                            alpha = 1f - (scrollOffset.value / 1000f).coerceIn(0f, 1f)
                        }
                ) {
                    if (challenge.thumbnail != null) {
                        AsyncImage(
                            model = challenge.thumbnail,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
        modifier = Modifier
            .fillMaxSize()
                                .background(colors.card)
                        )
                    }

                    // Gradient Overlay for text visibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        colors.background.copy(alpha = 0.9f),
                                        colors.background
                                    ),
                                    startY = 200f
                                )
                            )
                    )
                }
            }

            // 2. Title and Info
            item {
                Column(
                    modifier = Modifier
                        .offset(y = (-40).dp)
                        .padding(horizontal = 20.dp)
                ) {
                    // Tag & Sponsor
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!challenge.tag.isNullOrEmpty()) {
                            AppCard(
                                backgroundColor = colors.accent,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(50)
                            ) {
                                AppText(
                                    text = challenge.tag,
                                    style = AppTextStyle.Caption,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        if (!challenge.sponsor.isNullOrEmpty()) {
                            AppCard(
                                backgroundColor = colors.card.copy(alpha = 0.8f),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(50)
                            ) {
                                AppText(
                                    text = "by ${challenge.sponsor}",
                                    style = AppTextStyle.Caption,
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppText(
                        text = challenge.title,
                        style = AppTextStyle.Title,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Stats Row (Duration & Participants)
        Row(
            modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ChallengeStatCard(
                            icon = Icons.Default.Timer,
                            label = "Duration",
                            value = calculateChallengeDuration(
                                challenge.startTime,
                                challenge.endTime
                            ),
                            color = colors.accent,
                            modifier = Modifier.weight(1f)
                        )
                        ChallengeStatCard(
                            icon = Icons.Default.Group,
                            label = "Participants",
                            value = "${challengeRankings?.totalParticipants ?: challengeDetails?.participantCount ?: 0}",
                            color = colors.success,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 3. About Section
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(
                        title = "About Challenge",
                        icon = Icons.Default.Info,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AppText(
                        text = challenge.description,
                        style = AppTextStyle.Body,
                        color = colors.textSecondary,
                        modifier = Modifier.fillMaxWidth(),
                        lineHeight = 24.sp
                    )
                }
            }

            // 4. Timeline
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 32.dp)) {
                    SectionHeader(
                        title = "Timeline",
                        icon = Icons.Default.DateRange,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    AppCard(
                        backgroundColor = colors.card,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TimelineItem(
                                label = "Start",
                                date = formatDate(challenge.startTime),
                                color = colors.success
                            )

                            // Connector Line
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .padding(horizontal = 16.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(colors.border.copy(alpha = 0.3f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f) // Progress indicator example
                                        .fillMaxHeight()
                                        .background(colors.success)
                                )
                            }

                            TimelineItem(
                                label = "End",
                                date = formatDate(challenge.endTime),
                                color = colors.error
                            )
                        }
                    }
                }
            }

            // 5. Prizes
            if (!challenge.prize.isNullOrEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionHeader(
                            title = "Prizes & Rewards",
                            icon = Icons.Default.EmojiEvents,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        AppCard(
                            backgroundColor = colors.card,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.padding(24.dp)) {
                                PrizeContent(prizeHtml = challenge.prize, colors = colors)
                            }
                        }
                    }
                }
            }

            // 6. Rules
            if (!challenge.rules.isNullOrEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 32.dp)) {
                        SectionHeader(
                            title = "How to Win",
                            icon = Icons.Default.Flag,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AppCard(
                            backgroundColor = colors.card,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.padding(24.dp)) {
                                HowToParticipateContent(
                                    rulesHtml = challenge.rules,
                                    colors = colors
                                )
                            }
                        }
                    }
                }
            }

            // 7. Rankings (If joined)
            if (hasJoined) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionHeader(
                            title = "Leaderboard",
                            icon = Icons.Default.Leaderboard,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // User Rank Card
                        challengeRankings?.userRank?.let { userRank ->
                            AppCard(
                                backgroundColor = colors.success.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, colors.success.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ChallengeRankingItem(
                                    rank = userRank.rank,
                                    userId = "You",
                                    duration = userRank.totalDuration,
                                    rankPosition = userRank.rank,
                                    isCurrentUser = true
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Top Participants
                        challengeRankings?.rankings?.take(10)?.forEachIndexed { index, ranking ->
                            ChallengeRankingItem(
                                rank = ranking.rank,
                                userId = ranking.userId,
                                duration = ranking.totalDuration,
                                rankPosition = index + 1,
                                isCurrentUser = challengeRankings.userRank?.userId == ranking.userId
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        // Top Header (Always Visible)
        val showStickyHeader by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (showStickyHeader) colors.background.copy(alpha = 0.95f) else Color.Transparent
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButton(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (showStickyHeader) Color.Transparent else colors.background.copy(alpha = 0.8f)
                        )
                        .then(
                            if (!showStickyHeader) Modifier.border(1.dp, colors.border.copy(alpha = 0.2f), CircleShape)
                            else Modifier
                        )
                        .size(44.dp)
                ) {
                        Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }

                // Sticky Title
                androidx.compose.animation.AnimatedVisibility(
                    visible = showStickyHeader,
                    enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.slideInVertically { -it },
                    exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.slideOutVertically { -it },
                    modifier = Modifier.weight(1f)
                ) {
                    AppText(
                        text = challenge.title,
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                if (!showStickyHeader) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Share Button
                IconButton(
                    onClick = { /* TODO: Share */ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (showStickyHeader) Color.Transparent else colors.background.copy(alpha = 0.8f)
                        )
                        .then(
                            if (!showStickyHeader) Modifier.border(1.dp, colors.border.copy(alpha = 0.2f), CircleShape)
                            else Modifier
                        )
                        .size(44.dp)
                ) {
                Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = colors.textPrimary
                    )
                }
            }
        }

        // Bottom Action Bar
        if (!hasJoined && !isCompleted) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, colors.background)
                        )
                    )
                    .padding(20.dp)
            ) {
                AppPrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            12.dp,
                            RoundedCornerShape(28.dp),
                            spotColor = colors.success.copy(alpha = 0.5f)
                        ),
                    text = "Join Challenge",
                    enabled = !isJoining,
                    onClick = onJoinChallenge
                )
            }
        }
    }
}

@Composable
private fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    shape: Shape = RoundedCornerShape(16.dp),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .padding(contentPadding)
    ) {
        content()
    }
}

@Composable
private fun ChallengeStatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current ?: return

    AppCard(
        modifier = modifier.height(140.dp), // Fixed height for consistency
        backgroundColor = colors.card,
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                AppText(
                    text = value,
                    style = AppTextStyle.Title,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppText(
                    text = label,
                    style = AppTextStyle.Caption,
                    color = colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    val colors = LocalAppColors.current ?: return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.card),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        AppText(
            text = title,
            style = AppTextStyle.SubTitle,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun TimelineItem(label: String, date: String, color: Color) {
    val colors = LocalAppColors.current ?: return
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AppText(
            text = label,
            style = AppTextStyle.Caption,
            color = colors.textSecondary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        AppText(
            text = date,
            style = AppTextStyle.Label,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}


@Composable
private fun ChallengeImage(
    imageUrl: String,
    appName: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colors.border.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
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
private fun ChallengeCard(
    challenge: Challenge,
    challengeDetails: com.app.screentime.network.model.ChallengeDetails?
) {
    val colors = LocalAppColors.current ?: return

    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)
    val isActive = challengeDetails?.isActive ?: true

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Thumbnail
            if (challenge.thumbnail != null) {
                ChallengeImage(
                    imageUrl = challenge.thumbnail,
                    appName = challenge.title,
                    modifier = Modifier
                        .fillMaxWidth()
                    .height(200.dp)
                )
            }
            
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

        // Reward Badge
            if (challenge.reward.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.success.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = colors.success.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = colors.success,
                        modifier = Modifier.size(20.dp)
                    )
                AppText(
                        text = challenge.reward,
                    style = AppTextStyle.Label,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.success
                )
                }
            }
        }

        // Status Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isActive) colors.success.copy(alpha = 0.1f)
                    else colors.textMuted.copy(alpha = 0.1f)
                )
                .border(
                    width = 1.dp,
                    color = if (isActive) colors.success.copy(alpha = 0.3f)
                    else colors.textMuted.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = null,
                    tint = if (isActive) colors.success else colors.textMuted,
                    modifier = Modifier.size(16.dp)
                )
                AppText(
                    text = if (isActive) "Active Challenge" else "Inactive",
                    style = AppTextStyle.Caption,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isActive) colors.success else colors.textMuted
                )
            }
        }

        // Date Range
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
        modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = colors.textMuted,
                    modifier = Modifier.size(18.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
            AppText(
                        text = "Start Date",
                        style = AppTextStyle.Caption,
                        color = colors.textMuted
                    )
                    AppText(
                        text = startDate,
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Medium,
                color = colors.textPrimary
            )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = colors.textMuted,
                    modifier = Modifier.size(18.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    AppText(
                        text = "End Date",
                        style = AppTextStyle.Caption,
                        color = colors.textMuted
                    )
                    AppText(
                        text = endDate,
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary
                    )
                }
            }
        }
    }
}


@Composable
private fun ParticipantAvatarStack(participantCount: Int) {
    val colors = LocalAppColors.current ?: return
    val avatarSize = 32.dp
    val overlap = 18.dp
    // Generate sample avatars based on participant count
    val avatarCount = minOf(3, participantCount)
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

@Composable
private fun ChallengeRankingItem(
    rank: Int,
    userId: String,
    duration: Long,
    rankPosition: Int,
    isCurrentUser: Boolean = false
) {
    val colors = LocalAppColors.current ?: return

    val rankColor = when (rankPosition) {
        1 -> colors.rankGold
        2 -> colors.rankSilver
        3 -> colors.rankBronze
        else -> colors.card
    }

    val cardBackground = when {
        isCurrentUser -> colors.success.copy(alpha = 0.15f)
        rankPosition <= 3 -> rankColor.copy(alpha = 0.08f)
        else -> colors.card
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardBackground, shape = RoundedCornerShape(12.dp))
            .then(
                when {
                    isCurrentUser -> Modifier.border(
                        width = 1.5.dp,
                        color = colors.success,
                        shape = RoundedCornerShape(12.dp)
                    )

                    rankPosition <= 3 -> Modifier.border(
                        width = 1.5.dp,
                        color = rankColor,
                        shape = RoundedCornerShape(12.dp)
                    )

                    else -> Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Badge
            Box(
                modifier = Modifier
                    .size(if (rankPosition <= 3) 48.dp else 40.dp)
                    .shadow(
                        elevation = if (rankPosition <= 3) 4.dp else 2.dp,
                        shape = CircleShape,
                        spotColor = if (rankPosition <= 3) rankColor.copy(alpha = 0.5f) else colors.textPrimary.copy(
                            alpha = 0.2f
                        )
                    )
                    .background(
                        color = if (rankPosition <= 3) rankColor else colors.card,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (rankPosition <= 3) {
                            Icon(
                        imageVector = when (rankPosition) {
                            1 -> Icons.Default.EmojiEvents
                            2 -> Icons.Default.MilitaryTech
                            else -> Icons.Default.Star
                        },
                                contentDescription = null,
                        tint = colors.textOnPrimary,
                        modifier = Modifier.size(if (rankPosition == 1) 24.dp else 20.dp)
                            )
                } else {
                            AppText(
                        text = "#$rank",
                        style = AppTextStyle.Body,
                                fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                        AppText(
                        text = userId,
                            style = AppTextStyle.Body,
                        fontWeight = if (isCurrentUser || rankPosition <= 3) FontWeight.Bold else FontWeight.Medium,
                        color = colors.textPrimary
                    )
                    if (isCurrentUser) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = colors.success.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            AppText(
                                text = "You",
                                style = AppTextStyle.Label,
                                color = colors.success,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    AppText(
                        text = formatDuration(duration),
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Medium,
                        color = colors.textSecondary
                    )
                }
            }

            if (rankPosition <= 3) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = rankColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun ChallengeMetricsRow(challenge: Challenge) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ChallengeMetric(
            label = "Start Time",
            value = formatDate(challenge.startTime)
        )
        ChallengeMetric(
            label = "End Time",
            value = formatDate(challenge.endTime)
        )
    }
}

@Composable
private fun ChallengeMetric(label: String, value: String) {
    val colors = LocalAppColors.current ?: return
    Column {
        AppText(
            text = label,
            style = AppTextStyle.Caption,
            color = colors.textMuted
        )
        AppText(
            text = value,
            style = AppTextStyle.Body,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun RankBadge(rank: Int) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colors.success.copy(alpha = 0.15f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = colors.success,
                modifier = Modifier.size(18.dp)
            )
            AppText(
                text = "Rank #$rank",
                style = AppTextStyle.Label,
                fontWeight = FontWeight.SemiBold,
                color = colors.success
            )
        }
    }
}

// Removed unused functions that referenced old ChallengeAppRanking model

@Composable
private fun JoinChallengeCard() {
    val colors = LocalAppColors.current ?: return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.success.copy(alpha = 0.08f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AppText(
                    text = "Join this challenge",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.success
                )
                AppText(
                    text = "Sign in on the web dashboard to participate and improve your rank.",
                    style = AppTextStyle.Label,
                    color = colors.textSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                tint = colors.success
            )
        }
        AppText(
            text = "Coming soon",
            style = AppTextStyle.Caption,
            color = colors.textMuted
        )
    }
}

@Composable
private fun ChallengeErrorState(message: String?, onRetry: () -> Unit) {
    val colors = LocalAppColors.current ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = message ?: "Something went wrong",
            style = AppTextStyle.Body,
            color = colors.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = onRetry) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Retry",
                tint = colors.success
            )
        }
    }
}

@Composable
private fun ChallengeNotJoinedState(
    challenge: Challenge,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Flag,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        AppText(
            text = "Join to view details",
            style = AppTextStyle.Title,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppText(
            text = "You need to join the ${challenge.title} challenge before you can view rankings and details.",
            style = AppTextStyle.Body,
            color = colors.textSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        FilledTonalButton(
            onClick = onBack,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = colors.success.copy(
                    alpha = 0.15f
                )
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = colors.success
            )
            Spacer(modifier = Modifier.size(8.dp))
            AppText(
                text = "Go back",
                style = AppTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = colors.success
            )
        }
    }
}

@Composable
private fun ChallengeEmptyState() {
    val colors = LocalAppColors.current ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Flag,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.size(48.dp)
        )
        AppText(
            text = "No active challenges yet",
            style = AppTextStyle.SubTitle,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
        AppText(
            text = "Join a challenge from the web dashboard to track your rank.",
            style = AppTextStyle.Label,
            color = colors.textSecondary,
            modifier = Modifier.padding(horizontal = 24.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun formatDate(isoDateString: String): String {
    return com.app.screentime.utils.DateUtils.formatDate(isoDateString)
}

private fun formatMetric(value: Long, metricUnit: String?): String {
    return when (metricUnit?.lowercase()) {
        "ms", "milliseconds" -> formatDuration(value)
        "s", "seconds" -> formatDuration(value * 1000)
        "min", "minutes" -> formatDuration(value * 60 * 1000)
        else -> formatDuration(value)
    }
}

@Composable
private fun PrizeContent(prizeHtml: String, colors: com.app.screentime.ui.theme.AppColors) {
    // Parse HTML content and extract prize information
    val prizeItems = remember(prizeHtml) {
        parsePrizeHtml(prizeHtml)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        prizeItems.forEach { (rank, points) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = rank,
                    style = AppTextStyle.Label,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                AppText(
                    text = points,
                    style = AppTextStyle.Label,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )
            }
        }
    }
}

private fun parsePrizeHtml(html: String): List<Pair<String, String>> {
    val items = mutableListOf<Pair<String, String>>()

    // Remove HTML tags and extract content
    // Pattern: <div><strong>Rank X:</strong> Y points</div>
    val divPattern = Regex("<div>(.*?)</div>", RegexOption.DOT_MATCHES_ALL)
    val strongPattern = Regex("<strong>(.*?)</strong>")

    divPattern.findAll(html).forEach { divMatch ->
        val content = divMatch.groupValues[1]
        val strongMatch = strongPattern.find(content)

        if (strongMatch != null) {
            val rankText = strongMatch.groupValues[1].trim()
            val pointsText = content.replace(strongMatch.value, "").trim()
            items.add(rankText to pointsText)
        }
    }

    return items
}

@Composable
private fun HowToParticipateContent(
    rulesHtml: String,
    colors: com.app.screentime.ui.theme.AppColors
) {
    // Parse HTML content and extract rules
    val rulesItems = remember(rulesHtml) {
        parseRulesHtml(rulesHtml)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rulesItems.forEachIndexed { index, rule ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Numbered badge with accent color background
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(colors.accent.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(
                        text = "${index + 1}",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Bold,
                        color = colors.textOnPrimary
                    )
                }
                AppText(
                    text = rule,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun parseRulesHtml(html: String): List<String> {
    val items = mutableListOf<String>()

    // Extract list items from <ul><li>...</li></ul>
    val liPattern = Regex("<li>(.*?)</li>", RegexOption.DOT_MATCHES_ALL)

    liPattern.findAll(html).forEach { match ->
        val content = match.groupValues[1]
        // Remove any remaining HTML tags
        val cleanText = content
            .replace(Regex("<[^>]+>"), "")
            .trim()
        if (cleanText.isNotEmpty()) {
            items.add(cleanText)
        }
    }

    // If no list items found, try to extract from divs
    if (items.isEmpty()) {
        val divPattern = Regex("<div>(.*?)</div>", RegexOption.DOT_MATCHES_ALL)
        divPattern.findAll(html).forEach { match ->
            val content = match.groupValues[1]
            val cleanText = content
                .replace(Regex("<[^>]+>"), "")
                .trim()
            if (cleanText.isNotEmpty() && !cleanText.equals("Rules:", ignoreCase = true)) {
                items.add(cleanText)
            }
        }
    }

    return items
}

@Composable
private fun ChallengeInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconTint: Color
) {
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colors.card.copy(alpha = 0.8f))
            .border(
                width = 1.dp,
                color = colors.border.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            AppText(
                text = label,
                style = AppTextStyle.Caption,
                color = colors.textSecondary
            )
            AppText(
                text = value,
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }
    }
}


private fun calculateChallengeDuration(startTime: String, endTime: String): String {
    return com.app.screentime.utils.DateUtils.formatChallengeDuration(startTime, endTime)
}

private fun formatTime(isoDateString: String): String {
    return com.app.screentime.utils.DateUtils.formatDateTime(isoDateString)
}

@Composable
private fun ChallengeParticipantsCard(
    participantCount: Int,
    modifier: Modifier = Modifier,
    iconTint: Color
) {
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colors.card.copy(alpha = 0.8f))
            .border(
                width = 1.dp,
                color = colors.border.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            AppText(
                text = "Joined",
                style = AppTextStyle.Caption,
                color = colors.textSecondary
            )

            // Participant stack and count
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ParticipantAvatarStack(participantCount = participantCount)
                AppText(
                    text = "$participantCount",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChallengeDetailScreenPreview() {
    ChallengePreviewTheme {
        ChallengeContent(
            challenge = previewChallenges.first(),
            challengeDetails = null,
            challengeRankings = null,
            onRefresh = {},
            navController = null, onJoinChallenge = {}
        )
    }
}

private val previewChallenges = listOf(
    Challenge(
        id = 1,
        title = "Reduce Screen Time Challenge",
        description = "Reduce your daily screen time by 30%",
        reward = "Premium Badge",
        startTime = "2024-01-15T00:00:00Z",
        endTime = "2024-01-31T23:59:59Z",
        thumbnail = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop"
    )
)

@Preview(showBackground = true)
@Composable
private fun ChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(
                    LocalAppColors.current?.background
                        ?: androidx.compose.ui.graphics.Color.White
                )
                .padding(16.dp)
        ) {
            ChallengeCard(challenge = previewChallenges.first(), null)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JoinChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(
                    LocalAppColors.current?.background
                        ?: androidx.compose.ui.graphics.Color.White
                )
                .padding(16.dp)
        ) {
            JoinChallengeCard()
        }
    }
}

@Composable
private fun ChallengePreviewTheme(content: @Composable () -> Unit) {
    val previewColors = remember { getThemeColors(ThemeType.CLASSIC_LIGHT) }
    CompositionLocalProvider(
        LocalThemeMode provides false,
        LocalAppColors provides previewColors
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}
