package com.app.screentime.profile.screen

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
// import androidx.activity.compose.rememberLauncherForActivityResult // VPN feature disabled
import androidx.activity.enableEdgeToEdge
// import androidx.activity.result.ActivityResultLauncher // VPN feature disabled
// import androidx.activity.result.contract.ActivityResultContracts // VPN feature disabled
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.profile.component.ProfileTotpSection
import com.app.screentime.profile.component.SettingsItemCard
import com.app.screentime.profile.component.UserProfileCard
import com.app.screentime.profile.dialog.LanguageSelectionDialog
import com.app.screentime.profile.dialog.ThemeSelectionDialog
import com.app.screentime.profile.model.DialogType
import com.app.screentime.profile.model.ProfileSettingsKey
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.model.ProfileUiProps
import com.app.screentime.profile.model.SettingsItemClickResult
import com.app.screentime.profile.viewmodel.ProfileViewModel
import com.app.screentime.service.NotificationHistoryListener
 import com.app.screentime.service.ScreenTimeVpnService
import com.app.screentime.ui.language.LanguageViewModel
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ThemeViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToAppBlocking: () -> Unit = {},
    onNavigateToAppLock: () -> Unit = {},
    onNavigateToBlockedLinks: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme,
    onNavigateToCapturedNotifications: () -> Unit = {},
    onNavigateToWallpaper: () -> Unit = {}
    // Device Admin removed - not suitable for consumer apps
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
                },
                navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                )
            )
        }
    }

    val context = LocalContext.current
    val uiProps by viewModel.uiProps.collectAsState()
    val currentTheme by themeViewModel.theme.collectAsState()
    val currentLanguage by languageViewModel.language.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showHelpSupportBottomSheet by remember { mutableStateOf(false) }
    var showBlockedSitesBottomSheet by remember { mutableStateOf(false) }
    var showEditUsernameBottomSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // VPN feature disabled - commented out
    /*
    val vpnLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            // VPN permission granted, start service
            val intent = Intent(context, ScreenTimeVpnService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            viewModel.loadProfile() // Reload to update VPN status
        }
    }
    */

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { theme ->
                themeViewModel.setTheme(theme)
                showThemeDialog = false
            },
            scheme = scheme
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { language ->
                languageViewModel.setLanguage(language)
                showLanguageDialog = false
            },
            scheme = scheme
        )
    }

    if (showHelpSupportBottomSheet) {
        HelpSupportBottomSheetContent(
            onDismiss = { showHelpSupportBottomSheet = false })
    }


    if (showEditUsernameBottomSheet) {
        EditUsernameBottomSheetContent(
            currentUsername = uiProps?.profile?.username,
            onDismiss = {
                showEditUsernameBottomSheet = false
                viewModel.loadProfile() // Reload profile after dismissing
            })
    }

    // Handle settings item clicks
    LaunchedEffect(Unit) {
        // This effect will be triggered when needed
    }

    ODSColumn(
        modifier = Modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = ODSPadding(horizontal = 8.dp)
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        uiProps?.let { props ->
            ODSLazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = props.settingsList,
                    key = { item -> item.key?.name ?: item.text.toString() },
                    contentType = { item ->
                        when (item) {
                            is ProfileSettingsUi.ProfileData -> "profile_data"
                            is ProfileSettingsUi.Other -> "other"
                            is ProfileSettingsUi.SectionTitle -> "section_title"
                            is ProfileSettingsUi.Restriction -> "restriction"
                        }
                    }
                ) { data ->
                    ProfileSettingsItem(
                        data = data,
                        scheme = scheme,
                        uiProps = props,
                        viewModel = viewModel,
                        onNavigateToAppBlocking = onNavigateToAppBlocking,
                        onNavigateToAppLock = onNavigateToAppLock,
                        onNavigateToBlockedLinks = onNavigateToBlockedLinks,
                        context = context,
                        // vpnLauncher = vpnLauncher, // VPN feature disabled
                        coroutineScope = coroutineScope,
                        onThemeDialogShow = { showThemeDialog = true },
                        onLanguageDialogShow = { showLanguageDialog = true },
                        onHelpSupportShow = { showHelpSupportBottomSheet = true },
                        onUsernameClick = { showEditUsernameBottomSheet = true },
                        onNavigateToCapturedNotifications = onNavigateToCapturedNotifications,
                        onNavigateToWallpaper = onNavigateToWallpaper
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                }
            }
        }
    }
}

/**
 * Composable for rendering individual profile settings items
 * Uses only UI Props - no business logic
 */
