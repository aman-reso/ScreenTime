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
import com.app.screentime.ui.atom.AppUsageListUi
import com.app.screentime.ui.atom.WeeklyUsageChart
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
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
                        val appUsageList =
                            selectedDay.appUsage?.sortedByDescending { it.appScreenTime }
                                ?: emptyList()

                        itemsIndexed(
                            items = appUsageList,
                            key = { _, appUsage -> appUsage.packageName ?: appUsage.id.toString() },
                            contentType = { _, _ -> "app_usage_item" }) { index, appUsage ->
                            AppUsageListUi(
                                appUsage = appUsage,
                                index = index,
                                totalCount = appUsageList.size,
                                onClick = {
                                    appUsage.packageName?.let { packageName ->
                                        navController?.navigate(
                                            Screen.SingleAppUsageDetail.createRoute(packageName)
                                        )
                                    }
                                },
                                showIcon = false // Hide app icons in RecordDetailScreen
                            )
                        }
                    }
                }
            }
        }
    }
}
