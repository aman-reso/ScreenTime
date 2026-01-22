package com.app.screentime.challenge.screen

import android.content.Intent
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Launch
import androidx.compose.material.icons.outlined.Rule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.challenge.component.detail.ChallengeErrorState
import com.app.screentime.challenge.component.detail.ChallengeHeader
import com.app.screentime.challenge.component.detail.ChallengeImageSection
import com.app.screentime.challenge.component.detail.JoinButtonSection
import com.app.screentime.challenge.model.ChallengeDetailUiProps
import com.app.screentime.challenge.viewmodel.ChallengeDetailViewModel
import com.app.screentime.config.R
import com.app.screentime.config.data.Feature
import com.app.screentime.config.featureflag.FeatureFlagHelper
import com.app.screentime.consent.screen.ConsentBottomSheetContent
import com.app.screentime.leaderboard.screen.LeaderboardItem
import com.app.screentime.navigation.ToastSnackbarManager
import com.app.screentime.permission.AppPermissionScreen
import com.app.screentime.permission.createPermissionManager
import com.app.screentime.reward.component.RewardCardV2
import com.app.screentime.ui.atom.AppScreenShimmer
import com.app.screentime.ui.atom.PullToRefreshBox
import com.app.screentime.ui.theme.ColorPalette
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.headerTheme
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import kotlinx.coroutines.launch

/**
 * Format participant count for display (e.g., 2100 -> "2.1K")
 */
private fun formatParticipantCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

/**
 * Challenge Detail Screen using pure ODS components.
 * Displays challenge information, details, rankings, and allows joining.
 *
 * @param challengeId The ID of the challenge to display
 * @param modifier Modifier to be applied to the component
 * @param onBackClick Callback for back navigation
 * @param viewModel ViewModel for challenge data
 * @param scheme ODS theme scheme
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDetailScreen(
    challengeId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: ChallengeDetailViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme,
) {

    val uiState by viewModel.uiState.collectAsState()
    val activity = LocalActivity.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Permission and consent state
    var showPermissionScreen by remember { mutableStateOf(false) }
    var showConsentSheet by remember { mutableStateOf(false) }
    var pendingJoinChallengeId by remember { mutableStateOf<String?>(null) }

    // Permission manager
    val permissionManager = remember {
        if (activity is AppCompatActivity) {
            activity.createPermissionManager()
        } else null
    }

    // Load challenge details when screen opens
    LaunchedEffect(challengeId) {
        viewModel.trackScreenView()
        viewModel.loadChallengeDetails(challengeId)
    }
    val useDarkTheme = LocalThemeMode.current

    // Set edge-to-edge with header scheme color
    if (activity is AppCompatActivity) {
        activity.enableEdgeToEdge(
            statusBarStyle = if (useDarkTheme) {
                SystemBarStyle.dark(scheme.basicBackgroundCard.getIntColor())
            } else {
                SystemBarStyle.light(
                    scheme.basicBackgroundCard.getIntColor(),
                    darkScrim = scheme.basicBackgroundCard.getIntColor()
                )
            }, navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .fillMaxWidth(),
            background = listOf(ODSColorModel(scheme.basicBackgroundCard))
        ) {}

        // Always show header, even when loading
        ChallengeHeaderAndImageSection(
            uiProps = uiState.uiProps,
            challengeId = challengeId,
            onBackClick = onBackClick,
            onShareClick = {
                uiState.uiProps?.let { props ->
                    coroutineScope.launch {
                        viewModel.shareChallenge(
                            challengeId = props.id,
                            title = props.title,
                            prize = props.displayPrize,
                            imageUrl = props.thumbnail,
                            context = context
                        )
                    }
                }
            },
            headerScheme = scheme
        )

        val isRefreshing = uiState.isLoading

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                viewModel.loadChallengeDetails(challengeId)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    ODSLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
                        gap = DSVariables.spacingComponent3
                    ) {
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
                }

                uiState.error != null -> {
                    ChallengeErrorState(
                        message = uiState.error
                            ?: stringResource(R.string.failed_to_load_challenge_details),
                        onRetry = {
                            viewModel.loadChallengeDetails(challengeId)
                        },
                        scheme = scheme
                    )
                }

                uiState.uiProps == null -> {
                    ChallengeErrorState(
                        message = stringResource(R.string.challenge_not_found), onRetry = {
                            viewModel.loadChallengeDetails(challengeId)
                        }, scheme = scheme
                    )
                }

                else -> {
                    if (showPermissionScreen) {
                        AppPermissionScreen(
                            onAllPermissionsGranted = {
                                showPermissionScreen = false
                                // After permission granted, check consent
                                if (!viewModel.hasConsent()) {
                                    showConsentSheet = true
                                } else if (pendingJoinChallengeId != null) {
                                    // All checks passed, proceed with join challenge
                                    viewModel.joinChallenge(
                                        pendingJoinChallengeId!!,
                                        onSuccess = {
                                            viewModel.loadChallengeDetails(pendingJoinChallengeId!!)
                                            pendingJoinChallengeId = null
                                        },
                                        onError = { ToastSnackbarManager.showErrorAsync(it) }
                                    )
                                }
                            },
                            scheme = scheme
                        )
                    }

                    if (showConsentSheet) {
                        val warning = stringResource(R.string.please_allow_consents)
                        ConsentBottomSheetContent(
                            onDismiss = {
                                showConsentSheet = false
                                pendingJoinChallengeId = null
                                coroutineScope.launch {
                                    ToastSnackbarManager.showError(warning)
                                }
                            },
                            onAccept = {
                                showConsentSheet = false
                                if (pendingJoinChallengeId != null) {
                                    viewModel.joinChallenge(
                                        pendingJoinChallengeId!!,
                                        onSuccess = {
                                            viewModel.loadChallengeDetails(pendingJoinChallengeId!!)
                                            pendingJoinChallengeId = null
                                        },
                                        onError = { ToastSnackbarManager.showErrorAsync(it) }
                                    )
                                }
                            }
                        )
                    }

                    ChallengeContent(
                        uiProps = uiState.uiProps!!,
                        isJoining = uiState.isJoining,
                        lastSyncTime = uiState.lastSyncTime,
                        isSyncing = uiState.isSyncing,
                        onRefresh = {
                            viewModel.loadChallengeDetails(challengeId)
                        },
                        onJoinChallenge = {
                            // Check permissions and consent before joining
                            val hasUsageStatsPermission =
                                permissionManager?.hasUsageStatsPermission() ?: false
                            val hasConsent = viewModel.hasConsent()

                            if (!hasUsageStatsPermission) {
                                pendingJoinChallengeId = challengeId
                                showPermissionScreen = true
                            } else if (!hasConsent) {
                                pendingJoinChallengeId = challengeId
                                showConsentSheet = true
                            } else {
                                // All checks passed, proceed with join
                                viewModel.joinChallenge(
                                    challengeId,
                                    onSuccess = { viewModel.loadChallengeDetails(challengeId) },
                                    onError = { ToastSnackbarManager.showErrorAsync(it) }
                                )
                            }
                        },
                        onSyncChallenge = {
                            viewModel.syncChallenge(challengeId, onSuccess = {
                                viewModel.loadChallengeDetails(challengeId)
                            })
                        },
                        onBackClick = onBackClick,
                        viewModel = viewModel,
                        scheme = scheme,
                        headerScheme = scheme
                    )
                }
            }
        }
    }
}


/**
 * Header and Image section using header scheme.
 * Always visible, even when data is loading.
 */
