package com.app.screentime.appdetail.screen

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
// import androidx.compose.material.icons.filled.Block // Removed - App Blocking feature disabled
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNotifications
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.app.screentime.R
import com.app.screentime.appdetail.viewmodel.SingleAppUsageDetailViewModel
// import com.app.screentime.blocking.component.AddBlockingRuleBottomSheet // Removed - App Blocking feature disabled
// import com.app.screentime.blocking.manager.AppBlockManager // Removed - App Blocking feature disabled
import com.app.screentime.data.entity.AppUsage
import com.app.screentime.data.uiModel.WeeklyDataReport
import com.app.screentime.landing.component.NetworkCard
import com.app.screentime.landing.component.UsageSummaryCard
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.statistics.screen.WeeklyUsageChart
import com.app.screentime.statistics.model.ChartFormatterProps
import com.telekom.odsystem.organisms.barchart.ODSBarItemDirection
import com.telekom.odsystem.organisms.barchart.ODSBarItemProps
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
import com.telekom.odsystem.tokens.tokens.frogSecondaryScheme
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickAction
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionProps
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionSize
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

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
        if (activity is ComponentActivity) {
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
    val coroutineScope = rememberCoroutineScope()
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

    // Calculate today's usage (most recent day or current day)
    val todayUsage = remember(uiState.weeklyUsageData) {
        if (uiState.weeklyUsageData.isNotEmpty()) {
            uiState.weeklyUsageData.lastOrNull()?.screenTime ?: 0L
        } else {
            0L
        }
    }
    val todayUsageFormatted = remember(todayUsage) {
        formatDuration(todayUsage)
    }
    
    // Calculate today's notification count
    val todayNotificationCount = remember(uiState.weeklyUsageData) {
        if (uiState.weeklyUsageData.isNotEmpty()) {
            uiState.weeklyUsageData.lastOrNull()?.notificationCount ?: 0
        } else {
            0
        }
    }

    // Calculate percentage change (compare today with previous day)
    val percentageChange = remember(uiState.weeklyUsageData) {
        if (uiState.weeklyUsageData.size >= 2) {
            val today = uiState.weeklyUsageData.lastOrNull()?.screenTime ?: 0L
            val yesterday = uiState.weeklyUsageData[uiState.weeklyUsageData.size - 2].screenTime
            if (yesterday > 0) {
                ((today - yesterday).toFloat() / yesterday * 100f)
            } else if (today > 0) {
                100f // If yesterday is 0 but today has usage, show 100% increase
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

    var selectedDayIndex by remember { mutableStateOf<Int?>(null) }

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

    // Calculate network usage (in bytes)
    val totalWifiData = uiState.weeklyUsageData.sumOf { it.wifiDataUsage }
    val totalCellularData = uiState.weeklyUsageData.sumOf { it.mobileDataUsage }
    val totalData = totalWifiData + totalCellularData
    val wifiDataDisplay = totalWifiData.toReadableDataSize()
    val cellularDataDisplay = totalCellularData.toReadableDataSize()
    val totalDataDisplay = totalData.toReadableDataSize()

    // Bottom sheet states
    // var showBlockBottomSheet by remember { mutableStateOf(false) } // Removed - App Blocking feature disabled
    var showTimerBottomSheet by remember { mutableStateOf(false) }

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
                                        showCloseButton = true
                                    ),
                                    onDismiss = {
                                        // Handle dismiss if needed
                                    })
                            }

                            item {
                                QuickActionsCard(
                                    onLaunchClick = { launchApp(context, packageName) },
                                    onSetTimerClick = { showTimerBottomSheet = true },
                                    // onBlockClick = { showBlockBottomSheet = true }, // Removed - App Blocking feature disabled
                                    onSettingsClick = { openAppSettings(context, packageName) },
                                    scheme = scheme,
                                    packageName = packageName
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3 * 3))
                            }
                        }

                        // Divider
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
                                    todayTotal = todayUsageFormatted,
                                    dailyGoal = "6h",
                                    notificationCount = todayNotificationCount.takeIf { it > 0 },
                                    percentageChange = percentageChange,
                                    onClick = {},
                                    scheme = headerTheme.current
                                )
                            }

                            if (totalData > 0) {
                                item {
                                    NetworkCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        wifiDataUsage = totalWifiData,
                                        wifiDataUsageDisplay = wifiDataDisplay,
                                        cellularDataUsage = totalCellularData,
                                        cellularDataUsageDisplay = cellularDataDisplay,
                                        totalDataDisplayName = totalDataDisplay,
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
                                    showCloseButton = true
                                ),
                                onDismiss = {
                                    // Handle dismiss if needed
                                })
                        }

                        item {
                            UsageSummaryCard(
                                todayTotal = todayUsageFormatted,
                                dailyGoal = "6h",
                                notificationCount = todayNotificationCount.takeIf { it > 0 },
                                percentageChange = percentageChange,
                                onClick = {},
                                scheme = frogSecondaryScheme
                            )
                        }

                        if (totalData > 0) {
                            item {
                                NetworkCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    wifiDataUsage = totalWifiData,
                                    wifiDataUsageDisplay = wifiDataDisplay,
                                    cellularDataUsage = totalCellularData,
                                    cellularDataUsageDisplay = cellularDataDisplay,
                                    totalDataDisplayName = totalDataDisplay,
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
                                // onBlockClick = { showBlockBottomSheet = true }, // Removed - App Blocking feature disabled
                                onSettingsClick = { openAppSettings(context, packageName) },
                                scheme = scheme,
                                packageName
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3 * 3))
                        }
                    }
                }
            }
        }

        // Block App Bottom Sheet - Removed - App Blocking feature disabled
        /*
        if (showBlockBottomSheet) {
            AddBlockingRuleBottomSheet(
                selectedAppName = uiState.appName.ifEmpty { packageName },
                selectedPackageName = packageName,
                onDismiss = { showBlockBottomSheet = false },
                onBlockInstantly = { pkgName, appName ->
                    coroutineScope.launch {
                        try {
                            AppBlockManager.blockApp(pkgName, 0L) // 0L means instant block
                            showBlockBottomSheet = false
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                onBlockAfterLaunches = { pkgName, appName, maxLaunches ->
                    // This will be handled by the blocking system
                    showBlockBottomSheet = false
                },
                onBlockAfterDuration = { pkgName, appName, maxDurationMinutes ->
                    // This will be handled by the blocking system
                    showBlockBottomSheet = false
                },
                scheme = neutralScheme
            )
        }
        */

        // Timer Bottom Sheet
        if (showTimerBottomSheet) {
            TimerBottomSheet(
                appName = uiState.appName.ifEmpty { packageName },
                packageName = packageName,
                onDismiss = { showTimerBottomSheet = false },
                onSetTimer = { minutes ->
                    // Set timer logic here
                    showTimerBottomSheet = false
                })
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
    // onBlockClick: () -> Unit, // Removed - App Blocking feature disabled
    onSettingsClick: () -> Unit,
    scheme: ODSTheme,
    packageName: String
) {
    val context = LocalContext.current

    ODSColumn(
        modifier = Modifier.fillMaxWidth(), gap = DSVariables.spacingComponent3
    ) {
        ODSText(
            text = "Quick actions", style = DSTextStyles.bodyMRegular, color = scheme.basicText
        )
        if (packageName != context.packageName) {
            ActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                text = "Launch",
                onClick = onLaunchClick,
                scheme = scheme
            )

            ActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Timer,
                text = "Set timer",
                onClick = onSetTimerClick,
                scheme = scheme
            )

            // Block - Removed - App Blocking feature disabled
            /*
            ActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Block,
                text = "Block",
                onClick = onBlockClick,
                scheme = scheme
            )
            */

        }
        ActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Settings,
            text = "Settings",
            onClick = onSettingsClick,
            scheme = scheme
        )

        // Recover Notification - Removed - Notification recovery feature disabled
        /*
        ActionCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.EditNotifications,
            text = "Recover Notification",
            onClick = onBlockClick,
            scheme = scheme
        )
        */
    }
}

