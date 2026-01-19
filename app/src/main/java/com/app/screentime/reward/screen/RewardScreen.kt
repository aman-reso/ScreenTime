package com.app.screentime.reward.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import com.app.screentime.ads.RewardedAdManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.data.Feature
import com.app.screentime.config.featureflag.FeatureFlagHelper
import com.app.screentime.reward.component.PointsHeader
import com.app.screentime.reward.component.RewardCardV1
import com.app.screentime.reward.component.RewardClaimDialog
import com.app.screentime.reward.component.RewardClaimSuccessDialog
import com.app.screentime.reward.component.RewardErrorSnackbar
import com.app.screentime.reward.component.RewardInfoBottomSheet
import com.app.screentime.reward.model.RewardCatalogItem
import com.app.screentime.reward.viewmodel.RewardViewModel
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.headerTheme
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.flyoutmenu.ODSFlyoutMenu
import com.telekom.odsystem.molecules.flyoutmenu.ODSFlyoutMenuButtonProps
import com.telekom.odsystem.molecules.flyoutmenu.ODSFlyoutMenuMenuSize
import com.telekom.odsystem.molecules.flyoutmenu.ODSFlyoutMenuOptions
import com.telekom.odsystem.molecules.flyoutmenu.ODSFlyoutMenuProps
import com.app.screentime.ui.atom.PullToRefreshBox
import kotlin.math.absoluteValue
import com.app.screentime.config.R as ConfigR

/**
 * Data class for recommended activity (for backward compatibility with dialogs)
 */

