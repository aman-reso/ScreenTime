package com.app.screentime.appdetail.screen

// import androidx.compose.material.icons.filled.Block // Removed - App Blocking feature disabled
// import com.app.screentime.blocking.component.AddBlockingRuleBottomSheet // Removed - App Blocking feature disabled
// import com.app.screentime.blocking.manager.AppBlockManager // Removed - App Blocking feature disabled

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.appdetail.component.AppLaunchLimitBottomSheet
import com.app.screentime.appdetail.component.AppLimitBottomSheet
import com.app.screentime.appdetail.viewmodel.SingleAppUsageDetailViewModel
import com.app.screentime.config.R
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport
import com.app.screentime.landing.component.NetworkCard
import com.app.screentime.landing.component.UsageSummaryCard
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.statistics.model.ChartFormatterProps
import com.app.screentime.statistics.screen.WeeklyUsageChart
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.headerTheme
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.barchart.ODSBarItemDirection
import com.telekom.odsystem.organisms.barchart.ODSBarItemProps
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickAction
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionProps
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionSize
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleAppUsageDetailScreen(
    packageName: String,
    onBackClick: () -> Unit = {},
    viewModel: SingleAppUsageDetailViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {

    val activity = LocalActivity.current
    // Get theme mode for status bar styling
    val useDarkTheme = LocalThemeMode.current
    SideEffect {
        if (activity is AppCompatActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(scheme.basicBackground.getIntColor())
                } else {
                    SystemBarStyle.light(
                        scheme.basicBackground.getIntColor(),
                        darkScrim = scheme.basicBackground.getIntColor()
                    )
                }, navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT, Color.TRANSPARENT
                )
            )
        }
    }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

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

    var selectedDayIndex by remember { mutableStateOf<Int?>(null) }

    // Get the selected day or default to the most recent day
    val selectedDayData = remember(uiState.weeklyUsageData, selectedDayIndex) {
        if (uiState.weeklyUsageData.isNotEmpty()) {
            if (selectedDayIndex != null && selectedDayIndex!! < uiState.weeklyUsageData.size) {
                uiState.weeklyUsageData[selectedDayIndex!!]
            } else {
                uiState.weeklyUsageData.lastOrNull()
            }
        } else {
            null
        }
    }

    // Check if selected day is today (last item in the list)
    val isToday = remember(selectedDayIndex, uiState.weeklyUsageData) {
        selectedDayIndex == null || selectedDayIndex == uiState.weeklyUsageData.size - 1
    }

    // Format date label for non-today dates
    val dateLabel = remember(selectedDayData, isToday) {
        if (isToday || selectedDayData == null) {
            null // Will use default "Today's Total"
        } else {
            selectedDayData.date // e.g., "21 Jan"
        }
    }

    // Calculate selected day's usage
    val selectedUsage = selectedDayData?.screenTime ?: 0L
    val selectedUsageFormatted = remember(selectedUsage) {
        formatDuration(selectedUsage)
    }

    // Calculate selected day's notification count
    val selectedNotificationCount = selectedDayData?.notificationCount ?: 0

    // Calculate percentage change (compare selected day with previous day)
    val percentageChange = remember(uiState.weeklyUsageData, selectedDayIndex) {
        val currentIndex = selectedDayIndex ?: (uiState.weeklyUsageData.size - 1)
        if (uiState.weeklyUsageData.size >= 2 && currentIndex > 0) {
            val currentDay = uiState.weeklyUsageData[currentIndex].screenTime
            val previousDay = uiState.weeklyUsageData[currentIndex - 1].screenTime
            if (previousDay > 0) {
                ((currentDay - previousDay).toFloat() / previousDay * 100f)
            } else if (currentDay > 0) {
                100f // If previous day is 0 but current has usage, show 100% increase
            } else {
                null
            }
        } else {
            null
        }
    }

    // Convert weekly data to WeeklyDataReport format for chart
    val weeklyReports = remember(uiState.weeklyUsageData, packageName) {
        uiState.weeklyUsageData.map { dayData ->
            WeeklyDataReport(
                dayName = dayData.dayName,
                date = dayData.date,
                appUsage = listOf(
                    AppUsage(
                        id = 0L,
                        packageName = packageName,
                        appName = uiState.appName,
                        appScreenTime = dayData.screenTime,
                        wifiDataUsage = dayData.wifiDataUsage,
                        mobileDataUsage = dayData.mobileDataUsage,
                        notificationCount = dayData.notificationCount
                    ).apply {
                        applicationInfo = appInfo
                    }),
                totalScreenTime = dayData.screenTime,
                displayScreenTime = dayData.displayScreenTime,
                totalWifiDataUsage = dayData.wifiDataUsage,
                totalMobileDataUsage = dayData.mobileDataUsage,
                displayWifiDataUsage = dayData.displayWifiDataUsage,
                displayMobileDataUsage = dayData.displayMobileDataUsage,
                displayTotalDataUsage = dayData.displayTotalDataUsage,
                totalNotificationCount = dayData.notificationCount
            )
        }
    }

    // Create bar chart data from weekly reports
    val chartOrientation = ODSBarItemDirection.VERTICAL
    val barChartData = remember(weeklyReports, chartOrientation) {
        weeklyReports.mapIndexed { index, report ->
            val screenTimeHours = (report.totalScreenTime ?: 0L) / (1000.0 * 60.0 * 60.0)
            val dayLabel = when {
                report.dayName.isNullOrEmpty() -> "${index + 1}"
                report.dayName.length >= 3 -> report.dayName.take(3).uppercase()
                else -> report.dayName.uppercase()
            }
            ODSBarItemProps(
                xValue = index.toDouble(),
                xLabel = dayLabel,
                yValue = screenTimeHours,
                yLabel = String.format("%.1f", screenTimeHours)
            )
        }
    }

    // Create chart formatter props
    val chartFormatterProps = remember {
        ChartFormatterProps(
            valueFormatter = { x, y ->
                val hours = y.toInt()
                val minutes = ((y - hours) * 60).toInt()
                when {
                    hours > 0 -> "${hours}h ${minutes}m"
                    minutes > 0 -> "${minutes}m"
                    else -> "< 1m"
                }
            },
            verticalAxisFormatter = { value ->
                val hours = value.toInt()
                if (hours > 0) "${hours}h" else "${value.toInt()}h"
            }
        )
    }

    // Calculate network usage for selected day (not 7-day sum)
    val selectedWifiData = selectedDayData?.wifiDataUsage ?: 0L
    val selectedCellularData = selectedDayData?.mobileDataUsage ?: 0L
    val selectedTotalData = selectedWifiData + selectedCellularData
    val selectedWifiDataDisplay = selectedWifiData.toReadableDataSize()
    val selectedCellularDataDisplay = selectedCellularData.toReadableDataSize()
    val selectedTotalDataDisplay = selectedTotalData.toReadableDataSize()

    // Bottom sheet states
    // var showBlockBottomSheet by remember { mutableStateOf(false) } // Removed - App Blocking feature disabled
    var showTimerBottomSheet by remember { mutableStateOf(false) }
    var showAppLimitBottomSheet by remember { mutableStateOf(false) }
    var showAppLaunchLimitBottomSheet by remember { mutableStateOf(false) }

    ODSColumn(
        modifier = Modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .fillMaxWidth()
        ) {}

        when {
            uiState.isLoading -> {
                ODSBox(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = scheme.functionalSuccessStandard.getColor())
                }
            }

            uiState.error != null -> {
                ODSLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Error,
                                    tint = scheme.functionalDestructiveStandard
                                ), width = 48.dp, height = 48.dp
                            )
                            ODSText(
                                text = uiState.error ?: "Error loading data",
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.functionalDestructiveStandard
                            )
                        }
                    }
                }
            }

            else -> {
                val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
                val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)

                if (isExpandedScreen) {
                    // Two-pane layout: Info on left, Graph on right
                    ODSRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        background = listOf(ODSColorModel(scheme.basicBackground))
                    ) {
                        // Left pane: All information (50% width)
                        ODSLazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            padding = ODSPadding(horizontal = 8.dp)
                        ) {
                            // Header
                            item {
                                HeaderSection(
                                    appName = uiState.appName.ifEmpty { packageName },
                                    onBackClick = onBackClick,
                                    onMoreClick = { /* TODO */ },
                                    scheme = scheme
                                )
                            }

                            // Notification Card
                            item {
                                ODSInlineNotification(
                                    modifier = Modifier.fillMaxWidth(),
                                    scheme = scheme,
                                    props = ODSInlineNotificationProps(
                                        mode = ODSInlineNotificationMode.INFORMATIVE,
                                        title = stringResource(R.string.usage_insight),
                                        text = stringResource(R.string.usage_insight_description),
                                        showCloseButton = false
                                    ),
                                    onDismiss = {
                                        // Handle dismiss if needed
                                    })
                            }

                            item {
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3 * 3))
                            }
                        }

                        ODSBox(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight(),
                            background = listOf(ODSColorModel(scheme.basicStrokeSubtle))
                        ) {}

                        // Right pane: Graph and Network Card (50% width)
                        ODSLazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            padding = ODSPadding(horizontal = 8.dp)
                        ) {
                            item {
                                UsageSummaryCard(
                                    todayTotal = selectedUsageFormatted,
                                    notificationCount = selectedNotificationCount.takeIf { it > 0 },
                                    percentageChange = percentageChange,
                                    onClick = {},
                                    scheme = headerTheme.current,
                                    dateLabel = dateLabel
                                )
                            }

                            if (selectedTotalData > 0) {
                                item {
                                    NetworkCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        wifiDataUsage = selectedWifiData,
                                        wifiDataUsageDisplay = selectedWifiDataDisplay,
                                        cellularDataUsage = selectedCellularData,
                                        cellularDataUsageDisplay = selectedCellularDataDisplay,
                                        totalDataDisplayName = selectedTotalDataDisplay,
                                        scheme = scheme
                                    )
                                }
                            }

                            item {
                                ODSText(
                                    text = stringResource(R.string.activity),
                                    style = DSTextStyles.bodyL,
                                    color = scheme.basicText
                                )
                            }
                            if (weeklyReports.isNotEmpty()) {
                                item {
                                    WeeklyUsageChart(
                                        barChartData = barChartData,
                                        weeklyReports = weeklyReports,
                                        chartFormatterProps = chartFormatterProps,
                                        chartOrientation = chartOrientation,
                                        onBarClick = { index ->
                                            selectedDayIndex =
                                                if (selectedDayIndex == index) null else index
                                        },
                                        scheme = scheme
                                    )
                                }
                            } else {
                                item {
                                    ODSBox(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSText(
                                            text = stringResource(R.string.no_data_available),
                                            style = DSTextStyles.bodyMRegular,
                                            color = scheme.basicTextRecessive
                                        )
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3 * 3))
                            }
                        }
                    }
                } else {
                    // Single pane: All content in one column
                    ODSLazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        padding = ODSPadding(horizontal = 8.dp)
                    ) {
                        // Header
                        item {
                            HeaderSection(
                                appName = uiState.appName.ifEmpty { packageName },
                                onBackClick = onBackClick,
                                onMoreClick = { /* TODO */ },
                                scheme = scheme
                            )
                        }

                        // Notification Card
                        item {
                            ODSInlineNotification(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSInlineNotificationProps(
                                    mode = ODSInlineNotificationMode.INFORMATIVE,
                                    title = stringResource(R.string.usage_insight),
                                    text = stringResource(R.string.usage_insight_description),
                                    showCloseButton = false
                                )
                            )
                        }

                        item {
                            UsageSummaryCard(
                                todayTotal = selectedUsageFormatted,
                                notificationCount = selectedNotificationCount.takeIf { it > 0 },
                                percentageChange = percentageChange,
                                onClick = {},
                                scheme = headerTheme.current,
                                dateLabel = dateLabel
                            )
                        }

                        if (selectedTotalData > 0) {
                            item {
                                NetworkCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    wifiDataUsage = selectedWifiData,
                                    wifiDataUsageDisplay = selectedWifiDataDisplay,
                                    cellularDataUsage = selectedCellularData,
                                    cellularDataUsageDisplay = selectedCellularDataDisplay,
                                    totalDataDisplayName = selectedTotalDataDisplay,
                                    scheme = scheme
                                )
                            }
                        }

                        if (weeklyReports.isNotEmpty()) {
                            item {
                                WeeklyUsageChart(
                                    barChartData = barChartData,
                                    weeklyReports = weeklyReports,
                                    chartFormatterProps = chartFormatterProps,
                                    chartOrientation = chartOrientation,
                                    onBarClick = { index ->
                                        selectedDayIndex =
                                            if (selectedDayIndex == index) null else index
                                    },
                                    scheme = scheme
                                )
                            }
                        }

                        item {
                            QuickActionsCard(
                                onLaunchClick = { launchApp(context, packageName) },
                                onSetTimerClick = { showTimerBottomSheet = true },
                                onSetAppLimitClick = { showAppLimitBottomSheet = true },
                                onSetAppLaunchLimitClick = { showAppLaunchLimitBottomSheet = true },
                                onSettingsClick = { openAppSettings(context, packageName) },
                                scheme = scheme,
                                packageName = packageName
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3 * 3))
                        }
                    }
                }
            }
        }

        // App Limit Bottom Sheet
        if (showAppLimitBottomSheet) {
            AppLimitBottomSheet(
                appName = uiState.appName.ifEmpty { packageName },
                packageName = packageName,
                title = stringResource(R.string.set_app_limit),
                onDismiss = { showAppLimitBottomSheet = false },
                onSetLimit = { _, _ ->
                    showAppLimitBottomSheet = false
                }
            )
        }

        // App Launch Limit Bottom Sheet
        if (showAppLaunchLimitBottomSheet) {
            AppLaunchLimitBottomSheet(
                appName = uiState.appName.ifEmpty { packageName },
                packageName = packageName,
                onDismiss = { showAppLaunchLimitBottomSheet = false },
                onSetLimit = { _ ->
                    showAppLaunchLimitBottomSheet = false
                }
            )
        }
    }
}