@Composable
private fun ChallengeHeaderAndImageSection(
    uiProps: ChallengeDetailUiProps?,
    challengeId: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    headerScheme: ODSTheme
) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(headerScheme.basicBackground)),
        cornerRadius = ODSCorners(
            bottomLeft = DSVariables.spacingComponent4, bottomRight = DSVariables.spacingComponent4
        )
    ) {
        ODSColumn {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent2
                ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChallengeHeader(
                    title = uiProps?.title ?: stringResource(R.string.challenge),
                    onBackClick = onBackClick,
                    onShareClick = if (uiProps != null) onShareClick else null,
                    scheme = headerScheme
                )
            }
        }
    }
}

/**
 * Main challenge content using ODS components.
 * Uses only UI props - no business logic.
 */
@Composable
internal fun ChallengeContent(
    uiProps: ChallengeDetailUiProps,
    isJoining: Boolean,
    lastSyncTime: String?,
    isSyncing: Boolean,
    onRefresh: () -> Unit,
    onJoinChallenge: () -> Unit,
    onSyncChallenge: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ChallengeDetailViewModel,
    scheme: ODSTheme = neutralScheme,
    headerScheme: ODSTheme = macawSecondaryScheme
) {
    ODSBox(
        modifier = Modifier.fillMaxSize()
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            AboutTab(
                uiProps = uiProps,
                lastSyncTime = lastSyncTime,
                isSyncing = isSyncing,
                onSyncChallenge = onSyncChallenge,
                scheme = scheme,
                headerScheme = headerScheme,
                currentUserId = viewModel.getCurrentUserId(),
                bottomPadding = if (uiProps.showJoinButton) DSVariables.spacingLayout7 else 0.dp
            )
        }

        if (uiProps.showJoinButton) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                background = listOf(ODSColorModel(scheme.basicBackground))
            ) {
                JoinButtonSection(
                    isJoining = isJoining, onJoinClick = onJoinChallenge, scheme = scheme
                )
            }
        }
    }
}

