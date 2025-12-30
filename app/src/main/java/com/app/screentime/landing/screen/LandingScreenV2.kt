package com.app.screentime.landing.screen

import android.Manifest
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import android.os.Build
import android.provider.Settings
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import com.app.screentime.R
import com.app.screentime.permission.PermissionManager
import com.app.screentime.permission.PermissionUtils
import com.app.screentime.consent.screen.ConsentBottomSheetContent
import com.app.screentime.battery.component.BatteryHealthSection
import com.app.screentime.network.component.NetworkHealthSection
import com.app.screentime.landing.component.CategoryUsageSection
import com.app.screentime.landing.component.GreetingUi
import com.app.screentime.landing.component.JoinedChallengesCardStack
import com.app.screentime.landing.component.NetworkCard
import com.app.screentime.landing.component.UsageSummaryCard
import com.app.screentime.landing.viewmodel.LandingViewModel
import com.app.screentime.ui.atom.appUsageListUi
import com.app.screentime.ntoificationstack.ManageServiceNotificationStack
import com.app.screentime.ntoificationstack.OAServiceNotificationSingleProps
import com.app.screentime.ntoificationstack.ODSCardNotificationModel
import com.app.screentime.permission.createPermissionManager
import com.app.screentime.ui.theme.headerTheme
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButton
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonProps
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonSize
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonType
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.frogSecondaryScheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme

