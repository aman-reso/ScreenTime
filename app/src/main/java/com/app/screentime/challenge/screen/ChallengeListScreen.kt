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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Outbound
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

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


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Title: Explore Challenges
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            AppText(
                text = "Explore Challenges",
                style = AppTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }
        
        // Custom Segmented Control
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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

// Helper data class for card size configuration
private data class CardSizeConfig(
    val span: Int, // Grid span (1 or 2)
    val height: Int, // Height in dp
    val pattern: Int // Pattern type (0 = overlay, 1 = gradient, 2 = horizontal)
)

// Function to determine card size based on index for varied grid pattern
private fun getCardSizeConfig(index: Int): CardSizeConfig {
    // Create a repeating pattern for varied sizes
    // Mix of overlay, gradient, and horizontal designs
    val patternIndex = index % 9
    return when (patternIndex) {
        0 -> CardSizeConfig(span = 2, height = 420, pattern = 0) // Wide overlay
        1 -> CardSizeConfig(span = 1, height = 420, pattern = 1) // Narrow gradient
        2 -> CardSizeConfig(span = 1, height = 420, pattern = 0) // Tall overlay
        3 -> CardSizeConfig(span = 2, height = 180, pattern = 2) // Horizontal design
        4 -> CardSizeConfig(span = 2, height = 420, pattern = 1) // Wide gradient
        5 -> CardSizeConfig(span = 1, height = 420, pattern = 1) // Medium gradient
        6 -> CardSizeConfig(span = 2, height = 180, pattern = 2) // Horizontal design
        7 -> CardSizeConfig(span = 1, height = 420, pattern = 0) // Tall overlay
        8 -> CardSizeConfig(span = 1, height = 420, pattern = 1) // Medium gradient
        else -> CardSizeConfig(span = 1, height = 420, pattern = 0)
    }
}

