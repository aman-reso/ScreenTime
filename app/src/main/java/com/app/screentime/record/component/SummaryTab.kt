package com.app.screentime.record.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.landing.component.UsageDonutComponent
import com.app.screentime.record.util.formatUsageTime
import com.app.screentime.record.viewmodel.RecordDetailUiState
import com.app.screentime.record.viewmodel.RecordDetailViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppSecondaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.AppUsageListUi
import com.app.screentime.ui.theme.AppColors

@Composable
fun SummaryTab(
    viewModel: RecordDetailViewModel,
    uiState: RecordDetailUiState,
    colors: AppColors,
    navController: NavController,
    selectedDateDisplay: String
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
                            report = appUsageList,
                            totalScreenTime = totalScreenTime,
                            title = selectedDateDisplay
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
                        appUsage = appUsage,
                        index = index,
                        totalCount = totalCount,
                        onClick = null, // Remove click functionality in RecordDetailScreen
                        showIcon = false // Hide app icons in RecordDetailScreen
                    )
                }
            }
        }
    }
}

