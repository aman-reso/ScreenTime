package com.app.screentime.reward.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.reward.component.CoinHistoryItem
import com.app.screentime.reward.model.CoinHistoryFilter
import com.app.screentime.reward.viewmodel.CoinHistoryViewModel
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Coin History Screen
 * Displays user's coin transaction history with filters
 */
@Composable
fun CoinHistoryScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    viewModel: CoinHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val filters = listOf(
        CoinHistoryFilter.ALL,
        CoinHistoryFilter.EARNED,
        CoinHistoryFilter.USED,
        CoinHistoryFilter.EXPIRED
    )

    val tabElements = listOf(
        ODSTabItemModel(label = stringResource(R.string.all)),
        ODSTabItemModel(label = stringResource(R.string.earned)),
        ODSTabItemModel(label = stringResource(R.string.used)),
        ODSTabItemModel(label = stringResource(R.string.expired))
    )

    val selectedTabIndex = filters.indexOf(uiState.selectedFilter)

    ODSColumn(
        padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
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
                text = stringResource(R.string.coin_history_title),
                style = com.telekom.odsystem.DSTextStyles.titleS,
                color = scheme.basicText
            )
        }

        ODSTabs(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSTabsProps(
                tabElements = tabElements,
                size = ODSTabsSize.SMALL,
                variant = ODSTabsVariant.FILL,
                showDividerFrame = true
            ),
            selectedTabIndex = selectedTabIndex,
            onSelectedTabChange = { index ->
                viewModel.setFilter(filters[index])
            }
        )

        // Transaction List
        ODSLazyColumn(
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
                    ODSText(
                        text = uiState.error,
                        style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                        color = scheme.functionalDestructiveStandard
                    )
                }
            } else if (uiState.filteredHistory.isEmpty()) {
                item {
                    ODSText(
                        text = stringResource(R.string.no_transactions),
                        style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            } else {
                items(
                    items = uiState.filteredHistory,
                    key = { it.id }
                ) { item ->
                    CoinHistoryItem(
                        coinHistoryItem = item,
                        scheme = scheme
                    )

                    // Add divider between items (not after the last one)
                    if (item != uiState.filteredHistory.lastOrNull()) {
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