@Composable
private fun ChallengesTab(
    uiState: com.app.screentime.challenge.viewmodel.ChallengesUiState,
    navController: NavController?,
    viewModel: ChallengeViewModel,
) {
    val colors = LocalAppColors.current ?: return
    val mockData = com.app.screentime.challenge.viewmodel.MockChallengeData

    // Use mock data for now
    val featuredChallenge = mockData.getFeaturedChallenge()
    val trendingChallenges = mockData.getTrendingChallenges()
    val specialEvents = mockData.getSpecialEvents()
    val quickJoinChallenges = mockData.getQuickJoinChallenges()

    var selectedFilter by remember { mutableStateOf(0) }
    val filters = listOf("All", "Fitness", "Mindfulness", "Coding")

        LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Filter buttons
        item {
            Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
                filters.forEachIndexed { index, filter ->
                    FilterChip(
                        text = filter,
                        selected = selectedFilter == index,
                        onClick = { selectedFilter = index }
                    )
                }
            }
        }

        // Featured Challenge Section
        if (featuredChallenge != null) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    AppText(
                        text = "Featured Challenge",
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    FeaturedChallengeCard(
                        challenge = featuredChallenge,
                        onJoin = {
                            navController?.navigate(
                                Screen.ChallengeDetail.createRoute(featuredChallenge.id.toString())
                            )
                        },
                        onViewDetails = {
                            navController?.navigate(
                                Screen.ChallengeDetail.createRoute(featuredChallenge.id.toString())
                            )
                        }
                    )
                }
            }
        }

        // Trending Now Section
        if (trendingChallenges.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = "Trending Now",
                            style = AppTextStyle.SubTitle,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        TextButton(onClick = { /* See all */ }) {
                            AppText(
                                text = "See all",
                                style = AppTextStyle.Label,
                                color = colors.accent
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        trendingChallenges.forEach { challenge ->
                            TrendingChallengeCard(
                                challenge = challenge,
                                modifier = Modifier.weight(1f),
                                onJoin = {
                                    navController?.navigate(
                                        Screen.ChallengeDetail.createRoute(challenge.id.toString())
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // Special Events Section
        if (specialEvents.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    AppText(
                        text = "Special Events",
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    specialEvents.forEach { challenge ->
                        SpecialEventCard(
                            challenge = challenge,
                            modifier = Modifier.fillMaxWidth(),
                            onView = {
                                navController?.navigate(
                                    Screen.ChallengeDetail.createRoute(challenge.id.toString())
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        // Quick Join Section
        if (quickJoinChallenges.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    AppText(
                        text = "Quick Join",
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    quickJoinChallenges.forEach { challenge ->
                        QuickJoinCard(
                            challenge = challenge,
                            modifier = Modifier.fillMaxWidth(),
                            onJoin = {
                                navController?.navigate(
                                    Screen.ChallengeDetail.createRoute(challenge.id.toString())
                                )
            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

// Filter Chip Component
@Composable
private fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) colors.accent
                else colors.card
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        AppText(
            text = text,
            style = AppTextStyle.Label,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) colors.textOnPrimary else colors.textPrimary
        )
    }
}

// Featured Challenge Card
@Composable
private fun FeaturedChallengeCard(
    challenge: Challenge,
    onJoin: () -> Unit,
    onViewDetails: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                if (challenge.thumbnail != null) {
                    AsyncImage(
                        model = challenge.thumbnail,
                        contentDescription = challenge.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Trophy badge overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        AppText(
                            text = challenge.reward,
                            style = AppTextStyle.Label,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.card)
                    .padding(16.dp)
            ) {
                // Date and Participants row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        AppText(
                            text = "$startDate - $endDate",
                            style = AppTextStyle.Caption,
                            color = colors.textSecondary
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        AppText(
                            text = "1.2k joined",
                            style = AppTextStyle.Caption,
                            color = colors.textSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                AppText(
                    text = challenge.description,
                    style = AppTextStyle.Body,
                    color = colors.textSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Join Button
                AppPrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Join Challenge",
                    onClick = onJoin
                )
            }
        }
    }
}

// Trending Challenge Card
@Composable
private fun TrendingChallengeCard(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(colors.card)
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                if (challenge.thumbnail != null) {
                    AsyncImage(
                        model = challenge.thumbnail,
                        contentDescription = challenge.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Tag overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    AppText(
                        text = "7 Days Left",
                        style = AppTextStyle.Caption,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                AppText(
                    text = challenge.description,
                    style = AppTextStyle.Caption,
                    color = colors.textSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Points
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.accent.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(14.dp)
                            )
                            AppText(
                                text = challenge.reward,
                                style = AppTextStyle.Caption,
                                fontWeight = FontWeight.Bold,
                                color = colors.accent
                            )
                        }
                    }

                    // Plus button
                    IconButton(
                        onClick = onJoin,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(colors.textPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Join",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// Special Event Card
@Composable
private fun SpecialEventCard(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onView: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF424242),
                        Color(0xFF616161)
                    )
                )
            )
            .clickable(onClick = onView)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Limited Time tag
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                AppText(
                    text = "Limited Time",
                    style = AppTextStyle.Caption,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column {
                // Title
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                AppText(
                    text = challenge.description,
                    style = AppTextStyle.Body,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Points and View button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                        AppText(
                            text = challenge.reward,
                            style = AppTextStyle.Label,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // View button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF757575))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        AppText(
                            text = "View",
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

// Quick Join Card
@Composable
private fun QuickJoinCard(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.card)
            .border(1.dp, colors.border.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon and content
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                val iconColor = if (challenge.id == 5) Color(0xFF2196F3) else Color(0xFF9C27B0)
                val iconBg = if (challenge.id == 5) Color(0xFFE3F2FD) else Color(0xFFF3E5F5)
                val icon = if (challenge.id == 5) Icons.Default.WaterDrop else Icons.Outlined.DarkMode

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Title and description
                Column {
                    AppText(
                        text = challenge.title,
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AppText(
                        text = challenge.description,
                        style = AppTextStyle.Caption,
                        color = colors.textSecondary
                    )
                }
            }

            // Points and Join button
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppText(
                    text = "+${challenge.reward}",
                    style = AppTextStyle.Label,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )

                OutlinedButton(
                    onClick = onJoin,
                    modifier = Modifier.height(32.dp)
                ) {
                    AppText(
                        text = "Join",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }
            }
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
            when {
                uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                        AppLoader(color = colors.success)
                    }
                }

                uiState.error != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        .weight(1f)
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                        ) {
                            AppText(
                                text = uiState.error ?: "Unable to load challenges.",
                                style = AppTextStyle.Body,
                                color = colors.error
                            )
                    Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = viewModel::refresh) {
                                AppText(
                            text = "Retry", style = AppTextStyle.Label, color = colors.success
                                )
                            }
                        }
                    }

            joinedChallenges.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = colors.textMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                        AppText(
                        text = "No joined challenges",
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AppText(
                        text = "Join challenges from the Challenges tab to see them here.",
                        style = AppTextStyle.Body,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        joinedChallenges,
                        key = { _, challenge -> challenge.id },
                        span = { index, _ ->
                            val config = getCardSizeConfig(index)
                            GridItemSpan(config.span)
                        }
                    ) { index, challenge ->
                        val config = getCardSizeConfig(index)
                        CurrentChallengeCard(
                            challenge = challenge,
                            index = index,
                            cardHeight = config.height.dp,
                            useOverlayDesign = config.pattern == 0,
                            useHorizontalDesign = config.pattern == 2,
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
                            shape = MaterialTheme.shapes.medium
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
            .clip(MaterialTheme.shapes.extraLarge)
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
                        .clip(MaterialTheme.shapes.large)
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
            .clip(MaterialTheme.shapes.small)
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
    participantCount: Int = 0,
    cardHeight: androidx.compose.ui.unit.Dp = 420.dp,
    useOverlayDesign: Boolean = true,
    useHorizontalDesign: Boolean = false,
    isJoining: Boolean = false,
    onViewDetails: () -> Unit,
    onJoin: () -> Unit
) {
    when {
        useHorizontalDesign -> {
            CurrentChallengeCardHorizontal(
                challenge = challenge,
                cardHeight = cardHeight,
                isJoining = isJoining,
                onViewDetails = onViewDetails,
                onJoin = onJoin
            )
        }

        useOverlayDesign -> {
            CurrentChallengeCardOverlay(
                challenge = challenge,
                participantCount = participantCount,
                cardHeight = cardHeight,
                isJoining = isJoining,
                onViewDetails = onViewDetails,
                onJoin = onJoin
            )
        }

        else -> {
            CurrentChallengeCardGradient(
                challenge = challenge,
                participantCount = participantCount,
                cardHeight = cardHeight,
                isJoining = isJoining,
                onViewDetails = onViewDetails,
                onJoin = onJoin
            )
        }
    }
}

@Composable
private fun CurrentChallengeCardHorizontal(
    challenge: Challenge,
    cardHeight: androidx.compose.ui.unit.Dp = 180.dp,
    isJoining: Boolean = false,
    onViewDetails: () -> Unit,
    onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val hasJoined = challenge.hasJoined

    // Format dates
    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)

    // Button gradient (blue to green)
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF4A90E2), // Medium blue
            Color(0xFF50C878)  // Medium green
        )
    )

    // Interactive states
    val cardInteractionSource = remember { MutableInteractionSource() }
    val isCardPressed by cardInteractionSource.collectIsPressedAsState()

    val cardScale by animateFloatAsState(
        targetValue = if (isCardPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "cardScale"
    )

    Box(
            modifier = Modifier
                .fillMaxWidth()
            .height(cardHeight)
            .scale(cardScale)
            .shadow(
                elevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFFF5F5F5)) // Light grey background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Thumbnail Image (Left Side)
            Box(
                modifier = Modifier
                    .width(cardHeight - 24.dp) // Square thumbnail
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.medium)
                    .background(colors.border.copy(alpha = 0.1f))
        ) {
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
            }

            // Content (Right Side)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Section: Title and Description
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
            AppText(
                text = challenge.title,
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

                    // Description
            AppText(
                text = challenge.description,
                        style = AppTextStyle.Body,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )

                    // Rewards Badge (Light green pill)
                    if (challenge.reward.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .background(Color(0xFFE8F5E9)) // Light green background
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50), // Green
                                    modifier = Modifier.size(14.dp)
                                )
                                AppText(
                                    text = challenge.reward,
                style = AppTextStyle.Label,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF2E7D32) // Dark green text
                                )
                            }
                        }
                    }

                    // Dates Section
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF757575), // Grey
                            modifier = Modifier.size(14.dp)
                        )
                        AppText(
                            text = "$startDate",
                            style = AppTextStyle.Caption,
                            color = Color(0xFF757575) // Grey
                        )
                        AppText(
                            text = "•",
                            style = AppTextStyle.Caption,
                            color = Color(0xFF757575) // Grey
                        )
                        AppText(
                            text = "$endDate",
                            style = AppTextStyle.Caption,
                            color = Color(0xFF757575) // Grey
                        )
                    }
                }

                // Bottom Section: Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Join Challenge Button (Gradient blue-green)
                    if (!hasJoined) {
                        val buttonInteractionSource = remember { MutableInteractionSource() }
                        val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()

                        val buttonScale by animateFloatAsState(
                            targetValue = if (isButtonPressed) 0.95f else 1f,
                            animationSpec = tween(durationMillis = 150),
                            label = "buttonScale"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .scale(buttonScale)
                                .clip(MaterialTheme.shapes.medium)
                                .background(buttonGradient)
                                .clickable(
                                    interactionSource = buttonInteractionSource,
                                    enabled = !isJoining,
                                    onClick = onJoin
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AppText(
                                text = "Join Challenge",
                            style = AppTextStyle.Label,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                        )
                        }
                    } else {
                        // Joined state - show gradient button
                        val buttonInteractionSource = remember { MutableInteractionSource() }
                        val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()

                        val buttonScale by animateFloatAsState(
                            targetValue = if (isButtonPressed) 0.95f else 1f,
                            animationSpec = tween(durationMillis = 150),
                            label = "buttonScale"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .scale(buttonScale)
                                .clip(MaterialTheme.shapes.medium)
                                .background(buttonGradient)
                                .clickable(
                                    interactionSource = buttonInteractionSource,
                                    onClick = onViewDetails
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AppText(
                                text = "View Details",
                                style = AppTextStyle.Label,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Details Button (White with border)
                    val detailsButtonInteractionSource = remember { MutableInteractionSource() }
                    val isDetailsButtonPressed by detailsButtonInteractionSource.collectIsPressedAsState()

                    val detailsButtonScale by animateFloatAsState(
                        targetValue = if (isDetailsButtonPressed) 0.95f else 1f,
                        animationSpec = tween(durationMillis = 150),
                        label = "detailsButtonScale"
                    )

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp)
                            .scale(detailsButtonScale)
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE0E0E0), MaterialTheme.shapes.medium)
                            .clickable(
                                interactionSource = detailsButtonInteractionSource,
                                onClick = onViewDetails
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                        Icon(
                                imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                        )
                        AppText(
                                text = "Details",
                            style = AppTextStyle.Label,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentChallengeCardOverlay(
    challenge: Challenge,
    participantCount: Int = 0,
    cardHeight: androidx.compose.ui.unit.Dp = 420.dp,
    isJoining: Boolean = false,
    onViewDetails: () -> Unit,
    onJoin: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val hasJoined = challenge.hasJoined

    // Format dates
    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)

    // Button gradient
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(
            colors.tint,
            colors.accent
        )
    )

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
            .height(cardHeight)
            .scale(cardScale)
            .shadow(
                elevation = cardElevation.dp,
                shape = MaterialTheme.shapes.medium,
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .clip(MaterialTheme.shapes.medium)
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

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section - Badge
            Box {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), MaterialTheme.shapes.large)
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title
                AppText(
                    text = challenge.title,
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
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
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
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
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
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
                            .clip(MaterialTheme.shapes.medium)
                            .background(buttonGradient)
                            //.shadow(4.dp, MaterialTheme.shapes.medium)
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
                            .clip(MaterialTheme.shapes.medium)
                            .background(colors.success)
                            //.shadow(4.dp, MaterialTheme.shapes.medium)
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
    challenge: Challenge,
    participantCount: Int = 0,
    cardHeight: androidx.compose.ui.unit.Dp = 420.dp,
    isJoining: Boolean = false,
    onViewDetails: () -> Unit,
    onJoin: () -> Unit
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
            .height(cardHeight)
            .scale(cardScale)
            .shadow(
                elevation = cardElevation.dp,
                shape = MaterialTheme.shapes.large,
                spotColor = colors.tint.copy(alpha = 0.2f)
            )
            .clip(MaterialTheme.shapes.large)
            .background(cardGradient)
            .border(1.dp, colors.border.copy(alpha = 0.3f), MaterialTheme.shapes.large)
            .clickable(
                interactionSource = cardInteractionSource,
                onClick = onViewDetails
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // Thumbnail Section with Overlay - proportional to card height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight * 0.45f) // ~45% of card height to leave more space for content
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
                                .clip(MaterialTheme.shapes.large)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            colors.success,
                                            colors.success.copy(alpha = 0.8f)
                                        )
                                    )
                                )
                                .shadow(4.dp, MaterialTheme.shapes.large)
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
                                .clip(MaterialTheme.shapes.large)
                                .background(Color(0xFFFFF3CD).copy(alpha = 0.95f))
                                .border(1.dp, Color(0xFFFFE082), MaterialTheme.shapes.large)
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

                // Participants Count (top-right) - Show when count > 0
                if (participantCount > 0) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .background(Color.White.copy(alpha = 0.9f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    tint = colors.tint,
                                    modifier = Modifier.size(14.dp)
                                )
                                AppText(
                                    text = participantCount.toString(),
                                    style = AppTextStyle.Caption,
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
                                .clip(MaterialTheme.shapes.large)
                                .background(rewardGradient)
                                .shadow(4.dp, MaterialTheme.shapes.large)
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

            // Content Section - Use weight to take remaining space
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
            AppText(
                text = challenge.title,
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

                    // Description
            AppText(
                text = challenge.description,
                        style = AppTextStyle.Body,
                        color = colors.textSecondary,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )

                    // Date & Duration Info
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(MaterialTheme.shapes.small)
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
                                color = colors.textPrimary,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
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
                            .clip(MaterialTheme.shapes.medium)
                            .background(buttonGradient)
                            //.shadow(4.dp, MaterialTheme.shapes.medium)
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

private val previewJoinedChallengesUiState =
    com.app.screentime.challenge.viewmodel.ChallengesUiState(
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
                useHorizontalDesign = false,
                onViewDetails = {},
                onJoin = {})
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
                        useHorizontalDesign = true,
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