package com.app.screentime.feature.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Add Funds & Top-Up Screen (Matching Figma node-id 21-7: https://figma.com/design/1TELYpr19qLAv99DexRNpi/Untitled?node-id=21-7).
 *
 * Strictly adheres to project rules (GEMINI.md):
 * 1. 100% ODS components & ODSLazyColumn.
 * 2. Colors picked from composable's `scheme: ODSTheme`.
 * 3. Max text size is 16sp with Funnel Sans font.
 * 4. Padding, margins, and gaps mapped to ODSVariables.
 * 5. Status bar excluded as requested.
 * 6. Dynamic data fetched via WalletViewModel from GET /api/wallet/packages.
 */
@Composable
fun AddFundsScreen(
    onBack: () -> Unit,
    onPaymentSuccess: () -> Unit = onBack,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWallet()
        viewModel.loadPacks()
    }

    LaunchedEffect(uiState.rechargeSuccess) {
        if (uiState.rechargeSuccess) {
            context.showODSToast("Payment successful! Coins added to wallet. 🎉")
            viewModel.resetRechargeStatus()
            onPaymentSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            context.showODSToast(err)
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        clipContent = true,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        0.00f to scheme.basicBackground,
                        0.52f to scheme.basicBackgroundSubtle,
                        1.00f to scheme.basicBackgroundCard
                    ),
                    opacity = 1.00f,
                    angleInDegrees = 180f
                )
            )
        )
    ) {
        // ── 1. Top Header Bar (No dummy status bar) ────────────────────────────
        ODSRow(
            modifier = Modifier
                .fillMaxWidth(),
            padding = ODSPadding(
                top = ODSVariables.spacingComponent2,
                bottom = ODSVariables.spacingComponent3,
                left = ODSVariables.spacingLayout1,
                right = ODSVariables.spacingLayout1
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(
                padding = ODSPadding(all = ODSVariables.spacingComponent4),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBack
                )
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_arrow_left,
                        contentDescription = "Back"
                    ),
                    tint = scheme.basicTextDominant.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }

            ODSText(
                text = "Buy Points",
                style = ODSTextStyle(
                    fontFamily = R.font.funnelsans_bold,
                    fontSize = 18,
                    lineHeight = 18
                ),
                color = HexColor("#111827", 1.0f)
            )
            ODSBox(modifier = Modifier.size(44.dp))
        }

        // ── 2. Scrollable Body Content using ODSLazyColumn (Rule #1) ───────────
        ODSLazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            gap = ODSVariables.spacingComponent4
        ) {
            // ── Item 1: Current Balance Card ───────────────────────────────────
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                    cornerRadius = ODSCorners(all = 20.dp),
                    border = ODSBorder(
                        width = 2.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                ) {
                    ODSColumn(
                        gap = 4.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ODSText(
                            text = "AVAILABLE BALANCE",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextRecessive
                        )
                        ODSText(
                            text = "🪙 ${uiState.balance.toInt()} Coins",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )
                    }

                    ODSColumn(
                        gap = 4.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ODSText(
                            text = "CALL CREDITS",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextRecessive
                        )
                        ODSText(
                            text = "✨ ${if (uiState.creditBalance > 0) uiState.creditBalance else uiState.balance.toInt()} Credits",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicAccent
                        )
                    }
                }
            }

            // ── Item 2: Select Points Package Section ──────────────────────────
            item {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        text = "Select Points Package",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )

                    if (uiState.packs.isEmpty() && uiState.isLoading) {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSLoadingSpinner(
                                scheme = scheme,
                                props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.LARGE)
                            )
                        }
                    } else {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = 10.dp,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            uiState.packs.forEach { pack ->
                                val isSelected = uiState.selectedPack?.id == pack.id

                                ODSRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = { viewModel.selectPack(pack) }
                                        ),
                                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                                    cornerRadius = ODSCorners(all = 20.dp),
                                    border = ODSBorder(
                                        width = 2.dp,
                                        colorList = listOf(
                                            ODSColorModel(
                                                hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                            )
                                        )
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    background = listOf(
                                        ODSColorModel(
                                            hexColor = if (isSelected) scheme.basicBackgroundCard else scheme.basicBackgroundCardSubtle
                                        )
                                    )
                                ) {
                                    ODSRow(
                                        gap = 12.dp,
                                        horizontalAlignment = Alignment.Start,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        // Custom Radio Indicator Circle
                                        ODSBox(
                                            modifier = Modifier.size(20.dp),
                                            cornerRadius = ODSCorners(all = 10.dp),
                                            border = if (!isSelected) {
                                                ODSBorder(
                                                    width = 2.dp,
                                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                                )
                                            } else null,
                                            background = if (isSelected) {
                                                listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                            } else emptyList(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                ODSBox(
                                                    modifier = Modifier.size(8.dp),
                                                    cornerRadius = ODSCorners(all = 4.dp),
                                                    background = listOf(ODSColorModel(hexColor = scheme.basicTextOnAccent))
                                                )
                                            }
                                        }

                                        ODSColumn(
                                            gap = 2.dp,
                                            verticalAlignment = Alignment.Top,
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.Top
                                        ) {
                                            ODSText(
                                                text = "${pack.effectiveCredits} Credits",
                                                style = ODSTextStyles.bodyMBold,
                                                color = scheme.basicTextDominant
                                            )
                                            val subtitle = when {
                                                pack.voice_minutes > 0 && pack.video_minutes > 0 ->
                                                    "${pack.voice_minutes}m Voice · ${pack.video_minutes}m Video"

                                                pack.description.isNotBlank() ->
                                                    pack.description

                                                pack.benefits.isNotEmpty() ->
                                                    pack.benefits.first()

                                                else ->
                                                    "Instant WebRTC · Never Expires"
                                            }
                                            ODSText(
                                                text = subtitle,
                                                style = ODSTextStyles.microcopyRegular,
                                                color = scheme.basicTextRecessive
                                            )
                                        }
                                    }

                                    ODSRow(
                                        gap = 8.dp,
                                        horizontalAlignment = Alignment.End,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        if (pack.isPopularPack) {
                                            ODSRow(
                                                padding = ODSPadding(
                                                    top = 4.dp,
                                                    bottom = 4.dp,
                                                    left = 8.dp,
                                                    right = 8.dp
                                                ),
                                                cornerRadius = ODSCorners(all = 8.dp),
                                                horizontalAlignment = Alignment.Start,
                                                verticalAlignment = Alignment.Top,
                                                horizontalArrangement = Arrangement.Start,
                                                background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                                            ) {
                                                ODSText(
                                                    text = "POPULAR",
                                                    style = ODSTextStyles.microcopyBold,
                                                    color = scheme.basicTextOnAccent
                                                )
                                            }
                                        }

                                        ODSText(
                                            text = "₹${pack.effectivePrice.toInt()}",
                                            style = ODSTextStyles.bodyMBold,
                                            color = scheme.basicTextDominant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom clearance spacer
            item {
                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent5))
            }
        }

        // ── 3. Sticky Bottom CTA & Home Indicator ──────────────────────────────
        val selectedPack = uiState.selectedPack ?: uiState.packs.firstOrNull()
        val isEnabled = selectedPack != null && !uiState.isRecharging && !uiState.isVerifyingPayment

        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent3
                ),
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = when {
                        uiState.isVerifyingPayment -> "Verifying Purchase..."
                        uiState.isRecharging -> "Connecting to Google Play..."
                        selectedPack != null -> "Buy with Google Play (₹${selectedPack.effectivePrice.toInt()})"
                        else -> "Select a Package"
                    },
                    variant = ODSButtonVariant.PRIMARY,
                    disabled = !isEnabled
                ),
                onClick = {
                    val pack = selectedPack ?: return@ODSButton
                    val activity = context as? android.app.Activity
                    if (activity == null) {
                        context.showODSToast("Unable to process purchase on this device")
                        return@ODSButton
                    }
                    viewModel.buyWithGooglePlay(
                        activity = activity,
                        pack = pack,
                        onError = { errorMsg ->
                            context.showODSToast(errorMsg)
                        }
                    )
                }
            )
        }
    }
}
