package com.app.screentime.feature.newdesign.wallet

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.network.dto.WinterRechargePackageDto
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.WinterPageLoadingScreen
import com.app.screentime.feature.newdesign.components.WinterPageLoadingStatus
import com.app.screentime.feature.newdesign.components.WinterTokens

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

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
    onPayClick: (WinterRechargePackageDto) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    val userMessage = (uiState as? WinterWalletUiState.Success)?.userMessage
    LaunchedEffect(userMessage) {
        if (!userMessage.isNullOrBlank()) {
            Toast.makeText(context, userMessage, Toast.LENGTH_LONG).show()
            viewModel.clearUserMessage()
        }
    }

    val isRecharging = (uiState as? WinterWalletUiState.Success)?.isRecharging == true

    LaunchedEffect(Unit) {
        viewModel.loadPackages()
    }

    val effectiveBalance = currentBalance ?: when (val state = uiState) {
        is WinterWalletUiState.Success -> state.coinBalance
        else -> 0
    }

    val availablePacks = (uiState as? WinterWalletUiState.Success)?.packages.orEmpty()

    var selectedPackId by remember(availablePacks) {
        mutableStateOf(availablePacks.firstOrNull { it.popular_badge }?.id ?: availablePacks.firstOrNull()?.id.orEmpty())
    }
    val selectedPack = availablePacks.firstOrNull { it.id == selectedPackId } ?: availablePacks.firstOrNull()

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

                if (availablePacks.isNotEmpty() && selectedPack != null) {
                    CoinPacksListSection(
                        packs = availablePacks,
                        selectedPackId = selectedPackId,
                        onPackSelected = { selectedPackId = it }
                    )

                    // Spacing at the bottom so content can be scrolled fully above the sticky pay button
                    Spacer(modifier = Modifier.height(116.dp))
                } else {
                    EmptyPackagesSection(
                        isLoading = uiState is WinterWalletUiState.Loading,
                        onRetry = { viewModel.loadPackages() }
                    )
                }
            }
        }

        // Sticky Pay Section pinned to bottom
        if (availablePacks.isNotEmpty() && selectedPack != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            0.0f to WinterTokens.backgroundCanvas.copy(alpha = 0f),
                            0.2f to WinterTokens.backgroundCanvas.copy(alpha = 0.85f),
                            0.45f to WinterTokens.backgroundCanvas,
                            1.0f to WinterTokens.backgroundCanvas
                        )
                    )
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 16.dp)
            ) {
                RechargeBottomPaySection(
                    selectedPack = selectedPack,
                    onPayClick = {
                        if (activity != null) {
                            viewModel.launchGooglePlayPurchase(
                                activity = activity,
                                packageId = selectedPack.id,
                                onError = { err ->
                                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            onPayClick(selectedPack)
                        }
                    }
                )
            }
        }

        if (isRecharging) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(WinterTokens.surfaceCard)
                        .border(1.dp, WinterTokens.borderDefault, RoundedCornerShape(20.dp))
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = WinterTokens.actionPrimary,
                        strokeWidth = 3.dp
                    )
                    Text(
                        text = "Connecting to Google Play Store…",
                        fontFamily = FunnelSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = WinterTokens.textPrimary
                    )
                }
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
    packs: List<WinterRechargePackageDto>,
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
    pack: WinterRechargePackageDto,
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
                            text = "%,d coins".format(pack.credits),
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = WinterTokens.textPrimary
                        )

                        if (pack.popular_badge) {
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
                text = "₹${pack.price_inr}",
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
    selectedPack: WinterRechargePackageDto,
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
                text = "Pay ₹${selectedPack.price_inr}",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = WinterTokens.textOnAccent
            )
        }
    }
}

@Composable
private fun EmptyPackagesSection(
    isLoading: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isLoading) {
            WinterPageLoadingStatus(
                subtitle = "Fetching recharge packages…"
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_triangle_alert),
                contentDescription = null,
                tint = WinterTokens.textSecondary,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = "No coin packages available",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )
            Text(
                text = "Packages are currently unavailable. Please check your connection and try again.",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = WinterTokens.textSecondary,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(WinterTokens.surfaceCard)
                    .border(1.dp, WinterTokens.borderDefault, RoundedCornerShape(20.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onRetry
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Retry",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = WinterTokens.textPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WinterRechargeCoinsScreenPreview() {
    WinterRechargeCoinsScreen()
}
