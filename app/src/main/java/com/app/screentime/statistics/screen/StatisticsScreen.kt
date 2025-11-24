package com.app.screentime.statistics.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.navigation.Screen
import com.app.screentime.record.repository.LocalAppUsageRepository
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.statistics.viewmodel.StatisticsViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.WeeklyUsageChart
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppColors.current ?: return
    val isDarkMode = LocalThemeMode.current

    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                AppLoader(
                    type = AppLoaderType.CIRCULAR
                )
            }
        }

        uiState.error != null -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.error),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error ?: "",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.clearError() }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }
            }
        }

        uiState.weeklyReports.isEmpty() -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_data_available),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        colors.background
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    )
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = "Activity",
                            style = AppTextStyle.Title,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    WeeklyUsageChart(
                        weeklyReports = uiState.weeklyReports,
                        selectedDayIndex = uiState.selectedDayIndex,
                        onBarClick = { dayIndex ->
                            viewModel.selectDay(dayIndex)
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                uiState.selectedDayIndex?.let { dayIndex ->
                    if (dayIndex in uiState.weeklyReports.indices) {
                        val selectedDay = uiState.weeklyReports[dayIndex]
                        item {
                            DayActivitySection(
                                report = selectedDay,
                                isDarkMode = isDarkMode,
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
private fun DayActivitySection(
    report: com.app.screentime.data.uiModel.WeeklyDataReport,
    isDarkMode: Boolean,
    colors: com.app.screentime.ui.theme.AppColors
) {
    // Monday card: Surface Container - different color from "This week" card
    // Surface Container: #f0f4f9 (Light) / #1e2124 (Dark)
    val surfaceColor = if (isDarkMode) Color(0xFF1E2124) else Color(0xFFF0F4F9)
    val appUsageList = report.appUsage?.sortedByDescending { it.appScreenTime } ?: emptyList()

    // Find special entries
    val tetheringUsage = appUsageList.find {
        it.packageName == "com.android.tethering"
    }
    val removedAppsUsage = appUsageList.find {
        it.packageName == "com.android.removed"
    }

    // AppTime is the total screen time (sum of all app screen times)
    val appTimeMinutes = appUsageList
        .filter {
            it.packageName != "com.android.tethering" &&
                    it.packageName != "com.android.removed"
        }
        .sumOf { it.appScreenTime } / (1000 * 60)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(surfaceColor)
            .padding(16.dp)
    ) {
        // Day header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                AppText(
                    text = report.dayName ?: "Unknown",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                AppText(
                    text = report.date ?: "",
                    style = AppTextStyle.Label,
                    color = colors.textMuted
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                AppText(
                    text = report.displayScreenTime ?: "0m",
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                AppText(
                    text = report.displayTotalDataUsage ?: "0 B",
                    style = AppTextStyle.Label,
                    color = colors.textMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Activity items
        val activityItems = remember(appTimeMinutes, tetheringUsage, removedAppsUsage) {
            listOfNotNull(
                ActivityItem(
                    name = "AppTime",
                    icon = Icons.Default.AllInclusive,
                    iconBackground = Color(0xFFE0E7FF),
                    iconColor = Color(0xFF4F46E5),
                    screenTime = appTimeMinutes.toLong(),
                    dataUsage = 0L
                ),
                tetheringUsage?.let {
                    ActivityItem(
                        name = "Tethering",
                        icon = Icons.Default.MyLocation,
                        iconBackground = Color(0xFFDBEAFE),
                        iconColor = Color(0xFF2563EB),
                        screenTime = 0,
                        dataUsage = (it.wifiDataUsage + it.mobileDataUsage)
                    )
                },
                removedAppsUsage?.let {
                    ActivityItem(
                        name = "Removed Apps",
                        icon = Icons.Default.Delete,
                        iconBackground = Color(0xFFFCE7F3),
                        iconColor = Color(0xFFEC4899),
                        screenTime = 0,
                        dataUsage = (it.wifiDataUsage + it.mobileDataUsage)
                    )
                }
            )
        }

        activityItems.forEachIndexed { index, item ->
            ActivityItemCard(
                item = item,
                isFirst = index == 0,
                isLast = index == activityItems.size - 1,
                isDarkMode = isDarkMode
            )
            // Add gap between items
            if (index < activityItems.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private data class ActivityItem(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconBackground: Color,
    val iconColor: Color,
    val screenTime: Long, // in minutes
    val dataUsage: Long // in bytes
)

@Composable
private fun ActivityItemCard(
    item: ActivityItem,
    isFirst: Boolean,
    isLast: Boolean,
    isDarkMode: Boolean
) {
    val colors = LocalAppColors.current ?: return
    // Card background should be the lighter color (iconBackground)
    val cardBackground = item.iconBackground

    // Each item has fully rounded corners since there's spacing between them
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(cardBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Icon - white icon inside a darker colored circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp)) // Circular
                    .background(item.iconColor), // Darker color for the circle
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    tint = Color.White, // White icon
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name and data usage
            Column {
                AppText(
                    text = item.name,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                AppText(
                    text = if (item.dataUsage > 0) {
                        item.dataUsage.toReadableDataSize() ?: "0 B"
                    } else {
                        "0 B"
                    },
                    style = AppTextStyle.Label,
                    color = colors.textMuted
                )
            }
        }

        // Screen time
        AppText(
            text = if (item.screenTime > 0) {
                formatDuration(item.screenTime.toLong() * 1000 * 60)
            } else {
                "-"
            },
            style = AppTextStyle.Body,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
    }
}
