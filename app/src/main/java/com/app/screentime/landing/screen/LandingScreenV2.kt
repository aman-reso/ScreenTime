package com.app.screentime.landing.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.BannerAd
import com.app.screentime.ads.rememberBannerAd
import com.app.screentime.battery.component.BatteryHealthSection
import com.app.screentime.consent.screen.ConsentBottomSheetContent
import com.app.screentime.landing.component.DailyGoalBottomSheet
import com.app.screentime.landing.component.GreetingUi
import com.app.screentime.landing.component.quickActionsSection
import com.app.screentime.landing.component.JoinedChallengesCardStack
import com.app.screentime.profile.screen.FeedbackBottomSheetContent
import com.app.screentime.landing.component.NetworkCard
import com.app.screentime.landing.component.UsageSummaryCard
import com.app.screentime.landing.component.YearlyUsageStatsCard
import com.app.screentime.landing.component.rememberQuickActions
import com.app.screentime.landing.viewmodel.LandingViewModel
import com.app.screentime.network.component.NetworkHealthSection
import com.app.screentime.permission.PermissionUtils
import com.app.screentime.permission.createPermissionManager
import com.app.screentime.ui.atom.AppScreenShimmer
import com.app.screentime.ui.atom.PullToRefreshBox
import com.app.screentime.ui.theme.headerTheme
import com.app.screentime.utils.CountryUtils
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
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme


