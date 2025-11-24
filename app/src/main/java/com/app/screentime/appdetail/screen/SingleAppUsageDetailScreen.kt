package com.app.screentime.appdetail.screen

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.appdetail.viewmodel.SingleAppUsageDetailViewModel
import com.app.screentime.blocking.manager.AppBlockManager
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.record.repository.toReadableDataSize
import com.app.screentime.ui.atom.AppIcon
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleAppUsageDetailScreen(
    packageName: String,
    navController: NavController,
    viewModel: SingleAppUsageDetailViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
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
    val todayUsageMinutes = (todayUsage / (1000 * 60)).toInt()

    // Calculate weekly change percentage (compare first half vs second half of week)
    val weeklyChangePercent = remember(uiState.weeklyUsageData) {
        if (uiState.weeklyUsageData.size >= 6) {
            val firstHalf = uiState.weeklyUsageData.take(3).sumOf { it.screenTime }
            val secondHalf = uiState.weeklyUsageData.drop(3).take(3).sumOf { it.screenTime }
            if (firstHalf > 0) {
                ((secondHalf - firstHalf).toDouble() / firstHalf * 100).toInt()
            } else if (secondHalf > 0) {
                100 // If first half is 0 but second half has usage, show 100% increase
            } else {
                0
            }
        } else {
            0
        }
    }

    // Convert weekly data to chart format
    val weeklyData = remember(uiState.weeklyUsageData) {
        if (uiState.weeklyUsageData.isEmpty()) {
            emptyList()
        } else {
            val maxUsage = uiState.weeklyUsageData.maxOfOrNull { it.screenTime } ?: 1L
            uiState.weeklyUsageData.map { day ->
                val percentage = ((day.screenTime.toDouble() / maxUsage) * 100).toInt()
                DayUsage(
                    label = day.dayName.take(3), // "Mon", "Tue", etc.
                    percentage = percentage.coerceIn(0, 100),
                    value = day.displayScreenTime
                )
            }
        }
    }

    // Calculate network usage
    val totalWifiData = uiState.weeklyUsageData.sumOf { it.wifiDataUsage }
    val totalCellularData = uiState.weeklyUsageData.sumOf { it.mobileDataUsage }
    val totalData = totalWifiData + totalCellularData
    val wifiDataGB = totalWifiData / (1024.0 * 1024.0 * 1024.0)
    val cellularDataGB = totalCellularData / (1024.0 * 1024.0 * 1024.0)
    val totalDataGB = totalData / (1024.0 * 1024.0 * 1024.0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.success)
                }
            }

            uiState.error != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = colors.error,
                                modifier = Modifier.size(48.dp)
                            )
                            AppText(
                                text = uiState.error ?: "Error loading data",
                                style = AppTextStyle.Body,
                                color = colors.error
                            )
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    // Header
                    item {
                        HeaderSection(
                            appName = uiState.appName.ifEmpty { packageName },
                            onBackClick = { navController.popBackStack() },
                            onMoreClick = { /* TODO */ }
                        )
                    }

                    // Hero Card
                    item {
                        HeroCard(
                            todayUsage = todayUsageMinutes,
                            weeklyChange = weeklyChangePercent
                        )
                    }

                    // Weekly Chart
                    if (weeklyData.isNotEmpty()) {
                        item {
                            WeeklyChartCard(weeklyData = weeklyData)
                        }
                    }

                    // Quick Actions
                    item {
                        QuickActionsCard(
                            onLaunchClick = { launchApp(context, packageName) },
                            onSetTimerClick = { openAppTimerSettings(context, packageName) },
                            onBlockClick = {
                                coroutineScope.launch {
                                    AppBlockManager.blockApp(packageName, 5 * 60 * 1000L)
                                }
                            },
                            onSettingsClick = { openAppSettings(context, packageName) }
                        )
                    }

                    // Network Usage
                    if (totalData > 0) {
                        item {
                            NetworkUsageCard(
                                wifiDataGB = wifiDataGB,
                                cellularDataGB = cellularDataGB,
                                totalDataGB = totalDataGB
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    appName: String,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = colors.textSecondary,
                modifier = Modifier.size(24.dp)
            )
        }

        AppText(
            text = appName,
            style = AppTextStyle.Title,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary
        )

        IconButton(
            onClick = onMoreClick,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = colors.textSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun HeroCard(
    todayUsage: Int,
    weeklyChange: Int
) {
    val colors = LocalAppColors.current ?: return
    // Use accent color for primary, create gradient with success color
    val primaryColor = colors.accent
    val lightColor = colors.success

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, lightColor)
                )
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = primaryColor.copy(alpha = 0.3f)
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                AppText(
                    text = "Today's usage",
                    style = AppTextStyle.Label,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = "$todayUsage min",
                    style = AppTextStyle.Title,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(0x40FFFFFF),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    AppText(
                        text = "+$weeklyChange% this week",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // Decorative icon
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun WeeklyChartCard(weeklyData: List<DayUsage>) {
    val colors = LocalAppColors.current ?: return
    val primaryColor = colors.accent
    val lightColor = colors.success
    val tertiaryColor = colors.error
    val lightTertiary = colors.error.copy(alpha = 0.7f)
    var selectedIndex by remember { mutableStateOf(weeklyData.size - 1) } // Last day selected by default
    val maxHeight = weeklyData.maxOfOrNull { it.percentage } ?: 100

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Weekly activity",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                TextButton(onClick = { /* TODO */ }) {
                    AppText(
                        text = "Details",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Medium,
                        color = colors.accent
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyData.forEachIndexed { index, day ->
                    val isSelected = selectedIndex == index
                    val barHeight by animateFloatAsState(
                        targetValue = (day.percentage / maxHeight.toFloat()).coerceIn(0.1f, 1f),
                        animationSpec = tween(300 + index * 80, delayMillis = 300),
                        label = "barHeight"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedIndex = index },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height((140.dp * barHeight).coerceAtLeast(8.dp))
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                .background(
                                    brush = if (isSelected) {
                                        Brush.verticalGradient(
                                            colors = listOf(lightTertiary, tertiaryColor)
                                        )
                                    } else {
                                        Brush.verticalGradient(
                                            colors = listOf(lightColor, primaryColor)
                                        )
                                    }
                                )
                                .shadow(
                                    elevation = if (isSelected) 4.dp else 2.dp,
                                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                                    spotColor = if (isSelected) 
                                        tertiaryColor.copy(alpha = 0.35f) 
                                    else 
                                        primaryColor.copy(alpha = 0.25f)
                                )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Day label
                        AppText(
                            text = day.label,
                            style = AppTextStyle.Caption,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) tertiaryColor else colors.textSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsCard(
    onLaunchClick: () -> Unit,
    onSetTimerClick: () -> Unit,
    onBlockClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            AppText(
                text = "Quick actions",
                style = AppTextStyle.SubTitle,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Launch (Primary)
                ActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.ArrowForward,
                    text = "Launch",
                    isPrimary = true,
                    onClick = onLaunchClick
                )

                // Set timer
                ActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Timer,
                    text = "Set timer",
                    isPrimary = false,
                    onClick = onSetTimerClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Block
                ActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Block,
                    text = "Block",
                    isPrimary = false,
                    onClick = onBlockClick
                )

                // Settings
                ActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Settings,
                    text = "Settings",
                    isPrimary = false,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) {
                colors.accent
            } else {
                colors.card
            }
        ),
        border = if (!isPrimary) {
            androidx.compose.foundation.BorderStroke(2.dp, colors.border)
        } else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPrimary) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isPrimary) Color.White.copy(alpha = 0.2f)
                        else colors.accent.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = if (isPrimary) Color.White else colors.accent,
                    modifier = Modifier.size(24.dp)
                )
            }

            AppText(
                text = text,
                style = AppTextStyle.Label,
                fontWeight = FontWeight.Medium,
                color = if (isPrimary) Color.White else colors.textPrimary
            )
        }
    }
}