@Composable
private fun AboutTab(
    uiProps: ChallengeDetailUiProps,
    lastSyncTime: String?,
    isSyncing: Boolean,
    onSyncChallenge: () -> Unit,
    scheme: ODSTheme,
    headerScheme: ODSTheme,
    currentUserId: String?,
    bottomPadding: androidx.compose.ui.unit.Dp = 0.dp
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var showRulesBottomSheet by remember { mutableStateOf(false) }

    ODSLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3,
            bottom = bottomPadding
        )
    ) {
        item {
            ChallengeImageSection(
                thumbnail = uiProps.thumbnail
            )
        }
        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent5, top = DSVariables.spacingComponent5
                )
            ) {
                ODSText(
                    text = uiProps.title,
                    style = DSTextStyles.subtitle,
                    color = scheme.basicText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        if (uiProps.tags.isNotEmpty()) {
            item {
                ODSWrap(
                    modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent5
                    )
                ) {
                    uiProps.tags.forEach { tag ->
                        ODSTagStatic(
                            scheme = scheme, props = ODSTagStaticProps(
                                label = tag, type = ODSTagStaticType.STRONG
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = stringResource(R.string.total_prize_pool),
                        labelTextHtml = uiProps.prize,
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = stringResource(R.string.reward),
                        labelText = uiProps.reward,
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = stringResource(R.string.date_range),
                        labelText = uiProps.dateRange,
                        showDescriptionTitle = false,
                        icon = ODSIconModel(
                            imageVector = Icons.Default.DateRange,
                            tint = scheme.basicTextRecessive,
                            contentDescription = stringResource(R.string.date)
                        ),
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        // Last Sync section - only show if user has joined
        if (uiProps.hasJoined) {
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                        vertical = DSVariables.spacingComponent5
                    )
                ) {
                    ODSListRowStandard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (!isSyncing) onSyncChallenge.invoke() else null
                            }
                            .padding(horizontal = DSVariables.spacingComponent5)
                            .semantics(mergeDescendants = true) {},
                        scheme = scheme,
                        props = ODSListRowStandardProps(
                            label = stringResource(R.string.last_sync),
                            labelText = if (isSyncing) stringResource(R.string.syncing) else (lastSyncTime
                                ?: stringResource(R.string.never_synced)),
                            showDescriptionTitle = false,
                            variant = ODSListRowStandardVariant.ICON,
                            icon = ODSIconModel(
                                imageVector = Icons.Default.Refresh,
                                tint = if (isSyncing) scheme.basicTextRecessive else scheme.basicText,
                                contentDescription = stringResource(R.string.sync)
                            ),
                        )
                    )
                }
                ODSDivider(
                    scheme = scheme, props = ODSDividerProps(
                        inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                    )
                )
            }
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = stringResource(R.string.description),
                        labelText = uiProps.description,
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSListRowStandard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent5)
                        .semantics(mergeDescendants = true) {},
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        label = stringResource(R.string.joined_member),
                        labelText = "${formatParticipantCount(uiProps.participantCount)} ${
                            stringResource(
                                R.string.participants
                            )
                        }",
                        showDescriptionTitle = false,
                        variant = ODSListRowStandardVariant.STANDARD
                    ),
                )
            }
            ODSDivider(
                scheme = scheme, props = ODSDividerProps(
                    inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                )
            )
        }

        // App Details section
        if (!uiProps.appDetails.isNullOrEmpty()) {
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                        vertical = DSVariables.spacingComponent5
                    )
                ) {
                    ODSText(
                        text = stringResource(R.string.included_app),
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText,
                        modifier = Modifier.padding(horizontal = DSVariables.spacingComponent5)
                    )
                }
            }

            uiProps.appDetails.forEach { appDetail ->
                item {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                            vertical = DSVariables.spacingComponent2
                        )
                    ) {
                        ODSListRowStandard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        val url =
                                            if (appDetail.url.startsWith("http://") || appDetail.url.startsWith(
                                                    "https://"
                                                )
                                            ) {
                                                appDetail.url
                                            } else {
                                                "https://${appDetail.url}"
                                            }
                                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Handle error if URL cannot be opened
                                    }
                                }
                                .padding(horizontal = DSVariables.spacingComponent5)
                                .semantics(mergeDescendants = true) {},
                            scheme = scheme,
                            props = ODSListRowStandardProps(
                                label = appDetail.appname,
                                showDescriptionTitle = false,
                                variant = ODSListRowStandardVariant.ICON,
                                icon = ODSIconModel(
                                    imageVector = Icons.Outlined.Launch,
                                    tint = scheme.basicTextRecessive,
                                    contentDescription = stringResource(R.string.open_url)
                                ),
                            ),
                        )
                    }
                }
            }
        }

        if (!uiProps.sponsor.isNullOrEmpty()) {
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                        vertical = DSVariables.spacingComponent5
                    )
                ) {
                    ODSListRowStandard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DSVariables.spacingComponent5)
                            .semantics(mergeDescendants = true) {},
                        scheme = scheme,
                        props = ODSListRowStandardProps(
                            label = stringResource(R.string.sponsor),
                            labelText = uiProps.sponsor,
                            showDescriptionTitle = false,
                            variant = ODSListRowStandardVariant.STANDARD
                        ),
                    )
                }
                ODSDivider(
                    scheme = scheme, props = ODSDividerProps(
                        inset = true, spacing = false, variant = ODSDividerVariant.HORIZONTAL
                    )
                )
            }
        }
        if (!uiProps.rules.isNullOrEmpty()) {
            item {
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent7))
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = ODSPadding(all = 0.dp),
                    contentSlot = {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showRulesBottomSheet = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSListRowStandard(
                                modifier = Modifier.weight(0.9f),
                                scheme = scheme,
                                props = ODSListRowStandardProps(
                                    variant = ODSListRowStandardVariant.ICON,
                                    label = stringResource(R.string.rules),
                                    icon = ODSIconModel(
                                        imageVector = Icons.Outlined.Rule,
                                        tint = scheme.basicTextRecessive,
                                        contentDescription = stringResource(R.string.rules)
                                    )
                                )
                            )
                            ODSIcon(
                                modifier = Modifier.weight(0.1f), iconModel = ODSIconModel(
                                    tint = scheme.basicText,
                                    drawableRes = com.telekom.odsystem.R.drawable.right_condensed_type_standard,
                                    contentDescription = stringResource(R.string.view_rules)
                                )
                            )
                        }
                    })
            }
        }

        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(), padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent5,
                    vertical = DSVariables.spacingComponent5
                )
            ) {
                ODSText(
                    text = stringResource(R.string.leaderboard),
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        leaderboardTab(
            uiProps = uiProps,
            scheme = scheme,
            headerScheme = headerScheme,
            currentUserId = currentUserId,
            leaderboardError = uiProps.leaderboardError
        )

        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
        }
    }

    if (!uiProps.rules.isNullOrEmpty()) {
        RulesBottomSheet(
            showBottomSheet = showRulesBottomSheet,
            rules = uiProps.rules,
            onDismiss = { showRulesBottomSheet = false },
            scheme = scheme
        )
    }

}

