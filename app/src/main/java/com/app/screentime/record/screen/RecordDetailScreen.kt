package com.app.screentime.record.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.record.component.DateSpinner
import com.app.screentime.record.component.SummaryTab
import com.app.screentime.record.component.TimelineTab
import com.app.screentime.record.viewmodel.RecordDetailViewModel
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.SegmentedControl
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.utils.DateUtils
import kotlinx.coroutines.launch

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

    // Track selected date for display
    val today = com.app.screentime.utils.DateUtils.today()
    var selectedDateDisplay by remember { mutableStateOf(today.toString("d MMM yyyy")) }

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
                        val localDate = org.joda.time.LocalDate.parse(
                            selectedDate,
                            org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd")
                        )
                        selectedDateDisplay = localDate.toString("d MMM yyyy")
                        viewModel.getDailyUsageStats(targetUserId = username, date = selectedDate)
                    }
                )
            }
        }

        // Segmented Control
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
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
                        navController = navController,
                        selectedDateDisplay = selectedDateDisplay
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

