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
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.profile.component.ProfileTotpSection // Removed - TOTP feature disabled
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
// import com.app.screentime.service.NotificationHistoryListener // Removed - Notification History feature disabled
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
    // onNavigateToAppBlocking: () -> Unit = {}, // Removed - App Blocking feature disabled
    onNavigateToAppLock: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme,
    onNavigateToCapturedNotifications: () -> Unit = {},
    onNavigateToWallpaper: () -> Unit = {},
    onNavigateToRecoverNotification: () -> Unit = {}
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
                        // onNavigateToAppBlocking = onNavigateToAppBlocking, // Removed - App Blocking feature disabled
                        onNavigateToAppLock = onNavigateToAppLock,
                        context = context,
                        coroutineScope = coroutineScope,
                        onThemeDialogShow = { showThemeDialog = true },
                        onLanguageDialogShow = { showLanguageDialog = true },
                        onHelpSupportShow = { showHelpSupportBottomSheet = true },
                        onUsernameClick = { showEditUsernameBottomSheet = true },
                        onNavigateToCapturedNotifications = onNavigateToCapturedNotifications,
                        onNavigateToWallpaper = onNavigateToWallpaper,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                }
                item {
                    CraftedWithLoveSection(scheme = scheme)
                }
            }
        }
    }
}

/**
 * "Crafted with love from Patna" section at the bottom of profile
 */
@Composable
fun CraftedWithLoveSection(
    scheme: ODSTheme = neutralScheme
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = DSVariables.spacingComponent5,
                bottom = DSVariables.spacingComponent4
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = DSVariables.spacingComponent1
    ) {
        ODSText(
            text = "Made with ❤️ in India",
            style = DSTextStyles.oxSubtitle,
            color = scheme.basicText,
            textAlign = TextAlign.Center
        )
        ODSText(
            text = "& Crafted in Patna",
            style = DSTextStyles.oxBodySRegular,
            color = scheme.basicTextRecessive,
            textAlign = TextAlign.Center
        )
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
    // onNavigateToAppBlocking: () -> Unit, // Removed - App Blocking feature disabled
    onNavigateToAppLock: () -> Unit,
    context: Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onThemeDialogShow: () -> Unit,
    onLanguageDialogShow: () -> Unit,
    onHelpSupportShow: () -> Unit,
    onUsernameClick: () -> Unit,
    onNavigateToCapturedNotifications: () -> Unit,
    onNavigateToWallpaper: () -> Unit,
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
                            context = context,
                            coroutineScope = coroutineScope,
                            onThemeDialogShow = onThemeDialogShow,
                            onLanguageDialogShow = onLanguageDialogShow,
                            onHelpSupportShow = onHelpSupportShow,
                            onNavigateToCapturedNotifications = onNavigateToCapturedNotifications,
                            onNavigateToWallpaper = onNavigateToWallpaper,
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
    context: Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onThemeDialogShow: () -> Unit,
    onLanguageDialogShow: () -> Unit,
    onHelpSupportShow: () -> Unit,
    onNavigateToCapturedNotifications: () -> Unit,
    onNavigateToWallpaper: () -> Unit,
) {
    val result = viewModel.handleSettingsItemClick(key, url)

    when (result) {
        is SettingsItemClickResult.NavigateToScreen -> {
            when (result.route) {
                "wallpaper" -> onNavigateToWallpaper() // Removed - Wallpaper feature disabled
                "recover_notification" ->{
                    if (isNotificationListenerEnabled(context)) {
                        onNavigateToCapturedNotifications()
                    } else {
                        openNotificationAccessSettings(context)
                    }
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

        is SettingsItemClickResult.ShareApp -> {
            shareApp(context)
        }

        is SettingsItemClickResult.None -> {
            // No action needed
        }

        else -> {}
    }
}

/**
 * Share app with text and Play Store link
 */
private fun shareApp(context: Context) {
    try {
        val appName = context.getString(R.string.app_name)
        val packageName = context.packageName
        val playStoreLink = "https://play.google.com/store/apps/details?id=$packageName"
        val shareText =
            "Check out $appName - Track your screen time and manage your app usage!\n\n$playStoreLink"

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Check out $appName")
        }

        val chooserIntent = Intent.createChooser(shareIntent, "Share App")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    } catch (e: Exception) {
        Log.e("ProfileScreen", "Error sharing app", e)
    }
}


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

