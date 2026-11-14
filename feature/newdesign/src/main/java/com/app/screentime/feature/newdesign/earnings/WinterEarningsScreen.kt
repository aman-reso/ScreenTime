package com.app.screentime.feature.newdesign.earnings

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.WinterFloatingNavDefaults
import com.app.screentime.feature.newdesign.components.WinterFloatingNavigation
import com.app.screentime.feature.newdesign.components.WinterLoadingSpinner
import com.app.screentime.feature.newdesign.components.WinterNavItem
import com.app.screentime.feature.newdesign.components.WinterNavRole
import com.app.screentime.feature.newdesign.components.WinterPageLoadingScreen
import com.app.screentime.feature.newdesign.components.WinterPageLoadingStatus
import com.app.screentime.feature.newdesign.components.WinterTokens
import com.app.screentime.feature.newdesign.dialogs.WinterWithdrawAmountDialog


/**
 * Data model for Creator earning transaction history items.
 */
data class EarningHistoryItem(
    val id: String,
    val title: String,
    val details: String,
    val amountFormatted: String,
    val isPositive: Boolean,
    val iconRes: Int,
    val category: String = "All activity"
)

/**
 * WinterEarningsScreen - Figma screen "Winter · Earning" (Node 130:42).
 *
 * Rules:
 * 1. Multiples of 4dp for all padding, margins, spacers, and sizes.
 * 2. Broken down into clean, modular composable functions.
 * 3. Dynamic color scheme tokens (WinterTokens) and typography (FunnelSans).
 * 4. Never hardcode arbitrary container heights (flat header with 0 bottom corner radius).
 * 5. Back button uses exact Figma chevron back arrow (R.drawable.ic_back_arrow_chevron).
 */
@Composable
fun WinterEarningsScreen(
    modifier: Modifier = Modifier,
    viewModel: WinterEarningsViewModel = hiltViewModel(),
    showBottomNav: Boolean = false,
    contentBottomPadding: Dp = WinterFloatingNavDefaults.Height + 48.dp,
    onBackClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {},
    onDateFilterClick: () -> Unit = {},
    onNavItemSelected: (WinterNavItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showWithdrawModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadEarnings()
    }

    if (uiState.isLoading && uiState.filteredItems.isEmpty() && uiState.rawAvailableBalance == 0) {
        WinterPageLoadingScreen(
            modifier = modifier,
            contentBottomPadding = if (showBottomNav) WinterFloatingNavDefaults.Height + 48.dp else contentBottomPadding
        )
    } else {
        WinterEarningsScreenContent(
            modifier = modifier,
            availableEarnings = uiState.availableEarnings,
            totalEarnedSummary = uiState.totalEarnedSummary,
            audioCallDuration = uiState.audioCallDuration,
            videoCallDuration = uiState.videoCallDuration,
            earningsList = uiState.filteredItems,
            selectedFilter = uiState.selectedFilter,
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            showBottomNav = showBottomNav,
            contentBottomPadding = contentBottomPadding,
            onBackClick = onBackClick,
            onWithdrawClick = { showWithdrawModal = true },
            onFilterSelected = { viewModel.setFilter(it) },
            onDateFilterClick = onDateFilterClick,
            onNavItemSelected = onNavItemSelected
        )
    }

    if (showWithdrawModal) {
        WinterWithdrawAmountDialog(
            availableEarnings = uiState.rawAvailableBalance.toLong(),
            initialAmount = if (uiState.rawAvailableBalance >= 1000) 1000L else uiState.rawAvailableBalance.toLong(),
            isSubmitting = uiState.isSubmittingWithdrawal,
            errorMessage = if (!uiState.withdrawalSuccess) uiState.withdrawalMessage else null,
            isSuccess = uiState.withdrawalSuccess,
            successMessage = if (uiState.withdrawalSuccess) uiState.withdrawalMessage else null,
            withdrawnAmount = uiState.lastWithdrawnAmount.toLong(),
            onDismissRequest = {
                showWithdrawModal = false
                viewModel.clearWithdrawalMessage()
            },
            onConfirmWithdrawal = { amount ->
                viewModel.submitWithdrawal(amount.toInt())
            }
        )
    }
}

