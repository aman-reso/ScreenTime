package com.app.screentime.landing.screen

import android.app.AppOpsManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.consent.screen.ConsentBottomSheetContent
import com.app.screentime.landing.component.GreetingUi
import com.app.screentime.landing.component.UsageDonutComponent
import com.app.screentime.landing.viewmodel.LandingViewModel
import com.app.screentime.navigation.Screen
import com.app.screentime.search.component.GlassSearchBar
import com.app.screentime.search.component.GlassSearchBarPlaceholder
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppPermissionCard
import com.app.screentime.ui.atom.AppUsageListUi
import com.app.screentime.ui.atom.NetworkCard

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


    // Consent bottom sheet state - check from preferences
    var showConsentSheet by remember { mutableStateOf(viewModel.shouldShowConsentScreen()) }
    var isRefreshing by remember { mutableStateOf(false) }
    // Pull-to-refresh state
    val pullRefreshState = rememberPullToRefreshState()

    // Keep isRefreshing in sync with ViewModel loading state
    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }


    if (showConsentSheet) {
        ConsentBottomSheetContent(username = "test-user", onDismiss = {
            viewModel.markConsentShown()
            showConsentSheet = false
        }, onAccept = {
            viewModel.markConsentShown()
            showConsentSheet = false
        })
    }

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .pullToRefresh(isRefreshing, pullRefreshState, onRefresh = {
                isRefreshing = true
                viewModel.loadRealUsageDataFromHelper()
            })
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    AppLoader()
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
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

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    item {
                        GreetingUi(username = uiState.username)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        GlassSearchBarPlaceholder(onClick = openSearchScreen)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    uiState.topUsedApps?.let {
                        item {
                            UsageDonutComponent(
                                report = it,
                                totalScreenTime = uiState.todayTotalScreenTime
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
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

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        NetworkCard(
                            modifier = Modifier.fillMaxWidth(),
                            wifiDataUsage = uiState.todayTotalWifiDataUsage,
                            wifiDataUsageDisplay = uiState.displayWifiDataUsage,
                            cellularDataUsage = uiState.todayTotalMobileDataUsage,
                            cellularDataUsageDisplay = uiState.displayMobileDataUsage,
                            totalDataDisplayName = uiState.displayTotalDataUsage
                        )
                    }


                    item {
                        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
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





