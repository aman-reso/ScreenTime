package com.app.screentime.feature.wallet

import com.app.screentime.core.ui.util.showODSToast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.WalletTransaction
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

/**
 * Transactions History Screen with real-time paginated transaction loading,
 * category filtering (All, Calls, Purchases, Bonus, Spent), and running wallet balance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All") }

    val listState = rememberLazyListState()

    // Trigger next page when scrolled near bottom
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItemIndex >= totalItems - 2
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && uiState.hasMoreTransactions && !uiState.isLoadingTransactions) {
            viewModel.loadNextTransactionsPage()
        }
    }

    // Filter transactions based on category / type
    val filteredTransactions = remember(uiState.transactions, selectedFilter) {
        uiState.transactions.filter { tx ->
            when (selectedFilter) {
                "Calls" -> tx.category.equals("call", ignoreCase = true) ||
                        tx.transactionType.contains("call", ignoreCase = true)
                "Purchases" -> tx.category.equals("purchase", ignoreCase = true) ||
                        tx.transactionType.equals("purchase", ignoreCase = true) ||
                        tx.transactionType.equals("recharge", ignoreCase = true)
                "Bonus" -> tx.category.equals("bonus", ignoreCase = true) ||
                        tx.transactionType.contains("bonus", ignoreCase = true)
                "Spent" -> !tx.isPositive || tx.direction.equals("out", ignoreCase = true)
                "Refunds" -> tx.category.equals("refund", ignoreCase = true) ||
                        tx.transactionType.contains("refund", ignoreCase = true)
                else -> true
            }
        }
    }

    // Group filtered transactions by date
    val groupedTransactions = remember(filteredTransactions) {
        filteredTransactions.groupBy { formatTransactionDateHeader(it.timestamp) }
    }

    if (isLoading || (uiState.isLoadingTransactions && uiState.transactions.isEmpty())) {
        TransactionsShimmer(modifier = modifier, scheme = scheme)
        return
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = ZonaColors.Background))
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoadingTransactions && uiState.transactions.isNotEmpty(),
            onRefresh = { viewModel.loadTransactions(page = 1, refresh = true) },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // ── 1. Top Bar: Header with Back Chevron ────────────────────────
                item(key = "top_bar") {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = ODSVariables.spacingComponent2),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onBack
                                ),
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                            border = ODSBorder(
                                width = ODSVariables.strokes1,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                            ),
                            padding = ODSPadding(all = ODSVariables.spacingComponent4),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_arrow_left,
                                    contentDescription = "Back"
                                ),
                                tint = scheme.basicText.getColor(),
                                modifier = Modifier.size(ODSVariables.sizingComponent8)
                            )
                        }

                        ODSText(
                            text = "Transactions",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )

                        ODSBox(
                            modifier = Modifier.size(46.dp),
                            opacity = 0f
                        )
                    }
                }

                // ── 2. Total Balance Available Card ─────────────────────────────
                item(key = "balance_card") {
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                        cornerRadius = ODSCorners(all = 20.dp),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                        ),
                        padding = ODSPadding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            ODSColumn(gap = 6.dp) {
                                ODSText(
                                    text = "TOTAL BALANCE AVAILABLE",
                                    style = ODSTextStyles.microcopyBold,
                                    color = ZonaColors.LavenderMuted
                                )
                                ODSText(
                                    text = "${uiState.balance.toInt()} pts",
                                    style = ODSTextStyles.bodyL,
                                    color = ZonaColors.TextPrimary
                                )
                            }

                            ODSText(
                                text = "₹${String.format(Locale.ENGLISH, "%.2f", uiState.balance * 4.0)}",
                                style = ODSTextStyles.titleS,
                                color = ZonaColors.ActiveLime
                            )
                        }
                    }
                }

                // ── 3. Filter Chips: [All] [Calls] [Purchases] [Bonus] [Spent] [Refunds] ──
                item(key = "filter_chips") {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = 8.dp
                    ) {
                        listOf("All", "Calls", "Purchases", "Bonus", "Spent", "Refunds").forEach { filter ->
                            val isSelected = selectedFilter == filter
                            ODSBox(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = { selectedFilter = filter }
                                    ),
                                background = listOf(
                                    ODSColorModel(
                                        hexColor = if (isSelected) ZonaColors.ActiveLime else ZonaColors.SurfaceRaised
                                    )
                                ),
                                cornerRadius = ODSCorners(all = 16.dp),
                                border = if (!isSelected) {
                                    ODSBorder(
                                        width = 1.dp,
                                        colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                                    )
                                } else null,
                                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = filter,
                                    style = ODSTextStyles.bodySBold,
                                    color = if (isSelected) ZonaColors.TextInverse else ZonaColors.LavenderAlt
                                )
                            }
                        }
                    }
                }

                // ── 4. Grouped Transactions List ────────────────────────────────
                if (filteredTransactions.isEmpty()) {
                    item(key = "empty_transactions") {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                            cornerRadius = ODSCorners(all = 18.dp),
                            border = ODSBorder(
                                width = 1.dp,
                                colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                            ),
                            padding = ODSPadding(horizontal = 24.dp, vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                gap = 8.dp
                            ) {
                                ODSText(
                                    text = "No Transactions Found",
                                    style = ODSTextStyles.bodyMBold,
                                    color = ZonaColors.TextPrimary
                                )
                                ODSText(
                                    text = "No $selectedFilter activity recorded on your wallet yet.",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = ZonaColors.LavenderMuted
                                )
                            }
                        }
                    }
                } else {
                    groupedTransactions.forEach { (dateHeader, txList) ->
                        item(key = "header_$dateHeader") {
                            ODSText(
                                text = dateHeader,
                                style = ODSTextStyles.microcopyBold,
                                color = ZonaColors.LavenderMuted,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }

                        items(txList, key = { it.id }) { tx ->
                            RealTransactionRowItem(
                                tx = tx,
                                onReceiptClick = {
                                    context.showODSToast("${tx.description}: ${if (tx.balanceAfter > 0) "Balance: ${tx.balanceAfter.toInt()}" else "Processed"}")
                                }
                            )
                        }
                    }
                }

                // ── 5. Pagination Loading Indicator ─────────────────────────────
                if (uiState.hasMoreTransactions) {
                    item(key = "load_more") {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = ZonaColors.ActiveLime.getColor(),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RealTransactionRowItem(
    tx: WalletTransaction,
    onReceiptClick: () -> Unit
) {
    val isPositive = tx.isPositive
    val amountInt = abs(tx.amount).toInt()
    val amountDisplay = if (isPositive) "+$amountInt pts" else "-$amountInt pts"

    val displayTitle = tx.description.ifBlank {
        when (tx.transactionType.lowercase()) {
            "video_call" -> "Video Call"
            "voice_call" -> "Voice Call"
            "purchase" -> "Points Recharge"
            "welcome_bonus" -> "Welcome Bonus"
            "refund" -> "Call Refund"
            "boost" -> "Profile Boost"
            else -> "Wallet Transaction"
        }
    }

    val subtitle = buildString {
        append(formatTransactionTime(tx.timestamp))
        if (tx.balanceAfter > 0.0) {
            append(" • Bal: ")
            append(tx.balanceAfter.toInt())
        }
    }

    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
        cornerRadius = ODSCorners(all = 18.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
        ),
        padding = ODSPadding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 12.dp,
                modifier = Modifier.weight(1f)
            ) {
                // Direction Arrow Icon Container
                ODSBox(
                    modifier = Modifier.size(42.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.Surface)),
                    cornerRadius = ODSCorners(all = 12.dp),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = if (isPositive) R.drawable.ic_arrow_up_right else R.drawable.ic_arrow_down_left,
                            contentDescription = displayTitle
                        ),
                        tint = if (isPositive) ZonaColors.ActiveLime.getColor() else ZonaColors.ActionPrimary.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title & Subtitle Info
                ODSColumn(gap = 3.dp) {
                    ODSText(
                        text = displayTitle,
                        style = ODSTextStyles.bodyMBold,
                        color = ZonaColors.TextPrimary
                    )
                    ODSText(
                        text = subtitle,
                        style = ODSTextStyles.microcopyRegular,
                        color = ZonaColors.LavenderMuted
                    )
                }
            }

            // Amount & Receipt
            ODSColumn(
                horizontalAlignment = Alignment.End,
                gap = 3.dp
            ) {
                ODSText(
                    text = amountDisplay,
                    style = ODSTextStyles.bodyMBold,
                    color = if (isPositive) ZonaColors.ActiveLime else ZonaColors.ActionPrimary
                )
                ODSText(
                    text = "Details",
                    style = ODSTextStyles.microcopyRegular,
                    color = ZonaColors.LavenderAlt,
                    modifier = Modifier.clickable(onClick = onReceiptClick)
                )
            }
        }
    }
}

private fun formatTransactionDateHeader(epochMs: Long): String {
    return try {
        val now = LocalDate.now()
        val date = Instant.ofEpochMilli(epochMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        when {
            date.isEqual(now) -> "TODAY"
            date.isEqual(now.minusDays(1)) -> "YESTERDAY"
            else -> date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)).uppercase()
        }
    } catch (_: Exception) {
        "PAST TRANSACTIONS"
    }
}

private fun formatTransactionTime(epochMs: Long): String {
    return try {
        val instant = Instant.ofEpochMilli(epochMs)
        val time = instant.atZone(ZoneId.systemDefault())
        time.format(DateTimeFormatter.ofPattern("MMM d, yyyy • h:mm a", Locale.ENGLISH))
    } catch (_: Exception) {
        "Recent"
    }
}
