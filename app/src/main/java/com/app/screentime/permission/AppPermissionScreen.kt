package com.app.screentime.permission

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.permission.component.bottombar.BottomBar
import com.app.screentime.permission.component.bottombar.BottomBarProps
import com.app.screentime.permission.component.herosection.HeroSection
import com.app.screentime.permission.component.herosection.HeroSectionProps
import com.app.screentime.permission.component.infocard.InfoCardList
import com.app.screentime.permission.viewmodel.PermissionViewModel
import com.app.screentime.profile.dialog.LanguageSelectionDialog
import com.app.screentime.ui.language.LanguageViewModel
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * App Permission Screen - Complete permission request screen using component architecture.
 */
@Composable
fun AppPermissionScreen(
    modifier: Modifier = Modifier,
    onAllPermissionsGranted: () -> Unit = {},
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
                },
                navigationBarStyle = SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT
                )
            )
        }
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val languageViewModel: LanguageViewModel = hiltViewModel()
    val permissionViewModel: PermissionViewModel = hiltViewModel()
    val currentLanguage by languageViewModel.language.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Permission state
    var hasUsageStatsPermission by remember { mutableStateOf(false) }

    // Check permission on launch
    LaunchedEffect(Unit) {
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        if (hasUsageStatsPermission) {
            onAllPermissionsGranted()
        }
    }

    // Usage stats permission launcher
    val usageStatsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        if (hasUsageStatsPermission) {
            onAllPermissionsGranted()
        }
    }

    // Handle Allow button click
    val handleAllowClick = {
        if (!hasUsageStatsPermission) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            if (intent.resolveActivity(context.packageManager) != null) {
                usageStatsPermissionLauncher.launch(intent)
            } else {
                val fallback = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    "package:${context.packageName}".toUri()
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                usageStatsPermissionLauncher.launch(fallback)
            }
        } else {
            onAllPermissionsGranted()
        }
    }


    val scrollState = rememberScrollState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    
    ODSBox(modifier = modifier, clipContent = true) {
        ODSBox(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                ODSBox(
                    modifier = Modifier
                        .height(statusBarPadding)
                        .fillMaxWidth()
                ) {}
                ODSColumn(
                    modifier = Modifier,
                    clipContent = true,
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent3,
                        top = DSVariables.spacingLayout4,
                        bottom = DSVariables.spacingLayout9 + DSVariables.spacingLayout4
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeroSection(
                        scheme = scheme,
                        props = HeroSectionProps(),
                        scrollState = scrollState
                    )

                    ODSBox(height = DSVariables.spacingLayout4) {}

                    InfoCardList(
                        scheme = scheme
                    )
                }
            }

            // Language icon in top right corner
            ODSIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = statusBarPadding + 12.dp,
                        end = 12.dp
                    )
                    .clickable { 
                        permissionViewModel.trackLanguageChangeClick()
                        showLanguageDialog = true 
                    },
                iconModel = ODSIconModel(
                    imageVector = Icons.Outlined.Language,
                    tint = scheme.basicText
                ),
                width = 24.dp,
                height = 24.dp
            )

            BottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                scheme = scheme,
                props = BottomBarProps.default(context),
                onAllowClick = handleAllowClick
            )
        }
    }

    // Language selection dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { language ->
                showLanguageDialog = false
                coroutineScope.launch {
                    delay(100)
                    if (activity is Activity) {
                        activity.recreate()
                    }
                }
            },
            scheme = scheme
        )
    }
}

/**
 * Helper function to check usage stats permission
 */
fun checkUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}
