package com.app.screentime.profile.screen

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.navigation.Screen
import com.app.screentime.profile.component.ProfileTotpSection
import com.app.screentime.profile.component.SettingsItemCard
import com.app.screentime.profile.component.UserProfileCard
import com.app.screentime.profile.dialog.LanguageSelectionDialog
import com.app.screentime.profile.dialog.ThemeSelectionDialog
import com.app.screentime.profile.model.DialogType
import com.app.screentime.profile.model.ProfileSettingsKey
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.model.SettingsItemClickResult
import com.app.screentime.profile.viewmodel.ProfileViewModel
import com.app.screentime.service.ScreenTimeVpnService
import com.app.screentime.ui.language.LanguageViewModel
import com.app.screentime.ui.theme.ThemeViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToAppBlocking: () -> Unit = {},
    onNavigateToBlockedLinks: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
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

    if (showBlockedSitesBottomSheet) {
        BlockedSitesBottomSheetContent(
            onDismiss = { showBlockedSitesBottomSheet = false })
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

    ODSBox(
        modifier = Modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = ODSPadding(horizontal = 8.dp)
    ) {
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
                        onNavigateToBlockedLinks = onNavigateToBlockedLinks,
                        context = context,
                        vpnLauncher = vpnLauncher,
                        coroutineScope = coroutineScope,
                        onThemeDialogShow = { showThemeDialog = true },
                        onLanguageDialogShow = { showLanguageDialog = true },
                        onHelpSupportShow = { showHelpSupportBottomSheet = true },
                        onBlockedSitesShow = { showBlockedSitesBottomSheet = true },
                        onUsernameClick = { showEditUsernameBottomSheet = true },
                    )
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
    uiProps: com.app.screentime.profile.model.ProfileUiProps,
    viewModel: ProfileViewModel,
    onNavigateToAppBlocking: () -> Unit,
    onNavigateToBlockedLinks: () -> Unit,
    context: android.content.Context,
    vpnLauncher: androidx.activity.result.ActivityResultLauncher<Intent>,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onThemeDialogShow: () -> Unit,
    onLanguageDialogShow: () -> Unit,
    onHelpSupportShow: () -> Unit,
    onBlockedSitesShow: () -> Unit,
    onUsernameClick: () -> Unit
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
                            context = context,
                            vpnLauncher = vpnLauncher,
                            coroutineScope = coroutineScope,
                            onThemeDialogShow = onThemeDialogShow,
                            onLanguageDialogShow = onLanguageDialogShow,
                            onHelpSupportShow = onHelpSupportShow,
                            onBlockedSitesShow = onBlockedSitesShow
                        )
                    }
                }

                // Show VPN status and blocked sites when VPN is running
                if (data.key == ProfileSettingsKey.VPN_SERVICE && uiProps.isVpnRunning) {
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiProps.blockedSitesCount > 0) {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNavigateToBlockedLinks()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSText(
                                    text = stringResource(
                                        R.string.blocked_sites_count,
                                        uiProps.blockedSitesCount
                                    ),
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicText
                                )
                                if (uiProps.blockedSitesCount > 1) {
                                    ODSText(
                                        text = stringResource(R.string.see_all_sites),
                                        style = DSTextStyles.bodyMBold,
                                        color = scheme.basicText
                                    )
                                }
                            }
                        }
                    }
                }

                ODSDivider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSDividerProps(
                        variant = ODSDividerVariant.HORIZONTAL
                    )
                )
            }
        }

        is ProfileSettingsUi.Restriction -> {
            Column {
                SettingsItemCard(data)
                ODSDivider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSDividerProps(
                        variant = ODSDividerVariant.HORIZONTAL
                    )
                )
            }
        }

        is ProfileSettingsUi.SectionTitle -> {
            Spacer(modifier = Modifier.height(16.dp))
            ODSText(
                text = stringResource(data.text),
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            Spacer(modifier = Modifier.height(16.dp))
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
    context: android.content.Context,
    vpnLauncher: androidx.activity.result.ActivityResultLauncher<Intent>,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onThemeDialogShow: () -> Unit,
    onLanguageDialogShow: () -> Unit,
    onHelpSupportShow: () -> Unit,
    onBlockedSitesShow: () -> Unit
) {
    val result = viewModel.handleSettingsItemClick(key, url)

    when (result) {
        is SettingsItemClickResult.NavigateToScreen -> {
            // Handle navigation based on route
            when (result.route) {
                "app_blocking" -> onNavigateToAppBlocking()
                // Add other routes as needed
            }
        }
        is SettingsItemClickResult.ShowDialog -> {
            when (result.type) {
                DialogType.THEME -> onThemeDialogShow()
                DialogType.LANGUAGE -> onLanguageDialogShow()
                DialogType.HELP_SUPPORT -> onHelpSupportShow()
                DialogType.BLOCKED_SITES -> onBlockedSitesShow()
                DialogType.EDIT_USERNAME -> {
                    // Handled separately
                }
            }
        }
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
        is SettingsItemClickResult.OpenUrl -> {
            try {
                val intent = Intent(Intent.ACTION_VIEW, result.url.toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                android.util.Log.e("ProfileScreen", "Error opening URL: ${result.url}", e)
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
    }
}

