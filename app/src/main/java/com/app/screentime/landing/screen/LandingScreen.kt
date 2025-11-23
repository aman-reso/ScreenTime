package com.app.screentime.landing.screen

import android.app.AppOpsManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.permission.PermissionManager
import com.app.screentime.R
import com.app.screentime.consent.screen.ConsentBottomSheetContent
import com.app.screentime.landing.component.GreetingUi
import com.app.screentime.landing.component.UsageDonutComponent
import com.app.screentime.landing.viewmodel.LandingViewModel
import com.app.screentime.navigation.Screen
import com.app.screentime.search.component.GlassSearchBar
import com.app.screentime.search.component.GlassSearchBarPlaceholder
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppPermissionCard
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.AppUsageListUi
import com.app.screentime.ui.atom.NetworkCard
import com.app.screentime.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: LandingViewModel = hiltViewModel(),
    openSearchScreen: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val appColors = LocalAppColors.current ?: return

    // Permission manager
    val permissionManager = remember { PermissionManager(context) }

    // Notification permission state
    var hasNotificationPermission by remember {
        mutableStateOf(permissionManager.hasNotificationPermission())
    }
    var isPermissionDenied by remember { mutableStateOf(false) }
    var hasRequestedPermission by remember { mutableStateOf(false) }

    // Check if permission is denied (blocked)
    fun checkIfPermissionDenied(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                val activity = context as? androidx.activity.ComponentActivity?
                if (activity != null) {
                    val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                    return !shouldShowRationale
                }
            }
        }
        return false
    }

    // Notification permission launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasRequestedPermission = true
        // Re-check permission status after result
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = permissionManager.hasNotificationPermission()

            // If user dismissed (denied), show warning card
            // isPermissionDenied will be true if user denied (either temporarily or permanently)
            if (!isGranted) {
                isPermissionDenied = true
            } else {
                isPermissionDenied = false
            }
        }
    }

    // Settings launcher for opening app settings
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Re-check permission after returning from settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = permissionManager.hasNotificationPermission()
            isPermissionDenied = !hasNotificationPermission && checkIfPermissionDenied()
        }
    }

    // Check notification permission on launch and request if not granted
    // Only check on Android 13+ (TIRAMISU) where notification permission is required
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = permissionManager.hasNotificationPermission()

            // If permission is not granted, request it (show system popup)
            // This will show the system dialog with Allow/Dismiss options
            if (!hasNotificationPermission && !hasRequestedPermission) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else if (!hasNotificationPermission && hasRequestedPermission) {
                // If we already requested and permission is still not granted, show warning
                isPermissionDenied = true
            }
        }
    }

    // Usage Stats Permission Logic (First Time Only)
    var showUsageStatsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val hasUsageStats = permissionManager.hasUsageStatsPermission()
        if (!hasUsageStats && viewModel.shouldAskForUsageStatsPermission()) {
            showUsageStatsDialog = true
            viewModel.markUsageStatsPermissionRequested()
        }
    }

    if (showUsageStatsDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showUsageStatsDialog = false },
            title = {
                Text(
                    text = "Permission Required",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "To track your screen time accurately, this app needs Usage Access permission. Please enable it in Settings.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        showUsageStatsDialog = false
                        try {
                            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Fallback or log error
                            e.printStackTrace()
                        }
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showUsageStatsDialog = false }
                ) {
                    Text("Cancel")
                }
            },
            containerColor = appColors.card,
            titleContentColor = appColors.textPrimary,
            textContentColor = appColors.textSecondary
        )
    }

    // Consent bottom sheet state
    var showConsentSheet by remember { mutableStateOf(viewModel.shouldShowConsentScreen()) }
    var isRefreshing by remember { mutableStateOf(false) }
    // Pull-to-refresh state
    val pullRefreshState = rememberPullToRefreshState()

    // Keep isRefreshing in sync with ViewModel loading state
    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }

    // Show consent bottom sheet if not already displayed
    if (showConsentSheet) {
        ConsentBottomSheetContent(
            username = uiState.username ?: "",
            onDismiss = {
                // Mark as displayed even if dismissed (no matter success or failure)
                viewModel.markConsentShown()
                showConsentSheet = false
            },
            onAccept = {
                // Mark as displayed after acceptance (success case)
                viewModel.markConsentShown()
                showConsentSheet = false
            }
        )
    }

    Box(
        modifier = modifier
            .background(appColors.background)
            .padding(horizontal = 8.dp)
            .pullToRefresh(isRefreshing, pullRefreshState, onRefresh = {
                isRefreshing = true
                viewModel.loadRealUsageDataFromHelper()
            })
    ) {
        when {
            uiState.isLoading -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    item {
                        GreetingUi(
                            username = uiState.username,
                            onLeaderboardClick = {
                                navController?.navigate(Screen.Leaderboard.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        GlassSearchBarPlaceholder(
                            onClick = {
                                navController?.navigate(Screen.Search.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AppLoader()
                        }
                    }
                }
            }

            uiState.error != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    item {
                        GreetingUi(
                            username = uiState.username,
                            onLeaderboardClick = {
                                navController?.navigate(Screen.Leaderboard.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        GlassSearchBarPlaceholder(
                            onClick = {
                                navController?.navigate(Screen.Search.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Error",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.error ?: "",
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.clearError() }) {
                                    Text("Dismiss")
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    item {
                        GreetingUi(
                            username = uiState.username,
                            onLeaderboardClick = {
                                navController?.navigate(Screen.Leaderboard.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Show notification permission warning if denied (user dismissed the popup)
                    if (isPermissionDenied && !hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        item {
                            NotificationPermissionWarningCard(
                                onEnableClick = {
                                    // First check if we can show the permission dialog
                                    val activity = context as? androidx.activity.ComponentActivity
                                    if (activity != null) {
                                        val canShowDialog =
                                            ActivityCompat.shouldShowRequestPermissionRationale(
                                                activity,
                                                android.Manifest.permission.POST_NOTIFICATIONS
                                            )

                                        if (canShowDialog) {
                                            // Can show dialog, request permission again
                                            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            // Permission is permanently denied, open settings
                                            val intent =
                                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                    data =
                                                        Uri.fromParts(
                                                            "package",
                                                            context.packageName,
                                                            null
                                                        )
                                                }
                                            settingsLauncher.launch(intent)
                                        }
                                    } else {
                                        // Fallback: open settings if we can't determine
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data =
                                                    Uri.fromParts(
                                                        "package",
                                                        context.packageName,
                                                        null
                                                    )
                                            }
                                        settingsLauncher.launch(intent)
                                    }
                                },
                                onRequestClick = {
                                    // Try requesting permission again
                                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    item {
                        GlassSearchBarPlaceholder(
                            onClick = {
                                navController?.navigate(Screen.Search.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    uiState.topUsedApps?.let {
                        item {
                            UsageDonutComponent(
                                report = it,
                                totalScreenTime = uiState.todayTotalScreenTime,
                                onClick = {
                                    navController?.navigate(Screen.Statistics.route)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    item {
                        NetworkCard(
                            modifier = Modifier.fillMaxWidth(),
                            wifiDataUsage = uiState.todayTotalWifiDataUsage,
                            wifiDataUsageDisplay = uiState.displayWifiDataUsage,
                            cellularDataUsage = uiState.todayTotalMobileDataUsage,
                            cellularDataUsageDisplay = uiState.displayMobileDataUsage,
                            totalDataDisplayName = uiState.displayTotalDataUsage
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }


                    val topUsedAppsList = uiState.topUsedApps ?: emptyList()
                    val totalCount = topUsedAppsList.size
                    itemsIndexed(
                        items = topUsedAppsList,
                        key = { _, appUsage -> appUsage.packageName ?: appUsage.id },
                        contentType = { _, _ -> "app_usage_item" }
                    ) { index, data ->
                        AppUsageListUi(
                            appUsage = data,
                            index = index,
                            totalCount = totalCount,
                            onClick = {
                                data.packageName?.let { packageName ->
                                    navController?.navigate(
                                        Screen.SingleAppUsageDetail.createRoute(packageName)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

data class DailyUsageEntry(
    val dayLabel: String, // e.g., "M", "T", "W"
    val usageMinutes: Int // usage in minutes for that day
)

// Helper functions for permission checking and data loading
private fun checkUsageStatsPermission(context: android.content.Context): Boolean {
    val appOps = context.getSystemService(android.content.Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}

/**
 * Warning card shown when notification permission is denied
 */
@Composable
private fun NotificationPermissionWarningCard(
    onEnableClick: () -> Unit,
    onRequestClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEnableClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = colors.error.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = colors.error,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = stringResource(R.string.notification_permission_required),
                    style = AppTextStyle.Body,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = colors.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppText(
                    text = stringResource(R.string.notification_permission_warning_message),
                    style = AppTextStyle.Label,
                    color = colors.textSecondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onEnableClick,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colors.error
                )
            ) {
                AppText(
                    text = stringResource(R.string.enable),
                    style = AppTextStyle.Label,
                    color = colors.textOnPrimary
                )
            }
        }
    }
}


