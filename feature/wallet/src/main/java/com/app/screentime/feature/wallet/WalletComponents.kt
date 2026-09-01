package com.app.screentime.feature.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.TransactionType
import com.app.screentime.core.model.WalletTransaction
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Stacked Hero Balance Card (Cyber Lime Card + White Pill Button + VIP Badge)
 */
@Composable
fun HeroBalanceCard(
    balance: Double,
    scheme: ODSTheme,
    isModel: Boolean = false,
    onTopUp: () -> Unit
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth(),
        ) {}
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE5FF9E), // Radiant Cyber Lime
                            Color(0xFFD7FF81), // Primary Cyber Lime
                            Color(0xFFC4F26B)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 12.dp
            ) {
                ODSBox(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22000000))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    ODSText(
                        text = if (isModel) "EARNINGS (INR)" else "COINS (USD)",
                        style = ODSTextStyles.microcopyBold,
                        color = HexColor(0xff1e1145)
                    )
                }

                // Huge Main Balance Number (Image 1 style: e.g. 194,284)
                ODSColumn(gap = 2.dp) {
                    val formatted = String.format("%,d", balance.toInt())
                    ODSText(
                        text = formatted,
                        style = ODSTextStyles.titleL,
                        color = HexColor(0xff1e1145)
                    )
                    ODSText(
                        text = if (isModel) "₹${(balance * 0.8).toInt()} withdrawable · +12.4% this week" else "+₹${(balance * 0.1).toInt()} · +1.6% this week",
                        style = ODSTextStyles.microcopyBold,
                        color = HexColor(0xaa1e1145)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // White Pill Button (Image 2 style)
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSBox(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color.White)
                            .clickable(onClick = onTopUp)
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = if (isModel) "Withdraw Funds" else "+ Top Up Coins",
                            style = ODSTextStyles.bodySBold,
                            color = HexColor(0xff1e1145)
                        )
                    }

                    // Sparkle Icon (Image 2 style)
                    ODSText(
                        text = "✦",
                        style = ODSTextStyles.titleL,
                        color = HexColor(0xff7038db)
                    )
                }
            }
        }
    }
}

/**
 * Transaction Item Card with direction badges (Image 1 style)
 */
@Composable
fun TransactionCard(
    tx: WalletTransaction,
    scheme: ODSTheme,
    isHighlighted: Boolean = false
) {
    val isCredit =
        tx.type == TransactionType.TOPUP || tx.type == TransactionType.BONUS || tx.type == TransactionType.REFUND
    val bgHex = if (isHighlighted) HexColor(0xff4a2a7a) else scheme.basicBackgroundCard

    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = bgHex)),
        cornerRadius = ODSCorners(all = 20.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isHighlighted) scheme.basicAccentSecondary else scheme.basicStrokeSubtle
                )
            )
        ),
        padding = ODSPadding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 14.dp
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = when (tx.type) {
                            TransactionType.TOPUP -> com.telekom.odsystem.R.drawable.amount_come
                            TransactionType.CALL, TransactionType.GROUP_CALL -> com.telekom.odsystem.R.drawable.call
                            TransactionType.CHAT -> com.telekom.odsystem.R.drawable.message
                            else -> com.telekom.odsystem.R.drawable.amount_come
                        }
                    ),
                    tint = if (isCredit) scheme.basicAccent.getColor() else scheme.basicAccentSecondary.getColor()
                )
                ODSColumn(gap = 2.dp) {
                    ODSText(
                        text = tx.description.ifBlank { "Coin Transaction" },
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val diff = System.currentTimeMillis() - tx.timestamp
                    val timeStr = when {
                        diff < 3_600_000 -> "${(diff / 60_000).coerceAtLeast(1)}m ago"
                        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
                        else -> "${diff / 86_400_000}d ago"
                    }
                    ODSText(
                        text = when (tx.type) {
                            TransactionType.CALL -> "Voice Call · $timeStr"
                            TransactionType.TOPUP -> "UPI / Card · $timeStr"
                            TransactionType.BONUS -> "ScreenTime Reward · $timeStr"
                            else -> "Transfer · $timeStr"
                        },
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                    ODSText(
                        text = if (isCredit) "+${tx.amount.toInt()} Coins" else "-${tx.amount.toInt()} Coins",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                }
            }
        }
    }
}
