package com.app.screentime.feature.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletPacksBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onRechargeSuccess: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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

    ODSBottomSheet(
        scheme = scheme,
        showBottomSheet = true,
        props = ODSBottomSheetProps(showHandle = true),
        onDismissRequest = onDismissRequest,
        onCloseClicked = onDismissRequest,
        titleSlot = {
            ODSBottomSheetHeader(
                scheme = scheme,
                props = ODSBottomSheetHeaderProps(
                    largeHeading = "Add Coins",
                    subtitle = "Current Balance: ${uiState.balance.toInt()} Coins",
                    size = ODSBottomSheetHeaderSize.SMALL
                )
            )
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                gap = 10.dp
            ) {
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = 12.dp),
                    padding = ODSPadding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = "✨ Instant Wallet Recharge",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicAccent
                        )
                        ODSText(
                            text = "100% Secure",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                // Packs List
                if (uiState.packs.isEmpty() && uiState.isLoading) {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = scheme.basicAccent.getColor())
                    }
                } else {
                    uiState.packs.forEach { pack ->
                        PackageCard(
                            pack = pack,
                            isSelected = uiState.selectedPack?.id == pack.id,
                            scheme = scheme,
                            onClick = { viewModel.selectPack(pack) }
                        )
                    }
                }
            }
        },
        actionSlot = {
            val pack = uiState.selectedPack ?: uiState.packs.firstOrNull()

            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 6.dp
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Pay",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL,
                        disabled = uiState.isRecharging || pack == null
                    ),
                    onClick = { viewModel.rechargeSelectedPack() }
                )

                ODSText(
                    text = "Coins never expire · Available immediately for calls & chats",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }
    )
}
