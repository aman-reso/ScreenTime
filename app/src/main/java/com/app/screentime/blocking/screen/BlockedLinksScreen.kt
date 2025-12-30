package com.app.screentime.blocking.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
// import android.content.Intent
// import android.net.VpnService
import androidx.compose.ui.res.stringResource
// import androidx.core.content.ContextCompat
import com.app.screentime.R
// import com.app.screentime.service.ScreenTimeVpnService
// import com.app.screentime.service.VpnPermissionManager
import com.app.screentime.blocking.viewmodel.BlockedLinksViewModel
// import com.app.screentime.blocking.component.VpnPermissionScreenContent
// import com.app.screentime.blocking.component.PermissionCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.atoms.switch.ODSSwitchSize
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
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedLinksScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: BlockedLinksViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val activity = LocalActivity.current
    val useDarkTheme = LocalThemeMode.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var websiteUrl by remember { mutableStateOf("") }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Check VPN permission - VPN feature disabled
    // val vpnPermissionManager = remember { VpnPermissionManager(context) }
    // var hasVpnPermission by remember { mutableStateOf(vpnPermissionManager.hasVpnPermission()) }
    var hasVpnPermission by remember { mutableStateOf(false) }

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

    // VPN Permission Launcher - VPN feature disabled
    /*
    val vpnLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // After returning from VPN permission dialog, check if service can be started
        hasVpnPermission = vpnPermissionManager.hasVpnPermission()
        val intent = VpnService.prepare(context)
        if (intent == null) {
            // Permission granted, start service
            val serviceIntent = Intent(context, ScreenTimeVpnService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
            viewModel.checkVpnStatus(context)
        }
    }
    */

    LaunchedEffect(Unit) {
        viewModel.loadBlockedLinks()
        // VPN feature disabled
        // hasVpnPermission = vpnPermissionManager.hasVpnPermission()
        // while (true) {
        //     hasVpnPermission = vpnPermissionManager.hasVpnPermission()
        //     viewModel.checkVpnStatus(context)
        //     delay(2000)
        // }
        viewModel.checkVpnStatus(context)
    }

    // Re-check permission when screen resumes - VPN feature disabled
    /*
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasVpnPermission = vpnPermissionManager.hasVpnPermission()
                viewModel.checkVpnStatus(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    */

    // Show permission screen if VPN permission is not granted - VPN feature disabled
    /*
    if (!hasVpnPermission) {
        VpnPermissionScreenContent(
            modifier = modifier,
            hasVpnPermission = hasVpnPermission,
            onVpnPermissionClick = {
                val intent = VpnService.prepare(context)
                if (intent != null) {
                    vpnLauncher.launch(intent)
                }
            },
            onBackClick = onBackClick,
            scheme = scheme
        )
        return
    }
    */

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {

        }
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick, modifier = Modifier.size(44.dp)
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        tint = scheme.basicText,
                        contentDescription = "Back"
                    ), width = 24.dp, height = 24.dp
                )
            }

            ODSText(
                text = "Blocked Sites",
                style = DSTextStyles.bodyL,
                color = scheme.basicText,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = DSVariables.spacingComponent2),
                textAlign = TextAlign.Center
            )

            // Show add button - VPN feature disabled, always show
            // if (hasVpnPermission) {
                IconButton(
                    onClick = { showAddBottomSheet = true },
                    modifier = Modifier.size(44.dp)
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Default.Add,
                            tint = scheme.basicText,
                            contentDescription = "Add blocked site"
                        ),
                        width = 24.dp,
                        height = 24.dp
                    )
                }
            // }
        }

        ODSLazyColumn(
            modifier = Modifier.fillMaxSize(),
            padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
            gap = DSVariables.spacingComponent2
        ) {
            // VPN permission UI - Commented out VPN feature
            /*
            if (!hasVpnPermission) {
                item {
                    ODSBox(
                        modifier = Modifier.padding(top = DSVariables.spacingComponent4)
                    ) {
                        PermissionCard(
                            title = stringResource(R.string.permission_vpn_title),
                            description = stringResource(R.string.permission_vpn_description),
                            isGranted = hasVpnPermission,
                            icon = ODSIconModel(imageVector = Icons.Default.Security),
                            onClick = {
                                val intent = VpnService.prepare(context)
                                if (intent != null) {
                                    vpnLauncher.launch(intent)
                                }
                            },
                            scheme = scheme
                        )
                    }
                }

                // Request Permission Button
                item {
                    ODSButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = DSVariables.spacingComponent4,
                                vertical = DSVariables.spacingComponent2
                            ),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Request VPN Permission",
                            size = ODSButtonSize.SMALL,
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = {
                            val intent = VpnService.prepare(context)
                            if (intent != null) {
                                vpnLauncher.launch(intent)
                            }
                        }
                    )
                }
            }

            if (!hasVpnPermission) {
                item {
                    ODSBox(
                        modifier = Modifier.padding(top = DSVariables.spacingComponent4)
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth(),
                            background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = 16.dp),
                            padding = ODSPadding(all = DSVariables.spacingComponent4)
                        ) {
                            ODSColumn(gap = DSVariables.spacingComponent2) {
                                ODSText(
                                    text = "What VPN Does",
                                    style = DSTextStyles.titleS,
                                    color = scheme.basicText
                                )
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
                                ODSText(
                                    text = "• Blocks distracting websites you add to the list",
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                                ODSText(
                                    text = "• Filters network traffic to prevent access to blocked sites",
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                                ODSText(
                                    text = "• Works across all browsers and apps",
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                                ODSText(
                                    text = "• Tracks how many times each site was blocked",
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                                ODSText(
                                    text = "• Helps you stay focused and productive",
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }
                    }
                }
            }
            */

            // VPN Toggle - Commented out VPN feature
            /*
            if (hasVpnPermission) {
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = DSVariables.spacingComponent2)
                    ) {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DSVariables.spacingComponent4),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSSwitch(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSSwitchProps(
                                    label = if (uiState.isVpnRunning) "VPN Enabled" else "VPN Disabled",
                                    selected = uiState.isVpnRunning,
                                    size = ODSSwitchSize.SMALL
                                ),
                                onCheckedChange = { isChecked ->
                                    // VPN feature disabled
                                    // if (isChecked) {
                                    //     val intent = VpnService.prepare(context)
                                    //     if (intent != null) {
                                    //         vpnLauncher.launch(intent)
                                    //     } else {
                                    //         val serviceIntent =
                                    //             Intent(context, ScreenTimeVpnService::class.java)
                                    //         ContextCompat.startForegroundService(
                                    //             context,
                                    //             serviceIntent
                                    //         )
                                    //         viewModel.checkVpnStatus(context)
                                    //     }
                                    // } else {
                                    //     val stopIntent =
                                    //         Intent(context, ScreenTimeVpnService::class.java)
                                    //     stopIntent.putExtra("stop", true)
                                    //     ContextCompat.startForegroundService(context, stopIntent)
                                    //     viewModel.checkVpnStatus(context)
                                    // }
                                }
                            )
                        }
                    }
                    ODSDivider(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                    )
                }
            }
            */

            // Content
            if (uiState.isLoading) {
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "Loading...",
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            } else if (uiState.blockedLinks.isEmpty()) {
                // Spacer to push content to center
                item {
                    Spacer(modifier = Modifier.height(200.dp))
                }

                // Center content - "No sites blocked yet"
                item {
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            gap = DSVariables.spacingComponent4
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Block,
                                    tint = scheme.basicTextRecessive,
                                    contentDescription = "No blocked sites"
                                ),
                                width = 64.dp,
                                height = 64.dp
                            )
                            ODSText(
                                text = "No sites blocked yet",
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }
                }

                // Spacer to push button to bottom
                item {
                    Spacer(modifier = Modifier.height(200.dp))
                }

                // Bottom button - "Add URL or Domain" - VPN feature disabled, always show
                // if (hasVpnPermission) {
                    item {
                        ODSButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = DSVariables.spacingComponent4,
                                    vertical = DSVariables.spacingComponent4
                                ),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Add URL or Domain",
                                size = ODSButtonSize.SMALL,
                                variant = ODSButtonVariant.SECONDARY
                            ),
                            onClick = {
                                showAddBottomSheet = true
                            }
                        )
                    }
                // }
            } else {
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = DSVariables.spacingComponent4)
                    ) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ODSColumn {
                                ODSText(
                                    text = "Total Sites",
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ODSText(
                                    text = "${uiState.blockedLinks.size}",
                                    style = DSTextStyles.titleS,
                                    color = scheme.basicText
                                )
                            }
                            ODSColumn(horizontalAlignment = Alignment.End) {
                                ODSText(
                                    text = "Total Blocks",
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                ODSText(
                                    text = "${uiState.totalBlockCount}",
                                    style = DSTextStyles.titleS,
                                    color = scheme.basicText
                                )
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

                items(
                    items = uiState.blockedLinks,
                    key = { it.id }
                ) { link ->
                    // ... Item content
                    ODSRow(modifier = Modifier.fillMaxWidth()) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ODSListRowStandard(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.deleteBlockedLink(link.id)
                                    },
                                scheme = scheme,
                                props = ODSListRowStandardProps(
                                    variant = ODSListRowStandardVariant.STANDARD,
                                    label = link.link,
                                    labelText = "Blocked ${link.blockedCount} times"
                                )
                            )

                            ODSButton(
                                scheme = scheme,
                                props = ODSButtonProps(
                                    buttonIcon = ODSIconModel(
                                        imageVector = Icons.Default.Delete,
                                        tint = scheme.functionalDestructiveStandard,
                                        contentDescription = "Delete"
                                    ),
                                    buttonType = ODSButtonButtonType.ICON_ONLY,
                                    size = ODSButtonSize.SMALL,
                                    variant = ODSButtonVariant.GHOST
                                ),
                                onClick = {
                                    viewModel.deleteBlockedLink(link.id)
                                }
                            )
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

                item {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))
                }
            }
        }
    }

    // Add Blocked Site Bottom Sheet
    if (showAddBottomSheet) {
        ODSBottomSheet(
            scheme = scheme,
            props = ODSBottomSheetProps(),
            showBottomSheet = showAddBottomSheet,
            bottomSheetState = bottomSheetState,
            onDismissRequest = {
                showAddBottomSheet = false
                websiteUrl = ""
            },
            onCloseClicked = {
                showAddBottomSheet = false
                websiteUrl = ""
            },
            titleSlot = {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent4)
                ) {
                    ODSText(
                        text = "Add Blocked Site",
                        style = DSTextStyles.bodyL,
                        color = scheme.basicText
                    )
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
                    ODSText(
                        text = "Enter a website URL or domain to block",
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            },
            contentSlot = {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent4)
                ) {
                    ODSTextField(
                        scheme = scheme,
                        props = ODSTextFieldProps(
                            label = "Website URL",
                            inputText = websiteUrl,
                            size = ODSTextFieldSize.SMALL,
                            placeholderText = "example.com or https://example.com"
                        ),
                        onValueChange = { websiteUrl = it }
                    )
                }
            },
            actionSlot = {
                ODSButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent4),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Add",
                        size = ODSButtonSize.SMALL,
                        variant = ODSButtonVariant.PRIMARY,
                        disabled = websiteUrl.isBlank()
                    ),
                    onClick = {
                        if (websiteUrl.isNotBlank()) {
                            viewModel.addBlockedLink(websiteUrl.trim())
                            showAddBottomSheet = false
                            websiteUrl = ""
                        }
                    }
                )
            }
        )
    }
}