@Composable
private fun HeaderSection(
    appName: String, onBackClick: () -> Unit, onMoreClick: () -> Unit, scheme: ODSTheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick, modifier = Modifier.size(44.dp)
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    tint = scheme.basicTextRecessive,
                    contentDescription = "Back"
                ), width = 24.dp, height = 24.dp
            )
        }

        ODSText(
            text = appName,
            style = DSTextStyles.bodyL,
            color = scheme.basicText,
            modifier = Modifier
                .weight(1f)
                .padding(start = DSVariables.spacingComponent2),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun QuickActionsCard(
    onLaunchClick: () -> Unit,
    onSetTimerClick: () -> Unit,
    onSetAppLimitClick: () -> Unit,
    onSetAppLaunchLimitClick: () -> Unit,
    onSettingsClick: () -> Unit,
    scheme: ODSTheme,
    packageName: String
) {
    val context = LocalContext.current

    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent3
    ) {
        ODSText(
            text = stringResource(R.string.quick_actions),
            style = DSTextStyles.bodyMRegular,
            color = scheme.basicText
        )
        if (packageName != context.packageName) {
            ActionCard(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                text = stringResource(R.string.launch),
                onClick = onLaunchClick,
                scheme = scheme
            )

            ActionCard(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Timelapse,
                text = stringResource(R.string.set_app_limit),
                onClick = onSetAppLimitClick,
                scheme = scheme
            )

            ActionCard(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.RocketLaunch,
                text = stringResource(R.string.set_app_launch_limit),
                onClick = onSetAppLaunchLimitClick,
                scheme = scheme
            )
        }
        ActionCard(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Settings,
            text = stringResource(R.string.settings),
            onClick = onSettingsClick,
            scheme = scheme
        )
    }
}

