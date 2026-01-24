package com.app.screentime.notifications.screen

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues

import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.service.NotificationHistoryListener
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.database.entity.CapturedNotificationEntity
import com.app.screentime.notifications.viewmodel.CapturedNotificationsViewModel
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
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControls
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.app.screentime.permission.component.bottombar.BottomBar
import com.app.screentime.permission.component.bottombar.BottomBarProps
import com.app.screentime.permission.component.herosection.HeroSection
import com.app.screentime.permission.component.herosection.HeroSectionProps
import com.app.screentime.permission.component.infocard.InfoCard
import com.app.screentime.permission.component.infocard.InfoCardProps
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.slots.bottomsheettitlelabel.ODSBottomSheetTitleLabel
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.ads.BannerAd
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.rememberBannerAd
import com.app.screentime.profile.screen.isNotificationListenerEnabled
import com.app.screentime.profile.screen.openNotificationAccessSettings
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapturedNotificationsScreen(
    viewModel: CapturedNotificationsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {

    val activity = LocalActivity.current
    // Get theme mode for status bar styling
    val useDarkTheme = LocalThemeMode.current
    SideEffect {
        if (activity is AppCompatActivity) {
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
    val notifications by viewModel.notifications.collectAsState()
    val allAppNames by viewModel.allAppNames.collectAsState()
    val selectedPackageName by viewModel.selectedPackageName.collectAsState()

    var showFilterBottomSheet by remember { mutableStateOf(false) }
    var notificationToDelete by remember { mutableStateOf<CapturedNotificationEntity?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Permission state
    var hasNotificationListenerPermission by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Check permission on launch
    LaunchedEffect(Unit) {
        hasNotificationListenerPermission = isNotificationListenerEnabled(context)
    }

    // Re-check permission when screen resumes (e.g., after returning from settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasNotificationListenerPermission = isNotificationListenerEnabled(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Re-check permission after returning from settings
        hasNotificationListenerPermission = isNotificationListenerEnabled(context)
    }

    // Handle Allow button click
    val handleAllowClick = {
        openNotificationAccessSettings(context, permissionLauncher)
    }

    // Banner ad at bottom - remember once to prevent reload
    val bannerAd = rememberBannerAd(adUnitId = AdConfig.getBannerAdUnitId())

    ODSColumn(
        modifier = Modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        ODSPageHeader(
            scheme = scheme, props = ODSPageHeaderProps(
                type = ODSPageHeaderType.SUB_PAGE_HEADER
            ), subPageTitleSlot = {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.recovered_notification),
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    textAlign = TextAlign.Center
                )
            }, actionsSlot = {
                if (hasNotificationListenerPermission) {
                    ODSButton(
                        scheme = scheme, props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                imageVector = Icons.Default.FilterList,
                                tint = scheme.basicText,
                                contentDescription = stringResource(R.string.filter)
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL
                        ), onClick = { showFilterBottomSheet = true })
                }
            }, onBackButtonClick = onBackClick
        )

        // Content with sticky banner ad at bottom
        ODSBox(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            background = listOf(ODSColorModel(scheme.basicBackground))
        ) {
            // Show permission card if permission not granted
            if (!hasNotificationListenerPermission) {
                NotificationPermissionCard(
                    scheme = scheme,
                    onAllowClick = handleAllowClick
                )
            } else {
                // Content
                when {
                    notifications.isEmpty() -> {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = if (bannerAd != null) 60.dp else 0.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                gap = DSVariables.spacingComponent3
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        imageVector = Icons.Filled.Notifications,
                                        tint = scheme.basicTextRecessive,
                                        contentDescription = null
                                    ), modifier = Modifier.size(64.dp)
                                )
                                ODSText(
                                    text = if (selectedPackageName != null) {
                                        stringResource(R.string.no_notifications_found_for_app)
                                    } else {
                                        stringResource(R.string.no_notifications_captured_yet)
                                    },
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicTextRecessive,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    else -> {
                        ODSLazyColumn(
                            background = listOf(ODSColorModel(scheme.basicBackground)),
                            modifier = Modifier.fillMaxSize(),
                            padding = ODSPadding(
                                horizontal = DSVariables.spacingComponent4,
                                vertical = DSVariables.spacingComponent3,
                                bottom = if (bannerAd != null) 60.dp else DSVariables.spacingComponent3
                            ),
                            gap = DSVariables.spacingComponent3
                        ) {
                            items(notifications) { notification ->
                                NotificationItem(
                                    notification = notification,
                                    viewModel = viewModel,
                                    onDeleteClick = { notificationToDelete = notification },
                                    scheme = scheme
                                )
                            }
                        }
                    }
                }
            }

            if (hasNotificationListenerPermission && bannerAd != null) {
                val (adView, adState) = bannerAd
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    BannerAd(
                        adView = adView,
                        adState = adState,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Filter Bottom Sheet
        ODSBottomSheet(
            scheme = scheme,
            props = ODSBottomSheetProps(),
            showBottomSheet = showFilterBottomSheet,
            bottomSheetState = bottomSheetState,
            onDismissRequest = { showFilterBottomSheet = false },
            onCloseClicked = { showFilterBottomSheet = false },
            titleSlot = {
                ODSBottomSheetHeader(
                    props = ODSBottomSheetHeaderProps(
                        size = ODSBottomSheetHeaderSize.SMALL,
                        smallHeading = stringResource(R.string.choose_app)
                    )
                )
            },
            contentSlot = {
                FilterBottomSheetContent(
                    appNames = allAppNames,
                    selectedPackageName = selectedPackageName,
                    onPackageSelected = { packageName ->
                        viewModel.setFilter(packageName)
                        showFilterBottomSheet = false
                    },
                    onClearFilter = {
                        viewModel.clearFilter()
                        showFilterBottomSheet = false
                    },
                    scheme = scheme
                )
            })

        // Delete Confirmation Dialog
        DeleteConfirmationDialog(
            showDialog = notificationToDelete != null,
            notificationTitle = notificationToDelete?.title
                ?: stringResource(R.string.notification),
            onDismiss = { notificationToDelete = null },
            onConfirm = {
                notificationToDelete?.let { notification ->
                    viewModel.deleteNotification(notification)
                    notificationToDelete = null
                }
            },
            scheme = scheme
        )
    }
}

@Composable
private fun FilterBottomSheetContent(
    appNames: List<com.app.screentime.notifications.viewmodel.AppNameInfo>,
    selectedPackageName: String?,
    onPackageSelected: (String) -> Unit,
    onClearFilter: () -> Unit,
    scheme: ODSTheme
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(), gap = DSVariables.spacingComponent0
    ) {
        // All notifications option with radio icon on right
        ODSListRowControls(
            modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSListRowControlsProps(
                variant = ODSListRowControlsVariant.STANDARD,
                type = ODSControlsType.RADIO_ICON,
                labelText = stringResource(R.string.all_notifications),
                selected = selectedPackageName == null
            ), onRadioClick = onClearFilter
        )

        ODSDivider(
            modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSDividerProps(
                inset = true, variant = ODSDividerVariant.HORIZONTAL
            )
        )

        // App name list with radio icons on right
        if (appNames.isEmpty()) {
            ODSText(
                text = stringResource(R.string.no_apps_found),
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = DSVariables.spacingComponent4)
            )
        } else {
            appNames.forEachIndexed { index, appInfo ->
                ODSListRowControls(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSListRowControlsProps(
                        variant = ODSListRowControlsVariant.STANDARD,
                        type = ODSControlsType.RADIO_ICON,
                        labelText = appInfo.appName,
                        selected = selectedPackageName == appInfo.packageName
                    ),
                    onRadioClick = { onPackageSelected(appInfo.packageName) })

                // Add divider between items (not after last item)
                if (index < appNames.size - 1) {
                    ODSDivider(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSDividerProps(
                            inset = true, variant = ODSDividerVariant.HORIZONTAL
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: CapturedNotificationEntity,
    viewModel: CapturedNotificationsViewModel,
    onDeleteClick: () -> Unit,
    scheme: ODSTheme
) {
    ODSCardBasic(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        contentPadding = ODSPadding(all = DSVariables.spacingComponent3),
        props = ODSCardBasicProps(isHorizontal = false),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(), gap = DSVariables.spacingComponent2
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = viewModel.getAppName(notification.packageName),
                        style = DSTextStyles.bodySRegular,
                        color = scheme.functionalSuccessStandard
                    )
                    ODSRow(
                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = formatTimestamp(notification.timestamp),
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                        // Delete icon
                        ODSBox(
                            modifier = Modifier
                                .size(24.dp)
                                .customClickable(
                                    onClick = onDeleteClick, isPressed = {}),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Delete,
                                    tint = scheme.functionalDestructiveStandard,
                                    contentDescription = stringResource(R.string.delete)
                                ), modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Title
                if (!notification.title.isNullOrBlank()) {
                    ODSText(
                        text = notification.title,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText
                    )
                }

                // Text content
                if (!notification.text.isNullOrBlank()) {
                    ODSText(
                        text = notification.text,
                        style = DSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        })
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

/**
 * Check if notification listener service is enabled
 */
private fun isNotificationListenerEnabled(context: Context): Boolean {
    val packageName = context.packageName
    val flat = Settings.Secure.getString(
        context.contentResolver, "enabled_notification_listeners"
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

/**
 * Open notification access settings
 */
private fun openNotificationAccessSettings(
    context: Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    try {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS).apply {
                putExtra(
                    Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME, ComponentName(
                        context, NotificationHistoryListener::class.java
                    ).flattenToString()
                )
            }
        } else {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launcher.launch(intent)
    } catch (e: Exception) {
        // Handle error
    }
}

/**
 * Permission card component for notification access
 */
@Composable
private fun NotificationPermissionCard(
    scheme: ODSTheme,
    onAllowClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    ODSBox(modifier = Modifier.fillMaxSize(), clipContent = true) {
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
                    props = HeroSectionProps(
                        title = stringResource(R.string.notification_permission_required)
                    ),
                    scrollState = scrollState
                )

                ODSBox(height = DSVariables.spacingLayout4) {}

                InfoCard(
                    scheme = scheme,
                    props = InfoCardProps(
                        title = stringResource(R.string.notification_permission_required),
                        description = stringResource(R.string.enable_notification_access_recovery)
                    )
                )
            }
        }

        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            scheme = scheme,
            props = BottomBarProps.default(context),
            onAllowClick = onAllowClick
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    showDialog: Boolean,
    notificationTitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    scheme: ODSTheme
) {
    if (showDialog) {
        ODSDialog(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            scheme = scheme,
            onDismissRequest = onDismiss,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            props = ODSDialogProps(
                showCloseButton = true,
                showScrollbar = false,
                title = notificationTitle,
                bodyText = null
            ),
            contentSlot = {
                ODSText(
                    text = stringResource(R.string.delete_notification_confirmation),
                    style = DSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            actionSlot = {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.delete),
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            onConfirm()
                            onDismiss()
                        }
                    )
                }
            }
        )
    }
}