@Composable
fun WinterEarningsScreenContent(
    modifier: Modifier = Modifier,
    availableEarnings: String = "₹0",
    totalEarnedSummary: String = "₹0 earned · ₹0 withdrawn",
    audioCallDuration: String = "0m",
    videoCallDuration: String = "0m",
    earningsList: List<EarningHistoryItem> = emptyList(),
    selectedFilter: String = "All activity",
    isLoading: Boolean = false,
    errorMessage: String? = null,
    showBottomNav: Boolean = false,
    contentBottomPadding: Dp = WinterFloatingNavDefaults.Height + 48.dp,
    onBackClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {},
    onFilterSelected: (String) -> Unit = {},
    onDateFilterClick: () -> Unit = {},
    onNavItemSelected: (WinterNavItem) -> Unit = {}
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    var selectedNavItem by remember { mutableStateOf(WinterNavItem.EARNINGS) }
    val filterTabs = remember { listOf("All activity", "Calls", "Withdrawals") }

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
            // 1. Header: Balance & Withdraw (Node 130:43) - Flat, no bottom rounded corners
            item(key = "earnings_header") {
                EarningsHeaderSection(
                    statusBarPadding = statusBarPadding,
                    availableEarnings = availableEarnings,
                    totalEarnedSummary = totalEarnedSummary,
                    onBackClick = onBackClick,
                    onWithdrawClick = onWithdrawClick
                )
            }

            // 2. Call Time Summary Cards (Node 130:177 - 130:183)
            item(key = "call_time_summary") {
                EarningsCallTimeCardsRow(
                    audioCallDuration = audioCallDuration,
                    videoCallDuration = videoCallDuration
                )
            }

            // 3. Earnings History Header + Filter Tabs (Node 130:190 - 130:200)
            item(key = "earnings_history_header") {
                EarningsHistorySectionHeader(
                    selectedFilter = selectedFilter,
                    filterTabs = filterTabs,
                    onFilterSelected = onFilterSelected,
                    onDateFilterClick = onDateFilterClick
                )
            }

            // 4. Loading State
            if (isLoading && earningsList.isEmpty()) {
                item(key = "earnings_loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        WinterPageLoadingStatus()
                    }
                }
            }

            // 5. Empty State
            if (!isLoading && earningsList.isEmpty()) {
                item(key = "earnings_empty") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (errorMessage != null) errorMessage else "No earnings activity found for $selectedFilter",
                            fontFamily = FunnelSans,
                            fontSize = 14.sp,
                            color = WinterTokens.textSecondary
                        )
                    }
                }
            }

            // 6. Earnings History List Cards (Node 130:205 - 130:237)
            items(
                items = earningsList,
                key = { it.id }
            ) { item ->
                EarningsHistoryItemCard(
                    item = item,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // Floating Bottom Navigation & Home Indicator (Node 130:45 & 130:81)
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
                    role = WinterNavRole.CREATOR,
                    selectedItem = selectedNavItem,
                    onItemSelected = { item ->
                        selectedNavItem = item
                        onNavItemSelected(item)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Home indicator (Node 130:81)
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

@Composable
fun WinterWithdrawalDialog(
    availableBalance: Int,
    isSubmitting: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSubmit: (amount: Int) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val quickAmounts = remember { listOf(100, 500, 1000) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = WinterTokens.surfaceHeader,
            border = androidx.compose.foundation.BorderStroke(1.dp, WinterTokens.borderSubtle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Request Withdrawal",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = WinterTokens.textPrimary
                )

                Text(
                    text = "Available: ₹$availableBalance · Minimum: ₹50",
                    fontFamily = FunnelSans,
                    fontSize = 13.sp,
                    color = WinterTokens.textSecondary
                )

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Amount in ₹", color = WinterTokens.textSecondary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = WinterTokens.textPrimary,
                        unfocusedTextColor = WinterTokens.textPrimary,
                        focusedBorderColor = WinterTokens.borderDefault,
                        unfocusedBorderColor = WinterTokens.borderSubtle
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quickAmounts.forEach { preset ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(WinterTokens.surfaceControl)
                                .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(8.dp))
                                .clickable {
                                    if (preset <= availableBalance) {
                                        amountText = preset.toString()
                                    }
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "₹$preset",
                                fontFamily = FunnelSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (preset <= availableBalance) WinterTokens.textPrimary else WinterTokens.textSecondary
                            )
                        }
                    }
                    // All chip
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(WinterTokens.surfaceControl)
                            .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(8.dp))
                            .clickable {
                                if (availableBalance > 0) {
                                    amountText = availableBalance.toString()
                                }
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "All",
                            fontFamily = FunnelSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = WinterTokens.actionPrimary
                        )
                    }
                }

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        fontFamily = FunnelSans,
                        fontSize = 12.sp,
                        color = WinterTokens.statusError
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cancel",
                        fontFamily = FunnelSans,
                        fontSize = 14.sp,
                        color = WinterTokens.textSecondary,
                        modifier = Modifier
                            .clickable(onClick = onDismiss)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(WinterTokens.actionPrimary)
                            .clickable(enabled = !isSubmitting) {
                                val amt = amountText.toIntOrNull() ?: 0
                                onSubmit(amt)
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSubmitting) {
                            WinterLoadingSpinner(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                trackColor = Color(0x33FFFFFF),
                                spinnerColor = Color.White
                            )
                        } else {
                            Text(
                                text = "Submit Request",
                                fontFamily = FunnelSans,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun EarningsHeaderSection(
    statusBarPadding: Dp,
    availableEarnings: String,
    totalEarnedSummary: String,
    onBackClick: () -> Unit,
    onWithdrawClick: () -> Unit,
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
        EarningsTopBar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(24.dp))

        // Balance & Withdraw Action Row
        EarningsBalanceAndWithdrawRow(
            availableEarnings = availableEarnings,
            onWithdrawClick = onWithdrawClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lifetime Earned vs Withdrawn Summary
        Text(
            text = totalEarnedSummary,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = WinterTokens.textPrimary
        )
    }
}

/**
 * Top bar with 48dp back button and centered "Earning" title (Node 130:104 - 130:108).
 * Uses exact chevron back arrow (R.drawable.ic_back_arrow_chevron) matching Figma.
 */
@Composable
private fun EarningsTopBar(
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
            text = "Earning",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = WinterTokens.textPrimary
        )
    }
}