@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    scheme: ODSTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimerBottomSheet(
    appName: String, packageName: String, onDismiss: () -> Unit, onSetTimer: (Int) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scheme = neutralScheme

    var selectedMinutes by remember { mutableIntStateOf(30) }

    val appInfo = remember(packageName) {
        try {
            context.packageManager.getApplicationInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
    }

    val timeOptions = listOf(15, 30, 45, 60, 90, 120) // minutes

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = scheme.basicBackgroundCard.getColor(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            // Header
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    AppIcon(
//                        appInfo = appInfo,
//                        size = 64.dp,
//                        modifier = Modifier.size(64.dp)
//                    )
                    ODSColumn {
                        ODSText(
                            text = "Set Timer",
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ODSText(
                            text = appName,
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
                IconButton(
                    onClick = onDismiss, modifier = Modifier.size(40.dp)
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Default.Close,
                            tint = scheme.basicText,
                            contentDescription = "Close"
                        ), width = 24.dp, height = 24.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Section Label
            ODSText(
                text = "TIME OPTIONS",
                style = DSTextStyles.bodyMBold,
                color = scheme.basicTextRecessive,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Time Options Chips
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                timeOptions.forEach { minutes ->
                    FilterChip(
                        selected = selectedMinutes == minutes,
                        onClick = { selectedMinutes = minutes },
                        label = {
                            ODSText(
                                text = "${minutes}m",
                                style = DSTextStyles.bodyMBold,
                                color = if (selectedMinutes == minutes) scheme.basicTextOnAccent else scheme.basicText
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = scheme.basicAccent.getColor(),
                            containerColor = scheme.basicBackgroundCard.getColor(),
                            selectedLabelColor = scheme.basicTextOnAccent.getColor(),
                            labelColor = scheme.basicText.getColor()
                        ),
                        shape = RoundedCornerShape(9999.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Custom Time Input (Optional)
            ODSText(
                text = "Custom Time (minutes)",
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicText,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Slider for custom time
            ODSColumn {
                ODSText(
                    text = "$selectedMinutes minutes",
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicAccent,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Slider(
                    value = selectedMinutes.toFloat(),
                    onValueChange = { selectedMinutes = it.toInt() },
                    valueRange = 5f..180f,
                    steps = 34, // 5-minute steps from 5 to 180
                    colors = SliderDefaults.colors(
                        thumbColor = scheme.basicAccent.getColor(),
                        activeTrackColor = scheme.basicAccent.getColor(),
                        inactiveTrackColor = scheme.basicStroke.getColor()
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Set Timer Button
//            AppPrimaryButton(
//                modifier = Modifier.fillMaxWidth(),
//                text = "Set Timer",
//                onClick = {
//                    onSetTimer(selectedMinutes)
//                }
//            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
