package com.app.screentime.feature.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
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

    val welcomeBonusDesc = stringResource(R.string.wallet_welcome_bonus)
    val coinPackTopUpDesc = stringResource(R.string.wallet_coin_pack_top_up)
    val voiceCallDesc = stringResource(R.string.wallet_sample_voice_call)
    val chatDesc = stringResource(R.string.wallet_sample_chat)

    val displayTransactions = remember(
        uiState.transactions,
        welcomeBonusDesc,
        coinPackTopUpDesc,
        voiceCallDesc,
        chatDesc
    ) {
        uiState.transactions
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
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ODSText(
                    text = "Wallet",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 12.dp),
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
                    }
                }

                items(displayTransactions, key = { it.id }) { tx ->
                    val isHighlighted = tx.id == "tx-3"
                    TransactionCard(
                        tx = tx,
                        scheme = scheme,
                        isHighlighted = isHighlighted
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent9))
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
