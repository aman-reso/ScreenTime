package com.app.screentime.leaderboard.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import com.app.screentime.ui.atom.SegmentedControl
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.leaderboard.viewmodel.LeaderboardViewModel
import com.app.screentime.network.model.LeaderboardEntry
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.ui.atom.AppLoader
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf("Daily", "Weekly")
    val pagerState = rememberPagerState(pageCount = { tabs.size }, initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(colors.background)
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
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
                            text = "Leaderboard",
                            style = AppTextStyle.Title,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                    IconButton(
                        onClick = { viewModel.refresh() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh),
                            tint = colors.success,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Segmented Control
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
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

            Spacer(modifier = Modifier.height(8.dp))

            // Content
            HorizontalPager(
                pageSpacing = 12.dp,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> DailyLeaderboardContent(
                        entries = uiState.dailyEntries,
                        userRank = uiState.userDailyRank,
                        userDuration = uiState.userDailyDuration,
                        currentUsername = uiState.currentUsername,
                        isLoading = uiState.isLoading,
                        error = uiState.error
                    )

                    1 -> WeeklyLeaderboardContent(
                        entries = uiState.weeklyEntries,
                        userRank = uiState.userWeeklyRank,
                        userDuration = uiState.userWeeklyDuration,
                        currentUsername = uiState.currentUsername,
                        isLoading = uiState.isLoading,
                        error = uiState.error
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyLeaderboardContent(
    entries: List<LeaderboardEntry>,
    userRank: Int?,
    userDuration: Long?,
    currentUsername: String?,
    isLoading: Boolean,
    error: String?
) {
    LeaderboardContent(
        title = "Daily Leaderboard",
        entries = entries,
        userRank = userRank,
        userDuration = userDuration,
        currentUsername = currentUsername,
        isLoading = isLoading,
        error = error
    )
}

@Composable
private fun WeeklyLeaderboardContent(
    entries: List<LeaderboardEntry>,
    userRank: Int?,
    userDuration: Long?,
    currentUsername: String?,
    isLoading: Boolean,
    error: String?
) {
    LeaderboardContent(
        title = "Weekly Leaderboard",
        entries = entries,
        userRank = userRank,
        userDuration = userDuration,
        currentUsername = currentUsername,
        isLoading = isLoading,
        error = error
    )
}

@Composable
private fun LeaderboardContent(
    title: String,
    entries: List<LeaderboardEntry>,
    userRank: Int?,
    userDuration: Long?,
    currentUsername: String?,
    isLoading: Boolean,
    error: String?
) {
    val colors = LocalAppColors.current ?: return

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AppLoader(color = colors.success)
        }
        return
    }

    if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = colors.error,
                    modifier = Modifier.size(48.dp)
                )
                AppText(
                    text = error,
                    style = AppTextStyle.Body,
                    color = colors.error
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        if (userRank != null && userDuration != null && currentUsername != null) {
            item {
                UserProfileCard(
                    username = currentUsername,
                    rank = userRank,
                    duration = entries.find { it.username == currentUsername }?.totalScreenTime?.let {
                        formatDuration(
                            it
                        )
                    }
                        ?: formatDuration(userDuration),
                    isDaily = title.contains("Daily", ignoreCase = true)
                )
            }
        }

        // Top 10 List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = colors.success,
                        modifier = Modifier.size(20.dp)
                    )
                    AppText(
                        text = "Top 10",
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }
                AppText(
                    text = "${entries.size} players",
                    style = AppTextStyle.Label,
                    color = colors.textSecondary
                )
            }
        }

        itemsIndexed(entries.take(10)) { index, entry ->
            LeaderboardItem(
                rank = entry.rank,
                username = entry.username,
                name = entry.name,
                duration = formatDuration(entry.totalScreenTime),
                isCurrentUser = entry.username == currentUsername,
                rankPosition = index + 1
            )
        }

        if (entries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Leaderboard,
                            contentDescription = null,
                            tint = colors.textMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        AppText(
                            text = stringResource(R.string.no_leaderboard_data_available),
                            style = AppTextStyle.Body,
                            color = colors.textMuted
                        )
                    }
                }
            }
        }
    }
}

/**
 * Get initials from a name or username
 */
private fun getInitials(name: String?, username: String): String {
    val text = name?.trim() ?: username.trim()
    if (text.isEmpty()) return "?"
    
    val parts = text.split(" ", ".", "_", "-").filter { it.isNotEmpty() }
    return when {
        parts.size >= 2 -> "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
        text.length >= 2 -> text.take(2).uppercase()
        else -> text.first().uppercaseChar().toString()
    }
}

