package com.app.screentime.feature.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onNavigateToTopUp: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PompiereTitle(
                text = "Wallet & Coins",
                scheme = scheme,
                style = ODSTextStyles.pompiereHeader
            )
            IconButton(onClick = {}) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.History),
                    tint = scheme.basicText.getColor()
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BalanceCard(balance = uiState.balance, scheme = scheme, onTopUp = onNavigateToTopUp)
            }

            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 12.dp
                ) {
                    MiniStatCard(
                        value = "${uiState.totalSpent.toInt()}",
                        label = "Coins Spent",
                        icon = Icons.Outlined.TrendingDown,
                        iconTint = scheme.functionalDestructiveStandard,
                        scheme = scheme,
                        modifier = Modifier.weight(1f)
                    )
                    MiniStatCard(
                        value = "${uiState.balance.toInt()}",
                        label = "Available Coins",
                        icon = Icons.Outlined.TrendingUp,
                        iconTint = scheme.functionalSuccessStandard,
                        scheme = scheme,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PompiereTitle(
                        text = "Transaction History",
                        scheme = scheme,
                        style = ODSTextStyles.pompiereTitleM
                    )
                    ODSText(
                        text = "${uiState.transactions.size} records",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }

            if (uiState.transactions.isEmpty()) {
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "No transactions yet. Add coins to get started!",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            } else {
                items(uiState.transactions, key = { it.id }) { tx ->
                    TransactionCard(tx = tx, scheme = scheme)
                }
            }

            item { Spacer(Modifier.navigationBarsPadding()) }
        }
    }
}
