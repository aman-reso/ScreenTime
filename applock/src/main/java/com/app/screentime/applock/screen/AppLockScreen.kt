package com.app.screentime.applock.screen

import android.graphics.Bitmap
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.applock.component.OverlayPermissionDialog
import com.app.screentime.applock.component.PINBottomSheet
import com.app.screentime.applock.component.PatternBottomSheet
import com.app.screentime.applock.util.PermissionHelper
import com.app.screentime.applock.viewmodel.AppLockViewModel
import com.app.screentime.applock.viewmodel.InstalledApp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.app.screentime.config.R

/**
 * Main App Lock Screen with tabs for All Apps and Locked Apps
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppLockScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    useDarkTheme: Boolean = false
) {
    val viewModel: AppLockViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val coroutineScope = rememberCoroutineScope()

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
    // Permission launchers
    val overlayPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        viewModel.checkPermissions()
    }

    // Check if lock needs to be set first
    var showSetPinBottomSheet by remember { mutableStateOf(false) }
    var showSetPatternBottomSheet by remember { mutableStateOf(false) }
    var showOverlayPermissionDialog by remember { mutableStateOf(false) }
    var shouldShowLockAfterPermission by remember { mutableStateOf(false) }

    // When overlay permission is granted, show PIN setup directly if needed
    LaunchedEffect(uiState.hasOverlayPermission) {
        if (uiState.hasOverlayPermission && shouldShowLockAfterPermission) {
            shouldShowLockAfterPermission = false
            if (!uiState.isPinSet && !uiState.isPatternSet) {
                showSetPinBottomSheet = true
            }
        }
    }

    LaunchedEffect(uiState.isPinSet, uiState.isPatternSet) {
        val isLockSet = uiState.isPinSet || uiState.isPatternSet
        if (!isLockSet && uiState.showPinBottomSheet) {
            showSetPinBottomSheet = true
            viewModel.dismissPinBottomSheet()
        }
    }

    // Pager state for tabs (2 tabs: All, Locked apps)
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabElements = listOf(
        ODSTabItemModel(label = stringResource(R.string.app_lock_all)),
        ODSTabItemModel(label = stringResource(R.string.app_lock_locked_apps))
    )

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .fillMaxWidth()
        ) {}

        ODSPageHeader(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSPageHeaderProps(
                type = ODSPageHeaderType.SUB_PAGE_HEADER
            ),
            actionsSlot = {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Outlined.Search,
                        tint = scheme.basicText,
                        contentDescription = stringResource(R.string.app_lock_search)
                    ), modifier = Modifier.size(24.dp)
                )
            },
            subPageTitleSlot = {
                ODSText(
                    text = stringResource(R.string.app_lock), style = DSTextStyles.bodyL, color = scheme.basicText
                )
            },
            onBackButtonClick = onBackClick
        )

        when {
            uiState.isLoading -> {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier.wrapContentHeight(),
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = stringResource(R.string.app_lock_loading_apps),
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                        )
                    )
                }
            }

            else -> {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSTabs(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSTabsProps(
                            tabElements = tabElements,
                            size = ODSTabsSize.SMALL,
                            variant = ODSTabsVariant.FILL,
                            showDividerFrame = true
                        ),
                        selectedTabIndex = pagerState.currentPage,
                        onSelectedTabChange = { index ->
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        })
                }

                HorizontalPager(
                    state = pagerState, modifier = Modifier.weight(1f), userScrollEnabled = true
                ) { pageIndex ->
                    when (pageIndex) {
                        0 -> AllAppsTab(
                            apps = uiState.installedApps, scheme = scheme, onAppClick = { app ->
                                handleAppLockClick(
                                    app = app,
                                    isPinSet = uiState.isPinSet || uiState.isPatternSet,
                                    hasOverlayPermission = uiState.hasOverlayPermission,
                                    onShowSetPin = { 
                                        if (!uiState.isPinSet && !uiState.isPatternSet) {
                                            showSetPinBottomSheet = true
                                        }
                                    },
                                    onShowOverlayDialog = {
                                        shouldShowLockAfterPermission = !uiState.isPinSet && !uiState.isPatternSet
                                        showOverlayPermissionDialog = true
                                    },
                                    onLockApp = { viewModel.toggleAppLock(app) })
                            })

                        1 -> LockedAppsTab(
                            apps = uiState.lockedApps, scheme = scheme, onAppClick = { app ->
                                viewModel.toggleAppLock(app)
                            })
                    }
                }
            }
        }
    }

    // PIN Bottom Sheet for locking apps
    if (uiState.showPinBottomSheet && uiState.isPinSet) {
        PINBottomSheet(
            showBottomSheet = true,
            isSettingPin = false,
            scheme = scheme,
            onDismiss = { viewModel.dismissPinBottomSheet() },
            onPinEntered = { pin ->
                viewModel.lockAppWithPin(pin)
            })
    }
    
    // Pattern Bottom Sheet for locking apps
    if (uiState.showPinBottomSheet && uiState.isPatternSet) {
        PatternBottomSheet(
            showBottomSheet = true,
            isSettingPattern = false,
            scheme = scheme,
            onDismiss = { viewModel.dismissPinBottomSheet() },
            onPatternEntered = { pattern ->
                viewModel.lockAppWithPattern(pattern)
            })
    }

    // PIN Bottom Sheet for setting initial PIN
    if (showSetPinBottomSheet) {
        PINBottomSheet(showBottomSheet = true, isSettingPin = true, scheme = scheme, onDismiss = {
            showSetPinBottomSheet = false
            shouldShowLockAfterPermission = false
        }, onPinEntered = { pin ->
            viewModel.setPin(pin)
            showSetPinBottomSheet = false
            shouldShowLockAfterPermission = false
        })
    }
    
    // Pattern Bottom Sheet for setting initial Pattern (kept for backward compatibility)
    if (showSetPatternBottomSheet) {
        PatternBottomSheet(
            showBottomSheet = true,
            isSettingPattern = true,
            scheme = scheme,
            onDismiss = {
                showSetPatternBottomSheet = false
                shouldShowLockAfterPermission = false
            },
            onPatternEntered = { pattern ->
                viewModel.setPattern(pattern)
                showSetPatternBottomSheet = false
                shouldShowLockAfterPermission = false
            }
        )
    }

    OverlayPermissionDialog(showDialog = showOverlayPermissionDialog, scheme = scheme, onDismiss = {
        showOverlayPermissionDialog = false
        if (!uiState.hasOverlayPermission) {
            shouldShowLockAfterPermission = false
        }
    }, onAllowClick = {
        if (activity is ComponentActivity) {
            overlayPermissionLauncher.launch(
                PermissionHelper.getOverlayPermissionIntent(context)
            )
        }
    })
}

/**
 * Handle app lock click with permission checks
 * Flow: Check overlay permission first, then lock method (PIN/Pattern), then lock
 */
