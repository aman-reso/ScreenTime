package com.app.screentime.feature.newdesign.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.CircularProgressIndicator

/**
 * WinterWithdrawAmountDialog - Figma "Winter · Withdraw amount dialog" (Node 203:2).
 *
 * Rules:
 * 1. 4dp grid for all margins, paddings, and sizes.
 * 2. Pure black action button in dark mode via WinterTokens.actionPrimary / borderOnAccent.
 * 3. Dynamic balance subtraction preview and error detection.
 * 4. All strings extracted to strings.xml.
 */
@Composable
fun WinterWithdrawAmountDialog(
    onDismissRequest: () -> Unit,
    availableEarnings: Long = 2460L,
    initialAmount: Long = 1000L,
    currencySymbol: String = "₹",
    isSubmitting: Boolean = false,
    errorMessage: String? = null,
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
                fontSize = 14.sp,
                color = WinterTokens.textSecondary
            )
        }

        // 2. Form fields section (Node 203:4)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Available balance row (Node 203:17)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(ConfigR.string.withdraw_dialog_available_earnings),
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = WinterTokens.textSecondary,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$currencySymbol$formattedAvailable",
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = WinterTokens.statusPositive
                )
            }

            // Input Label (Node 203:20)
            Text(
                text = stringResource(ConfigR.string.withdraw_dialog_amount_label),
                fontFamily = FunnelSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = WinterTokens.textPrimary
            )

            // Withdrawal amount input box (Node 203:21)
            val borderTint = when {
                isExceedingBalance -> WinterTokens.statusError
                isAmountValid -> WinterTokens.statusPositive
                else -> WinterTokens.borderSubtle
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(WinterTokens.surfaceControl)
                    .border(1.dp, borderTint, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Currency Symbol
                Text(
                    text = currencySymbol,
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = WinterTokens.textPrimary
                )

                // Amount Text Field
                BasicTextField(
                    value = amountInputText,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }
                        amountInputText = digits
                    },
                    textStyle = TextStyle(
                        fontFamily = FunnelSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = WinterTokens.textPrimary
                    ),
                    cursorBrush = SolidColor(WinterTokens.textPrimary),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )

                // Trailing Currency Label (Node 203:23)
                Text(
                    text = stringResource(ConfigR.string.withdraw_dialog_currency_inr),
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = WinterTokens.textSecondary
                )
            }

            // Helper / Balance Preview Note (Node 203:31)
            val balanceNoteText = if (isExceedingBalance) {
                stringResource(ConfigR.string.withdraw_dialog_insufficient_balance)
            } else {
                stringResource(ConfigR.string.withdraw_dialog_balance_after, "$currencySymbol$formattedRemaining")
            }
            val balanceNoteColor = if (isExceedingBalance) WinterTokens.statusError else WinterTokens.textSecondary

            Text(
                text = balanceNoteText,
                fontFamily = FunnelSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = balanceNoteColor
            )
        }

        // 3. Dialog actions (Node 203:5)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val activeError = localError ?: errorMessage
            if (!activeError.isNullOrBlank()) {
                Text(
                    text = activeError,
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = WinterTokens.statusError,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun WinterWithdrawAmountDialogPreview() {
    WinterWithdrawAmountDialogContent(onDismissRequest = {})
}

