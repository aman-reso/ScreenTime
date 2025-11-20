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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.leaderboard.viewmodel.LeaderboardViewModel
import com.app.screentime.navigation.Screen
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
                                text = "Leaderboard",
                                style = AppTextStyle.Title,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        AppText(
                            text = "Compete with others",
                            style = AppTextStyle.Label,
                            color = colors.textSecondary
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

            if (navController != null) {
                Spacer(modifier = Modifier.height(8.dp))
                FilledTonalButton(
                    onClick = { navController.navigate(Screen.Challenges.route) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Challenges",
                        tint = colors.success
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    AppText(
                        text = "View Challenges",
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.success
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Tabs
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = colors.background,
                contentColor = colors.textPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = colors.success,
                        height = 2.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            AppText(
                                text = title,
                                style = AppTextStyle.Body,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (pagerState.currentPage == index) colors.success else colors.textMuted
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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

@Composable
private fun LeaderboardItem(
    rank: Int,
    username: String,
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
            .background(color = cardBackground, shape = RoundedCornerShape(12.dp))
            .then(
                if (isCurrentUser || rankPosition <= 3) {
                    Modifier.border(
                        width = 1.5.dp,
                        color = if (rankPosition <= 3) rankColor else colors.success,
                        shape = RoundedCornerShape(12.dp)
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
            // Rank Badge with enhanced design
            Box(
                modifier = Modifier
                    .size(if (rankPosition <= 3) 52.dp else 44.dp)
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
                        modifier = Modifier.size(if (rankPosition == 1) 28.dp else 24.dp)
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
                        text = duration,
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Medium,
                        color = colors.textSecondary
                    )
                }
            }

            // Trophy icon for top 3
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
                shape = RoundedCornerShape(20.dp),
                spotColor = primaryBlue.copy(alpha = 0.4f)
            )
    ) {
        // Main blue card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = primaryBlue,
                    shape = RoundedCornerShape(20.dp)
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

