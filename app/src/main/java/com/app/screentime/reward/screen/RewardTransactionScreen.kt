package com.app.screentime.reward.screen

import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.reward.component.RewardTransactionItem
import com.app.screentime.reward.viewmodel.RewardTransactionViewModel
import com.app.screentime.ui.atom.PullToRefreshBox
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * Reward Transaction History Screen
 * Displays user's reward claim transactions
 */
@Composable
fun RewardTransactionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    transactionId: Int? = null,
    scheme: ODSTheme = neutralScheme,
    viewModel: RewardTransactionViewModel = hiltViewModel()
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

    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll to transaction when it's loaded and transactionId is provided
    LaunchedEffect(transactionId, uiState.transactions) {
        if (transactionId != null && uiState.transactions.isNotEmpty() && !uiState.isLoading) {
            val index = uiState.transactions.indexOfFirst { it.id == transactionId }
            if (index >= 0) {
                coroutineScope.launch {
                    listState.animateScrollToItem(index)
                }
            }
        }
    }

    ODSColumn(
        padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}
        
        // Back button and Title row
        ODSRow(
            padding = ODSPadding(vertical = DSVariables.spacingComponent4),
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            gap = DSVariables.spacingComponent3
        ) {
            ODSBox(
                modifier = Modifier
                    .size(24.dp)
                    .customClickable(
                        onClick = onBackClick,
                        isPressed = {}
                    ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = com.telekom.odsystem.R.drawable.left_condensed_type_standard,
                        tint = scheme.basicText,
                        contentDescription = "Back"
                    ),
                    modifier = Modifier.size(24.dp)
                )
            }

            ODSText(
                text = stringResource(R.string.transaction_history),
                style = com.telekom.odsystem.DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
        }

        RewardTransactionPage(
            modifier = Modifier.weight(1f),
            transactionId = transactionId,
            scheme = scheme,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardTransactionPage(
    modifier: Modifier = Modifier,
    transactionId: Int? = null,
    scheme: ODSTheme = neutralScheme,
    viewModel: RewardTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll to transaction when it's loaded and transactionId is provided
    LaunchedEffect(transactionId, uiState.transactions) {
        if (transactionId != null && uiState.transactions.isNotEmpty() && !uiState.isLoading) {
            val index = uiState.transactions.indexOfFirst { it.id == transactionId }
            if (index >= 0) {
                coroutineScope.launch {
                    listState.animateScrollToItem(index)
                }
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refresh() },
        modifier = modifier.fillMaxSize()
    ) {
        ODSBox(
            modifier = Modifier.fillMaxSize(),
            background = listOf(ODSColorModel(scheme.basicBackground))
        ) {
            // Transaction List
            ODSLazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                gap = 0.dp,
                padding = ODSPadding(top = DSVariables.spacingComponent3)
            ) {
            if (uiState.isLoading) {
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSLoadingSpinner(
                            modifier = Modifier.wrapContentHeight(),
                            scheme = scheme,
                            props = ODSLoadingSpinnerProps(
                                labelText = stringResource(R.string.loading),
                                size = ODSLoadingSpinnerSize.SMALL,
                                variant = ODSLoadingSpinnerVariant.STANDARD,
                                labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                            )
                        )
                    }
                }
            } else if (uiState.error != null) {
                item {
                    ODSInlineNotification(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSInlineNotificationProps(
                            mode = ODSInlineNotificationMode.ERROR,
                            title = stringResource(R.string.error),
                            text = uiState.error,
                            link1Props = ODSLinkProps(
                                label = stringResource(R.string.retry),
                                alignment = ODSLinkAlignment.LEFT
                            )
                        ),
                        onFirstLinkClicked = {
                            viewModel.loadTransactions()
                        }
                    )
                }
            } else if (uiState.transactions.isEmpty()) {
                item {
                    ODSText(
                        text = stringResource(R.string.no_transactions_found),
                        style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            } else {
                items(
                    items = uiState.transactions,
                    key = { it.id }
                ) { transaction ->
                    RewardTransactionItem(
                        transaction = transaction,
                        scheme = scheme
                    )

                    // Add divider between items (not after the last one)
                    if (transaction != uiState.transactions.lastOrNull()) {
                        ODSDivider(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSDividerProps(
                                variant = ODSDividerVariant.HORIZONTAL
                            )
                        )
                    }
                }
            }
        }
        }
    }
}

