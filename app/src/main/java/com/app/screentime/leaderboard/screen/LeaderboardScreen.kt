//package com.app.screentime.leaderboard.screen
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.ui.draw.clip
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.*
//import com.app.screentime.ui.atom.SegmentedControl
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.app.screentime.leaderboard.viewmodel.LeaderboardViewModel
//import com.app.screentime.network.model.LeaderboardEntry
//import com.app.screentime.record.repository.formatDuration
//import com.telekom.odsystem.atoms.ODSText
//import com.telekom.odsystem.atoms.ODSTextStyle
//
//import kotlinx.coroutines.launch
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.res.stringResource
//import com.app.screentime.R
//import com.app.screentime.ui.atom.AppLoader
//import com.telekom.odsystem.tokens.tokens.ODSTheme
//import kotlin.math.absoluteValue
//import com.telekom.odsystem.foundations.HexColor
//import com.telekom.odsystem.DSTextStyles
//import com.telekom.odsystem.atoms.ODSBox
//import com.telekom.odsystem.atoms.ODSColumn
//import com.telekom.odsystem.atoms.ODSRow
//import com.telekom.odsystem.atoms.button.ODSButton
//import com.telekom.odsystem.atoms.button.ODSButtonProps
//import com.telekom.odsystem.atoms.button.ODSButtonVariant
//import com.telekom.odsystem.atoms.icon.ODSIcon
//import com.telekom.odsystem.atoms.icon.ODSIconModel
//import com.telekom.odsystem.foundations.ODSColorModel
//import com.telekom.odsystem.foundations.ODSPadding
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LeaderboardScreen(
//    modifier: Modifier = Modifier,
//    navController: NavController? = null,
//    viewModel: LeaderboardViewModel = hiltViewModel(),
//    scheme: ODSTheme = neutralScheme
//) {
//
//    val uiState by viewModel.uiState.collectAsState()
//
//    val tabs = listOf("Daily", "Weekly")
//    val pagerState = rememberPagerState(pageCount = { tabs.size }, initialPage = 0)
//    val coroutineScope = rememberCoroutineScope()
//
//    ODSBox(
//        modifier = modifier.fillMaxSize(),
//        background = listOf(ODSColorModel(scheme.basicBackground))
//    ) {
//        ODSColumn(
//            modifier = Modifier
//                .fillMaxSize(),
//            padding = ODSPadding(horizontal = 8.dp)
//        ) {
//            // Header
//            ODSBox(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight(),
//                padding = ODSPadding(vertical = 8.dp)
//            ) {
//                ODSRow(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    if (navController != null) {
//                        ODSButton(
//                            scheme = scheme,
//                            props = ODSButtonProps(
//                                buttonIcon = ODSIconModel(
//                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
//                                    tint = scheme.basicText,
//                                    contentDescription = "Back"
//                                ),
//                                buttonType = ODSButtonButtonType.ICON_ONLY,
//                                variant = ODSButtonVariant.GHOST,
//                                size = com.telekom.odsystem.atoms.button.ODSButtonSize.SMALL
//                            ),
//                            onClick = { navController.popBackStack() }
//                        )
//                    }
//                    ODSColumn(modifier = Modifier.weight(1f)) {
//                        ODSText(
//                            text = "Leaderboard",
//                            style = DSTextStyles.titleS,
//                            color = scheme.basicText
//                        )
//                    }
//                    ODSButton(
//                        scheme = scheme,
//                        props = ODSButtonProps(
//                            buttonIcon = ODSIconModel(
//                                imageVector = Icons.Default.Refresh,
//                                tint = scheme.functionalSuccessStandard,
//                                contentDescription = stringResource(R.string.refresh)
//                            ),
//                            buttonType = ODSButtonButtonType.ICON_ONLY,
//                            variant = ODSButtonVariant.GHOST,
//                            size = com.telekom.odsystem.atoms.button.ODSButtonSize.SMALL
//                        ),
//                        onClick = { viewModel.refresh() }
//                    )
//                }
//            }
//
//            // Segmented Control
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            ) {
//                SegmentedControl(
//                    items = tabs,
//                    selectedIndex = pagerState.currentPage,
//                    onItemSelected = { index ->
//                        coroutineScope.launch {
//                            pagerState.animateScrollToPage(index)
//                        }
//                    }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Content
//            HorizontalPager(
//                pageSpacing = 12.dp,
//                state = pagerState,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) { page ->
//                when (page) {
//                    0 -> DailyLeaderboardContent(
//                        entries = uiState.dailyEntries,
//                        userRank = uiState.userDailyRank,
//                        userDuration = uiState.userDailyDuration,
//                        currentUsername = uiState.currentUsername,
//                        isLoading = uiState.isLoading,
//                        error = uiState.error
//                    )
//
//                    1 -> WeeklyLeaderboardContent(
//                        entries = uiState.weeklyEntries,
//                        userRank = uiState.userWeeklyRank,
//                        userDuration = uiState.userWeeklyDuration,
//                        currentUsername = uiState.currentUsername,
//                        isLoading = uiState.isLoading,
//                        error = uiState.error
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun DailyLeaderboardContent(
//    entries: List<LeaderboardEntry>,
//    userRank: Int?,
//    userDuration: Long?,
//    currentUsername: String?,
//    isLoading: Boolean,
//    error: String?
//) {
//    LeaderboardContent(
//        title = "Daily Leaderboard",
//        entries = entries,
//        userRank = userRank,
//        userDuration = userDuration,
//        currentUsername = currentUsername,
//        isLoading = isLoading,
//        error = error
//    )
//}
//
//@Composable
//private fun WeeklyLeaderboardContent(
//    entries: List<LeaderboardEntry>,
//    userRank: Int?,
//    userDuration: Long?,
//    currentUsername: String?,
//    isLoading: Boolean,
//    error: String?
//) {
//    LeaderboardContent(
//        title = "Weekly Leaderboard",
//        entries = entries,
//        userRank = userRank,
//        userDuration = userDuration,
//        currentUsername = currentUsername,
//        isLoading = isLoading,
//        error = error
//    )
//}
//
//@Composable
//private fun LeaderboardContent(
//    title: String,
//    entries: List<LeaderboardEntry>,
//    userRank: Int?,
//    userDuration: Long?,
//    currentUsername: String?,
//    isLoading: Boolean,
//    error: String?
//) {
//
//
//    if (isLoading) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            AppLoader(color = HexColor(colors.success.value))
//        }
//        return
//    }
//
//    if (error != null) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Error,
//                    contentDescription = null,
//                    tint = colors.error,
//                    modifier = Modifier.size(48.dp)
//                )
//                ODSText(
//                    text = error,
//                    style = DSTextStyles.bodyMRegular,
//                    color = HexColor(colors.error.value)
//                )
//            }
//        }
//        return
//    }
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // User Profile Card
//        if (userRank != null && userDuration != null && currentUsername != null) {
//            item {
//                UserProfileCard(
//                    username = currentUsername,
//                    rank = userRank,
//                    duration = entries.find { it.username == currentUsername }?.totalScreenTime?.let {
//                        formatDuration(
//                            it
//                        )
//                    }
//                        ?: formatDuration(userDuration),
//                    isDaily = title.contains("Daily", ignoreCase = true)
//                )
//            }
//        }
//
//        // Top 10 List Header
//        item {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Star,
//                        contentDescription = null,
//                        tint = colors.success,
//                        modifier = Modifier.size(20.dp)
//                    )
//                    ODSText(
//                        text = "Top 10",
//                        style = DSTextStyles.subtitle,
//                        color = HexColor(colors.textPrimary.value)
//                    )
//                }
//                ODSText(
//                    text = "${entries.size} players",
//                    style = DSTextStyles.bodyMBold,
//                    color = HexColor(colors.textSecondary.value)
//                )
//            }
//        }
//
//        itemsIndexed(entries.take(10)) { index, entry ->
//            LeaderboardItem(
//                rank = entry.rank,
//                username = entry.username,
//                name = entry.name,
//                duration = formatDuration(entry.totalScreenTime),
//                isCurrentUser = entry.username == currentUsername,
//                rankPosition = index + 1
//            )
//        }
//
//        if (entries.isEmpty()) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(32.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Leaderboard,
//                            contentDescription = null,
//                            tint = colors.textMuted,
//                            modifier = Modifier.size(64.dp)
//                        )
//                        ODSText(
//                            text = stringResource(R.string.no_leaderboard_data_available),
//                            style = DSTextStyles.bodyMRegular,
//                            color = HexColor(colors.textMuted.value)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
///**
// * Get initials from a name or username
// */
//private fun getInitials(name: String?, username: String): String {
//    val text = name?.trim() ?: username.trim()
//    if (text.isEmpty()) return "?"
//
//    val parts = text.split(" ", ".", "_", "-").filter { it.isNotEmpty() }
//    return when {
//        parts.size >= 2 -> "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
//        text.length >= 2 -> text.take(2).uppercase()
//        else -> text.first().uppercaseChar().toString()
//    }
//}
//
///**
// * Get a color for an avatar based on the name/username
// */
//private fun getAvatarColor(name: String?, username: String): Color {
//    val text = (name ?: username).lowercase()
//    val colors = listOf(
//        Color(0xFFFFC1C1), // Light pink
//        Color(0xFFB3E5FC), // Light blue
//        Color(0xFFFFF59D), // Light yellow
//        Color(0xFFC5E1A5), // Light green
//        Color(0xFFFFCCBC), // Light orange
//        Color(0xFFE1BEE7), // Light purple
//        Color(0xFFB2DFDB), // Light teal
//        Color(0xFFFFE0B2), // Light amber
//    )
//    val index = text.hashCode().absoluteValue % colors.size
//    return colors[index]
//}
//
//@Composable
//private fun AvatarWithInitials(
//    name: String?,
//    username: String,
//    size: androidx.compose.ui.unit.Dp = 44.dp,
//    modifier: Modifier = Modifier
//) {
//    val initials = remember(name, username) { getInitials(name, username) }
//    val backgroundColor = remember(name, username) { getAvatarColor(name, username) }
//
//
//    Box(
//        modifier = modifier
//            .size(size)
//            .clip(CircleShape)
//            .background(backgroundColor),
//        contentAlignment = Alignment.Center
//    ) {
//        ODSText(
//            text = initials,
//            style = DSTextStyles.bodyMRegular,
//            color = HexColor(colors.textPrimary.value)
//        )
//    }
//}
//
//@Composable
//private fun LeaderboardItem(
//    rank: Int,
//    username: String,
//    name: String?,
//    duration: String,
//    isCurrentUser: Boolean,
//    rankPosition: Int
//) {
//
//
//    val rankColor = when (rankPosition) {
//        1 -> colors.rankGold
//        2 -> colors.rankSilver
//        3 -> colors.rankBronze
//        else -> colors.card
//    }
//
//    val cardBackground = if (isCurrentUser) {
//        colors.success.copy(alpha = 0.1f)
//    } else if (rankPosition <= 3) {
//        rankColor.copy(alpha = 0.08f)
//    } else {
//        colors.card
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = HexColor(cardBackground.value), shape = MaterialTheme.shapes.medium)
//            .then(
//                if (isCurrentUser || rankPosition <= 3) {
//                    Modifier.border(
//                        width = 1.5.dp,
//                        color = HexColor(if.value) (rankPosition <= 3) rankColor else colors.success,
//                        shape = MaterialTheme.shapes.medium
//                    )
//                } else Modifier
//            )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 14.dp),
//            horizontalArrangement = Arrangement.spacedBy(14.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Rank number
//            ODSText(
//                text = "$rank",
//                style = DSTextStyles.bodyMRegular,
//                color = HexColor(Color.value)(0xFF9CA3AF), // Gray color for rank
//                modifier = Modifier.width(28.dp)
//            )
//
//            // Avatar with initials
//            AvatarWithInitials(
//                name = name,
//                username = username,
//                size = 44.dp
//            )
//
//            // Username and duration
//            Column(modifier = Modifier.weight(1f)) {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    ODSText(
//                        text = username,
//                        style = DSTextStyles.bodyMRegular) FontWeight.Bold else FontWeight.Medium,
//                        color = HexColor(colors.textPrimary.value)
//                    )
//                    if (isCurrentUser) {
//                        Box(
//                            modifier = Modifier
//                                .background(
//                                    color = HexColor(colors.success.copy.value)(alpha = 0.2f),
//                                    shape = MaterialTheme.shapes.extraSmall
//                                )
//                                .padding(horizontal = 6.dp, vertical = 2.dp)
//                        ) {
//                            ODSText(
//                                text = "You",
//                                style = DSTextStyles.bodyMBold,
//                                color = HexColor(colors.success.value))
//                        }
//                    }
//                }
//            }
//
//            // Duration
//            ODSText(
//                text = duration,
//                style = DSTextStyles.bodyMBold,
//                color = HexColor(Color.value)(0xFF6B7280) // Gray color for time
//            )
//        }
//    }
//}
//
//@Composable
//private fun UserProfileCard(
//    username: String,
//    rank: Int,
//    duration: String,
//    isDaily: Boolean
//) {
//
//
//    // Blue colors
//    val primaryBlue = colors.success
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(
//                elevation = 12.dp,
//                shape = MaterialTheme.shapes.large,
//                spotColor = primaryBlue.copy(alpha = 0.4f)
//            )
//    ) {
//        // Main blue card
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    color = HexColor(primaryBlue.value),
//                    shape = MaterialTheme.shapes.large
//                )
//                .padding(20.dp)
//        ) {
//            Column {
//                // Username
//                ODSText(
//                    text = username,
//                    style = DSTextStyles.titleS,
//                    color = HexColor(colors.textOnPrimary.value)
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Statistics section - 2x2 grid
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    // First row: Rank and ScreenTime
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        // Rank
//                        Column(
//                            horizontalAlignment = Alignment.Start
//                        ) {
//                            ODSText(
//                                text = "Rank",
//                                style = DSTextStyles.bodyMBold,
//                                color = HexColor(colors.textOnPrimary.copy.value)(alpha = 0.7f)
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            ODSText(
//                                text = "$rank",
//                                style = DSTextStyles.subtitle,
//                                color = HexColor(colors.textOnPrimary.value)
//                            )
//                        }
//
//                        // ScreenTime
//                        Column(
//                            horizontalAlignment = Alignment.End
//                        ) {
//                            ODSText(
//                                text = "ScreenTime",
//                                style = DSTextStyles.bodyMBold,
//                                color = HexColor(colors.textOnPrimary.copy.value)(alpha = 0.7f)
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            ODSText(
//                                text = duration,
//                                style = DSTextStyles.subtitle,
//                                color = HexColor(colors.textOnPrimary.value)
//                            )
//                        }
//                    }
//
//                    // Second row: Focus Time and Activity
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        // Focus Time
//                        Column(
//                            horizontalAlignment = Alignment.Start
//                        ) {
//                            ODSText(
//                                text = "Focus Time",
//                                style = DSTextStyles.bodyMBold,
//                                color = HexColor(colors.textOnPrimary.copy.value)(alpha = 0.7f)
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            ODSText(
//                                text = duration,
//                                style = DSTextStyles.subtitle,
//                                color = HexColor(colors.textOnPrimary.value)
//                            )
//                        }
//
//                        // Activity
//                        Column(
//                            horizontalAlignment = Alignment.End
//                        ) {
//                            ODSText(
//                                text = "Activity",
//                                style = DSTextStyles.bodyMBold,
//                                color = HexColor(colors.textOnPrimary.copy.value)(alpha = 0.7f)
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            ODSText(
//                                text = "${rank * 2} Days",
//                                style = DSTextStyles.subtitle,
//                                color = HexColor(colors.textOnPrimary.value)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
