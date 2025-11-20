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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
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
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.navigation.Screen
import com.app.screentime.network.model.Challenge
import com.app.screentime.record.repository.formatDuration
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
                    items(uiState.challenges, key = { it.id }) { challenge ->
                        CurrentChallengeCard(
                            challenge = challenge,
                            isJoining = uiState.joiningChallengeIds.contains(challenge.id),
                            onViewDetails = {
                                navController?.navigate(
                                    Screen.ChallengeDetail.createRoute(challenge.id.toString())
                                )
                            },
                            onJoin = {
                                viewModel.joinChallenge(challenge.id)
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                    ),
                contentAlignment = Alignment.Center
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
    challenge: Challenge,
    isJoining: Boolean = false,
    onViewDetails: () -> Unit,
    onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val hasJoined = challenge.hasJoined
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(colors.card)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = colors.success.copy(alpha = 0.2f)
            )
    ) {
        if (challenge.thumbnail != null) {
            ChallengeImage(
                imageUrl = challenge.thumbnail,
                appName = challenge.title,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.95f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(colors.card.copy(alpha = 0.95f))
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                AppText(
                    text = challenge.description,
                    style = AppTextStyle.Label,
                    color = colors.textSecondary,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    ParticipantAvatarStack()
                    Column {
                        AppText(
                            text = "25+ participants",
                            style = AppTextStyle.Label,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary
                        )
                        AppText(
                            text = "Growing daily",
                            style = AppTextStyle.Caption,
                            color = colors.textSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                GradientJoinButton(
                    modifier = Modifier,
                    isLoading = isJoining,
                    enabled = !hasJoined && !isJoining,
                    label = if (hasJoined) "Already Joined" else "Join Challenge",
                    onClick = {
                        if (!hasJoined) {
                            onJoin()
                        }
                    }
                )
            }
        }

        OutlinedButton(
            onClick = onViewDetails,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(22.dp)),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.card.copy(alpha = 0.6f))
        ) {
            Icon(
                imageVector = Icons.Default.Outbound,
                contentDescription = null,
                tint = colors.tint
            )
            Spacer(modifier = Modifier.width(6.dp))
            AppText(
                text = "Details",
                style = AppTextStyle.Label,
                fontWeight = FontWeight.Medium,
                color = colors.tint
            )
        }
    }
}

@Composable
private fun ParticipantAvatarStack() {
    val colors = LocalAppColors.current ?: return
    val avatarSize = 32.dp
    val overlap = 18.dp
    val sampleParticipants = listOf(
        Color(0xFFFFC1C1) to "AM",
        Color(0xFFB3E5FC) to "JK",
        Color(0xFFFFF59D) to "LS"
    )
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
            .padding(horizontal = 20.dp, vertical = 10.dp),
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

private val previewCurrentChallenge = Challenge(
    id = 1,
    title = "Reduce Screen Time Challenge",
    description = "Reduce your daily screen time by 30%",
    reward = "Premium Badge",
    startTime = "2024-01-15T00:00:00Z",
    endTime = "2024-01-31T23:59:59Z",
    thumbnail = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop"
)

@Preview(showBackground = true)
@Composable
private fun CurrentChallengeCardPreview() {
    ChallengePreviewTheme {
        Column(
            modifier = Modifier
                .background(LocalAppColors.current?.background ?: androidx.compose.ui.graphics.Color.White)
                .padding(16.dp)
        ) {
            CurrentChallengeCard(
                challenge = previewCurrentChallenge,
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