private fun handleAppLockClick(
    app: InstalledApp,
    isPinSet: Boolean,
    hasOverlayPermission: Boolean,
    onShowSetPin: () -> Unit,
    onShowOverlayDialog: () -> Unit,
    onLockApp: () -> Unit
) {
    // If app is already locked, just unlock (no permission needed)
    if (app.isLocked) {
        onLockApp()
        return
    }

    // Check overlay permission first
    if (!hasOverlayPermission) {
        onShowOverlayDialog()
        return
    }

    // Check if lock method (PIN or Pattern) is set after overlay permission is granted
    if (!isPinSet) {
        onShowSetPin()
        return
    }

    // All checks passed, proceed to lock
    onLockApp()
}

@Composable
private fun AllAppsTab(
    apps: List<InstalledApp>, scheme: ODSTheme, onAppClick: (InstalledApp) -> Unit
) {
    if (apps.isEmpty()) {
        EmptyState(scheme = scheme, message = stringResource(R.string.app_lock_no_apps_found))
        return
    }

    ODSLazyColumn(
        modifier = Modifier.fillMaxSize(),
        gap = DSVariables.spacingComponent3,
        padding = ODSPadding(
            vertical = DSVariables.spacingComponent3, horizontal = DSVariables.spacingComponent4
        )
    ) {
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        }
        itemsIndexed(
            items = apps,
            key = { index, app -> "${app.packageName}_$index" }
        ) { _, app ->
            AppListItem(
                app = app, scheme = scheme, onClick = { onAppClick(app) })
        }
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        }
    }
}

@Composable
private fun LockedAppsTab(
    apps: List<InstalledApp>, scheme: ODSTheme, onAppClick: (InstalledApp) -> Unit
) {
    if (apps.isEmpty()) {
        EmptyState(scheme = scheme, message = stringResource(R.string.app_lock_no_locked_apps))
        return
    }

    ODSLazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2),
        padding = ODSPadding(vertical = DSVariables.spacingComponent2)
    ) {
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        }
        itemsIndexed(
            items = apps,
            key = { index, app -> "${app.packageName}_$index" }
        ) { _, app ->
            AppListItem(
                app = app, scheme = scheme, onClick = { onAppClick(app) })
        }
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        }
    }
}

@Composable
private fun AppListItem(
    app: InstalledApp, scheme: ODSTheme, onClick: () -> Unit
) {
    val context = LocalContext.current
    val packageName = app.packageName

    val bitmapState = produceState<Bitmap?>(initialValue = null, key1 = packageName) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                val drawable = app.applicationInfo.loadIcon(context.packageManager)
                drawable?.toBitmap()
            }.getOrNull()
        }
    }

    val appIcon = bitmapState.value?.let {
        ODSImageModel(bitmap = it)
    }

    val statusText = if (app.isLocked) stringResource(R.string.app_lock_locked) else stringResource(R.string.app_lock_unlocked)
    val lockIcon = if (app.isLocked) {
        Icons.Outlined.Lock
    } else {
        Icons.Outlined.LockOpen
    }
    val lockIconColor = if (app.isLocked) {
        scheme.functionalSuccessStandard
    } else {
        scheme.basicTextRecessive
    }

    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .onClick { onClick() },
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4, vertical = DSVariables.spacingComponent3
        )
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            gap = DSVariables.spacingComponent3
        ) {
            // App icon and info
            ODSRow(
                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // App icon
                if (appIcon != null) {
                    ODSImage(
                        imageModel = appIcon,
                        modifier = Modifier.size(32.dp),
                        width = 32.dp,
                        height = 32.dp
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.size(32.dp),
                        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Filled.Android, tint = scheme.basicTextRecessive
                            ), modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // App name and status
                ODSColumn(
                    modifier = Modifier.weight(1f), gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = app.appName,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = statusText,
                        style = DSTextStyles.oxMicrocopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }

            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = lockIcon, tint = lockIconColor, contentDescription = statusText
                ), modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun EmptyState(
    scheme: ODSTheme, message: String
) {
    ODSBox(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        ODSText(
            text = message, style = DSTextStyles.bodyMRegular, color = scheme.basicTextRecessive
        )
    }
}
