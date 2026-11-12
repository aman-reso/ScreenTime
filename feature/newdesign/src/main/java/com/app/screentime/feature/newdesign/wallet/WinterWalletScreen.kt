package com.app.screentime.feature.newdesign.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.WinterFloatingNavDefaults
import com.app.screentime.feature.newdesign.components.WinterFloatingNavigation
import com.app.screentime.feature.newdesign.components.WinterNavItem
import com.app.screentime.feature.newdesign.components.WinterNavRole
import com.app.screentime.feature.newdesign.components.WinterTokens

/**
 * Data model for User coin transaction history items.
 */
data class CoinTransactionItem(
    val id: String,
    val title: String,
    val timestampDetails: String,
    val amountFormatted: String,
    val isPositive: Boolean,
    val iconRes: Int,
    val category: String = "All"
)

/**
 * WinterWalletScreen - Figma screen "Winter · Wallet" (Node 130:2).
 *
 * Rules:
 * 1. Multiples of 4dp for all padding, margins, spacers, and sizes.
 * 2. Broken down into clean, modular composable functions.
 * 3. Dynamic color scheme tokens (WinterTokens) and typography (FunnelSans).
 * 4. Never hardcode arbitrary container heights (flat header with 0 bottom corner radius).
 */
@Composable
fun WinterWalletScreen(
    modifier: Modifier = Modifier,
    coinBalance: Int? = null,
    viewModel: WinterWalletViewModel = hiltViewModel(),
    showBottomNav: Boolean = false,
    contentBottomPadding: Dp = WinterFloatingNavDefaults.Height + 48.dp,
    onBackClick: () -> Unit = {},
    onRechargeClick: () -> Unit = {},
    onMonthFilterClick: () -> Unit = {},
    onNavItemSelected: (WinterNavItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedNavItem by remember { mutableStateOf(WinterNavItem.WALLET) }

    val filterTabs = remember { listOf("All", "Recharges", "Calls") }

    val effectiveCoinBalance = coinBalance ?: when (val state = uiState) {
        is WinterWalletUiState.Success -> state.coinBalance
        else -> 0
    }

    val transactionsList = when (val state = uiState) {
        is WinterWalletUiState.Success -> state.transactions
        else -> emptyList()
    }

    val filteredTransactions = remember(selectedFilter, transactionsList) {
        if (selectedFilter == "All") {
            transactionsList
        } else {
            transactionsList.filter { it.category.equals(selectedFilter, ignoreCase = true) }
        }
    }

    val bottomPadding = if (showBottomNav) {
        WinterFloatingNavDefaults.Height + 48.dp
    } else {
        contentBottomPadding
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WinterTokens.backgroundCanvas)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = bottomPadding)
        ) {
            // 1. Header: Balance & Recharge (Node 130:3) - Flat, no bottom rounded corners
            item(key = "wallet_header") {
                WalletHeaderSection(
                    statusBarPadding = statusBarPadding,
                    coinBalance = effectiveCoinBalance,
                    onBackClick = onBackClick,
                    onRechargeClick = onRechargeClick
                )
            }

            item(key = "transactions_header") {
                WalletTransactionsHeader(
                    selectedFilter = selectedFilter,
                    filterTabs = filterTabs,
                    onFilterSelected = {
                        selectedFilter = it
                        viewModel.loadWallet(it)
                    },
                    onMonthFilterClick = onMonthFilterClick
                )
            }

            items(
                items = filteredTransactions,
                key = { it.id }
            ) { transaction ->
                WalletTransactionItemCard(
                    transaction = transaction,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        // Floating Bottom Navigation & Home Indicator (Node 130:5)
        if (showBottomNav) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WinterFloatingNavigation(
                    role = WinterNavRole.USER,
                    selectedItem = selectedNavItem,
                    onItemSelected = { item ->
                        selectedNavItem = item
                        onNavItemSelected(item)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Home indicator (Node 130:41)
                Box(
                    modifier = Modifier
                        .size(width = 128.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(WinterTokens.controlIndicator)
                )
            }
        }
    }
}

/**
 * Top Header Card matching Figma Node 130:3.
 * Background: surfaceHeader (#222222), flat across width with zero corner radius.
 */
@Composable
private fun WalletHeaderSection(
    statusBarPadding: Dp,
    coinBalance: Int,
    onBackClick: () -> Unit,
    onRechargeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WinterTokens.surfaceHeader)
            .padding(
                top = statusBarPadding + 8.dp,
                bottom = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        // Top Navigation Bar
        WalletTopBar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(24.dp))

        // Balance & Recharge CTA Row
        WalletBalanceAndRechargeRow(
            coinBalance = coinBalance,
            onRechargeClick = onRechargeClick
        )
    }
}

/**
 * Top bar with 48dp back button and centered "Wallet" title (Node 130:82 - 130:86).
 */
