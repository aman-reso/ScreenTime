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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.shadow
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
        // Custom Segmented Control
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SegmentedControl(
                items = tabs, selectedIndex = pagerState.currentPage, onItemSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
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
                items(joinedChallenges, key = { it.id }) { challenge ->
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
private fun SegmentedControl(
    items: List<String>, selectedIndex: Int, onItemSelected: (Int) -> Unit
) {
    val colors = LocalAppColors.current ?: return

    // Create a gradient-like background color (reddish-purple translucent)
    val backgroundColor = colors.success.copy(alpha = 0.2f).let { baseColor ->
        androidx.compose.ui.graphics.Color(
            red = (baseColor.red * 255 + 20).coerceAtMost(255f) / 255f,
            green = (baseColor.green * 255 - 10).coerceAtLeast(0f) / 255f,
            blue = (baseColor.blue * 255 + 30).coerceAtMost(255f) / 255f,
            alpha = 0.25f
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            if (isSelected) {
                                colors.card // White/light background for selected
                            } else {
                                Color.Transparent
                            }
                        )
                        .clickable { onItemSelected(index) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center) {
                    AppText(
                        text = item,
                        style = AppTextStyle.Body,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) {
                            colors.textPrimary // Dark text for selected (white background)
                        } else {
                            colors.textOnPrimary.copy(alpha = 0.95f) // White/light text for unselected
                        }
                    )
                }
            }
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
    challenge: Challenge, isJoining: Boolean = false, onViewDetails: () -> Unit, onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val hasJoined = challenge.hasJoined

    // Format dates
    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.card)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (image, title, subtitle, reward, dateRow, joinButton, detailsIcon, handshakeIcon) = createRefs()

            if (challenge.thumbnail != null) {
                ChallengeImage(
                    imageUrl = challenge.thumbnail,
                    appName = challenge.title,
                    modifier = Modifier
                        .width(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(reward.bottom)
                            height = Dimension.fillToConstraints
                        })
            }

            AppText(
                text = challenge.title,
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(image.top)
                    start.linkTo(
                        if (challenge.thumbnail != null) image.end else parent.start, margin = 16.dp
                    )
                    end.linkTo(
                        if (hasJoined) handshakeIcon.start else detailsIcon.start, margin = 8.dp
                    )
                    width = Dimension.fillToConstraints
                })

            Row(
                modifier = Modifier.constrainAs(dateRow) {
                    top.linkTo(
                        title.bottom, margin = 6.dp
                    )
                    start.linkTo(title.start)
                    end.linkTo(title.end)
                    width = Dimension.fillToConstraints
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = colors.textMuted,
                    modifier = Modifier.size(14.dp)
                )
                AppText(
                    text = startDate, style = AppTextStyle.Caption, color = colors.textMuted
                )
                AppText(
                    text = "•", style = AppTextStyle.Caption, color = colors.textMuted
                )
                AppText(
                    text = endDate, style = AppTextStyle.Caption, color = colors.textMuted
                )
            }

            AppText(
                text = challenge.description,
                style = AppTextStyle.Label,
                color = colors.textSecondary,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(subtitle) {
                    top.linkTo(reward.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })

            if (challenge.reward.isNotEmpty()) {
                Box(
                    modifier = Modifier.constrainAs(reward) {
                        top.linkTo(dateRow.bottom, 6.dp)
                        start.linkTo(dateRow.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.success.copy(alpha = 0.1f))
                            .border(
                                width = 1.dp,
                                color = colors.success.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = colors.success,
                            modifier = Modifier.size(14.dp)
                        )
                        AppText(
                            text = challenge.reward,
                            style = AppTextStyle.Caption,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.success,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }


            AppPrimaryButton(text = "Join Challenge", enabled = true, onClick = {
                onJoin()
            }, modifier = Modifier.constrainAs(joinButton) {
                top.linkTo(subtitle.bottom, margin = 6.dp)
                start.linkTo(parent.start)
                end.linkTo(detailsIcon.start, margin = 8.dp)
                width = Dimension.fillToConstraints
            })

            IconButton(
                onClick = onViewDetails,
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(detailsIcon) {
                        top.linkTo(title.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(dateRow.bottom)
                    }) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = colors.tint.copy(alpha = 0.15f), shape = CircleShape
                        )
                        .padding(6.dp), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (false) {
                            Icons.Default.KeyboardArrowRight
                        } else {
                            Icons.Default.Check
                        },
                        contentDescription = "Details",
                        tint = colors.tint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ParticipantAvatarStack() {
    val colors = LocalAppColors.current ?: return
    val avatarSize = 32.dp
    val overlap = 18.dp
    val sampleParticipants = listOf(
        Color(0xFFFFC1C1) to "AM", Color(0xFFB3E5FC) to "JK", Color(0xFFFFF59D) to "LS"
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
                    .background(bgColor), contentAlignment = Alignment.Center
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