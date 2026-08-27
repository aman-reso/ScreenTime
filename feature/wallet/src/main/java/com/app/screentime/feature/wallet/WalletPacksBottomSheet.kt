package com.app.screentime.feature.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun WalletPacksBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onRechargeSuccess: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.loadWallet()
        viewModel.loadPacks()
    }

    LaunchedEffect(uiState.rechargeSuccess) {
        if (uiState.rechargeSuccess) {
            viewModel.resetRechargeStatus()
            onRechargeSuccess()
            onDismissRequest()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = scheme.basicBackground.getColor(),
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PompiereTitle(
                        text = "Add Coins",
                        scheme = scheme,
                        style = ODSTextStyles.pompiereHeader
                    )
                    ODSBox(
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                        cornerRadius = ODSCorners(all = 12.dp),
                        padding = ODSPadding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        ODSText(
                            text = "✦ ${uiState.balance.toInt()} coins",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicText
                        )
                    }
                }
            }

            // Info Banner
            item {
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                    cornerRadius = ODSCorners(all = 16.dp),
                    padding = ODSPadding(all = 16.dp)
                ) {
                    ODSColumn(gap = 4.dp) {
                        ODSText(
                            text = "✦ Coins never expire",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "Use for instant voice calls & private chat messages",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }

            // Subtitle
            item {
                PompiereTitle(
                    text = "Select a Coin Pack",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereTitleM
                )
            }

            // Interactive Grid in Pairs
            if (uiState.packs.isEmpty() && uiState.isLoading) {
                item {
                    ODSBox(
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = scheme.basicText.getColor())
                    }
                }
            } else {
                items(uiState.packs.chunked(2), key = { it.firstOrNull()?.id ?: "" }) { row ->
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 12.dp
                    ) {
                        row.forEach { pack ->
                            PackageCard(
                                pack = pack,
                                isSelected = uiState.selectedPack?.id == pack.id,
                                scheme = scheme,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.selectPack(pack) }
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            // Checkout Footer
            item {
                val pack = uiState.selectedPack
                if (pack != null) {
                    ODSColumn(gap = 8.dp, modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)) {
                        ODSText(
                            text = "You get ${pack.total_coins} coins for ₹${pack.price_inr.toInt()}",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Pay ₹${pack.price_inr.toInt()} via Razorpay",
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL,
                                disabled = uiState.isRecharging
                            ),
                            onClick = { viewModel.rechargeSelectedPack() }
                        )
                    }
                } else {
                    Spacer(Modifier.height(28.dp))
                }
            }
        }
    }
}
