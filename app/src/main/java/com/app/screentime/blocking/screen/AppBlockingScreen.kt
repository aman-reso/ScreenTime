package com.app.screentime.blocking.screen

import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.app.screentime.blocking.component.AddBlockingRuleBottomSheet
import com.app.screentime.blocking.component.BlockingRuleCard
import com.app.screentime.blocking.component.PermissionScreenContent
import com.app.screentime.blocking.model.BlockingRule
import com.app.screentime.blocking.viewmodel.AppBlockingViewModel
import com.app.screentime.ui.atom.AppImageIcon

import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBlockingScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: AppBlockingViewModel = hiltViewModel(),
    onNavigateToAppSelection: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Check permissions
    var hasAccessibilityPermission by remember {
        mutableStateOf(
            isAccessibilityServiceEnabled(
                context
            )
        )
    }
    var hasOverlayPermission by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

    // Re-check permissions when screen is focused
    LaunchedEffect(Unit) {
        hasAccessibilityPermission = isAccessibilityServiceEnabled(context)
        hasOverlayPermission = Settings.canDrawOverlays(context)
    }

    // Re-check permissions when lifecycle resumes (user returns from settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasAccessibilityPermission = isAccessibilityServiceEnabled(context)
                hasOverlayPermission = Settings.canDrawOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val allPermissionsGranted = hasAccessibilityPermission || hasOverlayPermission

    if (!allPermissionsGranted) {
        PermissionScreenContent(
            modifier = modifier,
            hasAccessibilityPermission = hasAccessibilityPermission,
            hasOverlayPermission = hasOverlayPermission,
            onContinue = {

            },
            scheme = scheme,
            onAccessibilityPermissionClick = {
                try {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        "Please enable accessibility service in Accessibility Settings",
                        Toast.LENGTH_LONG
                    ).show()
                    // Re-check permission periodically when user might return
                    coroutineScope.launch {
                        repeat(10) {
                            delay(1000)
                            val newStatus = isAccessibilityServiceEnabled(context)
                            if (newStatus != hasAccessibilityPermission) {
                                hasAccessibilityPermission = newStatus
                                return@launch
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context, "Error opening Accessibility Settings", Toast.LENGTH_SHORT
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
            })
        return
    }

    // Show app blocking screen if permissions granted
    val uiState by viewModel.uiState.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)

    var showAddBlockDialog by remember { mutableStateOf(false) }
    var selectedPackageName by remember { mutableStateOf<String?>(null) }
    var selectedAppName by remember { mutableStateOf<String?>(null) }

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

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = ODSPadding(horizontal = 8.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navController != null) {
                    ODSButton(
                        scheme = scheme, props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = scheme.basicText,
                                contentDescription = "Back"
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL
                        ), onClick = { navController.popBackStack() })
                }
                ODSText(
                    text = "App blocking",
                    style = DSTextStyles.subtitle,
                    color = scheme.basicText,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(40.dp)) // Balance for back button
            }

            ODSTabs(
                modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSTabsProps(
                    variant = ODSTabsVariant.FILL,
                    size = ODSTabsSize.SMALL,
                    showDividerFrame = true,
                    tabElements = listOf(
                        ODSTabItemModel(label = "All Apps"), ODSTabItemModel(label = "Blocked")
                    )
                ), selectedTabIndex = selectedTabIndex, onSelectedTabChange = { index ->
                    selectedTabIndex = index
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })

            // Tab Content with Pager
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        AllAppsTab(
                            onAppSelected = { packageName, appName ->
                                selectedPackageName = packageName
                                selectedAppName = appName
                                showAddBlockDialog = true
                            }, scheme = scheme
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
                                            imageVector = Icons.Default.Block,
                                            tint = scheme.basicTextRecessive,
                                            contentDescription = null
                                        ), width = 64.dp, height = 64.dp
                                    )
                                    ODSText(
                                        text = "No blocking rules",
                                        style = DSTextStyles.bodyMRegular,
                                        color = scheme.basicTextRecessive,
                                        textAlign = TextAlign.Center
                                    )
                                    ODSText(
                                        text = "Go to 'All Apps' tab to add a blocking rule",
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
                                    items = uiState.rules, key = { rule ->
                                        when (rule) {
                                            is BlockingRule.InstantBlock -> rule.packageName
                                            is BlockingRule.LaunchBasedBlock -> rule.packageName
                                            is BlockingRule.DurationBasedBlock -> rule.packageName
                                        }
                                    }) { rule ->
                                    BlockingRuleCard(
                                        rule = rule, onRemove = {
                                            val packageName = when (rule) {
                                                is BlockingRule.InstantBlock -> rule.packageName
                                                is BlockingRule.LaunchBasedBlock -> rule.packageName
                                                is BlockingRule.DurationBasedBlock -> rule.packageName
                                            }
                                            viewModel.removeBlockingRule(packageName)
                                        }, scheme = scheme
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Block Bottom Sheet
        if (showAddBlockDialog && selectedPackageName != null && selectedAppName != null) {
            AddBlockingRuleBottomSheet(
                selectedAppName = selectedAppName!!,
                selectedPackageName = selectedPackageName!!,
                onDismiss = {
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                },
                onBlockInstantly = { packageName, appName ->
                    viewModel.blockAppInstantly(packageName, appName)
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                },
                onBlockAfterLaunches = { packageName, appName, maxLaunches ->
                    viewModel.blockAppAfterLaunches(packageName, appName, maxLaunches)
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                },
                onBlockAfterDuration = { packageName, appName, maxDurationMinutes ->
                    viewModel.blockAppAfterDuration(packageName, appName, maxDurationMinutes)
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                },
                scheme = scheme
            )
        }
    }
}


@Composable
private fun AllAppsTab(
    onAppSelected: (String, String) -> Unit, scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current
    val installedApps = remember { com.app.screentime.blocking.component.getInstalledApps(context) }
    ODSLazyColumn(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
        }
        items(
            items = installedApps, key = { it.packageName }) { app ->
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .onClick {
                        onAppSelected.invoke(app.appName, app.packageName)
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                ODSListRowStandard(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        variant = ODSListRowStandardVariant.IMAGE,
                        label = app.appName,
                        image = AppImageIcon(appInfo = app.applicationInfo)
                    ),
                )
                ODSIcon(
                    modifier = Modifier.weight(0.1f), iconModel = ODSIconModel(
                        tint = scheme.basicText,
                        drawableRes = com.telekom.odsystem.R.drawable.right_condensed_type_standard,
                        contentDescription = app.appName
                    )
                )
            }

            // Add divider between items (not after the last one)
            if (app != installedApps.lastOrNull()) {
                ODSDivider(
                    modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSDividerProps(
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
 * Check if accessibility service is enabled
 */
private fun isAccessibilityServiceEnabled(context: android.content.Context): Boolean {
    val accessibilityManager =
        context.getSystemService(android.content.Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices =
        accessibilityManager.getEnabledAccessibilityServiceList(android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

    val serviceClassName = com.app.screentime.service.AppAccessibilityService::class.java.name
    val packageName = context.packageName

    // Check if our service is in the enabled services list
    // The service info name is the class name, and we need to match it with the package
    return enabledServices.any { serviceInfo ->
        val serviceInfoName = serviceInfo.resolveInfo.serviceInfo.name
        val servicePackageName = serviceInfo.resolveInfo.serviceInfo.packageName

        // Match by package name and class name
        servicePackageName == packageName && serviceInfoName == serviceClassName
    }
}