@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    ODSCardQuickAction(
        modifier = modifier, scheme = scheme, props = ODSCardQuickActionProps(
            size = ODSCardQuickActionSize.SMALL, filled = false
        ), onClick = onClick, contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSBox(
                    modifier = Modifier.size(DSVariables.sizingComponent10),
                    background = listOf(ODSColorModel(scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = icon,
                            tint = scheme.basicTextOnAccent,
                            contentDescription = text
                        ),
                        width = DSVariables.sizingComponent7,
                        height = DSVariables.sizingComponent7
                    )
                }

                ODSText(
                    text = text, style = DSTextStyles.bodySBold, color = scheme.basicText
                )
            }
        })
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
        openAppSettings(context, packageName)
    }
}

private fun openAppLimitSettings(context: Context, packageName: String) {
    try {
        // Try Digital Wellbeing app time limit settings
        val intent = Intent("android.settings.action.APP_USAGE_SETTINGS").apply {
            putExtra(Intent.EXTRA_PACKAGE_NAME, packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to app timer settings or app settings
        openAppTimerSettings(context, packageName)
    }
}

private fun openAppLaunchLimitSettings(context: Context, packageName: String) {
    try {
        // Try to open app launch limit settings (Digital Wellbeing)
        val intent = Intent("android.settings.action.APP_OPEN_BY_DEFAULT_SETTINGS").apply {
            putExtra(Intent.EXTRA_PACKAGE_NAME, packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to app usage settings or app settings
        openAppLimitSettings(context, packageName)
    }
}