@Composable
private fun ProfileSettingsItem(
    data: ProfileSettingsUi,
    scheme: ODSTheme,
    uiProps: ProfileUiProps,
    viewModel: ProfileViewModel,
    onNavigateToAppBlocking: () -> Unit,
    onNavigateToAppLock: () -> Unit,
    onNavigateToBlockedLinks: () -> Unit,
    context: Context,
    // vpnLauncher: ActivityResultLauncher<Intent>, // VPN feature disabled
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onThemeDialogShow: () -> Unit,
    onLanguageDialogShow: () -> Unit,
    onHelpSupportShow: () -> Unit,
    onUsernameClick: () -> Unit,
    onNavigateToCapturedNotifications: () -> Unit,
    onNavigateToWallpaper: () -> Unit
) {
    when (data) {
        is ProfileSettingsUi.ProfileData -> {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                UserProfileCard(
                    modifier = Modifier.fillMaxWidth(),
                    username = uiProps.profile?.username,
                    userId = uiProps.profile?.userId,
                    joinedOn = uiProps.profile?.joinedOn,
                    onUsernameClick = onUsernameClick
                )

                ProfileTotpSection(
                    viewModel = viewModel,
                    scheme = scheme
                )
            }
        }

        is ProfileSettingsUi.Other -> {
            Column {
                SettingsItemCard(data) {
                    data.key?.let { key ->
                        handleSettingsItemClick(
                            key = key,
                            url = data.url,
                            viewModel = viewModel,
                            onNavigateToAppBlocking = onNavigateToAppBlocking,
                            onNavigateToAppLock = onNavigateToAppLock,
                            context = context,
                            // vpnLauncher = vpnLauncher, // VPN feature disabled
                            coroutineScope = coroutineScope,
                            onThemeDialogShow = onThemeDialogShow,
                            onLanguageDialogShow = onLanguageDialogShow,
                            onHelpSupportShow = onHelpSupportShow,
                            onNavigateToCapturedNotifications = onNavigateToCapturedNotifications,
                            onNavigateToWallpaper = onNavigateToWallpaper,
                            onNavigateToBlockedLinks = onNavigateToBlockedLinks
                        )
                    }
                }

                // Show VPN status and blocked sites when VPN is running - VPN feature disabled
                // if (data.key == ProfileSettingsKey.VPN_SERVICE && uiProps.isVpnRunning) {
                if (false) { // VPN feature disabled
                    Spacer(modifier = Modifier.height(8.dp))
                    if (uiProps.blockedSitesCount > 0) {
                        ODSListRowStandard(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNavigateToBlockedLinks()
                                },
                            scheme = scheme,
                            props = ODSListRowStandardProps(
                                labelText = stringResource(
                                    R.string.blocked_sites_count,
                                    uiProps.blockedSitesCount
                                ), descriptionText = "See all sites"
                            )
                        )
                    }
                }
            }
        }

        is ProfileSettingsUi.Restriction -> {
            Column {
                SettingsItemCard(data)
            }
        }

        is ProfileSettingsUi.SectionTitle -> {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
            ODSText(
                text = stringResource(data.text),
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        }
    }
}

/**
 * Handle click events for profile settings items
 * All business logic is handled by the use case - UI only handles the result
 */
private fun handleSettingsItemClick(
    key: ProfileSettingsKey,
    url: String,
    viewModel: ProfileViewModel,
    onNavigateToAppBlocking: () -> Unit,
    onNavigateToAppLock: () -> Unit,
    context: android.content.Context,
    // vpnLauncher: ActivityResultLauncher<Intent>, // VPN feature disabled
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onThemeDialogShow: () -> Unit,
    onLanguageDialogShow: () -> Unit,
    onHelpSupportShow: () -> Unit,
    onNavigateToCapturedNotifications: () -> Unit,
    onNavigateToWallpaper: () -> Unit,
    onNavigateToBlockedLinks: () -> Unit
) {
    val result = viewModel.handleSettingsItemClick(key, url)

    when (result) {
        is SettingsItemClickResult.NavigateToScreen -> {
            when (result.route) {
                "app_blocking" -> onNavigateToAppBlocking()
                "app_lock" -> onNavigateToAppLock()
                "captured_notifications" -> {
                    if (isNotificationListenerEnabled(context)) {
                        onNavigateToCapturedNotifications()
                    } else {
                        openNotificationAccessSettings(context)
                    }
                }

                "wallpaper" -> onNavigateToWallpaper()
                "blocked_links" -> {
                    onNavigateToBlockedLinks()
                }

                // Add other routes as needed
            }
        }

        is SettingsItemClickResult.ShowDialog -> {
            when (result.type) {
                DialogType.THEME -> onThemeDialogShow()
                DialogType.LANGUAGE -> onLanguageDialogShow()
                DialogType.HELP_SUPPORT -> onHelpSupportShow()
                DialogType.BLOCKED_SITES -> {

                }

                DialogType.EDIT_USERNAME -> {
                    // Handled separately
                }
            }
        }

        // VPN feature disabled - commented out
        /*
        is SettingsItemClickResult.RequestVpnPermission -> {
            vpnLauncher.launch(result.intent)
        }

        is SettingsItemClickResult.StartVpnService -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(result.intent)
            } else {
                context.startService(result.intent)
            }
            viewModel.loadProfile() // Reload to update VPN status
        }

        is SettingsItemClickResult.StopVpnService -> {
            context.startService(result.intent)
            viewModel.loadProfile() // Reload to update VPN status
        }
        */

        is SettingsItemClickResult.OpenUrl -> {
            try {
                val intent = Intent(Intent.ACTION_VIEW, result.url.toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Error opening URL: ${result.url}", e)
            }
        }

        is SettingsItemClickResult.RequestWidgetSetup -> {
            coroutineScope.launch {
                viewModel.requestWidgetSetup()
            }
        }

        is SettingsItemClickResult.None -> {
            // No action needed
        }
        else -> {}
    }
}

/**
 * Check if notification listener service is enabled
 */
private fun isNotificationListenerEnabled(context: Context): Boolean {
    val packageName = context.packageName
    val flat = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    if (!flat.isNullOrBlank()) {
        val names = flat.split(":")
        for (name in names) {
            val componentName = ComponentName.unflattenFromString(name)
            if (componentName != null && packageName == componentName.packageName) {
                return true
            }
        }
    }
    return false
}

fun openNotificationAccessSettings(context: Context) {
    try {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS).apply {
                putExtra(
                    Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                    ComponentName(
                        context,
                        NotificationHistoryListener::class.java
                    ).flattenToString()
                )
            }
        } else {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e(
            "ProfileScreen",
            "Error opening Notification Access Settings",
            e
        )
    }
}