/**
 * Available earnings balance and "Withdraw now" CTA row (Node 130:110 - 130:122).
 */
@Composable
private fun EarningsBalanceAndWithdrawRow(
    availableEarnings: String,
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Money bag icon + Balance labels
        EarningsBalanceInfo(availableEarnings = availableEarnings)

        // Right: "Withdraw now" button (Node 130:119)
        EarningsWithdrawButton(onClick = onWithdrawClick)
    }
}

/**
 * Balance display with 32dp money bag icon and Available earnings label.
 */
@Composable
private fun EarningsBalanceInfo(
    availableEarnings: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_money_bag_header),
            contentDescription = null,
            tint = WinterTokens.textAccent,
            modifier = Modifier.size(32.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Available earnings",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = WinterTokens.textSecondary
            )

            Text(
                text = availableEarnings,
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
 * Primary "Withdraw now" action button (Node 130:119 - 48dp height, rounded 24dp, #ACE2C6).
 */
@Composable
private fun EarningsWithdrawButton(
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
            text = "Withdraw",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = WinterTokens.textOnAccent
        )
    }
}

/**
 * Dual Call Time Summary Cards (Audio Call Time + Video Call Time, Node 130:177 - 130:183).
 */
@Composable
private fun EarningsCallTimeCardsRow(
    audioCallDuration: String,
    videoCallDuration: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Audio Call Time Card
        EarningsCallTimeCard(
            title = "Audio call",
            duration = audioCallDuration,
            iconRes = R.drawable.ic_audio_phone,
            modifier = Modifier.weight(1f)
        )

        // Video Call Time Card
        EarningsCallTimeCard(
            title = "Video call",
            duration = videoCallDuration,
            iconRes = R.drawable.ic_video_icon,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Single Call Time summary card with icon, title, and bold duration (rounded 24dp, surfaceCard #222222).
 */
@Composable
private fun EarningsCallTimeCard(
    title: String,
    duration: String,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(WinterTokens.surfaceCard)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = WinterTokens.iconSecondary,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = title,
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = WinterTokens.textSecondary
                )
            }

            Text(
                text = duration,
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )
        }
    }
}