@Composable
private fun NetworkUsageCard(
    wifiDataGB: Double,
    cellularDataGB: Double,
    totalDataGB: Double
) {
    val colors = LocalAppColors.current ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Network usage",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = colors.card,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    AppText(
                        text = "${String.format("%.3f", totalDataGB)} GB",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // WiFi
            NetworkItem(
                name = "Wi-Fi",
                dataSize = "${String.format("%.3f", wifiDataGB)} GB",
                percentage = if (totalDataGB > 0) ((wifiDataGB / totalDataGB) * 100).toInt() else 0,
                icon = Icons.Default.Wifi,
                iconColor = colors.accent,
                progressColor = colors.accent,
                isWifi = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cellular
            NetworkItem(
                name = "Cellular",
                dataSize = "${String.format("%.3f", cellularDataGB)} GB",
                percentage = if (totalDataGB > 0) ((cellularDataGB / totalDataGB) * 100).toInt() else 0,
                icon = Icons.Default.SignalCellular4Bar,
                iconColor = colors.error,
                progressColor = colors.error,
                isWifi = false
            )
        }
    }
}

@Composable
private fun NetworkItem(
    name: String,
    dataSize: String,
    percentage: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    progressColor: Color,
    isWifi: Boolean
) {
    val colors = LocalAppColors.current ?: return
    val progressWidth by animateFloatAsState(
        targetValue = (percentage / 100f).coerceIn(0f, 1f),
        animationSpec = tween(1000),
        label = "progress"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            iconColor.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    AppText(
                        text = name,
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                    AppText(
                        text = dataSize,
                        style = AppTextStyle.Label,
                        color = colors.textSecondary
                    )
                }
            }

            AppText(
                text = "$percentage%",
                style = AppTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.border.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(progressColor)
            )
        }
    }
}

private data class DayUsage(
    val label: String,
    val percentage: Int,
    val value: String
)

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
