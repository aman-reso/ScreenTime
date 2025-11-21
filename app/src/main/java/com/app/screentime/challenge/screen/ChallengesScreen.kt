package com.app.screentime.challenge.screen

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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.network.model.Challenge
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.ui.atom.AppLoader
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

    androidx.compose.runtime.LaunchedEffect(challengeIdInt) {
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
                        challengeIdInt?.let { viewModel.loadChallengeDetails(it) }
                    },
                    onJoinChallenge = {
                        challengeIdInt?.let { viewModel.joinChallenge(it) }
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
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
                        if (navController != null) {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back",
                                    tint = colors.tint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            AppText(
                                text = challenge.title,
                                style = AppTextStyle.Title,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                    }
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = colors.success,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val listState = rememberLazyListState()
            val scrollOffset = remember {
                derivedStateOf {
                    if (listState.firstVisibleItemIndex == 0) {
                        listState.firstVisibleItemScrollOffset.toFloat()
                    } else {
                        0f
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = if (!hasJoined) 100.dp else 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Top Image with Parallax Effect
                item {
                    if (challenge.thumbnail != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(0.dp))
                        ) {
                            ChallengeImage(
                                imageUrl = challenge.thumbnail,
                                appName = challenge.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp) // Slightly taller to allow parallax movement
                                    .graphicsLayer {
                                        translationY = scrollOffset.value * 0.5f
                                    }
                            )
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppText(
                            text = challenge.title,
                            style = AppTextStyle.Title,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )

                        AppText(
                            text = challenge.description,
                            style = AppTextStyle.Body,
                            color = colors.textSecondary
                        )

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
                                        modifier = Modifier.size(18.dp)
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
                    }
                }

                // Participants Section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(colors.card)
                            .border(
                                width = 1.dp,
                                color = colors.border.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AppText(
                                text = "Participants",
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ParticipantAvatarStack(
                                        participantCount = challengeRankings?.totalParticipants
                                            ?: challengeDetails?.participantCount ?: 0
                                    )
                                    Column {
                                        val participantCount = challengeRankings?.totalParticipants
                                            ?: challengeDetails?.participantCount ?: 0
                                        AppText(
                                            text = if (participantCount > 25) "$participantCount+ participants" else "$participantCount participants",
                                            style = AppTextStyle.Label,
                                            fontWeight = FontWeight.SemiBold,
                                            color = colors.textPrimary
                                        )
                                    }
                                }

                                if (challengeRankings?.rankings?.isNotEmpty() == true) {
                                    Icon(
                                        imageVector = Icons.Default.Outbound,
                                        contentDescription = "View all participants",
                                        tint = colors.textMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Current User Rank Section
                challengeRankings?.userRank?.let { userRank ->
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(colors.success.copy(alpha = 0.1f))
                                .border(
                                    width = 1.5.dp,
                                    color = colors.success.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    AppText(
                                        text = "Your Rank",
                                        style = AppTextStyle.Caption,
                                        color = colors.textMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = colors.success,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        AppText(
                                            text = "#${userRank.rank}",
                                            style = AppTextStyle.SubTitle,
                                            fontWeight = FontWeight.Bold,
                                            color = colors.success
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    AppText(
                                        text = "Your Duration",
                                        style = AppTextStyle.Caption,
                                        color = colors.textMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    AppText(
                                        text = formatDuration(userRank.totalDuration),
                                        style = AppTextStyle.SubTitle,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.textPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Top Participants List
                challengeRankings?.rankings?.takeIf { it.isNotEmpty() }?.let { rankings ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AppText(
                                text = "Top Participants",
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                    }

                    itemsIndexed(rankings.take(10)) { index, ranking ->
                        ChallengeRankingItem(
                            rank = ranking.rank,
                            userId = ranking.userId,
                            duration = ranking.totalDuration,
                            rankPosition = index + 1,
                            isCurrentUser = challengeRankings.userRank?.userId == ranking.userId
                        )
                    }
                }
            }

            // Join Challenge Button at Bottom
        }
        if (!hasJoined) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                GradientJoinButton(
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = isJoining,
                    enabled = !isJoining,
                    label = "Join Challenge",
                    onClick = onJoinChallenge
                )
            }
        }
    }
}

@Composable
private fun GradientJoinButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    enabled: Boolean,
    label: String,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val gradient = Brush.horizontalGradient(listOf(colors.accent, colors.success))
    val disabledBrush = Brush.horizontalGradient(
        listOf(colors.border, colors.border)
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(if (enabled) gradient else disabledBrush)
            .clickable(enabled = enabled && !isLoading) { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = colors.textOnPrimary,
                strokeWidth = 2.dp
            )
        } else {
            AppText(
                text = label,
                style = AppTextStyle.Label,
                fontWeight = FontWeight.Bold,
                color = colors.textOnPrimary
            )
        }
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
private fun ChallengeStatsCard(
    participantCount: Int?,
    userRank: Int?,
    totalParticipants: Int?,
    userDuration: Long?
) {
    val colors = LocalAppColors.current ?: return

    val participants = participantCount ?: totalParticipants ?: 0

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = colors.success,
                modifier = Modifier.size(24.dp)
            )
            AppText(
                text = "Challenge Statistics",
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        // Stats Grid
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // First Row: Participants and Rank
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Participant Count
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.success.copy(alpha = 0.08f))
                        .border(
                            width = 1.dp,
                            color = colors.success.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = colors.success,
                                modifier = Modifier.size(18.dp)
                            )
                            AppText(
                                text = "Participants",
                                style = AppTextStyle.Caption,
                                color = colors.textMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AppText(
                            text = participants.toString(),
                            style = AppTextStyle.Title,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                }

                // User Rank
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (userRank != null) colors.success.copy(alpha = 0.08f)
                            else colors.textMuted.copy(alpha = 0.08f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (userRank != null) colors.success.copy(alpha = 0.2f)
                            else colors.textMuted.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (userRank != null) colors.success else colors.textMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            AppText(
                                text = "Your Rank",
                                style = AppTextStyle.Caption,
                                color = colors.textMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (userRank != null) {
                            AppText(
                                text = "#$userRank",
                                style = AppTextStyle.Title,
                                fontWeight = FontWeight.Bold,
                                color = colors.success
                            )
                        } else {
                            AppText(
                                text = "Not ranked",
                                style = AppTextStyle.Body,
                                color = colors.textSecondary
                            )
                        }
                    }
                }
            }

            // User Duration if available
            userDuration?.let { duration ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.accent.copy(alpha = 0.08f))
                        .border(
                            width = 1.dp,
                            color = colors.accent.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(18.dp)
                            )
                            AppText(
                                text = "Your Duration",
                                style = AppTextStyle.Caption,
                                color = colors.textMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AppText(
                            text = formatDuration(duration),
                            style = AppTextStyle.SubTitle,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                }
            }

            // Rank summary
            if (userRank != null && totalParticipants != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.success.copy(alpha = 0.1f))
                        .padding(12.dp)
                ) {
                    AppText(
                        text = "You are ranked #$userRank out of $totalParticipants participants",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Medium,
                        color = colors.success
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
    return try {
        val instant = java.time.Instant.parse(isoDateString)
        val dateTime = instant.atZone(java.time.ZoneId.systemDefault())
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")
        dateTime.format(formatter)
    } catch (e: Exception) {
        isoDateString
    }
}

private fun formatMetric(value: Long, metricUnit: String?): String {
    return when (metricUnit?.lowercase()) {
        "ms", "milliseconds" -> formatDuration(value)
        "s", "seconds" -> formatDuration(value * 1000)
        "min", "minutes" -> formatDuration(value * 60 * 1000)
        else -> formatDuration(value)
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
                    LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White
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
                    LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White
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