/**
 * Earnings history section header with date range filter and tab selector (Node 130:190 - 130:200).
 */
@Composable
private fun EarningsHistorySectionHeader(
    selectedFilter: String,
    filterTabs: List<String>,
    onFilterSelected: (String) -> Unit,
    onDateFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
    ) {
        // Row: "Earnings history" + Date Range Filter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Earnings history",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = WinterTokens.textPrimary
            )

            // Date Range Filter Pill (Node 130:191 - 40dp height, rounded 20dp, #222222)
            EarningsDateFilterPill(onClick = onDateFilterClick)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3 Filter Tabs: "All activity", "Calls", "Withdrawals" (Node 130:200)
        EarningsFilterTabs(
            selectedFilter = selectedFilter,
            filterTabs = filterTabs,
            onFilterSelected = onFilterSelected
        )
    }
}

/**
 * Date Range Filter capsule pill (Node 130:191).
 */
@Composable
private fun EarningsDateFilterPill(
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
            text = "1–10 Nov",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
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
 * Filter tab bar matching Figma Node 130:200 (40dp height container, 32dp active pill).
 */
@Composable
private fun EarningsFilterTabs(
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
 * Individual Earnings History Card matching Figma Node 130:205 (24dp rounded card in surfaceCard #222222).
 */
@Composable
private fun EarningsHistoryItemCard(
    item: EarningHistoryItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(WinterTokens.surfaceCard)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Icon Container (44dp, canvas black background, Node 130:206)
            EarningsTransactionIcon(
                iconRes = item.iconRes,
                isPositive = item.isPositive
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Title & Details Column
            EarningsTransactionDetails(
                title = item.title,
                details = item.details,
                modifier = Modifier.weight(1f)
            )

            // Amount (+amount in accent mint green, -amount in text primary)
            Text(
                text = item.amountFormatted,
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = if (item.isPositive) WinterTokens.textAccent else WinterTokens.textPrimary
            )
        }
    }
}

/**
 * 44dp Circular transaction icon container matching Figma Node 130:206.
 */
@Composable
private fun EarningsTransactionIcon(
    iconRes: Int,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(44.dp)
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
 * Title and details description column.
 */
@Composable
private fun EarningsTransactionDetails(
    title: String,
    details: String,
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
            fontSize = 14.sp,
            color = WinterTokens.textPrimary
        )

        Text(
            text = details,
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = WinterTokens.textSecondary
        )
    }
}

@Preview(name = "Winter · Earning (Node 130:42)", showBackground = true)
@Composable
private fun WinterEarningsScreenPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        WinterEarningsScreenContent(
            availableEarnings = "₹2,460",
            totalEarnedSummary = "₹3,460 earned · ₹1,000 withdrawn",
            audioCallDuration = "2h 40m",
            videoCallDuration = "1h 20m",
            showBottomNav = true
        )
    }
}
