package com.app.screentime.feature.newdesign.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.screentime.config.R as ConfigR
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.WinterTokens
import java.text.NumberFormat
import java.util.Locale

/**
 * WinterWithdrawAmountDialog - Figma "Winter · Withdraw amount dialog" (Node 203:2).
 *
 * Rules:
 * 1. 4dp grid for all margins, paddings, and sizes.
 * 2. Pure black action button in dark mode via WinterTokens.actionPrimary / borderOnAccent.
 * 3. Dynamic balance subtraction preview and error detection.
 * 4. Displays celebration success card with green tick badge upon completion.
 */
@Composable
fun WinterWithdrawAmountDialog(
    onDismissRequest: () -> Unit,
    availableEarnings: Long = 2460L,
    initialAmount: Long = 1000L,
    currencySymbol: String = "₹",
    isSubmitting: Boolean = false,
    errorMessage: String? = null,
    isSuccess: Boolean = false,
    successMessage: String? = null,
    withdrawnAmount: Long = 0L,
    onConfirmWithdrawal: (amount: Long) -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = !isSubmitting,
            dismissOnBackPress = !isSubmitting
        )
    ) {
        WinterWithdrawAmountDialogContent(
            onDismissRequest = onDismissRequest,
            availableEarnings = availableEarnings,
            initialAmount = initialAmount,
            currencySymbol = currencySymbol,
            isSubmitting = isSubmitting,
            errorMessage = errorMessage,
            isSuccess = isSuccess,
            successMessage = successMessage,
            withdrawnAmount = withdrawnAmount,
            onConfirmWithdrawal = onConfirmWithdrawal,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .padding(horizontal = 24.dp)
        )
    }
}

/**
 * Content container for WinterWithdrawAmountDialog, reusable in Dialog or BottomSheet.
 */
@Composable
fun WinterWithdrawAmountDialogContent(
    onDismissRequest: () -> Unit,
    availableEarnings: Long = 2460L,
    initialAmount: Long = 1000L,
    currencySymbol: String = "₹",
    isSubmitting: Boolean = false,
    errorMessage: String? = null,
    isSuccess: Boolean = false,
    successMessage: String? = null,
    withdrawnAmount: Long = 0L,
    onConfirmWithdrawal: (amount: Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var amountInputText by remember { mutableStateOf(if (initialAmount > 0) initialAmount.toString() else "") }
    var localError by remember { mutableStateOf<String?>(null) }

    val parsedAmount = amountInputText.toLongOrNull() ?: 0L
    val isAmountValid = parsedAmount >= 50L && parsedAmount <= availableEarnings
    val isExceedingBalance = parsedAmount > availableEarnings

    val numberFormatter = remember { NumberFormat.getNumberInstance(Locale.getDefault()) }
    val formattedAvailable = remember(availableEarnings) { numberFormatter.format(availableEarnings) }
    val remainingBalance = (availableEarnings - parsedAmount).coerceAtLeast(0L)
    val formattedRemaining = remember(remainingBalance) { numberFormatter.format(remainingBalance) }

    if (isSuccess) {
        val displayAmount = if (withdrawnAmount > 0) withdrawnAmount else parsedAmount
        WithdrawSuccessView(
            modifier = modifier,
            amount = displayAmount,
            currencySymbol = currencySymbol,
            message = successMessage,
            onDone = onDismissRequest
        )
        return
    }

    val handleContinueClick = {
        focusManager.clearFocus()
        val amount = amountInputText.toLongOrNull() ?: 0L
        when {
            availableEarnings <= 0L -> {
                localError = "Available balance is ₹0"
            }
            amount <= 0L -> {
                localError = "Please enter withdrawal amount"
            }
            amount < 50L -> {
                localError = "Minimum withdrawal amount is ₹50"
            }
            amount > availableEarnings -> {
                localError = "Amount exceeds available balance"
            }
            else -> {
                localError = null
                onConfirmWithdrawal(amount)
            }
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(WinterTokens.surfaceCard)
            .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(28.dp))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Heading section (Node 203:3)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title and Dismiss Row (Node 203:12)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(ConfigR.string.withdraw_dialog_title),
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = WinterTokens.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                // 40dp Dismiss Button (Node 203:14)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(WinterTokens.surfaceControl)
                        .border(1.dp, WinterTokens.borderSubtle, CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismissRequest
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x),
                        contentDescription = stringResource(ConfigR.string.close),
                        tint = WinterTokens.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Subtitle (Node 203:16)
            Text(
                text = stringResource(ConfigR.string.withdraw_dialog_subtitle),
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = WinterTokens.textSecondary
            )
        }

        // 2. Available earnings badge (Node 203:17)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(WinterTokens.surfaceControl)
                .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(ConfigR.string.withdraw_dialog_available_earnings),
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = WinterTokens.textSecondary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "$currencySymbol$formattedAvailable",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = WinterTokens.textPrimary
            )
        }

        // 3. Amount input section (Node 203:22)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(ConfigR.string.withdraw_dialog_amount_label),
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = WinterTokens.textSecondary
            )

            // Input card (Node 203:24)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(WinterTokens.surfaceControl)
                    .border(
                        width = 1.dp,
                        color = when {
                            localError != null || errorMessage != null -> WinterTokens.statusError
                            isExceedingBalance -> WinterTokens.statusError
                            else -> WinterTokens.borderSubtle
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = currencySymbol,
                        fontFamily = FunnelSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = if (amountInputText.isNotEmpty()) WinterTokens.textPrimary else WinterTokens.textSecondary
                    )

                    BasicTextField(
                        value = amountInputText,
                        onValueChange = { newText ->
                            val digitsOnly = newText.filter { it.isDigit() }
                            if (digitsOnly.length <= 8) {
                                amountInputText = digitsOnly
                                localError = null
                            }
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = WinterTokens.textPrimary
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(WinterTokens.textAccent),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { handleContinueClick() }
                        ),
                        decorationBox = { innerTextField ->
                            if (amountInputText.isEmpty()) {
                                Text(
                                    text = "0",
                                    fontFamily = FunnelSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = WinterTokens.textSecondary.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    )

                    Text(
                        text = stringResource(ConfigR.string.withdraw_dialog_currency_inr),
                        fontFamily = FunnelSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = WinterTokens.textSecondary
                    )
                }
            }

            // Calculation or Error line (Node 203:30)
            val effectiveError = localError ?: errorMessage
            val calculationText = when {
                effectiveError != null -> effectiveError
                isExceedingBalance -> stringResource(ConfigR.string.withdraw_dialog_insufficient_balance)
                else -> stringResource(ConfigR.string.withdraw_dialog_balance_after, "$currencySymbol$formattedRemaining")
            }

            Text(
                text = calculationText,
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = if (effectiveError != null || isExceedingBalance) WinterTokens.statusError else WinterTokens.textSecondary
            )
        }

        // 4. Action buttons section (Node 203:31)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Quick preset chips for rapid selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(100L, 500L, 1000L).forEach { preset ->
                    val isEnabled = preset <= availableEarnings
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (parsedAmount == preset) WinterTokens.surfaceHeader else WinterTokens.surfaceControl)
                            .border(
                                1.dp,
                                if (parsedAmount == preset) WinterTokens.textAccent else WinterTokens.borderSubtle,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable(enabled = isEnabled) {
                                amountInputText = preset.toString()
                                localError = null
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "₹$preset",
                            fontFamily = FunnelSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (isEnabled) WinterTokens.textPrimary else WinterTokens.textSecondary.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            // Continue primary button (Node 203:32)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(WinterTokens.actionPrimary)
                    .border(1.dp, WinterTokens.borderOnAccent, RoundedCornerShape(26.dp))
                    .clickable(
                        enabled = !isSubmitting,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = handleContinueClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = WinterTokens.textOnAccent,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(ConfigR.string.withdraw_dialog_continue),
                        fontFamily = FunnelSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = WinterTokens.textOnAccent
                    )
                }
            }

            // Cancel secondary button (Node 203:35)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismissRequest
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(ConfigR.string.withdraw_dialog_cancel),
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = WinterTokens.textSecondary
                )
            }
        }
    }
}