@Composable
private fun WalletTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .clip(CircleShape)
                .background(WinterTokens.surfaceControl)
                .border(1.dp, WinterTokens.borderSubtle, CircleShape)
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
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = "Wallet",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = WinterTokens.textPrimary
        )
    }
}

/**
 * Available coins balance and "Recharge coins" action button row (Node 130:88 - 130:101).
 */
@Composable
private fun WalletBalanceAndRechargeRow(
    coinBalance: Int,
    onRechargeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Coin stack icon + Balance Column
        WalletBalanceInfo(coinBalance = coinBalance)

        // Right: "Recharge coins" Button (Node 130:99)
        WalletRechargeButton(onClick = onRechargeClick)
    }
}

/**
 * Balance display with 32dp coin stack icon and Available coins label.
 */
@Composable
private fun WalletBalanceInfo(
    coinBalance: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_coin_stack_header),
            contentDescription = null,
            tint = WinterTokens.textAccent,
            modifier = Modifier.size(32.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Available coins",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = WinterTokens.textSecondary
            )

            Text(
                text = String.format("%,d", coinBalance),
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = (-1.5).sp,
                lineHeight = 28.sp,
                color = WinterTokens.textPrimary
            )
        }
    }
}

/**
 * Primary "Recharge coins" action button (Node 130:99 - 48dp height, rounded 24dp, #ACE2C6).
 */
@Composable
private fun WalletRechargeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(WinterTokens.actionPrimary)
            .border(1.dp, WinterTokens.borderOnAccent, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Recharge",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = WinterTokens.textOnAccent
        )
    }
}

/**
 * Transactions section header with month filter and tab selector (Node 130:4).
 */
@Composable
private fun WalletTransactionsHeader(
    selectedFilter: String,
    filterTabs: List<String>,
    onFilterSelected: (String) -> Unit,
    onMonthFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 24.dp, bottom = 12.dp)
    ) {
        // Row: "Transactions" + Month Filter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transactions",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )

            WalletMonthFilterPill(onClick = onMonthFilterClick)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3 Filter Tabs: "All", "Recharges", "Calls" (Node 130:134)
        WalletFilterTabs(
            selectedFilter = selectedFilter,
            filterTabs = filterTabs,
            onFilterSelected = onFilterSelected
        )
    }
}

/**
 * Month Filter capsule pill (Node 130:125).
 */
@Composable
private fun WalletMonthFilterPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(WinterTokens.surfaceControl)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar_white),
            contentDescription = null,
            tint = WinterTokens.iconPrimary,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = "Nov 2026",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = WinterTokens.textPrimary
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_down_filter),
            contentDescription = null,
            tint = WinterTokens.iconSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Filter tab bar matching Figma Node 130:134 (40dp height container, 32dp active pill).
 */
@Composable
private fun WalletFilterTabs(
    selectedFilter: String,
    filterTabs: List<String>,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(WinterTokens.surfaceControl)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        filterTabs.forEach { tab ->
            val isSelected = tab == selectedFilter
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) WinterTokens.actionPrimary else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onFilterSelected(tab) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab,
                    fontFamily = FunnelSans,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) WinterTokens.textOnAccent else WinterTokens.textSecondary
                )
            }
        }
    }
}

/**
 * Individual Transaction Card matching Figma Node 130:139 (24dp rounded card in surfaceCard #222222).
 */
@Composable
private fun WalletTransactionItemCard(
    transaction: CoinTransactionItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(WinterTokens.surfaceCard)
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WalletTransactionIcon(
                iconRes = transaction.iconRes,
                isPositive = transaction.isPositive
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Title & Timestamp Column
            WalletTransactionDetails(
                title = transaction.title,
                timestampDetails = transaction.timestampDetails,
                modifier = Modifier.weight(1f)
            )

            // Amount Column (+amount in accent green, -amount in text primary)
            WalletTransactionAmount(
                amountFormatted = transaction.amountFormatted,
                isPositive = transaction.isPositive
            )
        }
    }
}

/**
 * 52dp Circular transaction icon container matching Figma Node 130:140.
 */
@Composable
private fun WalletTransactionIcon(
    iconRes: Int,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(WinterTokens.backgroundCanvas),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = if (isPositive) WinterTokens.textAccent else WinterTokens.iconPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Title and timestamp details column.
 */
@Composable
private fun WalletTransactionDetails(
    title: String,
    timestampDetails: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = WinterTokens.textPrimary
        )

        Text(
            text = timestampDetails,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = WinterTokens.textSecondary
        )
    }
}

/**
 * Formatted transaction amount and "coins" unit.
 */
@Composable
private fun WalletTransactionAmount(
    amountFormatted: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = amountFormatted,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = if (isPositive) WinterTokens.textAccent else WinterTokens.textPrimary
        )

        Text(
            text = "coins",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = WinterTokens.textSecondary
        )
    }
}

@Preview(name = "Winter · Wallet (Node 130:2)", showBackground = true)
@Composable
private fun WinterWalletScreenPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        WinterWalletScreen(showBottomNav = true)
    }
}
