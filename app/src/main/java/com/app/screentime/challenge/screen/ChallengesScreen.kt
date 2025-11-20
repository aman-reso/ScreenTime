package com.app.screentime.challenge.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.challenge.viewmodel.ChallengesUiState
import com.app.screentime.network.model.ChallengeAppRanking
import com.app.screentime.network.model.ChallengeCompetitor
import com.app.screentime.network.model.ChallengeReward
import com.app.screentime.network.model.ChallengeTrend
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
    val selectedChallenge = uiState.challenges.find { it.challengeId == challengeId }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AppLoader(color = colors.success)
                }
            }

            uiState.error != null -> {
                ChallengeErrorState(
                    message = uiState.error,
                    onRetry = viewModel::refresh
                )
            }

            selectedChallenge == null -> {
                ChallengeErrorState(
                    message = "Challenge not found.",
                    onRetry = viewModel::refresh
                )
            }

            !selectedChallenge.isJoined -> {
                ChallengeNotJoinedState(
                    challenge = selectedChallenge,
                    onBack = { navController?.popBackStack() }
                )
            }

            else -> {
                ChallengeContent(
                    challenge = selectedChallenge,
                    lastUpdated = uiState.lastUpdated,
                    onRefresh = viewModel::refresh,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun ChallengeContent(
    challenge: ChallengeAppRanking,
    lastUpdated: String?,
    onRefresh: () -> Unit,
    navController: NavController?
) {
    val colors = LocalAppColors.current ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.tint
                        )
                    }
                }
                Column {
                    AppText(
                        text = challenge.appName,
                        style = AppTextStyle.Title,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    AppText(
                        text = challenge.description ?: "Track your rank and stay focused.",
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

        lastUpdated?.let {
            Spacer(modifier = Modifier.height(4.dp))
            AppText(
                text = "Updated $it",
                style = AppTextStyle.Caption,
                color = colors.textMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ChallengeCard(challenge = challenge)
            }
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
private fun ChallengeCard(challenge: ChallengeAppRanking) {
    val colors = LocalAppColors.current ?: return
    val rankProgress = if (challenge.totalParticipants <= 0) 0f
    else 1f - ((challenge.userRank - 1f) / challenge.totalParticipants.toFloat())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (challenge.iconUrl != null) {
                ChallengeImage(
                    imageUrl = challenge.iconUrl,
                    appName = challenge.appName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AppText(
                        text = challenge.appName,
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    challenge.description?.let {
                        Spacer(modifier = Modifier.height(2.dp))
                        AppText(
                            text = it,
                            style = AppTextStyle.Label,
                            color = colors.textSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                RankBadge(rank = challenge.userRank)
            }

            RankProgressIndicator(
                progress = rankProgress,
                userRank = challenge.userRank,
                total = challenge.totalParticipants
            )

            ChallengeMetricsRow(challenge)

            if (challenge.rewards.isNotEmpty()) {
                ChallengeRewardsSection(rewards = challenge.rewards)
            }

            if (challenge.topCompetitors.isNotEmpty()) {
                ChallengeLeaders(topCompetitors = challenge.topCompetitors)
            }

            //JoinChallengeCard()
        }
    }
}

@Composable
private fun ChallengeMetricsRow(challenge: ChallengeAppRanking) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ChallengeMetric(
            label = challenge.metricLabel ?: "Your usage",
            value = challenge.userMetricValue?.let { formatMetric(it, challenge.metricUnit) } ?: "--"
        )
        ChallengeMetric(
            label = "Participants",
            value = "${challenge.totalParticipants}"
        )
        ChallengeMetric(
            label = "Percentile",
            value = formatPercentile(challenge)
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
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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

@Composable
private fun RankProgressIndicator(progress: Float, userRank: Int, total: Int) {
    val colors = LocalAppColors.current ?: return
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppText(
                text = "Ahead of ${(progress * 100).roundToInt()}% players",
                style = AppTextStyle.Label,
                color = colors.textSecondary
            )
            AppText(
                text = "$userRank / $total",
                style = AppTextStyle.Label,
                color = colors.textSecondary
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            color = colors.success,
            trackColor = colors.border
        )
    }
}

@Composable
private fun ChallengeRewardsSection(rewards: List<ChallengeReward>) {
    val colors = LocalAppColors.current ?: return
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(16.dp)
            )
            AppText(
                text = "Rewards",
                style = AppTextStyle.Label,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            rewards.forEach { reward ->
                RewardItem(reward = reward)
            }
        }
    }
}

@Composable
private fun RewardItem(reward: ChallengeReward) {
    val colors = LocalAppColors.current ?: return
    val rewardIcon = when (reward.type.lowercase()) {
        "badge" -> Icons.Default.MilitaryTech
        "trophy" -> Icons.Default.EmojiEvents
        "points" -> Icons.Default.Star
        else -> Icons.Default.WorkspacePremium
    }
    
    val rewardColor = when (reward.tier?.lowercase()) {
        "gold" -> colors.rankGold
        "silver" -> colors.rankSilver
        "bronze" -> colors.rankBronze
        else -> colors.accent
    }
    
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(rewardColor.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = rewardIcon,
            contentDescription = null,
            tint = rewardColor,
            modifier = Modifier.size(12.dp)
        )
        reward.points?.let {
            AppText(
                text = "+$it",
                style = AppTextStyle.Caption,
                fontWeight = FontWeight.SemiBold,
                color = rewardColor,
                maxLines = 1
            )
        } ?: run {
            AppText(
                text = reward.title,
                style = AppTextStyle.Caption,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ChallengeLeaders(topCompetitors: List<ChallengeCompetitor>) {
    val colors = LocalAppColors.current ?: return
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                tint = colors.tint,
                modifier = Modifier.size(18.dp)
            )
            AppText(
                text = "Top challengers",
                style = AppTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            topCompetitors.take(3).forEach { competitor ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(colors.background),
                            contentAlignment = Alignment.Center
                        ) {
                            AppText(
                                text = competitor.rank.toString(),
                                style = AppTextStyle.Label,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        Column {
                            AppText(
                                text = competitor.username,
                                style = AppTextStyle.Body,
                                fontWeight = FontWeight.Medium,
                                color = colors.textPrimary
                            )
                            competitor.displayValue?.let {
                                AppText(
                                    text = it,
                                    style = AppTextStyle.Caption,
                                    color = colors.textMuted
                                )
                            }
                        }
                    }
                    val formattedMetric = competitor.displayValue
                        ?: competitor.metricValue?.let { formatMetric(it, null) }
                        ?: "--"
                    AppText(
                        text = formattedMetric,
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                }
            }
        }
    }
}

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
    challenge: ChallengeAppRanking,
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
            text = "You need to join the ${challenge.appName} challenge before you can view rankings and details.",
            style = AppTextStyle.Body,
            color = colors.textSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        FilledTonalButton(
            onClick = onBack,
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = colors.success.copy(alpha = 0.15f))
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

private fun formatPercentile(challenge: ChallengeAppRanking): String {
    val percentile = challenge.percentile
    return when {
        percentile != null -> "${percentile.roundToInt()}%"
        challenge.totalParticipants > 0 -> {
            val rank = max(1, challenge.userRank)
            val computed = (1f - ((rank - 1f) / challenge.totalParticipants)) * 100
            "${computed.roundToInt()}%"
        }
        else -> "--"
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
            lastUpdated = "Moments ago",
            onRefresh = {},
            navController = null
        )
    }
}

private val previewChallenges = listOf(
    ChallengeAppRanking(
        challengeId = "preview-youtube",
        appName = "YouTube",
        packageName = "com.google.android.youtube",
        iconUrl = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop",
        description = "Keep your watch time focused and below 120 minutes.",
        metricLabel = "Daily usage",
        metricUnit = "min",
        goalValue = 120,
        userRank = 4,
        totalParticipants = 150,
        userMetricValue = 90,
        percentile = 94.0,
        trend = ChallengeTrend(direction = "up", delta = 6.0),
        topCompetitors = listOf(
            ChallengeCompetitor("ZenMaster", 1, displayValue = "40 min"),
            ChallengeCompetitor("FocusFox", 2, displayValue = "55 min"),
            ChallengeCompetitor("BalanceBuddy", 3, displayValue = "62 min")
        ),
        isJoined = true,
        rewards = listOf(
            ChallengeReward(
                type = "badge",
                title = "Focus Master Badge",
                description = "Earned for maintaining watch time under 2 hours",
                points = 100,
                tier = "gold"
            ),
            ChallengeReward(
                type = "points",
                title = "Bonus Points",
                description = "500 points for top 10 finish",
                points = 500
            )
        )
    ),
    ChallengeAppRanking(
        challengeId = "preview-insta",
        appName = "Instagram",
        packageName = "com.instagram.android",
        iconUrl = "https://images.unsplash.com/photo-1611162616305-c69b3fa7fbe0?w=800&h=400&fit=crop",
        description = "Beat your circle by staying below 60 minutes.",
        metricLabel = "Daily usage",
        metricUnit = "min",
        goalValue = 60,
        userRank = 11,
        totalParticipants = 235,
        userMetricValue = 48,
        percentile = 81.0,
        trend = ChallengeTrend(direction = "steady", delta = 0.0),
        topCompetitors = listOf(
            ChallengeCompetitor("PhotoPro", 1, displayValue = "22 min"),
            ChallengeCompetitor("MindfulMike", 2, displayValue = "28 min"),
            ChallengeCompetitor("QuietMode", 3, displayValue = "33 min")
        ),
        isJoined = true,
        rewards = listOf(
            ChallengeReward(
                type = "trophy",
                title = "Social Media Champion",
                description = "Trophy for completing the Instagram challenge",
                tier = "silver"
            ),
            ChallengeReward(
                type = "points",
                title = "Challenge Points",
                description = "300 points for participation",
                points = 300
            )
        )
    )
)

@Preview(showBackground = true)
@Composable
private fun ChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White)
                .padding(16.dp)
        ) {
            ChallengeCard(challenge = previewChallenges.first())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JoinChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White)
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
