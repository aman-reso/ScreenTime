package com.app.screentime.record.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.landing.component.UsageDonutComponent
import com.app.screentime.network.model.AppUsageStatsData
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.record.viewmodel.RecordDetailUiState
import com.app.screentime.record.viewmodel.RecordDetailViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppSecondaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.AppUsageListUi
import com.app.screentime.ui.theme.AppColors
import com.app.screentime.ui.theme.LocalAppColors
import kotlinx.coroutines.launch
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    username: String,
    viewModel: RecordDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppColors.current ?: return
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf("Summary", "Timeline")
    val pagerState = rememberPagerState(pageCount = { tabs.size }, initialPage = 0)

    LaunchedEffect(Unit) {
        // After TOTP verification, use the new daily stats API
        // Use username as targetUserId (API may accept username or userId)
        viewModel.getDailyUsageStats(targetUserId = username)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back arrow - Left side
            IconButton(
                onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = colors.tint
                )
            }
            AppText(
                text = username,
                style = AppTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            Box(modifier = Modifier.width(200.dp)) {
                DateSpinner(
                    onDateSelected = { selectedDate ->
                        viewModel.getDailyUsageStats(targetUserId = username, date = selectedDate)
                    })
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = colors.background,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = colors.accent,
                    height = 2.dp
                )
            }) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = pagerState.currentPage == index, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, text = {
                    AppText(
                        text = title,
                        style = AppTextStyle.Body,
                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (pagerState.currentPage == index) colors.accent else colors.textMuted
                    )
                })
            }
        }

        // Tab Content with Pager
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> {
                    // Summary Tab
                    SummaryTab(
                        viewModel = viewModel,
                        uiState = uiState,
                        colors = colors,
                        navController = navController
                    )
                }

                1 -> {
                    // Timeline Tab
                    TimelineTab(
                        uiState = uiState, viewModel = viewModel, colors = colors
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryTab(
    viewModel: RecordDetailViewModel,
    uiState: RecordDetailUiState,
    colors: AppColors,
    navController: NavController
) {
    val context = LocalContext.current

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AppLoader(type = AppLoaderType.CIRCULAR)
            }
        }

        uiState.error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = colors.error.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AppText(
                            text = "Error", style = AppTextStyle.SubTitle, color = colors.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AppText(
                            text = uiState.error ?: "",
                            style = AppTextStyle.Body,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AppSecondaryButton(
                            text = "Dismiss", onClick = { viewModel.clearError() })
                    }
                }
            }
        }

        uiState.stats.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AppText(
                    text = "No usage records found",
                    style = AppTextStyle.Body,
                    color = colors.textSecondary
                )
            }
        }

        else -> {
            // Group by package and sum duration (duration is in milliseconds)
            val appUsageMap = remember(uiState.stats, context) {
                uiState.stats.groupBy { it.packageName }.mapValues { (packageName, stats) ->
                    // Sum all durations for this package
                    val totalDurationMs = stats.sumOf { it.duration ?: 0L }
                    val firstStat = stats.first()
                    val appInfo = packageName.let {
                        try {
                            context.packageManager.getApplicationInfo(it, 0)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    AppUsage(
                        packageName = packageName,
                        appName = firstStat.appName,
                        appScreenTime = totalDurationMs, // Use duration sum
                        mobileDataUsage = -1L,
                        wifiDataUsage = -1L
                    ).apply {
                        applicationInfo = appInfo
                        displayFormatScreenTime = formatUsageTime(totalDurationMs)
                    }
                }
            }

            val appUsageList = remember(appUsageMap) {
                appUsageMap.values.sortedByDescending { it.appScreenTime }
            }

            // Total time: sum of all durations
            val totalScreenTime = remember(uiState.stats) {
                uiState.stats.sumOf { it.duration ?: 0L }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                // Usage Donut Component
                if (appUsageList.isNotEmpty()) {
                    item {
                        UsageDonutComponent(
                            report = appUsageList, totalScreenTime = totalScreenTime
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // App Usage List
                val totalCount = appUsageList.size
                itemsIndexed(
                    items = appUsageList,
                    key = { _, appUsage -> appUsage.packageName ?: appUsage.id.toString() },
                    contentType = { _, _ -> "app_usage_item" }) { index, appUsage ->
                    AppUsageListUi(
                        appUsage = appUsage, index = index, totalCount = totalCount, onClick = {
                            appUsage.packageName?.let { packageName ->
                                navController.navigate(
                                    com.app.screentime.navigation.Screen.SingleAppUsageDetail.createRoute(
                                        packageName
                                    )
                                )
                            }
                        })
                }
            }
        }
    }
}

@Composable
private fun TimelineTab(
    uiState: RecordDetailUiState, viewModel: RecordDetailViewModel, colors: AppColors
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AppLoader(type = AppLoaderType.CIRCULAR)
            }
        }

        uiState.error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = colors.error.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AppText(
                            text = "Error", style = AppTextStyle.SubTitle, color = colors.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AppText(
                            text = uiState.error,
                            style = AppTextStyle.Body,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AppSecondaryButton(
                            text = "Dismiss", onClick = { viewModel.clearError() })
                    }
                }
            }
        }

        uiState.stats.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AppText(
                    text = "No usage records found",
                    style = AppTextStyle.Body,
                    color = colors.textSecondary
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                itemsIndexed(
                    items = uiState.timeLines,
                    key = { index, item ->
                        when (item) {
                            is TimelineListItem.HourHeaderItem -> "header-${item.hour}"
                            is TimelineListItem.TimelineEventItem -> "item-${item.stat.packageName}-${item.stat.eventTimestamp}-$index"
                        }
                    }
                ) { listIndex, item ->
                    when (item) {
                        is TimelineListItem.HourHeaderItem -> {
                            if (listIndex > 0) {
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            HourRangeHeader(hour = item.hour, colors = colors)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        is TimelineListItem.TimelineEventItem -> {
                            TimelineItem(
                                stat = item.stat,
                                isFirst = item.isFirst,
                                isLast = item.isLast,
                                colors = colors
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HourRangeHeader(hour: Int, colors: AppColors) {
    val hourRange = remember(hour) {
        val startHour = String.format("%02d:00", hour)
        val endHour = String.format("%02d:00", (hour + 1) % 24)
        "$startHour-$endHour"
    }

    AppText(
        text = hourRange,
        style = AppTextStyle.SubTitle,
        fontWeight = FontWeight.Bold,
        color = colors.textPrimary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}


@Composable
private fun TimelineItem(
    stat: AppUsageStatsData, isFirst: Boolean, isLast: Boolean, colors: AppColors
) {
    val cardShape = remember(isFirst, isLast) {
        when {
            isFirst && isLast -> RoundedCornerShape(12.dp) // Single item: all corners rounded
            isFirst -> RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp
            ) // First: top corners rounded
            isLast -> RoundedCornerShape(
                bottomStart = 12.dp,
                bottomEnd = 12.dp
            ) // Last: bottom corners rounded
            else -> RectangleShape
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(colors.card, cardShape)
    ) {
        val (topLine, indicator, bottomLine, content) = createRefs()

        // ------- Top Line -------
        if (!isFirst) {
            VerticalDivider(
                thickness = 2.dp,
                color = colors.success.copy(alpha = 0.3f),
                modifier = Modifier
                    .height(12.dp)
                    .constrainAs(topLine) {
                        top.linkTo(parent.top)
                        bottom.linkTo(indicator.top)
                        start.linkTo(indicator.start)
                        end.linkTo(indicator.end)
                    })
        }

        // ------- Indicator -------
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(colors.success.copy(alpha = 0.2f))
                .constrainAs(indicator) {
                    top.linkTo(if (isFirst) parent.top else topLine.bottom)
                    start.linkTo(parent.start)
                }, contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = colors.success,
                modifier = Modifier.size(16.dp)
            )
        }

        if (!isLast) {
            VerticalDivider(
                thickness = 2.dp,
                color = colors.success.copy(alpha = 0.3f),
                modifier = Modifier
                    .height(12.dp)
                    .constrainAs(bottomLine) {
                        top.linkTo(indicator.bottom)
                        start.linkTo(indicator.start)
                        end.linkTo(indicator.end)
                    })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
                .constrainAs(content) {
                    start.linkTo(indicator.end, margin = 12.dp)
                    end.linkTo(parent.end)
                    top.linkTo(indicator.top)
                    bottom.linkTo(indicator.bottom)
                    width = Dimension.fillToConstraints
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App name on the left
            AppText(
                text = stat.appName ?: "Unknown App",
                style = AppTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Exact time on the right
            val exactTime = remember(stat.eventTimestamp) {
                stat.eventTimestamp?.let { timestamp ->
                    try {
                        val instant = Instant.parse(timestamp)
                        val zonedDateTime = instant.atZone(java.time.ZoneId.systemDefault())
                        val formatter =
                            java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                        formatter.format(zonedDateTime)
                    } catch (e: Exception) {
                        timestamp // Fallback to original if parsing fails
                    }
                } ?: ""
            }

            AppText(
                text = exactTime,
                style = AppTextStyle.Label,
                color = colors.textSecondary,
                maxLines = 1
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSpinner(
    onDateSelected: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return

    val today = com.app.screentime.utils.DateUtils.today()
    val dates = (0..3).map { today.minusDays(it) }

    val dateDisplayMap = dates.associate {
        it.toString("yyyy-MM-dd") to it.toString("d MMM yyyy")
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(today.toString("yyyy-MM-dd")) }
    var displayDate by remember { mutableStateOf(dateDisplayMap[selectedDate]!!) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Box(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp, color = colors.border, shape = RoundedCornerShape(12.dp)
                )
                .background(colors.card)
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .clickable { expanded = true }   // opens dropdown
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ⭐ Actual Text Field Content
                BasicTextField(
                    value = displayDate, readOnly = true, onValueChange = {}, textStyle = TextStyle(
                        fontSize = 16.sp, color = colors.textPrimary
                    ), modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colors.textSecondary
                )
            }
        }

        // ⭐ Dropdown menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colors.card, RoundedCornerShape(12.dp))
        ) {
            dates.forEach { date ->

                val apiDate = date.toString("yyyy-MM-dd")
                val displayString = dateDisplayMap[apiDate]!!

                DropdownMenuItem(text = {
                    AppText(
                        text = displayString,
                        style = AppTextStyle.Body,
                        color = colors.textPrimary
                    )
                }, onClick = {
                    selectedDate = apiDate
                    displayDate = displayString
                    expanded = false
                    onDateSelected(apiDate)
                })
            }
        }
    }
}


private fun formatUsageTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}
