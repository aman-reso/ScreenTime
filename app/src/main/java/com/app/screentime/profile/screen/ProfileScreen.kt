package com.app.screentime.profile.screen

import android.content.Intent
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.navigation.Screen
import com.app.screentime.profile.component.SettingsItemCard
import com.app.screentime.profile.component.UserProfileCard
import com.app.screentime.profile.dialog.LanguageSelectionDialog
import com.app.screentime.profile.dialog.ThemeSelectionDialog
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.screen.EditUsernameBottomSheetContent
import com.app.screentime.profile.screen.HelpSupportBottomSheetContent
import com.app.screentime.profile.viewmodel.ProfileViewModel
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.repository.BlockedLinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.app.screentime.service.ScreenTimeVpnService
import com.app.screentime.service.VpnPermissionManager
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.language.LanguageViewModel
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.ThemeViewModel
import com.app.screentime.widget.WidgetSetupHelper
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri

private const val TIME_STEP_SECONDS = 60L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val internalState = viewModel.getInternalState()
    val currentTheme by themeViewModel.theme.collectAsState()
    val currentLanguage by languageViewModel.language.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showTOTPBottomSheet by remember { mutableStateOf(false) }
    var showHelpSupportBottomSheet by remember { mutableStateOf(false) }
    var showBlockedSitesBottomSheet by remember { mutableStateOf(false) }
    var showEditUsernameBottomSheet by remember { mutableStateOf(false) }
    val appColors = LocalAppColors.current ?: return
    val coroutineScope = rememberCoroutineScope()

    val vpnPermissionManager = remember { VpnPermissionManager(context) }
    var isVpnRunning by remember { mutableStateOf(false) }
    var blockedSitesCount by remember { mutableIntStateOf(0) }

    val vpnLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val intent = Intent(context, ScreenTimeVpnService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            isVpnRunning = true
        }
    }

    // Check VPN status
    LaunchedEffect(Unit) {
        isVpnRunning = vpnPermissionManager.hasVpnPermission() && isVpnServiceRunning(context)
    }

    // Update VPN status and blocked sites count periodically
    LaunchedEffect(Unit) {
        val repository = BlockedLinkRepository(
            ScreenTimeDatabase.getDatabase(context).blockedLinkDao()
        )
        while (true) {
            delay(2000) // Check every 2 seconds
            isVpnRunning = vpnPermissionManager.hasVpnPermission() && isVpnServiceRunning(context)
            if (isVpnRunning) {
                withContext(Dispatchers.IO) {
                    blockedSitesCount = repository.getBlockedLinksCount()
                }
            } else {
                blockedSitesCount = 0
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { theme ->
                themeViewModel.setTheme(theme)
                showThemeDialog = false
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { language ->
                languageViewModel.setLanguage(language)
                showLanguageDialog = false
            }
        )
    }

    if (showTOTPBottomSheet) {
        ShowTOTPBottomSheetContent(
            onDismiss = { showTOTPBottomSheet = false }
        )
    }

    if (showHelpSupportBottomSheet) {
        HelpSupportBottomSheetContent(
            onDismiss = { showHelpSupportBottomSheet = false }
        )
    }

    if (showBlockedSitesBottomSheet) {
        BlockedSitesBottomSheetContent(
            onDismiss = { showBlockedSitesBottomSheet = false }
        )
    }

    if (showEditUsernameBottomSheet) {
        EditUsernameBottomSheetContent(
            currentUsername = internalState.profile?.username,
            onDismiss = {
                showEditUsernameBottomSheet = false
                viewModel.loadProfile() // Reload profile after dismissing
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appColors.background)
    ) {
        when {
            internalState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AppLoader(type = AppLoaderType.CIRCULAR)
                }
            }

            internalState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.error),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = internalState.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.clearError() }) {
                        Text(stringResource(R.string.dismiss))
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    uiState.data?.let { settingsList ->
                        // Update VPN item text based on state
                        val updatedSettingsList = settingsList.map { item ->
                            if (item.key == "vpn_service") {
                                ProfileSettingsUi.Other(
                                    text = if (isVpnRunning) context.getString(R.string.disable_vpn) else context.getString(
                                        R.string.enable_vpn
                                    ),
                                    url = "",
                                    key = "vpn_service"
                                )
                            } else {
                                item
                            }
                        }
                        items(
                            items = updatedSettingsList,
                            key = { item -> item.key ?: item.text },
                            contentType = { item ->
                                when (item) {
                                    is ProfileSettingsUi.ProfileData -> "profile_data"
                                    is ProfileSettingsUi.AccountDetails -> "account_details"
                                    is ProfileSettingsUi.Other -> "other"
                                    is ProfileSettingsUi.SectionTitle -> "section_title"
                                    is ProfileSettingsUi.Restriction -> "restriction"
                                }
                            }
                        ) { data ->
                            when (data) {
                                is ProfileSettingsUi.ProfileData -> {
                                    UserProfileCard(
                                        username = internalState.profile?.username,
                                        userId = internalState.profile?.userId,
                                        onUsernameClick = {
                                            showEditUsernameBottomSheet = true
                                        }
                                    )
                                }

                                is ProfileSettingsUi.Other -> {
                                    Column {
                                        SettingsItemCard(data) {
                                            when (data.key) {
                                                "theme" -> showThemeDialog = true
                                                "language" -> showLanguageDialog = true
                                                "widget" -> {
                                                    coroutineScope.launch {
                                                        WidgetSetupHelper.requestWidgetSetup(context)
                                                    }
                                                }

                                                "help_support" -> showHelpSupportBottomSheet = true
                                                "block_app", "set_app_limit", "set_app_launch_limit" -> {
                                                    // Navigate to App Blocking screen (handles permissions internally)
                                                    navController?.navigate(Screen.AppBlocking.route)
                                                }

                                                "ad_blocking" -> {
                                                    // Navigate to App Blocking screen for ad blocking feature
                                                    navController?.navigate(Screen.AppBlocking.route)
                                                }

                                                "vpn_service" -> {
                                                    if (isVpnRunning) {
                                                        val stopIntent = Intent(
                                                            context,
                                                            ScreenTimeVpnService::class.java
                                                        )
                                                        stopIntent.putExtra("stop", true)
                                                        context.startService(stopIntent)
                                                        isVpnRunning = false
                                                    } else {
                                                        // Request VPN permission and start service
                                                        if (vpnPermissionManager.hasVpnPermission()) {
                                                            // Permission already granted, start service
                                                            val intent = Intent(
                                                                context,
                                                                ScreenTimeVpnService::class.java
                                                            )
                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                context.startForegroundService(
                                                                    intent
                                                                )
                                                            } else {
                                                                context.startService(intent)
                                                            }
                                                            isVpnRunning = true
                                                        } else {
                                                            // Request permission
                                                            vpnPermissionManager.requestVpnPermission(
                                                                vpnLauncher
                                                            )
                                                        }
                                                    }
                                                }

                                                else -> {
                                                    // Open URL if no specific key handler
                                                    if (data.url.isNotEmpty()) {
                                                        try {
                                                            val intent = Intent(
                                                                Intent.ACTION_VIEW,
                                                                data.url.toUri()
                                                            )
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            context.startActivity(intent)
                                                        } catch (e: Exception) {
                                                            android.util.Log.e(
                                                                "ProfileScreen",
                                                                "Error opening URL: ${data.url}",
                                                                e
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        // Show VPN status and stop option when VPN is running
                                        if (data.key == "vpn_service" && isVpnRunning) {
                                            Spacer(modifier = Modifier.height(8.dp))

                                            // VPN Stop Card
                                            androidx.compose.material3.Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        // Stop VPN service
                                                        val intent = Intent(
                                                            context,
                                                            ScreenTimeVpnService::class.java
                                                        )
                                                        intent.putExtra("stop", true)
                                                        context.stopService(intent)
                                                        // Stop foreground service properly
                                                        isVpnRunning = false
                                                    },
                                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                                    containerColor = appColors.error.copy(alpha = 0.1f)
                                                )
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 12.dp
                                                        ),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        AppText(
                                                            text = stringResource(R.string.disable_vpn),
                                                            style = AppTextStyle.Body,
                                                            color = appColors.error,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        AppText(
                                                            text = stringResource(R.string.tap_to_stop_vpn),
                                                            style = AppTextStyle.Label,
                                                            color = appColors.textSecondary
                                                        )
                                                    }
                                                    androidx.compose.material3.Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Stop VPN",
                                                        tint = appColors.error,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                            }

                                            // Show blocked sites count when VPN is on and count > 0
                                            if (blockedSitesCount > 0) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                androidx.compose.material3.Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            navController?.navigate(com.app.screentime.navigation.Screen.BlockedLinks.route)
                                                        },
                                                    colors = androidx.compose.material3.CardDefaults.cardColors(
                                                        containerColor = appColors.card
                                                    )
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                horizontal = 16.dp,
                                                                vertical = 12.dp
                                                            ),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        AppText(
                                                            text = stringResource(
                                                                R.string.blocked_sites_count,
                                                                blockedSitesCount
                                                            ),
                                                            style = AppTextStyle.Body,
                                                            color = appColors.textPrimary
                                                        )
                                                        if (blockedSitesCount > 1) {
                                                            AppText(
                                                                text = stringResource(R.string.see_all_sites),
                                                                style = AppTextStyle.Label,
                                                                color = appColors.success,
                                                                fontWeight = FontWeight.Medium
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                is ProfileSettingsUi.AccountDetails -> {
                                    SettingsItemCard(data) {
                                        when (data.key) {
                                            "totp" -> showTOTPBottomSheet = true
                                        }
                                    }
                                }

                                is ProfileSettingsUi.Restriction -> {
                                    SettingsItemCard(data)
                                }

                                is ProfileSettingsUi.SectionTitle -> {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    AppText(
                                        text = data.text, fontWeight = FontWeight.Bold,
                                        style = AppTextStyle.Label
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Check if VPN service is currently running
 */
private fun isVpnServiceRunning(context: android.content.Context): Boolean {
    val activityManager =
        context.getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
    return activityManager.getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == ScreenTimeVpnService::class.java.name }
}

