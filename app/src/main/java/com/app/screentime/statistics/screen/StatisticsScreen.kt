package com.app.screentime.statistics.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.navigation.Screen
import com.app.screentime.statistics.viewmodel.StatisticsViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.AppUsageListUi
import com.app.screentime.ui.atom.WeeklyUsageChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        when {
            uiState.isLoading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppLoader(
                            type = AppLoaderType.CIRCULAR,
                            modifier = Modifier
                        )
                    }
                }
            }

            uiState.error != null -> {
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

            uiState.weeklyReports.isEmpty() -> {
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

            else -> {
                item {
                    WeeklyUsageChart(
                        weeklyReports = uiState.weeklyReports,
                        selectedDayIndex = uiState.selectedDayIndex,
                        onBarClick = { dayIndex ->
                            viewModel.selectDay(dayIndex)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Display selected day's app usage
                uiState.selectedDayIndex?.let { dayIndex ->
                    if (dayIndex in uiState.weeklyReports.indices) {
                        val selectedDay = uiState.weeklyReports[dayIndex]
                        val appUsageList =
                            selectedDay.appUsage?.sortedByDescending { it.appScreenTime }
                                ?: emptyList()
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    AppText(
                                        text = selectedDay.dayName ?: "Unknown",
                                        style = AppTextStyle.SubTitle,
                                        fontWeight = FontWeight.Bold
                                    )
                                    AppText(
                                        text = selectedDay.date ?: "",
                                        style = AppTextStyle.Label
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    AppText(
                                        text = selectedDay.displayScreenTime ?: "0m",
                                        style = AppTextStyle.Body,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (!selectedDay.displayTotalDataUsage.isNullOrBlank()) {
                                        AppText(
                                            text = selectedDay.displayTotalDataUsage,
                                            style = AppTextStyle.Label
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // App Usage List for selected day
                        if (appUsageList.isNotEmpty()) {
                            val appUsageCount = appUsageList.size
                            itemsIndexed(
                                items = appUsageList,
                                key = { _, appUsage -> appUsage.packageName ?: appUsage.id },
                                contentType = { _, _ -> "app_usage_item" }
                            ) { appIndex, appUsage ->
                                AppUsageListUi(
                                    appUsage = appUsage,
                                    index = appIndex,
                                    totalCount = appUsageCount,
                                    onClick = {
                                        appUsage.packageName?.let { packageName ->
                                            navController.navigate(
                                                Screen.SingleAppUsageDetail.createRoute(packageName)
                                            )
                                        }
                                    }
                                )
                            }
                        } else {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AppText(
                                            text = stringResource(R.string.no_app_usage_for_day),
                                            style = AppTextStyle.Label
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
