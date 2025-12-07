package com.app.screentime.reward.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.reward.component.ExpiringPointsBanner
import com.app.screentime.reward.component.PointsHeader
import com.app.screentime.reward.component.RewardCardV1
import com.app.screentime.reward.component.RewardClaimDialog
import com.app.screentime.reward.component.RewardClaimSuccessDialog
import com.app.screentime.reward.component.RewardErrorSnackbar
import com.app.screentime.reward.component.RewardInfoBottomSheet
import com.app.screentime.reward.model.RewardCatalogItem
import com.app.screentime.reward.viewmodel.RewardViewModel
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
import com.telekom.odsystem.tokens.tokens.darkMode

/**
 * Data class for recommended activity (for backward compatibility with dialogs)
 */
private data class RecommendedActivity(
    val title: String,
    val description: String,
    val coin: String
)

/**
 * Reward Screen
 */
@Preview(showBackground = true)
@Composable
fun RewardScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToCoinHistory: () -> Unit = {},
    onNavigateToRewardHistory: (Int?) -> Unit = { },
    scheme: ODSTheme = neutralScheme,
    viewModel: RewardViewModel = hiltViewModel()
) {
    val darkScheme = darkMode

    val uiState by viewModel.uiState.collectAsState()
    var showClaimDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showRewardInfoBottomSheet by remember { mutableStateOf(false) }
    var selectedReward by remember { mutableStateOf<RewardCatalogItem?>(null) }
    var claimTransactionId by remember { mutableStateOf<Int?>(null) }
    var isClaimingReward by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    when {
        uiState.isLoading -> {
            ODSBox(
                modifier = modifier.fillMaxSize(),
                background = listOf(ODSColorModel(darkScheme.basicBackground)),
                contentAlignment = Alignment.Center
            ) {
                ODSLoadingSpinner(
                    modifier = Modifier.wrapContentHeight(),
                    scheme = darkScheme,
                    props = ODSLoadingSpinnerProps(
                        labelText = stringResource(R.string.loading),
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
                background = listOf(ODSColorModel(darkScheme.basicBackground))
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
                    modifier = Modifier
                        .fillMaxSize(),
                    gap = DSVariables.spacingComponent4
                ) {
                    PointsHeader(
                        points = uiState.totalCoins,
                        onBackClick = onBackClick,
                        onInfoClick = onNavigateToCoinHistory,
                        onOrderHistoryClick = {
                            onNavigateToRewardHistory(null)
                        },
                        onCoinHistoryClick = {
                            onNavigateToCoinHistory()
                        },
                        scheme = darkScheme
                    )

                    ExpiringPointsBanner(
                        expiringPoints = 20,
                        onUseClick = {

                        },
                        modifier = Modifier.fillMaxWidth(),
                        scheme = darkScheme
                    )

                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        background = listOf(ODSColorModel(scheme.basicBackground)),
                        cornerRadius = ODSCorners(topLeft = 24.dp, topRight = 24.dp)
                    ) {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            padding = ODSPadding(
                                top = DSVariables.spacingComponent4,
                                horizontal = DSVariables.spacingComponent4
                            ),
                            gap = DSVariables.spacingComponent6
                        ) {
                            // Available Rewards Section
                            ODSColumn(
                                modifier = Modifier.fillMaxWidth(),
                                gap = DSVariables.spacingComponent3
                            ) {
                                ODSText(
                                    text = "Available rewards",
                                    style = com.telekom.odsystem.DSTextStyles.subtitle,
                                    color = scheme.basicText
                                )

                                if (uiState.catalog.isEmpty()) {
                                    ODSText(
                                        text = "No rewards available",
                                        style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                                        color = scheme.basicTextRecessive
                                    )
                                } else {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                                        verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                                        contentPadding = PaddingValues(horizontal = 0.dp)
                                    ) {
                                        items(uiState.catalog) { catalogItem ->
                                            RewardCardV1(
                                                title = catalogItem.title,
                                                imageTag = catalogItem.imageUrl?.let {
                                                    ODSImageModel(
                                                        url = it,
                                                        contentDescription = catalogItem.title
                                                    )
                                                },
                                                coinOrPrice = "${catalogItem.coinPrice} coins",
                                                actionText = if (catalogItem.isActive && catalogItem.stockQuantity > 0) "Claim" else null,
                                                onActionClick = {
                                                    selectedReward = catalogItem
                                                    showRewardInfoBottomSheet = true
                                                },
                                                onClick = {
                                                    selectedReward = catalogItem
                                                    showRewardInfoBottomSheet = true
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                scheme = scheme
                                            )
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
            title = "We have a cash reward for you!",
            description = "Click the button below to claim your ${activity.coinPrice} Leaderboard bonus",
            coin = activity.coinPrice.toString(),
            onConfirmClick = {
                showClaimDialog = false
                showSuccessDialog = true
            },
            scheme = scheme
        )
    }

    RewardClaimSuccessDialog(
        showDialog = showSuccessDialog,
        onDismiss = {
            showSuccessDialog = false
            selectedReward = null
        },
        onKeepTradingClick = {
            onNavigateToRewardHistory(claimTransactionId)
            claimTransactionId = null
        },
        scheme = scheme
    )

    RewardInfoBottomSheet(
        showBottomSheet = showRewardInfoBottomSheet,
        onDismiss = {
            showRewardInfoBottomSheet = false
            selectedReward = null
        },
        reward = selectedReward,
        isLoading = isClaimingReward,
        onClaimClick = { name, email, phone, address, postalCode ->
            selectedReward?.let { reward ->
                isClaimingReward = true
                viewModel.claimReward(
                    rewardCatalogId = reward.id,
                    recipientName = name,
                    recipientPhone = phone,
                    shippingAddress = address,
                    postalCode = postalCode,
                    onSuccess = { transactionId ->
                        isClaimingReward = false
                        claimTransactionId = transactionId
                        showRewardInfoBottomSheet = false
                        selectedReward = null
                        showSuccessDialog = true
                    },
                    onError = { errorMsg ->
                        isClaimingReward = false
                        errorMessage = errorMsg
                    }
                )
            }
        },
        scheme = scheme
    )
}