/**
 * Get a color for an avatar based on the name/username
 */
private fun getAvatarColor(name: String?, username: String): Color {
    val text = (name ?: username).lowercase()
    val colors = listOf(
        Color(0xFFFFC1C1), // Light pink
        Color(0xFFB3E5FC), // Light blue
        Color(0xFFFFF59D), // Light yellow
        Color(0xFFC5E1A5), // Light green
        Color(0xFFFFCCBC), // Light orange
        Color(0xFFE1BEE7), // Light purple
        Color(0xFFB2DFDB), // Light teal
        Color(0xFFFFE0B2), // Light amber
    )
    val index = text.hashCode().absoluteValue % colors.size
    return colors[index]
}

@Composable
private fun AvatarWithInitials(
    name: String?,
    username: String,
    size: androidx.compose.ui.unit.Dp = 44.dp,
    modifier: Modifier = Modifier
) {
    val initials = remember(name, username) { getInitials(name, username) }
    val backgroundColor = remember(name, username) { getAvatarColor(name, username) }
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = initials,
            style = AppTextStyle.Body,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun LeaderboardItem(
    rank: Int,
    username: String,
    name: String?,
    duration: String,
    isCurrentUser: Boolean,
    rankPosition: Int
) {
    val colors = LocalAppColors.current ?: return

    val rankColor = when (rankPosition) {
        1 -> colors.rankGold
        2 -> colors.rankSilver
        3 -> colors.rankBronze
        else -> colors.card
    }

    val cardBackground = if (isCurrentUser) {
        colors.success.copy(alpha = 0.1f)
    } else if (rankPosition <= 3) {
        rankColor.copy(alpha = 0.08f)
    } else {
        colors.card
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardBackground, shape = MaterialTheme.shapes.medium)
            .then(
                if (isCurrentUser || rankPosition <= 3) {
                    Modifier.border(
                        width = 1.5.dp,
                        color = if (rankPosition <= 3) rankColor else colors.success,
                        shape = MaterialTheme.shapes.medium
                    )
                } else Modifier
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank number
            AppText(
                text = "$rank",
                style = AppTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9CA3AF), // Gray color for rank
                modifier = Modifier.width(28.dp)
            )

            // Avatar with initials
            AvatarWithInitials(
                name = name,
                username = username,
                size = 44.dp
            )

            // Username and duration
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = username,
                        style = AppTextStyle.Body,
                        fontWeight = if (isCurrentUser || rankPosition <= 3) FontWeight.Bold else FontWeight.Medium,
                        color = colors.textPrimary
                    )
                    if (isCurrentUser) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = colors.success.copy(alpha = 0.2f),
                                    shape = MaterialTheme.shapes.extraSmall
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
            }

            // Duration
            AppText(
                text = duration,
                style = AppTextStyle.Label,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280) // Gray color for time
            )
        }
    }
}

@Composable
private fun UserProfileCard(
    username: String,
    rank: Int,
    duration: String,
    isDaily: Boolean
) {
    val colors = LocalAppColors.current ?: return

    // Blue colors
    val primaryBlue = colors.success

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = MaterialTheme.shapes.large,
                spotColor = primaryBlue.copy(alpha = 0.4f)
            )
    ) {
        // Main blue card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = primaryBlue,
                    shape = MaterialTheme.shapes.large
                )
                .padding(20.dp)
        ) {
            Column {
                // Username
                AppText(
                    text = username,
                    style = AppTextStyle.Title,
                    fontWeight = FontWeight.Bold,
                    color = colors.textOnPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Statistics section - 2x2 grid
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // First row: Rank and ScreenTime
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Rank
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            AppText(
                                text = "Rank",
                                style = AppTextStyle.Label,
                                color = colors.textOnPrimary.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AppText(
                                text = "$rank",
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold,
                                color = colors.textOnPrimary
                            )
                        }

                        // ScreenTime
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            AppText(
                                text = "ScreenTime",
                                style = AppTextStyle.Label,
                                color = colors.textOnPrimary.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AppText(
                                text = duration,
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold,
                                color = colors.textOnPrimary
                            )
                        }
                    }

                    // Second row: Focus Time and Activity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Focus Time
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            AppText(
                                text = "Focus Time",
                                style = AppTextStyle.Label,
                                color = colors.textOnPrimary.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AppText(
                                text = duration,
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold,
                                color = colors.textOnPrimary
                            )
                        }

                        // Activity
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            AppText(
                                text = "Activity",
                                style = AppTextStyle.Label,
                                color = colors.textOnPrimary.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AppText(
                                text = "${rank * 2} Days",
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold,
                                color = colors.textOnPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