@OptIn(ExperimentalMaterial3Api::class)
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
    onNavigateToControlCenter: () -> Unit = {},
    onNavigateToManageLocation: () -> Unit = {},
    onNavigateToRecoverNotification: () -> Unit = {},
    onNavigateToAppLock: () -> Unit = {},
    onNavigateToFileManager: () -> Unit = {},
    onNavigateToWallpaper: () -> Unit = {},
    onNavigateToCustomisation: () -> Unit = {},
    viewModel: LandingViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val uiProps by viewModel.uiProps.collectAsState()
    val dailyGoalHours by viewModel.dailyGoalHours.collectAsState()
    val formattedDailyGoal = viewModel.getFormattedDailyGoal()
    val activity = LocalActivity.current ?: return

    val bannerAd = rememberBannerAd(
        adUnitId = AdConfig.getBannerAdUnitId(),
    )
    var showDailyGoalBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showFeedbackBottomSheet by rememberSaveable { mutableStateOf(false) }

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

    val quickActions = rememberQuickActions(
        onNavigateToControlCenter,
        onNavigateToManageLocation,
        onNavigateToRecoverNotification,
        onNavigateToAppLock,
        onNavigateToFileManager,
        onNavigateToWallpaper
    )

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
            ODSBox(
                modifier = Modifier
                    .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .fillMaxWidth()
            ) {}

            PullToRefreshBox(
                isRefreshing = uiProps?.isLoading == true,
                onRefresh = {
                    viewModel.loadLandingData()
                },
                modifier = Modifier.fillMaxSize()
            )
            {
                ODSLazyColumn(
                    modifier = modifier,
                    gap = DSVariables.spacingComponent3,
                    padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
                ) {
                    item {
                        GreetingUi(
                            username = uiProps?.username,
                            onLeaderboardClick = {
                                viewModel.trackLeaderboardClick()
                                onNavigateToLeaderboard()
                            },
                            onRewardClick = {
                                viewModel.trackRewardClick()
                                onNavigateToReward()
                            },
                            onSearchClick = {
                                viewModel.trackSearchClick()
                                onNavigateToSearch()
                            }
                        )
                    }
                    when {
                        uiProps == null || uiProps!!.isLoading -> {
                            item {
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                            }
                            item {
                                AppScreenShimmer(
                                    modifier = Modifier.fillMaxWidth(),
                                    scheme = scheme
                                )
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
                                        link1Props = ODSLinkProps(
                                            label = stringResource(R.string.retry),
                                            alignment = ODSLinkAlignment.LEFT
                                        ),
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

                            // Show joined challenges CardStack notification if available
                            uiProps?.joinedChallenges?.takeIf { it.isNotEmpty() }
                                ?.let { challenges ->
                                    item("joined_challenge") {
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

                            item("usage_card") {
                                uiProps?.usageDonutData?.let { donutData ->
                                    UsageSummaryCard(
                                        todayTotal = donutData.formattedTotalTime,
                                        dailyGoal = formattedDailyGoal,
                                        notificationCount = uiProps!!.totalNotificationCount.takeIf { it > 0 },
                                        percentageChange = uiProps!!.percentageChangeFromYesterday,
                                        onClick = onNavigateToStatistics,
                                        onEditDailyGoal = { showDailyGoalBottomSheet = true },
                                        scheme = headerTheme.current
                                    )
                                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                                }
                            }

                            // Yearly Usage Stats Card
                            if (uiProps?.yearlyUsageDays != null && uiProps!!.yearlyUsageDays > 0) {
                                item("yearly_stats_card") {
                                    YearlyUsageStatsCard(
                                        dailyScreenTime = uiProps!!.dailyScreenTimeFormatted,
                                        yearlyScreenTime = uiProps!!.yearlyScreenTimeFormatted,
                                        customColor = uiProps!!.customColorOption,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = onNavigateToCustomisation
                                    )
                                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                                }
                            }

                            if (CountryUtils.isUserInIndia(context)) {
                                if (!uiProps?.challengeBannerURL.isNullOrEmpty()) {
                                    item("challenge_banner") {
                                        ODSBox(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onNavigateToChallenges.invoke()
                                                },
                                            cornerRadius = ODSCorners(
                                                DSVariables.spacingComponent4
                                            ),
                                            clipContent = true,
                                        ) {
                                            ODSImage(
                                                height = DSVariables.sizingComponent18 + DSVariables.spacingLayout7,
                                                modifier = Modifier.fillMaxWidth(),
                                                imageModel = ODSImageModel(
                                                    url = uiProps?.challengeBannerURL!!,
                                                    contentDescription = "Challenge Banner"
                                                ),
                                                contentScale = ContentScale.FillWidth
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                                    }
                                } else {
                                    item("challenge_banner") {
                                        ODSBox(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onNavigateToChallenges.invoke()
                                                },
                                            cornerRadius = ODSCorners(
                                                DSVariables.spacingComponent4
                                            ),
                                            clipContent = true,
                                        ) {
                                            ODSImage(
                                                height = DSVariables.sizingComponent18 + DSVariables.spacingLayout7,
                                                modifier = Modifier.fillMaxWidth(),
                                                imageModel = ODSImageModel(
                                                    drawableRes = com.app.screentime.R.drawable.challengeimage,
                                                    contentDescription = "Challenge Banner"
                                                ),
                                                contentScale = ContentScale.FillWidth
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                                    }
                                }
                            }

                            uiProps?.let {
                                item("network_card") {
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

                            quickActionsSection(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                quickActions = quickActions
                            )

                            item("feedback_button") {
                                ODSCardBasic(
                                    modifier = Modifier.fillMaxWidth(),
                                    scheme = scheme,
                                    contentSlot = {
                                        ODSRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent4)
                                        ) {
                                            ODSIcon(
                                                iconModel = com.telekom.odsystem.atoms.icon.ODSIconModel(
                                                    imageVector = Icons.Outlined.RateReview,
                                                    tint = scheme.basicText,
                                                    contentDescription = stringResource(R.string.feedback)
                                                ),
                                                width = DSVariables.spacingComponent6,
                                                height = DSVariables.spacingComponent6
                                            )
                                            ODSText(
                                                text = stringResource(R.string.feedback),
                                                style = DSTextStyles.bodyMBold,
                                                color = scheme.basicText
                                            )
                                        }
                                    },
                                    onClick = { showFeedbackBottomSheet = true },
                                )
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
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

                            // Banner Ad
                            item {
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                            }
                            if (AdConfig.areAdsEnabled()) {
                                bannerAd?.let { (adView, adState) ->
                                    item(key = "banner_ad") {
                                        BannerAd(
                                            adView = adView,
                                            adState = adState
                                        )
                                    }
                                }
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

                        }
                    }
                }

            }
        }
    }
    // Daily Goal Bottom Sheet
    DailyGoalBottomSheet(
        showBottomSheet = showDailyGoalBottomSheet,
        currentGoalHours = dailyGoalHours,
        onDismiss = { showDailyGoalBottomSheet = false },
        onSave = { hours ->
            viewModel.saveDailyGoal(hours)
        },
        scheme = neutralScheme
    )

    // Feedback Bottom Sheet
    if (showFeedbackBottomSheet) {
        FeedbackBottomSheetContent(
            onDismiss = { showFeedbackBottomSheet = false },
            scheme = scheme
        )
    }
}