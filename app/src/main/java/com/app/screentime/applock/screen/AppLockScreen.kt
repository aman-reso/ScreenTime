package com.app.screentime.applock.screen

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.core.net.toUri
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.screentime.R
import com.app.screentime.applock.component.AppLockCard
import com.app.screentime.applock.component.PINSetupBottomSheet
import com.app.screentime.applock.component.PINVerificationBottomSheet
import com.app.screentime.applock.utils.stopService
import com.app.screentime.applock.utils.updateServiceState
import com.app.screentime.applock.viewmodel.AppLockViewModel
import com.app.screentime.blocking.component.PermissionScreenContent
import com.app.screentime.blocking.component.getInstalledApps
import com.app.screentime.ui.atom.AppImageIcon
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLockScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: AppLockViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val activity = LocalActivity.current
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
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Check permissions - USAGE_STATS and Overlay (no Accessibility Service needed)
    var hasUsageStatsPermission by remember { mutableStateOf(checkUsageStatsPermission(context)) }
    var hasOverlayPermission by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

    // Re-check permissions when screen is focused
    LaunchedEffect(Unit) {
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        hasOverlayPermission = Settings.canDrawOverlays(context)
    }

    // Re-check permissions when lifecycle resumes (user returns from settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasUsageStatsPermission = checkUsageStatsPermission(context)
                hasOverlayPermission = Settings.canDrawOverlays(context)
                updateServiceState(context, hasUsageStatsPermission, hasOverlayPermission)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Show permission screen if any permission not granted
    if (!hasUsageStatsPermission || !hasOverlayPermission) {
        PermissionScreenContent(
            modifier = modifier.verticalScroll(rememberScrollState()),
            hasAccessibilityPermission = hasUsageStatsPermission, // Reuse for USAGE_STATS
            hasOverlayPermission = hasOverlayPermission,
            onContinue = {
                // Start ListenerService only if there are locked apps and permissions are granted
                updateServiceState(context, hasUsageStatsPermission, hasOverlayPermission)
            },
            onBackClick = onBackClick,
            scheme = scheme,
            firstPermissionTitle = stringResource(R.string.usage_access_title),
            firstPermissionDescription = stringResource(R.string.usage_access_description),
            screenTitle = stringResource(R.string.app_lock_permissions_title),
            screenDescription = stringResource(R.string.app_lock_permissions_description),
            overlayDescription = stringResource(R.string.app_lock_overlay_description),
            onAccessibilityPermissionClick = {
                // Open USAGE_STATS settings instead of Accessibility
                try {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    intent.data = "package:${context.packageName}".toUri()
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        "Please enable 'Usage Access' permission for ScreenTime",
                        Toast.LENGTH_LONG
                    ).show()
                    // Re-check permission periodically when user might return
                    coroutineScope.launch {
                        repeat(10) {
                            delay(1000)
                            val newStatus = checkUsageStatsPermission(context)
                            if (newStatus != hasUsageStatsPermission) {
                                hasUsageStatsPermission = newStatus
                                return@launch
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error opening Usage Access settings: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onOverlayPermissionClick = {
                try {
                    if (!Settings.canDrawOverlays(context)) {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            "package:${context.packageName}".toUri()
                        )
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        Toast.makeText(
                            context,
                            "Please enable 'Display over other apps' permission",
                            Toast.LENGTH_LONG
                        ).show()
                        // Re-check permission periodically when user might return
                        coroutineScope.launch {
                            repeat(10) {
                                delay(1000)
                                val newStatus = Settings.canDrawOverlays(context)
                                if (newStatus != hasOverlayPermission) {
                                    hasOverlayPermission = newStatus
                                    return@launch
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context, "Overlay permission already granted", Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context, "Error opening Overlay Settings", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        return
    }

    // Start/Stop ListenerService based on locked apps and permissions
    // Stop service immediately if app lock list becomes empty
    LaunchedEffect(hasUsageStatsPermission, hasOverlayPermission, uiState.rules) {
        // Update service state based on active rules (Lock or Block) and permissions
        updateServiceState(context, hasUsageStatsPermission, hasOverlayPermission)
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    
    var showPINSetup by remember { mutableStateOf(false) }
    var showAppSelection by remember { mutableStateOf(false) }
    var selectedPackageName by remember { mutableStateOf<String?>(null) }
    var selectedAppName by remember { mutableStateOf<String?>(null) }

    // Check if PIN is set, show setup if not
    LaunchedEffect(uiState.isPINSet) {
        if (!uiState.isPINSet && !showPINSetup) {
            showPINSetup = true
        }
    }

    // Sync tab selection with pager state
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    // Sync pager with tab selection
    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = ODSPadding(horizontal = 8.dp)
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = scheme.basicText,
                            contentDescription = "Back"
                        ),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onBackClick
                )
                ODSText(
                    text = "App Lock",
                    style = DSTextStyles.subtitle,
                    color = scheme.basicText,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(40.dp)) // Balance for back button
            }

            ODSTabs(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSTabsProps(
                    variant = ODSTabsVariant.FILL,
                    size = ODSTabsSize.SMALL,
                    showDividerFrame = true,
                    tabElements = listOf(
                        ODSTabItemModel(label = "All Apps"),
                        ODSTabItemModel(label = "Locked Apps")
                    )
                ),
                selectedTabIndex = selectedTabIndex,
                onSelectedTabChange = { index ->
                    selectedTabIndex = index
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )

            // Tab Content with Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        AllAppsTab(
                            onAppSelected = { packageName, appName ->
                                if (uiState.isPINSet) {
                                    selectedPackageName = packageName
                                    selectedAppName = appName
                                    viewModel.lockApp(packageName, appName)
                                } else {
                                    showPINSetup = true
                                }
                            },
                            lockedApps = uiState.rules.map { it.packageName }.toSet(),
                            scheme = scheme
                        )
                    }

                    1 -> {
                        if (uiState.rules.isEmpty()) {
                            ODSBox(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSColumn(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            imageVector = Icons.Default.Lock,
                                            tint = scheme.basicTextRecessive,
                                            contentDescription = null
                                        ),
                                        width = 64.dp,
                                        height = 64.dp
                                    )
                                    ODSText(
                                        text = "No locked apps",
                                        style = DSTextStyles.bodyMRegular,
                                        color = scheme.basicTextRecessive,
                                        textAlign = TextAlign.Center
                                    )
                                    ODSText(
                                        text = "Go to 'All Apps' tab to lock an app",
                                        style = DSTextStyles.bodyMBold,
                                        color = scheme.basicTextRecessive,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            ODSLazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                gap = DSVariables.spacingComponent3,
                                padding = ODSPadding(horizontal = DSVariables.spacingComponent3)
                            ) {
                                item {
                                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                                }
                                items(
                                    items = uiState.rules,
                                    key = { it.packageName }
                                ) { rule ->
                                    AppLockCard(
                                        rule = rule,
                                        onRemove = {
                                            viewModel.removeAppLock(rule.packageName)
                                        },
                                        scheme = scheme
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // PIN Setup Bottom Sheet
        if (showPINSetup) {
            PINSetupBottomSheet(
                onDismiss = {
                    showPINSetup = false
                },
                onPINSet = { pin ->
                    viewModel.setPIN(pin)
                    showPINSetup = false
                },
                scheme = scheme
            )
        }
    }
}

@Composable
private fun AllAppsTab(
    onAppSelected: (String, String) -> Unit,
    lockedApps: Set<String>,
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current
    val installedApps = remember { getInstalledApps(context) }
    
    ODSLazyColumn(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        }
        items(
            items = installedApps,
            key = { it.packageName }
        ) { app ->
            val isLocked = lockedApps.contains(app.packageName)
            
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .onClick {
                        onAppSelected.invoke(app.packageName, app.appName)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSListRowStandard(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        variant = ODSListRowStandardVariant.IMAGE,
                        label = app.appName,
                        image = AppImageIcon(appInfo = app.applicationInfo),
                        labelText = if (isLocked) "Locked" else null
                    ),
                )
                if (isLocked) {
                    ODSIcon(
                        modifier = Modifier.weight(0.1f),
                        iconModel = ODSIconModel(
                            imageVector = Icons.Default.Lock,
                            tint = scheme.basicText,
                            contentDescription = "Locked"
                        ),
                        width = 24.dp,
                        height = 24.dp
                    )
                } else {
                    ODSIcon(
                        modifier = Modifier.weight(0.1f),
                        iconModel = ODSIconModel(
                            tint = scheme.basicText,
                            drawableRes = com.telekom.odsystem.R.drawable.right_condensed_type_standard,
                            contentDescription = app.appName
                        )
                    )
                }
            }

            // Add divider between items (not after the last one)
            if (app != installedApps.lastOrNull()) {
                ODSDivider(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSDividerProps(
                        variant = ODSDividerVariant.HORIZONTAL
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))
        }
    }
}

/**
 * Check if USAGE_STATS permission is granted
 */
private fun checkUsageStatsPermission(context: android.content.Context): Boolean {
    val appOps =
        context.getSystemService(android.content.Context.APP_OPS_SERVICE) as android.app.AppOpsManager
    val mode = appOps.checkOpNoThrow(
        android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == android.app.AppOpsManager.MODE_ALLOWED
}

