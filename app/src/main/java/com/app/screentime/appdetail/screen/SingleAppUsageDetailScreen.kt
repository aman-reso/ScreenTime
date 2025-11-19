package com.app.screentime.appdetail.screen

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.appdetail.viewmodel.SingleAppUsageDetailViewModel
import com.app.screentime.blocking.manager.AppBlockManager
import com.app.screentime.landing.screen.DailyUsageEntry
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.ui.atom.AppErrorCard
import com.app.screentime.R
import com.app.screentime.ui.atom.AppGlassyCard
import com.app.screentime.ui.atom.AppIcon
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.NetworkCard
import com.app.screentime.ui.atom.WeeklyUsageBarChart
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.LocalAppColors
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
    val colors = LocalAppColors.current ?: return

    // Get app info for icon
    val appInfo = remember(packageName) {
        try {
            context.packageManager.getApplicationInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
    }

    LaunchedEffect(packageName) {
        if (uiState.packageName != packageName) {
            val appName = try {
                val info = context.packageManager.getApplicationInfo(packageName, 0)
                context.packageManager.getApplicationLabel(info).toString()
            } catch (e: Exception) {
                packageName
            }
            viewModel.loadAppUsageData(packageName, appName)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section: Always show Back arrow, App Name (Title), and App Icon
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back arrow
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        contentDescription = "",
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        tint = colors.tint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // App Name (Title) and per day average (Subtitle) - Left side
                Column(modifier = Modifier.weight(1f)) {
                    // Title: App Name - Always show
                    AppText(
                        text = uiState.appName.ifEmpty { packageName },
                        style = AppTextStyle.Title,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    // Subtitle: Average per day - Only show when data is loaded
                    if (uiState.weeklyUsageData.isNotEmpty() && !uiState.isLoading) {
                        Spacer(modifier = Modifier.height(4.dp))
                        val totalMinutes =
                            uiState.weeklyUsageData.sumOf { it.screenTime / (1000 * 60) }
                        val avgMinutes = if (uiState.weeklyUsageData.isNotEmpty()) {
                            totalMinutes / uiState.weeklyUsageData.size
                        } else 0
                        AppText(
                            text = stringResource(R.string.per_day_avg, avgMinutes),
                            style = AppTextStyle.SubTitle,
                            color = colors.textSecondary
                        )
                    }
                }

                // App Icon - Right side (end) - Always show
                if (appInfo != null) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AppIcon(
                            appInfo = appInfo,
                            size = 44.dp
                        )
                    }
                }
            }
        }

        when {
            uiState.isLoading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
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
                        }
                    )
                }
            }

            else -> {

                // 2. Usage Graph Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.card)
                            .padding(20.dp)
                    ) {
                        AppText(
                            text = stringResource(R.string.weekly_screen_time),
                            style = AppTextStyle.SubTitle,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
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

                // 3. Shortcuts Section (in the middle)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.card)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppText(
                            text = stringResource(R.string.actions),
                            style = AppTextStyle.SubTitle,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )

                        // Action buttons in rows
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

                // 4. WiFi/Data Usage Section (at bottom)
                item {
                    val totalWifiData = uiState.weeklyUsageData.sumOf { it.wifiDataUsage }
                    val totalMobileData = uiState.weeklyUsageData.sumOf { it.mobileDataUsage }
                    val totalData = totalWifiData + totalMobileData

                    NetworkCard(
                        modifier = Modifier.fillMaxWidth(),
                        wifiDataUsage = totalWifiData,
                        wifiDataUsageDisplay = totalWifiData.toReadableDataSize(),
                        cellularDataUsage = totalMobileData,
                        cellularDataUsageDisplay = totalMobileData.toReadableDataSize(),
                        totalDataDisplayName = totalData.toReadableDataSize()
                    )
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
    onClick1: () -> Unit = {},
    icon2: androidx.compose.ui.graphics.vector.ImageVector,
    text2: String,
    onClick2: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return

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
    val colors = LocalAppColors.current ?: return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        shape = RoundedCornerShape(12.dp)
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
                tint = colors.accent,
                modifier = Modifier.size(20.dp)
            )
            AppText(
                text = text,
                style = AppTextStyle.Body,
                color = colors.textPrimary
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

@Preview(showBackground = true)
@Composable
fun xy() {

}