package com.app.screentime.appdetail.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.appdetail.viewmodel.SingleAppUsageDetailViewModel
import com.app.screentime.blocker.AppBlockManager
import com.app.screentime.landing.screen.DailyUsageEntry
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.ui.atom.AppErrorCard
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.app.screentime.ui.atom.AppIcon
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.WeeklyUsageBarChart
import com.app.screentime.ui.theme.ErrorRed
import com.app.screentime.ui.theme.lightTextColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleAppUsageDetailScreen(
    packageName: String,
    navController: NavController,
    viewModel: SingleAppUsageDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(packageName) {
        if (uiState.packageName != packageName) {
            val appName = try {
                val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
                context.packageManager.getApplicationLabel(appInfo).toString()
            } catch (e: Exception) {
                packageName
            }
            viewModel.loadAppUsageData(packageName, appName)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppLoader(type = AppLoaderType.CIRCULAR)
                    }
                }
            }

            uiState.error != null -> {
                item {
                    AppErrorCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.error),
                        subTitleText = uiState.error ?: "",
                        callback = {
                            viewModel.loadAppUsageData(uiState.packageName, uiState.appName)
                        })
                }
            }

            else -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val appInfo = try {
                                context.packageManager.getApplicationInfo(packageName, 0)
                            } catch (e: Exception) {
                                null
                            }
                            AppIcon(
                                appInfo = appInfo,
                                size = 48.dp
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                AppText(
                                    text = uiState.appName.ifEmpty { packageName },
                                    style = AppTextStyle.SubTitle,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                AppText(
                                    text = packageName,
                                    style = AppTextStyle.Label,
                                    color = lightTextColor
                                )
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                            AppText(
                                text = stringResource(R.string.weekly_screen_time),
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val totalMinutes =
                                uiState.weeklyUsageData.sumOf { it.screenTime / (1000 * 60) }
                            val avgMinutes = if (uiState.weeklyUsageData.isNotEmpty()) {
                                totalMinutes / uiState.weeklyUsageData.size
                            } else 0
                            AppText(
                                text = stringResource(R.string.per_day_avg, avgMinutes),
                                style = AppTextStyle.Body,
                                color = ErrorRed
                            )
                        Spacer(modifier = Modifier.height(16.dp))
                        SingleAppWeeklyChart(
                            usageData = uiState.weeklyUsageData.map {
                                DailyUsageEntry(
                                    dayLabel = it.dayName.take(1),
                                    usageMinutes = (it.screenTime / (1000 * 60)).toInt()
                                )
                            }
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                            AppText(
                                text = stringResource(R.string.weekly_network_usage),
                                style = AppTextStyle.SubTitle,
                                fontWeight = FontWeight.Bold
                            )
                        Spacer(modifier = Modifier.height(8.dp))
                        val totalData =
                            uiState.weeklyUsageData.sumOf { it.wifiDataUsage + it.mobileDataUsage }
                        AppText(
                            text = totalData.toReadableDataSize() ?: "0 B",
                            style = AppTextStyle.Body,
                            color = ErrorRed
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SingleAppWeeklyNetworkChart(
                            usageData = uiState.weeklyUsageData.map {
                                DailyUsageEntry(
                                    dayLabel = it.dayName.take(1),
                                    usageMinutes = ((it.wifiDataUsage + it.mobileDataUsage) / (1024 * 1024)).toInt() // MB
                                )
                            }
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppText(
                            text = stringResource(R.string.actions),
                            style = AppTextStyle.SubTitle,
                            fontWeight = FontWeight.Bold
                        )

                        ActionButtonRow(
                            modifier = Modifier.fillMaxWidth(),
                            icon1 = Icons.Default.Timer,
                            text1 = stringResource(R.string.set_app_timer),
                            onClick1 = {
                                openAppTimerSettings(context, packageName)
                            },
                            icon2 = Icons.Default.Block,
                            text2 = stringResource(R.string.block_for_minutes),
                            onClick2 = {
                                coroutineScope.launch {
                                    AppBlockManager.blockApp(
                                        packageName,
                                        5 * 60 * 1000L
                                    ) // 5 minutes
                                    // Optionally, show a snackbar or toast
                                }
                            }
                        )

                        ActionButtonRow(
                            modifier = Modifier.fillMaxWidth(),
                            icon1 = Icons.Default.Wifi,
                            text1 = stringResource(R.string.internet_access),
                            onClick1 = {
                                // TODO: Implement internet access control
                            },
                            icon2 = Icons.Default.Settings,
                            text2 = stringResource(R.string.app_settings),
                            onClick2 = {
                                openAppSettings(context, packageName)
                            }
                        )

                        AppPrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.launch_app),
                            onClick = {
                                launchApp(context, packageName)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtonRow(
    modifier: Modifier = Modifier,
    icon1: androidx.compose.ui.graphics.vector.ImageVector,
    text1: String,
    onClick1: () -> Unit,
    icon2: androidx.compose.ui.graphics.vector.ImageVector,
    text2: String,
    onClick2: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionButton(
            modifier = Modifier.weight(1f),
            icon = icon1,
            text = text1,
            onClick = onClick1
        )
        ActionButton(
            modifier = Modifier.weight(1f),
            icon = icon2,
            text = text2,
            onClick = onClick2
        )
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary
            )
            AppText(
                text = text,
                style = AppTextStyle.Body
            )
        }
    }
}

@Composable
private fun SingleAppWeeklyChart(
    usageData: List<DailyUsageEntry>
) {
    WeeklyUsageBarChart(
        usageEntries = usageData,
        selectedDayIndex = null,
        onBarClick = null
    )
}

@Composable
private fun SingleAppWeeklyNetworkChart(
    usageData: List<DailyUsageEntry>
) {
    WeeklyUsageBarChart(
        usageEntries = usageData,
        selectedDayIndex = null,
        onBarClick = null
    )
}

private fun launchApp(context: Context, packageName: String) {
    try {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun openAppSettings(context: Context, packageName: String) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun openAppTimerSettings(context: Context, packageName: String) {
    try {
        val intent = Intent("android.settings.action.APP_TIMER").apply {
            putExtra(Intent.EXTRA_PACKAGE_NAME, packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        openAppSettings(context, packageName)
    }
}
