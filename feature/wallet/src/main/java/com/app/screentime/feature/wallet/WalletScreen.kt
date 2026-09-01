package com.app.screentime.feature.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CropFree
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.TransactionType
import com.app.screentime.core.model.WalletTransaction
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onNavigateToTopUp: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showTopUpSheet by remember { mutableStateOf(false) }

    val displayTransactions = remember(uiState.transactions) {
        if (uiState.transactions.isNotEmpty()) {
            uiState.transactions
        } else {
            // Default sample transactions matching reference design
            listOf(
                WalletTransaction(
                    id = "tx-1",
                    amount = 1000.0,
                    type = TransactionType.BONUS,
                    description = "Welcome Bonus",
                    timestamp = System.currentTimeMillis() - 15 * 60 * 1000
                ),
                WalletTransaction(
                    id = "tx-2",
                    amount = 250.0,
                    type = TransactionType.TOPUP,
                    description = "Coin Pack Top-Up",
                    timestamp = System.currentTimeMillis() - 2 * 3600 * 1000
                ),
                WalletTransaction(
                    id = "tx-3",
                    amount = 120.0,
                    type = TransactionType.CALL,
                    description = "Voice Call with Sarah",
                    timestamp = System.currentTimeMillis() - 6 * 3600 * 1000
                ),
                WalletTransaction(
                    id = "tx-4",
                    amount = 50.0,
                    type = TransactionType.CHAT,
                    description = "Chat Session Unlock",
                    timestamp = System.currentTimeMillis() - 24 * 3600 * 1000
                )
            )
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadWallet() },
        modifier = modifier.fillMaxSize()
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        ) {
            // ── Top Navigation Bar (Image 1 style) ──
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Scanner / QR Icon
                ODSBox(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.CropFree),
                        tint = scheme.basicText.getColor()
                    )
                }

                ODSText(
                    text = "Wallet",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                // Right: Notification Bell with Badge (Image 1 style)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    ODSBox(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Outlined.Notifications),
                            tint = scheme.basicText.getColor()
                        )
                    }

                    // Notification Count Tag "2"
                    ODSBox(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .clip(CircleShape),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)), // Cyber Lime
                        padding = ODSPadding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        ODSText(
                            text = "2",
                            style = ODSTextStyles.microcopyBold,
                            color = HexColor(0xff1e1145)
                        )
                    }
                }
            }

            // ── Main Scrollable Content ──
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(key = "hero_balance_card") {
                    HeroBalanceCard(
                        balance = uiState.balance,
                        scheme = scheme,
                        isModel = uiState.isModel,
                        onTopUp = {
                            showTopUpSheet = true
                            onNavigateToTopUp()
                        }
                    )
                }

                // 3. Transactions Section Header
                item(key = "transactions_header") {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = "Transactions",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSBox(
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = 12.dp),
                            padding = ODSPadding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            ODSText(
                                text = "Last 4 days",
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }
                }

                // 4. Transactions List Feed
                items(displayTransactions, key = { it.id }) { tx ->
                    val isHighlighted = tx.id == "tx-3"
                    TransactionCard(
                        tx = tx,
                        scheme = scheme,
                        isHighlighted = isHighlighted
                    )
                }
            }
        }

        if (showTopUpSheet) {
            WalletPacksBottomSheet(
                onDismissRequest = { showTopUpSheet = false },
                scheme = scheme,
                onRechargeSuccess = {
                    showTopUpSheet = false
                    viewModel.loadWallet()
                }
            )
        }
    }
}
