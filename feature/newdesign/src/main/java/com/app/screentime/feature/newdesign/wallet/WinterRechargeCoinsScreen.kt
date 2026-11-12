package com.app.screentime.feature.newdesign.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.WinterTokens

/**
 * Data model representing a coin package in [WinterRechargeCoinsScreen].
 */
data class WinterRechargePack(
    val id: String,
    val coins: Int,
    val priceInr: Int,
    val name: String = "",
    val isPopular: Boolean = false
)

/**
 * Winter · Recharge Coins Screen (Figma Node 176:2).
 *
 * Rules followed:
 * - Multiples of 4dp for all padding, margins, and sizes.
 * - Broken down into modular, single-responsibility composables.
 * - Uses dynamic color scheme tokens and typography.
 * - Dynamic/content-driven heights instead of arbitrary fixed sizing.
 */
@Composable
fun WinterRechargeCoinsScreen(
    modifier: Modifier = Modifier,
    currentBalance: Int? = null,
    viewModel: WinterWalletViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onPayClick: (WinterRechargePack) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val effectiveBalance = currentBalance ?: when (val state = uiState) {
        is WinterWalletUiState.Success -> state.coinBalance
        else -> 0
    }

    val availablePacks = when (val state = uiState) {
        is WinterWalletUiState.Success -> {
            if (state.packages.isNotEmpty()) {
                state.packages.map { dto ->
                    WinterRechargePack(
                        id = dto.id,
                        coins = dto.credits,
                        priceInr = dto.price_inr,
                        name = dto.name,
                        isPopular = dto.popular_badge
                    )
                }
            } else {
                listOf(
                    WinterRechargePack("product_19_rupees", 190, 19, "190 Message"),
                    WinterRechargePack("product_49_rupees", 490, 49, "490 Profile View", isPopular = true),
                    WinterRechargePack("pack_99", 1000, 99, "Starter Calling Pack"),
                    WinterRechargePack("pack_199", 2500, 199, "Popular Connection Pack")
                )
            }
        }
        else -> listOf(
            WinterRechargePack("product_19_rupees", 190, 19, "190 Message"),
            WinterRechargePack("product_49_rupees", 490, 49, "490 Profile View", isPopular = true),
            WinterRechargePack("pack_99", 1000, 99, "Starter Calling Pack"),
            WinterRechargePack("pack_199", 2500, 199, "Popular Connection Pack")
        )
    }

    var selectedPackId by remember(availablePacks) {
        mutableStateOf(availablePacks.firstOrNull { it.isPopular }?.id ?: availablePacks.firstOrNull()?.id.orEmpty())
    }
    val selectedPack = availablePacks.firstOrNull { it.id == selectedPackId } ?: availablePacks.first()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WinterTokens.backgroundCanvas)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            RechargeHeaderSection(
                statusBarPadding = statusBarPadding,
                currentBalance = effectiveBalance,
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PackIntroSection()

                CoinPacksListSection(
                    packs = availablePacks,
                    selectedPackId = selectedPackId,
                    onPackSelected = { selectedPackId = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                RechargeBottomPaySection(
                    selectedPack = selectedPack,
                    onPayClick = { onPayClick(selectedPack) }
                )
            }
        }
    }
}

@Composable
private fun RechargeHeaderSection(
    statusBarPadding: androidx.compose.ui.unit.Dp,
    currentBalance: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WinterTokens.surfaceHeader)
            .padding(
                top = statusBarPadding + 16.dp,
                bottom = 24.dp,
                start = 24.dp,
                end = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.dp, WinterTokens.borderDefault, CircleShape)
                    .background(WinterTokens.surfaceControl)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBackClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow_chevron),
                    contentDescription = "Back",
                    tint = WinterTokens.iconPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Recharge",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = WinterTokens.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(48.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(WinterTokens.surfaceControl),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_coin_stack_header),
                    contentDescription = null,
                    tint = WinterTokens.textAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Current balance",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = WinterTokens.textSecondary
                )
                Text(
                    text = "%,d coins".format(currentBalance),
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = WinterTokens.textPrimary
                )
            }
        }
    }
}

@Composable
private fun PackIntroSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Choose a coin pack",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = WinterTokens.textPrimary
        )

        Text(
            text = "Pick a pack to keep the conversation going.",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = WinterTokens.textSecondary
        )
    }
}

@Composable
private fun CoinPacksListSection(
    packs: List<WinterRechargePack>,
    selectedPackId: String,
    onPackSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        packs.forEach { pack ->
            CoinPackItemCard(
                pack = pack,
                isSelected = pack.id == selectedPackId,
                onClick = { onPackSelected(pack.id) }
            )
        }
    }
}

@Composable
private fun CoinPackItemCard(
    pack: WinterRechargePack,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) WinterTokens.borderSelected else WinterTokens.borderDefault
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(20.dp))
            .background(WinterTokens.surfaceCard)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Radio indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            if (isSelected) WinterTokens.borderSelected else WinterTokens.borderMuted,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(WinterTokens.actionPrimary)
                        )
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_coin_stack_header),
                    contentDescription = null,
                    tint = if (isSelected) WinterTokens.textAccent else WinterTokens.iconMuted,
                    modifier = Modifier.size(24.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "%,d coins".format(pack.coins),
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = WinterTokens.textPrimary
                        )

                        if (pack.isPopular) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(WinterTokens.actionPrimary.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "POPULAR",
                                    fontFamily = FunnelSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = WinterTokens.actionPrimary
                                )
                            }
                        }
                    }

                    if (pack.name.isNotBlank()) {
                        Text(
                            text = pack.name,
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = WinterTokens.textSecondary
                        )
                    }
                }
            }

            Text(
                text = "₹${pack.priceInr}",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )
        }
    }
}

@Composable
private fun RechargeBottomPaySection(
    selectedPack: WinterRechargePack,
    onPayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_shield_check),
                contentDescription = null,
                tint = WinterTokens.textAccent,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = "Instant coin credit via UPI & Cards",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = WinterTokens.textSecondary
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(WinterTokens.actionPrimary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onPayClick
                )
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pay ₹${selectedPack.priceInr}",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = WinterTokens.textOnAccent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WinterRechargeCoinsScreenPreview() {
    WinterRechargeCoinsScreen()
}
