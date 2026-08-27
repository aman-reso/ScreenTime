package com.app.screentime.feature.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.TransactionType
import com.app.screentime.core.model.WalletTransaction
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*

@Composable
fun BalanceCard(balance: Double, scheme: ODSTheme, onTopUp: () -> Unit) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 20.dp),
        padding = ODSPadding(all = 20.dp)
    ) {
        ODSColumn(gap = 12.dp) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 10.dp
            ) {
                ODSBox(
                    modifier = Modifier.size(36.dp).clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.AccountBalanceWallet),
                        tint = scheme.basicText.getColor()
                    )
                }
                ODSText(
                    text = "Total Balance",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicTextRecessive
                )
            }

            ODSRow(verticalAlignment = Alignment.Bottom, gap = 8.dp) {
                ODSText(
                    text = "✦ ${balance.toInt()}",
                    style = ODSTextStyles.pompiereDisplayL,
                    color = scheme.basicText
                )
                ODSText(
                    text = "coins available",
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive
                )
            }

            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "+ Top Up / Add Coins",
                    variant = ODSButtonVariant.PRIMARY,
                    size = ODSButtonSize.SMALL
                ),
                onClick = onTopUp
            )
        }
    }
}

@Composable
fun MiniStatCard(
    value: String,
    label: String,
    icon: ImageVector,
    iconTint: HexColor,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = 16.dp)
    ) {
        ODSColumn(gap = 6.dp) {
            ODSIcon(iconModel = ODSIconModel(imageVector = icon), tint = iconTint.getColor())
            ODSText(text = value, style = ODSTextStyles.pompiereTitleM, color = scheme.basicText)
            ODSText(text = label, style = ODSTextStyles.microcopyRegular, color = scheme.basicTextRecessive)
        }
    }
}

@Composable
fun TransactionCard(tx: WalletTransaction, scheme: ODSTheme) {
    val isCredit = tx.type == TransactionType.TOPUP || tx.type == TransactionType.BONUS || tx.type == TransactionType.REFUND
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = 14.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            gap = 14.dp
        ) {
            ODSBox(
                modifier = Modifier.size(42.dp).clip(CircleShape),
                background = listOf(
                    ODSColorModel(
                        hexColor = if (isCredit) macawSecondaryScheme.basicBackgroundSubtle else cheddarSecondaryScheme.basicBackgroundSubtle
                    )
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = if (isCredit) Icons.Outlined.Add else
                            if (tx.type == TransactionType.CALL || tx.type == TransactionType.GROUP_CALL) Icons.Outlined.Phone else Icons.Outlined.Chat
                    ),
                    tint = scheme.basicText.getColor()
                )
            }
            ODSColumn(modifier = Modifier.weight(1f)) {
                ODSText(text = tx.description, style = ODSTextStyles.bodySBold, color = scheme.basicText)
                val diff = System.currentTimeMillis() - tx.timestamp
                val timeStr = when {
                    diff < 3_600_000 -> "${diff / 60_000}m ago"
                    diff < 86_400_000 -> "${diff / 3_600_000}h ago"
                    else -> "${diff / 86_400_000}d ago"
                }
                ODSText(text = timeStr, style = ODSTextStyles.microcopyRegular, color = scheme.basicTextRecessive)
            }
            ODSText(
                text = if (isCredit) "+${tx.amount.toInt()}" else "${tx.amount.toInt()}",
                style = ODSTextStyles.bodyMBold,
                color = if (isCredit) scheme.functionalSuccessStandard else scheme.functionalDestructiveStandard
            )
        }
    }
}