private fun LazyListScope.leaderboardTab(
    uiProps: ChallengeDetailUiProps,
    scheme: ODSTheme,
    headerScheme: ODSTheme,
    currentUserId: String?,
    leaderboardError: String? = null
) {
    val rank1Scheme = ColorPalette.schemeGet(headerScheme)
    val rank2Scheme = ColorPalette.schemeGet(rank1Scheme)
    val rank3Scheme = ColorPalette.schemeGet(rank2Scheme)

    // Show leaderboard error if present
    if (leaderboardError != null) {
        item {
            ODSInlineNotification(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DSVariables.spacingComponent4),
                scheme = scheme,
                props = ODSInlineNotificationProps(
                    mode = ODSInlineNotificationMode.ERROR,
                    title = stringResource(R.string.error),
                    text = leaderboardError,
                    showCloseButton = false
                )
            )
        }
    }

    if (uiProps.topRankings.isNotEmpty()) {
        uiProps.topRankings.forEachIndexed { index, entry ->
            val itemScheme = when (entry.rank) {
                1 -> rank1Scheme
                2 -> rank2Scheme
                3 -> rank3Scheme
                else -> scheme
            }
            item {
                LeaderboardItem(
                    entry = entry,
                    isCurrentUser = entry.userId == currentUserId,
                    scheme = itemScheme
                )
                if (index < uiProps.topRankings.size - 1) {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                }
            }
        }
    } else if (leaderboardError == null) {
        item {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DSVariables.spacingComponent5),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                gap = DSVariables.spacingComponent3
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.EmojiEvents,
                        tint = scheme.basicTextRecessive,
                        contentDescription = null
                    ), modifier = Modifier.size(64.dp)
                )
                ODSText(
                    text = stringResource(R.string.no_leaderboard_data_available),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

/**
 * Bottom sheet for displaying challenge rules
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RulesBottomSheet(
    showBottomSheet: Boolean, rules: String, onDismiss: () -> Unit, scheme: ODSTheme = neutralScheme
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ODSBottomSheet(
        scheme = scheme,
        props = ODSBottomSheetProps(),
        showBottomSheet = showBottomSheet,
        bottomSheetState = bottomSheetState,
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSText(
                text = stringResource(R.string.rules),
                style = DSTextStyles.titleS,
                color = scheme.basicText
            )
        },
        contentSlot = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DSVariables.spacingComponent4),
                verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
            ) {
                ODSText(
                    text = AnnotatedString.fromHtml(rules),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )
            }
        })
}