/**
 * Celebration success card with Green Tick, transferred amount, and Done CTA.
 */
@Composable
private fun WithdrawSuccessView(
    modifier: Modifier = Modifier,
    amount: Long,
    currencySymbol: String = "₹",
    message: String?,
    onDone: () -> Unit
) {
    val numberFormatter = remember { NumberFormat.getNumberInstance(Locale.getDefault()) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(WinterTokens.surfaceCard)
            .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(28.dp))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Green Check Badge with Glow Circle
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFF132E22))
                .border(2.dp, Color(0xFF34D399), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "Success",
                tint = Color(0xFF34D399),
                modifier = Modifier.size(32.dp)
            )
        }

        // Title
        Text(
            text = "Withdrawal Submitted!",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = WinterTokens.textPrimary,
            textAlign = TextAlign.Center
        )

        // Amount Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(WinterTokens.surfaceControl)
                .border(1.dp, WinterTokens.borderSubtle, RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$currencySymbol${numberFormatter.format(amount)}",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color(0xFF34D399)
                )
                Text(
                    text = "Transfer to UPI initiated",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = WinterTokens.textSecondary
                )
            }
        }

        // Subtitle message
        Text(
            text = message?.ifBlank { "Your withdrawal request has been received. Funds will reflect in your bank account shortly." }
                ?: "Your withdrawal request has been received. Funds will reflect in your bank account shortly.",
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = WinterTokens.textSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Done button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(WinterTokens.actionPrimary)
                .border(1.dp, WinterTokens.borderOnAccent, RoundedCornerShape(26.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDone
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Done",
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = WinterTokens.textOnAccent
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun WinterWithdrawAmountDialogPreview() {
    WinterWithdrawAmountDialogContent(onDismissRequest = {})
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun WinterWithdrawSuccessPreview() {
    WithdrawSuccessView(
        amount = 1000L,
        message = "Withdrawal request submitted successfully",
        onDone = {}
    )
}
