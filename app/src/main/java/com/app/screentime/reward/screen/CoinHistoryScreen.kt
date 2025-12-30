package com.app.screentime.reward.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.Instant
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.reward.component.CoinHistoryItem
import com.app.screentime.reward.model.CoinHistoryFilter
import com.app.screentime.reward.viewmodel.CoinHistoryViewModel
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
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

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
    val activity = LocalActivity.current
    val useDarkTheme = LocalThemeMode.current
    val coroutineScope = rememberCoroutineScope()
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

    val pagerState = androidx.compose.foundation.pager.rememberPagerState(
        initialPage = filters.indexOf(uiState.selectedFilter).coerceAtLeast(0)
    ) { filters.size }

    // Sync ViewModel filter when pager page changes
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setFilter(filters[pagerState.currentPage])
    }

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

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        // Back button and Title row
        ODSRow(
            padding = ODSPadding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent4
            ),
            modifier = Modifier.fillMaxWidth(),
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
                style = com.telekom.odsystem.DSTextStyles.bodyL,
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
            selectedTabIndex = pagerState.currentPage,
            onSelectedTabChange = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = true
        ) { pageIndex ->
            CoinHistoryList(
                filter = filters[pageIndex],
                scheme = scheme,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CoinHistoryList(
    filter: CoinHistoryFilter,
    scheme: ODSTheme,
    viewModel: CoinHistoryViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val filteredHistory = remember(filter, uiState.coinHistory) {
        val now = Instant.now()
        when (filter) {
            CoinHistoryFilter.ALL -> uiState.coinHistory
            CoinHistoryFilter.EARNED -> uiState.coinHistory.filter { it.amount > 0 }
            CoinHistoryFilter.USED -> uiState.coinHistory.filter { it.amount < 0 }
            CoinHistoryFilter.EXPIRED -> {
                uiState.coinHistory.filter { item ->
                    item.expiresAt != null && try {
                        val expiresAt = Instant.parse(item.expiresAt)
                        expiresAt.isBefore(now) && item.amount > 0
                    } catch (e: Exception) {
                        false
                    }
                }
            }
        }
    }

    ODSLazyColumn(
        modifier = Modifier.fillMaxSize(),
        gap = 0.dp,
        padding = ODSPadding(
            top = DSVariables.spacingComponent3,
            horizontal = DSVariables.spacingComponent4
        )
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
                    text = uiState.error ?: "",
                    style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                    color = scheme.functionalDestructiveStandard
                )
            }
        } else if (filteredHistory.isEmpty()) {
            item {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = stringResource(R.string.no_transactions),
                        style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        } else {
            items(
                items = filteredHistory,
                key = { it.id }
            ) { item ->
                CoinHistoryItem(
                    coinHistoryItem = item,
                    scheme = scheme
                )

                if (item != filteredHistory.lastOrNull()) {
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

