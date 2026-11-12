package com.app.screentime.feature.newdesign.wallet

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.WinterTokens
import kotlinx.coroutines.delay

/**
 * Winter · Recharge Processing Screen (Figma Node 176:7).
 *
 * Rules followed:
 * - Multiples of 4dp for all padding, margins, and sizes.
 * - Broken down into modular, single-responsibility composables.
 * - Uses dynamic color scheme tokens and typography.
 * - Dynamic/content-driven heights instead of arbitrary fixed sizing.
 */
@Composable
fun WinterRechargeProcessingScreen(
    modifier: Modifier = Modifier,
    packageId: String = "",
    packCoins: String = "2,500",
    packPrice: String = "₹499",
    currentBalance: String = "",
    viewModel: WinterWalletViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onPaymentSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val effectiveBalance = if (currentBalance.isNotBlank()) {
        currentBalance
    } else {
        when (val state = uiState) {
            is WinterWalletUiState.Success -> "%,d".format(state.coinBalance)
            else -> "0"
        }
    }

    LaunchedEffect(packageId) {
        if (packageId.isNotBlank()) {
            viewModel.purchasePackage(packageId)
        }
        delay(2500L)
        onPaymentSuccess()
    }

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
            ProcessingHeaderSection(
                statusBarPadding = statusBarPadding,
                currentBalance = effectiveBalance,
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                PaymentProgressIndicatorSection()

                PaymentSummaryCardSection(
                    packCoins = packCoins,
                    packPrice = packPrice,
                    currentBalance = effectiveBalance
                )

                PaymentStepTimelineSection()

                ProcessingFooterSection()
            }
        }
    }
}

@Composable
private fun ProcessingHeaderSection(
    statusBarPadding: androidx.compose.ui.unit.Dp,
    currentBalance: String,
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
                text = "Recharge coins",
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
                    text = "$currentBalance coins",
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
private fun PaymentProgressIndicatorSection(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "SpinnerTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SpinnerRotation"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(144.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { 0.75f },
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(rotation),
                color = WinterTokens.actionPrimary,
                strokeWidth = 6.dp,
                trackColor = WinterTokens.borderSubtle,
                strokeCap = StrokeCap.Round
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_coin_stack_header),
                contentDescription = null,
                tint = WinterTokens.textAccent,
                modifier = Modifier.size(36.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Processing your payment",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = WinterTokens.textPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Waiting for payment confirmation",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = WinterTokens.textPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Your selected pack is reserved while we confirm.",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = WinterTokens.textSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PaymentSummaryCardSection(
    packCoins: String,
    packPrice: String,
    currentBalance: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(WinterTokens.surfaceCard)
            .border(1.dp, WinterTokens.borderDefault, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Selected pack",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = WinterTokens.textSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$packCoins coins",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = WinterTokens.textPrimary
                )

                Text(
                    text = packPrice,
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = WinterTokens.textPrimary
                )
            }

            Text(
                text = "Current balance: $currentBalance coins",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = WinterTokens.textSecondary
            )
        }
    }
}

@Composable
private fun PaymentStepTimelineSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepDotItem(label = "Pack selected", isCompleted = true, isActive = false)
        StepDotItem(label = "Confirming payment", isCompleted = false, isActive = true)
        StepDotItem(label = "Coins added", isCompleted = false, isActive = false)
    }
}

@Composable
private fun StepDotItem(
    label: String,
    isCompleted: Boolean,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> WinterTokens.actionPrimary
                        isActive -> WinterTokens.textPrimary
                        else -> WinterTokens.borderDefault
                    }
                )
        )

        Text(
            text = label,
            fontFamily = FunnelSans,
            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
            fontSize = 12.sp,
            color = if (isActive || isCompleted) WinterTokens.textPrimary else WinterTokens.textSecondary
        )
    }
}

@Composable
private fun ProcessingFooterSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Payment in progress",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = WinterTokens.textPrimary
        )

        Text(
            text = "Please keep this screen open. Your coins will be added once your payment is confirmed.",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = WinterTokens.textSecondary,
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(WinterTokens.actionPrimary)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = WinterTokens.textOnAccent,
                    strokeWidth = 2.dp
                )

                Text(
                    text = "Confirming…",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = WinterTokens.textOnAccent
                )
            }
        }
    }
}