/**
 * Reward Screen
 */


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun RewardScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToCoinHistory: () -> Unit = {},
    onNavigateToRewardHistory: (Int?) -> Unit = { },
    scheme: ODSTheme = neutralScheme,
    headerScheme: ODSTheme = headerTheme.current,
    viewModel: RewardViewModel = hiltViewModel()
) {

    val activity = LocalActivity.current
    // Get theme mode for status bar styling
    val useDarkTheme = LocalThemeMode.current
    SideEffect {
        if (activity is ComponentActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(headerScheme.basicBackgroundCard.getIntColor())
                } else {
                    SystemBarStyle.light(
                        headerScheme.basicBackgroundCard.getIntColor(),
                        darkScrim = headerScheme.basicBackgroundCard.getIntColor()
                    )
                }, navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT, Color.TRANSPARENT
                )
            )
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    
    // Reload data when screen is opened
    LaunchedEffect(Unit) {
        viewModel.loadRewardData()
    }
    
    var showClaimDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showRewardInfoBottomSheet by remember { mutableStateOf(false) }
    var selectedReward by remember { mutableStateOf<RewardCatalogItem?>(null) }
    var claimTransactionId by remember { mutableStateOf<Int?>(null) }
    var isClaimingReward by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var showAdRewardSuccess by remember { mutableStateOf(false) }
    var earnedCoinsFromAd by remember { mutableStateOf(0) }
    var showWatchAdSection by remember { mutableStateOf(true) }
    var isAdLoading by remember { mutableStateOf(false) }
    var adError by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    var expandedHeaderHeightPx by remember { mutableIntStateOf(0) }

    var lastScrollOffset by remember { mutableIntStateOf(0) }
    var lastFirstVisibleItem by remember { mutableIntStateOf(0) }

    var headerOffsetPx by remember { mutableFloatStateOf(0f) }

    val collapseFraction by remember {
        derivedStateOf {
            if (expandedHeaderHeightPx == 0) return@derivedStateOf 0f
            headerOffsetPx / expandedHeaderHeightPx
        }
    }


    val headerHeightDp by remember {
        derivedStateOf {
            with(density) {
                (expandedHeaderHeightPx * (1f - collapseFraction)).toDp()
            }
        }
    }



    // Reset header height measurement when watch ad section visibility changes
    LaunchedEffect(showWatchAdSection) {
        expandedHeaderHeightPx = 0
        headerOffsetPx = 0f
        lastScrollOffset = 0
        lastFirstVisibleItem = 0
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->

            val delta = when {
                index == lastFirstVisibleItem -> {
                    (offset - lastScrollOffset).toFloat()
                }

                index > lastFirstVisibleItem -> {
                    // Scrolled down to next item
                    offset.toFloat()
                }

                else -> {
                    // Scrolled up to previous item
                    -lastScrollOffset.toFloat()
                }
            }

            headerOffsetPx = (headerOffsetPx + delta).coerceIn(0f, expandedHeaderHeightPx.toFloat())

            lastFirstVisibleItem = index
            lastScrollOffset = offset
        }
    }





    ODSColumn(modifier = Modifier.fillMaxSize()) {
        // Status bar padding with header scheme
        ODSBox(
            modifier = Modifier
                .height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .fillMaxWidth(),
            background = listOf(ODSColorModel(headerScheme.basicBackgroundCard))
        ) {}

        when {
            uiState.isLoading -> {
                ODSBox(
                    modifier = modifier.fillMaxSize(),
                    background = listOf(ODSColorModel(scheme.basicBackground)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier.wrapContentHeight(),
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = stringResource(com.app.screentime.config.R.string.loading),
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                        )
                    )
                }
            }

            else -> {
                ODSBox(
                    modifier = modifier.fillMaxSize(),
                    background = listOf(ODSColorModel(scheme.basicBackground))
                ) {
                    RewardErrorSnackbar(
                        message = errorMessage,
                        onDismiss = {
                            errorMessage = null
                        },
                        scheme = scheme,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                    )
                    ODSColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            background = listOf(ODSColorModel(headerScheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(
                                bottomLeft = DSVariables.spacingComponent4,
                                bottomRight = DSVariables.spacingComponent4
                            )
                        ) {
                            ODSColumn {
                                ODSRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    padding = ODSPadding(
                                        horizontal = DSVariables.spacingComponent3,
                                        vertical = DSVariables.spacingComponent3
                                    ),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ODSButton(
                                        scheme = headerScheme, props = ODSButtonProps(
                                            buttonIcon = ODSIconModel(
                                                drawableRes = R.drawable.left_condensed_type_standard_size_standard,
                                                tint = headerScheme.basicText,
                                                contentDescription = stringResource(ConfigR.string.back)
                                            ),
                                            buttonType = ODSButtonButtonType.ICON_ONLY,
                                            variant = ODSButtonVariant.GHOST,
                                            size = ODSButtonSize.SMALL
                                        ), onClick = onBackClick
                                    )

                                    ODSText(
                                        text = stringResource(ConfigR.string.reward),
                                        style = DSTextStyles.bodyMBold,
                                        color = headerScheme.basicText,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )

                                    ODSFlyoutMenu(
                                        scheme = headerScheme,
                                        props = ODSFlyoutMenuProps(
                                            expanded = isMenuExpanded,
                                            menuSize = ODSFlyoutMenuMenuSize.SMALL,
                                            buttonProps = ODSFlyoutMenuButtonProps(
                                                buttonIcon = ODSIconModel(
                                                    drawableRes = R.drawable.menu_type_standard_size_standard,
                                                    tint = headerScheme.basicText,
                                                    contentDescription = stringResource(ConfigR.string.menu)
                                                ),
                                                variant = ODSButtonVariant.GHOST,
                                                size = ODSButtonSize.SMALL
                                            ),
                                            options = listOf(
                                                ODSFlyoutMenuOptions(label = stringResource(ConfigR.string.coin_history)),
                                                ODSFlyoutMenuOptions(label = stringResource(ConfigR.string.order_history))
                                            )
                                        ),
                                        onClick = { isMenuExpanded = !isMenuExpanded },
                                        onDismissRequest = { isMenuExpanded = false },
                                        onMenuListItemClicked = { index ->
                                            isMenuExpanded = false
                                            when (index) {
                                                0 -> onNavigateToCoinHistory()
                                                1 -> onNavigateToRewardHistory(null)
                                            }
                                        })
                                }

                                ODSBox(
                                    modifier = Modifier
                                    .fillMaxWidth()
                                    .onSizeChanged { size ->
                                            val heightDiff = if (expandedHeaderHeightPx > 0) {
                                                (expandedHeaderHeightPx - size.height).absoluteValue
                                            } else {
                                                Int.MAX_VALUE // Force update on first measurement
                                            }
                                            // Update height if initial measurement or if height changed significantly
                                            if (expandedHeaderHeightPx == 0 || heightDiff > 10) {
                                                val wasInitial = expandedHeaderHeightPx == 0
                                            expandedHeaderHeightPx = size.height
                                                if (!wasInitial) {
                                                    headerOffsetPx = 0f
                                                }
                                        }
                                    }
                                    .then(
                                        if (expandedHeaderHeightPx > 0) {
                                            Modifier.height(headerHeightDp)
                                        } else {
                                            Modifier // Let it measure naturally first
                                        }
                                    )
                                    .graphicsLayer {
                                        alpha = 1f - collapseFraction
                                    }) {
                                    PointsHeader(
                                        points = uiState.totalCoins,
                                        coinHistory = uiState.coinHistory,
                                        onInfoClick = {
                                            onNavigateToCoinHistory()
                                        },
                                        scheme = headerScheme,
                                        modifier = Modifier.fillMaxWidth(),
                                        showWatchAdSection = showWatchAdSection,
                                        isAdLoading = isAdLoading,
                                        adError = adError,
                                        onWatchAdClick = {
                                            activity?.let {
                                                isAdLoading = true
                                                adError = null
                                                RewardedAdManager.showRewardedAd(
                                                    activity = it,
                                                    onRewardEarned = { rewardItem ->
                                                        earnedCoinsFromAd = rewardItem.amount
                                                        // Add coins via API
                                                        viewModel.addCoinsForAdWatch(
                                                            onSuccess = {
                                                                isAdLoading = false
                                                                showAdRewardSuccess = true
                                                                // Hide the section after successful coin addition
                                                                showWatchAdSection = false
                                                            },
                                                            onError = { errorMsg ->
                                                                isAdLoading = false
                                                                adError = errorMsg
                                                                showAdRewardSuccess = false
                                                            }
                                                        )
                                                    },
                                                    onAdDismissed = {
                                                        // If ad was dismissed without reward, reset loading state
                                                        if (isAdLoading) {
                                                            isAdLoading = false
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        // Rewards List Section
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            background = listOf(ODSColorModel(scheme.basicBackground))
                        ) {
                            val isRefreshing = uiState.isLoading

                            PullToRefreshBox(
                                isRefreshing = isRefreshing,
                                onRefresh = {
                                    viewModel.loadRewardData()
                                },
                                modifier = Modifier.fillMaxSize()
                        ) {
                            ODSLazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                padding = ODSPadding(
                                    top = DSVariables.spacingComponent4,
                                    horizontal = DSVariables.spacingComponent4
                                ),
                                gap = DSVariables.spacingComponent3
                            ) {

                                item {
                                    ODSText(
                                            text = stringResource(ConfigR.string.available_rewards),
                                        style = DSTextStyles.bodyMBold,
                                        color = scheme.basicText
                                    )
                                }

                                if (uiState.catalogPairs.isEmpty()) {
                                    item {
                                        ODSText(
                                                text = stringResource(ConfigR.string.no_rewards_available),
                                            style = DSTextStyles.bodyMRegular,
                                            color = scheme.basicTextRecessive
                                        )
                                    }
                                } else {
                                    items(
                                        items = uiState.catalogPairs,
                                        key = { pair ->
                                            pair.firstOrNull()?.id?.toString() ?: ""
                                        }) { pair ->
                                        ODSRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            gap = DSVariables.spacingComponent3
                                        ) {
                                            pair.forEach { catalogItem ->
                                                RewardCardV1(
                                                    onClick = {
                                                        selectedReward = catalogItem
                                                        showRewardInfoBottomSheet = true
                                                    },
                                                    title = catalogItem.title,
                                                    imageTag = catalogItem.imageUrl?.let {
                                                        ODSImageModel(
                                                            url = it,
                                                            contentDescription = catalogItem.title
                                                        )
                                                    },
                                                        coinOrPrice = "${catalogItem.coinPrice} ${stringResource(ConfigR.string.coins)}",
                                                        actionText = if (catalogItem.isActive && catalogItem.stockQuantity > 0) stringResource(ConfigR.string.claim) else null,
                                                    onActionClick = {
                                                        selectedReward = catalogItem
                                                        showRewardInfoBottomSheet = true
                                                    },
                                                    modifier = Modifier.weight(1f),
                                                    scheme = scheme
                                                )
                                            }
                                            // Add spacer if odd number of items
                                            if (pair.size == 1) {
                                                Spacer(modifier = Modifier.weight(1f))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedReward?.let { activity ->
        RewardClaimDialog(
            showDialog = showClaimDialog,
            onDismiss = {
                showClaimDialog = false
                selectedReward = null
            },
            title = stringResource(ConfigR.string.we_have_cash_reward),
            description = stringResource(ConfigR.string.claim_reward_description, activity.coinPrice),
            coin = activity.coinPrice.toString(),
            onConfirmClick = {
                showClaimDialog = false
                showSuccessDialog = true
            },
            scheme = scheme
        )
    }

    RewardClaimSuccessDialog(
        showDialog = showSuccessDialog, onDismiss = {
            showSuccessDialog = false
            selectedReward = null
        }, onKeepTradingClick = {
            onNavigateToRewardHistory(claimTransactionId)
            claimTransactionId = null
        }, scheme = scheme
    )

    // Ad Reward Success Dialog
    RewardClaimSuccessDialog(
        showDialog = showAdRewardSuccess,
        onDismiss = {
            showAdRewardSuccess = false
            earnedCoinsFromAd = 0
        },
        title = stringResource(ConfigR.string.coins_earned),
        description = stringResource(ConfigR.string.coins_earned_description, earnedCoinsFromAd),
        onKeepTradingClick = {
            showAdRewardSuccess = false
            earnedCoinsFromAd = 0
        },
        scheme = scheme
    )

    // Reload saved details when bottom sheet opens
    val savedClaimDetails = remember(showRewardInfoBottomSheet) {
        if (showRewardInfoBottomSheet) {
            viewModel.getSavedClaimDetails()
        } else {
            null
        }
    }

    RewardInfoBottomSheet(
        showBottomSheet = showRewardInfoBottomSheet,
        onDismiss = {
            showRewardInfoBottomSheet = false
            selectedReward = null
            errorMessage = null
        },
        reward = selectedReward,
        isLoading = isClaimingReward,
        errorMessage = errorMessage,
        savedClaimDetails = savedClaimDetails,
        onClaimClick = { name, email, phone, address, postalCode, saveDetails ->
            selectedReward?.let { reward ->
                isClaimingReward = true
                errorMessage = null
                viewModel.claimReward(
                    rewardCatalogId = reward.id,
                    recipientName = name,
                    recipientEmail = email,
                    recipientPhone = phone,
                    shippingAddress = address,
                    postalCode = postalCode,
                    saveDetails = saveDetails,
                    onSuccess = { transactionId ->
                        isClaimingReward = false
                        claimTransactionId = transactionId
                        showRewardInfoBottomSheet = false
                        selectedReward = null
                        errorMessage = null
                        showSuccessDialog = true
                    },
                    onError = { errorMsg ->
                        isClaimingReward = false
                        errorMessage = errorMsg
                    })
            }
        },
        scheme = scheme
    )
}
