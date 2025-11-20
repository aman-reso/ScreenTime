package com.app.screentime.challenge.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Outbound
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.navigation.Screen
import com.app.screentime.network.model.ChallengeAppRanking
import com.app.screentime.network.model.ChallengeCompetitor
import com.app.screentime.network.model.ChallengeReward
import com.app.screentime.network.model.ChallengeTrend
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ThemeType
import com.app.screentime.ui.theme.Typography
import com.app.screentime.ui.theme.getThemeColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun ChallengeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: ChallengeViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
    val uiState by viewModel.uiState.collectAsState()

    val upcomingChallenges = remember { sampleUpcomingChallenges }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(top = 12.dp)
    ) {
        Header(onRefresh = viewModel::refresh)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
                                text = uiState.error ?: "Unable to load challenges.",
                                style = AppTextStyle.Body,
                                color = colors.error
                            )
                            TextButton(onClick = viewModel::refresh) {
                                AppText(
                                    text = "Retry",
                                    style = AppTextStyle.Label,
                                    color = colors.success
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
                    items(uiState.challenges, key = { it.challengeId }) { challenge ->
                        CurrentChallengeCard(
                            challenge = challenge,
                            isJoining = uiState.joiningChallengeIds.contains(challenge.challengeId),
                            onViewDetails = {
                                navController?.navigate(
                                    Screen.ChallengeDetail.createRoute(challenge.challengeId)
                                )
                            },
                            onJoin = {
                                viewModel.joinChallenge(challenge.challengeId)
                            }
                        )
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
}

@Composable
private fun Header(onRefresh: () -> Unit) {
    val colors = LocalAppColors.current ?: return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = colors.success
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, tint: androidx.compose.ui.graphics.Color) {
    AppText(
        text = title,
        style = AppTextStyle.SubTitle,
        fontWeight = FontWeight.Bold,
        color = tint
    )
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
private fun CurrentChallengeCard(
    challenge: ChallengeAppRanking,
    isJoining: Boolean = false,
    onViewDetails: () -> Unit,
    onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.card)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (challenge.iconUrl != null) {
                ChallengeImage(
                    imageUrl = challenge.iconUrl,
                    appName = challenge.appName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
            
            AppText(
                text = challenge.appName,
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            AppText(
                text = challenge.description ?: "Daily usage goal ${challenge.goalValue ?: 0} minutes.",
                style = AppTextStyle.Label,
                color = colors.textSecondary
            )
            
            if (challenge.rewards.isNotEmpty()) {
                ChallengeRewardsPreview(rewards = challenge.rewards)
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (challenge.isJoined) {
                    OutlinedButton(onClick = onViewDetails) {
                        Icon(
                            imageVector = Icons.Default.Outbound,
                            contentDescription = null,
                            tint = colors.tint
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AppText(
                            text = "View",
                            style = AppTextStyle.Label,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.tint
                        )
                    }
                } else {
                    FilledTonalButton(
                        onClick = onJoin,
                        enabled = !isJoining,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = colors.success.copy(alpha = 0.15f))
                    ) {
                        if (isJoining) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = colors.success,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AppText(
                                text = "Joining...",
                                style = AppTextStyle.Label,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.success
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = null,
                                tint = colors.success
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AppText(
                                text = "Join now",
                                style = AppTextStyle.Label,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.success
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeRewardsPreview(rewards: List<ChallengeReward>) {
    val colors = LocalAppColors.current ?: return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rewards.take(3).forEach { reward ->
            RewardBadge(reward = reward)
        }
    }
}

@Composable
private fun RewardBadge(reward: ChallengeReward) {
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
private fun UpcomingChallengeCard(challenge: UpcomingChallenge) {
    val colors = LocalAppColors.current ?: return
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.card)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppText(
                text = challenge.title,
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            AppText(
                text = challenge.description,
                style = AppTextStyle.Label,
                color = colors.textSecondary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = colors.textMuted
                )
                AppText(
                    text = "Starts ${challenge.startsOn}",
                    style = AppTextStyle.Caption,
                    color = colors.textMuted
                )
            }
            FilledTonalButton(
                onClick = { /* TODO: waitlist action */ },
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = colors.accent.copy(alpha = 0.15f))
            ) {
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
    val id: String,
    val title: String,
    val description: String,
    val startsOn: String
)

private val sampleUpcomingChallenges = listOf(
    UpcomingChallenge(
        id = "digital-detox",
        title = "Digital Detox Weekend",
        description = "Zero entertainment apps for 48 hours to unlock a focus badge.",
        startsOn = "Dec 12"
    ),
    UpcomingChallenge(
        id = "focus-marathon",
        title = "Focus Marathon",
        description = "Log 20 focused hours in 7 days and climb the leaderboard.",
        startsOn = "Dec 20"
    )
)

private val previewCurrentChallengeJoined = ChallengeAppRanking(
    challengeId = "preview-joined",
    appName = "YouTube",
    packageName = "com.google.android.youtube",
    iconUrl = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop",
    description = "Keep daily watch time under two hours",
    metricLabel = "Daily usage",
    metricUnit = "min",
    goalValue = 120,
    userRank = 5,
    totalParticipants = 128,
    userMetricValue = 95,
    percentile = 92.0,
    trend = ChallengeTrend(direction = "up", delta = 4.0),
    topCompetitors = emptyList(),
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
)

private val previewCurrentChallengeNotJoined = ChallengeAppRanking(
    challengeId = "preview-not-joined",
    appName = "Instagram",
    packageName = "com.instagram.android",
    iconUrl = "https://images.unsplash.com/photo-1611162616305-c69b3fa7fbe0?w=800&h=400&fit=crop",
    description = "Beat your friends by staying under 60 minutes",
    metricLabel = "Daily usage",
    metricUnit = "min",
    goalValue = 60,
    userRank = 0,
    totalParticipants = 210,
    userMetricValue = null,
    percentile = null,
    trend = null,
    topCompetitors = emptyList(),
    isJoined = false,
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

@Preview(showBackground = true)
@Composable
private fun CurrentChallengeCardJoinedPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White)
                .padding(16.dp)
        ) {
            CurrentChallengeCard(
                challenge = previewCurrentChallengeJoined,
                onViewDetails = {},
                onJoin = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CurrentChallengeCardNotJoinedPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White)
                .padding(16.dp)
        ) {
            CurrentChallengeCard(
                challenge = previewCurrentChallengeNotJoined,
                onViewDetails = {},
                onJoin = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpcomingChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White)
                .padding(16.dp)
        ) {
            UpcomingChallengeCard(challenge = sampleUpcomingChallenges.first())
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