@Composable
fun LandingScreenV2(
    modifier: Modifier = Modifier,
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToReward: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToSingleAppUsageDetail: (String) -> Unit = {},
    onNavigateToChallengeDetail: (String) -> Unit = {},
    onNavigateToChallenges: () -> Unit = {},
    viewModel: LandingViewModel = hiltViewModel(),
    openSearchScreen: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    val uiProps by viewModel.uiProps.collectAsState()
    val activity = LocalActivity.current ?: return

    // Check if notification permission is denied (reactive)
    var showNotificationWarning by rememberSaveable { mutableStateOf(false) }
    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    // Check notification permission
    val permissionManager = remember {
        if (activity is ComponentActivity) {
            activity.createPermissionManager()
        } else null
    }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            permissionRequested = true
            showNotificationWarning = !isGranted
        }


    val context = LocalContext.current

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat
                .from(context)
                .areNotificationsEnabled()
        } else true
    }

    fun isPermanentlyDenied(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                permissionRequested &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
    }

    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    fun openNotificationSettingsCompat(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        }
        context.startActivity(intent)
    }


    LaunchedEffect(Unit) {
        if (!hasNotificationPermission()) {
            showNotificationWarning = true
        }
    }


    // Request permission function
    val requestPermission: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            if (activity is ComponentActivity) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            }
        }
    }

    // Check permission status on app open and request if needed
    LaunchedEffect(permissionManager, activity) {
        if (activity is ComponentActivity && permissionManager != null) {
            if (PermissionUtils.isNotificationPermissionRequired()) {
                val hasPermission = permissionManager.hasNotificationPermission()

                if (!hasPermission) {
                    // Check if permission was denied (user clicked "Don't allow") or never asked
                    val wasDenied = ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                    if (wasDenied) {
                        showNotificationWarning = true
                    } else {
                        // Permission never asked - request it automatically
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    // Permission granted - no warning needed
                    showNotificationWarning = false
                }
            } else {
                showNotificationWarning = false
            }
        }
    }


    // Dummy preview data for apptime notification stack
    val dummyNotifications = remember {
        listOf(
            ODSCardNotificationModel(
                notificationProps = OAServiceNotificationSingleProps(
                    actionText = "App Usage Alert",
                    titleLabel = "You've spent 2 hours on Instagram today. Consider taking a break!"
                )
            ),
            ODSCardNotificationModel(
                notificationProps = OAServiceNotificationSingleProps(
                    actionText = "App Usage Alert",
                    titleLabel = "You've spent 2 hours on Instagram today. Consider taking a break!"
                )
            ),
            ODSCardNotificationModel(
                notificationProps = OAServiceNotificationSingleProps(
                    actionText = "App Usage Alert",
                    titleLabel = "You've spent 2 hours on Instagram today. Consider taking a break!"
                )
            ),
        )
    }

    // Consent bottom sheet state
    var showConsentSheet by remember { mutableStateOf(false) }

    // Update consent sheet state when uiProps changes
    LaunchedEffect(uiProps?.shouldShowConsent) {
        uiProps?.shouldShowConsent?.let {
            showConsentSheet = it
        }
    }

    // Show consent bottom sheet if not already displayed
    if (showConsentSheet) {
        ConsentBottomSheetContent(
            onDismiss = {
                viewModel.markConsentShown()
                showConsentSheet = false
            },
            onAccept = {
                viewModel.markConsentShown()
                showConsentSheet = false
            }
        )
    }

    ODSBox(modifier = Modifier.fillMaxSize()) {
        ODSColumn(modifier = Modifier.fillMaxSize()) {
            // Status bar padding
            ODSBox(
                modifier = Modifier
                    .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .fillMaxWidth()
            ) {}

            ODSLazyColumn(
                modifier = modifier,
                gap = DSVariables.spacingComponent3,
                padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
            ) {
                item {
                    GreetingUi(
                        username = uiProps?.username,
                        onLeaderboardClick = onNavigateToLeaderboard,
                        onRewardClick = onNavigateToReward,
                        onSearchClick = onNavigateToSearch
                    )
                }
                when {
                    uiProps == null || uiProps!!.isLoading -> {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            ODSBox(
                                modifier = Modifier
                                    .height(40.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSLoadingSpinner(
                                    scheme = scheme, props = ODSLoadingSpinnerProps(
                                        labelText = stringResource(R.string.loading),
                                        size = ODSLoadingSpinnerSize.SMALL,
                                        variant = ODSLoadingSpinnerVariant.STANDARD,
                                        labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                                    )
                                )
                            }
                        }
                    }

                    uiProps!!.error != null -> {
                        item {
                            ODSInlineNotification(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSInlineNotificationProps(
                                    mode = ODSInlineNotificationMode.ERROR,
                                    title = stringResource(R.string.error),
                                    text = uiProps!!.error,
                                    link1Props = ODSLinkProps(label = stringResource(R.string.retry)),
                                    showCloseButton = false
                                ),
                                onFirstLinkClicked = {
                                    viewModel.loadLandingData()
                                },
                                onDismiss = {
                                    viewModel.clearError()
                                })
                        }
                    }

                    uiProps!!.topUsedApps.isEmpty() -> {
                        item {
                            ODSColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
                                ODSText(
                                    text = stringResource(R.string.no_data_available),
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }
                    }

                    else -> {
                        item {
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                        }

//                        // Show apptime notification stack
//                        item {
//                            ManageServiceNotificationStack(
//                                modifier = Modifier.fillMaxWidth(),
//                                scheme = jacuzziSecondaryScheme,
//                                notifications = dummyNotifications,
//                                viewAllText = "View All",
//                                collapseAllText = "Collapse"
//                            )
//                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
//                        }

                        // Show joined challenges CardStack notification if available
                        uiProps?.joinedChallenges?.takeIf { it.isNotEmpty() }?.let { challenges ->
                            item {
                                JoinedChallengesCardStack(
                                    joinedChallenges = challenges,
                                    modifier = Modifier.fillMaxWidth(),
                                    onNavigateToChallengeDetail = onNavigateToChallengeDetail,
                                    onNavigateToChallenges = onNavigateToChallenges,
                                    scheme = jacuzziSecondaryScheme, // Use jacuzzi scheme for notification stack
                                    onDismiss = {
                                        // Dismiss handled by component
                                    }
                                )
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                            }
                        }

                        item {
                            uiProps?.usageDonutData?.let { donutData ->
                                UsageSummaryCard(
                                    todayTotal = donutData.formattedTotalTime,
                                    dailyGoal = "6h",
                                    percentageChange = uiProps!!.percentageChangeFromYesterday,
                                    onClick = onNavigateToStatistics,
                                    scheme = headerTheme.current // Use frog scheme for today's total card
                                )
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                            }
                        }

                        uiProps?.let { it ->
                            item {
                                NetworkCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    wifiDataUsage = it.todayTotalWifiDataUsage,
                                    wifiDataUsageDisplay = it.displayWifiDataUsage,
                                    cellularDataUsage = it.todayTotalMobileDataUsage,
                                    cellularDataUsageDisplay = it.displayMobileDataUsage,
                                    totalDataDisplayName = it.displayTotalDataUsage
                                )
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                            }
                        }

                        item("category_usage") {
                            CategoryUsageSection(
                                categoryUsage = uiProps!!.categoryUsage,
                                scheme = scheme
                            )
                        }

                        item("battery_info") {
                            BatteryHealthSection(
                                scheme = scheme
                            )
                        }

                        item("network_health") {
                            NetworkHealthSection(
                                scheme = scheme
                            )
                        }

                        // Show notification permission warning if denied
                        if (showNotificationWarning) {
                            item("notification_warning") {
                                ODSInlineNotification(
                                    modifier = Modifier.fillMaxWidth(),
                                    scheme = scheme,
                                    props = ODSInlineNotificationProps(
                                        mode = ODSInlineNotificationMode.WARNING,
                                        title = stringResource(R.string.notification_permission_required),
                                        text = stringResource(R.string.notification_permission_warning_message),
                                        link1Props = ODSLinkProps(
                                            label = stringResource(R.string.open_settings)
                                        ),
                                        showCloseButton = false
                                    ),
                                    onFirstLinkClicked = {
                                        if (isPermanentlyDenied()) {
                                            openNotificationSettingsCompat(context)
                                        } else {
                                            requestNotificationPermission()
                                        }
                                    },
                                    onDismiss = {
                                        // Dismiss handled
                                    }
                                )
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                        }
                        item {
                            ODSText(
                                text = stringResource(R.string.usage_detail_insight),
                                style = DSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                        }
                        appUsageListUi(
                            uiProps!!.topUsedApps, scheme = scheme, onClick = { data ->
                                data.packageName?.let { packageName ->
                                    onNavigateToSingleAppUsageDetail(packageName)
                                }
                            })
                    }
                }
            }

        }
    }
}